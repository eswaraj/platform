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
		<link rel="stylesheet" href="${staticHost}/css/dashboard.css">
		<link rel="stylesheet" href="${staticHost}/css/div_list_row.css" />

	</head>
	<body>
		<div class="">
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
			<script>
				var kml_path = "${location.kml}";
				var complaints = [
				    <%int i = 0;%>  
					    <c:forEach items="${complaintList}" var="oneComplaint">
						    <%if (i > 0)
							out.println(",");
						        i++;
						    %>
						    {lat:${oneComplaint.lattitude},lng:${oneComplaint.longitude},data:{id:${oneComplaint.id},category:"${oneComplaint.categoryTitle}",address:"Coming Soon",date: "${oneComplaint.complaintTimeIso}", userId: "${oneComplaint.loggedBy.id}", userName: "TODO", userImgUrl : "http://www.panoramio.com/user/4483", complaintImgUrl: "http://www.panoramio.com/user/4483"}}
				  </c:forEach>
				  ];
			</script>
			</c:if>

			<c:if test="${viewType eq 'analytics'}">
			<script type="text/javascript" src="http://www.google.com/jsapi"></script>
			<script src="${staticHost}/js/analytics.js" type="text/javascript"></script>
			<script src="${staticHost}/js/d3.min.js" type="text/javascript"></script>
			<script src="${staticHost}/js/nv.d3.min.js" type="text/javascript"></script>
			<link rel="stylesheet" href="${staticHost}/css/nv.d3.css">
			<link rel="stylesheet" href="${staticHost}/css/analytics.css">
			<script type="text/javascript">
				var analyticsData = ${locationCounters};

			</script>
			</c:if>
<div class="container-fluid">
	<div class="row">
		<div class="col-sm-2">
			<div class="left_filter">
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
			</div>
			</div>
		</div>
		<div class="col-sm-7">
			<div class="listing-wrapper">
				<div class="secondary-wrapper">
						<!--h1 class="loc_head_text">${location.name}</h1--> <!-- Not working at present, to be updated from backend -->
						<h1 class="loc_head_text">Delhi</h1>									
						<div class="views_div">

							<c:if test="${viewType eq 'list'}">
							<a type="button" href="#" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
							<a type="button" href="?type=map" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
							<a type="button" href="?type=analytics" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>
							</c:if>
							<c:if test="${viewType eq 'map'}">
							<a type="button" href="?type=list" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
							<a type="button" href="#" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
							<a type="button" href="?type=analytics" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>
							</c:if>
							<c:if test="${viewType eq 'analytics'}">
							<a type="button" href="?type=list" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
							<a type="button" href="?type=map" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
							<a type="button" href="#" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>
							</c:if>

						</div>
					<div class="clearfix"></div>
				</div>
				<c:if test="${viewType eq 'list'}">

				<div class="listing">
					<!-- new_div starts -->
					<c:forEach items="${complaintList}" var="oneComplaint">
					<div class="list-row">
						<!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"--> <!-- not working as expected -->
						<div class="innerblock">
							<div class="col-sm-1 profile-info profile_pic_adjust">
								<div class="profile-pic">
								<c:if test="${!empty oneComplaint.loggedBy[0].photo}">
								    <a href="#" ><img src="${oneComplaint.loggedBy[0].photo}" alt=""></a>
								</c:if>
                                <c:if test="${empty oneComplaint.loggedBy[0].photo}">
                                    <a href="#" ><img src="${staticHost}/images/profile-pic.jpg" alt=""></a>
                                </c:if>
								</div>
							</div>
							<div class="col-sm-10 profile-info profile_info_adjust">
								<p class="whom">
								<strong class="issue-id">Issue #${oneComplaint.id}</strong>
								<span class="connector">raised by</span>
								
								<a href="#" class="username">
								<c:forEach items="${oneComplaint.loggedBy}" var="onePerson">
								${onePerson.name}
								</c:forEach>
								</a>
								<!-- social media share buttons -->								
								<a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)"><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>		
								<a href="https://plus.google.com/share?url={URL}" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
								<a href="https://twitter.com/share" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
								</p>

								<p class="whenwhere">
								<span>
									<img src = "${staticHost}/images/time.png" class="posttimestatus" alt="">
									<a href="#" class="location"><abbr class="timeago" title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr></a>
								</span>
								<span class="connector">at</span>
								<span>
									<i class="glyphicon glyphicon-map-marker"></i>
									<a href="#" class="location">Cessna Business Park main road,Keverappa Layout</a>
								</span>
								<span>
									<a href="#"><img src = "${staticHost}/images/underreview.png" class="postcurrentstatus" alt=""></a>
								</span>
								</p>
							</div>
							<div class="issue-info" >

								<p>
								<a href="${location.url}/category/${oneComplaint.subCategoryId}.html?type=${viewType}" class="issue-scope">${oneComplaint.categoryTitle}</a>
								<a href="${location.url}/category/${oneComplaint.categoryId}.html?type=${viewType}" class="issue-scope-type">
									<img src = "${staticHost}/images/potholeissue.jpg" class="issue_type_pic" alt="">
									<c:forEach items="${oneComplaint.categories}" var="oneCategory">
									<c:if test="${oneCategory.root}">
									Type - ${oneCategory.name}
									</c:if>
									</c:forEach>
								</a>
								</p>

								<c:if test="${!empty oneComplaint.description}">
								<p class="desc elipsis">
								${oneComplaint.description}
								</p>
								</c:if>

								<c:if test="${!empty oneComplaint.photos}">
								<div class="issue-pic">
									<a href="#" ><img src="${oneComplaint.photos[0].orgUrl}" alt="" align="middle"></a>
								</div>
								</c:if>

							</div>
						</div>
					</div>
					</c:forEach>
					<!-- new_div ends  -->
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
				<div class="listing-wrapper">
					<div class="secondary-wrapper">
						<div class="pull-left">
							<h1><strong>Analytics</strong></h1><br/>
						</div>
						<div id="filter_list">
						</div>

						<div id="chart_ts">
							<svg>
							</svg>
						</div>
						<div id="chart_bar_c">
							<svg>
							</svg>
						</div>
						<div id="chart_bar_s">
							<svg>
							</svg>
						</div>

						<div id="chart_pie_c">
							<svg>
							</svg>
						</div>
						<div id="chart_pie_s">
							<svg>
							</svg>
						</div>
					</div>
				</div>
				</c:if>
			</div>
		</div>
		<div class="col-sm-3">
			<div class="right_profile">

						<% int i=0; %>
						<c:forEach items="${leaders}" var="oneLeader">
						  <% if(i == 0){ %>
						  <!--div class="item active"-->
						  <% }else{ %>
						  <!--div class="item"-->
						  <% }
						  i++;
						  %>

						  <div class="mla-profile">
                                    <img src="${oneLeader.profilePhoto}?type=square&height=200&width=200" class="politician_image" alt="Leader image"><br \>
                                    <a href="${oneLeader.urlIdentifier}.html"><strong>${oneLeader.name}, ${oneLeader.politicalAdminType.shortName}</strong></a><br \>
									<p>In Office since ${oneLeader.since}</p>
						</div>					
						</c:forEach>
			</div>
		</div>
	</div>
</div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>