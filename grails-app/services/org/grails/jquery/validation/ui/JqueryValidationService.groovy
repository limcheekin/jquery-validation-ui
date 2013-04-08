/* Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.jquery.validation.ui

import org.codehaus.groovy.grails.validation.ConstrainedPropertyBuilder
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import grails.util.GrailsNameUtils
import grails.validation.ValidationErrors
import org.springframework.web.context.request.RequestContextHolder
import net.zorched.grails.plugins.validation.CustomConstraintFactory

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class JqueryValidationService {
	
	static final String TYPE_MISMATCH_MESSAGE_PREFIX = "typeMismatch."
	static final Integer VALIDATION_RULE_LENGTH = 15
	static final Integer VALIDATION_MESSAGE_LENGTH = 30
	static final ERROR_CODE_SUFFIXES = [
		"error",
		"invalid"
	]   
	static final String VALUE_PLACEHOLDER = "[[[JqueryValidationUIValuePlaceholder]]]"
	static final DEFAULT_ERROR_MESSAGE_CODES_MAP = [
		matches: "default.doesnt.match.message",
		url: "default.invalid.url.message",
		creditCard: "default.invalid.creditCard.message",
		email: "default.invalid.email.message",
		range: "default.invalid.range.message",
		size: "default.invalid.size.message",
		max: "default.invalid.max.message",
		min: "default.invalid.min.message",
		maxSize: "default.invalid.max.size.message",
		minSize: "default.invalid.min.size.message",
		inList: "default.not.inlist.message",
		blank: "default.blank.message",
		notEqual: "default.not.equal.message",
		nullable: "default.null.message",
		validator: "default.invalid.validator.message",
		unique: "default.not.unique.message"
	]

	static final GRAILS_CONSTRAINT_FAILURE_CODES_MAP = [
		blank:'blank',
		creditCard:'creditCard.invalid',
		email:'email.invalid',
		inList:'not.inList',
		matches:'matches.invalid',
		max:'max.exceeded',
		maxSize:'maxSize.exceeded',
		min:'min.notmet',
		minSize:'minSize.notmet',
		notEqual:'notEqual',
		nullable:'nullable',
		range:null,
		size:null,
		unique:'unique',
		url:'url.invalid',
		validator:'validator.invalid'
	]

	static transactional = false
	def grailsApplication
	def messageSource
	
	Map getConstrainedProperties(Class validatableClass) {
		def constrainedProperties
		if (!validatableClass.constraints) {
			throw new NullPointerException("validatableClass.constraints is null!") 
		}
		if (validatableClass.constraints instanceof Closure) {
			def validationClosure = validatableClass.constraints
			def constrainedPropertyBuilder = new ConstrainedPropertyBuilder(validatableClass.newInstance())
			validationClosure.setDelegate(constrainedPropertyBuilder)
			validationClosure()
			constrainedProperties = constrainedPropertyBuilder.constrainedProperties
		} else {
			constrainedProperties = validatableClass.constraints
		}
		return constrainedProperties
	}
	
	String createJavaScriptConstraints(List constrainedPropertiesEntries, Locale locale) {
		FastStringWriter javaScriptConstraints = new FastStringWriter(VALIDATION_RULE_LENGTH * constrainedPropertiesEntries.size())
		String namespacedPropertyName
		def constrainedPropertyValues

		constrainedPropertiesEntries.eachWithIndex { constrainedPropertiesEntry, entryIndex ->
			constrainedPropertyValues = constrainedPropertiesEntry.constrainedProperties.values()
			constrainedPropertyValues.eachWithIndex { constrainedProperty, propertyIndex  ->
				namespacedPropertyName = constrainedPropertiesEntry.namespace?"'${constrainedPropertiesEntry.namespace}.${constrainedProperty.propertyName}'":constrainedProperty.propertyName
				javaScriptConstraints << "${namespacedPropertyName}: "
				javaScriptConstraints << _createJavaScriptConstraints(constrainedProperty, locale, constrainedPropertiesEntry.namespace, false)
       if (entryIndex == constrainedPropertiesEntries.size() - 1 && 
		       propertyIndex == constrainedPropertyValues.size() - 1) {
				  javaScriptConstraints << "\n" 
				} else {
				  javaScriptConstraints << ",\n"
				}
			}
		}
		return javaScriptConstraints.toString()
	}
	
	String createJavaScriptMessages(List constrainedPropertiesEntries, Locale locale) {
		FastStringWriter javaScriptMessages = new FastStringWriter(VALIDATION_MESSAGE_LENGTH * constrainedPropertiesEntries.size())
		String namespacedPropertyName
		def constrainedPropertyValues

		constrainedPropertiesEntries.eachWithIndex { constrainedPropertiesEntry, entryIndex ->
			constrainedPropertyValues = constrainedPropertiesEntry.constrainedProperties.values()
			constrainedPropertyValues.eachWithIndex { constrainedProperty, propertyIndex  ->
				namespacedPropertyName = constrainedPropertiesEntry.namespace?"'${constrainedPropertiesEntry.namespace}.${constrainedProperty.propertyName}'":constrainedProperty.propertyName
				javaScriptMessages << "${namespacedPropertyName}: "
				javaScriptMessages << _createJavaScriptMessages(constrainedProperty, locale, constrainedPropertiesEntry.namespace)
       if (entryIndex == constrainedPropertiesEntries.size() - 1 && 
		       propertyIndex == constrainedPropertyValues.size() - 1) {
				  javaScriptMessages << "\n" 
				} else {
				  javaScriptMessages << ",\n"
				}
			}
		}
		return javaScriptMessages.toString()
	}
	
	String getValidationMetadatas(DefaultGrailsDomainClass domainClass, String[] properties, Locale locale) {
		def constrainedProperties = _getConstrainedProperties(domainClass, properties)
		String namespace
		Integer dotIndex
		Integer propertiesSize = properties.size()
		FastStringWriter validationMetadatas = new FastStringWriter(propertiesSize * (VALIDATION_RULE_LENGTH + VALIDATION_MESSAGE_LENGTH))
		validationMetadatas << "{ "
		properties.eachWithIndex { p, i ->
			dotIndex = p.indexOf('.')
			namespace = dotIndex > -1 ? p.substring(0, dotIndex) : null
			// println "$i) \"$p\": \"${_createJavaScriptConstraints(constrainedProperties[p], locale, namespace, true) }\""
			validationMetadatas << "\"$p\": \"${_createJavaScriptConstraints(constrainedProperties[p], locale, namespace, true) }\""
			if (i < propertiesSize - 1) {
				validationMetadatas << ", "
			} else {
				validationMetadatas << " "
			}
		}
		validationMetadatas << " };"
		println "validationMetadatas.toString() = ${validationMetadatas.toString()}"
		return validationMetadatas.toString();
	}
	
	private Map getConstraintsMap(Class propertyType) {
		def constraintsMap
		if (propertyType == String) {
			constraintsMap = grailsApplication.config.jqueryValidationUi.StringConstraintsMap
		} else if (propertyType == Date) {
			constraintsMap = grailsApplication.config.jqueryValidationUi.DateConstraintsMap
		} else if (propertyType.superclass == Number) {
			constraintsMap = grailsApplication.config.jqueryValidationUi.NumberConstraintsMap
		} else if (propertyType.interfaces.find { it == Collection }) {
			constraintsMap = grailsApplication.config.jqueryValidationUi.CollectionConstraintsMap
		} else {
			constraintsMap = grailsApplication.config.jqueryValidationUi.ObjectConstraintsMap
		}
		return constraintsMap
	}
	
	private List getConstraints(def constrainedProperty) {
		def constraints = []
		constraints.addAll(constrainedProperty.appliedConstraints)
		if (constraints.find{it.name == "blank"} && constraints.find {it.name == "nullable"}) {
			constraints.removeAll {it.name == "nullable"} // blank constraint take precedence
		}
		return constraints
	}        
	
	private String createRemoteJavaScriptConstraints(String contextPath, String constraintName, String validatableClassName, String propertyName) {
		String remoteJavaScriptConstraints = "\t${constraintName.equals('unique') || constraintName.equals('validator')?constraintName:'custom'}: {\n" +
				"\turl: '${contextPath}/JQueryRemoteValidator/validate',\n" +
				"\ttype: 'post',\n" +
				"\tdata: {\n" +
				"\t\tvalidatableClass: '${validatableClassName}',\n" +
				"\t\tproperty: '${propertyName}'"
		
		if (!constraintName.equals('unique') && !constraintName.equals('validator')) {
			remoteJavaScriptConstraints += ",\n\t\tconstraint: '${constraintName}'"
		}
		
		if (constraintName.equals('unique')) {
			remoteJavaScriptConstraints += ",\n\t\tid: \$('input:hidden#id').length ? \$('input:hidden#id').val() : '0'"
		}
		
		remoteJavaScriptConstraints += "\n\t}\n\t}"
		
		return remoteJavaScriptConstraints
	}
	
	private String getTypeMismatchMessage(Class validatableClass, Class propertyType, String propertyNamespace, String propertyName, Locale locale) {
		def code
		def defaultMessage = "Error message for ${code} undefined."
		def message
		
		if (propertyNamespace) {
			code = "${TYPE_MISMATCH_MESSAGE_PREFIX}${propertyNamespace}.${propertyName}"
		} else {
			code = "${TYPE_MISMATCH_MESSAGE_PREFIX}${validatableClass.name}.${propertyName}"
		}
		message = messageSource.getMessage(code, null, null, locale)
		
		if (!message) {
			code = "${TYPE_MISMATCH_MESSAGE_PREFIX}${propertyName}"
			message = messageSource.getMessage(code, null, null, locale)
		}
		
		if (!message) {
			code = "${TYPE_MISMATCH_MESSAGE_PREFIX}${propertyType.name}"
			message = messageSource.getMessage(code, [propertyName].toArray(), defaultMessage, locale)
		}
		
		return message.encodeAsJavaScript()
	}
	
	private String getMessage(def constraint, Class validatableClass, def args, Locale locale) {
		def argArray = args == null ? null : args.toArray()
		String defaultMessageCode
		String message
		// The preferred way to do things is to create an object to validate and pretend that a validation error occurred
		// so that we simulate as closely as possible all of the error codes that grails generates on the server side.
		// Another approach would be to copy the code from AbstractConstraint that does all the work of generating all of
		// the message code possibilities and sticks them into the Errors object here.  That would allow us to eliminate the
		// else case below.  It would come at the cost of having to make sure it was kept in sync with the grails source from
		// which is was copied.  It would be nice if grails would refactor that bit of code into a utility class that we 
		// could call...
		if (!grailsApplication.config.jqueryValidationUi.useLegacyMessageCodes && constraint.respondsTo('rejectValue')) {
			String failureCode
			// Handle custom constraints correctly by getting the default message code, default message and the
			// failure code to use.  It would be nice if the standard grails constraints also worked this way, but
			// unfortunately we have to just hard code in the values for those constraints based on the grails
			// documentation...
			if (constraint instanceof CustomConstraintFactory.CustomConstraintClass) {
				defaultMessageCode = constraint.constraint.getDefaultMessageCode()
				failureCode = constraint.constraint.getFailureCode()
			} else {
				failureCode = GRAILS_CONSTRAINT_FAILURE_CODES_MAP[constraint.name]
			}
			// Fake a call to reject the constraint which should result in our validationErrors object having a single 
			// FieldError in it which will contain a list of the standard grails message codes for validation failure
			// in the correct search order...
			def targetObject = validatableClass.newInstance()
			def validationErrors = new ValidationErrors(targetObject, constraint.propertyName)
			// Casts to string needed here to avoid ambiguous method overloading exceptions since sometimes the values of
			// defaultMessageCode and failureCode are null...
			constraint.rejectValue(targetObject, validationErrors, (String)defaultMessageCode, (String)failureCode, argArray)
			message = validationErrors.fieldErrors[0].codes.findResult {messageSource.getMessage(it, argArray, null, locale)}
		} else {
			// This is not the common case, but could happen if someone implemented Constraint without subclassing
			// AbstractConstraint. This is a rather inaccurate attempt at trying to find a message from a series of message code
			// possibilities. It is inaccurate because the name of the constraint often can't be generated by tacking on '.error'
			// or '.invalid' to the end of ${classname}.${constraint.propertyName}.${constraint.name}.  Also, for custom constraints, the user
			// can define whatever message code they want, so we're not respecting that at all here.  Finally, for custom
			// constraints, the default message code and default message can be defined, but there is no attempt to even try to
			// find a default value for a custom constraint here...
			def code = "${validatableClass.name}.${constraint.propertyName}.${constraint.name}"
			message = messageSource.getMessage(code, args == null ? null : args.toArray(), null, locale)

			ERROR_CODE_SUFFIXES.each { errorSuffix ->
				message = message?:messageSource.getMessage("${code}.${errorSuffix}", argArray, null, locale)
			}
			if (!message) {
				code = "${GrailsNameUtils.getPropertyName(validatableClass)}.${constraint.propertyName}.${constraint.name}"
				message = messageSource.getMessage(code, argArray, null, locale)
			}
		
			ERROR_CODE_SUFFIXES.each { errorSuffix ->
				message = message?:messageSource.getMessage("${code}.${errorSuffix}", argArray, null, locale)
			}
		}
		if (!message) {
			String defaultMessage = "Property [{0}] of class [{1}] with value [{2}] is not valid"
			defaultMessageCode = defaultMessageCode ?: DEFAULT_ERROR_MESSAGE_CODES_MAP[constraint.name]
			message = messageSource.getMessage(defaultMessageCode, argArray, defaultMessage, locale)
		}
		return message.encodeAsJavaScript()
	}        
	
	private String _createJavaScriptConstraints(def constrainedProperty, Locale locale, String namespace, boolean forMetadata) {
		FastStringWriter javaScriptConstraints = new FastStringWriter(forMetadata ? VALIDATION_RULE_LENGTH : VALIDATION_RULE_LENGTH + VALIDATION_MESSAGE_LENGTH)
		def constraintsMap
		String javaScriptConstraint
		String javaScriptConstraintCode
		
		javaScriptConstraints << "{ "
		constraintsMap = getConstraintsMap(constrainedProperty.propertyType)
		def constraints = getConstraints(constrainedProperty)
		
		switch (constrainedProperty.propertyType) {
			case Date:
				javaScriptConstraintCode = "date: true"
				break
			case Long:
			case Integer:
			case Short:
			case BigInteger:
				javaScriptConstraintCode = "digits: true"
				break
			case Float:
			case Double:
			case BigDecimal:
				javaScriptConstraintCode = "${(locale.country == 'br' || locale.country == 'de')?'numberDE':'number'}: true"
				break
		}
		
		if (javaScriptConstraintCode) {
			javaScriptConstraints << javaScriptConstraintCode
			if (constraints.size() > 0) {
				javaScriptConstraints << ", "
			} else {
				javaScriptConstraints << " "
			}
		}
		constraints.eachWithIndex { constraint, i ->
			javaScriptConstraint = constraintsMap[constraint.name]
			javaScriptConstraintCode = null
			if (javaScriptConstraint) {
				switch (constraint.name) {
					case "nullable":
						if (!constrainedProperty.isNullable()) {
							javaScriptConstraintCode = "${javaScriptConstraint}: true"
						}
						break
					case "blank":
						if (!constrainedProperty.isBlank()) {
							javaScriptConstraintCode = "${javaScriptConstraint}: true"
						}
						break
					case "creditCard":
						if (constrainedProperty.isCreditCard()) {
							javaScriptConstraintCode = "${javaScriptConstraint}: true"
						}
						break
					case "email":
						if (constrainedProperty.isEmail()) {
							javaScriptConstraintCode = "${javaScriptConstraint}: true"
						}
						break
					case "url":
						if (constrainedProperty.isUrl()) {
							javaScriptConstraintCode = "${javaScriptConstraint}: true"
						}
						break
					case "inList":
						javaScriptConstraintCode = "${javaScriptConstraint}: ["
						if (constrainedProperty.propertyType == Date) {
							constrainedProperty.inList.eachWithIndex { val, listIndex ->
								javaScriptConstraintCode += "new Date(${val.time})"
								javaScriptConstraintCode += listIndex < constrainedProperty.inList.size() - 1 ? ", " : ""
							}
						} else {
							constrainedProperty.inList.eachWithIndex { val, listIndex ->
								javaScriptConstraintCode += "'${val}'"
								javaScriptConstraintCode += listIndex < constrainedProperty.inList.size() - 1 ? ", " : ""
							}
						}
						javaScriptConstraintCode += "]"
						break
					case "matches":
						javaScriptConstraintCode = "${javaScriptConstraint}: '${constrainedProperty.matches.replaceAll('\\\\', '\\\\\\\\')}'"
						break
					case "max":
						javaScriptConstraintCode = "${javaScriptConstraint}: ${constrainedProperty.propertyType == Date ? "new Date(${constrainedProperty.max.time})" : constrainedProperty.max}"
					break
				case "maxSize":
					javaScriptConstraintCode = "${javaScriptConstraint}: ${constrainedProperty.maxSize}"
					break
				case "min":
					javaScriptConstraintCode = "${javaScriptConstraint}: ${constrainedProperty.propertyType == Date ? "new Date(${constrainedProperty.min.time})" : constrainedProperty.min}"
				break
			case "minSize":
				javaScriptConstraintCode = "${javaScriptConstraint}: ${constrainedProperty.minSize}"
				break
			case "notEqual":
				javaScriptConstraintCode = "${javaScriptConstraint}: ${constrainedProperty.propertyType == Date ? "new Date(${constrainedProperty.notEqual.time})" : "'${constrainedProperty.notEqual}'"}"
			break
		case "range":
			def range = constrainedProperty.range
			if (constrainedProperty.propertyType == Date) {
				javaScriptConstraintCode = "${javaScriptConstraint}: [new Date(${range.from.time}), new Date(${range.to.time})]"
			} else {
				javaScriptConstraintCode = "${javaScriptConstraint}: [${range.from}, ${range.to}]"
			}
			break
		case "size":
			def size = constrainedProperty.size
			javaScriptConstraintCode = "${javaScriptConstraint}: [${size.from}, ${size.to}]"
			break
		case "unique":
		case "validator":
			javaScriptConstraintCode = createRemoteJavaScriptConstraints(RequestContextHolder.requestAttributes.contextPath, constraint.name, constrainedProperty.owningClass.name, constrainedProperty.propertyName)
			break
		default:
			// custom constraint...
			def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
			if (customConstraintsMap && customConstraintsMap[constraint.name]) {
				javaScriptConstraintCode = "${javaScriptConstraint}: ${customConstraintsMap[constraint.name]}"
			} else {
				log.error "Failed to generate javascript validation rule for constraint '${constraint.name}' " +
					"with javascript constraint '${javaScriptConstraint}':  missing custom constraints map " +
					"entry. Ignoring this constraint and moving on."
			}
			break
	}
} else {
	// Old way of generating the custom constraint javascript rule is to assume the javascript constraint validation method is
	// the same name as the grails constraint. The preferred way to do things now is to create a map entry in the appropriate
	// map (string, number, date, etc.) for the custom constraint as is done for the built-in grails constraints...
	def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
	if (customConstraintsMap && customConstraintsMap[constraint.name]) {
		javaScriptConstraintCode = "${constraint.name}: ${customConstraintsMap[constraint.name]}"
	} else {
		log.info "${constraint.name} constraint not found even in the CustomConstraintsMap, use custom constraint and remote validation"
		javaScriptConstraintCode = createRemoteJavaScriptConstraints(RequestContextHolder.requestAttributes.contextPath, constraintName, constrainedProperty.owningClass.name, constrainedProperty.propertyName)
	}
}
if (javaScriptConstraintCode) {
	javaScriptConstraints << javaScriptConstraintCode
	if (i < constraints.size() - 1) {
		javaScriptConstraints << ", "
	} else {
		javaScriptConstraints << " "
	}
}
}

if (forMetadata) {
	javaScriptConstraints << ", messages: ${_createJavaScriptMessages(constrainedProperty, locale, namespace)}"
}

javaScriptConstraints << "}"

return javaScriptConstraints.toString()
}

private String _createJavaScriptMessages(def constrainedProperty, Locale locale, String namespace) {
def args = []
FastStringWriter javaScriptMessages = new FastStringWriter(VALIDATION_MESSAGE_LENGTH)
String javaScriptConstraint
def constraints
String javaScriptMessageCode


def constraintsMap = getConstraintsMap(constrainedProperty.propertyType)
javaScriptMessages << "{ "
constraints = getConstraints(constrainedProperty)
javaScriptMessageCode = null
switch (constrainedProperty.propertyType) {
case Date:
	javaScriptMessageCode = "date: '${getTypeMismatchMessage(constrainedProperty.owningClass, constrainedProperty.propertyType, namespace, constrainedProperty.propertyName, locale)}'"
	break
case Long:
case Integer:
case Short:
case BigInteger:
	javaScriptMessageCode = "digits: '${getTypeMismatchMessage(constrainedProperty.owningClass, constrainedProperty.propertyType, namespace, constrainedProperty.propertyName, locale)}'"
	break
case Float:
case Double:
case BigDecimal:
	if (locale.country == 'br' || locale.country == 'de')
		javaScriptMessageCode = "numberDE: '${getTypeMismatchMessage(constrainedProperty.owningClass, constrainedProperty.propertyType, namespace, constrainedProperty.propertyName, locale)}'"
	else
		javaScriptMessageCode = "number: '${getTypeMismatchMessage(constrainedProperty.owningClass, constrainedProperty.propertyType, namespace, constrainedProperty.propertyName, locale)}'"
	break
}

if (javaScriptMessageCode) {
javaScriptMessages << javaScriptMessageCode
if (constraints.size() > 0) {
	javaScriptMessages << ", "
} else {
	javaScriptMessages << " "
}
}

constraints.eachWithIndex { constraint, i ->
javaScriptConstraint = constraintsMap[constraint.name]
javaScriptMessageCode = null
args.clear()
args = [constrainedProperty.propertyName, constrainedProperty.owningClass]
if (javaScriptConstraint) {
	switch (constraint.name) {
		case "nullable":
			if (!constrainedProperty.isNullable()) {
				javaScriptMessageCode = "${javaScriptConstraint}: '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'"
			}
		case "blank":
			if (!constrainedProperty.isBlank()) {
				javaScriptMessageCode = "${javaScriptConstraint}: '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'"
			}
			break
		case "creditCard":
		case "email":
		case "url":
			if (constrainedProperty.isCreditCard() || constrainedProperty.isEmail() || constrainedProperty.isUrl()) {
				args << VALUE_PLACEHOLDER
				javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'; }".replace(
					VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			}
			break
		case "inList":
		case "matches":
		case "max":
		case "maxSize":
		case "min":
		case "minSize":
		case "notEqual":
			args << VALUE_PLACEHOLDER
			args << constrainedProperty."${constraint.name}"
			javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'; }".replace(
				VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			break
		
		case "range":
		case "size":
			args << VALUE_PLACEHOLDER
			def range = constrainedProperty."${constraint.name}"
			args << range.from
			args << range.to
			javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'; }".replace(
				VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			break
		
		case "unique":
		case "validator":
			args << VALUE_PLACEHOLDER
			javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'; }".replace(
				VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			break
		default:
			// custom constraint...
			def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
			if (customConstraintsMap && customConstraintsMap[constraint.name]) {
				args << VALUE_PLACEHOLDER
				javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'; }".replace(
					VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			}
			break
	}
} else {
	// Old way of generating the custom constraint javascript message is to assume the javascript constraint validation method is
	// the same name as the grails constraint. The preferred way to do things now is to create a map entry in the appropriate
	// map (string, number, date, etc.) for the custom constraint as is done for the built-in grails constraints...
	def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
	if (customConstraintsMap && customConstraintsMap[constraint.name]) {
		args << VALUE_PLACEHOLDER
		javaScriptMessageCode = "${constraint.name}: function() { return '${getMessage(constraint, constrainedProperty.owningClass, args, locale)}'; }".replace(
			VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
	} // else remote validation, using remote message.
}
if (javaScriptMessageCode) {
	javaScriptMessages << javaScriptMessageCode
	if (i < constraints.size() - 1) {
		javaScriptMessages << ", "
	} else {
		javaScriptMessages << " "
	}
}
}
javaScriptMessages << "}"

return javaScriptMessages.toString()
}

private _getConstrainedProperties(DefaultGrailsDomainClass domainClass, String[] properties) {
	def constrainedProperties = domainClass.constrainedProperties.findAll{ k, v -> properties.contains(k) }
	def childConstrainedProperties
	def dotProperties = properties.findAll { it.indexOf('.') > -1 } // nested/embedded class
	def dotPropertiesValues = dotProperties.collect {  it.substring(0, it.indexOf('.')) }.unique()
	def dotConstrainedProperties = domainClass.constrainedProperties.findAll{ k, v -> dotPropertiesValues.contains(k) }
	dotConstrainedProperties.each { propertyName, constrainedProperty ->
	childConstrainedProperties = getConstrainedProperties(constrainedProperty.propertyType).findAll{ k, v -> dotProperties.contains("$propertyName.$k" as String) } // contains() only work after converted to String
		childConstrainedProperties.each { k, v ->
			constrainedProperties["$propertyName.$k" as String] = v
		}
	}
	
	/*constrainedProperties.each { k, v ->
	 println "* $k = $v"
	 }*/
	
	return constrainedProperties
}

}
