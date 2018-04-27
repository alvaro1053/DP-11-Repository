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

	<form:form action="volume/user/edit.do" modelAttribute="volumeForm">
	<form:hidden path="id" />
	<form:hidden path="version" /> 
	
	
	<acme:textbox code="volume.title" path="title"/>
	
	<acme:textarea code="volume.description" path="description"/>
	
	<acme:textbox code="volume.year" path="year"/>
	
	<acme:selectMultiple items="${newspapers}" itemLabel="title" code="volume.newspapers" path="newspapers"/>
	
	<acme:submit name="save" code="master.page.save"/>
	
	<acme:cancel url="volume/list.do" code="master.page.cancel"/>
	
	</form:form>