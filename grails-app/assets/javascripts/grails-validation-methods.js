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
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/

// inList:["A","B","C"]
jQuery.validator.addMethod("inList", function(value, element, params) {
	return this.optional(element) || $.inArray(value, params) != -1;
}, "Value not exists in the pre-defined List.");  

// matches: "[a-z]+" => letteronly
jQuery.validator.addMethod("matches", function(value, element, param) {
	return this.optional(element) || value.match(new RegExp("^" + param + "$"));
}, jQuery.validator.format('Value not matches the given regular expression "{0}".'));

// notEqual: "ABC"
jQuery.validator.addMethod("notEqual", function(value, element, param) {
	return this.optional(element) || value != param;
}, jQuery.validator.format('Value should not equals to "{0}"'));

// minDate: new Date(ms)
// new Date(year, month, day, hours, minutes, seconds, milliseconds)
jQuery.validator.addMethod("minDate", function(value, element, param) {
	return this.optional(element) || new Date(value) >= param;
}, jQuery.validator.format('The date must be on or after {0}'));

// maxDate: new Date(ms)
jQuery.validator.addMethod("maxDate", function(value, element, param) {
	return this.optional(element) || new Date(value) <= param;
}, jQuery.validator.format('The date must be on or before {0}'));

// rangeDate: [new Date(ms), new Date(ms)]
jQuery.validator.addMethod("rangeDate", function(value, element, params) {
	var dateValue = new Date(value)
	return this.optional(element) || dateValue >= params[0] && dateValue <= params[1];
}, jQuery.validator.format('The date must be between {0} and {1}.'));

jQuery.validator.addMethod("unique", function(value, element, params) { 
  return JQueryValidatorUI.remote(this, "unique", value, element, params);
}, '');

jQuery.validator.addMethod("validator", function(value, element, params) { 
	return JQueryValidatorUI.remote(this, "validator", value, element, params);
}, '');

jQuery.validator.addMethod("custom", function(value, element, params) { 
	return JQueryValidatorUI.remote(this, null, value, element, params); // unknown constraint
}, '');

// http://www.24hourapps.com/2009/02/jquery-international-phone-number.html
jQuery.validator.addMethod("phone", function(value, element, params) { 
	return this.optional(element) || value.match(/^((\+)?[1-9]{1,2})?([-\s\.])?((\(\d{1,4}\))|\d{1,4})(([-\s\.])?[0-9]{1,12}){1,2}$/); 
}, 'Invalid international phone number.');

// amended from existing remote method
var JQueryValidatorUI = {
  remote: function(validator, constraint, value, element, params) { 
			if ( validator.optional(element) )
				return "dependency-mismatch";
			
			var previous = validator.previousValue(element);
			if (!validator.settings.messages[element.name] )
				validator.settings.messages[element.name] = {};
			previous.originalMessage = validator.settings.messages[element.name].remote;
			validator.settings.messages[element.name].remote = previous.message;
			
			if ( previous.old !== value ) {
				previous.old = value;
				validator.startRequest(element);
			  params.data[element.name] = value;
			  if (constraint)
			    params.data['constraint'] = constraint;
				$.ajax($.extend(true, {
					url: params.url,
					mode: "abort",
					port: "validate" + element.name,
					dataType: "json",
					data: params.data,
					success: function(response) {
						validator.settings.messages[element.name].remote = previous.originalMessage;
						var valid = response === true;
						if ( valid ) {
							var submitted = validator.formSubmitted;
							validator.prepareElement(element);
							validator.formSubmitted = submitted;
							validator.successList.push(element);
							validator.showErrors();
						} else {
							var errors = {};
							var message = (previous.message = response.message || validator.defaultMessage( element, constraint ));
							errors[element.name] = $.isFunction(message) ? message(value) : message;
							validator.showErrors(errors);
						}
						previous.valid = valid;
						validator.stopRequest(element, valid);
					}
				}, params.url));
				return "pending";
			} else if( validator.pending[element.name] ) {
				return "pending";
			}
			return previous.valid;
		}	
};
	