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
		<div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="index.php"> <img
						src="${staticHost}/images/eswaraj-dashboard-logo.png" class="pull-left" alt="">
						<span pull-left="">e-Swaraj</span>
					</a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav">
						<li><a href="index.php"><i
								class="glyphicon glyphicon-home"></i></a></li>
						<li class="active"><a href="05-constituency.php">My
								Constituency</a></li>
						<li><a href="07-my-mla.php">My MLA</a></li>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li class="active"><a href="10-profile-pending.php"> <i
								class="glyphicon glyphicon-user"></i>
						</a></li>
					</ul>
				</div>
				<!-- /.nav-collapse -->
			</div>
		</div>
		<!-- /.navbar -->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-3">
					<div class="panel panel-default mla-placeholder">
						<div class="panel-body">
							<div class="mla-profile">
								<img src="${staticHost}/images/issues/issues.png" alt="mla image">
								<p>
									<strong>Rahuram Desai, MLA</strong> <span>In Office
										since Jan, 2014</span> <a href="#" class="badge badge-default">Know
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
									data-href="https://www.facebook.com/FacebookDevelopers"
									data-show-border="false" data-height="400"
									data-colorscheme="light" data-show-faces="true"
									data-header="false" data-stream="true" data-show-border="true"></div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="eswaraj-int-twitter">
								<a class="twitter-timeline"
									href="https://twitter.com/somnath_sn"
									data-widget-id="505705935222747136">Tweets by @somnath_sn</a>
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
