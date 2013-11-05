/* Copyright 2010-2013 the original author or authors.
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
    def version = "1.4.8"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
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
    def description = 'JQuery Validation UI Plugin - Client Side Validation without writing JavaScript'

    // URL to the plugin's documentation
  def documentation = "http://limcheekin.github.com/jquery-validation-ui"
	def issueManagement = [ system:"GitHub", url:"http://github.com/limcheekin/jquery-validation-ui/issues" ]
	def scm = [ url:"http://github.com/limcheekin/jquery-validation-ui" ]
}
