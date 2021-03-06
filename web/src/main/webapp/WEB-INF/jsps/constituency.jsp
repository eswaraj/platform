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
                                <!-- <img src="http://www.thelovelyplanet.net/wp-content/uploads/2012/06/tajmahal_india_in_sunset.jpg" style="
                                    opacity: 0.4;
                                    position: fixed;
                                    height: 100%;
                                    width: 100%;
                                    z-index: -1;
                                "> -->
                                <div class="main_content_page">
                                    <jsp:include page="header.jsp" />
                                    <script>
                                    jQuery(document).ready(function() {
                                        jQuery("abbr.timeago").timeago();
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
                                                {lat:${oneComplaint.lattitude},lng:${oneComplaint.longitude},data:{id:${oneComplaint.id},category:"${oneComplaint.categoryTitle}",address:"Coming Soon",date: "${oneComplaint.complaintTimeIso}", userId: "${oneComplaint.createdBy[0].id}", userName: "TODO", userImgUrl : "http://www.panoramio.com/user/4483", complaintImgUrl: "http://www.panoramio.com/user/4483"}}
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
                                            <div class="constituency_left_pane col-sm-3 col-md-3">
                                                <div class="reporter_cover_profile_quote">
                                                    <div class="cons_issue_reporters_box_cover_quote">
                                                        <p>“It is Swaraj when we learn to rule ourselves. It is, therefore, in the palm of our hands. But such Swaraj has to be experienced, by each one for himself.”</p> 
                                                    </div>
                                                </div>

                                                <div class="reporter_profile_data">
                                                    <div class="cons_issue_reporters_box_pic">
                                                                <a type="button" href="#!" title="Location View">
                                                                <div class="cons_location_name">
                                                                    <img src=" http://www.naturalhighsafaris.com/cdn/cache/made/cdn/uploads/country_images/India/North/Delhi/India-Gate--Delhi-Photos2_940_529_80_s_c1.jpg" class="location_image">
                                                                    <span class="location_text_adjust">${location.name}</span>
                                                                </div>
                                                                </a>
                                                                <!-- <c:if test="${!empty user.person.profilePhoto}">
                                                            <img src="${user.person.profilePhoto}?type=square&width=100&height=100" alt="profile-pic" class= "reporter-profile-pic" style="width: 100px;">
                                                        </c:if>
                                                        <c:if test="${empty user.person.profilePhoto}">
                                                            <img src="http://www.browserstack.com/images/dummy_avatar.png?type=square&width=80&height=80" alt="profile-pic" class="reporter-profile-pic">
                                                        </c:if> -->
                                                        <!-- <p class="center-align">
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
                                                        </p> -->
                                                    </div>
                                                        <div class="complaints_followers_counter">
                                                        <p class="col-sm-4 col-md-4 reporter_total_complaints">
                                                            <span class="grey_text">Complaints</span> <br /> 
                                                            <strong class="blue_color_text">200</strong>
                                                        </p>
                                                        
                                                        <p class="col-sm-4 col-md-4 total_cons_population">
                                                            <span class="grey_text">Population</span> <br /> 
                                                            <strong class="blue_color_text">9.879 million</strong>
                                                        </p>
                                                        
                                                        <p class="col-sm-4 col-md-4 total_cons_area">
                                                            <span class="grey_text">Area</span> <br /> 
                                                            <strong class="blue_color_text">1,484 km²</strong>
                                                        </p>

                                                        </div>
                                                </div>

                                            <!--     <div class="list-group">
                                                
                                                    <div class="filter_types">
                                                    <p>
                                                        <strong class="filter_citzn_serv">Filter by Citizen Services</strong>
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
                                                                <strong class="filter_sys_lvl">Filter by SubCategory</strong>
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
                                                                <strong class="filter_temporal">Filter by Time</strong>
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
                                                                <strong class="filter_spatial">Filter by Location</strong>
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

                                                </div> -->
                                            </div>
                                            <div class="constituency_mid_pane col-sm-6 col-md-6">
                                                <div class="listing-wrapper">
                                                    <div class="secondary-wrapper">
                                                        <div class="views_div">
                                                            <c:if test="${viewType eq 'list'}">
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="?type=list" title="List View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-list"></i>
                                                                    <span class="list_icon" class="icon_selected">List View</span>
                                                                </div></a>
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="?type=map" title="Map View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-map-marker"></i>
                                                                    <span class="list_icon">Map View</span>
                                                                </div></a>
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="#" title="Analytics View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-stats"></i>
                                                                    <span class="list_icon">Analytics View</span>
                                                                </div></a>
                                                            </c:if>
                                                            <c:if test="${viewType eq 'map'}">
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="?type=list" title="List View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-list"></i>
                                                                    <span class="list_icon">List View</span>
                                                                </div></a>
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="?type=map" title="Map View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-map-marker"></i>
                                                                    <span class="list_icon" class="icon_selected">Map View</span>
                                                                </div></a>
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="#" title="Analytics View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-stats"></i>
                                                                    <span class="list_icon">Analytics View</span>
                                                                </div></a>
                                                            </c:if>
                                                            <c:if test="${viewType eq 'analytics'}">
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="?type=list" title="List View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-list"></i>
                                                                    <span class="list_icon">List View</span>
                                                                </div></a>
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="?type=map" title="Map View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-map-marker"></i>
                                                                    <span class="list_icon">Map View</span>
                                                                </div></a>
                                                                <a class="col-sm-4 col-md-4 views_adjust" type="button" href="#" title="Analytics View"><div class="header_views_div_adjust">
                                                                    <i class="glyphicon glyphicon-stats"></i>
                                                                    <span class="list_icon" class="icon_selected">Analytics View</span>
                                                                </div></a>
                                                            </c:if>

                                                        </div>
                                                        <div class="clearfix"></div>
                                                    </div>
                                                    <c:if test="${viewType eq 'list'}">

                                                        <div class="listing">
                                                            <!-- new_div starts -->
                                                            <c:forEach items="${complaintList}" var="oneComplaint">
                                                                <div class="list-row" onclick="window.location='/complaint/${oneComplaint.id}.html'; return false;" style="cursor:pointer;">
                                                                        <div class="col-sm-1 profile-info profile_pic_adjust">
                                                                            <div class="profile-pic">
                                                                                <c:if test="${!empty oneComplaint.createdBy[0].profilePhoto}">
                                                                                    <a href="#!" class="anchorlink" ><img src="${oneComplaint.createdBy[0].profilePhoto}" alt=""></a>
                                                                                </c:if>
                                                                                <c:if test="${empty oneComplaint.createdBy[0].profilePhoto}">
                                                                                    <a href="#!" class="anchorlink" ><img src="${staticHost}/images/anonymous_profile_pic.png" alt="" style="width: 50px; max-width: 50px; border: 1px solid #ccc;" ></a>
                                                                                </c:if>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-sm-10 profile-info profile_info_adjust">
                                                                            <p class="col-sm-12 col-md-12 whom">
                                                                                <span class="col-sm-4 col-md-4 username text-limit name_adjust">
                                                                                    <c:forEach items="${oneComplaint.createdBy}" var="onePerson">
                                                                                        <a href="#!" class="anchorlink username_adjust" >${onePerson.name}</a>
                                                                                    </c:forEach>
                                                                                </span>

                                                                                <span class="col-sm-4 col-md-4 issue-scope-type text-limit type_adjust">
                                                                                    <c:forEach items="${oneComplaint.categories}" var="oneCategory">
                                                                                    <c:if test="${oneCategory.root}">
                                                                                        <img src = "${oneCategory.imageUrl}" class="issue_type_pic" alt="">
                                                                                    </c:if>
                                                                                    </c:forEach>
                                                                                    
                                                                                </span>
                                                                                
                                                                                <span class="col-sm-4 col-md-4 time_info_adjust">
                                                                                <i class="glyphicon glyphicon-time"></i>
                                                                                    <a href="#!" class="anchorlink" >
                                                                                    <span class="location">
                                                                                        <abbr class="timeago" title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr>
                                                                                    </span>
                                                                                    </a>
                                                                                </span>
                                                                                
                                                                            </p>

                                                                            <p class="whenwhere">

                                                                                <strong class="issue-id">#${oneComplaint.id}</strong>
                                                                            
                                                                            </p>

                                                                            <div class="issue-info" >

                                                                            <p class="category_title_adjust">
                                                                                <a href="${location.url}/category/${oneComplaint.subCategoryId}.html?type=${viewType}" class="anchorlink" ><span class="issue-scope">${oneComplaint.categoryTitle}</span></a>
                                                                            </p>

                                                                            <c:if test="${!empty oneComplaint.description}">
                                                                                <p class="desc elipsis description_adjust">
                                                                                    ${oneComplaint.description}
                                                                                </p>
                                                                            </c:if>

                                                                            <c:if test="${!empty oneComplaint.photos}">
                                                                                <div class="issue-pic">
                                                                                    <img src="${oneComplaint.photos[0].orgUrl}" alt="" align="middle">
                                                                                </div>
                                                                            </c:if>

                                                                            <p class="list_row_footer_adjust">

                                                                            <span class="col-sm-4 col-md-4 address_adjust">
                                                                                <i class="glyphicon glyphicon-map-marker"></i>
                                                                                <a href="#!" class="anchorlink" ><span class="location">${oneComplaint.locationAddress}</span></a>
                                                                            </span>

                                                                            <span class="col-sm-4 col-md-4 comments_adjust">
                                                                                <i class="glyphicon glyphicon-comment"></i>
                                                                                <a href="#!" class="anchorlink" ><span class="comments">Comments(${oneComplaint.totalComments})</span></a>
                                                                            </span>

                                                                            <span class="col-sm-4 col-md-4 status_adjust">
                                                                            <i class="glyphicon glyphicon-eye-open"></i>
                                                                                <a href="#!" class="anchorlink" ><span class="issue_status">Pending</span></a>
                                                                            </span>
                                                                            
                                                                            </p>
                                                                            
                                                                        </div>
                                                                    </div>
                                                                        <div class="col-sm-1 share_buttons_adjust">
                                                                        <p class="innerdiv-sharebtn">
                                                                        <!-- Social Media Share button js script for fb, to be moved to existing js file if needed -->
                                                                        <script>function fbShare(url, title, descr, image, winWidth, winHeight) {var winTop = (screen.height / 2) - (winHeight / 2);var winLeft = (screen.width / 2) - (winWidth / 2);window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);}</script>
                                                                        <!-- social media share buttons -->                                
                                                                        <a href="javascript:fbShare('http://www.eswaraj.com/', 'Fb Share', 'Facebook share popup', '', 520, 350)" class="anchorlink" ><img src="${staticHost}/images/fbicon.png" alt="" align="middle" class="icon_resize"></a>    
                                                                        <br />                                                                        
                                                                        <a href="https://plus.google.com/share?url=http://www.eswaraj.com/" class="anchorlink" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="https://www.gstatic.com/images/icons/gplus-32.png" alt="Share on Google+"  class="icon_resize"/></a>
                                                                        <br />
                                                                        <a href="https://twitter.com/share" class="anchorlink" onclick="javascript:window.open(this.href,'','menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=350,width=520,top=200,left=400 ');return false;"><img src="${staticHost}/images/twittericon.png" alt="Share on Twitter"  class="icon_resize"/></a>
                                                                    </p>
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
                                                        <div id="map-container">
                                                        <div id="panel">
                                                            <button onclick="createHeatmap()" class="col-sm-4 col-md-4 map_panel_buttons_adjust">
                                                                <i class="glyphicon glyphicon-fire"></i>
                                                                <span class="list_icon">HeatMap View</span>
                                                            </button>
                                                            <button onclick="createCluster()" class="col-sm-4 col-md-4 map_panel_buttons_adjust">
                                                                <i class="glyphicon glyphicon-sound-5-1"></i>
                                                                <span class="list_icon">Cluster View</span>
                                                            </button>
                                                            <button onclick="createMarker()" class="col-sm-4 col-md-4 map_panel_buttons_adjust">
                                                                <i class="glyphicon glyphicon-pushpin"></i>
                                                                <span class="list_icon">Marker View</span>
                                                            </button>
                                                        </div>
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
                                            <div class="constituency_right_pane col-sm-2 col-md-2">
                                                <div class="right_profile">
                                                    <div id="myCarousel" class="carousel slide" data-ride="carousel" data-interval="false">
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
<!--
                                                    <div class="platform_constituency_counter">
                                                    <p class="p_c_counter_loc red_orng_clr_text"><span>${location.name}</span></p>
                                                    <p class="total_const_count">
                                                        <span class="grey_text">Total Constituencies</span> <br /> 
                                                        <strong class="blue_color_text">70</strong>
                                                    </p>
                                                    <p class="total_houses">
                                                        <span class="grey_text">Total Houses</span> <br /> 
                                                        <strong class="blue_color_text">3435999</strong>
                                                    </p>
                                                    <p class="total_male_pop">
                                                        <span class="grey_text">Total Male Population</span> <br /> 
                                                        <strong class="blue_color_text">8987326</strong>
                                                    </p>
                                                    <p class="total_female_pop">
                                                        <span class="grey_text">Total Female Population</span> <br /> 
                                                        <strong class="blue_color_text">7800615</strong>
                                                    </p>
                                                    <p class="total_literate_pop">
                                                        <span class="grey_text">Total Literate Population</span> <br /> 
                                                        <strong class="blue_color_text">12737767</strong>
                                                    </p>
                                                    <p class="total_working_pop">
                                                        <span class="grey_text">Total Working Population</span> <br /> 
                                                        <strong class="blue_color_text">5587049</strong>
                                                    </p>
                                                    </div>
-->
                                                    <div class="social_media_sharing_buttons">

                                                    <p>Get the App:</p>
                                                    <a href="https://play.google.com/store/apps/details?id=com.next.eswaraj&amp;hl=en" target="blank">
                                                        <div class="gplay">
                                                        </div>
                                                    </a>
                                                    
                                                    <p class=""><span>Follow Us: </span></p>
                                                        <div id="fb-root"></div>
                                                        <script>(function(d, s, id) {
                                                          var js, fjs = d.getElementsByTagName(s)[0];
                                                          if (d.getElementById(id)) return;
                                                          js = d.createElement(s); js.id = id;
                                                          js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.0";
                                                          fjs.parentNode.insertBefore(js, fjs);
                                                        }(document, 'script', 'facebook-jssdk'));</script>                                                        
                                                        <div class="fb-like" data-href="https://www.facebook.com/eSwarajApp?ref=hl" data-width="5" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>
                                                                                                                
                                                        <a href="https://twitter.com/share" class="twitter-share-button" data-url="https://twitter.com/eSwarajApp" data-text="Follow eSwaraj :" data-via="eSwarajApp" data-size="large">Tweet</a>
                                                        <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>
                                                    
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