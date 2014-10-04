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
</head>
<body>
	<div class="outerwrapper">
		<jsp:include page="header.jsp" />
		<!-- /.navbar -->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
					<div class="panel panel-default mla-placeholder">
						<div class="panel-body">
							<div class="mla-profile">
								<img src="${leader.profilePic}" alt="mla image">
								<p>
									<strong>${leader.name}, ${leader.politicalAdminType.shortName}</strong> <span>In Office
										since ${leader.since}</span> <a href="#" class="badge badge-default">Know
										your MLA</a>
								</p>
								<div class="visible-xs">
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing
										elit. Dolores, illum facilis aut officiis, molestias saepe
										beatae laudantium dolorum labore facere eaque mollitia, ab
										pariatur. At nostrum magni fugit commodi sequi!</p>
									<button class="btn btn-default btn-xs">show more /
										show less</button>
								</div>
							</div>
						</div>
					</div>
					<div class="row mla-record">
						<div class="col-sm-12">
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
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-body">
							<a href="#" target="_blank" class="btn btn-info btn-block">Constituency
								Link</a>
							<hr>
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
