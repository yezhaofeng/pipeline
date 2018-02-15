define(['app', 'angular', 'constants'], function (app, angular, constants) {
    'use strict';

    app.controller('JenkinsJobController', ['$http', '$scope', '$interval', 'pipelineDataService', 'pipelineContextService', JenkinsJobController]);

    function JenkinsJobController($http, $scope, $interval, pipelineDataService, pipelineContextService) {
        var username = pipelineContextService.context.username;
        var selectedFullName = $scope.$parent.config.jobFullName;
        // 获取JenkinsJob列表
        pipelineDataService.getJenkinsJobs(username).then(function (data) {
            // 处理数据, 成2个list;
            var jenkinsJobConfs = [];
            var jenkinsJobFullNameList = [];
            angular.forEach(data, function (jenkinsServer, index) {
                var id = jenkinsServer.id;
                var jenkinsServerId = jenkinsServer.jenkinsServerId;
                var serverUrl = jenkinsServer.serverUrl;
                angular.forEach(jenkinsServer.jobs, function (jobName, index) {
                    var jenkinsJob = {};
                    jenkinsJob.jenkinsServerId = jenkinsServerId;
                    jenkinsJob.jobName = jobName;
                    jenkinsJob.serverUrl = serverUrl;
                    jenkinsJob.jobFullName = serverUrl + 'job/' + jobName;
                    jenkinsJobConfs.push(jenkinsJob);
                    var jobFullName = serverUrl + 'job/' + jobName;
                    jenkinsJobFullNameList.push(jobFullName);
                });
            });
            $scope.jenkinsJobs = jenkinsJobConfs;
            $scope.selectedFullName = selectedFullName;
            $scope.jenkinsJobFullNameList = jenkinsJobFullNameList;

        });

        $scope.al = function () {
            alert($scope.selectedFullName);
        };

        $scope.$watch('selectedFullName', function (selectedFullName) {
            console.log( $scope.$parent.config);
            console.log(selectedFullName);
            // 从jenkinsJobs找到对应的id，赋值给config
            for (var i in $scope.jenkinsJobs) {
                if ($scope.jenkinsJobs[i].jobFullName == selectedFullName) {
                    $scope.$parent.config.jenkinsServerId = $scope.jenkinsJobs[i].jenkinsServerId;
                    $scope.$parent.config.jobName = $scope.jenkinsJobs[i].jobName;
                    $scope.$parent.config.jobFullName = $scope.jenkinsJobs[i].jobFullName;
                    return;
                }
            }
        });
    }
});