<div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="index.php">
						<img src="images/eswaraj-dashboard-logo.png" class="pull-left" alt="">
						<span pull-left="">e-Swaraj</span>
					</a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav">
						<li>
							<a href="index.php"> <i class="glyphicon glyphicon-home"></i>
							</a>
						</li>
						<li class="active">
							<a href="05-constituency.php">My Constituency</a>
						</li>
						<li>
							<a href="07-my-mla.php">My MLA</a>
						</li>
						<li>
							<a href="08-citizen-services.php">Citizen Services</a>
						</li>
					</ul>
					
					<c:if test="${!loggedIn}">					
					<ul class="nav navbar-nav navbar-right">
								 <li class="download">
								<a href="#">
									<span>
										<img src="images/android-icon.png"  alt=""></span>
								</a>
							</li>
							<li class="download">
								<a href="#">
									<span>
										<img src="images/apple-icon.png" alt=""></span>
								</a>
							</li>		
						<li>
							<div onclick="location.href='05-constituency.php';" class="btn-group">
								<button class="btn btn-fb btn-sm" type="button">
									<i class="icomoon icomoon-facebook"></i>
								</button>
								<button class="btn btn-fb btn-sm" type="button">Log in with Facebook</button>
							</div>
						</li>
					</ul>					
					</c:if>
					
					<c:if test="${loggedIn}">
					<ul class="nav navbar-nav navbar-right">
								 <li class="download">
								<a href="#">
									<span>
										<img src="images/android-icon.png"  alt=""></span>
								</a>
							</li>
							<li class="download">
								<a href="#">
									<span>
										<img src="images/apple-icon.png" alt=""></span>
								</a>
							</li>
					<li class="dropdown user-profile">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<img src="${user.person.profilePhoto}" alt="profile-pic">
								${user.person.name}
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menu">
								<li>
									<a href="10-profile-pending.php">View Profile</a>
								</li>
								
								<li>
									<a href="/web/logout">Logout</a>
								</li>
								
							</ul>
						</li>
					</ul>
					</c:if>	
				</div>
				<!-- /.nav-collapse -->
			</div>
		</div>
		<!-- /.navbar -->