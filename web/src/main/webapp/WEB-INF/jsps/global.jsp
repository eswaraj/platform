<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
<h1>${title}</h1>
<br>
</br>
<h1>Total Complaints Since Start = ${totalComplaints}</h1>
<br></br>
<h2>Complaints By Year<h2>
<table border="1">
<tr><th>Year key</th><th>Number of Complaints</th></tr>
<c:forEach items="${yearComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>



<br></br>
<h2>Complaints By Month(Current Year Only)<h2>
<table border="1">
<tr><th>Month key</th><th>Number of Complaints</th></tr>
<c:forEach items="${monthComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>


<br></br>
<h2>Complaints By Day(Current Month Only)<h2>
<table border="1">
<tr><th>Day key</th><th>Number of Complaints</th></tr>
<c:forEach items="${dayComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>

<br></br>
<h2>Complaints By Hour(Today Only)<h2>
<table border="1">
<tr><th>Hour key</th><th>Number of Complaints</th></tr>
<c:forEach items="${dayHourComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>


<br></br>
<h2>Complaints By Hour(Last 24 Hours Only)<h2>
<table border="1">
<tr><th>Hour key</th><th>Number of Complaints</th></tr>
<c:forEach items="${last24HourComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>

<br></br>
<h2>Complaints By Category in this location<h2>
<table border="1">
<tr><th>Category</th><th>Number of Complaints</th></tr>
<c:forEach items="${totalCategoryComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>

<br></br>
<h2>Choose Locations<h2>
<table border="1">
<tr><th>Location Name</th></tr>
<c:forEach items="${locations}" var="oneLocation">
<tr>
    <td><a href="/stat/location/${oneLocation.id}.html" >${oneLocation.name}</a></td>
    </tr>
</c:forEach>
</table>

</body>
</html>