modules = {
	'jquery-validation-ui' {
		dependsOn 'jquery, jquery-validate'
		resource id:"validation-methods", url:[plugin:'jqueryValidationUi', dir:'js/jquery-validation-ui', file:'grails-validation-methods.js']
    }
    
	'jquery-validation-ui-qtip' {
		dependsOn 'jquery-validation-ui'
		resource id:"qtip", url:[plugin:'jqueryValidationUi', dir:'js/qTip', file:'jquery.qtip.pack.js']
		resource id:"qtip-theme", url:[plugin:'jqueryValidationUi', dir:'css/qTip', file:'jquery.qtip.css']
    }    
}
