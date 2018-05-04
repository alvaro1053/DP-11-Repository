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
<td class ="left-display"> <strong> <spring:message code="followUp.title" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${followUp.title}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="followUp.summary" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${followUp.summary}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="followUp.text" /> : </strong> </td>
<td class="right-display">  <jstl:out value = "${followUp.text}"/> &nbsp;  </td>
</tr>

<tr>
<td class ="left-display"> <strong> <spring:message code="followUp.article" /> : </strong> </td>
<td class="right-display">  <a href="article/display.do?articleId=${followUp.article.id}"><jstl:out value="${followUp.article.title}"/></a> &nbsp; </td>
</tr>

<tr>
<spring:message code = "followUp.date.format" var = "format"/>
<td class ="left-display"> <strong> <spring:message code="followUp.publicationDate" /> : </strong> </td>
<td class="right-display">  <fmt:formatDate value="${followUp.publicationDate}" pattern="${format}"/> &nbsp; </td>
</tr>


<tr>
<td class ="left-display"> <strong> <spring:message code="followUp.photosURL" /> : </strong> </td>
<td class="right-display"> 

<jstl:choose>
<jstl:when test="${not empty followUp.photosURL}"> 
<ul>
<jstl:forEach items="${followUp.photosURL}" var="photo">
<img src="<jstl:out value = "${photo}" />"/> &nbsp;
</jstl:forEach>
</ul> 
</jstl:when>
<jstl:otherwise>

<spring:message code="followUp.photosURL.empty" />

</jstl:otherwise>
</jstl:choose>

</td>
</tr>

</table>