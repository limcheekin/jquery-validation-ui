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
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.validation.BeanPropertyBindingResult

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class JQueryRemoteValidatorController {
	
	def jqueryValidationService

	def validate = {		
		def validatableClass = grailsApplication.classLoader.loadClass(params.validatableClass)
		def constrainedProperties = jqueryValidationService.getConstrainedProperties(validatableClass)
		
		def validatableInstance 
		if (!params.id || params.id.equals("0")) {
			validatableInstance = validatableClass.newInstance()
			// Wire in spring dependencies...
			applicationContext.autowireCapableBeanFactory?.autowireBeanProperties(
				validatableInstance, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false)
		} else {
			validatableInstance = validatableClass.get(params.id.toLong())
		}
		
		def errors = new BeanPropertyBindingResult(validatableInstance, validatableInstance.class.name)
		def constrainedProperty = constrainedProperties[params.property]
		constrainedProperty.messageSource = grailsApplication.mainContext.messageSource

		Object propertyValue 
		if (constrainedProperty.propertyType == String) {
			propertyValue = params[params.property]
		} else {
      bindData(validatableInstance, params, [include: [params.property]])
	    propertyValue = validatableInstance."${params.property}"		
		}
		
		constrainedProperty.validate(validatableInstance, propertyValue, errors)
		if (grailsApplication.isDomainClass(validatableInstance.getClass()) && validatableInstance.isAttached()) {
			validatableInstance.discard()
		}
		def fieldError = errors.getFieldError(params.property)
		// println "fieldError = ${fieldError}, code = ${fieldError?.code}, params.constraint = ${params.constraint}"
		
		response.setContentType("text/json;charset=UTF-8")
		render fieldError ? """{"message":"${message(error: fieldError)}"}""" : "true"
	}
}
