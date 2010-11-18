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

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*/
class JqueryValidationUiGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.5 > *"
    // the other plugins this plugin depends on
    def dependsOn = [jqueryValidation: '1.7 > *']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Lim Chee Kin"
    def authorEmail = "limcheekin@vobject.com"
    def title = "JQuery Validation UI Plugin - The Ultimate Grails Client Side Validation"
    def description = '''\
Grails Validation mechanism is great, I like it! 
However, it is just server side validation solution, to build a comprehensive validation solution 
and highly interactive web application it has one missing important piece... 
Yes! It is client side validation solution that support all standard Grails constraints. 
Similar grails plugins such as Javascript Validator and Remote Constraints attempt to address this problem, 
but the outcome is less satisfactory in my opinion. I like the solution and demo in the blog post titled 
"A jQuery inline form validation, because validation is a mess", but unluckily it is not using the excellent 
JQuery Validation plugin as it's validation engine, it had it's own solution known as jQuery Validation Engine. 

The JQuery Validation UI Plugin will bring Javascript Validator, Remote Constraints, jQuery Validation plugin and [qTip (jQuery tooltip plugin)|http://craigsworks.com/projects/qtip_new/] under the same root and 
deliver solution more than the jQuery Validation Engine. In short, when someone ask you what is JQuery Validation UI Plugin, just show them the following code block:
{code}
Javascript Validator + Remote Constraints + jQuery Validation plugin + qTip 
> jQuery Validation Engine
{code}

 * Project Site and Documentation: http://code.google.com/p/jquery-validation-ui-plugin/
 * Support: http://code.google.com/p/jquery-validation-ui-plugin/issues/list
 * Discussion Forum: http://groups.google.com/group/jquery-validation-ui-plugin
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/jquery-validation-ui"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
