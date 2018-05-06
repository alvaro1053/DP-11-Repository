
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



<!-- Buscar  -->

<form action="article${uri}/list.do" method="get">
	<spring:message code="article.search.placeholder" var="placeholder" />
	<input name="filter" placeholder="${placeholder}"/>
	<br />
<spring:message code="article.search" var="search" />
	<input type="submit"
		value="${search}" />

</form>
<br>


<!-- Listing grid -->

<display:table class="displaytag" name="articles"  requestURI="article${uri}/list.do" id="row">


	<security:authorize access="hasRole('USER')">
		
			<jstl:if test="${row.isDraft == true}">
				<display:column>
					<a href="article/user/edit.do?articleId=${row.id}"><spring:message code ="article.edit"/></a>
				</display:column>
			</jstl:if>
	</security:authorize>
	
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="article.confirm" var="confirmArticle"  />
		<display:column>
				<a href="article/admin/delete.do?articleId=${row.id}" onclick="return confirm('${confirmArticle}')"><spring:message code ="article.delete" /></a>
		</display:column>
	</security:authorize>
	
	
	<spring:message code="article.title" var="titleHeader" />
	<display:column title="${titleHeader}">
	<jsp:useBean id="now" class="java.util.Date"/>
		
		<jstl:choose>
			<jstl:when test="${row.newspaper.isPrivate == false && row.newspaper.publicationDate lt now}">
				<a href="article/display.do?articleId=${row.id}"><jstl:out value="${row.title}"></jstl:out></a>
			</jstl:when>
			<jstl:otherwise>
			<jstl:out value="${row.title}"/>
			</jstl:otherwise>
			</jstl:choose>
	</display:column>
	
	<spring:message code="article.summary" var="summary" />
	<display:column property="summary" title="${summary}"/>
	
	<spring:message code="article.moment" var="moment" />
	<spring:message code="article.moment.format" var="dateFormat" />
	<display:column property="moment" format="{0,date,${dateFormat}}" title="${moment}"/>
	
	<spring:message code="article.photosURL.failed" var="failed" />
	<spring:message code="article.photosURL" var="picture" />
	<display:column  title="${picture}"> 
		<jstl:forEach var="picture" items="${row.photosURL}">
			<jstl:if test="${not empty picture}">
				<img src="${picture}" alt= "${failed}" height="150" width=auto />
			</jstl:if> 
		</jstl:forEach>	
	</display:column>


</display:table>

<spring:message code="datatables.locale.lang" var="tableLang"/>
<spring:message code="datatables.moment.format" var="tableFormatMoment"/>
<script>
$(document).ready( function () {	
	$.fn.dataTable.moment('${tableFormatMoment}');
	
    $('#row').dataTable( {
    	"language": {
        	"url": '${tableLang}'
    	},
	    "searching": false,
		"lengthMenu": [ 5, 10, 25, 50, 100 ]
    } );
} );
</script>

	<security:authorize access="hasRole('USER')">
		<a href="article/user/create.do"> <spring:message code="article.create" /> </a>
	</security:authorize>