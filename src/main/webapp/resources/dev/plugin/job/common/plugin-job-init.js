/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 发布项目视图
 */
define(['../common/app-injector', 'constants', 'angular'], function appPluginJobInit(injector, constants, angular) {
    return injector.invoke(
        [
            '$http',
            function ($http) {
                // 插件的页面扩展点
                // configPage: 插件JOB的配置页面
                // buildBreifPage: 插件JOB构建的简要信息展示
                // buildPage: 插件JOB构建的构建详情信息展示
                // executeRequirePage 执行时所需参数或者其他信息页面
                var contains = function (array, str) {
                    angular.forEach(array, function (item) {
                        if (str === item) {
                            return true;
                        }
                    });
                    return false;
                };
                var pages = ['configPage', 'buildBriefPage', 'buildPage', 'executeRequirePage'];
                return $http.get(constants.api('plugin/job/configs'), {cache: true}).then(function (data) {
                    var deps = [];
                    var configs = data.data;
                    if (configs instanceof Array) {
                        angular.forEach(configs, function (config) {
                            var array = [];
                            config && angular.forEach(pages, function (page) {
                                var pageConfig = config[page];
                                if (pageConfig) {
                                    var js = pageConfig.js;
                                    if (js && !contains(array, js)) {
                                        deps.push(constants.resource('/plugin/job/type/' + js));
                                        array.push(js);
                                    }
                                }
                            });
                        });
                    }
                    return deps;
                });
            }
        ]
    );
});
