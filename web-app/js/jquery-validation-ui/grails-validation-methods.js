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
	return this.optional(element) || value.match(new RegExp("." + param + "$"));
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

//http://stackoverflow.com/questions/976384/jquery-validator-addmethod-custom-message
jQuery.validator.addMethod("unique", function(value, element, params) { 
    var validator = this;
    params.data[element.name] = value;
    params.data['constraint'] = 'unique';
    $.post(params.url, params.data, function(response) {
    	if (response == 'true'){ return true; }
    	else {
    		var errors = {};
    		errors[element.name] =  response;
    		validator.showErrors(errors);
    		return false;
    	}
    }, 'text');
}, '');

jQuery.validator.addMethod("validator", function(value, element, params) { 
    var validator = this;
    params.data[element.name] = value;
    params.data['constraint'] = 'validator';
    $.post(params.url, params.data, function(response) {
    	if (response == 'true'){ return true; }
    	else {
    		var errors = {};
    		errors[element.name] =  response;
    		validator.showErrors(errors);
    		return false;
    	}
    }, 'text');
}, '');

jQuery.validator.addMethod("custom", function(value, element, params) { 
    var validator = this;
    params.data[element.name] = value;
    $.post(params.url, params.data, function(response) {
    	if (response == 'true'){ return true; }
    	else {
    		var errors = {};
    		errors[element.name] =  response;
    		validator.showErrors(errors);
    		return false;
    	}
    }, 'text');
}, '');