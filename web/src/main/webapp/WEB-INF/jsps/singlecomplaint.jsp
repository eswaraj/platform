<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
            <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


                        <!DOCTYPE html>
                        <html lang="en" ng-app="complaintsApp">
                            <head>
                                <title>e-Swaraj - #${complaint.id}</title>
                                <jsp:include page="include.jsp" />
                                <link rel="stylesheet" href="${staticHost}/css/dashboard.css">
                                <link rel="stylesheet" href="${staticHost}/css/div_list_row.css" />
                                <link rel="stylesheet" href="${staticHost}/css/singlecomplaint.css" />

                                <script src="${staticHost}/js/masonry.pkgd.min.js"></script>
                                <script src="${staticHost}/js/jquery.flexslider-min.js"></script>
                                <script src="${staticHost}/js/jmpress.min.js"></script>
                                <script src="${staticHost}/js/carousel_image_slider.js"></script><!-- Script -->
                                <script src="${staticHost}/js/user_interaction.js"></script><!-- Script -->
                                <script src="${staticHost}/js/angular.min.js"></script>
                                <script src="${staticHost}/js/singlecomplaint.js"></script>
                                <script>
                                    var complaintId = ${complaint.id};
                                    var loggedIn = ${loggedIn};
                                    var totalComments = ${complaint.totalComments};
                                </script>
                                <script type="text/javascript">
                                    $(function () {
                                        $('#dasky').Dasky()
                                    });
                                    jQuery(document).ready(function() {
                                        jQuery("abbr.timeago").timeago();
                                    });
                                </script>

                                <!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
                                <script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>



                            </head>
                            <body ng-controller="complaintsController" class="body_style_nd_adjust_footer">
                                <div class="outerwrapper">
                                    <jsp:include page="header.jsp" />

                                    <!-- Reference : http://codecanyon.net/item/dasky-timeline-slider/full_screen_preview/5071233 -->
                                    <!--div id="issue_timeline" class="issue_timeline_data">
                                        <iframe style="width: 100%; zoom: 4; height: 100px; border: none; margin: 0; padding:0; scrolling: no;" src="http://eswaraj.github.io/timebar/issue_timeline.html"></iframe>
                                    </div-->

                                    <div class="innerwrapper">

                                        <div class="col-md-3 col-sm-3 md3_pad">

                                            <div class="issue_reporters_box_pic">

                                                <p class="text_reporters_p">
                                                    <a href="#!" class="text_reporters_anchor_pic">
                                                        <c:forEach var="cat" items="${complaint.categories}">
                                                            <c:if test="${not cat.root}">
                                                                ${cat.name} Issue 
                                                            </c:if>
                                                        </c:forEach>

                                                    </a>
                                                </p>

                                                <div class="profile-pic profile-pic-padding">
                                                    <a href="#!" ><img src="${staticHost}/images/trafficjam.png" class="complaints_issue_pic" alt=""></a>

														<span>
																<a href="#" class="issue-scope-type">
																	<c:forEach var="cat" items="${complaint.categories}">
																		<c:if test="${cat.root}">
																			Type - ${cat.name}
																		</c:if>
																	</c:forEach>

																</a>
															</span>
															
													</div>

                                            </div>

                                        </div>

                                        <div class="col-md-7 col-sm-7">

											<div class="list-row">
													<div class="col-sm-1 col-md-1 profile-info profile_pic_adjust">
														<div class="profile-pic">
															<c:if test="${!empty complaint.createdBy[0].profilePhoto}">
																<a href="#!" class="anchorlink" ><img src="${complaint.createdBy[0].profilePhoto}" alt=""></a>
															</c:if>
															<c:if test="${empty complaint.createdBy[0].profilePhoto}">
																<a href="#!" class="anchorlink" ><img src="${staticHost}/images/anonymous_profile_pic.png" alt="" style="width: 50px; max-width: 50px; border: 1px solid #ccc;" ></a>
															</c:if>
														</div>
													</div>
													<div class="col-sm-10 col-md-10 profile-info profile_info_adjust">
														<p class="col-sm-12 col-md-12 whom">
															<span class="username text-limit name_adjust col-sm-4 col-md-4">
																<c:forEach items="${complaint.createdBy}" var="onePerson">
																	<a href="#!" class="anchorlink username_adjust" >${onePerson.name}</a>
																</c:forEach>
															</span>

															<span class="issue-scope-type text-limit type_adjust col-sm-4 col-md-4">
																<c:forEach items="${complaint.categories}" var="oneCategory">
																<c:if test="${oneCategory.root}">
																	<img src = "${oneCategory.imageUrl}" class="issue_type_pic" alt="">
																</c:if>
																</c:forEach>																
															</span>
															
															<span class="time_info_adjust col-sm-4 col-md-4">
															<i class="glyphicon glyphicon-time"></i>
																<a href="#!" class="anchorlink" >
																<span class="location">
																	<abbr class="timeago" title="${complaint.complaintTime}">${complaint.complaintTimeIso}</abbr>
																</span>
																</a>
															</span>
															
														</p>

														<p class="whenwhere">

															<strong class="col-sm-3 col-md-3 issue-id">#${complaint.id}</strong>
															<p class="col-sm-9 col-md-9 status_nd_socialmedia">
																	<!-- social media share buttons -->								
																	<a class="addspacing" href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)"><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>		
																	<a href="https://plus.google.com/share?url={URL}" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
																	<a class="rightshift" href="https://twitter.com/share" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
																	<span>
																		<a href="#"><img src = "${staticHost}/images/status_public_open.png" class="issue_status_pic" alt=""></a>
																		<a href="#"><img src = "${staticHost}/images/status_politician_closed.png" class="issue_status_pic" alt=""></a>
																		<a href="#"><img src = "${staticHost}/images/status_admin_closed.png" class="issue_status_pic" alt=""></a>
																	</span>
															</p>
														
														</p>

														<div class="issue-info" >

														<c:forEach var="cat" items="${complaint.categories}">
															<c:if test="${not cat.root}">
																<p class="category_title_adjust">
																	<a class="anchorlink"><span class="issue-scope">${cat.name}</span></a>
																</p> 
															</c:if>
														</c:forEach>

														<c:if test="${!empty complaint.description}">
															<p class="desc elipsis description_adjust">
																${complaint.description}
															</p>
														</c:if>

														<c:if test="${!empty complaint.photos}">
																<div class="issue-pic">
																	<img src="${complaint.photos[0].orgUrl}" alt="" align="middle">
																</div>
														</c:if>

														<p class="list_row_footer_adjust">

														<span class="col-sm-4 col-md-4 address_adjust">
															<i class="glyphicon glyphicon-map-marker"></i>
															<a href="#!" class="anchorlink" ><span class="location">${complaint.locationAddress}</span></a>
														</span>

														<span class="col-sm-4 col-md-4 comments_adjust">
															<i class="glyphicon glyphicon-comment"></i>
															<a href="#!" class="anchorlink" ><span class="comments">Comments(${complaint.totalComments})</span></a>
														</span>

														<span class="col-sm-4 col-md-4 status_adjust">
														<i class="glyphicon glyphicon-eye-open"></i>
															<a href="#!" class="anchorlink" ><span class="issue_status">Pending</span></a>
														</span>
														
														</p>
														
													</div>
												</div>
											
													<div class="col-sm-1 col-md-1">
																										
													</div>
											</div>
										

										<div class="issue-info" >
                                                <!-- Comments Box -->

                                                <div id="load_comments_box" style="clear: both;"><a href="#!" id="comments_status" class="comments_controller">Comments from Users ( ${complaint.totalComments} )</a>

                                                    <div id="comments_box" class="div_comments_box">

                                                        <div id="add_comment"> 
                                                            <div ng-show="!loggedIn">
                                                                Please log in to add comment.
                                                            </div>

                                                            <form id="comment_form">

                                                                <a href="#" class="profile-pic-comments"><img src="${user.person.profilePhoto}" alt=""></a>
                                                                <input id="user_input" type="text" class="user_input_text" title="Please add your comment here..." ng-model="commentText" ng-disabled="!loggedIn"/>
                                                                <input id="user_input_button" type="button" value="Add Comment" class="comments_controller" ng-click="saveComment()"/>

                                                            </form>
                                                        </div>	

                                                        <!-- Old Comments -->

                                                        <div ng-repeat="comment in comments">
                                                            <div id="old_comments_block">

                                                                <a href="#" class="profile-pic-comments"><img src="{{comment.postedBy.profilePhoto}}" alt=""></a>

                                                                <p class="comments_whom">
                                                                    <a href="#" class="username">{{comment.postedBy.name}}</a>
                                                                    <!-- social media share buttons -->								
                                                                    <img src = "${staticHost}/images/time.png" class="posttimestatus" alt="">
                                                                    <a href="#" class="location">{{comment.creationTime | dateFormatter}}</a>
                                                                    <img src = "${staticHost}/images/admin_ribbon.png" class="posttimestatus leftshift" alt="" title="Admin"  ng-show="comment.adminComment">
                                                                </p>

                                                                <div class="comments-info" >

                                                                    <p class="desc elipsis">
                                                                        {{comment.text}}
                                                                    </p>

                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="collap_show_btn">
                                                            <a id="show_full_comments_page" href="#!" class="comments_controller" ng-click="getNext()">Show More...</a>
                                                            <a href="#!" id="collapse_comments_box" class="comments_controller">Collapse</a>
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
