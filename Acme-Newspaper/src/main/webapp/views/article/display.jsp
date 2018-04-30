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
<td class ="left-display"> <strong> <spring:message code="article.title" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${article.title}"/> &nbsp;  </td>
</tr>

<tr>
<spring:message code="article.moment.format" var = "format"/>
<td class ="left-display"> <strong> <spring:message code="article.moment" /> : </strong> </td>
<td class="right-display">  <fmt:formatDate value = "${article.moment}" pattern="${format}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="article.summary" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${article.summary}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="article.body" /> : </strong> </td>
<td class="right-display"> <jstl:out value ="${article.body}" /> &nbsp; </td>
</tr>


<tr>
<td class ="left-display"> <strong> <spring:message code="article.photosURL" /> : </strong> </td>
<td class="right-display"> 

<jstl:choose>
<jstl:when test="${not empty article.photosURL}"> 
<ul>
<jstl:forEach items="${article.photosURL}" var="photoURL">
	<img src="${photoURL}"  width="auto" height="200"> &nbsp;
</jstl:forEach>
</ul> 
</jstl:when>
<jstl:otherwise>

<spring:message code="article.photosURL.empty" />

</jstl:otherwise>
</jstl:choose>

</td>
</tr>



<tr>
<td class ="left-display"> <strong> <spring:message code="article.isDraft" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${article.isDraft}" /> &nbsp; </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="article.newspaper" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${article.newspaper.title}" /> &nbsp; </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="article.user" /> : </strong> </td>
<td class="right-display">  <jstl:out value="${article.user.name}" /> &nbsp; </td>
</tr>


<spring:message code="article.followUp" var="showFollowUp"/>

<tr>
<td class ="left-display"> <strong> <spring:message code="article.followUps" /> : </strong> </td>
<td class="right-display"> 

<jstl:choose>
<jstl:when test="${not empty article.followUps}"> 
<ul>
<jstl:forEach items="${article.followUps}" var="followUp">
<li> <jstl:out value="${followUp.title}"/> &nbsp; (<a href="followUp${uri}/display.do?followUpId=${followUp.id}"> ${showFollowUp} </a>) </li>
</jstl:forEach>
</ul> 
</jstl:when>
<jstl:otherwise>

<spring:message code="article.followUps.empty" />

</jstl:otherwise>
</jstl:choose>

</td>
</tr>


</table>


<jstl:if test="${advert != null}">
	<spring:message code ="article.imageBannerNotFound" var = "imageBannerNotFound"></spring:message>
	<a href="${advert.targetPageURL}">
		<img src="${advert.bannerURL}" alt="${imageBanner}">
	</a>
</jstl:if>