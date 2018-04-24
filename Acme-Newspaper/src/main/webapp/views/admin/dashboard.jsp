

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<security:authorize access="hasRole('ADMIN')">
	
	<h1 id="h1Statistics"><spring:message code="administrator.statistics" /></h1>
	<br/>
	<!-- 1 -->
	<table class="displayStyle">

		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.deviation" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.averageAndDesviationOfNewspapersPerUser" /></td>
			<td>${AverageNewspapersPerUser }</td>
			<td>${StandardDesviationNewspapersPerUser }</td>
		</tr>
	</table>
	
	<!-- 2 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.deviation" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.averageAndDesviationOfArticlesPerUser" /></td>
			<td>${AverageArticlesPerUser }</td>
			<td>${StandardDesviationArticlesPerUser }</td>
		</tr>
	</table>
	
	<!-- 3 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.deviation" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.averageAndDesviationOfArticlesPerNewspaper" /></td>
			<td>${AverageArticlesPerNewspaper }</td>
			<td>${StandardDesviationArticlesPerNewspaper }</td>
		</tr>
	</table>
	
	<!-- 4 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.RatioUsersWithAtLeast1Newspaper" /></td>
			<td>${RatioUsersWithAtLeast1Newspaper }</td>
		</tr>
	</table>
	
	<!-- 5 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.RatioUsersWithAtLeast1Article" /></td>
			<td>${RatioUsersWithAtLeast1Article }</td>
		</tr>
	</table>
	
		<!-- 6 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.AverageFollowsUpPerArticle" /></td>
			<td>${AverageFollowsUpPerArticle }</td>
		</tr>
	</table>
	
		<!-- 7 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.AverageFollowUpPerArticleOneWeek" /></td>
			<td>${AverageFollowUpPerArticleOneWeek }</td>
		</tr>
	</table>
	
	<!-- 8 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.AverageFollowUpPerArticleTwoWeek" /></td>
			<td>${AverageFollowUpPerArticleTwoWeek }</td>
		</tr>
	</table>
	
		<!-- 9 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.deviation" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.averageAndDesviationOfChirpsPerUser" /></td>
			<td>${AverageChirpsPerUser }</td>
			<td>${StandardDesviationChirpsPerUser }</td>
		</tr>
	</table>
	
	<!-- 10 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.RatioUsersWithMoreAvgChirps" /></td>
			<td>${RatioUsersWithMoreAvgChirps }</td>
		</tr>
	</table>
	
	<!-- 11 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.RatioPublicVersusPrivate" /></td>
			<td>${RatioPublicVersusPrivate }</td>
		</tr>
	</table>
	
	<!-- 12 -->
	
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.AverageArticlesPerPrivateNewspaper" /></td>
			<td>${AverageArticlesPerPrivateNewspaper }</td>
		</tr>
	</table>
	
	<!-- 13 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.AverageArticlesPerPublicNewspaper" /></td>
			<td>${AverageArticlesPerPublicNewspaper }</td>
		</tr>
	</table>
	
	<!-- 14 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.ratioPublicVersusPrivatePerPublisher" /></td>
			<td>${ratioPublicVersusPrivatePerPublisher }</td>
		</tr>
	</table>
	
	
	<!-- 15 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.AverageRatioOfPrivateVersusPublicNewspapers" /></td>
			<td>${AverageRatioOfPrivateVersusPublicNewspapers }</td>
		</tr>
	</table>
	
	<!-- 16 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.NewspapersWithMoreArticlesThanAverage" /></td>
			<td>${NewspapersWithMoreArticlesThanAverage }</td>
		</tr>
	</table>
	
	<!-- 17 -->
	<table class="displayStyle">
		
		<tr>
			<th><spring:message code="administrator.query" /></th>
			<th><spring:message code="administrator.value" /></th>
		
		</tr>
		
		<tr>
			<td><spring:message code="administrator.NewspapersWithLessArticlesThanAverage" /></td>
			<td>${NewspapersWithLessArticlesThanAverage }</td>
		</tr>
	</table>
	
	
	
</security:authorize>
