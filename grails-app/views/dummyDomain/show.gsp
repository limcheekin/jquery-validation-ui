
<%@ page import="org.grails.jquery.validation.ui.DummyDomain" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dummyDomain.label', default: 'DummyDomain')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.title.label" default="Title" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "title")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.description.label" default="Description" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "description")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.nullableField.label" default="Nullable Field" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "nullableField")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.blankField.label" default="Blank Field" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "blankField")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.age.label" default="Age" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "age")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.creditCardNo.label" default="Credit Card No" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "creditCardNo")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.email.label" default="Email" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "email")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.webAddress.label" default="Web Address" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "webAddress")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.inListInteger.label" default="In List Integer" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "inListInteger")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.letterOnly.label" default="Letter Only" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "letterOnly")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.notEqualABC.label" default="Not Equal ABC" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "notEqualABC")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.notEqual123.label" default="Not Equal123" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "notEqual123")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="dummyDomain.noConstraint.label" default="No Constraint" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: dummyDomainInstance, field: "noConstraint")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${dummyDomainInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
