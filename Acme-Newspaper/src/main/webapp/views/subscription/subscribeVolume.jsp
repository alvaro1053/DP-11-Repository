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

<form:form action="subscription/customer/editVolume.do" modelAttribute="subscriptionForm">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="volume" />
	
<fieldset>
 <legend><spring:message code="subscription.creditCard" /></legend>

<acme:textbox code="subscription.creditCard.holder" path="creditCard.holderName"/>
	<br />

<acme:textbox code="subscription.creditCard.brand" path="creditCard.brandName"/>
	<br />

<acme:textbox code="subscription.creditCard.number" path="creditCard.number"/>
	<br />

<acme:textbox code="subscription.creditCard.expirationMonth" path="creditCard.expirationMonth"/>
	<br />

<acme:textbox code="subscription.creditCard.expirationYear" path="creditCard.expirationYear"/>
	<br />

<acme:textbox code="subscription.creditCard.CVV" path="creditCard.CVV"/>
	<br />


</fieldset>
	



	<spring:message code="subscription.save" var="savesubscription"  />
	<spring:message code="subscription.delete" var="deletesubscription"  />
	<spring:message code="subscription.cancel" var="cancelsubscription"  />
	<spring:message code="subscription.confirm" var="confirmsubscription"  />
	
	
	<input type="submit" name="save"
		value="${savesubscription}" />&nbsp; 

	<input type="button" name="cancel"
		value="${cancelsubscription}"
		onclick="javascript: relativeRedir('volume/list.do');" />
	<br />
</form:form>