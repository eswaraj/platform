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
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-9">
            <div class="listing-wrapper">
                <div class="secondary-wrapper">
                    <c:if test="${empty positions}">
                    You do not have political position
                    </c:if>
                    <c:if test="${!empty positions}">
                    <div class="pull-left">
                        <h1>Edit Staff</h1>
                        <table>
                        
                        <c:forEach items="${positions}" var="onePosition">
                        <tr>
                        <td>${onePosition.politicalBodyType}</td>
                        <td>${onePosition.politicalBodyTypeShort}</td>
                        <td>${onePosition.locationName}</td>
                        <td>${onePosition.id}</td>
                        </tr>
                        </c:forEach>
                        </table>
                    </div>
                    <!-- All Vaibhav code will go here -->
                    <!--  and finish here -->
                    </c:if>
                    
                </div>
                
            </div>
        </div>
    </div>
</div>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
