
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="chirp/user/create.do" modelAttribute="chirpForm" >


	<form:hidden path="id"/>
	<form:hidden path="version"/>

	
	<acme:textbox code="chirp.title" path="title"/>
	<br />
	
	<acme:textarea code="chirp.description" path="description"/>
	<br />
	
	<input type="submit" name="save" id="save"
		value="<spring:message code="chirp.save" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="chirp.cancel" />"
		onclick="javascript: relativeRedir('');" />
	<br />
<jstl:out value="${message}"/>
</form:form>






