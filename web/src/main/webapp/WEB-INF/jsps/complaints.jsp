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
                            </head>
                            <body ng-controller="complaintsController">
                                <div class="outerwrapper">
                                    <jsp:include page="header.jsp" />
                                    <div class="container-fluid">
                                        <div class="row">
                                            <div class="col-sm-2">
                                                <div class="left_filter">
                                                    <h3 class="text-footer">Select Position</h3>
                                                    <select class="select dropdownlist" ng-options="position as label(position.politicalBodyType, position.locationName) for position in positions" ng-model="selectedPosition" ng-change="onPositionSelected()">
                                                    </select>
                                                    <a href="#" class="list-group-item active" ng-click="onRefresh()">Refresh</a>

                                                    <p class="left_filter_category">
                                                        <strong>Filter Issues by category</strong>
                                                    </p>
                                                    
                                                    <div class="list-group">
                                                       <div>
                                                           <a href="#" class="list-group-item" ng-click="onCategorySelected(null)">Show All</a>
                                                       </div>
                                                        <div  ng-repeat="category in categories">
                                                        <a href="#" class="list-group-item" ng-click="onCategorySelected(category)">{{category.name}}</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-10 wrap_pad_left">
                                                <div class="listing-wrapper">
                                                    <div class="secondary-wrapper">
                                                        <h1 class="loc_head_text">{{selectedPosition.locationName}}</h1>									
                                                        <div class="views_div">


                                                            <a type="button" href="#!" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
                                                            <a type="button" href="?type=map" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
                                                            <a type="button" href="?type=analytics" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>




                                                        </div>
                                                        <div class="clearfix"></div>
                                                    </div>

                                                    <div class="listing">
                                                        <!-- new_div starts -->
                                                        <div class="mla_unread_complaints_controller">
                                                            <span class="unread_complaints"><b>Unread Complaints (2)</b></span>
                                                            <span class="glyphicon glyphicon-circle-arrow-up glyph_unread_complaints glyph_comments_right_float"></span>
                                                            <span class="comments_display_per_page"><b>Showing 2 of 2000 Complaints</b></span>
                                                        </div>	
                                                        <div class="mla_unread_complaints" >
                                                            <!-- 1 -->
                                                            <div ng-repeat="complaint in complaints">
                                                                <div class="list-row" ng-click="showDetailsAndMarkViewed($event, complaint)">
                                                                    <div class="innerblock">
                                                                        <span class="glyphicon glyphicon-fullscreen glyph_right_float" ng-class="{'glyphicon-collapse-up' : complaint.showMode}"></span>
                                                                        <div class="col-sm-1 profile_pic_adjust">
                                                                            <div class="profile-pic">
                                                                                <a href="#!" >
																					<c:set var="person_name" value="{{complaint.createdByPersons[0].name}}"/>
																					<c:out value="${person_name}"/>
																					<c:choose>
																						<c:when test="${person_name eq 'anonymous'}">
																							<img src="http://www.browserstack.com/images/dummy_avatar.png" alt=""></a>
																						</c:when>

																						<c:otherwise>
																							<img src="{{complaint.createdByPersons[0].profilePhoto}}" alt=""></a>
																						</c:otherwise>
																					</c:choose>
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
                                                                    <div class="innerdiv-innerblock">
                                                                        <div class="innerdiv-issue-info" >

                                                                            <p class="issue-title">
                                                                                <!--a href="#!" class="innerdiv-issue-scope">{{complaint.title}}</a-->
																				<a href="#!" class="innerdiv-issue-scope">{{complaint.categories  | subCategory}}</a>
                                                                            </p>

                                                                            <p class="desc elipsis">
																					<c:choose>
																						<c:when test="${complaint.description == ''}">
																						Lorem Ipsum is simply dummy text of the printing and typesetting 
																						industry. Lorem Ipsum has been the industry's standard dummy text 
																						ever since the 1500s, when an unknown printer took a galley of type 
																						and scrambled it to make a type specimen book. It has survived not 
																						only five centuries, but also the leap into electronic typesetting, 
																						remaining essentially unchanged. It was popularised in the 1960s with 
																						the release of Letraset sheets containing Lorem Ipsum passages, and
																						more recently with desktop publishing software like Aldus PageMaker 
																						including versions of Lorem Ipsum.																				
																						</c:when>

																						<c:otherwise>
																							{{complaint.description}}
																						</c:otherwise>
																						
																					</c:choose>
                                                                            </p>

                                                                            <div class="carousel_map_tab">
                                                                                <ul class="nav nav-tabs" id="c_m_tab{{$index + 1}}" >
                                                                                    <li><a href="#issues_images_carousel{{$index + 1}}" ng-click="showTab($event)">Complaint Pictures</a></li>
                                                                                    <li><a href="#loc_on_map{{$index + 1}}" ng-click="showTab($event)">Show on Map</a></li>
                                                                                </ul>

                                                                                <div class="tab-content">

                                                                                    <div class="tab-pane" id="issues_images_carousel{{$index + 1}}">
																						<div id="myCarousel{{$index + 1}}" class="carousel slide" data-ride="carousel">
																						<!-- Carousel items -->
																							<div class="carousel-inner">
																									<c:set var="first_complaint_image" value="{{complaint.images[0]}}"/>
																									<c:out value="${first_complaint_image}"/>
																									<c:choose>
																										<c:when test="${first_complaint_image eq ''}">
																										<div class="active item">
																											<img src="http://www.findtransfers.com/Photos/no_image.jpg" />
																										</div>
																										</c:when>

																										<c:otherwise>
																											<c:forEach items="${complaint.images}" var="onePhoto"  varStatus="counter">
																												<c:choose>
																													<c:when test="${counter.count == '1'}">
																													<div class="active item">
																														<img src="${onePhoto.orgUrl}" />
																													</div>
																													</c:when>

																													<c:otherwise>
																													<div class="item">
																														<img src="${onePhoto.orgUrl}" />
																													</div>
																													</c:otherwise>																										
																												</c:choose>
																											</c:forEach>
																										</c:otherwise>																										
																									</c:choose>
																							 </div>
																							<!-- Carousel nav -->
																							<a class="left carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
																							<a class="right carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
																						</div>
                                                                                    </div>

                                                                                    <div class="tab-pane" id="loc_on_map{{$index + 1}}">
                                                                                        <div>
                                                                                            <google-map id="googleMap{{$index + 1}}" lat="{{complaint.lattitude}}" lng="{{complaint.longitude}}"></google-map>
                                                                                        </div>
                                                                                    </div>

                                                                                </div>

                                                                            </div>

                                                                        </div>

                                                                        <!-- Comments Box -->

                                                                        <div id="load_comments_box">
                                                                            <a href="#!" id="comments_status" class="comments_controller allcomments_expand">Comments from Users ( 140 )</a>

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
                                <jsp:include page="footer.jsp" />
                            </body>
                        </html>
