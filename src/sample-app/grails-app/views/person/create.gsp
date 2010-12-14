<%@ page import="org.grails.jquery.validation.ui.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        <g:javascript library="jquery" plugin="jquery"/>
        <jqval:resources />
        <jqvalui:resources />
        <jqvalui:renderValidationScript for="org.grails.jquery.validation.ui.Person" also="homeAddress, workAddress" />	                	                
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${personInstance}">
            <jqvalui:renderErrors style="margin-bottom:10px">
                <g:renderErrors bean="${personInstance}" as="list" />
            </jqvalui:renderErrors>
            </g:hasErrors>
            <jqvalui:renderErrors style="margin-bottom:10px"/>
            <g:form action="save" name="personForm">
								<g:render template="createAndEdit" />
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
