

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>




<display:table class="displaytag" 
	name="volumes" requestURI="volume/list.do" id="row">
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			<a href="volume/user/edit.do?volumeId=${row.id}"><spring:message code ="volume.edit"/></a>
		</display:column>
	</security:authorize>

	
	<!-- title -->
	<spring:message code="volume.title"
		var="titleHeader" />
	<display:column property="title" title="${titleHeader}"/>
		
	
	<!-- description -->
	<spring:message code="volume.description"
		var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"/>
	
	
	<!-- Year -->
	<spring:message code="volume.year"
		var="yearHeader" />
	<display:column property="year" title="${yearHeader}" />		
	
	
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
	<display:column title="${userHeader}"  > 
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

<jstl:set var="subscrito" value="${true}"/>
<jstl:forEach var="newspaper" items="${row.newspapers}">
<jstl:if test="${not subscribed.contains(newspaper) and (newspaper.isPrivate == true)}">
<jstl:set var="subscrito" value="${false}"/>
</jstl:if>
</jstl:forEach>

		<display:column>
		<jstl:if test="${(subscrito == false)}">
		<a href="subscription/customer/createVolume.do?volumeId=${row.id}"> <spring:message
			code="article.subscribe" />
		</a>
		</jstl:if>
		
	</display:column>
</security:authorize>


		
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

<security:authorize access="hasRole('USER')">
<a href="volume/user/create.do"> <spring:message
			code="volume.create" /> </a>
</security:authorize>


