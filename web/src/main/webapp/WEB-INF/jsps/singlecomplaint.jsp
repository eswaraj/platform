<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
            <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


                        <!DOCTYPE html>
                        <html lang="en">
                            <head>
                                <title>e-Swaraj - Complaint ${complaint.id}</title>
                                <jsp:include page="include.jsp" />
                                <link rel="stylesheet" href="${staticHost}/css/dashboard.css">
                                <link rel="stylesheet" href="${staticHost}/css/div_list_row.css" />
                                
                                <script src="${staticHost}/js/masonry.pkgd.min.js"></script>
                                <script src="${staticHost}/js/jquery.flexslider-min.js"></script>
                                <script src="${staticHost}/js/jmpress.min.js"></script>
                                <script src="${staticHost}/js/carousel_image_slider.js"></script><!-- Script -->
                                <script src="${staticHost}/js/user_interaction.js"></script><!-- Script -->
                                <script type="text/javascript">
                                    $(function () {
                                        $('#dasky').Dasky()
                                    });
                                </script>

                                <!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
                                <script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>



                            </head>
                            <body>
                                <div class="outerwrapper">
                                    <jsp:include page="header.jsp" />

                                    <!-- Reference : http://codecanyon.net/item/dasky-timeline-slider/full_screen_preview/5071233 -->
                                    <div id="issue_timeline" class="issue_timeline_data">
                                        <iframe style="width: 100%; zoom: 4; height: 100px; border: none; margin: 0; padding:0; scrolling: no;" src="issue_timeline/issue_timeline.html"></iframe>
                                    </div>

                                    <div class="innerwrapper">

                                        <div class="col-md-3 md3_pad">

                                            <div class="issue_reporters_box_pic">

                                                <p class="text_reporters_p">
                                                    <a href="#!" class="text_reporters_anchor_pic">${complaint.categories} Complaints</a>
                                                </p>

                                                <div class="profile-pic profile-pic-padding">
                                                    <a href="#!" ><img src="images/trafficjam.png" class="complaints_issue_pic" alt=""></a>
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

                                                <p>
                                                    <a href="#" class="issue-scope">${complaint.categories}</a>
                                                    <a href="#" class="issue-scope-type"><img src = "images/potholeissue.jpg" class="issue_type_pic" alt="">Type - ${complaint.categories}</a>
                                                </p>

                                                <p class="whenwhere">
                                                    <span>
                                                        <i class="glyphicon glyphicon-map-marker"></i>
                                                        <a href="#" class="location">Cessna Business Park main road,Keverappa Layout</a>
                                                    </span>
                                                    <span>
                                                        <img src = "images/time.png" class="posttimestatus" alt="">
                                                        <a href="#" class="location">Latest Update : ${complaint.complaintTime}</a>
                                                    </span>
                                                </p>

                                                <p class="whom">
                                                    <a href="#" ><strong class="issue-id rightshift">Total Issues Raised : ${fn:length(complaint.loggedBy)}</strong>
                                                        <!-- social media share buttons -->								
                                                        <a class="addspacing" href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)"><img src="images/fbicon.png" alt="" align="middle" class="icon_resize"></a>		
                                                        <a href="https://plus.google.com/share?url={URL}" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
                                                        <a class="rightshift" href="https://twitter.com/share" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
                                                        <span>
                                                            <a href="#"><img src = "images/status_public_open.png" class="issue_status_pic" alt=""></a>
                                                            <a href="#"><img src = "images/status_politician_closed.png" class="issue_status_pic" alt=""></a>
                                                            <a href="#"><img src = "images/status_admin_closed.png" class="issue_status_pic" alt=""></a>
                                                        </span>
                                                        </p>

                                                <div id="myCarousel" class="carousel slide" data-ride="carousel">
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
                                                    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
                                                    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
                                                </div>

                                                <div id="myCarousel2" class="carousel slide" data-ride="carousel">
                                                    <!-- Carousel items -->
                                                    <div class="carousel-inner">
                                                        <div class="active item">
                                                            <ul class="issue_description">
                                                                <li>
                                                                    <p class="description_text"><span class="glyphicon glyphicon-bookmark rightshift"></span>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                                                                    <div class="description_author">
                                                                        <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                        <ul class="description_author_info">
                                                                            <li class="description_author_name">Somanth Nabajja</li>
                                                                            <li class="description_author_area"><span class="glyphicon glyphicon-map-marker"></span>Kadubeesanahalli</li>
                                                                        </ul>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                        <div class="item">
                                                            <ul class="issue_description">
                                                                <li>
                                                                    <p class="description_text"><span class="glyphicon glyphicon-bookmark rightshift"></span>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                                                                    <div class="description_author">
                                                                        <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                        <ul class="description_author_info">
                                                                            <li class="description_author_name">Shankar Mahadevan</li>
                                                                            <li class="description_author_area"><span class="glyphicon glyphicon-map-marker"></span>Bellandur</li>
                                                                        </ul>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                        <div class="item">
                                                            <ul class="issue_description">
                                                                <li>
                                                                    <p class="description_text"><span class="glyphicon glyphicon-bookmark rightshift"></span>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                                                                    <div class="description_author">
                                                                        <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                        <ul class="description_author_info">
                                                                            <li class="description_author_name">Ramesh Sharma</li>
                                                                            <li class="description_author_area"><span class="glyphicon glyphicon-map-marker"></span>Cessna Business Park</li>
                                                                        </ul>
                                                                    </div>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                        <div class="item">
                                                            <ul class="issue_description">
                                                                <li>
                                                                    <p class="description_text"><span class="glyphicon glyphicon-bookmark rightshift"></span>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                                                                    <div class="description_author">
                                                                        <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                        <ul class="description_author_info">
                                                                            <li class="description_author_name">Rishabh Shekhar</li>
                                                                            <li class="description_author_area"><span class="glyphicon glyphicon-map-marker"></span>Marathalli</li>
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
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Odit totam saepe iste maiores neque animi molestias nihil illum nisi temporibus.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Labore nostrum nisi, doloremque error hic nam nemo doloribus porro impedit perferendis. Tempora, distinctio hic suscipit. </p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Suresh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Exercitationem quibusdam eveniet, molestiae laborum voluptatibus minima hic quasi accusamus ut facere, eius expedita, voluptatem? Repellat incidunt veniam quaerat, qui laboriosam dicta. </p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Mahesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Libero voluptates officiis tempore quae officia! Beatae quia deleniti cum corporis eos perferendis libero reiciendis nemo iusto accusamus, debitis tempora voluptas praesentium repudiandae laboriosam excepturi laborum, nisi optio repellat explicabo, incidunt ex numquam. Ullam perferendis officiis harum doloribus quae corrupti minima quia, aliquam nostrum expedita pariatur maxime repellat, voluptas sunt unde, inventore.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ganesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Odit totam saepe iste maiores neque animi molestias nihil illum nisi temporibus.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Dinesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Perspiciatis quia quas, quis illo adipisci voluptate ex harum iste commodi nulla dolor. Eius ratione quod ab!</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ritesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Consequatur, dignissimos iure rem fugiat consequuntur officiis.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Harish Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. At temporibus tempora necessitatibus reiciendis provident deserunt maxime sit id. Dicta aut voluptatibus placeat quibusdam vel, dolore.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Corporis iusto sapiente, excepturi velit, beatae possimus est tenetur cumque fugit tempore dolore fugiat! Recusandae, vel suscipit? Perspiciatis non similique sint suscipit officia illo, accusamus dolorum, voluptate vitae quia ea amet optio magni voluptatem nemo, natus nihil.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolor quasi officiis pariatur, fugit minus omnis animi ut assumenda quod commodi, ad a alias maxime unde suscipit magnam, voluptas laboriosam ipsam quibusdam quidem, dolorem deleniti id.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. At temporibus tempora necessitatibus reiciendis provident deserunt maxime sit id. Dicta aut voluptatibus placeat quibusdam vel, dolore.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-item">
                                                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque tempore ipsam, eos suscipit nostrum molestias reprehenderit, rerum amet cum similique a, ipsum soluta delectus explicabo nihil repellat incidunt! Minima magni possimus mollitia deserunt facere, tempore earum modi, ea ipsa dicta temporibus suscipit quidem ut quibusdam vero voluptatibus nostrum excepturi explicabo nulla harum, molestiae alias. Ab, quidem rem fugit delectus quod.</p>

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
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
                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Suresh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Mahesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ganesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Dinesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ritesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Harish Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>

                                                            <li class="cd-testimonials-pics-item">

                                                                <div class="cd-author">
                                                                    <img src="images/profile-pic.jpg" class="reported_description_pic" alt="Author image">
                                                                    <ul class="cd-author-info">
                                                                        <li>Ramesh Sharma</li>
                                                                        <li>Cessna Business Park</li>
                                                                    </ul>
                                                                </div> <!-- cd-author -->
                                                            </li>
                                                        </ul>
                                                    </div>	<!-- cd-testimonials-pics-all-wrapper -->

                                                    <a href="#0" class="close-btn">Close</a>
                                                </div> <!-- cd-pics-all -->

                                                <!-- Comments Box -->

                                                <div id="load_comments_box" style="clear: both;"><a href="#!" id="comments_status" class="comments_controller">Comments from Users ( 3 )</a>

                                                    <div id="comments_box" class="div_comments_box">

                                                        <div id="add_comment"> 

                                                            <form id="comment_form">

                                                                <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>
                                                                <input id="user_input" type="text" class="user_input_text" title="Please add your comment here..."/>
                                                                <input id="user_input_button" type="button" value="Add Comment" class="comments_controller"</input>

                                                            </form>
                                                        </div>	

                                                        <!-- Old Comments -->

                                                        <!-- Comment #3 -->
                                                        <div id="old_comments_block">

                                                            <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>

                                                            <p class="comments_whom">
                                                                <strong class="issue-id">Comment #3 by</strong>
                                                                <a href="#" class="username">Somnath Nabajja</a>
                                                                <!-- social media share buttons -->								
                                                                <img src = "images/time.png" class="posttimestatus" alt="">
                                                                <a href="#" class="location">2 hrs ago</a>
                                                                <img src = "images/admin_ribbon.png" class="posttimestatus leftshift" alt="" title="Admin"></img>
                                                        </p>

                                                    <div class="comments-info" >

                                                        <p class="desc elipsis">
                                                            Massive traffics jams have been reported from many parts of Bangalore as rain lashed the Garden City this evening.
                                                            Water logging in several areas of the Region has made things worse for rush hour traffic.
                                                            Many people, stuck in traffic, have been tweeting about the jams.
                                                            "Too much traffic in Delhi and we are in castrpohic jam, (sic)" tweeted @anaam2720.
                                                        </p>

                                                    </div>

                                                </div>

                                                <!-- Comment #2 -->
                                                <div id="old_comments_block">

                                                    <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>

                                                    <p class="comments_whom">
                                                        <strong class="issue-id">Comment #2 by</strong>
                                                        <a href="#" class="username">Somnath Nabajja</a>
                                                        <!-- social media share buttons -->								
                                                        <img src = "images/time.png" class="posttimestatus" alt="">
                                                        <a href="#" class="location">2 hrs ago</a>
                                                        <img src = "images/mla_ribbon.png" class="posttimestatus leftshift" alt="" title="MLA"></img>
                                                </p>

                                            <div class="comments-info" >

                                                <p class="desc elipsis">
                                                    Massive traffics jams have been reported from many parts of Bangalore as rain lashed the Garden City this evening.
                                                    Water logging in several areas of the Region has made things worse for rush hour traffic.
                                                    Many people, stuck in traffic, have been tweeting about the jams.
                                                    "Too much traffic in Delhi and we are in castrpohic jam, (sic)" tweeted @anaam2720.
                                                </p>

                                            </div>

                                        </div>

                                        <!-- Comment #1 -->
                                        <div id="old_comments_block">

                                            <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>

                                            <p class="comments_whom">
                                                <strong class="issue-id">Comment #1 by</strong>
                                                <a href="#" class="username">Somnath Nabajja</a>
                                                <!-- social media share buttons -->								
                                                <img src = "images/time.png" class="posttimestatus" alt="">
                                                <a href="#" class="location">2 hrs ago</a>
                                            </p>

                                            <div class="comments-info" >

                                                <p class="desc elipsis">
                                                    Massive traffics jams have been reported from many parts of Bangalore as rain lashed the Garden City this evening.
                                                    Water logging in several areas of the Region has made things worse for rush hour traffic.
                                                    Many people, stuck in traffic, have been tweeting about the jams.
                                                    "Too much traffic in Delhi and we are in castrpohic jam, (sic)" tweeted @anaam2720.
                                                </p>

                                            </div>

                                            <div id="sub_comments_wrapper">
                                                <!-- Sub Comment #1 -->
                                                <div id="old_sub_comments_block">

                                                    <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>

                                                    <p class="comments_whom">
                                                        <strong class="issue-id">Comment #1 by</strong>
                                                        <a href="#" class="username">Somnath Nabajja</a>
                                                        <!-- social media share buttons -->								
                                                        <img src = "images/time.png" class="posttimestatus" alt="">
                                                        <a href="#" class="location">2 hrs ago</a>
                                                    </p>

                                                    <div class="comments-info" >

                                                        <p class="desc elipsis">
                                                            This is a sub comment.
                                                        </p>

                                                    </div>

                                                </div>

                                                <!-- Sub Comment #2 -->
                                                <div id="old_sub_comments_block">

                                                    <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>

                                                    <p class="comments_whom">
                                                        <strong class="issue-id">Comment #2 by</strong>
                                                        <a href="#" class="username">Somnath Nabajja</a>
                                                        <!-- social media share buttons -->								
                                                        <img src = "images/time.png" class="posttimestatus" alt="">
                                                        <a href="#" class="location">2 hrs ago</a>
                                                    </p>

                                                    <div class="comments-info" >

                                                        <p class="desc elipsis">
                                                            This is a sub comment.
                                                        </p>

                                                    </div>

                                                </div>

                                                <!-- Sub Comment #3 -->
                                                <div id="old_sub_comments_block">

                                                    <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>

                                                    <p class="comments_whom">
                                                        <strong class="issue-id">Comment #3 by</strong>
                                                        <a href="#" class="username">Somnath Nabajja</a>
                                                        <!-- social media share buttons -->								
                                                        <img src = "images/time.png" class="posttimestatus" alt="">
                                                        <a href="#" class="location">2 hrs ago</a>
                                                    </p>

                                                    <div class="comments-info" >

                                                        <p class="desc elipsis">
                                                            This is a sub comment.
                                                        </p>

                                                    </div>

                                                </div>


                                                <div id="add_sub_comment"> 

                                                    <form id="comment_form">

                                                        <a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a>
                                                        <input id="user_sub_com_input" type="text" class="user_sub_com_input_text" title="Please add your comment here..."/>
                                                        <input id="user_sub_com_input_button" type="button" value="Add Comment" class="comments_controller"</input>

                                                    </form>
                                                </div>	
                                            </div>
                                        </div>

                                        <div id="show_full_comments_page">
                                            <a href="#" id="collapse_comments_box" class="comments_controller">Collapse</a>
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