<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin</title>
</head>
<body>
    <a href=<c:url value="/j_spring_security_logout"/>>Logout</a><br/>
    <h1>Only Admin allowed here</h1>
    
    <table style="border: 1px solid; width: 500px; text-align:center">
	<thead style="background:#fcf">
		<tr>
			<th>First Name</th>
			<th>Last Name</th>
			<th colspan="3"></th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${users}" var="user">
			<c:url var="editUrl" value="/user/users/edit?id=${user.id}" />
			<c:url var="deleteUrl" value="/user/users/delete?id=${user.id}" />
		<tr>
			<td><c:out value="${user.userName}" /></td>
			<td><c:out value="${user.password}" /></td>
			<td><a href="${editUrl}">Edit</a></td>
			<td><a href="${deleteUrl}">Delete</a></td>
			<td><a href="${addUrl}">Add</a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<c:if test="${empty users}">
	There are currently no users in the list. <a href="${addUrl}">Add</a> a user.
</c:if>
    
</body>
</html>