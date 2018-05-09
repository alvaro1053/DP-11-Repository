<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>


<div>
	<a href="/Acme-Newspaper"><img src="https://i.imgur.com/Gonrz7s.png"  class="center" alt="Acme Newspaper Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="dashboard/admin/display.do"><spring:message code="master.page.administrator.dashboard" /></a></li>
						
				</ul>
			</li>
		</security:authorize>
		
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv"><spring:message	code="master.page.user" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="chirp/user/followingChirps.do"><spring:message code="master.page.chirp.followingChirps" /> </a></li>
					<li><a href="user/user/list.do"><spring:message code="master.page.userList" /> </a></li>		
					<li><a href="user/user/followingList.do"><spring:message code="master.page.chirp.followingList" /> </a></li>
					<li><a href="user/user/followersList.do"><spring:message code="master.page.chirp.followersList" /> </a></li>
				</ul>
			</li>
		</security:authorize>
		
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="folder/actor/list.do"><spring:message code="master.page.mail" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="volume/list.do"><spring:message code="master.page.volume" /></a></li>
		</security:authorize>
		<security:authorize access="hasRole('USER') || hasRole('AGENT') || hasRole('ADMIN')">
			<li><a class="fNiv" href="volume/list.do"><spring:message code="master.page.volume" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="newspaper/list.do"><spring:message code="master.page.newspaper" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="newspaper/user/list.do"><spring:message code="master.page.newspaper" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="newspaper/admin/list.do"><spring:message code="master.page.newspaper" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv" href="newspaper/customer/list.do"><spring:message code="master.page.newspaper" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv" href="article/customer/list.do"><spring:message code="master.page.articles" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv" href="volume/customer/list.do"><spring:message code="master.page.volume" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a href="chirp/user/create.do"><spring:message code="master.page.chirp.create" /> </a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
		
			<li><a class="fNiv"><spring:message	code="master.page.article" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="article/admin/list.do"><spring:message code="master.page.articles" /></a></li>
				</ul>
			</li>
			</security:authorize>
			
			<!--  
			<li><a class="fNiv"><spring:message	code="master.page.newspaperSingle" /></a>
				<ul>
					<li class="arrow"></li>
					
				</ul>
			</li>
			-->
		
		<security:authorize access="hasRole('USER')">
		<li><a class = "fNiv"><spring:message code = "master.page.followUp"/></a>
			<ul>
				<li class="arrow"></li>
					<li><a href="followUp/user/list.do"><spring:message code="master.page.followUp.articles" /></a></li>
			</ul>
		</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
		<li><a class = "fNiv"><spring:message code = "master.page.tabooWords"/></a>
			<ul>
				<li class="arrow"></li>
					<li><a href="customisation/admin/display.do"><spring:message code="master.page.tabooWords" /></a></li>
					<li><a href="admin/admin/listArticles.do"><spring:message code="master.page.articlesTaboo" /></a></li>
					<li><a href="admin/admin/listNewspapers.do"><spring:message code="master.page.newspapersWithTaboo" /></a></li>
					<li><a href="admin/admin/listChirps.do"><spring:message code="master.page.chirpsWithTaboo" /></a></li>
					<li><a href="admin/admin/listAdverts.do"><spring:message code="master.page.AdvertsWithTaboo" /></a></li>
			</ul>
		</li>
		</security:authorize>
		
		<security:authorize access="hasRole('AGENT')">
		<li><a class = "fNiv"><spring:message code = "master.page.advertisement"/></a>
			<ul>
				<li class="arrow"></li>
					<li><a href="advertisement/agent/listPlacedAds.do"><spring:message code="master.page.advertisement.listPlacedAds" /></a></li>
					<li><a href="advertisement/agent/listNotPlacedAds.do"><spring:message code="master.page.advertisement.listNotPlacedAds" /></a></li>
			</ul>	
		</li>
		</security:authorize>

		<security:authorize access="isAnonymous() || hasRole('ADMIN')">
			<li><a class="fNiv" href="user/list.do"><spring:message code="master.page.userList" /></a></li>
		</security:authorize>
			


		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="article/list.do"><spring:message code="master.page.articles" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="article/user/list.do"><spring:message code="master.page.articles" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
					
						<security:authorize access="hasRole('CUSTOMER')">
							<li><a href="customer/customer/displayProfile.do"><spring:message code="master.page.displayProfile" /> </a></li>
							<li><a href="customer/customer/editProfile.do"><spring:message code="master.page.editUserProfile" /> </a></li>
						</security:authorize>
						
						<security:authorize access="hasRole('AGENT')">
							<li><a href="agent/agent/displayProfile.do"><spring:message code="master.page.displayProfile" /> </a></li>
							<li><a href="agent/agent/editProfile.do"><spring:message code="master.page.editUserProfile" /> </a></li>
						</security:authorize>
						
						<security:authorize access="hasRole('ADMIN')">
							<li><a href="admin/admin/displayProfile.do"><spring:message code="master.page.displayProfile" /> </a></li>
							<li><a href="admin/admin/editProfile.do"><spring:message code="master.page.editUserProfile" /> </a></li>
						</security:authorize>
						
						<security:authorize access="hasRole('USER')">
							<li><a href="user/user/displayUserProfile.do"><spring:message code="master.page.chirp.displayUserProfile" /> </a></li>
							<li><a href="user/user/editProfile.do"><spring:message code="master.page.editUserProfile" /> </a></li>
						</security:authorize>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

