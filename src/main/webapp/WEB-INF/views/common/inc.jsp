<%@ page import="com.jlu.user.model.GithubUser" %>
<%
    String path = request.getContextPath();
    if (!path.endsWith("/")) {
        path += "/";
    }
    String basePath = path + "resources/";
    GithubUser user = (GithubUser) request.getSession().getAttribute("currentUser");
%>
