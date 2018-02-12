<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <nav class="navbar navbar-default navbar-default-plus">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#" style="color: white !important;">Pipeline</a>
            </div>

            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <form class="navbar-form navbar-left">
                    <div class="form-group">
                        <input class="form-control" style="width: 350px;" type="text"
                               ng-model="ctrl.searchModule"
                               placeholder="请输入模块名"
                               uib-typeahead="module.module for module in ctrl.getSearchModules($viewValue, '${currentUser.username}')"
                               typeahead-loading="loadingLocations"
                               typeahead-no-results="noResults"
                               typeahead-on-select="ctrl.moduleSelected($model)"
                               autocomplete="off"
                               role="search"
                               uib-dropdown-toggle>
                    </div>
                </form>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="http://blog.csdn.net/z521598/article/details/79307482" target="_blank" style="color: white !important;">用户手册</a></li>
                    <li><a href="mailto:576506402@qq.com" style="color: white !important;">联系我们</a></li>
                    <li><a href="#" style="color: white !important;">${currentUser.username}</a></li>
                    <li class="btn-group" uib-dropdown="">
                        <a href="#" role="button" class="dropdown-toggle" uib-dropdown-toggle=""
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"
                           style="color: white !important;">
                            <img src="${currentUser.avatarUrl}" style="width:25px;height: 25px"/>
                            <span class="caret"></span>
                        </a>
                        <ul class="uib-dropdown-menu" role="menu">
                            <li>
                                <a href="https://github.com/${currentUser.username}" target="_blank"
                                   style="color: #4a4a4a !important;">github主頁</a></li>
                            <li>
                                <a href="" onclick="javascript:void(window.alert('${currentUser.pipelineToken}'))">查看token</a>
                            </li>
                            <li><a href="/github/exit">退出登录</a></li>
                        </ul>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
</header>
