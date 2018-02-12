/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 发布项目视图
 */
define(['app-plugin-job', 'constants'], function appRealJobBuildBrief(app, constants) {
    app.directive(
        'appRealJobBuildBrief',
        [
            'appPluginJobService',
            function (appPluginJobService) {
                return {
                    restrict: 'E',
                    scope: {
                        build: '=appBuild',
                        type: '=appType',
                        context: '=appContext',
                        stageBuild: '=',
                        jobBuildId: '@',
                        compileBuildId: '@',
                        packageBuildType: '='
                    },
                    template: '<div class="agile-plugin-job" ng-include="templateHtml" ng-show="show"></div>',
                    replace: true,
                    link: function (scope, el, attr) {
                        scope.context.isOutside = constants.isOutside();
                        scope.detailUrl = constants.page.pipeline.detail(scope.context.pipelineBuildId,
                            scope.context.jobBuildId);
                        scope.$watch(function () {
                            return scope.type;
                        }, function (type) {
                            type && appPluginJobService.getConfigByType(type).then(function (config) {
                                var html = '';
                                if (config) {
                                    var page = config.buildBriefPage;
                                    if (page) {
                                        html = page.html;
                                    }
                                }

                                // plugin job config html
                                scope.templateHtml = constants.resource(html ? '/plugin/job/type/' + html
                                    : '/plugin/common/blank.html');

                                scope.show = true;
                            });
                        });
                    }
                };
            }
        ]
    );
});
