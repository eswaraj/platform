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
Index.jsp
<br>
</br>
<h1>Total Complaints Since Start = ${totalComplaints}</h1>
<br></br>
<h2>Complaints By Year<h2>
<table>
<tr><th>Year key</th><th>Number of Complaints</th></tr>
<c:forEach items="${yearComplaints}" var="entry">
<tr>
    <td>${entry.key}</td><td> ${entry.value}</td>
    </tr>
</c:forEach>
</table>
${locationDto.id}
</body>
</html>