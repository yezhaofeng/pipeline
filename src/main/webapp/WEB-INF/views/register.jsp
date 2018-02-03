<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pipeline注册</title>
</head>
<body>
<p>
    就差一步了!
</p>
<img src="${avatarUrl}"/><br/>
<a href="${githubHome}">GitHub主页</a><br/>
<form action="/github/initUser" method="post">
    <input type="hidden" name="registerToken" value="${registerToken}"/><br/>
    <input type="hidden" name="avatarUrl" value="${avatarUrl}"/><br/>
    用户名:<input type="text" name="username" value="${username}" readonly="readonly"/><br/>
    GitHub-token:<input type="text" name="gitHubToken"/><br/>
    邮箱:<input type="email" name="email"/><br/>
    需要同步的代码库：<br/>
    <c:forEach var="repo" items="${repoNames}" varStatus="status">
        <input type="checkbox" name="syncRepos" value="${repo}"/>${repo}&nbsp;&nbsp;&nbsp;&nbsp;
        <c:if test="${status.index % 4 == 3}">
            <br/>
        </c:if>
    </c:forEach>
    <br/>
    <input type="submit" value="注册"/>
</form>
</body>
</html>
