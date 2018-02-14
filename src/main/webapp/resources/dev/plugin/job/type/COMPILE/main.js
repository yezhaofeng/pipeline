/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file BCloud plugin controller
 * @author liming16
 */
define(['app', 'constants'], function (app, constants) {
    'use strict';

    app.controller('BCloudPluginController', ['$http', '$scope', '$interval', BCloudPluginController]);

    function BCloudPluginController($http, $scope, $interval) {
        //alert(1);
        console.log("编译");
        //console.log($scope.$parent.config);
        $scope.config = $scope.$parent.config;

        $scope.update = function(){
            $scope.config.id = 2333;
        };
        //    var self = this;
        //    self.mode = 'bcloud';
        //    self.codemirrorOpts = {
        //        lineNumbers: true,
        //        lineWrapping: true,
        //        indentWithTabs: false,
        //        theme: 'ambiance',
        //        fixedGutter: false,
        //        viewportMargin: Infinity,
        //        readOnly: true
        //    };
        //    self.isOutside = constants.isOutside();
        //
        //    // js 字符串连接 较为高效的方式
        //    // see https://developers.google.com/speed/articles/optimizing-javascript
        //    // see http://www.sitepoint.com/javascript-fast-string-concatenation/
        //    $scope.logDataBuffer = ['日志加载中...'];
        //
        //    self.logData = $scope.logDataBuffer.join('\n');
        //    self.loadingLogDataPromise = null;
        //    // 每隔1s去请求log
        //    self.TIME_UNIT_TO_FETCH_LOG = 1000;
        //
        //    self.isLogCompleted = true;
        //    self.logUrl = '';
        //
        //    var currentPos = 0;
        //    var currentIndex = 0;
        //
        //    var fetchInterval = $interval(function () {
        //        self.loadingLogDataPromise = $http.get(constants.api('build/completeStatus/' + $scope.build.id))
        //            .then(function (compileStatus) {
        //                return compileStatus.data;
        //            })
        //            .then(function (isCompileDone) {
        //                $http.get(constants.api('log/compile/' + $scope.build.id + '/' + currentIndex))
        //                    .then(function (origin) {
        //                        if (angular.isNumber(origin.data.index)) {
        //                            currentIndex = origin.data.index;
        //                        }
        //                        var logSegment = origin.data.content;
        //                        if (angular.isString(logSegment)) {
        //                            logSegment = logSegment.trim();
        //                            if (logSegment.length !== 0) {
        //                                $scope.logDataBuffer[currentPos++] = logSegment;
        //                            }
        //                        }
        //                        self.isLogCompleted = origin.data.complete;
        //                        self.logUrl = origin.data.url;
        //                    });
        //                if (isCompileDone) {
        //                    $interval.cancel(fetchInterval);
        //                }
        //            });
        //    }, self.TIME_UNIT_TO_FETCH_LOG);
        //
        //    $scope.$watchCollection('logDataBuffer', function (newValue) {
        //        if ($scope.logDataBuffer != null && $scope.logDataBuffer.length > 0) {
        //            self.logData = $scope.logDataBuffer.join('\n');
        //        }
        //    });
        //    $scope.$on('$destroy', function () {
        //        $interval.cancel(fetchInterval);
        //    });
        //}
        //
        //app.directive('bcloudPluginTool', [function () {
        //    return {
        //        restrict: 'AE',
        //        replace: true,
        //        scope: true,
        //        link: function (scope, element, attrs) {
        //            scope.tool = {
        //                generateWgetProductPath: function (productPath) {
        //                    if (constants.isOutside()) {
        //                        return constants.originAgileApi(productPath);
        //                    }
        //                    return productPath;
        //                }
        //            };
        //        }
        //    };
        //}]);
        //
        //app.controller('BcloudBriefController',
        //    ['$http', '$scope', '$interval', BcloudBriefController]);
        //function BcloudBriefController($http, $scope, $interval) {
        //    var self = this;
        //
        //    self.packageSeeds = [];
        //    self.showPackageSeeds = false;
        //    self.packageBuildId = '';
        //
        //    self.packageStatus = '';
        //    self.seedStatus = '';
        //
        //    self.formatStatus = function (status) {
        //        if ('RUNNING' === status) {
        //            return '进行中...';
        //        } else if ('SUCC' === status) {
        //            return '成功！';
        //        } else if ('FAIL' === status) {
        //            return '失败';
        //        } else if ('WAITTING' === status) {
        //            return '请稍等...';
        //        } else {
        //            return status;
        //        }
        //    };
        //
        //    self.getPackageSeeds = function (compileBuildId) {
        //        $http.
        //        get(constants.api('/plugin/pkgseed/bcloudPkg?compileBuildId=') + compileBuildId).
        //        then(function (data) {
        //            self.showPackageSeeds = true;
        //            self.packageBuildId = data.data.packageBuildId;
        //            self.packageSeeds = data.data.packageSeeds;
        //            self.packageStatus = data.data.packageStatus;
        //            self.seedStatus = data.data.seedStatus;
        //
        //            if (null === self.packageSeeds) {
        //                self.showPackageSeeds = false;
        //                return;
        //            }
        //            ;
        //        });
        //    };
        //}
        //
        //app.directive('showBcloudBrief', [function () {
        //    return {
        //        restrict: 'AE',
        //        replace: true,
        //        templateUrl: constants.resource('/plugin/job/type/bcloud/build-brief_pkg_seed_show.html')
        //    };
        //}]);
    }
});