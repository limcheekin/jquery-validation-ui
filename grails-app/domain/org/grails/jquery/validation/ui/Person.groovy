package org.grails.jquery.validation.ui

class Person {
	String name
	Address homeAddress
	Address workAddress
	static embedded = ['homeAddress', 'workAddress']
	
	static constraints = {
		name blank:false, nullable:false
	}
}
