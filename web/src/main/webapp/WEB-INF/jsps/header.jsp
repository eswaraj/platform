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
            <a class="navbar-brand" href="index.php">
                <img src="${staticHost}/images/eswaraj-dashboard-logo.png" class="pull-left" alt="">
                <span pull-left="">e-Swaraj</span>
            </a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
			<c:if test="${loggedIn}">
			<li><img class="home-profile-pic" src="${user.person.profilePhoto}"/></li>                
			</c:if>	
                <li>
                    <a href="05-constituency.php">My Constituency</a>
                </li>
                <li>
                    <a href="07-my-mla.php">My MLA</a>
                </li>
                <li>
                    <a href="home.php">Page</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
			 <li class="download">
				<a href="https://play.google.com/store/apps/details?id=com.next.eswaraj&hl=en">
					<span>
						<img alt="" src="images/android-icon.png"></span>
				</a>
			</li>
			<li class="download">
				<a href="https://itunes.apple.com/in/app/eswaraj/id689751495?mt=8">
					<span>
						<img alt="" src="images/apple-icon.png"></span>
				</a>
			</li> 
                <li class="active">
				<c:if test="${!loggedIn}">
                    <a href="10-profile-pending.php"> <i class="glyphicon glyphicon-user"></i>
					</c:if>
					<c:if test="${loggedIn}">
                    <a href="/web/logout"> <i class="glyphicon glyphicon-off"></i>
					</c:if>
                    </a>
                </li>
            </ul>
        </div>
        <!-- /.nav-collapse -->
    </div>
</div>
<!-- /.navbar -->

