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
    </head>
    <body ng-controller="pbadminStaffController">
        <div class="outerwrapper">
            <jsp:include page="header.jsp" />
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9">
            <div class="listing-wrapper">
                <div class="secondary-wrapper">
                    <div class="pull-left">
			    Select Political Position
			    <ul ng-repeat="position in positions">
				    <li>
				    <div>
					    <p>{{position.politicalBodyType}}</p>
					    <p>{{position.locationName}}</p>
					    <button ng-click="onPositionSelected($index)">Select</button>
				    </div>
				    </li>
			    </ul>
		    </div>
		    <div>
			    <h1>Existing staff</h1>
			    <ul ng-repeat="staff in staffs">
				    <li index={{$index}}>
				    <div>
					    <img src="{{staff.personDto.profilePhoto}}">
					    <p>{{staff.personDto.name}}</p>
					    <p>{{staff.post}}</p>
					    <button ng-click="deleteStaff($index)">Delete</button>
				    </div>
				    </li>
			    </ul>
		    </div>
		    <div>
			    <h1>Add new Staff</h1>
			    <typeahead items="items" prompt="Start typing a name" title="name" subtitle="biodata" img="profilePhoto" min="3" url="/api/person/search/name/" querystring="term" model="selected.name" on-select="onPersonSelected()" />
			    <input type="text" ng-model="new.post" placeholder="Enter post name"></input>
			    <button ng-click="addStaff()">Add Staff Member</button>
		    </div>
		    <!-- All Vaibhav code will go here -->
		    <!--  and finish here -->

	    </div>

    </div>
	</div>
</div>
</div>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
