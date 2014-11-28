<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en" ng-app="complaintsApp" >
<head>
<title>eSwaraj</title>
<jsp:include page="include.jsp" />
<link rel="stylesheet" href="${staticHost}/css/dashboard.css">
<link rel="stylesheet" href="${staticHost}/css/leader.css" />
<script src="${staticHost}/js/angular.min.js"></script>
<script src="${staticHost}/js/ui-bootstrap-tpls-0.11.2.min.js"></script>
<script src="${staticHost}/js/complaints.js"></script>
</head>
<body ng-controller="complaintsController">
<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&appId=1505507139725051&version=v2.0";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
								<img src="http://www.thelovelyplanet.net/wp-content/uploads/2012/06/tajmahal_india_in_sunset.jpg" style="
									opacity: 0.4;
									position: fixed;
									height: 100%;
									width: 100%;
									z-index: -1;
								">

<div class="outerwrapper main_content_page">
		<jsp:include page="header.jsp" />
		<!-- /.navbar -->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
					<div class="leader_page_left_pane">
						<div class="leader_profile_pic_bio">
								<c:if test="${!empty leader.profilePhoto}">
									<img src="${leader.profilePhoto}?type=square&width=100&height=100" alt="profile-pic" class= "leader-profile-pic" style="width: 100px;">
									<p class="center-align name-top-adjust">
										<strong class="red_orng_clr_text">${leader.name}</strong> <br /> 
										<c:if test="${!empty leader.politicalAdminType.shortName}">
											<span class="grey_text">
											 ${leader.politicalAdminType.shortName}
											</span> <br \>
											<span>In Office since ${leader.since}</span> <br \>
										</c:if>
										<c:if test="${empty leader.politicalAdminType.shortName}">
											<span class="grey_text">
											 Position Not Yet Declared
											</span><br \>
										</c:if>
									<a href="#!" id="mla_bio_data_link" class="btn btn-info">Know Your MLA</a>
									<a href="${locationUrl}" class="btn btn-info">Constituency Link</a>
									<hr>
									</p>
								</c:if>
								<c:if test="${empty leader.profilePhoto}">
									<img src="http://www.browserstack.com/images/dummy_avatar.png?type=square&width=80&height=80" alt="profile-pic" class="leader-profile-pic">
									<p class="center-align name-top-adjust">
									<strong class="red_orng_clr_text">Leader Not Yet Declared</strong> <br /> 
										<span class="grey_text">
										 Position Not Yet Declared 
										</span> <br />
									<a href="#!" id="mla_bio_data_link" class="btn btn-info">Know Your MLA</a>
									<a href="${locationUrl}" class="btn btn-info">Constituency Link</a>
									</p>
								</c:if>
						</div>

						<div class="leader_profile_edu">
								<div class="leader_educational_bg">
									<div>
										<h5 class="red_orng_clr_text">Educational Records</h5>
									</div>
									<ul>
										<li class="grey_text">B.Sc in 1977-79 | Pune Vidyapeeth</li>
										<li class="grey_text">M.Sc in 1980-81 | IIT, Kanpur</li>
									</ul>
								</div>
						</div>

						<div class="leader_profile_stats">
							<div class="complaints_followers_counter">
									<div>
										<h5 class="red_orng_clr_text leader_curr_stat">Current Statistics</h5>
									</div>
									<p class="leader_last_active">
										<span class="grey_text">Last Active</span> <br /> 
										<strong class="blue_color_text">16th August, 2014</strong>
									</p>
									<p class="leader_total_complaints">
										<span class="grey_text">Complaints</span> <br /> 
										<strong class="blue_color_text">20080</strong>
									</p>
									<p class="leader_followers">
										<span class="grey_text">Followers</span> <br /> 
										<strong class="blue_color_text">507800</strong>
									</p>
							</div>
						</div>

						<div class="list-group">
						
							<div class="filter_types">
							<p>
								<strong class="filter_citzn_serv">Filter Comments by Citizen Services</strong>
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
										<strong class="filter_sys_lvl">Filter Comments by SubCategory</strong>
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
										<strong class="filter_temporal">Filter Comments by Time</strong>
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
										<strong class="filter_spatial">Filter Comments by Location</strong>
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

						</div>
					</div>
					<div class="platform_constituency_counter">
						<p class="p_c_counter_loc red_orng_clr_text"><span>${location.name} Location</span></p>
						<table>
						<tbody>
							<tr>
								<td>
									<p class="total_cons_population">
										<span class="grey_text">Total Population</span> <br /> 
										<strong class="blue_color_text">9.879 million</strong>
									</p>
								</td>
								<td>
									<p class="total_cons_area">
										<span class="grey_text">Total Area</span> <br /> 
										<strong class="blue_color_text">1,484 km²</strong>
									</p>
								</td>
							</tr>
							<tr>
								<td>
									<p class="total_const_count">
										<span class="grey_text">Total Constituencies</span> <br /> 
										<strong class="blue_color_text">70</strong>
									</p>
								</td>
								<td>
									<p class="total_voters">
										<span class="grey_text">Total Voters</span> <br /> 
										<strong class="blue_color_text">56458975</strong>
									</p>
								</td>
							</tr>
							<tr>
								<td>
									<p class="total_eswaraj_reg_voters">
										<span class="grey_text">Voters with eSwaraj</span> <br /> 
										<strong class="blue_color_text">42453975</strong>
									</p>
								</td>
								<td>
									<p class="total_eswaraj_const_coverage">
										<span class="grey_text">eSwaraj Coverage</span> <br /> 
										<strong class="blue_color_text">90%</strong>
									</p>
								</td>
							</tr>
						</tbody>
						</table>
					</div>

					<h3 class="text-footer red_orng_clr_text">Select Position</h3>
					<select class="select dropdownlist dlist_width" ng-options="position as label(position.politicalBodyType, position.locationName) for position in positions" ng-model="selectedPosition" ng-change="onPositionSelected()">
					</select>
					<a href="#" class="list-group-item active refresh_button" ng-click="onRefresh()">Refresh</a>
				</div>
				<div class="col-sm-6">
					
					<h3 class="red_orng_clr_text mla_calendar_head">MLA Calendar</h3>
					<div class="mla_cal_contents">
						<img src="${staticHost}/images/conf_cal_sample.png" width="100%" alt="">
					</div>

					<h3 class="red_orng_clr_text mla_comm_feed">MLA Comments Feed</h3>
					<div class="listing-wrapper">
						<div class="listing">
							<!-- new_div starts -->
							<div class="mla_unread_complaints" >
								<!-- 1 -->
								<div ng-repeat="complaint in complaints">
									<div class="list-row" ng-click="showDetailsAndMarkViewed($event, complaint)">
										<div class="innerblock">
											<span class="glyphicon glyphicon-fullscreen glyph_right_float" ng-class="{'glyphicon-collapse-up' : complaint.showMode}"></span>
											<div class="col-sm-1 profile_pic_adjust">
												<div class="profile-pic" ng-switch on="complaint.createdByPersons[0].name">
													<div ng-switch-when="anonymous">
														<a href="#!"><img src="${staticHost}/images/anonymous_profile_pic.png" alt="" style="width: 35px;" /></a>
													</div>
													<div ng-switch-default>
														<a href="#!"><img src="{{complaint.createdByPersons[0].profilePhoto}}" alt="" /></a>
													</div>
												</div>
											</div>
											<div class="profile-info profile_info_adjust">
												<span>
													<strong class="text-limit issue-id"><span>#</span>{{complaint.id}}</strong>
													<span class="text-limit connector">by</span>
													<a href="#!" class="text-limit username_adjust">{{complaint.createdByPersons[0].name}}</a>
												</span>
												<span class="comment_type_adjust">
													<img src = "http://dev.eswaraj.com/images/potholeissue.jpg" class="issue-type-pic" alt="">
													<a href="#!" class="text-limit issue-scope-type">Type - {{complaint.categories | rootCategory}}</a>
												</span>
												<span class="comment_content_adjust">
													<a href="#!" class="text-limit issue-scope">{{complaint.categories  | subCategory}}</a>
												</span>
												<br />
												<span>
													<i class="glyphicon glyphicon-map-marker glyph_adjust"></i>
													<a href="#!" class="text-limit location_adjust">Cessna Business Park main road,Keverappa Layout</a>
												</span>
												<!--span class="comment_status_adjust">
													<a href="#!" class="text-limit issue-scope-status">Status - {{complaint.politicalAdminComplaintStatus}}</a>
												</span-->
												<span>
													<img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus posttimestatus_adjust" alt="">
													<a href="#!" class="location"><abbr class="text-limit timeago" title="{{complaint.complaintTime | dateFormatter}}">{{complaint.complaintTime | dateFormatter}}</abbr></a>
												</span>
											</div>
										</div>
									</div>

									<div class="innerdiv-list-row" ng-class="{'innerdiv-box-shadow' : complaint.showMode}" ng-show="complaint.showMode">
									   <div class="innerdiv-innerblock">
											<div class="innerdiv-issue-info" >

												<p class="issue-title">
													<!--a href="#!" class="innerdiv-issue-scope">{{complaint.title}}</a-->
													<a href="#!" class="innerdiv-issue-scope">{{complaint.categories  | subCategory}}</a>
												</p>

											</div>

											<!-- Comments Box -->

											<div id="load_comments_box">

												<div id="comments_box" class="div_comments_box">

												<!-- Old Comments -->
													<div ng-repeat="comment in complaint.comments" ng-show="comment.adminComment">
														<div id="old_comments_block">

															<a href="#!" class="profile-pic-comments"><img src="{{comment.postedBy.profilePhoto}}" alt=""></a>

															<p class="comments_whom">
																<a href="#!" class="username">{{comment.postedBy.name}}</a>
																<img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
																<a href="#!" class="location"><abbr class="timeago" title="{{comment.creationTime}}">{{comment.creationTime | dateFormatter}}</abbr></a>
																<img src="http://dev.eswaraj.com/images/admin_ribbon.png" class="posttimestatus leftshift" alt="" title="Admin" ng-show="comment.adminComment">
															</p>

															<div class="comments-info" >
																	{{comment.text}}
															</div>

															<!--div class="comments-info" >
																<textcollapse>
																	{{comment.text}}
																</textcollapse>
															</div-->

														</div>

													</div>

													<div id="show_full_comments_page">
														<a href="http://dev.eswaraj.com/complaint/{{complaint.id}}" id="show_all_comments" class="comments_controller">Go to Complaint Page >></a>
														<a href="#!" id="collapse_comments_box" class="comments_controller" ng-click="showDetailsAndMarkViewed($event, complaint)">Close</a>
													</div>
	
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- new_div ends  -->
								<div class="pagination-wrapper">
									<ul class="pagination">
										<li><a href="#!" ng-click="getPrevious()">&laquo;</a></li>
										<li><a href="#!" ng-click="getNext()">&raquo;</a></li>
									</ul>
								</div>
							</div>	
						</div>
					</div>

				</div>
				<div class="col-sm-3">
					<h3 class="red_orng_clr_text">MLA on Social Media</h3>
					<div class="eswaraj-int-facebook">
						<div class="fb-like-box" data-href="https://www.facebook.com/narendramodi" data-width="307" data-colorscheme="light" data-show-faces="false" data-header="false" data-stream="true" data-show-border="false"></div>
						<!--div class="fb-like-box"
							data-href="https://www.facebook.com/${leader.fbPage}"
							data-show-border="false" data-height="400"
							data-colorscheme="light" data-show-faces="true"
							data-header="false" data-stream="true" data-show-border="true"></div-->
					</div>
					<div class="eswaraj-int-twitter">
						<!--a class="twitter-timeline"
							href="https://twitter.com/${leader.twitterHandle}"
								data-widget-id="505705935222747136">Tweets by @${leader.twitterHandle}</a-->
						<a class="twitter-timeline"
						href="https://twitter.com/narendramodi"
						data-widget-id="536914450473697280">Tweets by @narendramodi</a>
						<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
					</div>
					<div class="mla_related_videos">
								<h5 class="red_orng_clr_text leader_curr_stat">MLA Videos</h5>
									<div id="myCarousel" class="carousel slide"> <!--  data-ride="carousel" removed to avoid auto slide -->
										<!-- Carousel items -->
										<div class="carousel-inner">
												<div class="active item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/1mUXNqQ1-cA" frameborder="0"
														allowfullscreen></iframe>
												</div>
												<div class="item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/pTTe7wTk57c" frameborder="0"
														allowfullscreen></iframe>
												</div>
												<div class="item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/qRFCqb4SeHM" frameborder="0"
														allowfullscreen></iframe>
												</div>
												<div class="item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/ADdxXM1dC5k" frameborder="0"
														allowfullscreen></iframe>
												</div>
										</div>
										
											<!-- Carousel nav -->
											<a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
											<a class="right carousel-control" href="#myCarousel" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
									</div>
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
	
								<div id="mla-bio-data-modal">
									<div id="mla-bio-data-modal-innerdiv">
									<h1><span>Lorem Ipsum</span></h1>
									<h4>"Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit..."</h4>
									<h5>"There is no one who loves pain itself, who seeks after it and wants to have it, simply because it is pain..."</h5>
									<div style="float:right;margin-left:6px;margin-bottom:6px;">
										<p>
										Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porttitor sapien eget elit interdum viverra. In et leo eu magna egestas tempus. Sed accumsan accumsan lacus, quis vehicula velit rutrum vel. Quisque lacinia convallis magna, ac aliquam ipsum pellentesque sit amet. Suspendisse finibus nibh non urna congue sagittis. Nunc scelerisque sapien at eros efficitur, at dapibus neque ultricies. Donec suscipit posuere felis eu feugiat. Integer commodo risus erat, vel maximus dolor tempor nec. Sed sodales lectus suscipit metus auctor, quis aliquam nisl tristique.
										</p>
										<p>
										Vestibulum quis tortor sem. Aliquam interdum ante ac iaculis fringilla. Duis varius vitae orci iaculis sodales. Mauris elementum ullamcorper sapien sed feugiat. Curabitur dapibus non risus ut laoreet. Donec molestie mattis ullamcorper. Vestibulum eu ante eget lorem elementum tempus. Aenean vitae ante nec odio lobortis egestas at nec lorem. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas sit amet felis ac diam aliquet rhoncus. Morbi viverra mi at maximus gravida.
										</p>
										<p>
										Donec quis quam volutpat, varius urna quis, facilisis ligula. Curabitur vel dictum arcu, sed rutrum odio. Praesent tempor pellentesque elit, quis pretium nibh aliquet quis. Proin rutrum urna vel pulvinar iaculis. Suspendisse sed sem sed velit imperdiet sollicitudin at vitae libero. In sodales, urna in sodales porta, justo dolor ullamcorper urna, hendrerit sollicitudin nisl leo in turpis. Fusce ac urna euismod est cursus consectetur. Mauris cursus ultrices convallis. Mauris nunc dui, congue id fermentum quis, malesuada ut tellus. Phasellus elementum dapibus venenatis. Proin sodales neque turpis, accumsan sagittis felis finibus ac. Vestibulum at urna tempor, convallis velit vitae, egestas risus.
										</p>
										<p>
										Donec convallis est eu lorem vestibulum, a suscipit libero pharetra. Mauris iaculis mi turpis, non efficitur magna dictum sit amet. Mauris maximus libero eu ornare interdum. Ut volutpat, lectus vitae viverra congue, ligula diam posuere velit, nec malesuada turpis turpis eget lectus. Nulla ultricies luctus magna at auctor. Phasellus libero dolor, interdum eget tincidunt vel, euismod a orci. Nam sit amet ipsum elit. Curabitur vehicula at urna ac placerat. Mauris mattis dolor ut lacus cursus suscipit id quis augue. Proin vitae lorem felis. Nunc vitae est lacus.
										</p>
										<p>
										Phasellus leo urna, gravida vitae justo eleifend, vulputate porttitor erat. Sed euismod ligula at tortor pellentesque rhoncus. Phasellus sed lacus blandit, aliquet tortor eu, accumsan sapien. Sed accumsan nulla augue, nec pulvinar neque sodales sit amet. Nullam a tincidunt nibh. Duis in tincidunt diam. Cras interdum imperdiet nulla, vitae rhoncus massa rhoncus ac. Aliquam ut lorem euismod magna dapibus laoreet id in sapien. Cras in orci ac nulla accumsan faucibus. Quisque et euismod lorem, convallis viverra dui. Mauris enim tellus, interdum a ex vel, porttitor aliquam metus. In hac habitasse platea dictumst. Nullam eu mollis erat. In et tortor metus.
										</p>
										<a href="#0" class="close-btn">Close</a>
									</div>
								</div>
								</div>
	
	<jsp:include page="footer.jsp" />
    <script src="${staticHost}/js/bootstrap-tagsinput-bloodhound.js"></script>
	<script type="text/javascript" src="${staticHost}/js/typeahead.bundle.js"></script>    
	<script src="${staticHost}/js/leader_page.js"></script>
</body>
</html>
