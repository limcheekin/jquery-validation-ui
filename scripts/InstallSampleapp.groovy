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
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 0.1
*
*/

includeTargets << grailsScript("Init")

target(main: "Install person sample application") {
	ant.copy (todir:"${basedir}/grails-app/views", overwrite: true) {
		fileset dir:"${jqueryValidationUiPluginDir}/src/sample-app/grails-app/views"
	 }
	
	ant.copy (todir:"${basedir}/grails-app/controllers", overwrite: true) {
		fileset dir:"${jqueryValidationUiPluginDir}/src/sample-app/grails-app/controllers"
	 }
	
	ant.copy (todir:"${basedir}/grails-app/domain", overwrite: true) {
		fileset dir:"${jqueryValidationUiPluginDir}/src/sample-app/grails-app/domain"
	 }
	
  updateMessages(jqueryValidationUiPluginDir)
}

setDefaultTarget(main)

private void updateMessages(def pluginBasedir) {
	def messagesFile = new File(basedir, 'grails-app/i18n/messages.properties')
	if (messagesFile.exists() && messagesFile.text.indexOf("person") == -1) {
		messagesFile.withWriterAppend { messagesFileWriter ->
			messagesFileWriter.writeLine('')
			new File("${pluginBasedir}/src/sample-app/grails-app/i18n/messages.properties").eachLine { line ->
					messagesFileWriter.writeLine(line)
			}
		}
		ant.echo '''
******************************************************************
* Your grails-app/i18n/messages.properties has been updated with *
* default messages of the Person Sample Application;             *
* please verify that the values are correct.                     *
******************************************************************
		'''
	}
}