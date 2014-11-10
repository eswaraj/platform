<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
            <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


                        <!DOCTYPE html>
                        <html lang="en" ng-app="complaintsApp">
                            <head>
                                <title>e-Swaraj - Complaint ${complaint.id}</title>
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
                                </script>

                                <!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
                                <script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>



                            </head>
                            <body ng-controller="complaintsController">
                                <div class="outerwrapper">
                                    <jsp:include page="header.jsp" />

                                    <!-- Reference : http://codecanyon.net/item/dasky-timeline-slider/full_screen_preview/5071233 -->
                                    <div id="issue_timeline" class="issue_timeline_data">
                                        <iframe style="width: 100%; zoom: 4; height: 100px; border: none; margin: 0; padding:0; scrolling: no;" src="http://eswaraj.github.io/timebar/issue_timeline.html"></iframe>
                                    </div>

                                    <div class="innerwrapper">

                                        <div class="col-md-3 md3_pad">

                                            <div class="issue_reporters_box_pic">

                                                <p class="text_reporters_p">
                                                    <a href="#!" class="text_reporters_anchor_pic">
                                                        <c:forEach var="cat" items="${complaint.categories}">
                                                            <c:if test="${not cat.root}">
                                                                ${cat.name} Complaints 
                                                            </c:if>
                                                        </c:forEach>
                                                    
                                                    </a>
                                                </p>

                                                <div class="profile-pic profile-pic-padding">
                                                    <a href="#!" ><img src="${staticHost}/images/trafficjam.png" class="complaints_issue_pic" alt=""></a>
                                                </div>

                                            </div>

                                            <div class="issue_reporters_box">

                                                <p class="text_reporters_p">
                                                    <a href="#!" class="text_reporters_anchor">Issue Reported by ${fn:length(complaint.loggedBy)}</a>
                                                </p>

                                                <div class="profile-pic profile-pic-padding">
                                                    <c:forEach items="${complaint.loggedBy}" var="oneUser">
                                                        <a href="#!" ><img class="reporters_profile_pic" src="${oneUser.profilePhoto}" alt=""></a>
                                                    </c:forEach>
                                                </div>

                                            </div>

                                        </div>

                                        <div class="col-md-9 md9_pad">
                                            <div class="issue-info" >
                                                <c:forEach var="cat" items="${complaint.categories}">
                                                    <c:if test="${not cat.root}">
                                                        <p>
                                                            <a href="#" class="issue-scope">${cat.name}</a>
                                                        </p> 
                                                    </c:if>
                                                </c:forEach>
                                                <p class="whenwhere">
                                                    <span>
                                                    <a href="#" class="issue-scope-type">
                                                    <img src = "${staticHost}/images/potholeissue.jpg" class="issue_type_pic" alt="">
                                                        <c:forEach var="cat" items="${complaint.categories}">
                                                            <c:if test="${cat.root}">
                                                                Type - ${cat.name}
                                                            </c:if>
                                                        </c:forEach>
                                                    
                                                    </a>
                                                    </span>
                                                    <span>
                                                        <i class="glyphicon glyphicon-map-marker"></i>
                                                        <a href="#" class="location">Cessna Business Park main road,Keverappa Layout</a>
                                                    </span>
                                                    <span>
                                                        <img src = "${staticHost}/images/time.png" class="posttimestatus" alt="">
                                                        <a href="#" class="location">Latest Update : ${complaint.complaintTime}</a>
                                                    </span>
                                                </p>

                                                <p class="whom">
                                                    <a href="#" ><strong class="issue-id rightshift">Total Issues Raised : ${fn:length(complaint.loggedBy)}</strong>
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

                                                <div id="myCarousel" class="carousel slide" data-ride="carousel">
                                                    <!-- Carousel items -->
                                                    <div class="carousel-inner">
                                                        <c:forEach items="${complaint.photos}" var="onePhoto"  varStatus="counter">
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
                                                    </div>
                                                    <!-- Carousel nav -->
                                                    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
                                                    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                                                </div>

                                                <div id="myCarousel2" class="carousel slide" data-ride="carousel">
                                                    <!-- Carousel items -->
                                                    <div class="carousel-inner">
                                                        <div class="active item">
                                                            <ul class="issue_description">
                                                                <li>
                                                                    <p class="description_text"><span class="glyphicon glyphicon-bookmark rightshift"></span>${complaint.description}</p>
                                                                    <div class="description_author">
                                                                        <img src="${complaint.loggedBy[0].profilePhoto}" class="reported_description_pic" alt="Author image">
                                                                        <ul class="description_author_info">
                                                                            <li class="description_author_name">${complaint.loggedBy[0].name}</li>
                                                                            <li class="description_author_area"><span class="glyphicon glyphicon-map-marker"></span>Kadubeesanahalli</li>
                                                                        </ul>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <!-- Carousel nav -->
                                                    <a class="left carousel-control" href="#myCarousel2" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
                                                    <a class="right carousel-control" href="#myCarousel2" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                                                    <a href="#0" style="text-decoration:none;" class="cd-see-all">See all</a>
                                                </div>								



                                                <div class="cd-testimonials-all">
                                                    <div class="cd-testimonials-all-wrapper">
                                                        <ul>
                                                            <li class="cd-testimonials-item">
                                                                <p>${complaint.description}</p>

                                                                <div class="cd-author">
                                                                    <img src="${complaint.loggedBy[0].profilePhoto}" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>${complaint.loggedBy[0].name}</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>
                                                        </ul>
                                                    </div>	<!-- cd-testimonials-all-wrapper -->

                                                    <a href="#0" class="close-btn">Close</a>
                                                </div> <!-- cd-testimonials-all -->


                                                <!-- cd-testimonials-pics-all -->
                                                <div class="cd-testimonials-pics-all">
                                                    <div class="cd-testimonials-pics-all-wrapper">
                                                        <ul>
                                                            <c:forEach items="${complaint.photos}" var="onePhoto">
                                                                <li class="cd-testimonials-pics-item">
                                                                    <div class="cd-author">
                                                                        <img src="${onePhoto.orgUrl}" class="reported_description_pic" alt="Author image">
                                                                        <ul class="cd-author-info">
                                                                            <li>${complaint.loggedBy[0].profilePhoto}</li>
                                                                            <li>Cessna Business Park</li>
                                                                        </ul>
                                                                    </div> <!-- cd-author -->
                                                                </li>
                                                            </c:forEach>
                                                        </ul>
                                                    </div>	<!-- cd-testimonials-pics-all-wrapper -->

                                                    <a href="#0" class="close-btn">Close</a>
                                                </div> <!-- cd-pics-all -->

                                                <!-- Comments Box -->

                                                <div id="load_comments_box" style="clear: both;"><a href="#!" id="comments_status" class="comments_controller">Comments from Users ( {{totalComments}} )</a>

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
                                <div>.</div>
                                <jsp:include page="footer.jsp" />
                            </body>
                        </html>
