/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 发布项目视图
 */
define(['app-plugin-job', 'constants', 'css!./main'], function appPluginJobJenkinsJobSelector(app, constants) {
    var tool = {
        containsJob: function (jobs, job) {
            for (var i in jobs) {
                if (jobs[i].jobId === job.jobId) {
                    return true;
                }
            }
            return false;
        },
        addNoRepeatJobs: function (jobs, jobs2Add) {
            for (var i = 0; i < jobs2Add.length; i++) {
                var job = jobs2Add[i];
                if (!tool.containsJob(jobs, job)) {
                    jobs.push(job);
                }
            }
        }
    };
    app.directive(
        'appPluginJobJenkinsJobSelector',
        [
            '$http',
            function ($http) {
                return {
                    restrict: 'E',
                    templateUrl: constants.resource('/plugin/job/type/jenkins/job-selector.html'),
                    replace: true,
                    link: function (scope, el, attr) {
                        scope.jobs = [];
                        var context = scope.context;
                        if (context && context.pipelineType === 'MODULE') {
                            $http
                                .get('/plugin/job/jenkins/jobs?module='
                                + context.module + '&svnType=' + context.svnType, {cache: true})
                                .success(function (data) {
                                    scope.jobs = data instanceof Array ? data : [];
                                })
                                .error(function () {
                                    scope.jobs = [];
                                });
                        } else {
                            scope.jobs = [];
                            angular.forEach(context.modulesConf, function (moduleConf) {
                                var svnType = moduleConf.branch && angular.uppercase(moduleConf.branch) !== 'TRUNK'
                                    ? 'BRANCHES' : 'TRUNK';
                                $http
                                    .get('/plugin/job/jenkins/jobs?module='
                                    + moduleConf.module + '&svnType=' + svnType, {cache: true})
                                    .success(function (data) {
                                        data instanceof Array && tool.addNoRepeatJobs(scope.jobs, data);
                                    });
                            });
                        }
                    }
                };
            }
        ]
    );
    app.controller('JenkinsJobController', ['$http', JenkinsJobController]);
    function JenkinsJobController($http) {
        var self = this;
        self.jenkinsJob = null;
        self.loaded = false;

        self.getInfo = function (build) {
            if (build != null) {
                $http.get('/plugin/job/jenkins/' + build.id).then(function (data) {
                    self.jenkinsJob = data.data;
                    self.loaded = true;
                });
            } else {
                self.loaded = true;
            }
        };
    }
});