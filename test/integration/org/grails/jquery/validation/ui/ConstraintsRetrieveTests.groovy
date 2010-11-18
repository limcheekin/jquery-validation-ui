package org.grails.jquery.validation.ui

import grails.test.*
import org.codehaus.groovy.grails.validation.ConstrainedPropertyBuilder

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
		println "domainClass.constraints instanceof Closure = ${(domainClass.constraints instanceof Closure)}"
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
	}
	
	void testRetrieveConstraintsFromCommandInControllerClass() {
		def commandClass = grailsApplication.classLoader.loadClass("org.grails.jquery.validation.ui.LoginCommand")
		assertNotNull commandClass
		assertNotNull commandClass.constraints
		println commandClass.constraints
	}
}
