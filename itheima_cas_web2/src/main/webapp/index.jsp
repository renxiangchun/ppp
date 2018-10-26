<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>web1</title>
</head>
<body>
欢迎来到CAS_WEB2工程:<%=request.getRemoteUser()%>
<br/>
<a href="http://localhost:9100/cas/logout">退出登录</a>
</body>
</html>