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
<td class ="left-display"> <strong> <spring:message code="admin.username" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${admin.userAccount.username}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="admin.name" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${admin.name}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="admin.surname" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${admin.surname}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="admin.email" /> : </strong> </td>
<td class="right-display"> <jstl:out value ="${admin.email}" /> &nbsp; </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="admin.phone" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${admin.phone}" /> &nbsp; </td>
</tr>

<security:authorize access="hasRole('USER') || hasRole('CUSTOMER') || hasRole('ADMIN') || hasRole('AGENT')">
<jstl:if test="${principal.id == admin.id }">
	<tr>
	<td class ="left-display"> <strong> <spring:message code="admin.address" /> : </strong> </td>
	<td class="right-display">  <jstl:out value="${admin.postalAddress}" /> &nbsp; </td>
	</tr>
	</jstl:if>
</security:authorize>

</table>

