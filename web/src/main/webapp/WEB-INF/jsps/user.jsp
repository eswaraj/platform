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
							<script>
								jQuery(document).ready(function() {
									jQuery("abbr.timeago").timeago();
									});
							</script>

</head>
<body>
<!-- <img src="http://www.glowmagazine.me/wp-content/uploads/2012/04/taj-mahal-at-sunset.jpg" style="
	opacity: 0.4;
	position: fixed;
	height: 100%;
	width: 100%;
	z-index: -1;
"> -->
	<div class="outerwrapper main_content_page">
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
			<div class="row">
				<div class="user_dash_left_pane col-sm-3 col-md-3">
												<div class="reporter_cover_profile_quote">
													<div class="cons_issue_reporters_box_cover_quote">
														<p>“It is Swaraj when we learn to rule ourselves. It is, therefore, in the palm of our hands. But such Swaraj has to be experienced, by each one for himself.”</p> 
													</div>
												</div>

												<div class="reporter_profile_data">
													<div class="cons_issue_reporters_box_pic">
														<c:if test="${!empty user.person.profilePhoto}">
															<img src="${user.person.profilePhoto}?type=square&width=100&height=100" alt="profile-pic" class= "reporter-profile-pic" style="width: 100px;">
														</c:if>
														<c:if test="${empty user.person.profilePhoto}">
															<img src="http://www.browserstack.com/images/dummy_avatar.png?type=square&width=80&height=80" alt="profile-pic" class="reporter-profile-pic">
														</c:if>
														<p class="center-align reporters_info">
														<c:if test="${!empty user.person.profilePhoto}">
															<strong class="red_orng_clr_text">${user.person.name}</strong> <br /> 
															<c:if test="${!empty age}">
																<span class="grey_text">
																${age} Yrs,
																</span>
															</c:if>
															<span class="grey_text"> 
															${user.person.gender}
															</span>
														</c:if>
														<c:if test="${empty user.person.profilePhoto}">
															<strong class="red_orng_clr_text">Aam Aadmi</strong> <br /> 
														</c:if>
														</p>
														<div class="complaints_followers_counter">
														<p class="col-sm-4 col-md-4 reporter_total_complaints">
															<span class="grey_text">Complaints</span> <br /> 
															<strong class="blue_color_text">200</strong>
														</p>
														
														<p class="col-sm-4 col-md-4 total_cons_population">
															<span class="grey_text">Population</span> <br /> 
															<strong class="blue_color_text">9.879 million</strong>
														</p>
														
														<p class="col-sm-4 col-md-4 total_cons_area">
															<span class="grey_text">Area</span> <br /> 
															<strong class="blue_color_text">1,484 km²</strong>
														</p>

														</div>
													</div>
												</div>

												<!-- <div class="list-group">
												
													<div class="filter_types" style="padding-left:10px;">
													<p>
														<strong class="filter_citzn_serv">Filter by Citizen Services</strong>
														<span class="glyphicon glyphicon-filter advanced-filter-citzn-serv"></span>
													</p>

													<div class="cat-search example example_objects_as_tags">
													  <div class="bs-example">
														<input id="citizen_services_input" type="text" />
													  </div>
												   </div>

													<hr />
														<div class="left_filter">
															<p>
																<strong class="filter_sys_lvl">Filter by SubCategory</strong>
																<span class="glyphicon glyphicon-filter advanced-filter-subcategory"></span>
															</p>

															<div class="subcat-search example example_objects_as_tags_subcat">
															  <div class="bs-example">
																<input id="subcategory_input" type="text" />
															  </div>
														   </div>
														</div>
													<hr />
														<div class="left_filter">
															<p>
																<strong class="filter_temporal">Filter by Time</strong>
															</p>

															<select class="select dropdownlist">
																<option selected>Select</option>
																<option>Today</option>
																<option>Yesterday</option>
																<option>Last 72 Hrs</option>
																<option>Last 1 Week</option>
																<option>Last 2 Weeks</option>
																<option>Last 1 Month</option>
																<option>Last 3 Months</option>
															</select>
														</div>
													<hr />
														<div class="left_filter">
															<p>
																<strong class="filter_spatial">Filter by Location</strong>
															</p>
															<select class="select dropdownlist">
																<option selected>Select</option>
																<option>Cessna Business Park main road</option>
																<option>Mahadevapura, More Mall</option>
																<option>Chandni Chowk</option>
																<option>South Delhi</option>
																<option>Gurgaon</option>
															</select>
														</div>
													
													</div>

												</div> -->
				</div>
				<div class="col-sm-6 col-md-6 user_dash_mid_pane">
					<div class="listing-wrapper" style="width: 100%; margin-left: 0px;">
						<div class="secondary-wrapper">
							<div class="pull-left">
								<strong></strong>
							</div>
							<div class="clearfix" style="margin-bottom: 1%; margin-top: 0px;"></div>
						</div>
						<div class="user_feed_colsm">
							<div class="listing">
								<!-- .list-row  -->
								<c:forEach items="${userComplaints}" var="oneComplaint">
									<div class="list-row user-dashboard-settings" onclick="window.location='http://dev.eswaraj.com/complaint/${oneComplaint.id}.html'; return false;" style="cursor:pointer;">
										<!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"-->
										<!-- not working as expected -->
											<div class="col-sm-1 profile-info profile_pic_adjust">
												<div class="profile-pic">
														<a href="#">
													<span>
														<c:forEach items="${oneComplaint.categories}" var="oneCategory">
														<c:if test="${oneCategory.root}">
															<img src = "${oneCategory.imageUrl}" style="width: 40px; max-width: 40px; border: 0px solid  #ccc;" alt="">
														</c:if>
														</c:forEach>
													</span>

														</a>
												</div>
											</div>
											<div class="col-sm-10 profile-info profile_info_adjust">
												<p class="col-sm-12 col-md-12 whom">
													<span class="username text-limit name_adjust col-sm-6 col-md-6">
														<strong class="issue-id">#${oneComplaint.id}</strong>
													</span>
													
													<span class="time_info_adjust col-sm-6 col-md-6">
														<i class="glyphicon glyphicon-time"></i>
														<a href="#!" class="anchorlink" >
															<span class="location"><abbr class="timeago" title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr></span>
														</a>
													</span>

												</p>

											<div class="issue-info">

												<p class="category_title_adjust">
													<a href="${location.url}/category/${oneComplaint.subCategoryId}.html?type=${viewType}" class="anchorlink" ><span class="issue-scope">${oneComplaint.categoryTitle}</span></a>
												</p>

												<c:if test="${!empty oneComplaint.description}">
													<p class="desc elipsis description_adjust">${oneComplaint.description}</p>
												</c:if>

												<c:if test="${!empty oneComplaint.photos}">
													<div class="issue-pic">
														<a href="#"><img src="${oneComplaint.photos[0].orgUrl}" alt="" align="middle"></a>
													</div>
												</c:if>
													
												<p class="list_row_footer_adjust">

													<span class="col-sm-4 col-md-4 address_adjust">
														<i class="glyphicon glyphicon-map-marker"></i>
														<a href="#!" class="anchorlink" ><span class="location">${oneComplaint.locationAddress}</span></a>
													</span>

													<span class="col-sm-4 col-md-4 comments_adjust">
														<i class="glyphicon glyphicon-comment"></i>
														<a href="#!" class="anchorlink" ><span class="comments">Comments(${oneComplaint.totalComments})</span></a>
													</span>

													<span class="col-sm-4 col-md-4 status_adjust">
													<i class="glyphicon glyphicon-eye-open"></i>
														<a href="#!" class="anchorlink" ><span class="issue_status">Pending</span></a>
													</span>
													
												</p>
												
											</div>

											</div>

											<div class="col-sm-1 share_buttons_adjust">
											<p class="innerdiv-sharebtn">
											<!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
											<script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>
											<!-- social media share buttons -->								
											<a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)" class="anchorlink" ><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>	
											<br />																		
											<a href="https://plus.google.com/share?url=http://www.eswaraj.com/" class="anchorlink" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
											<br />
											<a href="https://twitter.com/share" class="anchorlink" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
											</p>
											</div>

											</div>
								</c:forEach>

								<div class="pagination-wrapper" style="margin-bottom: 7%;">
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
				</div>
				</div>
				<div class="col-sm-3 col-md-3 user_dash_right_pane">

					<div id="chart_pie_c">
						<svg>
						</svg>
					</div>

					<div id="chart_bar_c">
						<svg>
						</svg>
					</div>

				</div>
			</div>
		</div>
	</div>
								<div id="modal-background-subcategory">
								<div id="modal-background-subcategory-innerdiv">
								<h2 class="blue_color_text">Select SubCategories</h2><hr />
										<a href="${location.url}.html?type=${viewType}" class="list-group-item active">Show All</a>
										<hr />
										<c:forEach items="${rootCategories}" var="oneCategory">
										<span class="red_orng_clr_text">${oneCategory.name}</span> <br />
										<c:forEach items="${oneCategory.childCategories}" var="subCategory">
											<a href="${location.url}/category/${subCategory.id}.html?type=${viewType}" " class="list-group-item">${subCategory.name}</a>
										</c:forEach>
										<hr />
									</c:forEach>
								<a href="#0" class="close-btn">Close</a>
								</div>
								</div>
								
								<div id="md-bg-services-plus-sys-level">
								<div id="md-bg-services-plus-sys-level-innerdiv">
								<h2 class="red_orng_clr_text">Select Citizen Services</h2><hr />
									<c:if test="${empty selectedCategory}">
										<a href="#" class="list-group-item active">Show All
											(${total})</a>
									</c:if>
									<c:if test="${!empty selectedCategory}">
										<a href="${location.url}.html?type=${viewType}"
										   class="list-group-item">Show All (${total})</a>
									</c:if>
									<hr />
									<c:forEach items="${rootCategories}" var="oneCategory">
										<c:if test="${selectedCategory eq oneCategory.id}">
											<a href="#" class="list-group-item active">${oneCategory.name}
												(${oneCategory.locationCount})</a><br />
										</c:if>
										<c:if test="${ selectedCategory ne oneCategory.id}">
											<a href="${location.url}/category/${oneCategory.id}.html?type=${viewType}" class="list-group-item">
											${oneCategory.name} (${oneCategory.locationCount}) </a><br />
										</c:if>
									</c:forEach>
								<a href="#0" class="close-btn">Close</a>
								</div>
								</div>
								
	<jsp:include page="footer.jsp" />
    <script src="${staticHost}/js/bootstrap-tagsinput-bloodhound.js"></script>
	<script type="text/javascript" src="${staticHost}/js/typeahead.bundle.js"></script>    
	<script src="${staticHost}/js/filter_settings.js"></script>
</body>
</html>
