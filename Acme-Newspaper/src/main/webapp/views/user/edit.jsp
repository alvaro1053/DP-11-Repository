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


<form:form action="${requestURI}" modelAttribute="editActorForm">
	
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<acme:textbox code="user.name" path="name"/>
	<br />
	
	<acme:textbox code="user.surname" path="surname"/>
	<br />
	
	<acme:textbox code="user.email" path="email"/>
	<br />
	
	<!-- Input del form con el formato antiguo para permitir el pattern -->
	<form:label path="phone">
		<spring:message code="user.phone" />
	</form:label>	
	<form:input path="phone" pattern="+?([0-9]{9})?"/>	
	<form:errors path="phone" cssClass="error" />
	<br />
	<br />
	<acme:textbox code="user.address" path="address"/>
	<br />		

	<input type="submit" name="save" id="save"
		value="<spring:message code="user.save" />" />&nbsp; 
		
	<security:authorize access="hasRole('USER')">
		<input type="button" name="cancel"
			value="<spring:message code="user.cancel" />"
			onclick="javascript: relativeRedir('user/user/displayUserProfile.do');" />
		<br />
	</security:authorize>
	
	<security:authorize access="hasRole('AGENT')">
		<input type="button" name="cancel"
			value="<spring:message code="user.cancel" />"
			onclick="javascript: relativeRedir('agent/agent/displayProfile.do');" />
		<br />
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<input type="button" name="cancel"
			value="<spring:message code="user.cancel" />"
			onclick="javascript: relativeRedir('admin/admin/displayProfile.do');" />
		<br />
	</security:authorize>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<input type="button" name="cancel"
			value="<spring:message code="user.cancel" />"
			onclick="javascript: relativeRedir('customer/customer/displayProfile.do');" />
		<br />
	</security:authorize>
	
<jstl:out value="${message}"/>
</form:form>
