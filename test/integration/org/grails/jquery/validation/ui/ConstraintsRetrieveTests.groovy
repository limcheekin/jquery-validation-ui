package org.grails.jquery.validation.ui

import grails.test.*
import org.codehaus.groovy.grails.validation.ConstrainedPropertyBuilder
import grails.util.GrailsNameUtils
import org.springframework.validation.BeanPropertyBindingResult

class ConstraintsRetrieveTests extends GrailsUnitTestCase {
	def grailsApplication
	
	protected void setUp() {
		super.setUp()
	}
	
	protected void tearDown() {
		super.tearDown()
	}
	
	void testRetrieveConstraintsFromDomainClass() {
		def domainClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.DummyDomain")
		assertNotNull domainClass
		assertNotNull domainClass.constraints
		def excludedDeclaredFields = ["id", "version", "metaClass", "constraints", "mapping"]
		def declaredFields = domainClass.declaredFields.findAll { it.name.indexOf('$') == -1 && it.name.indexOf('__') == -1 && !excludedDeclaredFields.contains(it.name)}
		declaredFields.each { println it.name }
		domainClass.constraints.each { k, v ->
			println "k=$k"
			println "v=$v"
		}
		def domainInstance = domainClass.newInstance()
		def errors = new BeanPropertyBindingResult(domainInstance, domainInstance.class.name)
		domainClass.constraints["title"].messageSource = grailsApplication.mainContext.messageSource
		domainClass.constraints["title"].validate(domainInstance, "Valid Title", errors) 
		assertTrue !hasError(domainClass, "title", "blank", errors)
	}
	
	void testRetrieveConstraintsFromIndependentCommandClass() {
		def commandClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.ProcessElementCommand")
		assertNotNull commandClass
		def validationClosure = commandClass.constraints
		println "validationClosure instanceof Closure = ${(validationClosure instanceof Closure)}"
		def constrainedPropertyBuilder = new ConstrainedPropertyBuilder(commandClass.newInstance())
		validationClosure.setDelegate(constrainedPropertyBuilder)
		validationClosure()
		def constrainedProperties = constrainedPropertyBuilder.constrainedProperties
		constrainedProperties.each { k, v ->
			println "k=$k"
			println "v=$v"
		}
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
	
	// http://www.ibm.com/developerworks/java/library/j-grails10148/index.html
	private hasError(validatableClass, property, constraint, errors) {
		def badField = errors.getFieldError(property)
		String code = "${GrailsNameUtils.getPropertyName(validatableClass)}.${property}.${constraint}"
		return badField?.codes.find {it == code} != null
	}
}
