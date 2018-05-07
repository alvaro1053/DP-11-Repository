

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Formulario para la creación de mensajes nuevos, (escritura de un mensaje a un actor) --%>
<jstl:if test="${mailMessage.id==0}">
<form:form action="${requestURI}" modelAttribute="mailMessage">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="isSpam" />
	<form:hidden path="sender" />
	<form:hidden path="moment" />
	<form:hidden path="folder" />
	
	
	<jstl:if test="${broadcast}">
	<form:hidden path="recipient" value="${recipient.id}" />
	
	</jstl:if>
	
	<%-- Mostrado para elegir en el input el destinatario del mensaje, cuando es un mensaje broadcast de un administrador este input no se muestra puesto que el destinatario son todos los actores del sistema--%>
	<jstl:if test="${!broadcast}">
	<form:label path="recipient">
		<spring:message code="message.recipient.userAccount" />:
	</form:label>
	<form:select path="recipient" >
	<form:option label="----" value="0" />
	<form:options items="${recipients}" itemLabel="userAccount.username" itemValue="id" />
	</form:select>
	<form:errors cssClass="error" path="recipient" />
	<br>
	<br>
	</jstl:if>
	
	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:input path="subject" />
	<form:errors cssClass="error" path="subject" />
	<br>
	<br>
	
	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br>
	<br>
	
	<form:label path="priority">
		<spring:message code="message.priority" />:
	</form:label>
	<form:radiobutton path="priority" value="HIGH"/><spring:message code="message.priority.high" />
	<form:radiobutton path="priority" value="NORMAL" checked="checked" /><spring:message code="message.priority.normal" />
	<form:radiobutton path="priority" value="LOW"/><spring:message code="message.priority.low" />
	<br>
	<br>
	
	<jstl:if test="${mailMessage.id==0}">
	<input type="submit" name="save"
		value="<spring:message code="message.send" />" />&nbsp; 
	</jstl:if>
	
	<acme:cancel url="folder/actor/list.do" code="message.cancel"/>
	<br />

	

</form:form>
</jstl:if>






<jstl:if test="${permission }">

<%-- Formulario para la edición de mensajes que incluye: borrar un mensaje y moverlo a una carpeta diferente --%>
<jstl:if test="${mailMessage.id!=0}">
<form:form action="message/actor/edit.do" modelAttribute="mailMessage">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="isSpam" />
	<form:hidden path="sender" />
	<form:hidden path="moment" />
	<form:hidden path="recipient" />
	<form:hidden path="priority" />
	<form:hidden path="subject" />
	<form:hidden path="body" />
	
	<%-- El fieldset tiene todos los input disabled, es simplemente para que el usuario vea en la vista el mensaje que está editando --%>
	<fieldset>
	<legend><spring:message code="message.legend" /></legend>
	
	<form:label path="sender">
		<spring:message code="message.sender.userAccount" />:
	</form:label>
	<input type="text"  value="${mailMessage.sender.userAccount.username}"  disabled />
	<br>
	<br>
	
	
	
	<form:label path="recipient">
		<spring:message code="message.recipient.userAccount" />:
	</form:label>
	<input type="text" value="${mailMessage.recipient.userAccount.username}" disabled />
	<br>
	<br>
	
	
	<form:label path="moment">
	<spring:message code="message.moment" />:
	</form:label>
	<input type="text" value="${mailMessage.moment}"  disabled />
	<br>
	<br>
	
	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<input type="text" value="${mailMessage.subject }" disabled />
	<br>
	<br>
	
	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<input type="text"  value="${mailMessage.body}" disabled />
	<br>
	<br>

	</fieldset>
	<br>
	<br>
	
	<form:label path="folder">
		<spring:message code="message.folder" />:
	</form:label>
	<form:select path="folder" >
	<form:option label="----" value="0" />
	<form:options items="${folders}" itemLabel="name" itemValue="id" />
	</form:select>
	<form:errors cssClass="error" path="folder" />
	<br>
	<br>
	
	<input type="submit" name="move"
		value="<spring:message code="message.move" />" />&nbsp; 
	
	<input type="submit" name="delete"
			value="<spring:message code="message.delete" />"
			onclick="return confirm('<spring:message code="message.confirm.delete" />')" />&nbsp;
		
	<acme:cancel url="folder/actor/list.do" code="message.cancel"/>
	<br />
	
</form:form>
</jstl:if>

</jstl:if>


<jstl:if test="${!permission }">
<h3><spring:message code="message.nopermission" /></h3>
</jstl:if>