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
<td class ="left-display"> <strong> <spring:message code="volume.title" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${volume.title}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="volume.description" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${volume.description}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="volume.year" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${volume.year}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="volume.user" /> : </strong> </td>
<td class="right-display"> <jstl:out value ="${volume.user.name}" /> &nbsp; </td>
</tr>

</table>

<display:table name="newspapers" id="row" requestURI="newspaper${uri}/list.do" pagesize="5" class="displaytag">

	<!-- title -->
	<spring:message code="newspaper.title"
		var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		 />
		
	
	
	<security:authorize access="!(isAnonymous())">
	
	<!-- publicationDate -->
	<spring:message code="newspaper.publicationDate"
		var="publicationDateHeader" />
	<display:column property="publicationDate" title="${publicationDateHeader}"
		 />		
	
	
	
	<!-- description -->
	
	
	<spring:message code="newspaper.description"
		var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		 />

		
	<!-- pictureURL -->
	<spring:message code="newspaper.pictureURL" var="pictureHeader" />
	<spring:message code="newspaper.pictureError" var="pictureError" />

	<display:column title="${pictureHeader}"  > 
	<img src="${row.pictureURL}" alt="${pictureError}"  width="200" height="200"> 
	</display:column>
	</security:authorize>


	<!-- isPrivate -->
		<spring:message code="newspaper.isPrivate"
		var="isPrivateHeader" />
	<display:column title="${isPrivateHeader}"> 
	<security:authorize access="hasRole('USER')">

	<jstl:if test="${principal.newspapers.contains(row)}">
	
	<jstl:choose>
		<jstl:when test="${row.isPrivate == true}">
			<a href="newspaper/user/private.do?newspaperId=${row.id}"> <spring:message
			code="newspaper.makePublic" />
		</a>
		</jstl:when>
		 
		<jstl:otherwise>
			<a href="newspaper/user/private.do?newspaperId=${row.id}"> <spring:message
			code="newspaper.makePrivate" />
		</a>
		</jstl:otherwise>
		</jstl:choose>
	</jstl:if>
	</security:authorize>
	<jstl:choose>
			<jstl:when test="${row.isPrivate == true}">
			<img class="alarmImg" src="images/lock.png" width="30" height="auto"/>
		</jstl:when>
		
		<jstl:otherwise>
			<img class="alarmImg" src="images/open.png" width="30" height="auto"/>
		</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
		
	<!-- articles -->
	<spring:message code="newspaper.articles"
  	var="articles" />
	<display:column title="${articles}">
	<ul>
		<jstl:forEach items="${row.articles}" var="article"> 
			 <li>
			 <jstl:choose>
				<jstl:when test="${suscrito == true|| row.isPrivate == false}">
					<a href="article${uri}/display.do?articleId=${article.id}">
						<jstl:out value="${article.title}"/>
					</a>
				</jstl:when>
			<jstl:otherwise>
			<jstl:out value="${article.title}"/>
			</jstl:otherwise>
			</jstl:choose>
			 </li>
		</jstl:forEach>
	</ul>
	</display:column>
	

	<!-- Publisher -->
	<spring:message code="newspaper.user"
		var="userHeader" />
	<display:column title="${userHeader}"  > 
		<a href="user${uri}/display.do?userId=${row.user.id}">
			<jstl:out value="${row.user.name} ${row.user.surname}"/>
		</a>
	</display:column>
	
	
	<display:column>
		<a href="newspaper${uri}/display.do?newspaperId=${row.id}"> <spring:message
			code="newspaper.display" />
		</a>
	</display:column>
	
<security:authorize access="hasRole('ADMIN')">
		<display:column>
		<a href="newspaper/admin/delete.do?newspaperId=${row.id}"> <spring:message
			code="master.page.delete" />
		</a>
	</display:column>
</security:authorize>


<security:authorize access="hasRole('CUSTOMER')">

<jstl:set var="subscrito" value="${false}"/>
<jstl:forEach var="subscription" items="${principal.subscriptions}">
<jstl:if test="${subscription.newspaper.id == row.id}">
<jstl:set var="subscrito" value="${true}"/>
</jstl:if>
</jstl:forEach>

		<jsp:useBean id="now" class="java.util.Date"/>
		<jstl:if test="${!(subscrito == true) and (row.isPrivate == true) and (row.publicationDate < now)}">
		<display:column>
		<a href="subscription/customer/create.do?newspaperId=${row.id}"> <spring:message
			code="article.subscribe" />
		</a>
		
		</display:column>
		</jstl:if>
	
</security:authorize>
</display:table>