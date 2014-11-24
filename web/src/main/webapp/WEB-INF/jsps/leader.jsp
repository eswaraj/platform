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
								<img src="http://www.thelovelyplanet.net/wp-content/uploads/2012/06/tajmahal_india_in_sunset.jpg" style="
									opacity: 0.4;
									position: fixed;
									height: 100%;
									width: 100%;
									z-index: -1;
								">

<div class="outerwrapper">
		<jsp:include page="header.jsp" />
		<!-- /.navbar -->
		<div class="container-fluid">
			<div class="row" style="margin-top: 5%;">
				<div class="col-sm-3">
					<div class="constituency_left_pane">
						<div class="leader_profile_pic_bio">
								<c:if test="${!empty leader.profilePhoto}">
									<img src="${leader.profilePhoto}?type=square&width=100&height=100" alt="profile-pic" class= "leader-profile-pic" style="width: 100px;">
									<p class="center-align">
										<strong class="red_orng_clr_text">${leader.name}</strong> <br /> 
										<c:if test="${!empty leader.politicalAdminType.shortName}">
											<span class="grey_text">
											 ${leader.politicalAdminType.shortName}
											</span> <br \>
											<span>In Office since ${leader.since}</span> <br \>
										</c:if>
										<c:if test="${empty leader.politicalAdminType.shortName}">
											<span class="grey_text">
											 Political Position To be Updated
											</span>
										</c:if>
									<a href="${locationUrl}" target="_blank" class="btn btn-info btn-block">Constituency
										Link</a>
									<hr>
									</p>
								</c:if>
								<c:if test="${empty leader.profilePhoto}">
									<img src="http://www.browserstack.com/images/dummy_avatar.png?type=square&width=80&height=80" alt="profile-pic" class="leader-profile-pic">
									<p class="center-align">
									<strong class="red_orng_clr_text">Leader Not Yet Declared</strong> <br /> 
										<span class="grey_text">
										 Political Position Not Yet Declared
										</span>
									</p>
								</c:if>
						</div>

						<div class="leader_profile_edu_stats">
							<div class="cons_issue_reporters_box_pic">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">Education Records</h3>
								</div>
								<ul class="list-group">
									<li class="list-group-item">Graduate Professional</li>
									<li class="list-group-item">B.Sc in 1977-78, B.ed in
										1980-81 from Pune Vidyapeeth</li>
								</ul>
							</div>
								<div class="complaints_followers_counter">
								<p class="reporter_total_complaints">
									<span class="grey_text">Complaints</span> <br /> 
									<strong class="blue_color_text">200</strong>
								</p>
								<p class="reporter_complaint_followers">
									<span class="grey_text">Followers</span> <br /> 
									<strong class="blue_color_text">5000</strong>
								</p>
								</div>
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
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="widget-wrapper">
								<h3>MLA related videos</h3>
								<div class="embed-responsive embed-responsive-4by3">
									<iframe class="embed-responsive-item" width="100%"
										src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
										allowfullscreen></iframe>
								</div>
								<div class="embed-responsive embed-responsive-4by3">
									<iframe class="embed-responsive-item" width="100%"
										src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
										allowfullscreen></iframe>
								</div>
								<div class="embed-responsive embed-responsive-4by3">
									<iframe class="embed-responsive-item" width="100%"
										src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
										allowfullscreen></iframe>
								</div>
								<div class="embed-responsive embed-responsive-4by3">
									<iframe class="embed-responsive-item" width="100%"
										src="//www.youtube.com/embed/ex6LhnQwunA" frameborder="0"
										allowfullscreen></iframe>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-9">
					<div class="about-mla hidden-xs">
						<h1>Know your MLA</h1>
						<div class="desc-wrapper">
							<div class="desc">
								<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
									Harum quia laboriosam adipisci assumenda quis natus eaque
									delectus, praesentium, vitae, cupiditate, aut similique! Ea
									voluptates quos alias officia vero provident sed veniam
									recusandae.</p>
								<button class="btn btn-default btn-xs">show less</button>
							</div>
						</div>
					</div>
					<hr>
					<h3 class="sub-header">MLA on Social Media</h3>
					<div class="row">
						<div class="col-sm-6">
							<div class="eswaraj-int-facebook">
								<div id="fb-root"></div>
								<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.0";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
								<div class="fb-like-box"
									data-href="https://www.facebook.com/${leader.fbPage}"
									data-show-border="false" data-height="400"
									data-colorscheme="light" data-show-faces="true"
									data-header="false" data-stream="true" data-show-border="true"></div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="eswaraj-int-twitter">
								<a class="twitter-timeline"
									href="https://twitter.com/${leader.twitterHandle}"
									data-widget-id="505705935222747136">Tweets by @${leader.twitterHandle}</a>
								<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
							</div>
						</div>
					</div>
					<h3 class="sub-header">MLA Calender</h3>
					<div class="row">
						<div class="col-sm-12">
							<img src="${staticHost}/images/mla-calendar.png" width="100%" alt="">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
