/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 扩充job执行参数
 * @author liming16
 */
define(['app-plugin-job', 'constants'], function JobExecuteRequire(app, constants) {
    app.directive(
        'jobExecuteRequire',
        [
            'appPluginJobService',
            function (appPluginJobService) {
                return {
                    restrict: 'E',
                    scope: {
                        job: '=',
                        context: '=appContext',
                        disableFlag: '='
                    },
                    template: '<div class="agile-plugin-job" ng-include="templateHtml" ng-show="show"></div>',
                    replace: true,
                    link: function (scope, el, attr) {

                        appPluginJobService.getConfigByType(scope.job.jobType).then(function (config) {
                            var html = '';
                            if (config) {
                                var page = config.executeRequirePage;
                                if (page) {
                                    html = page.html;
                                }
                            }
                            // plugin job execute require html
                            scope.templateHtml = constants.resource(html ? '/plugin/job/type/' + html
                                : '/plugin/common/blank.html');
                            scope.show = true;
                        });
                        var agileJobBuild = scope.job;
                        scope.$watch(function () {
                            return scope.job;
                        }, function (job) {
                            if (agileJobBuild) {
                                angular.extend(job, agileJobBuild);
                            }
                            agileJobBuild = scope.job;
                        });
                    }
                };
            }
        ]
    );
});
