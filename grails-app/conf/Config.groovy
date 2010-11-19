// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

grails.views.javascript.library="jquery"

// Added by the JQuery Validation plugin:
jqueryValidation.packed = true
jqueryValidation.cdn = false  // false or "microsoft"
jqueryValidation.additionalMethods = true

// Added by the JQuery Validation UI plugin:
jqueryValidationUi.qTip.packed = false

/*
* Grails constraints to JQuery Validation rules mapping for client side validation.
* Constraints not found in the mapping will trigger remote validation,
* except nullable:true is skip and nullable:false same with blank:false.
*/
jqueryValidationUi {
	StringConstraintsMap = [
		blank:"required", // inverse: blank=false,required:true
		creditCard:"creditcard",
		email:"email",
		inList:"inList",
		minSize:"minlength",
		maxSize:"maxlength",
		size:"rangelength",
		matches:"matches",
		notEqual:"notEqual",
		url:"url",
		nullable:"required",
		unique:"unique",
		validator:"validator"
	]
	
	// Long, Integer, Short, Float, Double, BigDecimal
	NumberConstraintsMap = [
		min:"min",
		max:"max",
		range:"range",
		notEqual:"notEqual",
		nullable:"required",
		inList:"inList"
	]
	
	CollectionConstraintsMap = [
		minSize:"minlength",
		maxSize:"maxlength",
		size:"rangelength",
		nullable:"required"
	]
	
	DateConstraintsMap = [
		min:"minDate",
		max:"maxDate",
		range:"rangeDate",
		notEqual:"notEqual",
		nullable:"required",
		inList:"inList"
	]
}


