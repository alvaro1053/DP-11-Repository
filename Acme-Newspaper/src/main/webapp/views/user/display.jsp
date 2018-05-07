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


<table class="displayStyle" >


<tr>
<td class ="left-display"> <strong> <spring:message code="user.username" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${user.userAccount.username}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="user.name" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${user.name}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="user.surname" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${user.surname}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="user.email" /> : </strong> </td>
<td class="right-display"> <jstl:out value ="${user.email}" /> &nbsp; </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="user.phone" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${user.phone}" /> &nbsp; </td>
</tr>

<security:authorize access="hasRole('USER')">
<jstl:if test="${principal.id == user.id }">
<tr>
<td class ="left-display"> <strong> <spring:message code="user.address" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${user.postalAddress}" /> &nbsp; </td>
</tr>
</jstl:if>
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
<tr>
<td class ="left-display"> <strong> <spring:message code="user.address" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${user.postalAddress}" /> &nbsp; </td>
</tr>

</security:authorize>
 
<spring:message code="user.articles" var="showArticle"/>

<tr>
<td class ="left-display"> <strong> <spring:message code="user.articles" /> : </strong> </td>
<td class="right-display"> 

<jstl:choose>
<jstl:when test="${not empty articles}"> 
<ul>
<jstl:forEach items="${articles}" var="article">
<li> <jstl:out value="${article.title}"/> &nbsp; (<a href="article${uri}/display.do?articleId=${article.id}"> ${showArticle} </a>) </li>
</jstl:forEach>
</ul> 
</jstl:when>
<jstl:otherwise>

<spring:message code="user.articles.empty" />

</jstl:otherwise>
</jstl:choose>

</td>
</tr>


</table>

<display:table name="chirps" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<spring:message code="user.chirp.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
	
	<spring:message code="user.chirp.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="true" />
	
	<spring:message code = "user.chirp.moment" var = "momentHeader" />
	<spring:message code = "user.chirp.format" var = "format" />
	<display:column property = "moment" format="${format}" title ="${momentHeader}" sortable="true"/>
</display:table>

