<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <a href=<c:url value="/j_spring_security_logout"/>>Logout</a><br/>

    <sec:authorize ifAnyGranted="ROLE_ADMIN">
        <h1>Only admin can see this</h1><br/>
        <a href="admin"> Admin Home </a>
    </sec:authorize>

    <h1>Welcome</h1>



</body>
</html>