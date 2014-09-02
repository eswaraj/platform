<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<jsp:include page="header.jsp" />
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
                                <option value="1">Show All</option>
                                <option value="2">Road</option>
                                <option value="3">Water</option>
                                <option value="4">Transportation</option>
                                <option value="5">Electricity</option>
                                <option value="6">Law &amp; order</option>
                                <option value="7">Sewage</option>
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
            <a href="#" class="list-group-item active">Show All</a>
            <a href="#" class="list-group-item">Water</a>
            <a href="#" class="list-group-item">Electricity</a>
            <a href="#" class="list-group-item">Sewage &amp; Sanitation</a>
            <a href="#" class="list-group-item">Law &amp; Order</a>
            <a href="#" class="list-group-item">Roads</a>
            <a href="#" class="list-group-item">Transportation</a>
        </div>
    </div>
    <div class="col-sm-9">
        <div class="listing-wrapper">
            <div class="secondary-wrapper">
                <div class="pull-left">
                    <h1>CBD Belapur Constituency</h1>
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
            <div class="listing">
                <!-- .list-row  -->
                <div class="list-row">
                    <div class="issue-pic">
                        <img src="${staticHost}/images/issues/issues.png" alt=""></div>
                    <div class="innerblock">
                        <div class="profile-info">
                            <div class="profile-pic">
                                <img src="${staticHost}/images/profile-pic.jpg" alt=""></div>
                            <p class="whom">
                                <strong class="issue-id">Issue #51</strong>
                                <span class="connector">raised by</span>
                                <a href="#" class="username">Somnath Nabajja</a>
                            </p>
                            <p class="whenwhere">
                                <span>2 hrs ago</span>
                                <span class="connector">at</span>
                                <span>
                                    <i class="glyphicon glyphicon-map-marker"></i>
                                    <a href="#" class="location">Cassena Business Park main road,Keverappa Layout</a>
                                </span>
                            </p>
                        </div>
                        <div class="issue-info">
                            <p class="desc elipsis">
                                Lorem ipsum Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sapiente enim facilis explicabo perferendis possimus veniam assumenda sit dolore cumque aliquam. dolor sit amet, consectetur adipisicing elit. Laboriosam, sint
                            </p>
                            <p class="classify">
                                <small class="badge badge-infra">Type - Lack of Transparency</small>
                            </p>
                        </div>
                    </div>
                </div>
                <!-- /.list-row  -->
                <!-- .list-row  -->
                <div class="list-row">
                    <div class="issue-pic">
                        <img src="${staticHost}/images/issues/issues.png" alt=""></div>
                    <div class="innerblock">
                        <div class="profile-info">
                            <div class="profile-pic">
                                <img src="${staticHost}/images/profile-pic.jpg" alt=""></div>
                            <p class="whom">
                                <strong class="issue-id">Issue #51</strong>
                                <span class="connector">raised by</span>
                                <a href="#" class="username">Somnath Nabajja</a>
                            </p>
                            <p class="whenwhere">
                                <span>2 hrs ago</span>
                                <span class="connector">at</span>
                                <span>
                                    <i class="glyphicon glyphicon-map-marker"></i>
                                    <a href="#" class="location">Cassena Business Park main road,Keverappa Layout</a>
                                </span>
                            </p>
                        </div>
                        <div class="issue-info">
                            <p class="desc elipsis">
                                Lorem ipsum Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nesciunt nam aspernatur obcaecati cupiditate dolorum odio cumque debitis repellendus recusandae. Accusantium. dolor sit amet, consectetur adipisicing elit. Laboriosam, sint
                            </p>
                            <p class="classify">
                                <small class="badge badge-infra">Type - Lack of Transparency</small>
                            </p>
                        </div>
                    </div>
                </div>
                <!-- /.list-row  -->
                <!-- .list-row  -->
                <div class="list-row">
                    <div class="issue-pic">
                        <img src="${staticHost}/images/issues/issues.png" alt=""></div>
                    <div class="innerblock">
                        <div class="profile-info">
                            <div class="profile-pic">
                                <img src="${staticHost}/images/profile-pic.jpg" alt=""></div>
                            <p class="whom">
                                <strong class="issue-id">Issue #51</strong>
                                <span class="connector">raised by</span>
                                <a href="#" class="username">Somnath Nabajja</a>
                            </p>
                            <p class="whenwhere">
                                <span>2 hrs ago</span>
                                <span class="connector">at</span>
                                <span>
                                    <i class="glyphicon glyphicon-map-marker"></i>
                                    <a href="#" class="location">Cassena Business Park main road,Keverappa Layout</a>
                                </span>
                            </p>
                        </div>
                        <div class="issue-info">
                            <p class="desc elipsis">
                                Lorem ipsum Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dicta voluptas molestias at minus, magni est accusamus dolore aliquid nisi totam. dolor sit amet, consectetur adipisicing elit. Laboriosam, sint
                            </p>
                            <p class="classify">
                                <small class="badge badge-infra">Type - Lack of Transparency</small>
                            </p>
                        </div>
                    </div>
                </div>
                <!-- /.list-row  -->
                <!-- .list-row  -->
                <div class="list-row">
                    <div class="issue-pic">
                        <img src="${staticHost}/images/issues/issues.png" alt=""></div>
                    <div class="innerblock">
                        <div class="profile-info">
                            <div class="profile-pic">
                                <img src="${staticHost}/images/profile-pic.jpg" alt=""></div>
                            <p class="whom">
                                <strong class="issue-id">Issue #51</strong>
                                <span class="connector">raised by</span>
                                <a href="#" class="username">Somnath Nabajja</a>
                            </p>
                            <p class="whenwhere">
                                <span>2 hrs ago</span>
                                <span class="connector">at</span>
                                <span>
                                    <i class="glyphicon glyphicon-map-marker"></i>
                                    <a href="#" class="location">Cassena Business Park main road,Keverappa Layout</a>
                                </span>
                            </p>
                        </div>
                        <div class="issue-info">
                            <p class="desc elipsis">
                                Lorem ipsum Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum dignissimos id cum quaerat soluta praesentium perferendis, possimus dolore libero eaque. dolor sit amet, consectetur adipisicing elit. Laboriosam, sint
                            </p>
                            <p class="classify">
                                <small class="badge badge-infra">Type - Lack of Transparency</small>
                            </p>
                        </div>
                    </div>
                </div>
                <!-- /.list-row  -->
                <!-- .list-row  -->
                <div class="list-row">
                    <div class="issue-pic">
                        <img src="${staticHost}/images/issues/issues.png" alt=""></div>
                    <div class="innerblock">
                        <div class="profile-info">
                            <div class="profile-pic">
                                <img src="${staticHost}/images/profile-pic.jpg" alt=""></div>
                            <p class="whom">
                                <strong class="issue-id">Issue #51</strong>
                                <span class="connector">raised by</span>
                                <a href="#" class="username">Somnath Nabajja</a>
                            </p>
                            <p class="whenwhere">
                                <span>2 hrs ago</span>
                                <span class="connector">at</span>
                                <span>
                                    <i class="glyphicon glyphicon-map-marker"></i>
                                    <a href="#" class="location">Cassena Business Park main road,Keverappa Layout</a>
                                </span>
                            </p>
                        </div>
                        <div class="issue-info">
                            <p class="desc elipsis">
                                Lorem ipsum Lorem ipsum dolor sit amet, consectetur adipisicing elit. Distinctio nostrum excepturi voluptas, laborum. Porro omnis esse voluptatem molestiae, ad voluptates! dolor sit amet, consectetur adipisicing elit. Laboriosam, sint
                            </p>
                            <p class="classify">
                                <small class="badge badge-infra">Type - Lack of Transparency</small>
                            </p>
                        </div>
                    </div>
                </div>
                <!-- /.list-row  -->
                <div class="pagination-wrapper">
                    <ul class="pagination">
                        <li class="disabled">
                            <a href="#">&laquo;</a>
                        </li>
                        <li class="active">
                            <a href="#">1</a>
                        </li>
                        <li>
                            <a href="#">2</a>
                        </li>
                        <li>
                            <a href="#">3</a>
                        </li>
                        <li>
                            <a href="#">4</a>
                        </li>
                        <li>
                            <a href="#">5</a>
                        </li>
                        <li>
                            <a href="#">&raquo;</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<jsp:include page="footer.jsp" />
