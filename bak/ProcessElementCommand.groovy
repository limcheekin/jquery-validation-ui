package org.grails.jquery.validation.ui


class ProcessElementCommand implements Serializable {
	String id
	String name
	
	static constraints = {
		id blank:false, size:5..50, notEqual:"definitions"
		name blank:false, size:5..50
	}
}