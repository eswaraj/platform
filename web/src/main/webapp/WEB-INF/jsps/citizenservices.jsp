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
<jsp:include page="include.jsp" />
</head>
<body>
	<div class="container-fluid cs-wrapper">
		<header>
			<jsp:include page="header.jsp" />
		</header>
		<div class="row">
			<div class="col-sm-3">
				<c:if test="${loggedIn}">
					<div class="panel panel-default">
						<div class="panel-body user-profile">
							<img src="${user.person.profilePhoto}/picture?type=large"
								alt="profile-image"> 
						</div>
					</div>
				</c:if>
                    <div id="toc"></div>
			</div>
			<div class="col-sm-9">
				<article>
					<c:forEach items="${rootCategories}" var="oneCategory">
						<div class="article-body">
							<h2>${oneCategory.name}</h2>
							<!-- Nav tabs -->
							<ul class="nav nav-tabs" role="tablist">
								<li class="active"><a href="#home-1" role="tab"
									data-toggle="tab"> <i
										class="glyphicon glyphicon-facetime-video" title="Video"></i>
								</a></li>
								<li><a href="#profile-1" role="tab" data-toggle="tab">
										<i class="icomoon icomoon-file3" title="Article"></i>
								</a></li>
								<li><a href="#messages-1" role="tab" data-toggle="tab">
										<i class="icomoon icomoon-bars" title="Infographic"></i>
								</a></li>
							</ul>
							<!-- Tab panes -->
							<div class="tab-content">
								<div class="tab-pane active" id="home-1">
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing
										elit. Delectus veritatis repudiandae velit qui necessitatibus,
										ipsa, dicta quasi saepe illum facilis, ut quas. Quidem
										excepturi, nobis blanditiis ipsum libero!</p>
									<iframe width="630" height="470"
										src="//www.youtube.com/embed/QtNImqwB7DY" frameborder="0"
										allowfullscreen></iframe>
									<div class="share-plugin">
										<div class="btn-group">
											<button type="button" class="btn btn-default disabled">
												<i class="icomoon icomoon-share"></i>
											</button>
											<button type="button" class="btn btn-default">
												<i class="icomoon icomoon-facebook"></i>
											</button>
											<button type="button" class="btn btn-default">
												<i class="icomoon icomoon-twitter"></i>
											</button>
										</div>
									</div>
								</div>
								<div class="tab-pane" id="profile-1">
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing
										elit. Expedita neque quo ad. Sint velit quaerat nostrum,
										perspiciatis sapiente amet reprehenderit.</p>
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing
										elit. Aliquam consequatur rerum, dolores ipsam fuga ullam
										sequi modi voluptatum quibusdam. Non quo similique
										perspiciatis accusantium corporis deleniti assumenda, minus
										quibusdam quas laudantium quae eum placeat quidem expedita.
										Iste accusamus id, ab rem, iusto soluta amet! Quis iure, quos
										excepturi! Rem sit, laudantium eveniet culpa voluptates
										explicabo sapiente veniam at.</p>
									<ul>
										<li>Lorem ipsum dolor.</li>
										<li>Lorem ipsum dolor sit amet, consectetur adipisicing
											elit. Soluta.</li>
										<li>Lorem ipsum dolor sit.</li>
										<li>Lorem ipsum dolor sit amet, consectetur adipisicing
											elit. Fuga ratione ea iusto, magni!</li>
									</ul>
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing
										elit. Similique rerum recusandae necessitatibus blanditiis
										nemo adipisci. Laudantium excepturi qui, suscipit assumenda
										iusto culpa minima recusandae nisi!</p>
									<a href="#">show more/show less</a>
									<div class="share-plugin">
										<div class="btn-group">
											<button type="button" class="btn btn-default disabled">
												<i class="icomoon icomoon-share"></i>
											</button>
											<button type="button" class="btn btn-default">
												<i class="icomoon icomoon-facebook"></i>
											</button>
											<button type="button" class="btn btn-default">
												<i class="icomoon icomoon-twitter"></i>
											</button>
										</div>
									</div>
								</div>
								<div class="tab-pane" id="messages-1">
									<button class="btn btn-default btn-xs">Download</button>
									<button class="btn btn-default btn-xs">Large View</button>
									<img src="${staticHost}/images/infographic.jpg"
										alt="infographic" class="small-img infographic">
									<div class="share-plugin">
										<div class="btn-group">
											<button type="button" class="btn btn-default disabled">
												<i class="icomoon icomoon-share"></i>
											</button>
											<button type="button" class="btn btn-default">
												<i class="icomoon icomoon-facebook"></i>
											</button>
											<button type="button" class="btn btn-default">
												<i class="icomoon icomoon-twitter"></i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>

				</article>
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			$("#toc").tocify({
				selectors : "h2",
				extendPage : false,
				scrollTo : 69,
			});
		});
	</script>
	<jsp:include page="footer.jsp" />
</body>
</html>
