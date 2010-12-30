package org.grails.jquery.validation.ui

class Person {
	String name
	String phone
	Address homeAddress
	Address workAddress
	static embedded = ['homeAddress', 'workAddress']
	
	static constraints = {
		name blank:false
		phone phone:true, minSize: 10
	}
}
