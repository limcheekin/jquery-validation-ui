

<%@ page import="org.grails.jquery.validation.ui.DummyDomain" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dummyDomain.label', default: 'DummyDomain')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${dummyDomainInstance}">
            <div class="errors">
                <g:renderErrors bean="${dummyDomainInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${dummyDomainInstance?.id}" />
                <g:hiddenField name="version" value="${dummyDomainInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="dummyDomain.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${dummyDomainInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="title"><g:message code="dummyDomain.title.label" default="Title" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'title', 'errors')}">
                                    <g:textField name="title" maxlength="20" value="${dummyDomainInstance?.title}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="dummyDomain.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" maxlength="30" value="${dummyDomainInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="nullableField"><g:message code="dummyDomain.nullableField.label" default="Nullable Field" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'nullableField', 'errors')}">
                                    <g:textField name="nullableField" value="${dummyDomainInstance?.nullableField}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="blankField"><g:message code="dummyDomain.blankField.label" default="Blank Field" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'blankField', 'errors')}">
                                    <g:textField name="blankField" value="${dummyDomainInstance?.blankField}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="age"><g:message code="dummyDomain.age.label" default="Age" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'age', 'errors')}">
                                    <g:textField name="age" value="${fieldValue(bean: dummyDomainInstance, field: 'age')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="creditCardNo"><g:message code="dummyDomain.creditCardNo.label" default="Credit Card No" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'creditCardNo', 'errors')}">
                                    <g:textField name="creditCardNo" value="${dummyDomainInstance?.creditCardNo}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="email"><g:message code="dummyDomain.email.label" default="Email" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'email', 'errors')}">
                                    <g:textField name="email" value="${dummyDomainInstance?.email}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="webAddress"><g:message code="dummyDomain.webAddress.label" default="Web Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'webAddress', 'errors')}">
                                    <g:textField name="webAddress" value="${dummyDomainInstance?.webAddress}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="inListInteger"><g:message code="dummyDomain.inListInteger.label" default="In List Integer" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'inListInteger', 'errors')}">
                                    <g:select name="inListInteger" from="${dummyDomainInstance.constraints.inListInteger.inList}" value="${fieldValue(bean: dummyDomainInstance, field: 'inListInteger')}" valueMessagePrefix="dummyDomain.inListInteger"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="letterOnly"><g:message code="dummyDomain.letterOnly.label" default="Letter Only" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'letterOnly', 'errors')}">
                                    <g:textField name="letterOnly" value="${dummyDomainInstance?.letterOnly}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="notEqualABC"><g:message code="dummyDomain.notEqualABC.label" default="Not Equal ABC" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'notEqualABC', 'errors')}">
                                    <g:textField name="notEqualABC" value="${dummyDomainInstance?.notEqualABC}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="notEqual123"><g:message code="dummyDomain.notEqual123.label" default="Not Equal123" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'notEqual123', 'errors')}">
                                    <g:textField name="notEqual123" value="${fieldValue(bean: dummyDomainInstance, field: 'notEqual123')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="noConstraint"><g:message code="dummyDomain.noConstraint.label" default="No Constraint" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: dummyDomainInstance, field: 'noConstraint', 'errors')}">
                                    <g:textField name="noConstraint" value="${dummyDomainInstance?.noConstraint}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
