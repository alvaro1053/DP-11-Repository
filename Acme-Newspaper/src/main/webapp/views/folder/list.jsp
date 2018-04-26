<%--
 * 
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>





<a href="message/actor/create.do"><spring:message code="folder.writeMessage" /></a>
<br/>

<security:authorize access="hasRole('ADMIN')">
<a href="message/admin/create.do"><spring:message code="folder.broadCastMessage" /></a>
<br>
</security:authorize>

<jstl:if test="${empty currentFolder.name}">
<a href="folder/actor/create.do"><spring:message code="folder.createFolder" /></a>
</jstl:if>

<jstl:if test="${not empty currentFolder.name}">
<a href="folder/actor/create.do?currentFolder=${currentFolder.id}"><spring:message code="folder.createFolder.nested" /></a>
</jstl:if>

<h3> <spring:message code="folder.listFolders" /></h3>

<jstl:if test="${empty currentFolder.name}">
<display:table pagesize="7" class="displaytag" 
	name="folders" requestURI="folder/actor/list.do" id="folder">
	
	<display:column>
	<a href="folder/actor/list.do?folderId=${folder.id}"><spring:message code="folder.show" /></a>
	</display:column>
	
	<spring:message code="folder.folder.name" var="name" />
	<display:column property="name" title="${name}" sortable="true" />
	
	<spring:message code="folder.folder.subFolders" var="subFolders" />
	<display:column  title="${subFolders}" sortable="false" >
	
		<jstl:if test="${not empty folder.childFolders}">
		<ul>
		<jstl:forEach items="${folder.childFolders}" var="childFolder">
			<li><a href="folder/actor/list.do?folderId=${childFolder.id}">${childFolder.name}</a></li>
		</jstl:forEach>
		</ul>
		</jstl:if>
		
		<jstl:if test="${empty folder.childFolders}">
		<spring:message code="folder.childFolders.empty" var="childFoldersEmpty"/>
		<jstl:out value="${childFoldersEmpty}" />
		</jstl:if>
		
	</display:column>
	
	
	<display:column>
	<jstl:if test="${!folder.isSystem}">
	<a href="folder/actor/edit.do?folderId=${folder.id}"><spring:message code="folder.editFolder" /></a>
	</jstl:if>
	</display:column>
	
	
</display:table>
</jstl:if>


<jstl:if test="${not empty currentFolder.name}">

<h3> <spring:message code="folder.current.folder" /> : <jstl:out value="${currentFolder.name}" /> </h3>

<jstl:if test="${empty folders}">
<spring:message code="folder.childFolders.empty" var="childFoldersEmptyAgain"/>
<jstl:out value="${childFoldersEmptyAgain}"></jstl:out>
</jstl:if>


<jstl:if test="${not empty folders}">
<display:table pagesize="5" class="displaytag" 
	name="folders" requestURI="folder/actor/list.do" id="folder">
	
	<display:column>
	<a href="folder/actor/list.do?folderId=${folder.id}"><spring:message code="folder.show" /></a>
	</display:column>
	
	<spring:message code="folder.folder.name" var="name" />
	<display:column property="name" title="${name}" sortable="true" />
	
	<spring:message code="folder.folder.subFolders" var="subFolders" />
	<display:column  title="${subFolders}" sortable="false" >
	
		<jstl:if test="${not empty folder.childFolders}">
		<ul>
		<jstl:forEach items="${folder.childFolders}" var="childFolder">
			<li><a href="folder/actor/list.do?folderId=${childFolder.id}">${childFolder.name}</a></li>
		</jstl:forEach>
		</ul>
		</jstl:if>
		
		<jstl:if test="${empty folder.childFolders}">
		<spring:message code="folder.childFolders.empty" var="childFoldersEmpty"/>
		<jstl:out value="${childFoldersEmpty}" />
		</jstl:if>
	</display:column>
	
	
	<display:column>
	<a href="folder/actor/edit.do?folderId=${folder.id}"><spring:message code="folder.editFolder" /></a>
	</display:column>
	
	
</display:table>
</jstl:if>

<%-- La lista de mensajes de la carpeta seleccionada: --%>

<h3> <spring:message code="folder.listMessages" /> ${currentFolder.name}</h3>

<jstl:if test="${empty messages}">
<spring:message code="folder.messages.empty" var="messagesEmpty"/>
<jstl:out value="${messagesEmpty}"></jstl:out>
</jstl:if>

<jstl:if test="${not empty messages}">
<display:table pagesize="5" class="displaytag" 
	name="messages" requestURI="folder/actor/list.do" id="message">
	
	<jstl:if test="${currentFolder.name != 'out box'}">
	
	<spring:message code="folder.message.sender" var="sender" />
	<display:column title="${sender}" sortable="true" >
		
		<%-- Caso normal, se muestra el username del sender --%>
		<jstl:if test="${message.sender != message.recipient }">
			<jstl:out value="${message.sender.userAccount.username }"></jstl:out>
		</jstl:if>
		
		<%-- Caso para las notificaciones de cambio de status de application,
		se ha modelado que si el sender y el recipient son el mismo actor, 
		en la columna de sender debe aparecer SYSTEM para especificar que es un mensaje del sistema --%>
		
		<jstl:if test="${message.sender == message.recipient }">
			<spring:message code="folder.message.system" var ="system"/>
			<jstl:out value ="${system }"/>
		</jstl:if>
	</display:column>
	</jstl:if>
	
	<jstl:if test="${(currentFolder.name == 'out box')||(!currentFolder.isSystem)}">
	
		<spring:message code="folder.message.recipient" var="recipient" />
		<display:column title="${recipient}" sortable="true">
		
		<%-- Caso normal en el que es un mensaje de una actor a otro actor diferente --%>
		<jstl:if test="${message.sender != message.recipient }">
		<jstl:out value="${message.recipient.userAccount.username}" />
		</jstl:if>
		
		<%-- Caso broadcast, según hemos modelado cuando el sender y el recipient es el mismo y
		 ambos es un administrador significa que es un mensaje broadcast que hay que guardar 
		 en el out box del admin que realizó dicho broadcast --%>
		  
		<jstl:if test="${(message.sender == message.recipient)&& (message.sender.userAccount.authorities[0].authority == 'ADMIN') }">
		<spring:message code="folder.message.broadcast" var="broadcast" />
		<jstl:out value="${broadcast}" />
		</jstl:if>
		
		
		</display:column>
	</jstl:if>
	
	<spring:message code="folder.message.moment" var="moment" />
	<display:column property="moment" title="${moment}" sortable="true" format = "{0,date,dd/MM/yyyy HH:mm}"/>
	
	<spring:message code="folder.message.subject" var="subject" />
	<display:column property="subject" title="${subject}" sortable="false" />
	
	<spring:message code="folder.message.body" var="body" />
	<display:column property="body" title="${body}" sortable="false" />
	
	<spring:message code="folder.message.priority" var="priority" />
	<display:column property="priority" title="${priority}" sortable="true" />
	
	<display:column>
	<a href="message/actor/edit.do?messageId=${message.id}"><spring:message code="folder.message.edit"/></a>
	</display:column>
	
</display:table>
</jstl:if>

</jstl:if>




