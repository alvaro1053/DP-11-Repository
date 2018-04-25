

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>




<display:table pagesize="5" class="displaytag" 
	name="volumes" requestURI="volume${uri}/list.do" id="row">
	

	
	<!-- title -->
	<spring:message code="volume.title"
		var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="true" />
		
	
	<!-- description -->
	<spring:message code="volume.description"
		var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />
	
	
	<!-- Year -->
	<spring:message code="volume.year"
		var="yearHeader" />
	<display:column property="year" title="${yearHeader}"
		sortable="true" />		
	
	
	<!-- newspapers -->
	<spring:message code="volume.newspapers"
  	var="newspapers" />
	<display:column title="${newspapers}">
	<ul>
		<jstl:forEach items="${row.newspapers}" var="newspaper"> 
			 <li>
				<a href="newspaper/display.do?newspaperId=${newspaper.id}">
					<jstl:out value="${newspaper.title}"/>
				</a>
			 </li>
		</jstl:forEach>
	</ul>
	</display:column>
	

	<!-- Publisher -->
	<spring:message code="volume.user"
		var="userHeader" />
	<display:column title="${userHeader}" sortable="true" > 
		<a href="user${uri}/display.do?userId=${row.user.id}">
			<jstl:out value="${row.user.name} ${row.user.surname}"/>
		</a>
	</display:column>
	
	
	<display:column>
		<a href="volume${uri}/display.do?volumeId=${row.id}"> <spring:message
			code="volume.display" />
		</a>
	</display:column>


<security:authorize access="hasRole('CUSTOMER')">

<jstl:set var="subscrito" value="${false}"/>
<jstl:forEach var="subscription" items="${principal.subscriptions}">
<jstl:if test="${subscription.newspaper.id == row.id}">
<jstl:set var="subscrito" value="${true}"/>
</jstl:if>
</jstl:forEach>

		<display:column>
		<jstl:if test="${!(subscrito == true)}">
		<a href="subscription/customer/create.do?volumeId=${row.id}"> <spring:message
			code="article.subscribe" />
		</a>
		</jstl:if>
		
	</display:column>
</security:authorize>


		
</display:table>

<security:authorize access="hasRole('USER')">
<a href="volume/user/create.do"> <spring:message
			code="volume.create" /> </a>
</security:authorize>


