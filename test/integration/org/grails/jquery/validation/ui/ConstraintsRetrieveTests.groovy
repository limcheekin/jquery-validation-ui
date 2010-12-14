package org.grails.jquery.validation.ui

import grails.test.*
import grails.util.GrailsNameUtils
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.util.ReflectionUtils
import org.springframework.validation.DefaultMessageCodesResolver

class ConstraintsRetrieveTests extends GrailsUnitTestCase {
	def grailsApplication
	def jqueryValidationService
	
	protected void setUp() {
		super.setUp()
	}
	
	protected void tearDown() {
		super.tearDown()
	}
	
	void testRetrieveConstraintsFromDomainClass() {
		def domainClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.DummyDomain")
		def constrainedProperties = getConstrainedProperties(domainClass)
		def domainInstance = domainClass.newInstance()
		def errors = new BeanPropertyBindingResult(domainInstance, domainInstance.class.name)
		constrainedProperties["title"].messageSource = grailsApplication.mainContext.messageSource
		constrainedProperties["title"].validate(domainInstance, "Valid Title", errors) 
		assertTrue !hasError(domainClass, "title", "blank", errors)
	}
	
	void testRetrieveConstraintsFromUserGroupClass() {
		def domainClass = grailsApplication.classLoader.loadClass("org.grails.activiti.springsecurity.UserGroup")
		def constrainedProperties = getConstrainedProperties(domainClass)
	}
	
	void testRetrieveConstraintsFromPersonClass() {
		def domainClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.Person")
		def constrainedProperties = getConstrainedProperties(domainClass)
		assertNotNull "findField(domainClass, 'homeAddress') not null", findField(domainClass,'homeAddress')
		 
		assertNotNull "findField(domainClass, 'homeAddress.code') not null", findField(domainClass,'homeAddress.code')
		assertEquals (['homeAddress', 'workAddress'], domainClass.embedded)
	}
	
	def findField(Class clazz, String name) {
		def field
		
		if (name.indexOf('.') == -1) {
			field = ReflectionUtils.findField(clazz, name)
		} else {
		  Class declaringClass = clazz
			def fieldNames = name.split("\\.")
			for (fieldName in fieldNames) {
				println "${declaringClass.name}.${fieldName}"
				field = ReflectionUtils.findField(declaringClass, fieldName)
				if (!field) {
					throw new IllegalArgumentException("Property $name invalid!")
				}
				declaringClass = field.type
			}
		}
		return field
	}
  private Map getConstrainedProperties(Class validatableClass) {
	  def constrainedProperties = jqueryValidationService.getConstrainedProperties(validatableClass)
	  constrainedProperties.each { k, v ->
		  println "k=$k"
		  println "v=$v"
	    }
	  return constrainedProperties
	 } 
	
	void testRetrieveConstraintsFromIndependentCommandClass() {
		def commandClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.ProcessElementCommand")
		def constrainedProperties = getConstrainedProperties(commandClass)
		def commandInstance = commandClass.newInstance()
		def errors = new BeanPropertyBindingResult(commandInstance, commandInstance.class.name)
		constrainedProperties["name"].messageSource = grailsApplication.mainContext.messageSource
		constrainedProperties["name"].validate(commandInstance, "", errors)
		assertTrue hasError(commandClass, "name", "blank", errors)
	}
	
	void testRetrieveConstraintsFromCommandInControllerClass() {
		def commandClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.LoginCommand")
		assertNotNull commandClass
		assertNotNull commandClass.constraints
	}
	
	void testRetrieveMessageCodesFromPersonWorkAddressNumber() {
		def domainClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.Person")
		def constrainedProperties = getConstrainedProperties(domainClass)
		
		constrainedProperties.each { name, constrainedProperty ->
			if (!constrainedProperty.appliedConstraints) {
				println "Property '$name' has no constraints"
			}
			else {
				constrainedProperty.appliedConstraints.each { c ->
					println "Property '$name' has constraint $c"
				}
			}
		}		
	}
	
	// http://www.ibm.com/developerworks/java/library/j-grails10148/index.html
	private hasError(validatableClass, property, constraint, errors) {
		def badField = errors.getFieldError(property)
		String code = "${GrailsNameUtils.getPropertyName(validatableClass)}.${property}.${constraint}"
		return badField?.codes.find {it == code} != null
	}
}
