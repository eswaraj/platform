<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>eSwaraj</title>
		<jsp:include page="include.jsp" />
	</head>
	<body>
		<div class="outerwrapper">
			<jsp:include page="header.jsp" />
			<script>
				jQuery(document).ready(function() {
					jQuery("abbr.timeago").timeago();
				});
			</script>
			<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&v=3.exp&libraries=visualization"></script>
			<c:if test="${viewType eq 'map'}">
			<script src="${staticHost}/js/markerclusterer.js" type="text/javascript"></script>
			<script src="${staticHost}/js/mapview.js" type="text/javascript"></script>
			</c:if>

			<c:if test="${viewType eq 'analytics'}">
			<script type="text/javascript" src="http://www.google.com/jsapi"></script>
			<script type="text/javascript">
				google.load("visualization", "1", {packages:["corechart"]});
				google.setOnLoadCallback(drawChart);
				function drawChart() {
					var data = google.visualization.arrayToDataTable([
							['Category', 'Complaints']
							<c:forEach items="${rootCategories}" var="oneCategory">
							,['${oneCategory.name}',    ${oneCategory.locationCount}]
							</c:forEach>
							]);

					var options = {
title: 'Categories wise',
       is3D: true,
					};

					var chart = new google.visualization.PieChart(document.getElementById('category_3d_pie'));
					chart.draw(data, options);
				}
</script>
</c:if>
<div class="container-fluid">
	<div class="banner">
		<div class="locate-on-map">
			<div style="overflow: hidden; height: 100%; width: 100%;">
				<div id="gmap_canvas" style="height: 100%; width: 100%;"></div>
			</div>
			<script type="text/javascript"> function init_map(){var myOptions = {zoom:14,center:new google.maps.LatLng(${location.latitude},${location.longitude}),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById("gmap_canvas"), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(${location.latitude},${location.longitude} )});infowindow = new google.maps.InfoWindow({content:"<b></b><br/><br/>400706 " });google.maps.event.addListener(marker, "click", function(){infowindow.open(map,marker);});}google.maps.event.addDomListener(window, 'load', init_map);</script>
		</div>
		<div class="row">
			<div class="col-sm-8"></div>
			<div class="col-sm-4">
				<div class="banner-widget">
					<div id="carousel-example-generic" class="carousel slide"
						data-ride="carousel">
						<!-- Wrapper for slides -->
						<div class="carousel-inner">
							<div class="item active">
								<div class="mla-profile">
									<img
									src="http://uxgraphy.com/eswaraj/v7/images/issues/issues.png"
									alt="mla image">
									<p>
									<a href="#"><strong>Rahuram Desai, MLA</strong></a> <span>In
										Office since Jan, 2014</span>
									</p>
								</div>
							</div>
							<div class="item">
								<div class="mla-profile">
									<img
									src="http://uxgraphy.com/eswaraj/v7/images/profile-pic.jpg"
									alt="mla image">
									<p>
									<a href="#"><strong>Rahuram Desai, MLA</strong></a> <span>In
										Office since Jan, 2014</span>
									</p>
								</div>
							</div>
							<div class="item">
								<div class="mla-profile">
									<img
									src="http://uxgraphy.com/eswaraj/v7/images/issues/issues.png"
									alt="mla image">
									<p>
									<a href="#"><strong>Rahuram Desai, MLA</strong></a> <span>In
										Office since Jan, 2014</span>
									</p>
								</div>
							</div>
						</div>
						<!-- Controls -->
						<a class="left carousel-control"
							href="#carousel-example-generic" role="button"
							data-slide="prev"> <span
								class="glyphicon glyphicon-chevron-left"></span>
							</a> <a class="right carousel-control"
							href="#carousel-example-generic" role="button"
							data-slide="next"> <span
								class="glyphicon glyphicon-chevron-right"></span>
						</a>
						<!-- Indicators -->
						<ol class="carousel-indicators">
							<li data-target="#carousel-example-generic" data-slide-to="0"
							class="active"></li>
							<li data-target="#carousel-example-generic" data-slide-to="1"></li>
							<li data-target="#carousel-example-generic" data-slide-to="2"></li>
						</ol>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-sm-3">
			<p>
			<strong>Refine Search</strong>
			</p>
			<div class="refine-search">
				<input type="text" value="Amsterdam,Washington" data-role="tagsinput">
				<div class="filter-options">
					<span class="trigger btn btn-default"> <i
							class="glyphicon glyphicon-cog"></i>
					</span>
					<div class="dropdown">
						<form action="">
							<div class="form-group">
								<label>Your Locality</label> <select class="form-control">
									<option value="1"></option>
									<option value="2">Locality</option>
									<option value="3">Locality</option>
									<option value="4">Locality</option>
									<option value="5">Locality</option>
									<option value="6">Locality</option>
									<option value="7">Locality</option>
								</select>
							</div>
							<div class="form-group">
								<label>Citizen Services</label> <select class="form-control">
									<c:forEach items="${rootCategories}" var="oneCategory">
									<option value="${oneCategory.id}">${oneCategory.name}
									(${oneCategory.locationCount})</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label>Issue Type</label> <select class="form-control">
									<option value="1">Issue</option>
									<option value="2">Issue</option>
									<option value="3">Issue</option>
									<option value="4">Issue</option>
									<option value="5">Issue</option>
									<option value="6">Issue</option>
									<option value="7">Issue</option>
								</select>
							</div>
							<%-- 
							<div class="form-group">
								<label>Duration</label>
								<select class="form-control">
									<option value="1"></option>
									<option value="2">Today</option>
									<option value="3">Yesterday</option>
									<option value="4">Last 72 Hrs</option>
									<option value="5">Last 1 Week</option>
									<option value="6">Last 2 Week</option>
									<option value="7">Last 1 Month</option>
									<option value="8">Last 3 Month</option>
								</select>
							</div>
							--%>
							<div class="form-group">
								<div class="btn btn-default btn-xs">Clear</div>
								<div class="btn btn-primary btn-xs">Search</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<p>
			<strong>Filter Issues by category</strong>
			</p>
			<div class="list-group">
				<c:if test="${viewType eq 'list'}">
					<c:if test="${empty selectedCategory}">
					<a href="#" class="list-group-item active">Show All
						(${total})</a>
					</c:if>
					<c:if test="${!empty selectedCategory}">
					<a href="${location.url}.html?type=${viewType}"
						class="list-group-item">Show All (${total})</a>
					</c:if>


					<c:forEach items="${rootCategories}" var="oneCategory">
					<c:if test="${selectedCategory eq oneCategory.id}">
					<a href="#" class="list-group-item active">${oneCategory.name}
						(${oneCategory.locationCount})</a>
					</c:if>
					<c:if test="${ selectedCategory ne oneCategory.id}">
					<a
						href="${location.url}/category/${oneCategory.id}.html?type=${viewType}"
						class="list-group-item">${oneCategory.name}
						(${oneCategory.locationCount}) </a>
					</c:if>

					</c:forEach>
				</c:if>
				<c:if test="${viewType eq 'map'}">
					<a href="#!" class="list-group-item" onclick="getData('${location.id}')">Show All (${total})</a>
					<c:forEach items="${rootCategories}" var="oneCategory">
						<a href="#!" class="list-group-item" onclick="getData('${location.id}/${oneCategory.id}')">${oneCategory.name} (${oneCategory.locationCount}) </a>
					</c:forEach>
				</c:if>
				<c:if test="${viewType eq 'analytics'}">
					<a href="#!" url="${location.id}" class="list-group-item">Show All (${total})</a>
					<c:forEach items="${rootCategories}" var="oneCategory">
					<a href="#!" url="${location.id}/${oneCategory.id}" class="list-group-item">${oneCategory.name} (${oneCategory.locationCount}) </a>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<div class="col-sm-9">
			<div class="listing-wrapper">
				<div class="secondary-wrapper">
					<div class="pull-left">
						<h1>${location.name}</h1>
					</div>
					<div class="pull-right">
						<div class="btn-group">
							<c:if test="${viewType eq 'list'}">
							<a type="button" href="#" class="btn btn-default active" title="List View"> <i class="glyphicon glyphicon-list"></i></a>
							<a type="button" href="?type=map" class="btn btn-default" title="Map View"><i class="glyphicon glyphicon-map-marker"></i></a>
							<a type="button" href="?type=analytics" class="btn btn-default" title="Analytics View"><i class="glyphicon glyphicon-signal"></i></a>
							</c:if>
							<c:if test="${viewType eq 'map'}">
							<a type="button" href="?type=list" class="btn btn-default" title="List View"> <i class="glyphicon glyphicon-list"></i></a>
							<a type="button" href="#" class="btn btn-default active" title="Map View"><i class="glyphicon glyphicon-map-marker"></i></a>
							<a type="button" href="?type=analytics" class="btn btn-default" title="Analytics View"><i class="glyphicon glyphicon-signal"></i></a>
							</c:if>
							<c:if test="${viewType eq 'analytics'}">
							<a type="button" href="?type=list" class="btn btn-default" title="List View"> <i class="glyphicon glyphicon-list"></i></a>
							<a type="button" href="?type=map" class="btn btn-default" title="Map View"><i class="glyphicon glyphicon-map-marker"></i></a>
							<a type="button" href="#" class="btn btn-default active" title="Analytics View"><i class="glyphicon glyphicon-signal"></i></a>
							</c:if>


						</div>
						<button class="btn btn-primary">Raise Issue</button>
					</div>
					<div class="clearfix"></div>
				</div>
				<c:if test="${viewType eq 'list'}">

				<div class="listing">
					<!-- .list-row  -->
					<c:forEach items="${complaintList}" var="oneComplaint">
					<div class="list-row">
						<div class="issue-pic">
							<c:if test="${!empty oneComplaint.photos}">
							<img src="${oneComplaint.photos[0].orgUrl}" alt="">
							</c:if>

							<c:if test="${empty oneComplaint.photos}">
							<img src="${staticHost}/images/issues/issues.png" alt="">
							</c:if>
						</div>
						<div class="innerblock">
							<div class="profile-info">
								<div class="profile-pic">
									<img src="${staticHost}/images/profile-pic.jpg" alt="">
								</div>
								<p class="whom">
								<strong class="issue-id">Issue #${oneComplaint.id}</strong>
								<span class="connector">raised by</span> <a href="#"
									class="username">${oneComplaint.loggedBy.name}</a>
								</p>
								<p class="whenwhere">
								<span><abbr class="timeago"
										title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr></span>
								<span class="connector">at</span> <span> <i
										class="glyphicon glyphicon-map-marker"></i> <a href="#"
										class="location">TODO</a>
								</span>
								</p>
							</div>
							<div class="issue-info">
								<p class="desc elipsis">
								<c:if test="${empty oneComplaint.description}">
								${oneComplaint.categoryTitle}
								</c:if>
								<c:if test="${!empty oneComplaint.description}">
								${oneComplaint.description}
								</c:if>
								</p>
								<p class="classify">
								<c:forEach items="${oneComplaint.categories}"
								var="oneCategory">
								<c:if test="${oneCategory.root}">
								<small class="badge badge-infra">Type -
									${oneCategory.name}</small>
								</c:if>

								</c:forEach>

								</p>
							</div>
						</div>
					</div>
					<!-- /.list-row  -->
					</c:forEach>

					<div class="pagination-wrapper">
						<ul class="pagination">
							<c:if test="${enableFirst}">
							<li class="active"><a href="?page=1">&laquo;</a></li>
							</c:if>
							<c:if test="${!enableFirst}">
							<li class="disabled"><a href="#">&laquo;</a></li>
							</c:if>
							<c:forEach items="${pages}" var="onePage">
							<c:if test="${onePage eq currentPage}">
							<li class="active"><a href="?page=${onePage}">${onePage}</a>
							</li>
							</c:if>
							<c:if test="${onePage ne currentPage}">
							<li><a href="?page=${onePage}">${onePage}</a></li>
							</c:if>

							</c:forEach>
							<c:if test="${enableLast}">
							<li class="active"><a href="?page=${totalPages}">&raquo;</a></li>
							</c:if>
							<c:if test="${!enableLast}">
							<li class="disabled"><a href="#">&raquo;</a></li>
							</c:if>

						</ul>
					</div>
				</div>
				</c:if>
				<c:if test="${viewType eq 'map'}">
				<div id="panel">
					<button onclick="createHeatmap()">Heatmap View</button>
					<button onclick="createCluster()">Cluster View</button>
					<button onclick="createMarker()">Marker View</button>
				</div>
				<div id="map-container">
					<div id="map-canvas"></div>
				</div>
				</c:if>
				<c:if test="${viewType eq 'analytics'}">
				<div id="category_3d_pie" style="height: 600px, width:98%;"></div>
				</c:if>
			</div>
		</div>
	</div>
</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
