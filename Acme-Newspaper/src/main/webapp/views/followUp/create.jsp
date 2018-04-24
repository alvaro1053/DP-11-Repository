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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<script>
    $(document).ready(function() {
        $("#list1").dynamiclist();
    });
</script>
<form:form action="followUp/user/create.do" modelAttribute="followUp" >

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="followUp.article.title" path="title"/>
	<br />
	
	<acme:textarea code="followUp.article.summary" path="summary"/>
	<br />
	
	<acme:textarea code="followUp.article.text" path="text"/>
	<br />

	<acme:textbox code="followUp.article.publicationDate" path="publicationDate"/>
	<br />
	
	<acme:select items="${articles}" itemLabel="title" code="followUp.article.name" path="article"/>
	<br/>
	
	<fieldset>
		<legend> <spring:message code="followUp.article.photosURL"/> :  </legend>
		
	    <div id="list1">
		<jstl:choose> 
			<jstl:when test="${empty photosURL}">
				<div class="list-item">
					 <form:input path="photosURL[0]"/>
					 <a href="#" class="list-remove" onclick="event.preventDefault();"> <spring:message code="followUp.removePhoto"/> </a>
				 </div>
				 
			</jstl:when>
			<jstl:otherwise>
				<jstl:forEach items="${photosURL}" var="word" varStatus="j" begin="0">
	    			<div class="list-item">
	      				<form:input path="photosURL[${j.index}]"/>
	      				<a href= "#" class="list-remove" onclick="event.preventDefault();"> <spring:message code="followUp.removePhoto"/> </a>
		    		</div>
	
	        	</jstl:forEach>
			</jstl:otherwise>
		</jstl:choose>
	    <a href="#" class="list-add" onclick="event.preventDefault();"> <spring:message code="followUp.addPhoto"/> </a>
	    </div>
	    
		<form:errors cssClass="error" path="photosURL" />

	</fieldset>
	
	<br/>

	<input type="submit" name="save" id="save"
		value="<spring:message code="followUp.save" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="followUp.cancel" />"
		onclick="javascript: relativeRedir('followUp/user/list.do');" />
	<br />
	<jstl:out value="${message}"/>
</form:form>