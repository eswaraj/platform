<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                            </head>
                            <body>
								<!--img src="https://c1.staticflickr.com/9/8540/10201285295_15b4859929_b.jpg" style="
									opacity: 0.5;
									position: fixed;
									height: 100%;
									width: 100%;
									z-index: -1;
								"-->                                
								<img src="http://www.thelovelyplanet.net/wp-content/uploads/2012/06/tajmahal_india_in_sunset.jpg" style="
									opacity: 0.4;
									position: fixed;
									height: 100%;
									width: 100%;
									z-index: -1;
								">
								<div class="">
                                    <jsp:include page="header.jsp" />
                                    <script>
                                        jQuery(document).ready(function() {
                                            jQuery("abbr.timeago").timeago();
										$('.anchorlink').click(function(e){
											e.stopPropagation();
										});
                                        });
                                    </script>
                                    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&v=3.exp&libraries=visualization"></script>
                                    <c:if test="${viewType eq 'map'}">
                                        <script src="${staticHost}/js/markerclusterer.js" type="text/javascript"></script>
                                        <script src="${staticHost}/js/mapview.js" type="text/javascript"></script>
                                        <script>
                                            var kml_path = "${location.kml}";
                                            var complaints = [
                                                <%int i = 0;%>  
                                                <c:forEach items="${complaintList}" var="oneComplaint">
                                                <%if (i > 0)
                                                out.println(",");
                                                i++;
                                                %>
                                                {lat:${oneComplaint.lattitude},lng:${oneComplaint.longitude},data:{id:${oneComplaint.id},category:"${oneComplaint.categoryTitle}",address:"Coming Soon",date: "${oneComplaint.complaintTimeIso}", userId: "${oneComplaint.loggedBy[0].id}", userName: "TODO", userImgUrl : "http://www.panoramio.com/user/4483", complaintImgUrl: "http://www.panoramio.com/user/4483"}}
                                            </c:forEach>
                                            ];
                                        </script>
                                    </c:if>

                                    <c:if test="${viewType eq 'analytics'}">
                                        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
                                        <script src="${staticHost}/js/analytics.js" type="text/javascript"></script>
                                        <script src="${staticHost}/js/d3.min.js" type="text/javascript"></script>
                                        <script src="${staticHost}/js/nv.d3.min.js" type="text/javascript"></script>
                                        <link rel="stylesheet" href="${staticHost}/css/nv.d3.css">
                                        <link rel="stylesheet" href="${staticHost}/css/analytics.css">
                                        <script type="text/javascript">
                                            var analyticsData = <c:out value="${locationCounters}" escapeXml="false" /> ;

                                        </script>
                                    </c:if>
                                    <div class="container-fluid">
                                        <div class="row">
											<div class="constituency_left_pane">
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
														<p class="center-align">
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
														<p class="reporter_total_complaints">
															<span class="grey_text">Complaints</span> <br /> 
															<strong class="blue_color_text">200</strong>
														</p>
														<p class="reporter_complaint_followers">
															<span class="grey_text">Followers</span> <br /> 
															<strong class="blue_color_text">5000</strong>
														</p>
														<p class="reporter_complaint_visited">
															<span class="grey_text">Following</span> <br /> 
															<strong class="blue_color_text">50</strong>
														</p>
														</div>
													</div>
												</div>

												<div class="left_filter">
													<p>
														<strong class="ref_search">Refine Search</strong>
													</p>
													<div class="refine-search">
														<input type="text" value="Amsterdam,Washington" data-role="tagsinput">
													</div>
												</div>
												<p>
													<strong class="filter_category">Filter Issues by category</strong>
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
											<div class="constituency_mid_pane">
												<div class="listing-wrapper">
													<div class="secondary-wrapper">
														<div class="views_div">
																<a type="button" href="#!" title="Location View">
																<div class="cons_location_name">
																	<span class="location_text_adjust">${location.name}</span>
																	<img src=" http://www.naturalhighsafaris.com/cdn/cache/made/cdn/uploads/country_images/India/North/Delhi/India-Gate--Delhi-Photos2_940_529_80_s_c1.jpg" class="location_image">
																</div>
																</a>	
															<c:if test="${viewType eq 'list'}">
																<a type="button" href="#" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
																<a type="button" href="?type=map" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
																<a type="button" href="?type=analytics" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>
															</c:if>
															<c:if test="${viewType eq 'map'}">
																<a type="button" href="?type=list" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
																<a type="button" href="#" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
																<a type="button" href="?type=analytics" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>
															</c:if>
															<c:if test="${viewType eq 'analytics'}">
																<a type="button" href="?type=list" title="List View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-list glyphicon_margin"></span><span class="list_view_text_margin">List View</span></span></div></a>
																<a type="button" href="?type=map" title="Map View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-map-marker glyphicon_margin"></span><span class="map_view_text_margin">Map View</span></span></div></a>
																<a type="button" href="#" title="Analytics View"><div><span class="frame_glyphicon"><span class="glyphicon glyphicon-signal glyphicon_margin"></span><span class="ay_view_text_margin">Analytics View</span></span></div></a>
															</c:if>

														</div>
														<div class="clearfix"></div>
													</div>
													<c:if test="${viewType eq 'list'}">

														<div class="listing">
															<!-- new_div starts -->
															<c:forEach items="${complaintList}" var="oneComplaint">
																<div class="list-row" onclick="window.location='http://dev.eswaraj.com/complaint/${oneComplaint.id}.html'; return false;" style="cursor:pointer;">
																	<p class="innerdiv-sharebtn">
																		<!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
																		<script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>
																		<!-- social media share buttons -->								
																		<a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)" class="anchorlink" ><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>		
																		<a href="https://plus.google.com/share?url=http://www.eswaraj.com/" class="anchorlink" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
																		<a href="https://twitter.com/share" class="anchorlink" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
																	</p>
																	<div class="innerblock">
																		<div class="col-sm-1 profile-info profile_pic_adjust">
																			<div class="profile-pic">
																				<c:if test="${!empty oneComplaint.loggedBy[0].photo}">
																					<a href="#!" class="anchorlink" ><img src="${oneComplaint.loggedBy[0].photo}" alt=""></a>
																				</c:if>
																				<c:if test="${empty oneComplaint.loggedBy[0].photo}">
																					<a href="#!" class="anchorlink" ><img src="${staticHost}/images/anonymous_profile_pic.png" alt="" style="width: 50px; max-width: 50px; border: 1px solid #ccc;" ></a>
																				</c:if>
																			</div>
																		</div>
																		<div class="col-sm-10 profile-info profile_info_adjust">
																			<p class="whom">
																				<strong class="issue-id">Issue #${oneComplaint.id}</strong>
																				<span class="connector">raised by</span>

																				<span class="username text-limit name_adjust">
																					<c:forEach items="${oneComplaint.loggedBy}" var="onePerson">
																						<a href="#!" class="anchorlink" >${onePerson.name}</a>
																					</c:forEach>
																				</span>
																				<span class="issue-scope-type text-limit type_adjust">
																					<img src = "${staticHost}/images/potholeissue.jpg" class="issue_type_pic" alt="">
																					<c:forEach items="${oneComplaint.categories}" var="oneCategory">
																						<c:if test="${oneCategory.root}">
																							<a href="${location.url}/category/${oneCategory.id}.html?type=${viewType}" class="anchorlink" >Type - ${oneCategory.name}</a>
																						</c:if>
																					</c:forEach>
																				</span>
																			</p>

																			<p class="whenwhere">
																				<span>
																					<img src = "${staticHost}/images/time.png" class="posttimestatus" alt="">
																					<a href="#!" class="anchorlink" >
																					<span class="location">
																						<abbr class="timeago" title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr>
																					</span>
																					</a>
																				</span>
																				<span class="connector">at</span>
																				<span>
																					<i class="glyphicon glyphicon-map-marker"></i>
																					<a href="#!" class="anchorlink" ><span class="location">Cessna Business Park main road,Keverappa Layout</span></a>
																				</span>
																				<span>
																					<a href="#!" class="anchorlink" ><img src = "${staticHost}/images/underreview.png" class="postcurrentstatus" alt=""></a>
																				</span>
																			</p>
																		</div>
																		<div class="issue-info" >

																			<p>
																				<a href="${location.url}/category/${oneComplaint.subCategoryId}.html?type=${viewType}" class="anchorlink" ><span class="issue-scope">${oneComplaint.categoryTitle}</span></a>
																			</p>

																			<c:if test="${!empty oneComplaint.description}">
																				<p class="desc elipsis">
																					${oneComplaint.description}
																				</p>
																			</c:if>

																			<c:if test="${!empty oneComplaint.photos}">
																				<div class="issue-pic">
																					<img src="${oneComplaint.photos[0].orgUrl}" alt="" align="middle">
																				</div>
																			</c:if>

																		</div>
																	</div>
																</div>
															</c:forEach>
															<!-- new_div ends  -->
															<div class="pagination-wrapper">
																<ul class="pagination">
																	<c:if test="${enableFirst}">
																		<li class="active"><a href="?page=1">&laquo;</a></li>
																	</c:if>
																	<c:if test="${!enableFirst}">
																		<li class="disabled"><a href="#">&laquo;</a></li>
																	</c:if>
																	<c:forEach items="${pages}" var="onePage">
																		<c:if test="${onePage eq currentPage}">
																			<li class="active"><a href="?page=${onePage}">${onePage}</a>
																			</li>
																		</c:if>
																		<c:if test="${onePage ne currentPage}">
																			<li><a href="?page=${onePage}">${onePage}</a></li>
																		</c:if>

																	</c:forEach>
																	<c:if test="${enableLast}">
																		<li class="active"><a href="?page=${totalPages}">&raquo;</a></li>
																	</c:if>
																	<c:if test="${!enableLast}">
																		<li class="disabled"><a href="#">&raquo;</a></li>
																	</c:if>

																</ul>
															</div>
														</div>
													</c:if>
													<c:if test="${viewType eq 'map'}">
														<div id="panel">
															<button onclick="createHeatmap()">Heatmap View</button>
															<button onclick="createCluster()">Cluster View</button>
															<button onclick="createMarker()">Marker View</button>
														</div>
														<div id="map-container">
															<div id="map-canvas"></div>
														</div>
													</c:if>
													<c:if test="${viewType eq 'analytics'}">
														<div class="listing-wrapper">
															<div class="secondary-wrapper">
																<div class="pull-left">
																	<h1><strong>Analytics</strong></h1><br/>
																</div>
																<div id="filter_list">
																</div>

																<div id="chart_ts">
																	<svg>
																	</svg>
																</div>
																<div id="chart_bar_c">
																	<svg>
																	</svg>
																</div>
																<div id="chart_bar_s">
																	<svg>
																	</svg>
																</div>

																<div id="chart_pie_c">
																	<svg>
																	</svg>
																</div>
																<div id="chart_pie_s">
																	<svg>
																	</svg>
																</div>
															</div>
														</div>
													</c:if>
												</div>
											</div>
											<div class="constituency_right_pane">
												<div class="right_profile">
													<div id="myCarousel" class="carousel slide" data-ride="carousel">
														<!-- Carousel items -->
														<div class="carousel-inner">

															<c:if test="${!empty leaders}">
																<c:forEach items="${leaders}" var="oneLeader"  varStatus="counter">
																	<c:choose>
																		<c:when test="${counter.count == '1'}">
																			<div class="active item">
																				<img src="${oneLeader.profilePhoto}?type=square&height=200&width=200" class="politician_image" alt="Leader image"><br \>
																				<a href="${oneLeader.urlIdentifier}.html"><strong>${oneLeader.name}, ${oneLeader.politicalAdminType.shortName}</strong></a><br \>
																				<p>In Office since ${oneLeader.since}</p>
																			</div>
																		</c:when>

																		<c:otherwise>
																			<div class="item">
																				<img src="${oneLeader.profilePhoto}?type=square&height=200&width=200" class="politician_image" alt="Leader image"><br \>
																				<a href="${oneLeader.urlIdentifier}.html"><strong>${oneLeader.name}, ${oneLeader.politicalAdminType.shortName}</strong></a><br \>
																				<p>In Office since ${oneLeader.since}</p>
																			</div>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</c:if>
														</div>
														
														<c:if test="${fn:length(leaders) > 1}">
															<!-- Carousel nav -->
															<a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
															<a class="right carousel-control" href="#myCarousel" role="button" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
														</c:if>
													</div>

													<div class="platform_constituency_counter">
													<p class="p_c_counter_loc red_orng_clr_text"><span>${location.name} Constituency</span></p>
													<p class="total_cons_population">
														<span class="grey_text">Total Population</span> <br /> 
														<strong class="blue_color_text">9.879 million</strong>
													</p>
													<p class="total_cons_area">
														<span class="grey_text">Total Area</span> <br /> 
														<strong class="blue_color_text">1,484 km²</strong>
													</p>
													<p class="total_const_count">
														<span class="grey_text">Total Constituencies</span> <br /> 
														<strong class="blue_color_text">70</strong>
													</p>
													<p class="total_voters">
														<span class="grey_text">Total Voters</span> <br /> 
														<strong class="blue_color_text">56458975</strong>
													</p>
													<p class="total_eswaraj_reg_voters">
														<span class="grey_text">Voters with eSwaraj</span> <br /> 
														<strong class="blue_color_text">42453975</strong>
													</p>
													<p class="total_eswaraj_const_coverage">
														<span class="grey_text">eSwaraj Coverage</span> <br /> 
														<strong class="blue_color_text">90%</strong>
													</p>
													</div>

													<div class="social_media_sharing_buttons">

													<p class="follow_eswaraj_box red_orng_clr_text"><span>Follow eSwaraj on: </span></p>
														<div id="fb-root"></div>
														<script>(function(d, s, id) {
														  var js, fjs = d.getElementsByTagName(s)[0];
														  if (d.getElementById(id)) return;
														  js = d.createElement(s); js.id = id;
														  js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.0";
														  fjs.parentNode.insertBefore(js, fjs);
														}(document, 'script', 'facebook-jssdk'));</script>														
														<div class="fb-like" data-href="https://www.facebook.com/eSwarajApp?ref=hl" data-width="5" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>></div>
														
														<br />
														
														<a href="https://twitter.com/share" class="twitter-share-button" data-url="https://twitter.com/eSwarajApp" data-text="Follow eSwaraj :" data-via="eSwarajApp" data-size="large">Tweet</a>
														<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>
													
													</div>
												</div>
											</div>
                                    </div>
                                </div>
                                </div>
                            <jsp:include page="footer.jsp" />
                            </body>
                        </html>