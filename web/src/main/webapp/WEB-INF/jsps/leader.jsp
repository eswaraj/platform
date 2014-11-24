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
<link rel="stylesheet" href="${staticHost}/css/leader.css" />
</head>
<body style="height:auto;">
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
											</span>
										</c:if>
									<a href="${locationUrl}" target="_blank" class="btn btn-info btn-block">Constituency
										Link</a>
									<hr>
									</p>
								</c:if>
								<c:if test="${empty leader.profilePhoto}">
									<img src="http://www.browserstack.com/images/dummy_avatar.png?type=square&width=80&height=80" alt="profile-pic" class="leader-profile-pic">
									<p class="center-align name-top-adjust">
									<strong class="red_orng_clr_text">Leader Not Yet Declared</strong> <br /> 
										<span class="grey_text">
										 Position Not Yet Declared
										</span>
									</p>
								</c:if>
						</div>

						<div class="leader_profile_edu">
								<div class="leader_educational_bg">
									<div>
										<h5 class="red_orng_clr_text">Educational Records</h5>
									</div>
									<ul>
										<li class="grey_text">B.Sc in 1977-78 | Pune Vidyapeeth</li>
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
				</div>
				<div class="col-sm-6">
					<div class="about-mla hidden-xs">
						<h2 class="blue_color_text">Know your MLA</h2>
						<div class="desc-wrapper">
							<div class="desc">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Harum quia laboriosam adipisci assumenda quis natus eaque
									delectus, praesentium, vitae, cupiditate, aut similique! Ea
									voluptates quos alias officia vero provident sed veniam
									recusandae.</p>
							</div>
						</div>
					</div>
						<h3 class="blue_color_text">MLA Calender</h3>
					<div class="mla_cal_contents">
						<img src="${staticHost}/images/mla-calendar.png" width="100%" alt="">
					</div>
				</div>
				<div class="col-sm-3">
					<h3 class="blue_color_text">MLA on Social Media</h3>
					<div class="eswaraj-int-facebook">
						<div class="fb-like-box" data-href="https://www.facebook.com/narendramodi" data-width="307" data-colorscheme="light" data-show-faces="false" data-header="false" data-stream="true" data-show-border="false"></div>
						<!--div class="fb-like-box"
							data-href="https://www.facebook.com/${leader.fbPage}"
							data-show-border="false" data-height="400"
							data-colorscheme="light" data-show-faces="true"
							data-header="false" data-stream="true" data-show-border="true"></div-->
					</div>
					<hr />
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
														src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
														allowfullscreen></iframe>
												</div>
												<div class="item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
														allowfullscreen></iframe>
												</div>
												<div class="item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
														allowfullscreen></iframe>
												</div>
												<div class="item embed-responsive embed-responsive-4by3">
													<iframe class="embed-responsive-item" width="100%"
														src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
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
	
	<jsp:include page="footer.jsp" />
    <script src="${staticHost}/js/bootstrap-tagsinput-bloodhound.js"></script>
	<script type="text/javascript" src="${staticHost}/js/typeahead.bundle.js"></script>    
	<script src="${staticHost}/js/filter_settings.js"></script>
</body>
</html>
