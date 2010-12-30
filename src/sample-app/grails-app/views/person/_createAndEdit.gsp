           <div class="dialog">
               <table>
                   <tbody>
                       <tr class="prop">
                           <td valign="top" class="name">
                               <label for="name"><g:message code="person.name.label" default="Person Name" /></label>
                           </td>
                           <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'name', 'errors')}">
                               <g:textField name="name" value="${personInstance?.name}" />
                           </td>
                           <td>
                           <g:if test="${hasErrors(bean: personInstance, field: 'name', 'errors')}">
                           	<jqvalui:renderError for="name" style="margin-top: -5px">
                           	    <g:eachError bean="${personInstance}" field="name"><g:message error="${it}" /></g:eachError>
                           	</jqvalui:renderError>
                           </g:if>
                           </td>
                       </tr>
                   
                       <tr class="prop">
                           <td valign="top" class="name">
                               <label for="phone"><g:message code="person.phone.label" default="Phone" /></label>
                           </td>
                           <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'phone', 'errors')}">
                               <g:textField name="phone" value="${personInstance?.phone}" />
                           </td>
                           <td>
                           <g:if test="${hasErrors(bean: personInstance, field: 'phone', 'errors')}">
                           	<jqvalui:renderError for="phone" style="margin-top: -5px">
                           	    <g:eachError bean="${personInstance}" field="phone"><g:message error="${it}" /></g:eachError>
                           	</jqvalui:renderError>
                           </g:if>
                           </td>
                       </tr>
                                          
                       <tr class="prop">
                           <td valign="top" class="name">
                               <label for="workAddress.code"><g:message code="workAddress.code.label" default="Work Address Code" /></label>
                           </td>
                           <td valign="top" class="value ${hasErrors(bean: personInstance?.workAddress, field: 'code', 'errors')}">
                               <g:textField name="workAddress.code" value="${personInstance?.workAddress?.code}" />
                           </td>
                           <td>
                           <g:if test="${hasErrors(bean: personInstance, field: 'workAddress.code', 'errors')}">
                           	<jqvalui:renderError for="workAddress.code" style="margin-top: -5px">
                           	    <g:eachError bean="${personInstance}" field="workAddress.code"><g:message error="${it}" /></g:eachError>
                           	</jqvalui:renderError>
                           </g:if>
                           </td>                                
                       </tr>
                   
                       <tr class="prop">
                           <td valign="top" class="name">
                               <label for="workAddress.number"><g:message code="workAddress.number.label" default="Work Address Number" /></label>
                           </td>
                           <td valign="top" class="value ${hasErrors(bean: personInstance?.workAddress, field: 'number', 'errors')}">
                               <g:textField name="workAddress.number" value="${fieldValue(bean: personInstance?.workAddress, field: 'number')}" />
                           </td>
                           <td>
                           <g:if test="${hasErrors(bean: personInstance, field: 'workAddress.number', 'errors')}">
                           	<jqvalui:renderError for="workAddress.number" style="margin-top: -5px">
                           	    <g:eachError bean="${personInstance}" field="workAddress.number"><g:message error="${it}" /></g:eachError>
                           	</jqvalui:renderError>
                           </g:if>
                           </td>                                  
                       </tr>
                       <tr class="prop">
                           <td valign="top" class="name">
                               <label for="homeAddress.code"><g:message code="homeAddress.code.label" default="Home Address Code" /></label>
                           </td>
                           <td valign="top" class="value ${hasErrors(bean: personInstance?.homeAddress, field: 'code', 'errors')}">
                               <g:textField name="homeAddress.code" value="${personInstance?.homeAddress?.code}" />
                           </td>
                           <td>
                           <g:if test="${hasErrors(bean: personInstance, field: 'homeAddress.code', 'errors')}">
                           	<jqvalui:renderError for="homeAddress.code" style="margin-top: -5px">
                           	    <g:eachError bean="${personInstance}" field="homeAddress.code"><g:message error="${it}" /></g:eachError>
                           	</jqvalui:renderError>
                           </g:if>
                           </td>                                  
                       </tr>
                   
                       <tr class="prop">
                           <td valign="top" class="name">
                               <label for="homeAddress.number"><g:message code="homeAddress.number.label" default="Home Address Number" /></label>
                           </td>
                           <td valign="top" class="value ${hasErrors(bean: personInstance?.homeAddress, field: 'number', 'errors')}">
                               <g:textField name="homeAddress.number" value="${fieldValue(bean: personInstance?.homeAddress, field: 'number')}" />
                           </td>
                           <td>
                           <g:if test="${hasErrors(bean: personInstance, field: 'homeAddress.number', 'errors')}">
                           	<jqvalui:renderError for="homeAddress.number" style="margin-top: -5px">
                           	    <g:eachError bean="${personInstance}" field="homeAddress.number"><g:message error="${it}" /></g:eachError>
                           	</jqvalui:renderError>
                           </g:if>
                           </td>  
                       </tr>                        
                   </tbody>
               </table>
           </div>