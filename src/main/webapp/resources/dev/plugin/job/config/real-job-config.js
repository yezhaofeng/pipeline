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
                    templateUrl: 'resources/dev/plugin/job/type/bcloud/config.html',
                    replace: true,
                    link: function (scope, el, attr) {
                        scope.$watch(function () {
                            return scope.page;
                        }, function (page) {
                            console.log(scope);
                            var html = '';
                            if (page) {
                                html = page.html;
                            }
                            alert(1);
                            // plugin job config html
                            scope.templateHtml = constants.resource(html ? '/plugin/job/type/' + html
                                : '/plugin/common/blank.html');

                            scope.show = true;
                        });
                    }
                };
            }
        ]
    );
});
