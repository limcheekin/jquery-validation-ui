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
import org.springframework.web.context.request.RequestContextHolder

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
	
	private List getConstraintNames(def constrainedProperty) {
		def constraintNames = constrainedProperty.appliedConstraints.collect { return it.name }
		if (constraintNames.contains("blank") && constraintNames.contains("nullable")) {
			constraintNames.remove("nullable") // blank constraint take precedence
		}
		return constraintNames
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
			def conf = grailsApplication.config.jqueryValidationUi.flatten()
			String idSelector = conf.get("${GrailsNameUtils.getPropertyName(validatableClassName)}.${propertyName}.unique.idSelector" as String)
			idSelector = idSelector?:conf.get("${propertyName}.unique.idSelector" as String)
			if (idSelector) {
				remoteJavaScriptConstraints += ",\n\t\tid: function() { return \$('$idSelector').length ? \$('$idSelector').val() : '0'; }"
			} else {
				remoteJavaScriptConstraints += ",\n\t\tid: function() { return \$('input:hidden#id', myForm).length ? \$('input:hidden#id', myForm).val() : '0'; }"
			} 
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
	
	private String getMessage(Class validatableClass, String propertyName, def args, String constraintName, Locale locale) {
		def code = "${validatableClass.name}.${propertyName}.${constraintName}"
		def defaultMessage = "Error message for ${code} undefined."
		def message = messageSource.getMessage(code, args == null ? null : args.toArray(), null, locale)
		
		ERROR_CODE_SUFFIXES.each { errorSuffix ->
			message = message?:messageSource.getMessage("${code}.${errorSuffix}", args == null ? null : args.toArray(), null, locale)
		}
		if (!message) {
			code = "${GrailsNameUtils.getPropertyName(validatableClass)}.${propertyName}.${constraintName}"
			message = messageSource.getMessage(code, args == null ? null : args.toArray(), null, locale)
		}
		
		ERROR_CODE_SUFFIXES.each { errorSuffix ->
			message = message?:messageSource.getMessage("${code}.${errorSuffix}", args == null ? null : args.toArray(), null, locale)
		}
		if (!message) {
			code = DEFAULT_ERROR_MESSAGE_CODES_MAP[constraintName]
			message = messageSource.getMessage(code, args == null ? null : args.toArray(), defaultMessage, locale)
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
		def constraintNames = getConstraintNames(constrainedProperty)
		
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
			if (constraintNames.size() > 0) {
				javaScriptConstraints << ", "
			} else {
				javaScriptConstraints << " "
			}
		}
		constraintNames.eachWithIndex { constraintName, i ->
			javaScriptConstraint = constraintsMap[constraintName]
			javaScriptConstraintCode = null
			if (javaScriptConstraint) {
				switch (constraintName) {
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
			javaScriptConstraintCode = createRemoteJavaScriptConstraints(RequestContextHolder.requestAttributes.contextPath, constraintName, constrainedProperty.owningClass.name, constrainedProperty.propertyName)
			break
	}
} else {
	def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
	if (customConstraintsMap && customConstraintsMap[constraintName]) {
		javaScriptConstraintCode = "$constraintName: ${customConstraintsMap[constraintName]}"
	} else {
		log.info "${constraintName} constraint not found even in the CustomConstraintsMap, use custom constraint and remote validation"
		javaScriptConstraintCode = createRemoteJavaScriptConstraints(RequestContextHolder.requestAttributes.contextPath, constraintName, constrainedProperty.owningClass.name, constrainedProperty.propertyName)
	}
}
if (javaScriptConstraintCode) {
	javaScriptConstraints << javaScriptConstraintCode
	if (i < constraintNames.size() - 1) {
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
def constraintNames
String javaScriptMessageCode


def constraintsMap = getConstraintsMap(constrainedProperty.propertyType)
javaScriptMessages << "{ "
constraintNames = getConstraintNames(constrainedProperty)
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
if (constraintNames.size() > 0) {
	javaScriptMessages << ", "
} else {
	javaScriptMessages << " "
}
}

constraintNames.eachWithIndex { constraintName, i ->
javaScriptConstraint = constraintsMap[constraintName]
javaScriptMessageCode = null
args.clear()
args = [constrainedProperty.propertyName, constrainedProperty.owningClass]
if (javaScriptConstraint) {
	switch (constraintName) {
		case "nullable":
			if (!constrainedProperty.isNullable()) {
				javaScriptMessageCode = "${javaScriptConstraint}: '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'"
			}
		case "blank":
			if (!constrainedProperty.isBlank()) {
				javaScriptMessageCode = "${javaScriptConstraint}: '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'"
			}
			break
		case "creditCard":
		case "email":
		case "url":
			if (constrainedProperty.isCreditCard() || constrainedProperty.isEmail() || constrainedProperty.isUrl()) {
				args << VALUE_PLACEHOLDER
				javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'; }".replace(
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
			args << constrainedProperty."${constraintName}"
			javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'; }".replace(
				VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			break
		
		case "range":
		case "size":
			args << VALUE_PLACEHOLDER
			def range = constrainedProperty."${constraintName}"
			args << range.from
			args << range.to
			javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'; }".replace(
				VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			break
		
		case "unique":
		case "validator":
			args << VALUE_PLACEHOLDER
			javaScriptMessageCode = "${javaScriptConstraint}: function() { return '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'; }".replace(
				VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
			break
	}
} else {
	def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
	if (customConstraintsMap && customConstraintsMap[constraintName]) {
		args << VALUE_PLACEHOLDER
		javaScriptMessageCode = "${constraintName}: function() { return '${getMessage(constrainedProperty.owningClass, constrainedProperty.propertyName, args, constraintName, locale)}'; }".replace(
			VALUE_PLACEHOLDER, "' + \$('#${constrainedProperty.propertyName}').val() + '");
	} // else remote validation, using remote message.
}
if (javaScriptMessageCode) {
	javaScriptMessages << javaScriptMessageCode
	if (i < constraintNames.size() - 1) {
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
