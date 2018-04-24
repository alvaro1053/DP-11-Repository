
<%--
 * list.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<script>
    $(document).ready(function() {
        $("#list1").dynamiclist();
    });
</script>
<form:form action="customisation/admin/edit.do" modelAttribute="customisation">

	<fieldset>

	
	<legend> <spring:message code="customisation.tabooWords"/> :  </legend>
	
    <div id="list1">
    <jstl:choose> 
		<jstl:when test="${empty customisation.tabooWords}">
			<div class="list-item">
				 <form:input path="tabooWords[0]"/>
				 <a href="#" class="list-remove" onclick="event.preventDefault();"> <spring:message code="customisation.removeWord"/> </a>
			 </div>
			 
		</jstl:when>
		<jstl:otherwise>
			<jstl:forEach items="${customisation.tabooWords}" var="word" varStatus="j" begin="0">
    			<div class="list-item">
      				<form:input path="tabooWords[${j.index}]"/>
      				<a href= "#" class="list-remove" onclick="event.preventDefault();"> <spring:message code="customisation.removeWord"/> </a>
	    		</div>

        	</jstl:forEach>
		</jstl:otherwise>
	</jstl:choose>
     
    <a href="#" class="list-add" onclick="event.preventDefault();"> <spring:message code="customisation.addWord"/> </a>
    </div>
    
	
	<form:errors cssClass="error" path="tabooWords" />

	</fieldset>
	
	
	<input type="submit" name="save" id="save"
		value="<spring:message code="customisation.save" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="customisation.cancel" />"
		onclick="javascript: relativeRedir('');" />
	<br />
<jstl:out value="${message}"/>
	

</form:form>