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
<jsp:include page="include.jsp" />
</head>
<body>
	<div class="outerwrapper">
		<jsp:include page="header.jsp" />
		<script type="text/javascript"
			src="http://maps.google.com/maps/api/js?sensor=false"></script>
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
						<input type="text" value="Amsterdam,Washington"
							data-role="tagsinput">
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
						<strong>Sort Issues by category</strong>
					</p>
					<div class="list-group">
						<c:if test="${empty selectedCategory}">
							<a href="#" class="list-group-item active">Show All
								(${total})</a>
						</c:if>
						<c:if test="${!empty selectedCategory}">
							<a href="${location.url}.html" class="list-group-item">Show
								All (${total})</a>
						</c:if>


						<c:forEach items="${rootCategories}" var="oneCategory">
							<c:if test="${selectedCategory eq oneCategory.id}">
								<a href="#" class="list-group-item active">${oneCategory.name}
									(${oneCategory.locationCount})</a>
							</c:if>
							<c:if test="${ selectedCategory ne oneCategory.id}">
								<a href="${location.url}/category/${oneCategory.id}.html"
									class="list-group-item">${oneCategory.name}
									(${oneCategory.locationCount})</a>
							</c:if>

						</c:forEach>
					</div>
				</div>
				<div class="col-sm-9">
					<table>
						<tr>
							<th>Name</th>
							<th>Description</th>
							<th>root</th>
							<th>Image</th>
							<th>headerImageUrl</th>
							<th>videoUrl</th>
							<td>pageUrl</td>
						</tr>
						<c:forEach items="${rootCategories}" var="oneCategory">
							<tr>

								<td>${oneCategory.name}</td>
								<td>${oneCategory.description}</td>
								<td>${oneCategory.root}</td>
								<td><img src="${oneCategory.imageUrl}" width="100" /></td>
								<td><img src="${oneCategory.headerImageUrl}" height="100" /></td>
								<td>${oneCategory.videoUrl}</td>
								<td><a
									href="${location.url}/category/${oneCategory.id}.html">${oneCategory.name}(${oneCategory.globalCount})</a></td>
								<%--Now look into child categories --%>
								<c:if test="${!empty oneCategory.childCategories}">
									<tr>
										<td colspan="7">
											<table>
												<c:forEach items="${oneCategory.childCategories}"
													var="oneChildCategory">
													<tr>
														<td>${oneChildCategory.name}</td>
														<td>${oneChildCategory.description}</td>
														<td>${oneChildCategory.root}</td>
														<td><img src="${oneChildCategory.imageUrl}" width="100" /></td>
														<td><img src="${oneChildCategory.headerImageUrl}"
															height="100" /></td>
														<td>${oneChildCategory.videoUrl}</td>
														<td><a
															href="${location.url}/category/${oneChildCategory.id}.html">${oneChildCategory.name}(${oneChildCategory.globalCount})</a></td>
													</tr>
												</c:forEach>
											</table>
										</td>
									</tr>
								</c:if>

							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
