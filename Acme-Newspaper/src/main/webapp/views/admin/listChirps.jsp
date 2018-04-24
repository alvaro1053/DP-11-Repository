
<%--
 * list.jsp
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


<!-- Listing grid -->



<display:table pagesize="5" class="displaytag" name="chirps"  requestURI="admin/admin/listChirps.do" id="row">
	
	
	<display:column>
		<spring:message code = "chirp.confirm.delete" var = "confirmDeleteChirp" />
		<a href = "admin/admin/delete.do?chirpId=${row.id}" onclick="return confirm('${confirmDeleteChirp}')"><spring:message code= "chirp.delete"/></a>
	</display:column>
	
	<spring:message code="chirp.title" var="title" />
	<display:column property="title" title="${title}"/>
	
	<spring:message code="chirp.description" var="description" />
	<display:column property="description" title="${description}"/>
	
</display:table>