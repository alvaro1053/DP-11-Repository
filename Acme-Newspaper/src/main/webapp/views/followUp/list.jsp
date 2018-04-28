
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

<display:table class="displaytag" name="articles"  requestURI="followUp/user/list.do" id="row">
	
	<spring:message code="followUp.article.title" var="titleHeader" />
	<display:column title="${titleHeader}"><a href="article/display.do?articleId=${row.id}"><jstl:out value="${row.title}"></jstl:out></a></display:column>
	
	<spring:message code="followUp.article.summary" var="summary" />
	<display:column property="summary" title="${summary}"/>
	
	<spring:message code="followUp.article.moment" var="moment" />
	<spring:message code="followUp.date.format" var="dateFormat" />
	<display:column property="moment" format="{0,date,${dateFormat}}" title="${moment}"/>
	
	<spring:message code="followUp.article.photosURL" var="picture" />
	<display:column  title="${picture}"> <jstl:forEach var="picture" items="${row.photosURL}"><img src="${picture}" height="150" width=auto /> </jstl:forEach></display:column>

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
		"lengthMenu": [ 5, 10, 25, 50, 100 ]
    } );
} );
</script>

<a href = "followUp/user/create.do"><spring:message code="followUp.create"/></a>