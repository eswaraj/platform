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
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>eSwaraj</title>
	<jsp:include page="include.jsp" />
</head>
<body>
	<div class="outerwrapper">
		<div class="container">
			<header>
			<jsp:include page="header.jsp" />
				<div class="row">
					<div class="col-sm-7">
					<div class="logo">
							<a href="index.html">
								<img src="${staticHost}/images/eswaraj-logo.png" alt="logo"></a>
						</div>
						
						<div class="heading">
							<h1>
								<span>eSwaraj -</span>
								Leveraging Mobile
								<br>&amp; Internet for a Better Governance</h1>
							<p>
								E-Swaraj is for everyone. It helps the common people to report about issues in their city. And it helps the government to govern better...
								<a href="#know-more" class="scroll-to">know More</a>
							</p>
							<div class="dwld">
								<a href="https://play.google.com/store/apps/details?id=com.next.eswaraj" target="_blank">
									<img src="${staticHost}/images/android.png" alt="android link"></a>
								<a href="https://itunes.apple.com/in/app/eswaraj/id689751495?mt=8" target="_blank">
									<img src="${staticHost}/images/app-store.png" height="65" width="200" alt="android link"></a>
							</div>
							<div class="for-user">
							
						</div>
					</div>
				</div>
				<div class="col-sm-5 mobile-anim">
					<img src="${staticHost}/images/mobile.png" alt=""></div>
			</div>
			
</header>
<div class="poster"  id="know-more">
	<div class="row">
		<div class="col-xs-12">
			<img src="${staticHost}/images/eswaraj-poster.png" alt="banner"></div>
	</div>
	<div class="highlight">
		<h2>
			A simple App to report
			<br>and track everyday issues.</h2>
		<p>Scroll along to explore how it helps!</p>
	</div>
</div>
<div class="contentwrapper" id="know-more">
	<div class="content">
		<div class="row">
			<div class="col-sm-6">
				<img src="${staticHost}/images/eswaraj-01.png" alt=""></div>
			<div class="col-sm-6">
				<h3>Signing the constitution.</h3>
				<p>
					We Came together and  signed a contract named Constitution which  in turn paved the way for founding stone of this great nation , Contract that guaranteed that your life will be much better off by being part of the nation then living individually. Contract that promised together we can be much more than then individually.
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-push-6">
				<img src="${staticHost}/images/eswaraj-02.png" alt=""></div>
			<div class="col-sm-6 col-sm-pull-6">
				<h3>Administrative structure.</h3>
				<p>
					A system was laid out to achieve things in the constitution. Considering everyone’s similar sort of basic needs, India got divided in parts with same administrative/political structure,  repeated everywhere  in the country.
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<img src="${staticHost}/images/eswaraj-03.png" alt=""></div>
			<div class="col-sm-6">
				<h3>The need for social audit.</h3>
				<p>
					Even the greatest of plans fall short without ground-level feedback . Eswaraj aims at removing disconnect between ground-level realities and top-level perceptions and bringing  transparency in governance. So now, relevant people can focus on solving things instead of worrying about
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-push-6">
				<img src="${staticHost}/images/eswaraj-04.png" alt=""></div>
			<div class="col-sm-6 col-sm-pull-6">
				<h3>Analytics and more.</h3>
				<p>
					Visual analytics offering deeper insights  to improve governance. Per constituency/colony based map view to locate problems.
				</p>
				<ul class="analytics-plus">
					<li>
						<img src="${staticHost}/images/eswaraj-analytics.png" alt="">
						<span>Train Administrative staff</span>
					</li>
					<li>
						<img src="${staticHost}/images/eswaraj-spending.png" alt="">
						<span>Locate areas &amp; amenities that need more spending.</span>
					</li>
					<li>
						<img src="${staticHost}/images/eswaraj-improvements.png" alt="">
						<span>
							Find areas and amenities that need quality improvement and maintenance.
						</span>
					</li>
					<li>
						<img src="${staticHost}/images/eswaraj-awareness.png" alt="">
						<span>Find out amenities and areas that need awareness programme.</span>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="team placeholder-wrapper" id="team">
			<h3>Our Team</h3>
			<div class="col-xs-6 col-sm-4 placeholder">
				<img  data-src="holder.js/200x200/auto/sky" class="img-responsive img-default" alt="200x200" src="${staticHost}/images/rakesh.jpg">
				<h4>Rakesh Pratap Singh</h4>
				<span class="text-muted">
					I am a technologist with special interest in politics , education and finance.
				</span>
			</div>
			<div class="col-xs-6 col-sm-4 placeholder">
				<img  data-src="holder.js/200x200/auto/vine" class="img-responsive img-default" alt="200x200" src="${staticHost}/images/ravi.jpg">
				<h4>Ravi Sharma</h4>
				<span class="text-muted">
					Socialist and Technolgist at heart, working towards making india a better place 
				</span>
			</div>
			<div class="col-xs-6 col-sm-4 placeholder">
				<img  data-src="holder.js/200x200/auto/sky" class="img-responsive img-default" alt="200x200" src="${staticHost}/images/pradeep.jpg">
				<h4>Pradeeo Kumar Sonkar</h4>
				<span class="text-muted">
					Socialist and Technolgist at heart, working towards making india a better place 
				</span>
			</div>
			<div class="col-xs-6 col-sm-4 placeholder">
				<img  data-src="holder.js/200x200/auto/vine" class="img-responsive img-default" alt="200x200" src="${staticHost}/images/vaibhav.jpg">
				<h4>Vaibhav Sinha</h4>
				<span class="text-muted">
					Socialist and Technolgist at heart, working towards making india a better place 
				</span>
			</div>
			<div class="col-xs-6 col-sm-4 placeholder">
				<img  data-src="holder.js/200x200/auto/sky" class="img-responsive img-default" alt="200x200" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iIzBEOEZEQiIvPjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojZmZmO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4=">
				<h4>Label</h4>
				<span class="text-muted">
					Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quae odio iure sit.
				</span>
			</div>
			<div class="col-xs-6 col-sm-4 placeholder">
				<img  data-src="holder.js/200x200/auto/vine" class="img-responsive img-default" alt="200x200" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iIzM5REJBQyIvPjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojMUUyOTJDO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4=">
				<h4>Label</h4>
				<span class="text-muted">
					Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quo modi tempora beatae!
				</span>
			</div>
		</div>
	</div>
</div>
<jsp:include page="footer.jsp" />
</div>
</div>

</body>
</html>