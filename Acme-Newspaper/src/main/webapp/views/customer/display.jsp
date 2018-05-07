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
<td class ="left-display"> <strong> <spring:message code="customer.username" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${customer.userAccount.username}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="customer.name" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${customer.name}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="customer.surname" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${customer.surname}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="customer.email" /> : </strong> </td>
<td class="right-display"> <jstl:out value ="${customer.email}" /> &nbsp; </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="customer.phone" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${customer.phone}" /> &nbsp; </td>
</tr>

<security:authorize access="hasRole('USER') || hasRole('CUSTOMER') || hasRole('ADMIN')">
<jstl:if test="${principal.id == customer.id }">
	<tr>
	<td class ="left-display"> <strong> <spring:message code="customer.address" /> : </strong> </td>
	<td class="right-display">  <jstl:out value="${customer.postalAddress}" /> &nbsp; </td>
	</tr>
	</jstl:if>
</security:authorize>

</table>

