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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="now" class="java.util.Date"/>
<jstl:choose>
<jstl:when test="${newspaper.publicationDate < now}">

<table class="displayStyle" >

<tr>
<td class ="left-display"> <strong> <spring:message code="newspaper.title" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${newspaper.title}"/> &nbsp;  </td>
</tr>


<jstl:if test="${suscrito == true || newspaper.isPrivate == false}"> 


<tr>
<td class ="left-display"> <strong> <spring:message code="newspaper.publicationDate" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${newspaper.publicationDate}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="newspaper.description" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${newspaper.description}"/> &nbsp;  </td>
</tr>


<tr>
<spring:message code="newspaper.pictureError" var="pictureError" />
<td class ="left-display"> <strong> <spring:message code="newspaper.pictureURL" /> : </strong> </td>
<td class="right-display">  <img src="${newspaper.pictureURL}" width="auto" height="200" alt ="${pictureError}"> &nbsp; </td>
</tr>


<tr>
<td class ="left-display"> <strong> <spring:message code="newspaper.user" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${newspaper.user.name}" /> &nbsp; </td>
</tr>

</jstl:if>

<jstl:choose>
<jstl:when test="${not empty newspaper.articles}"> 


<display:table name="articles" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">
	
	<spring:message code="newspaper.article.title" var="titleHeader" />
	<display:column title="${titleHeader}" sortable="true" >
	<jstl:choose>
		<jstl:when test="${suscrito == true || newspaper.isPrivate == false}">
		<a href="article${uri}/display.do?articleId=${row.id}">
			<jstl:out value="${row.title}"/>
		</a>
		</jstl:when>
		<jstl:otherwise>
			<jstl:out value="${row.title}"/>
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
	
	
	<spring:message code="article.user" var="userHeader" />
	<display:column title="${userHeader}" sortable="true" >
	<jstl:choose>
		<jstl:when test="${suscrito == true || newspaper.isPrivate == false}">
		<a href="user/display.do?userId=${row.user.id}">
			<jstl:out value="${row.user.name}"/>
		</a>
		</jstl:when>
		<jstl:otherwise>
			<jstl:out value="${row.user.name}"/>
		</jstl:otherwise>
	</jstl:choose>
	</display:column>
	
	<spring:message code="article.summary" var = "summaryHeader" />
	<display:column property="summary" title ="${summaryHeader}" sortable="true"/>
</display:table>

</jstl:when>
<jstl:otherwise>

<spring:message code="newspaper.articles.empty" />

</jstl:otherwise>
</jstl:choose>


</table>

<jstl:if test="${advert != null}">
	<spring:message code ="newspaper.imageBannerNotFound" var = "imageBannerNotFound"></spring:message>
	<a href="${advert.targetPageURL}">
		<img src="${advert.bannerURL}" alt="${imageBanner}">
	</a>
</jstl:if>

 </jstl:when>
<jstl:otherwise>
<spring:message code="newspaper.notPublished" />
</jstl:otherwise>
</jstl:choose>

