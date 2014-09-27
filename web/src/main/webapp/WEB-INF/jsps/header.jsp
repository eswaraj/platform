<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="index.html">
						<img src="${staticHost}/images/eswaraj-dashboard-logo.png" class="pull-left" alt="">
						<span pull-left="">e-Swaraj</span>
					</a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav">
						<li>
							<a href="index.html"> <i class="glyphicon glyphicon-home"></i>
							</a>
						</li>
						<li class="active">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">My Area
							<span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li>
									<a href="/myac">Assembly Constituency</a>
								</li>
								
								<li>
									<a href="/mypc">Parliament Constituency</a>
								</li>
								<li>
                                    <a href="/myward">Local/Ward</a>
                                </li>
							</ul>
							
						</li>
						<li>
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">My Leaders
							<span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li>
									<a href="/mymp">MP</a>
								</li>
								
								<li>
									<a href="/mymla">MLA</a>
								</li>
								<li>
                                    <a href="/mywardmember">Ward Member</a>
                                </li>
								
							</ul>
						</li>
						<li>
							<a href="/citizenservices.html">My Citizen Services</a>
						</li>
					</ul>
					
					<c:if test="${!loggedIn}">					
					<ul class="nav navbar-nav navbar-right">
								 <li class="download">
								<a href="#">
									<span>
										<img src="${staticHost}/images/android-icon.png"  alt=""></span>
								</a>
							</li>
							<li class="download">
								<a href="#">
									<span>
										<img src="${staticHost}/images/apple-icon.png" alt=""></span>
								</a>
							</li>		
						<li>
							<div onclick="location.href='/web/login/facebook?redirect_url=${currentUrl}';" class="btn-group">
								<button class="btn btn-fb btn-sm" type="button">
									<i class="icomoon icomoon-facebook"></i>
								</button>
								<button class="btn btn-fb btn-sm" type="button">Log in with Facebook</button>
							</div>
						</li>
					</ul>					
					</c:if>
					
					<c:if test="${loggedIn}">
					<ul class="nav navbar-nav navbar-right">
								 <li class="download">
								<a href="#">
									<span>
										<img src="${staticHost}/images/android-icon.png"  alt=""></span>
								</a>
							</li>
							<li class="download">
								<a href="#">
									<span>
										<img src="${staticHost}/images/apple-icon.png" alt=""></span>
								</a>
							</li>
					<li class="dropdown user-profile">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<img src="${user.person.profilePhoto}" alt="profile-pic">
								${user.person.name}
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menu">
								<li>
									<a href="/editprofile.html">View/Edit Profile</a>
								</li>
								
								<li>
									<a href="/web/logout">Logout</a>
								</li>
								
							</ul>
						</li>
					</ul>
					</c:if>	
				</div>
				<!-- /.nav-collapse -->
			</div>
		</div>
		<!-- /.navbar -->
