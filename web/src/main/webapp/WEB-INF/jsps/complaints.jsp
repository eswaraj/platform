<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
            <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

                        <!DOCTYPE html>
                        <html lang="en" ng-app="complaintsApp">
                            <head>
                                <title>eSwaraj</title>
                                <jsp:include page="include.jsp" />
                                <link rel="stylesheet" href="${staticHost}/css/dashboard.css">
								<link rel="stylesheet" href='${staticHost}/css/complaints.css' />
                                <script src="${staticHost}/js/angular.min.js"></script>
                                <script src="${staticHost}/js/ui-bootstrap-tpls-0.11.2.min.js"></script>
                                <script src="${staticHost}/js/complaints.js"></script>
                                <script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.exp"></script>

								<!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
								<script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>

							</head>
                            <body ng-controller="complaintsController">
								<!-- <img src="http://www.thelovelyplanet.net/wp-content/uploads/2012/06/tajmahal_india_in_sunset.jpg" style="
									opacity: 0.4;
									position: fixed;
									height: 100%;
									width: 100%;
									z-index: -1;
								"> -->
                                <div class="outerwrapper main_content_page">
                                    <jsp:include page="header.jsp" />
                                    <div class="container-fluid">
                                        <div class="row">
                                            <div class="col-sm-2">
                                                <div class="left_filter">
                                                    <h3 class="text-footer">Select Position</h3>
                                                    <select class="select dropdownlist dlist_width" ng-options="position as label(position.politicalBodyType, position.locationName) for position in positions" ng-model="selectedPosition" ng-change="onPositionSelected()">
                                                    </select>
                                                    <a href="#" class="list-group-item active refresh_button" ng-click="onRefresh()">Refresh</a>

												<div class="list-group">
												
													<div class="filter_types">
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
														<div class="left_filter_sections">
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
														<div class="left_filter_sections">
															<p>
																<strong class="filter_temporal">Filter by Time</strong>
															</p>

															<select class="select dropdownlist dlist_width">
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
														<div class="left_filter_sections">
															<p>
																<strong class="filter_spatial">Filter by Location</strong>
															</p>
															<select class="select dropdownlist dlist_width">
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
                                            </div>
                                            <div class="col-sm-10 wrap_pad_left">
                                                <div class="listing-wrapper">
                                                    <div class="secondary-wrapper">
                                                        <div class="views_div">


															<a type="button" href="#!" title="Location View"><div class="cons_location_name"><span class="location_text_adjust">{{selectedPosition.locationName}}</span><img src=" http://www.naturalhighsafaris.com/cdn/cache/made/cdn/uploads/country_images/India/North/Delhi/India-Gate--Delhi-Photos2_940_529_80_s_c1.jpg" class="location_image"></div></a>	
                                                            <a type="button" href="#!" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
                                                            <a type="button" href="?type=map" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
                                                            <a type="button" href="?type=analytics" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>




                                                        </div>
                                                        <div class="clearfix"></div>
                                                    </div>

                                                    <div class="listing">
                                                        <!-- new_div starts -->
                                                        <div class="mla_unread_complaints_controller">
                                                            <span class="unread_complaints"><b>Unread Complaints (<span ng-repeat="item in complaints | filter:{isVisible:'true'} = (items | filter:filterExpr)"></span><span>{{complaints.length}}</span>)</b></span>
                                                            <span class="glyphicon glyphicon-circle-arrow-up glyph_unread_complaints glyph_comments_right_float"></span>
                                                            <span class="comments_display_per_page"><b>Showing <span ng-repeat="item in complaints | filter:{isVisible:'true'} = (items | filter:filterExpr)"></span><span>{{complaints.length}}</span> of <span>{{complaints.length}}</span> Complaints</b></span>
                                                        </div>	
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
                                                                            <span>
                                                                                <i class="glyphicon glyphicon-map-marker glyph_adjust"></i>
                                                                                <a href="#!" class="text-limit location_adjust">Cessna Business Park main road,Keverappa Layout</a>
                                                                            </span>
                                                                            <span class="comment_status_adjust">
                                                                                <a href="#!" class="text-limit issue-scope-status">Status - {{complaint.politicalAdminComplaintStatus}}</a>
                                                                            </span>
                                                                            <span>
                                                                                <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus posttimestatus_adjust" alt="">
                                                                                <a href="#!" class="location"><abbr class="text-limit timeago" title="{{complaint.complaintTime | dateFormatter}}">{{complaint.complaintTime | dateFormatter}}</abbr></a>
                                                                            </span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="innerdiv-list-row" ng-class="{'innerdiv-box-shadow' : complaint.showMode}" ng-show="complaint.showMode">
																	<p class="innerdiv-sharebtn">
																	<!-- social media share buttons -->								
																	<a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)"><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>		
																	<a href="https://plus.google.com/share?url={URL}" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
																	<a href="https://twitter.com/share" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
																	</p>
                                                                   <div class="innerdiv-innerblock">
                                                                        <div class="innerdiv-issue-info" >

                                                                            <p class="issue-title">
                                                                                <!--a href="#!" class="innerdiv-issue-scope">{{complaint.title}}</a-->
																				<a href="#!" class="innerdiv-issue-scope">{{complaint.categories  | subCategory}}</a>
                                                                            </p>

                                                                            <p class="desc elipsis">
																				<span ng-switch on="complaint.description">
																					<span ng-switch-when="">
																					</span>
																					<span ng-switch-default>
																						{{complaint.description}}
																					</span>
																				</span>
                                                                            </p>

                                                                            <div class="carousel_map_tab">
                                                                                <ul class="nav nav-tabs c_m_tab_content" id="c_m_tab{{$index + 1}}">
                                                                                    <li><a href="#issues_images_carousel{{$index + 1}}" ng-click="showTab($event); $event.preventDefault(); $event.stopPropagation();">Complaint Pictures</a></li>
                                                                                    <li><a href="#loc_on_map{{$index + 1}}" ng-click="showTab($event); $event.preventDefault(); $event.stopPropagation();">Show on Map</a></li>
                                                                                </ul>

                                                                                <div class="tab-content">

                                                                                    <div class="tab-pane" id="issues_images_carousel{{$index + 1}}">
																						<div id="myCarousel{{$index + 1}}" class="myCarousel_tabcontent carousel slide" data-ride="carousel">
																						<!-- Carousel items -->
																							<div class="carousel-inner">
																									<span ng-switch on="complaint.images">
																										<span ng-switch-when="">
																										<div class="active item">
																											<img src="http://www.findtransfers.com/Photos/no_image.jpg" />
																										</div>
																										</span>
																										<span ng-switch-default>
																											<div class="item" ng-class="{active : $first}" ng-repeat="img in complaint.images">
																												<img ng-src="{{img.orgUrl}}" />
																											</div>
																										</span>
																									</span>
																							 </div>
																							<!-- Carousel nav -->
																									<span ng-show="complaint.createdBy > 1">
																											<a class="left carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
																											<a class="right carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
																									</span>
																						</div>
                                                                                    </div>

                                                                                    <div class="tab-pane" id="loc_on_map{{$index + 1}}">
                                                                                        <div class="googleMap_tabcontent">
                                                                                            <google-map id="googleMap{{$index + 1}}" lat="{{complaint.lattitude}}" lng="{{complaint.longitude}}" class="gmap_canvas"></google-map>
                                                                                        </div>
                                                                                    </div>

                                                                                </div>

                                                                            </div>

                                                                        </div>

                                                                        <!-- Comments Box -->

                                                                        <div id="load_comments_box">
                                                                            <a href="#!" id="comments_status" class="comments_controller allcomments_expand" scroll-on-click>Comments from Users ( )</a>

                                                                            <div id="comments_box" class="div_comments_box">

                                                                                <div id="change_status"> 

                                                                                    <span class="status_change">
                                                                                        <label class="label_status_options">Change Status : </label>
                                                                                        <select class="select dropdownlist status_options" ng-options="status for status in statuses" ng-model="complaint.newStatus" ng-change="onStatusSelected()">
                                                                                        </select>
                                                                                        <a href="#!" id="save_status_changes" class="comments_controller save_option" ng-click="saveStatus(complaint)">Save</a>
                                                                                    </span>
                                                                                    <span class="status_change_tracker">
                                                                                        <span class="status_change_display_message"></span>
                                                                                    </span>

                                                                                </div>	

                                                                                <div id="add_comment"> 

                                                                                    <form id="comment_form">

                                                                                        <a href="#!" class="profile-pic-comments"><img src="${user.person.profilePhoto}" alt=""></a>
                                                                                        <input id="user_input" type="text" class="user_input_text" placeholder="Please add your comment here..." ng-model="complaint.commentText"/>
                                                                                        <input id="user_input_button" type="button" value="Add Comment" class="comments_controller" ng-click="addComment(complaint)"/>

                                                                                    </form>

                                                                                </div>	

                                                                                <!-- Old Comments -->
                                                                                <div ng-repeat="comment in complaint.comments">
                                                                                    <div id="old_comments_block">

                                                                                        <a href="#!" class="profile-pic-comments"><img src="{{comment.postedBy.profilePhoto}}" alt=""></a>

                                                                                        <p class="comments_whom">
                                                                                            <a href="#!" class="username">{{comment.postedBy.name}}</a>
                                                                                            <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                            <a href="#!" class="location"><abbr class="timeago" title="{{comment.creationTime}}">{{comment.creationTime | dateFormatter}}</abbr></a>
                                                                                            <img src="http://dev.eswaraj.com/images/admin_ribbon.png" class="posttimestatus leftshift" alt="" title="Admin" ng-show="comment.adminComment">
                                                                                        </p>

                                                                                        <div class="comments-info" >
                                                                                            <textcollapse>
                                                                                                {{comment.text}}
                                                                                            </textcollapse>
                                                                                        </div>

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
										   <div>
											   <a href="#" class="list-group-item" ng-click="onCategorySelected(null)">Show All</a>
										   </div>
									<hr />
											<div  ng-repeat="category in categories">
											<a href="#" class="list-group-item" ng-click="onCategorySelected(category)">{{category.name}}</a>
											</div>
								<a href="#0" class="close-btn">Close</a>
								</div>
								</div>
								
                                <jsp:include page="footer.jsp" />

    <script src="${staticHost}/js/bootstrap-tagsinput-bloodhound.js"></script>
	<script type="text/javascript" src="${staticHost}/js/typeahead.bundle.js"></script>    
	<script src="${staticHost}/js/filter_settings.js"></script>
                            </body>
                        </html>
