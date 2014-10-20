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
                                                            <div class="list-row"  ng-repeat="complaint in complaints">
                                                                <!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"--> <!-- not working as expected -->
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
                                                                <!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"--> <!-- not working as expected -->
                                                                <div class="innerdiv-innerblock">
                                                                    <div class="innerdiv-issue-info" >

                                                                        <p class="issue-title">
                                                                            <a href="#!" class="innerdiv-issue-scope">{{complaint.title}}</a>
                                                                        </p>

                                                                        <p class="desc elipsis">
                                                                            {{complaint.description}}								
                                                                        </p>

                                                                        <div class="carousel_map_tab">
                                                                            <ul class="nav nav-tabs" id="c_m_tab{{$index + 1}}">
                                                                                <li><a href="#issues_images_carousel{{$index + 1}}">Complaint Pictures</a></li>
                                                                                <li><a href="#loc_on_map{{$index + 1}}">Show on Map</a></li>
                                                                            </ul>

                                                                            <div class="tab-content">

                                                                                <div class="tab-pane" id="issues_images_carousel{{$index + 1}}">
                                                                                    <div id="myCarousel{{$index + 1}}" class="carousel slide" data-ride="carousel">
                                                                                        <!-- Carousel items -->
                                                                                        <div class="carousel-inner" ng-repeat="image in complaint.images">
                                                                                            <div class="item">
                                                                                                <img src="{{image}}" />
                                                                                            </div>                                                                    
                                                                                        </div>
                                                                                        <!-- Carousel nav -->
                                                                                        <a class="left carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
                                                                                        <a class="right carousel-control" href="#myCarousel{{$index + 1}}" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="tab-pane" id="loc_on_map{{$index + 1}}">
                                                                                    <div>
                                                                                        <div id="googleMap{{$index + 1}}"></div>
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
                                                                                    <select class="select dropdownlist status_options" ng-options="status for status in statuses" ng-model="selectedStatus" ng-change="onStatusSelected()">
                                                                                    </select>
                                                                                    <a href="#!" id="save_status_changes" class="comments_controller save_option" ng-click="saveStatus()">Save</a>
                                                                                </span>
                                                                                <span class="status_change_tracker">
                                                                                    <span class="status_change_display_message"></span>
                                                                                </span>

                                                                            </div>	

                                                                            <div id="add_comment"> 

                                                                                <form id="comment_form">

                                                                                    <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>
                                                                                    <input id="user_input" type="text" class="user_input_text" placeholder="Please add your comment here..." ng-model="newComment.commentText"/>
                                                                                    <input id="user_input_button" type="button" value="Add Comment" class="comments_controller" ng-click="addComment()"></input>

                                                                                </form>

                                                                            </div>	

                                                                            <!-- Old Comments -->

                                                                            <!-- Comment #140 -->
                                                                            <div id="old_comments_block">

                                                                                <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>

                                                                                <p class="comments_whom">
                                                                                    <strong class="issue-id">Comment #140 by</strong>
                                                                                    <a href="#!" class="username">Somnath Nabajja</a>
                                                                                    <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                    <a href="#!" class="location"><abbr class="timeago" title="2014-10-13T14:54:55Z">2014-10-14T14:54:55Z</abbr></a>
                                                                                </p>

                                                                                <div class="comments-info" >
                                                                                    <p class="desc elipsis text-content short-text">
                                                                                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla pretium nisl sit amet odio maximus, eu faucibus sem posuere. Donec eget justo sit amet elit pulvinar finibus. Vestibulum vel sem non arcu ornare hendrerit. Aenean aliquet tristique nisl, nec auctor justo. Donec in elit a quam varius lacinia id nec orci. Suspendisse et risus vel lorem sodales dictum. Aenean congue odio ut nisl maximus, vel rhoncus dolor suscipit. Integer vitae rhoncus sem. Proin at mi ex. Phasellus dignissim lacus lacus, et mollis lacus ultrices eget. Nunc sit amet risus sit amet elit tempor dictum nec eu ante. Nunc fermentum pulvinar lobortis. Praesent pulvinar id lorem non pulvinar. Mauris vestibulum nibh ligula, eget mollis diam commodo quis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.												
                                                                                    </p>
                                                                                    <p class="show-more">
                                                                                        <a href="#">Show more</a>
                                                                                    </p>
                                                                                </div>

                                                                            </div>

                                                                            <!-- Comment #139 -->
                                                                            <div id="old_comments_block">

                                                                                <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>

                                                                                <p class="comments_whom">
                                                                                    <strong class="issue-id">Comment #139 by</strong>
                                                                                    <a href="#!" class="username">Somnath Nabajja</a>
                                                                                    <!-- social media share buttons -->								
                                                                                    <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                    <a href="#!" class="location"><abbr class="timeago" title="2014-10-12T14:54:55Z">2014-10-14T14:54:55Z</abbr></a>
                                                                                </p>

                                                                                <div class="comments-info" >

                                                                                    <p class="desc elipsis text-content short-text">
                                                                                        Fusce ullamcorper tellus sit amet nisl faucibus pretium. Nullam eu sem lacinia nibh lacinia suscipit. Ut nec lacinia elit. Curabitur mauris quam, interdum quis mi id, dictum pulvinar nulla. Donec ac libero condimentum, consectetur eros nec, rutrum velit. Sed consequat nulla in arcu ultrices, vel tempor enim iaculis. Sed rutrum elit nunc, volutpat pulvinar ex dignissim ut. In hac habitasse platea dictumst. Aliquam ornare mauris at nisl volutpat, at rutrum ligula maximus. In hac habitasse platea dictumst. Nulla sit amet mauris diam. Sed purus nisi, consectetur condimentum mollis in, condimentum sit amet ipsum. Cras dictum non lacus non maximus. Vestibulum porta nisi ac massa fermentum, eu blandit orci ornare. Donec odio nulla, varius a elementum pretium, molestie eu felis.												
                                                                                    </p>
                                                                                    <p class="show-more">
                                                                                        <a href="#">Show more</a>
                                                                                    </p>

                                                                                </div>

                                                                            </div>

                                                                            <!-- Comment #138 -->
                                                                            <div id="old_comments_block">

                                                                                <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>

                                                                                <p class="comments_whom">
                                                                                    <strong class="issue-id">Comment #138 by</strong>
                                                                                    <a href="#!" class="username">Somnath Nabajja</a>
                                                                                    <!-- social media share buttons -->								
                                                                                    <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                    <a href="#!" class="location"><abbr class="timeago" title="2014-10-11T14:54:55Z">2014-10-14T14:54:55Z</abbr></a>
                                                                                </p>

                                                                                <div class="comments-info" >

                                                                                    <p class="desc elipsis text-content short-text">
                                                                                        Pellentesque venenatis fermentum nibh vel auctor. Cras sit amet lectus non dolor rutrum vulputate id non nisl. Mauris sollicitudin tempus justo eu lacinia. Proin quam tortor, pharetra eu ex sit amet, euismod bibendum enim. Donec in felis egestas, consectetur ipsum non, volutpat ante. Sed porta nisl purus, sed dictum sapien vestibulum ac. Aenean commodo, nunc sit amet malesuada rutrum, velit ex pulvinar elit, pulvinar feugiat dui lectus id tortor. Nunc condimentum vitae magna eget blandit. Cras pharetra ligula leo. Morbi nec consequat tellus. Nullam risus magna, sagittis et arcu non, semper commodo mauris. Sed sit amet arcu vel sem sodales sollicitudin. Proin a odio eu orci placerat commodo. Sed pellentesque vestibulum mi sit amet rhoncus. Morbi non enim in tellus bibendum gravida at a arcu. Mauris porttitor nulla in leo dictum condimentum.												
                                                                                    </p>
                                                                                    <p class="show-more">
                                                                                        <a href="#">Show more</a>
                                                                                    </p>

                                                                                </div>

                                                                            </div>

                                                                            <div id="show_full_comments_page">
                                                                                <a href="#!" id="show_all_comments" class="comments_controller">Go to Complaint Page >></a>
                                                                                <a href="#!" id="collapse_comments_box" class="comments_controller">Back To Top</a>
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
                                                            <div class="list-row">
                                                                <!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"--> <!-- not working as expected -->
                                                                <div class="innerblock">
                                                                    <span class="glyphicon glyphicon-fullscreen glyph_right_float"></span>
                                                                    <div class="col-sm-1 profile_pic_adjust">
                                                                        <div class="profile-pic">
                                                                            <a href="#!" ><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>
                                                                        </div>
                                                                    </div>
                                                                    <div class="profile-info profile_info_adjust">
                                                                        <span>
                                                                            <strong class="text-limit issue-id">#84042</strong>
                                                                            <span class="text-limit connector">by</span>
                                                                            <a href="#!" class="text-limit username_adjust">Vinay Kumar</a>
                                                                        </span>
                                                                        <span class="comment_type_adjust">
                                                                            <img src = "http://dev.eswaraj.com/images/potholeissue.jpg" class="issue-type-pic" alt="">
                                                                            <a href="#!" class="text-limit issue-scope-type">Type - Road</a>
                                                                        </span>
                                                                        <span class="comment_content_adjust">
                                                                            <a href="#!" class="text-limit issue-scope">Huge Number of Potholes on Road</a>
                                                                        </span>
                                                                        <span>
                                                                            <i class="glyphicon glyphicon-map-marker glyph_adjust"></i>
                                                                            <a href="#!" class="text-limit location_adjust">Embassy Golf Link, Koramangla Area</a>
                                                                        </span>
                                                                        <span class="comment_status_adjust">
                                                                            <a href="#!" class="text-limit issue-scope-status">Status - Open</a>
                                                                        </span>
                                                                        <span>
                                                                            <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus posttimestatus_adjust" alt="">
                                                                            <a href="#!" class="location"><abbr class="text-limit timeago" title="2014-10-11T14:54:55Z">2014-10-11T14:54:55Z</abbr></a>
                                                                        </span>
                                                                    </div>
                                                                </div>

                                                            </div>

                                                            <div class="innerdiv-list-row">
                                                                <!--div class="innerblock"  onclick="window.location='http://www.eswaraj.com/'; return false;"--> <!-- not working as expected -->
                                                                <div class="innerdiv-innerblock">
                                                                    <p class="innerdiv-whom">
                                                                        <!-- social media share buttons -->								
                                                                        <a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)"><img src="http://dev.eswaraj.com/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>
                                                                        <a href="https://plus.google.com/share?url={URL}" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
                                                                        <a href="https://twitter.com/share" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="http://dev.eswaraj.com/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
                                                                    </p>
                                                                    <div class="innerdiv-issue-info" >

                                                                        <p class="issue-title">
                                                                            <a href="#!" class="innerdiv-issue-scope">Lot of Potholes on Road</a>
                                                                        </p>

                                                                        <p class="desc elipsis">
                                                                            Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of "de Finibus Bonorum et Malorum" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes from a line in section 1.10.32.
                                                                        </p>

                                                                        <div class="carousel_map_tab">
                                                                            <ul class="nav nav-tabs" id="c_m_tab3">
                                                                                <li><a href="#issues_images_carousel3">Complaints Pic</a></li>
                                                                                <li><a href="#loc_on_map3">Location on Map</a></li>
                                                                            </ul>

                                                                            <div class="tab-content">

                                                                                <div class="tab-pane" id="issues_images_carousel3">
                                                                                    <div id="myCarousel3" class="carousel slide" data-ride="carousel">
                                                                                        <!-- Carousel items -->
                                                                                        <div class="carousel-inner">
                                                                                            <div class="active item">
                                                                                                <img src="images/issues.png" />
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <img src="images/issues2.png" />
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <img src="images/issues3.png" />
                                                                                            </div>
                                                                                            <div class="item">
                                                                                                <img src="images/issues4.png" />
                                                                                            </div>
                                                                                        </div>
                                                                                        <!-- Carousel nav -->
                                                                                        <a class="left carousel-control" href="#myCarousel3" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
                                                                                        <a class="right carousel-control" href="#myCarousel3" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                                                                                    </div>
                                                                                </div>

                                                                                <div class="tab-pane" id="loc_on_map3">
                                                                                    <div>
                                                                                        <div id="googleMap3"></div>
                                                                                    </div>
                                                                                </div>

                                                                            </div>

                                                                        </div>


                                                                    </div>

                                                                    <!-- Comments Box -->

                                                                    <div id="load_comments_box"><a href="#!" id="comments_status" class="comments_controller allcomments_expand">Comments from Users ( 88 )</a>

                                                                        <div id="comments_box" class="div_comments_box">

                                                                            <div id="change_status"> 

                                                                                <span class="status_change">
                                                                                    <label class="label_status_options">Change Status : </label>
                                                                                    <select class="status_options">
                                                                                        <option class="opopen">Open</option>
                                                                                        <option class="opackn">Acknowledged</option>
                                                                                        <option class="opinprocess" selected>In Process</option>
                                                                                        <option class="opclosed">Closed</option>
                                                                                    </select>
                                                                                    <a href="#!" id="save_status_changes" class="comments_controller save_option">Save</a>
                                                                                </span>
                                                                                <span class="status_change_tracker">
                                                                                    <span class="status_change_display_message"></span>
                                                                                </span>

                                                                            </div>	

                                                                            <div id="add_comment"> 

                                                                                <form id="comment_form">

                                                                                    <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>
                                                                                    <input id="user_input" type="text" class="user_input_text" placeholder="Please add your comment here..."/>
                                                                                    <input id="user_input_button" type="button" value="Add Comment" class="comments_controller"></input>

                                                                                </form>

                                                                            </div>	

                                                                            <!-- Old Comments -->

                                                                            <!-- Comment #88 -->
                                                                            <div id="old_comments_block">

                                                                                <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>

                                                                                <p class="comments_whom">
                                                                                    <strong class="issue-id">Comment #88 by</strong>
                                                                                    <a href="#!" class="username">Somnath Nabajja</a>
                                                                                    <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                    <a href="#!" class="location"><abbr class="timeago" title="2014-10-13T14:54:55Z">2014-10-14T14:54:55Z</abbr></a>
                                                                                </p>

                                                                                <div class="comments-info" >
                                                                                    <p class="desc elipsis text-content short-text">
                                                                                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla pretium nisl sit amet odio maximus, eu faucibus sem posuere. Donec eget justo sit amet elit pulvinar finibus. Vestibulum vel sem non arcu ornare hendrerit. Aenean aliquet tristique nisl, nec auctor justo. Donec in elit a quam varius lacinia id nec orci. Suspendisse et risus vel lorem sodales dictum. Aenean congue odio ut nisl maximus, vel rhoncus dolor suscipit. Integer vitae rhoncus sem. Proin at mi ex. Phasellus dignissim lacus lacus, et mollis lacus ultrices eget. Nunc sit amet risus sit amet elit tempor dictum nec eu ante. Nunc fermentum pulvinar lobortis. Praesent pulvinar id lorem non pulvinar. Mauris vestibulum nibh ligula, eget mollis diam commodo quis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.												
                                                                                    </p>
                                                                                    <p class="show-more">
                                                                                        <a href="#">Show more</a>
                                                                                    </p>
                                                                                </div>

                                                                            </div>

                                                                            <!-- Comment #87 -->
                                                                            <div id="old_comments_block">

                                                                                <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>

                                                                                <p class="comments_whom">
                                                                                    <strong class="issue-id">Comment #87 by</strong>
                                                                                    <a href="#!" class="username">Vinay Kumar</a>
                                                                                    <!-- social media share buttons -->								
                                                                                    <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                    <a href="#!" class="location"><abbr class="timeago" title="2014-10-12T14:54:55Z">2014-10-14T14:54:55Z</abbr></a>
                                                                                </p>

                                                                                <div class="comments-info" >

                                                                                    <p class="desc elipsis text-content short-text">
                                                                                        Fusce ullamcorper tellus sit amet nisl faucibus pretium. Nullam eu sem lacinia nibh lacinia suscipit. Ut nec lacinia elit. Curabitur mauris quam, interdum quis mi id, dictum pulvinar nulla. Donec ac libero condimentum, consectetur eros nec, rutrum velit. Sed consequat nulla in arcu ultrices, vel tempor enim iaculis. Sed rutrum elit nunc, volutpat pulvinar ex dignissim ut. In hac habitasse platea dictumst. Aliquam ornare mauris at nisl volutpat, at rutrum ligula maximus. In hac habitasse platea dictumst. Nulla sit amet mauris diam. Sed purus nisi, consectetur condimentum mollis in, condimentum sit amet ipsum. Cras dictum non lacus non maximus. Vestibulum porta nisi ac massa fermentum, eu blandit orci ornare. Donec odio nulla, varius a elementum pretium, molestie eu felis.												
                                                                                    </p>
                                                                                    <p class="show-more">
                                                                                        <a href="#">Show more</a>
                                                                                    </p>

                                                                                </div>

                                                                            </div>

                                                                            <!-- Comment #86 -->
                                                                            <div id="old_comments_block">

                                                                                <a href="#!" class="profile-pic-comments"><img src="http://dev.eswaraj.com/images/profile-pic.jpg" alt=""></a>

                                                                                <p class="comments_whom">
                                                                                    <strong class="issue-id">Comment #86 by</strong>
                                                                                    <a href="#!" class="username">Ravi Kumar</a>
                                                                                    <!-- social media share buttons -->								
                                                                                    <img src = "http://dev.eswaraj.com/images/time.png" class="posttimestatus" alt="" />
                                                                                    <a href="#!" class="location"><abbr class="timeago" title="2014-10-11T14:54:55Z">2014-10-14T14:54:55Z</abbr></a>
                                                                                </p>

                                                                                <div class="comments-info" >

                                                                                    <p class="desc elipsis text-content short-text">
                                                                                        Pellentesque venenatis fermentum nibh vel auctor. Cras sit amet lectus non dolor rutrum vulputate id non nisl. Mauris sollicitudin tempus justo eu lacinia. Proin quam tortor, pharetra eu ex sit amet, euismod bibendum enim. Donec in felis egestas, consectetur ipsum non, volutpat ante. Sed porta nisl purus, sed dictum sapien vestibulum ac. Aenean commodo, nunc sit amet malesuada rutrum, velit ex pulvinar elit, pulvinar feugiat dui lectus id tortor. Nunc condimentum vitae magna eget blandit. Cras pharetra ligula leo. Morbi nec consequat tellus. Nullam risus magna, sagittis et arcu non, semper commodo mauris. Sed sit amet arcu vel sem sodales sollicitudin. Proin a odio eu orci placerat commodo. Sed pellentesque vestibulum mi sit amet rhoncus. Morbi non enim in tellus bibendum gravida at a arcu. Mauris porttitor nulla in leo dictum condimentum.												
                                                                                    </p>
                                                                                    <p class="show-more">
                                                                                        <a href="#">Show more</a>
                                                                                    </p>

                                                                                </div>

                                                                            </div>

                                                                            <div id="show_full_comments_page">
                                                                                <a href="#!" id="show_all_comments" class="comments_controller">Go to Complaint Page >></a>
                                                                                <a href="#!" id="collapse_comments_box" class="comments_controller">Back To Top</a>
                                                                            </div>

                                                                        </div>


                                                                    </div>

                                                                </div>
                                                            </div>

                                                            <div class="pagination-wrapper">
                                                                <ul class="pagination">


                                                                    <li class="disabled"><a href="#!">&laquo;</a></li>



                                                                    <li class="active"><a href="?page=1">1</a>
                                                                    </li>






                                                                    <li><a href="?page=2">2</a></li>





                                                                    <li><a href="?page=3">3</a></li>





                                                                    <li class="disabled"><a href="#!">&raquo;</a></li>


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
