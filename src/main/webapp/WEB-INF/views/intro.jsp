<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/intro.css">
    <link rel="stylesheet" type="text/css" href="/resources/base/jquery.fullPage.css">
    <script type="text/javascript" src="/resources/base/jquery.js"></script>
    <script type="text/javascript" src="/resources/base/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="/resources/base/jquery-ui-timepicker-addon.js"></script>
    <script type="text/javascript" src="/resources/base/jquery.fullPage.min.js"></script>
    <script type="text/javascript" src="/resources/js/intro.js"></script>
</head>
<body>
<div id="search-wrapper" class="search-wrapper-top">
    <input class="search" id="module-search" type="text"
           onkeydown='if(event.keyCode==13){Agileplus.introPage.moduleSearch()}' placeholder="输入模块名立即体验"/>

    <img src="/resources/images/intro_search_icon.png" id="search-btn" onclick="Agileplus.introPage.moduleSearch()">
</div>
<div id="fullpage">
    <div class="section" id="section0"
         style="background-image: url('/resources/images/page1.bmp'); background-repeat: no-repeat; background-size: 100%;">
    </div>
    <div class="section" id="section1"
         style="background-image: url('/resources/images/page2.bmp'); background-repeat: no-repeat; background-size: 100%;">
    </div>
    <div class="section" id="section2"
         style="background-image: url('/resources/images/page3.bmp'); background-repeat: no-repeat; background-size: 100%;">
    </div>
    <div class="section" id="section3"
         style="background-image: url('/resources/images/page4.bmp'); background-repeat: no-repeat; background-size: 100%;">
    </div>
    <div class="section" id="section4"
         style="background-image: url('/resources/images/page5.png'); background-repeat: no-repeat; background-size: 100%;">
    </div>
</div>
</body>
</html>