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
<link rel="stylesheet" href="${staticHost}/css/div_list_row.css" />
<link rel="stylesheet" href="${staticHost}/css/nv.d3.css"
	type="text/css"></link>
<script src="${staticHost}/js/d3.min.js"></script>
<script src="${staticHost}/js/nv.d3.min.js"></script>
<script src="${staticHost}/js/user_analytics.js"></script>
<script src="${staticHost}/js/user_map.js"></script>


<!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
<script>
                                function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}
                            </script>
<script>
                                var analyticsData = {
                                    "cat":[{"name":"No water","count":942,"color":"#98abc5"},{"name":"Dirty Water","count":542,"color":"#8a89a6"},{"name":"Too expensive","count":342,"color":"#7b6888"},{"name":"No connection","count":242,"color":"#6b486b"},{"name":"Intermittent supply","count":142,"color":"#a05d56"},{"name":"Leaking pipes","count":442,"color":"#ff8c00"},{"name":"No fixed time","count":350,"color":"#ff0000"}]
                                };
                            </script>

</head>
<body>
	<div class="outerwrapper">
		<jsp:include page="header.jsp" />
		<div class="container-fluid">
			<!-- 
			<div class="banner">
				<div class="locate-on-map">
					<script type="text/javascript"
						src="http://maps.google.com/maps/api/js?sensor=false"></script>
					<div style="overflow: hidden; height: 100%; width: 100%;">
						<div id="gmap_canvas" style="height: 100%; width: 100%;"></div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-8"></div>
					<div class="col-sm-4">
						<div class="banner-widget"></div>
					</div>
				</div>
			</div>
			 -->
			<div class="row" style="margin-top:5%;">
				<div class="col-sm-2">
					<p>
						<strong>Refine Search</strong>
					</p>
					<div class="refine-search">
						<input type="text" value="Amsterdam,Washington" 
							data-role="tagsinput">
						<!-- 
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
											<option value="1">Show All</option>
											<option value="2">Road</option>
											<option value="3">Water</option>
											<option value="4">Transportation</option>
											<option value="5">Electricity</option>
											<option value="6">Law &amp; order</option>
											<option value="7">Sewage</option>
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
									<div class="form-group">
										<label>Duration</label> <select class="form-control">
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
									<div class="form-group">
										<div class="btn btn-default btn-xs">Clear</div>
										<div class="btn btn-primary btn-xs">Search</div>
									</div>
								</form>
							</div>
						</div>
						-->
					</div>
					<p>
						<strong>Sort Issues by category</strong>
					</p>
					<div class="list-group">
						<a href="#" class="list-group-item active">Show All</a> <a
							href="#" class="list-group-item">Water</a> <a href="#"
							class="list-group-item">Electricity</a> <a href="#"
							class="list-group-item">Sewage &amp; Sanitation</a> <a href="#"
							class="list-group-item">Law &amp; Order</a> <a href="#"
							class="list-group-item">Roads</a> <a href="#"
							class="list-group-item">Transportation</a>
					</div>
				</div>
				<div class="col-sm-10" style="padding: 0px 0px 0px 5px; ">
					<div class="listing-wrapper" style="width: 100%; margin-left: 0px;">
						<div class="secondary-wrapper">
							<div class="pull-left">
								<strong>My Activity Feed</strong>
							</div>
							<div class="pull-right" style="margin-right: 33%;">
								<button class="btn btn-primary">Raise Issue</button>
							</div>
							<div class="clearfix" style="margin-bottom: 1.5%; margin-top: 0px;"></div>
						</div>
						<div class="col-sm-8 user_feed_colsm">
							<div class="listing">
								<!-- .list-row  -->
								<c:forEach items="${userComplaints}" var="oneComplaint">
									<div class="list-row">
										<!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"-->
										<!-- not working as expected -->
											<p class="innerdiv-sharebtn">
											<!-- social media share buttons -->								
											<a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)"><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>		
											<a href="https://plus.google.com/share?url={URL}" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
											<a href="https://twitter.com/share" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
											</p>
										<div class="innerblock">
											<div class="col-sm-1 profile-info profile_pic_adjust">
												<div class="profile-pic">
													<c:if test="${!empty oneComplaint.loggedBy[0].photo}">
														<a href="#"><img
															src="${oneComplaint.loggedBy[0].photo}" alt=""></a>
													</c:if>
													<c:if test="${empty oneComplaint.loggedBy[0].photo}">
														<a href="#"><img
															src="${staticHost}/images/profile-pic.jpg" alt=""></a>
													</c:if>
												</div>
											</div>
											<div class="col-sm-10 profile-info profile_info_adjust">
												<p class="whom" style="margin-bottom: 0px; line-height:100%;">
													<strong class="issue-id">Issue #${oneComplaint.id}</strong>
													<span class="connector">raised by</span> 
													<a href="#" class="username text-limit name_adjust">
														<c:forEach
															items="${oneComplaint.loggedBy}" var="onePerson">
											                                ${onePerson.name}
											            </c:forEach></a>
													<a
														href="${location.url}/category/${oneComplaint.subCategoryId}.html?type=${viewType}"
														class="issue-scope">${oneComplaint.categoryTitle}</a> <a
														href="${location.url}/category/${oneComplaint.categoryId}.html?type=${viewType}"
														class="issue-scope-type"> <img
														src="${staticHost}/images/potholeissue.jpg"
														class="issue_type_pic" alt=""> <c:forEach
															items="${oneComplaint.categories}" var="oneCategory">
															<c:if test="${oneCategory.root}">
															Type - ${oneCategory.name}
															</c:if>
														</c:forEach>
													</a>
												</p>

												<p class="whenwhere">
													<span> <img src="${staticHost}/images/time.png"
														class="posttimestatus" alt=""> <a href="#"
														class="location"><abbr class="timeago"
															title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr></a>
													</span> <span class="connector">at</span> <span> <i
														class="glyphicon glyphicon-map-marker"></i> <a href="#"
														class="location">Cessna Business Park main
															road,Keverappa Layout</a>
													</span> <span> <a href="#"><img
															src="${staticHost}/images/underreview.png"
															class="postcurrentstatus" alt=""></a>
													</span>
												</p>
											</div>
											<div class="issue-info">

												<c:if test="${!empty oneComplaint.description}">
													<p class="desc elipsis">${oneComplaint.description}</p>
												</c:if>

												<c:if test="${!empty oneComplaint.photos}">
													<div class="issue-pic">
														<a href="#"><img
															src="${oneComplaint.photos[0].orgUrl}" alt=""
															align="middle"></a>
													</div>
												</c:if>

											</div>
										</div>
									</div>
								</c:forEach>

								<div class="pagination-wrapper" style="margin-left: 34%; margin-right: 34%;">
									<ul class="pagination">
										<li class="disabled"><a href="#">&laquo;</a></li>
										<li class="active"><a href="#">1</a></li>
										<li><a href="#">2</a></li>
										<li><a href="#">3</a></li>
										<li><a href="#">4</a></li>
										<li><a href="#">5</a></li>
										<li><a href="#">&raquo;</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="col-sm-4 analytics_data_colsm" style="padding-left: 10px; margin-left: -20px; padding-right: 0px;">

							<div id="chart_bar_c" style="border: 0px solid #fff;">
								<svg>
                                                        </svg>
							</div>

							<hr style="width: 92%; border-top: 1px solid #d2d2d2;">

							<div id="chart_pie_c" style="border: 0px solid #fff;">
								<svg>
                                                        </svg>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
