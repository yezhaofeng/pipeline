/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 发布项目视图
 */
define(['app', 'constants'], function appRealJobConfig(app, constants) {
    app.directive(
        'appRealJobConfig',
        [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        job: '=appJob',
                        form: '=appForm',
                        page: '=appPage',
                        config: '=appConfig',
                        context: '=appContext'
                    },
                    controller: '',
                    template: '<div class="agile-plugin-job" ng-include="templateHtml" ng-show="show"></div>',
                    replace: true,
                    link: function (scope, el, attr) {
                        scope.templateHtml = "/resources/dev/plugin/common/blank.html";
                        scope.show = false;
                        scope.$watch(function () {
                            return scope.job;
                        }, function (job) {
                            scope.templateHtml = '/resources/dev/plugin/job/type/' + job + "/config.html";
                            scope.show = true;
                        });
                    }
                };
            }
        ]
    );
});
