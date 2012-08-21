/* Copyright 2010 the original author or authors.
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
import grails.util.GrailsNameUtils


/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class JqueryValidationService {

    static final String TYPE_MISMATCH_MESSAGE_PREFIX = "typeMismatch."
    static final ERROR_CODE_SUFFIXES = [
		  "error",
		  "invalid"
    ]   
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
        return message
    }           
}
