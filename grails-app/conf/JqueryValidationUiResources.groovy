modules = {
	// NOTE: jquery validation is also required, but support
	//	needs to be added to the jquery-validation plugin first.
	'jquery-validation-ui' {
		dependsOn 'jquery'
		resource id:"qtip", url:[plugin:'jqueryValidationUi', dir:'js/qTip', file:'jquery.qtip.pack.js']
		resource id:"validation-methods", url:[plugin:'jqueryValidationUi', dir:'js/jquery-validation-ui', file:'grails-validation-methods.js']
		
		resource id:"qtip-theme", url:[plugin:'jqueryValidationUi', dir:'css/qTip', file:'jquery.qtip.css']
    }
}