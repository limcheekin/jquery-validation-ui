package org.grails.jquery.validation.ui

class DummyDomain {
	 String name
   String title
   String description
   String nullableField
   String blankField
   Integer age
   String creditCardNo
   String email
   String url
   Integer inListInteger
   String letterOnly
   String notEqualABC
   Integer notEqual123
   String noConstraint
   
   static constraints = { 
    name unique:true
	  title blank: false, size: 5..20
    description blank: false, minSize: 5, maxSize:30 
		nullableField nullable: true
		blankField blank: true 	
		age min: 18
		creditCardNo creditCard: true
		email email:true
		url url:true
		inListInteger inList:[10,11,12]
		letterOnly matches:"[a-zA-Z]+"
		notEqualABC notEqual:"ABC"
		notEqual123 notEqual:123
   } 
}
