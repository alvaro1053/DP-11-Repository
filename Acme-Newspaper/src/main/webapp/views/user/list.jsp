

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->
<jstl:if test="${followingTitle}">
<h3><spring:message code ="user.followingTitle"/></h3>
</jstl:if>
<jstl:if test="${followersTitle}">
<h3><spring:message code ="user.followersTitle"/></h3>
</jstl:if>




<display:table class="displaytag"
	name="users" requestURI="user${uri}/list.do" id="row">
	
		<!-- Follow -->
	<security:authorize access="hasRole('USER')">
	<spring:message code="user.follow" var="followHeader" />
	<spring:message code="user.unfollow" var="unfollowHeader" />
		<jstl:choose>
			<jstl:when test="${principal.follows.contains(row)}">
				<display:column title="${followHeader}">
					<a href = "user/user/unfollow.do?userId=${row.id}">${unfollowHeader}</a>
				</display:column>	
			</jstl:when>
			<jstl:otherwise>
				<display:column title="${followHeader}">
					<a href = "user/user/follow.do?userId=${row.id}">${followHeader}</a>
				</display:column>
			</jstl:otherwise>
			
		</jstl:choose>
	</security:authorize>
	
	<!-- userAccount -->
	<spring:message code="user.username"
		var="usernameHeader" />
	<display:column property="userAccount.username" title="${usernameHeader}"/>
		
		
	<!-- name -->
	<spring:message code="user.name"
		var="nameHeader" />
	<display:column property="name" title="${nameHeader}"
		  />

	<!-- surname -->
	<spring:message code="user.surname"
		var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}"
		  />
		
	<!-- email -->
	<spring:message code="user.email"
		var="emailHeader" />
	<display:column property="email" title="${emailHeader}"
		  />
		
	<!-- phone -->
	<spring:message code="user.phone"
		var="phoneHeader" />
	<display:column property="phone" title="${phoneHeader}"
		  />
		
	<!-- address -->
	<spring:message code="user.address"
			var="addressHeader" />
	<security:authorize access="hasRole('USER')">
			<display:column title="${addressHeader}"
			 >
		<jstl:if test="${principal==row}">
			<jstl:out value ="${row.postalAddress}"/>
		</jstl:if>
			</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
			<display:column title="${addressHeader}"
			 >
			<jstl:out value ="${row.postalAddress}"/>
			</display:column>
	</security:authorize>

	
		
	<display:column>
		<a href="user${uri}/display.do?userId=${row.id}"> <spring:message
			code="user.display" />
		</a>
	</display:column>
		
</display:table>

<spring:message code="datatables.locale.lang" var="tableLang"/>
<script>
$(document).ready( function () {
    $('#row').dataTable( {
    	"language": {
        	"url": '${tableLang}'
    	},
		"lengthMenu": [ 5, 10, 25, 50, 100 ]
    } );
} );
</script>