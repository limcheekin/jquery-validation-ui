
<%@ page import="org.grails.jquery.validation.ui.DummyDomain" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'dummyDomain.label', default: 'DummyDomain')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'dummyDomain.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'dummyDomain.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="title" title="${message(code: 'dummyDomain.title.label', default: 'Title')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'dummyDomain.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="nullableField" title="${message(code: 'dummyDomain.nullableField.label', default: 'Nullable Field')}" />
                        
                            <g:sortableColumn property="blankField" title="${message(code: 'dummyDomain.blankField.label', default: 'Blank Field')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${dummyDomainInstanceList}" status="i" var="dummyDomainInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${dummyDomainInstance.id}">${fieldValue(bean: dummyDomainInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: dummyDomainInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: dummyDomainInstance, field: "title")}</td>
                        
                            <td>${fieldValue(bean: dummyDomainInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: dummyDomainInstance, field: "nullableField")}</td>
                        
                            <td>${fieldValue(bean: dummyDomainInstance, field: "blankField")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${dummyDomainInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
