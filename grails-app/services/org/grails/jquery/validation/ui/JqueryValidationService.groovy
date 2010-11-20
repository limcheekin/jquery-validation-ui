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
*/
package org.grails.jquery.validation.ui

import org.codehaus.groovy.grails.validation.ConstrainedPropertyBuilder


/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class JqueryValidationService {

    static transactional = false

    Map getConstrainedProperties(Class validatableClass) {
        def constrainedProperties
        if (!validatableClass.constraints) {
           throw new NullPointerException("validatableClass.constraints is null!") 
        }
        if (validatableClass.constraints instanceof Closure) {
            def validationClosure = validatableClass.constraints
            def constrainedPropertyBuilder = new ConstrainedPropertyBuilder(validatableClass.newInstance())
            validationClosure.setDelegate(constrainedPropertyBuilder)
            validationClosure()
            constrainedProperties = constrainedPropertyBuilder.constrainedProperties
        } else {
            constrainedProperties = validatableClass.constraints
        }
        return constrainedProperties
    }
}
