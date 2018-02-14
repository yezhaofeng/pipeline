define(['app', 'angular', 'constants'], function (app, angular, constants) {
    'use strict';

    app.controller('JenkinsJobController', ['$http', '$scope', '$interval', 'pipelineDataService', 'pipelineContextService', JenkinsJobController]);

    function JenkinsJobController($http, $scope, $interval, pipelineDataService, pipelineContextService) {
        $scope.config = $scope.$parent.config;
        var username = pipelineContextService.context.username;
        // 获取JenkinsJob列表
        pipelineDataService.getJenkinsJobs(username).then(function (data) {
            // 处理数据
            var jenkinsJobConfs = [];
            angular.forEach(data, function (jenkinsServer, index) {
                var jenkinsServerId = jenkinsServer.jenkinsServerId;
                var serverUrl = jenkinsServer.serverUrl;
                angular.forEach(jenkinsServer.jobs, function (jobName, index) {
                    var jenkinsJob = {};
                    jenkinsJob.jenkinsServerId = jenkinsServerId;
                    jenkinsJob.jobName = jobName;
                    jenkinsJob.serverUrl = serverUrl;
                    jenkinsJob.fullname = serverUrl + 'job/' + jobName;
                    jenkinsJobConfs.push(jenkinsJob);
                });
            });
            $scope.jenkinsJobs = jenkinsJobConfs;
        });
        $scope.jenkinsJobOnChange = function () {
            alert(1);
            $scope.config.id = null;
            $scope.config.jenkinsServerId = $scope.jenkinsJob.jenkinsServerId;
            $scope.config.jobName = $scope.jenkinsJob.jobName;

        };

        $scope.$watch($scope.selected, function (newValue) {
            console.log(newValue);
            //console.log($scope.config);
        });
    }
});