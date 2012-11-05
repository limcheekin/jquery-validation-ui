package org.grails.jquery.validation.ui

import spock.lang.*
import grails.test.mixin.*
import org.springframework.context.MessageSource

@TestFor(JqueryValidationService)
public class JqueryValidationServiceSpec extends Specification {
	def setup() {
		String.metaClass.encodeAsJavaScript = { ->
			org.codehaus.groovy.grails.plugins.codecs.JavaScriptCodec.encode(delegate)
		}
	}

	@Issue("https://github.com/limcheekin/jquery-validation-ui/issues/15")
	def "Escaped messages"() {
		given:
		MessageSource messageSource = Mock()
		service.messageSource = messageSource

		when:
		def message = service.getMessage(this.class, "prop", null, "max", null)

		then:
		1 * messageSource.getMessage("${this.class.name}.prop.max", null, null, null) >> "my 'message'"
		0 * _._
		message=="my \\'message\\'"
	}

	@Issue("https://github.com/limcheekin/jquery-validation-ui/issues/15")
	def "Escaped typeMismatch messages"() {
		given:
		MessageSource messageSource = Mock()
		service.messageSource = messageSource

		when:
		def message = service.getTypeMismatchMessage(this.class, null, null, "prop", null)

		then:
		1 * messageSource.getMessage("typeMismatch.${this.class.name}.prop", null, null, null) >> "my 'message'"
		0 * _._
		message=="my \\'message\\'"
	}
}