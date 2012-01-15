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

import org.springframework.web.servlet.support.RequestContextUtils as RCU
import grails.util.GrailsNameUtils
import java.lang.reflect.Field
import org.springframework.util.ReflectionUtils

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class JQueryValidationUiTagLib {
    static namespace = "jqvalui"
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
	
    static final ERROR_CODE_SUFFIXES = [
		  "error",
		  "invalid"
    ]
	  
    static final String TAG_ERROR_PREFIX = "Tag [jqvalui:renderValidationScript] Error: "
	
    static final String TYPE_MISMATCH_MESSAGE_PREFIX = "typeMismatch."
	  
    def jqueryValidationService
	
    def resources = { attrs, body ->
        String type = attrs.remove("type")
        def packed 
        if (!type) {
            packed = grailsApplication.config.jqueryValidationUi.qTip.get("packed", true)
            renderJavaScript g.resource(plugin:"jqueryValidationUi", dir:"js/qTip", file:"jquery.qtip.${packed?'pack.js':'js'}")
            renderJavaScript g.resource(plugin:"jqueryValidationUi", dir:"js/jquery-validation-ui", file:"grails-validation-methods.js")
            renderCSS g.resource(plugin:"jqueryValidationUi", dir:"css/qTip", file:"jquery.qtip.css")
        } else if (type.equals("js")) {
            packed = grailsApplication.config.jqueryValidationUi.qTip.get("packed", true)
            renderJavaScript g.resource(plugin:"jqueryValidationUi", dir:"js/qTip", file:"jquery.qtip.${packed?'pack.js':'js'}")
            renderJavaScript g.resource(plugin:"jqueryValidationUi", dir:"js/jquery-validation-ui", file:"grails-validation-methods.js")
        } else if (type.equals("css")) {
            renderCSS g.resource(plugin:"jqueryValidationUi", dir:"css/qTip", file:"jquery.qtip.css")
        }
    }
	
    def renderErrors = { attrs, body ->
        def renderErrorsOnTop = attrs.render ? Boolean.valueOf(attrs.render) : grailsApplication.config.jqueryValidationUi.get("renderErrorsOnTop", true)
        if (renderErrorsOnTop) {
            String qTipClasses = grailsApplication.config.jqueryValidationUi.qTip.classes?:""
            String style = attrs.remove("style")?:''
            String bodyText = body()
            def writer = getOut()
            writer << """
<div id="errorContainer${bodyText?'ServerSide':''}" style="z-index: 15001; opacity: 1; ${bodyText?'display: block':'display: none'}; position: relative; ${style}" class="ui-tooltip qtip ui-helper-reset ui-tooltip-focus ui-tooltip-pos-bc${qTipClasses?" $qTipClasses":""}">
<div style="width: 12px; height: 12px; background-color: transparent; border: 0px none; left: 50%; margin-left: -6px; bottom: -12px;" class="ui-tooltip-tip">
</div>
<div class="ui-tooltip-wrapper">
<div class="ui-tooltip-content errors">"""
            if (bodyText) {
                writer << bodyText
            } else {
                writer << '<ul></ul>'
            }
            writer << """
</div>
</div>
</div>
"""
        }
    }
	
    def renderError = { attrs, body ->
        String labelFor = attrs.remove("for")
        if (!labelFor) {
            throwTagError("Tag [jqvalui:renderError] Error: Tag missing required attribute [for]")
        }
        def renderErrorsOnTop = grailsApplication.config.jqueryValidationUi.get("renderErrorsOnTop", true)
        if (!renderErrorsOnTop) {
            String style = attrs.remove("style")?:''
            String qTipClasses = grailsApplication.config.jqueryValidationUi.qTip.classes?:""
            def writer = getOut()
            writer << """
<div style="z-index: 15001; opacity: 1; display: block; ${style}" class="ui-tooltip qtip ui-helper-reset ui-tooltip-focus ui-tooltip-pos-lc${qTipClasses?" $qTipClasses":""}">
<div style="width: 12px; height: 12px; background-color: transparent; border: 0px none; top: 50%; margin-top: -6px; left: -12px;" class="ui-tooltip-tip">
<div style="border-right: 12px solid ${getConnectorColor()}; border-top: 6px dashed transparent; border-bottom: 6px dashed transparent;" class="ui-tooltip-tip-inner">
</div>
</div>
<div class="ui-tooltip-wrapper">
  <div class="ui-tooltip-content">
     <label style="display: block;" for="${labelFor}">"""
            writer << body()
            writer << """</label>
  </div>
</div>
</div>
"""
        }
    }
	
    private String getConnectorColor() {
        def jQueryUiStyle = grailsApplication.config.jqueryValidationUi.qTip.get("jQueryUiStyle", false)
        return "rgb(217, 82, 82)"
    }
	
    private renderJavaScript(def url) {
        out << '<script type="text/javascript" src="' + url + '"></script>\n'
    }
	
    private renderCSS(def url) {
        out << '<link rel="stylesheet" type="text/css" media="screen" href="' + url + '" />\n'
    }
	
    def renderValidationScript = { attrs, body ->
        String forClass = attrs.remove("for")
        def alsoProperties = attrs.remove("also")
        def notProperties = attrs.remove("not")
        String form = attrs.remove("form")
        def config = grailsApplication.config.jqueryValidationUi
        def jQueryUiStyle = config.qTip.get("jQueryUiStyle", false)
        String qTipClasses = config.qTip.classes?:""
        String errorClass = attrs.errorClass?:config.errorClass?:"error"
        String validClass = attrs.validClass?:config.validClass?:"valid"
        def onsubmit = attrs.onsubmit ? Boolean.valueOf(attrs.onsubmit) : config.get("onsubmit", true)
		    def submitHandler = attrs.remove("submitHandler")
        def renderErrorsOnTop = attrs.renderErrorsOnTop ? Boolean.valueOf(attrs.renderErrorsOnTop) : config.get("renderErrorsOnTop", true)
        String renderErrorsOptions
        Locale locale = RCU.getLocale(request)
        
        if (!forClass) {
            throwTagError("${TAG_ERROR_PREFIX}Tag missing required attribute [for]")
        }
        def validatableClass = grailsApplication.classLoader.loadClass(forClass)
        if (!validatableClass) {
            throwTagError("${TAG_ERROR_PREFIX}Invalid validatableClass defined in attribute [for], $validatableClassName not found!")
        }
		
        if (notProperties) {
            notProperties = notProperties.split(',').collect { it.trim() }
        }
        if (alsoProperties) {
            alsoProperties = alsoProperties.split(',').collect { it.trim() }
            if (notProperties) {
                notProperties.addAll(alsoProperties)
            } else { 
                notProperties = new ArrayList(alsoProperties)
            }
        }
		
        if (renderErrorsOnTop) {
            renderErrorsOptions = """
errorContainer: '#errorContainer',
errorLabelContainer: 'div.errors ul',
wrapper: 'li',
"""	
        } else {
            renderErrorsOptions = """
success: function(label)
{
	\$('[id=' + label.attr('for') + ']').qtip('destroy');
},
errorPlacement: function(error, element)
{
	if (\$(error).text())
	\$(element).filter(':not(.${validClass})').qtip({
		overwrite: true,
		content: error,
		position: { my: 'left center', at: 'right center' },
		show: {
			event: false,
			ready: true
		},
		hide: false,
		style: {
			widget: ${jQueryUiStyle},
			classes: '${qTipClasses}',
			tip: true
		}
	});
},
"""
        }
        out << '<script type="text/javascript">\n'
        out << """\$(function() {
var myForm = \$('${form?"#$form":"form:first"}');
myForm.validate({
onkeyup: false,
errorClass: '${errorClass}',
validClass: '${validClass}',			
onsubmit: ${onsubmit},
"""
		if (submitHandler)
			out << "submitHandler: ${submitHandler},"
		out << """
${renderErrorsOptions}			
rules: {
"""

        ConstrainedPropertiesEntry rootConstrainedPropertiesEntry = new ConstrainedPropertiesEntry(validatableClass: validatableClass)
        rootConstrainedPropertiesEntry.constrainedProperties = jqueryValidationService.getConstrainedProperties(validatableClass)
        if (notProperties) {
            def rootNotProperties = notProperties.findAll { it.indexOf('.') == -1 }
            notProperties.removeAll(rootNotProperties)
            rootConstrainedPropertiesEntry.constrainedProperties = rootConstrainedPropertiesEntry.constrainedProperties.findAll{ k, v -> !rootNotProperties.contains(k) }
        }
        def constrainedPropertiesEntries = [rootConstrainedPropertiesEntry]
        ConstrainedPropertiesEntry childConstrainedPropertiesEntry
        def childNotProperties
        Class childValidatableClass
        alsoProperties.each { propertyName ->
            childValidatableClass = findField(validatableClass, propertyName).type
            childConstrainedPropertiesEntry = new ConstrainedPropertiesEntry(namespace:propertyName, validatableClass: childValidatableClass)
            childConstrainedPropertiesEntry.constrainedProperties = jqueryValidationService.getConstrainedProperties(childValidatableClass)
            if (notProperties) {
                childNotProperties = notProperties.collect {it.indexOf(propertyName) != -1 ? it.substring(propertyName.length() + 1) : null }
                notProperties.removeAll(childNotProperties)
                childConstrainedPropertiesEntry.constrainedProperties = childConstrainedPropertiesEntry.constrainedProperties.findAll{ k, v -> !childNotProperties.contains(k) }
            }
            constrainedPropertiesEntries << childConstrainedPropertiesEntry
        }
    
        out << createJavaScriptConstraints(constrainedPropertiesEntries, locale)
        out << "},\n" // end rules
        out << "messages: {\n"
        out << createJavaScriptMessages(constrainedPropertiesEntries, locale)
        out << "}\n" // end messages
        out << "});\n"
        out << "});\n"
        out << "</script>\n"
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
	
    private String createJavaScriptConstraints(List constrainedPropertiesEntries, Locale locale) {
        String javaScriptConstraints = ""
        def constraintsMap
        String javaScriptConstraint
        String namespacedPropertyName
        def constrainedPropertyValues
        String javaScriptConstraintCode

        constrainedPropertiesEntries.eachWithIndex { constrainedPropertiesEntry, entryIndex ->
            constrainedPropertyValues = constrainedPropertiesEntry.constrainedProperties.values()
            constrainedPropertyValues.eachWithIndex { constrainedProperty, propertyIndex  ->
                namespacedPropertyName = constrainedPropertiesEntry.namespace?"'${constrainedPropertiesEntry.namespace}.${constrainedProperty.propertyName}'":constrainedProperty.propertyName
                javaScriptConstraints += "${namespacedPropertyName}: {\n"
                constraintsMap = getConstraintsMap(constrainedProperty.propertyType)
                def constraintNames = getConstraintNames(constrainedProperty)			
                log.debug "$namespacedPropertyName: $constraintNames"
                javaScriptConstraintCode = null
						
                switch (constrainedProperty.propertyType) {
                    case Date:
                    javaScriptConstraintCode = "\tdate: true"
                    break
                    case Long:
                    case Integer:
                    case Short:
                    case BigInteger:
                    javaScriptConstraintCode = "\tdigits: true"
                    break
                    case Float:
                    case Double:
                    case BigDecimal:
                    javaScriptConstraintCode = "\t${(locale.country == 'br' || locale.country == 'de')?'numberDE':'number'}: true"
                    break
                }
								
                if (javaScriptConstraintCode) {
                    javaScriptConstraints += javaScriptConstraintCode
                    javaScriptConstraints += constraintNames.size() > 0 ? ",\n" : "\n"
                }
                constraintNames.eachWithIndex { constraintName, i ->
                    javaScriptConstraint = constraintsMap[constraintName]
                    javaScriptConstraintCode = null
                    if (javaScriptConstraint) {
                        switch (constraintName) {
                            case "nullable":
                            if (!constrainedProperty.isNullable()) {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: true"
                            }
                            break
                            case "blank":
                            if (!constrainedProperty.isBlank()) {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: true"
                            }
                            break
                            case "creditCard":
                            if (constrainedProperty.isCreditCard()) {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: true"
                            }
                            break
                            case "email":
                            if (constrainedProperty.isEmail()) {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: true"
                            }
                            break
                            case "url":
                            if (constrainedProperty.isUrl()) {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: true"
                            }
                            break
                            case "inList":
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: ["
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
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: '${constrainedProperty.matches}'"
                            break
                            case "max":
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: ${constrainedProperty.propertyType == Date ? "new Date(${constrainedProperty.max.time})" : constrainedProperty.max}"
                            break
                            case "maxSize":
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: ${constrainedProperty.maxSize}"
                            break
                            case "min":
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: ${constrainedProperty.propertyType == Date ? "new Date(${constrainedProperty.min.time})" : constrainedProperty.min}"
                            break
                            case "minSize":
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: ${constrainedProperty.minSize}"
                            break
                            case "notEqual":
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: ${constrainedProperty.propertyType == Date ? "new Date(${constrainedProperty.notEqual.time})" : "'${constrainedProperty.notEqual}'"}"
                            break
                            case "range":
                            def range = constrainedProperty.range
                            if (constrainedProperty.propertyType == Date) {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: [new Date(${range.from.time}), new Date(${range.to.time})]"
                            } else {
                                javaScriptConstraintCode = "\t${javaScriptConstraint}: [${range.from}, ${range.to}]"
                            }
                            break
                            case "size":
                            def size = constrainedProperty.size
                            javaScriptConstraintCode = "\t${javaScriptConstraint}: [${size.from}, ${size.to}]"
                            break
                            case "unique":
                            case "validator":
                            javaScriptConstraintCode = createRemoteJavaScriptConstraints(constraintName, constrainedPropertiesEntry.validatableClass.name, constrainedProperty.propertyName)
                            break
                        }
                    } else {
                        def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
                        if (customConstraintsMap && customConstraintsMap[constraintName]) {
                            javaScriptConstraintCode = "\t$constraintName: ${customConstraintsMap[constraintName]}"
                        } else {
                            log.info "${constraintName} constraint not found even in the CustomConstraintsMap, use custom constraint and remote validation"
                            javaScriptConstraintCode = createRemoteJavaScriptConstraints(constraintName, constrainedPropertiesEntry.validatableClass.name, constrainedProperty.propertyName)
                        }
                    }
                    if (javaScriptConstraintCode) {
                        javaScriptConstraints += javaScriptConstraintCode
                        javaScriptConstraints += i < constraintNames.size() - 1 ? ",\n" : "\n"
                    }
                }
                javaScriptConstraints += entryIndex == constrainedPropertiesEntries.size() - 1 && propertyIndex == constrainedPropertyValues.size() - 1 ? "}\n" : "},\n"
            }
        }
        return javaScriptConstraints
    }

    private String createRemoteJavaScriptConstraints(String constraintName, String validatableClassName, String propertyName) {
        String remoteJavaScriptConstraints = "\t${constraintName.equals('unique') || constraintName.equals('validator')?constraintName:'custom'}: {\n" +
		        "\turl: '${request.contextPath}/JQueryRemoteValidator/validate',\n" +
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
	  
    private List getConstraintNames(def constrainedProperty) {
        def constraintNames = constrainedProperty.appliedConstraints.collect { return it.name }
        if (constraintNames.contains("blank") && constraintNames.contains("nullable")) {
            constraintNames.remove("nullable") // blank constraint take precedence
        }
        return constraintNames
    }
	  
    private String createJavaScriptMessages(List constrainedPropertiesEntries, Locale locale) {
        def constraintsMap
        def args = []
        String javaScriptMessages = ""
        String javaScriptConstraint
        def constraintNames
        String namespacedPropertyName
        def constrainedPropertyValues
        String javaScriptMessageCode

        constrainedPropertiesEntries.eachWithIndex { constrainedPropertiesEntry, entryIndex ->
            constrainedPropertyValues = constrainedPropertiesEntry.constrainedProperties.values()
            constrainedPropertyValues.eachWithIndex { constrainedProperty, propertyIndex  ->
                namespacedPropertyName = constrainedPropertiesEntry.namespace?"'${constrainedPropertiesEntry.namespace}.${constrainedProperty.propertyName}'":constrainedProperty.propertyName
                constraintsMap = getConstraintsMap(constrainedProperty.propertyType)
                javaScriptMessages += "${namespacedPropertyName}: {\n"
                constraintNames = getConstraintNames(constrainedProperty)
                javaScriptMessageCode = null
                switch (constrainedProperty.propertyType) {
                    case Date:
                    javaScriptMessageCode = "\tdate: '${getTypeMismatchMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyType, constrainedPropertiesEntry.namespace, constrainedProperty.propertyName)}'"
                    break
                    case Long:
                    case Integer:
                    case Short:
                    case BigInteger:
                    javaScriptMessageCode = "\tdigits: '${getTypeMismatchMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyType, constrainedPropertiesEntry.namespace, constrainedProperty.propertyName)}'"
                    break
                    case Float:
                    case Double:
                    case BigDecimal:
                    if (locale.country == 'br' || locale.country == 'de')
                      javaScriptMessageCode = "\tnumberDE: '${getTypeMismatchMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyType, constrainedPropertiesEntry.namespace, constrainedProperty.propertyName)}'"
                    else
                      javaScriptMessageCode = "\tnumber: '${getTypeMismatchMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyType, constrainedPropertiesEntry.namespace, constrainedProperty.propertyName)}'"
                    break
                }

                if (javaScriptMessageCode) {
                    javaScriptMessages += javaScriptMessageCode
                    javaScriptMessages += constraintNames.size() > 0 ? ",\n" : "\n"
                }
		
                constraintNames.eachWithIndex { constraintName, i ->
                    javaScriptConstraint = constraintsMap[constraintName]
                    javaScriptMessageCode = null
                    args.clear()
                    args = [constrainedProperty.propertyName, constrainedPropertiesEntry.validatableClass]
                    if (javaScriptConstraint) {
                        switch (constraintName) {
                            case "nullable":
                            if (!constrainedProperty.isNullable()) {
                                javaScriptMessageCode = "\t${javaScriptConstraint}: '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'"
                            }
                            case "blank":
                            if (!constrainedProperty.isBlank()) {
                                javaScriptMessageCode = "\t${javaScriptConstraint}: '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'"
                            }
                            break
                            case "creditCard":
                            case "email":
                            case "url":
                            if (constrainedProperty.isCreditCard() || constrainedProperty.isEmail() || constrainedProperty.isUrl()) {
                                args << "' + \$('#${constrainedProperty.propertyName}').val() + '"
                                javaScriptMessageCode = "\t${javaScriptConstraint}: function() { return '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'; }"
                            }
                            break
                            case "inList":
                            case "matches":
                            case "max":
                            case "maxSize":
                            case "min":
                            case "minSize":
                            case "notEqual":
                            args << "' + \$('#${constrainedProperty.propertyName}').val() + '"
                            args << constrainedProperty."${constraintName}"
                            javaScriptMessageCode = "\t${javaScriptConstraint}: function() { return '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'; }"
                            break

                            case "range":
                            case "size":
                            args << "' + \$('#${constrainedProperty.propertyName}').val() + '"
                            def range = constrainedProperty."${constraintName}"
                            args << range.from
                            args << range.to
                            javaScriptMessageCode = "\t${javaScriptConstraint}: function() { return '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'; }"
                            break
							
														case "unique":
														case "validator":
														args << "' + \$('#${constrainedProperty.propertyName}').val() + '"
														javaScriptMessageCode = "\t${javaScriptConstraint}: function() { return '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'; }"
														break
                        }
                    } else {
                        def customConstraintsMap = grailsApplication.config.jqueryValidationUi.CustomConstraintsMap
                        if (customConstraintsMap && customConstraintsMap[constraintName]) {
                            args << "' + \$('#${constrainedProperty.propertyName}').val() + '"
                            javaScriptMessageCode = "\t${constraintName}: function() { return '${getMessage(constrainedPropertiesEntry.validatableClass, constrainedProperty.propertyName, args, constraintName)}'; }"
                        } // else remote validation, using remote message.
                    }
                    if (javaScriptMessageCode) {
                        javaScriptMessages += javaScriptMessageCode
                        javaScriptMessages += i < constraintNames.size() - 1 ? ",\n" : "\n"
                    }
                }
                javaScriptMessages += entryIndex == constrainedPropertiesEntries.size() - 1 && propertyIndex == constrainedPropertyValues.size() - 1 ? "}\n" : "},\n"
            }
        }
        return javaScriptMessages
    }
	
    private String getMessage(Class validatableClass, String propertyName, def args, String constraintName) {
        def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
        def locale = RCU.getLocale(request)
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
	
    private String getTypeMismatchMessage(Class validatableClass, Class propertyType, String propertyNamespace, String propertyName) {
      def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
      def locale = RCU.getLocale(request)
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

    private Field findField(Class clazz, String name) {
        Field field
		
        if (name.indexOf('.') == -1) {
            field = ReflectionUtils.findField(clazz, name)
        } else {
            Class declaringClass = clazz
            def fieldNames = name.split("\\.")
            for (fieldName in fieldNames) {
                field = ReflectionUtils.findField(declaringClass, fieldName)
                if (!field) {
                    throw new IllegalArgumentException("Property $name invalid!")
                }
                declaringClass = field.type
            }
        }
        return field
    }
}


