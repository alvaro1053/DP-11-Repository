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


<form:form action="advertisement/agent/edit.do" modelAttribute="advertisementForm">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="advertisement.title" path="title"/>
	
	<acme:textbox code="advertisement.urlBanner" path="bannerURL"/>
	
	<acme:textbox code="advertisement.urlTarget" path="targetPageURL"/>
	
	<fieldset>
		<legend><spring:message code="advertisement.creditCard"/></legend>
		
		<acme:textbox code="advertisement.creditCard.holder" path="creditCard.holderName"/>
		<acme:textbox code="advertisement.creditCard.brand" path="creditCard.brandName"/>

		<acme:textbox code="advertisement.creditCard.number" path="creditCard.number"/>

		<acme:textbox code="advertisement.creditCard.expirationMonth" path="creditCard.expirationMonth"/>

		<acme:textbox code="advertisement.creditCard.expirationYear" path="creditCard.expirationYear"/>

		<acme:textbox code="advertisement.creditCard.CVV" path="creditCard.CVV"/>
	
	</fieldset>
	
	<acme:selectMultiple items="${newspapers}" itemLabel="title" code="advertisement.newspaper" path="newspapers"/>

	<acme:submit name="save" code="advertisement.save"/>
	<acme:cancel url="/advertisement/agent/listPlacedAds.do" code="advertisement.cancel"/>
</form:form>

