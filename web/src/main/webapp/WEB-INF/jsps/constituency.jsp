<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<jsp:include page="header.jsp" />
<script>
jQuery(document).ready(function() {
  jQuery("abbr.timeago").timeago();
});
</script>
<c:if test="${viewType eq 'map'}">
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor="Electricity"&amp;language=en"></script>
    <script src="${staticHost}/js/gmap3.min.js"></script>
<script>
var complaints = [
    <% int i=0; %>  
  <c:forEach items="${complaintList}" var="oneComplaint">
    <% if (i>0) out.println(","); i++; %> {lat:${oneComplaint.lattitude},lng:${oneComplaint.longitude},data:{id:${oneComplaint.id},category:"${oneComplaint.categoryTitle}",address:"TREMBLAY-EN-FRANCE",date: "${oneComplaint.complaintTimeIso}", userId: ${oneComplaint.loggedBy.id}, userName: "${oneComplaint.loggedBy.name}", userImgUrl : "http://www.panoramio.com/user/4483", complaintImgUrl: "http://www.panoramio.com/user/4483"}}
  </c:forEach>
</script>

<script src="${staticHost}/js/loadloc.js"></script>
</c:if>
<div class="container-fluid">
    <div class="banner">
    <div class="locate-on-map">
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <div style="overflow:hidden;height:100%;width:100%;">
            <div id="gmap_canvas" style="height:100%;width:100%;"></div>
        </div>
        <script type="text/javascript"> function init_map(){var myOptions = {zoom:14,center:new google.maps.LatLng(19.0344647,73.01100959999997),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById("gmap_canvas"), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(19.0344647, 73.01100959999997)});infowindow = new google.maps.InfoWindow({content:"<b></b><br/><br/>400706 " });google.maps.event.addListener(marker, "click", function(){infowindow.open(map,marker);});}google.maps.event.addDomListener(window, 'load', init_map);</script>
    </div>
    <div class="row">
        <div class="col-sm-9"></div>
        <div class="col-sm-3">
            <div class="mla-profile">
                <img src="${staticHost}/images/issues/issues.png"  alt="mla image">
                <p> <strong>Rahuram Desai, MLA</strong>
                    <span>In Office since Jan, 2014</span>
                </p>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-3">
        <p> <strong>Refine Search</strong>
        </p>
        <div class="refine-search">
            <input type="text" value="Amsterdam,Washington" data-role="tagsinput">
            <div class="filter-options">
                <span class="trigger btn btn-default"> <i class="glyphicon glyphicon-cog"></i>
                </span>
                <div class="dropdown">
                    <form action="">
                        <div class="form-group">
                            <label>Your Locality</label>
                            <select class="form-control">
                                <option value="1"></option>
                                <option value="2">Locality</option>
                                <option value="3">Locality</option>
                                <option value="4">Locality</option>
                                <option value="5">Locality</option>
                                <option value="6">Locality</option>
                                <option value="7">Locality</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Citizen Services</label>
                            <select class="form-control">
                                <c:forEach items="${rootCategories}" var="oneCategory">
                                    <option value="${oneCategory.id}">${oneCategory.name} (${oneCategory.locationCount})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Issue Type</label>
                            <select class="form-control">
                                <option value="1">Issue</option>
                                <option value="2">Issue</option>
                                <option value="3">Issue</option>
                                <option value="4">Issue</option>
                                <option value="5">Issue</option>
                                <option value="6">Issue</option>
                                <option value="7">Issue</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Duration</label>
                            <select class="form-control">
                                <option value="1"></option>
                                <option value="2">Today</option>
                                <option value="3">Yesterday</option>
                                <option value="4">Last 72 Hrs</option>
                                <option value="5">Last 1 Week</option>
                                <option value="6">Last 2 Week</option>
                                <option value="7">Last 1 Month</option>
                                <option value="8">Last 3 Month</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <div class="btn btn-default btn-xs">Clear</div>
                            <div class="btn btn-primary btn-xs">Search</div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <p>
            <strong>Sort Issues by category</strong>
        </p>
        <div class="list-group">
            <c:if test="${empty selectedCategory}">
                <a href="#" class="list-group-item active">Show All (${total})</a>
            </c:if>
            <c:if test="${!empty selectedCategory}">
                <a href="${location.url}.html" class="list-group-item">Show All (${total})</a>
            </c:if>
                                    
            
            <c:forEach items="${rootCategories}" var="oneCategory">
                <c:if test="${selectedCategory eq oneCategory.id}">
                    <a href="#" class="list-group-item active">${oneCategory.name} (${oneCategory.locationCount})</a>
                </c:if>
                <c:if test="${ selectedCategory ne oneCategory.id}">
                    <a href="${location.url}/category/${oneCategory.id}.html" class="list-group-item">${oneCategory.name} (${oneCategory.locationCount})</a>
                </c:if>
                
            </c:forEach>
        </div>
    </div>
    <div class="col-sm-9">
        <div class="listing-wrapper">
            <div class="secondary-wrapper">
                <div class="pull-left">
                    <h1>${location.name}</h1>
                </div>
                <div class="pull-right">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default active" title="List View"> <i class="glyphicon glyphicon-list"></i>
                        </button>
                        <button type="button" class="btn btn-default" title="Map View">
                            <i class="glyphicon glyphicon-map-marker"></i>
                        </button>
                        <button type="button" class="btn btn-default" title="Analytical View">
                            <i class="glyphicon glyphicon-signal"></i>
                        </button>
                    </div>
                    <button class="btn btn-primary">Raise Issue</button>
                </div>
                <div class="clearfix"></div>
            </div>
            <c:if test="${viewType eq 'list'}">
            
            <div class="listing">
                <!-- .list-row  -->
                <c:forEach items="${complaintList}" var="oneComplaint">
                    <div class="list-row">
                    <div class="issue-pic">
                        <c:if test="${!empty oneComplaint.photos}">
                            <img src="${oneComplaint.photos[0].orgUrl}" alt="">
                        </c:if>
                        
                        <c:if test="${empty oneComplaint.photos}">
                            <img src="${staticHost}/images/issues/issues.png" alt="">
                        </c:if>
                    </div>
                    <div class="innerblock">
                        <div class="profile-info">
                            <div class="profile-pic">
                                <img src="${staticHost}/images/profile-pic.jpg" alt=""></div>
                            <p class="whom">
                                <strong class="issue-id">Issue #${oneComplaint.id}</strong>
                                <span class="connector">raised by</span>
                                <a href="#" class="username">${oneComplaint.loggedBy.name}</a>
                            </p>
                            <p class="whenwhere">
                                <span><abbr class="timeago" title="${oneComplaint.complaintTimeIso}">${oneComplaint.complaintTimeIso}</abbr></span>
                                <span class="connector">at</span>
                                <span>
                                    <i class="glyphicon glyphicon-map-marker"></i>
                                    <a href="#" class="location">TODO</a>
                                </span>
                            </p>
                        </div>
                        <div class="issue-info">
                            <p class="desc elipsis">
                                <c:if test="${empty oneComplaint.description}">
                                    ${oneComplaint.categoryTitle}
                                </c:if>
                                <c:if test="${!empty oneComplaint.description}">
                                    ${oneComplaint.description}
                                </c:if>
                            </p>
                            <p class="classify">
                                <c:forEach items="${oneComplaint.categories}" var="oneCategory">
                                    <c:if test="${oneCategory.root}">
                                        <small class="badge badge-infra">Type - ${oneCategory.name}</small>
                                    </c:if>
                                    
                                </c:forEach>
                                
                            </p>
                        </div>
                    </div>
                </div><!-- /.list-row  -->
                </c:forEach>
                
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
                        <li class="active">
                            <a href="?page=${onePage}">${onePage}</a>
                        </li>
                        </c:if>
                        <c:if test="${onePage ne currentPage}">
                        <li>
                            <a href="?page=${onePage}">${onePage}</a>
                        </li>
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
                <div id="map-container"><div id="my_map"></div></div>
            </c:if>
        </div>
    </div>
</div>
</div>
<jsp:include page="footer.jsp" />
