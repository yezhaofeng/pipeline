<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>就差一步,就注册完了!</title>
    <link href="/resources/base/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="/resources/base/jquery.js"></script>
    <script src="/resources/base/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="jumbotron">
            </div>
            <img alt="100x100" src="${avatarUrl}" class="img-circle"/>
            <p class="help-block">
                <a href="${githubHome}" target="_blank">${username}</a>，欢迎您
            </p>
            <form role="form" action="/github/initUser" method="post">
                <input type="hidden" name="username" value="${username}"/>
                <input type="hidden" name="registerToken" value="${registerToken}"/><br/>
                <input type="hidden" name="avatarUrl" value="${avatarUrl}"/><br/>
                <div class="form-group">
                    <label for="gitHubTokenid">GitHub-token</label>
                    <input type="text" name="gitHubToken" class="form-control" id="gitHubTokenid"/>
                </div>
                <div class="form-group">
                    <label for="emailid">邮箱</label>
                    <input type="email" name="email" class="form-control" id="emailid"/>
                </div>
                需要同步的代码库：<br/>
                <c:forEach var="repo" items="${repoNames}" varStatus="status">
                    <input type="checkbox" name="syncRepos" value="${repo}"/>${repo} &nbsp;&nbsp;&nbsp;
                    <c:if test="${status.index % 4 == 3}">
                        <br/>
                    </c:if>
                </c:forEach>
                <br/>
                <input type="submit" class="btn btn-default" value="注册"/>
            </form>
        </div>
    </div>
</div>


<%--<img src="${avatarUrl}"/><br/>--%>
<%--<a href="${githubHome}" target="_blank">GitHub主页</a><br/>--%>
<%--<form action="/github/initUser" method="post">--%>
<%--<input type="hidden" name="registerToken" value="${registerToken}"/><br/>--%>
<%--<input type="hidden" name="avatarUrl" value="${avatarUrl}"/><br/>--%>
<%--用户名:<input type="text" name="username" value="${username}" readonly="readonly"/><br/>--%>
<%--GitHub-token:<input type="text" name="gitHubToken"/><br/>--%>
<%--邮箱:<input type="email" name="email"/><br/>--%>
<%--需要同步的代码库：<br/>--%>
<%--<c:forEach var="repo" items="${repoNames}" varStatus="status">--%>
<%--<input type="checkbox" name="syncRepos" value="${repo}"/>${repo}&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--<c:if test="${status.index % 4 == 3}">--%>
<%--<br/>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--<br/>--%>
<%--<input type="submit" value="注册"/>--%>
</form>
</body>
</html>
