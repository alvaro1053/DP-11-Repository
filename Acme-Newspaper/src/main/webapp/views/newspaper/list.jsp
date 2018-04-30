

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>



<jstl:if test="${location != null}">
	<h3><spring:message code="${location}"></spring:message></h3>
</jstl:if>

<br>
<form action="newspaper${uri}/list.do" method="get">
	<spring:message code="newspaper.search.placeholder" var="placeholder" />
	<input name="filter" placeholder="${placeholder}"/>
	<br />
<spring:message code="newspaper.search" var="search" />
	<input type="submit"
		value="${search}" />

</form>
<br>


<display:table class="displaytag" 
	name="newspapers" requestURI="newspaper${uri}/list.do" id="row">
	

	
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
				<jstl:when test="${suscrito == true}">
					<a href="article/display.do?articleId=${article.id}">
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
	
	
<security:authorize access="hasRole('USER')">
		<display:column>
		<jsp:useBean id="now" class="java.util.Date"/>
			<jstl:if test="${principal.newspapers.contains(row) && row.publicationDate > now}">
		<a href="newspaper/user/publish.do?newspaperId=${row.id}"> <spring:message
			code="newspaper.publish" />
		</a>
		</jstl:if>
	</display:column>
</security:authorize>
	
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

		<display:column>
		<jstl:if test="${!(subscrito == true) and (row.isPrivate == true)}">
		<a href="subscription/customer/create.do?newspaperId=${row.id}"> <spring:message
			code="article.subscribe" />
		</a>
		</jstl:if>
		
	</display:column>
</security:authorize>
		
</display:table>

<security:authorize access="hasRole('AGENT')">
		<h3>
			<a href="advertisement/agent/create.do">
				<spring:message code="advertisement.create" />
			</a>
		</h3>
</security:authorize>

<security:authorize access="hasRole('USER')">
<a href="newspaper/user/create.do"> <spring:message
			code="newspaper.create" /> </a>
</security:authorize>

<script>
$(document).ready( function () {
    $('#row').DataTable();
} );

$('#row').dataTable( {
  "searching": false,
  "lengthMenu": [ 5, 10, 25, 50, 100 ]
} );
</script>




