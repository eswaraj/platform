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
                                <script src="${staticHost}/js/angular.min.js"></script>
                                <script src="${staticHost}/js/complaints.js"></script>
                                <script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.exp"></script>
                                <link rel="stylesheet" href='${staticHost}/css/complaints.css' />
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

                                                    <p class="left_filter_category">
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
                                            <div class="col-sm-10 wrap_pad_left">
                                                <div class="listing-wrapper">
                                                    <div class="secondary-wrapper">
                                                        <!--h1 class="loc_head_text">Delhi</h1--> <!-- Not working at present, to be updated from backend -->
                                                        <h1 class="loc_head_text">Delhi</h1>									
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
                                                                        <span class="glyphicon glyphicon-fullscreen glyph_right_float"></span>
                                                                        <div class="col-sm-1 profile_pic_adjust">
                                                                            <div class="profile-pic">
                                                                                <a href="#!" ><img src="{{complaint.createdByPersons[0].profilePhoto}}" alt=""></a>
                                                                            </div>
                                                                        </div>
                                                                        <div class="profile-info profile_info_adjust">
                                                                            <span>
                                                                                <strong class="text-limit issue-id">{{complaint.id}}</strong>
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
                                                                                <a href="#!" class="location"><abbr class="text-limit timeago" title="2014-10-14T14:54:55Z">{{complaint.complaintTime | dateFormatter}}</abbr></a>
                                                                            </span>
                                                                        </div>
                                                                    </div>

                                                                </div>

                                                                <div class="innerdiv-list-row">
                                                                    <div class="innerdiv-innerblock">
                                                                        <div class="innerdiv-issue-info" >

                                                                            <p class="issue-title">
                                                                                <a href="#!" class="innerdiv-issue-scope">{{complaint.title}}</a>
                                                                            </p>

                                                                            <p class="desc elipsis">
                                                                                {{complaint.description}}								
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
                                                                                            <div class="carousel-inner" ng-repeat="image in complaint.images">
                                                                                                <div class="item">
                                                                                                    <img src="{{image.orgUrl}}" />
                                                                                                </div>                                                                    
                                                                                            </div>
                                                                                            <!-- Carousel nav -->
                                                                                            <a class="left carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
                                                                                            <a class="right carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                                                                                        </div>
                                                                                    </div>

                                                                                    <div class="tab-pane" id="loc_on_map{{$index + 1}}">
                                                                                        <div>
                                                                                            <google-map id="googleMap{{$index + 1}}" lat="complaint.lattitude" lng="complaint.longitude"></google-map>
                                                                                        </div>
                                                                                    </div>

                                                                                </div>

                                                                            </div>

                                                                        </div>

                                                                        <!-- Comments Box -->

                                                                        <div id="load_comments_box"><a href="#!" id="comments_status" class="comments_controller allcomments_expand">Comments from Users ( 140 )</a>

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
                                                                                        <input id="user_input_button" type="button" value="Add Comment" class="comments_controller" ng-click="addComment(complaint)"></input>

                                                                                    </form>

                                                                            </div>	

                                                                            <!-- Old Comments -->

                                                                            <div id="show_full_comments_page">
                                                                                <a href="http://dev.eswaraj.com/complaint/{{complaint.id}}" id="show_all_comments" class="comments_controller">Go to Complaint Page >></a>
                                                                                <a href="#!" id="collapse_comments_box" class="comments_controller">Back To Top</a>
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

                                                    <div class="mla_read_complaints_controller">
                                                        <span class="read_complaints"><b>Read Complaints (4)</b></span>
                                                        <span class="glyphicon glyphicon-circle-arrow-up glyph_read_complaints glyph_comments_right_float"></span>
                                                        <span class="comments_display_per_page"><b>Showing 4 of 10000 Complaints</b></span>
                                                    </div>
                                                    <div class="mla_read_complaints">
                                                        <!-- 3 -->
                                                    </div>	

                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                </div>
                            <jsp:include page="footer.jsp" />
                            </body>
                        <script>
                            $(document).ready(function(){

                                $('#c_m_tab1 a:first').tab('show');
                                $('#c_m_tab2 a:first').tab('show');
                                $('#c_m_tab3 a:first').tab('show');
                                $('#c_m_tab4 a:first').tab('show');
                                $('#c_m_tab5 a:first').tab('show');
                                $('#c_m_tab6 a:first').tab('show');


                                /*enable tabs to be selected*/
                                $('#c_m_tab1 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                                /*enable tabs to be selected*/
                                $('#c_m_tab2 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                                /*enable tabs to be selected*/
                                $('#c_m_tab3 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                                /*enable tabs to be selected*/
                                $('#c_m_tab4 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                                /*enable tabs to be selected*/
                                $('#c_m_tab5 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                                /*enable tabs to be selected*/
                                $('#c_m_tab6 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });

                                $( ".mla_unread_complaints_controller" ).on( 'click', function () {
                                    $( this ).next( ".mla_unread_complaints" ).fadeToggle(500);
                                    $( this ).find( ".glyph_unread_complaints" ).toggleClass("glyphicon-collapse-down");
                                });

                                $( ".mla_read_complaints_controller" ).on( 'click', function () {
                                    $( this ).next( ".mla_read_complaints" ).fadeToggle(500);
                                    $( this ).find( ".glyph_read_complaints" ).toggleClass("glyphicon-collapse-down");
                                });

                            });
                        </script>
                        </html>
