package org.grails.jquery.validation.ui

class Person {
	Address homeAddress
	Address workAddress
	Address notEmbeddedAddress
	static embedded = ['homeAddress', 'workAddress']
}
