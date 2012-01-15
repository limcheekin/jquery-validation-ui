/* Copyright 2010-2012 the original author or authors.
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
    def version = "1.2.4-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [jqueryValidation: '1.7 > *', constraints: '0.5.1 > *']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

	def license = "APACHE"
    def author = "Lim Chee Kin"
    def authorEmail = "limcheekin@vobject.com"
	// Additional developers
	def developers = [
		[name:"Brian Saville", email:"bksaville@gmail.com"],
	]
	
    def title = "JQuery Validation UI Plugin - Client Side Validation without writing JavaScript"
    def description = '''\
Grails Validation mechanism is great, I like it! 
However, it is just server side validation solution, to build a comprehensive validation solution 
and highly interactive web application it has one missing important piece... 
Yes! It is client side validation solution that support all standard Grails constraints. 
Similar grails plugins such as Javascript Validator and Remote Constraints attempt to address this problem, 
but the outcome is less satisfactory in my opinion. I like the solution and demo published in the blog post titled 
"A jQuery inline form validation, because validation is a mess", but unluckily it is not using the excellent 
JQuery Validation plugin as it's validation engine, it had it's own solution known as jQuery Validation Engine. 

The JQuery Validation UI Plugin will bring Javascript Validator, Remote Constraints, jQuery Validation plugin and [qTip (jQuery tooltip plugin)|http://craigsworks.com/projects/qtip_new/] under the same root and 
deliver solution more than the jQuery Validation Engine. In short, when someone ask you what is JQuery Validation UI Plugin, just show them the following code block:
{code}
Javascript Validator + Remote Constraints + Custom Constraints + jQuery Validation plugin + qTip 
> jQuery Validation Engine
{code}

 * Source code: https://github.com/limcheekin/jquery-validation-ui
 * Documentation: http://limcheekin.github.com/jquery-validation-ui
 * Support: https://github.com/limcheekin/jquery-validation-ui/issues 
 ** Older bugs available here: http://code.google.com/p/jquery-validation-ui-plugin/issues/list
 * Discussion Forum: http://groups.google.com/group/jquery-validation-ui-plugin
'''

    // URL to the plugin's documentation
  def documentation = "http://limcheekin.github.com/jquery-validation-ui"
	def issueManagement = [ system:"GitHub", url:"http://github.com/limcheekin/jquery-validation-ui/issues" ]
	def scm = [ url:"http://github.com/limcheekin/jquery-validation-ui" ]
}
