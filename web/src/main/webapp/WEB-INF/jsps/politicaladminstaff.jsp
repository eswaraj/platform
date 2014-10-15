<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
            <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

                        <!DOCTYPE html>
                        <html lang="en" ng-app="pbadminStaff">
                            <head>
                                <title>eSwaraj</title>
                                <jsp:include page="include.jsp" />
                                <script src="${staticHost}/js/angular.min.js"></script>
                                <script src="${staticHost}/js/typeahead.js"></script>
                                <script src="${staticHost}/js/pbadmin_staff.js"></script>
                                <link rel="stylesheet" href="${staticHost}/css/typeahead.css">
                                <link rel="stylesheet" href='${staticHost}/css/mla_staff.css' />
                            </head>
                            <body ng-controller="pbadminStaffController">
                                <div class="wrapper" ng-show="!addMode">
                                    <div class="container">
                                        <div class="row">
                                            <div class="col-md-12 profile-margin">
                                                <div class="col-md-3"> 
                                                    <div id="node_div">
                                                        <h3 class="text-footer">Select Position</h3>
                                                        <select class="select dropdownlist" ng-options="position as label(position.politicalBodyType, position.locationName) for position in positions" 
                                                                ng-model="selectedPosition" ng-change="onPositionSelected()">
                                                        </select>
                                                    </div>

                                                </div>
                                                <div class="col-md-9 shift_right">
                                                    <div id="staff_admin_page"> 
                                                        <h3 class="text-footer-extended">Staff Member's List</h3>
                                                        <br />			
                                                        <div id="staff_admin_type_div">
                                                            <table id="admin_type_data" class="table_topdown_border">
                                                                <thead>
                                                                    <tr>
                                                                        <th>S.No</th>
                                                                        <th>Photo</th>
                                                                        <th>Name</th>
                                                                        <th>Post</th>
                                                                        <th>Remove</th>
                                                                    </tr>
                                                                </thead>

                                                                <tbody ng-repeat="staff in staffs track by $index">
                                                                    <tr>
                                                                        <td>{{$index}}</td>
                                                                        <td><img src="{{staff.personDto.profilePhoto}}" class="thumb"></td>
                                                                        <td>{{staff.personDto.name}}</td>
                                                                        <td>{{staff.post}}</td>
                                                                        <td><button class="btn btn-primary blue btn_round_half add-admin-type" type="button" ng-click="deleteStaff($index)">Remove</button></td>
                                                                    </tr>
                                                                </tbody>
                                                            </table>



                                                            <br>
                                                            <button class="btn btn-primary blue btn_round add-admin-type" type="button" ng-click="showForm()">Add New Staff</button>

                                                        </div>					
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div><!-- Container -->
                                </div>
                                <div id="add_edit_admin_page" ng-show="addMode">
                                    <h4 class="text-footer-extended shift-bottom">Add New Staff Member</h4>

                                    <div id="edit_person" >
                                        <div id="search_by_name" style="float: left;">
                                            Search by Name
                                            <typeahead text="text" items="items" prompt="Start typing a name" title="name" subtitle="email" img="profilePhoto" min="3" url="/api/person/search/name/" querystring="term" model="selected" on-select="onPersonSelected()"></typeahead>
                                        </div>

                                        <div id="search_by_email" style="margin-left: 380px;">
                                            Search by Email
                                            <typeahead text="text" items="items" prompt="Start typing a name" title="name" subtitle="email" img="profilePhoto" min="3" url="/api/person/search/email/" querystring="term" model="selected" on-select="onPersonSelected()"></typeahead>
                                        </div>

                                        <br />
                                        <br />

                                        <div id="person_details">Personal Details<span class="glyphicon glyphicon-chevron-up right_float"></span></div>

                                        <div id="person_details_innerdiv">

                                            <div class="col-sm-3">

                                                <img src="images/profile-pic.jpg" class="member_pic">

                                            </div>

                                            <div class="col-sm-9">

                                                <table>

                                                    <tr>
                                                        <td><label for="pbadmin_name">Name</label></td>
                                                        <td><input type="text" id="pbadmin_name" placeholder="xyz" disabled ng-model="selected.name"/></td>
                                                        <td><label for="pbadmin_gender">Gender</label></td>
                                                        <td><input type="text" id="pbadmin_gender" placeholder="Gender" disabled ng-model="selected.gender"/></td>
                                                    </tr>

                                                    <tr>
                                                        <td><label for="pbadmin_email">E-Mail</label></td>
                                                        <td><input type="text" id="pbadmin_email" placeholder="abc@xyz.com" disabled ng-model="selected.email"/></td>
                                                        <td><label for="pbadmin_post">Post</label></td>
                                                        <td><input type="text" id="pbadmin_post" placeholder="" ng-model="new.post"/></td>
                                                    </tr>

                                                </table>

                                            </div>

                                        </div>

                                        <br />

                                    </div>
                                    <button class="btn btn-primary blue btn_round edit_page_close" type="button" ng-click="closeForm()">Close</button>
                                    <button class="btn btn-primary blue btn_round edit_page_save_changes" type="button" ng-click="addStaff()">Add to My Staff</button>

                                </div>


                                <jsp:include page="footer.jsp" />
                            </body>
                        </html>
