
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

<display:table class="displaytag" name="advertisements"  requestURI="advertisement/agent/list.do" id="row">


	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<spring:message code ="advertisement.delete" var="delete"/>
			<spring:message code ="advertisement.confirmdelete" var="confirmDelete"/>
			<a href="admin/admin/deleteAdvert.do?advertisementId=${row.id}" onclick="return confirm('${confirmDelete}')"><jstl:out value="delete"/></a>
		</display:column>
	</security:authorize>
	
	
	<spring:message code="advertisement.title" var="titleHeader" />
	<display:column title="${titleHeader}"><jstl:out value="${row.title}"></jstl:out></display:column>
	
	<spring:message code="advertisement.urlBanner" var="pictureHeader" />
	<spring:message code="advertisement.altPicture" var="altPic" />
	<display:column title="${pictureHeader}"><img src ="${row.bannerURL}" alt="altPic"></display:column>
	
	<spring:message code="advertisement.newspaper" var="newspaperHeader" />
	<display:column title="${newspaperHeader}">
		<jstl:forEach items="${row.newspapers}" var="newspaper">
			<ul>
				<li>
					<a href="newspaper/admin/display.do?newspaperId=${newspaper.id}">
						<jstl:out value="${newspaper.title}"/>
					</a>
				</li>
			</ul>
		</jstl:forEach>
	</display:column>
	
	
	<spring:message code="advertisement.agent" var="agentHeader" />
	<display:column title="${agentHeader}"><jstl:out value="${row.agent.userAccount.username}"></jstl:out></display:column>
	
</display:table>

<script>
$(document).ready( function () {
    $('#row').DataTable();
} );

$('#row').dataTable( {
  "lengthMenu": [ 5, 10, 25, 50, 100 ]
} );
</script>
