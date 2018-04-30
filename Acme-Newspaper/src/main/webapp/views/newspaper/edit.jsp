<%--
 * edit.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<form:form action="newspaper/user/edit.do" modelAttribute="newspaperForm">
	<form:hidden path="id" />
	<form:hidden path="version" /> 
	
	
	<acme:textarea code="newspaper.title" path="title"/>
	
	<acme:textbox code="newspaper.description" path="description"/>
	
	<acme:textbox code="newspaper.pictureURL" path="pictureURL"/>
	
	<spring:message code="newspaper.publicationDate" var="publicationDate"/>
	<form:label path="publicationDate">${publicationDate}</form:label>
	<form:input path="publicationDate" placeholder="dd/mm/yyyy"/>
	<form:errors cssClass="error" path="publicationDate"/>
	<br />
	
	<spring:message code="newspaper.isPrivate"/>
	<form:checkbox path="isPrivate" name="isPrivate" value="true"/>
	
	
	
	<acme:submit name="save" code="master.page.save"/>
	
	<acme:cancel url="newspaper/list.do" code="master.page.cancel"/>
	
	</form:form>