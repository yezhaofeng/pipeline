/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file 送测
 * @author liming16
 */
define(['app-plugin-job', 'constants', 'css!./noticeQA.css'], function (app, constants) {
    app.controller('NoticeQAController', ['$http', '$scope', '$uibModal', NoticeQAController]);

    function NoticeQAController($http, $scope, $uibModal) {
        var self = this;

        self.noticeQATesters = [];
        self.noticeQAContents = '';
        self.currentModule = $scope.context.module;
        self.realJobBuild = {};

        self.noticeQAInfo = {};
        self.confirmTestsPass = 'PASS';
        self.noticeQAStatus = 'INIT';
        self.noticeQAResultReason = '';

        // 展开brief获取送测信息
        (function () {
            if (angular.isDefined($scope.build)) {
                self.realJobBuild = $scope.build;
                return $http.get(constants.api('plugin/job/noticeqa/info/' + $scope.build.id))
                    .then(function (noticeQaInfo) {
                        self.noticeQAStatus = noticeQaInfo.data.noticeQaStatus;
                        self.noticeQAContents = noticeQaInfo.data.operSteps;
                        self.noticeQAResultReason = noticeQaInfo.data.confirmReason;
                    });
            }
        })();


        // 配置页面加载配置, 加载已有配置并赋值给 父Scope
        if (angular.isDefined($scope.job)) {
            if ($scope.job.testEmail !== null) {
                if (angular.isString($scope.job.testEmail)) {
                    var qaEmails = $scope.job.testEmail.split(',');
                    angular.forEach(qaEmails, function (email) {
                        if (email !== '') {
                            self.noticeQATesters.push({
                                name: email
                            });
                        }
                    });
                }
            }
        }


        self.getBaiduUsers = function (query) {
            return query && $http.get(constants.api('ajax/users/query?multi=true&limit=50&q='
                    + query))
                    .then(function (usersList) {
                        var cacheBaiduUsers = [];
                        angular.forEach(usersList.data, function (user) {
                            var cacheObj = {
                                name: user.name,
                                displayText: user.name + ',' + user.chineseName + ',' + user.departmentName
                            };
                            cacheBaiduUsers.push(cacheObj);
                        });
                        return cacheBaiduUsers;
                    });
        };


        self.testersChanged = function () {
            if (!angular.isDefined($scope.job)) {
                $scope.job = {};
            }
            $scope.job.testEmail = '';
            $scope.job.module = self.currentModule;
            angular.forEach(self.noticeQATesters, function (tester) {
                $scope.job.testEmail += tester.name + ',';
            });
        };


        self.openNoticeQAModal = function (type, build) {
            $http.get(constants.api('plugin/job/noticeqa/info/' + self.realJobBuild.id))
                .then(function (noticeQaInfo) {
                    self.noticeQAInfo = noticeQaInfo.data;
                    self.noticeQAStatus = noticeQaInfo.data.noticeQaStatus;
                    self.noticeQAContents = noticeQaInfo.data.operSteps;
                    self.noticeQAResultReason = noticeQaInfo.data.confirmReason;
                }).then(function (data) {
                    self.uibModalInstance = $uibModal.open({
                        templateUrl: constants.resource('/plugin/job/type/noticeQA/confirm-form.html'),
                        scope: $scope,
                        size: 'md',
                        windowClass: 'zoom noticeQA-modal'
                    });
                });
        };

        // cancel OR 关闭
        self.cancel = function () {
            self.uibModalInstance && self.uibModalInstance.close();
        };


        self.confirmPassOrNot = function () {
            if (self.noticeQAInfo.compileBuildId === -1) {
                self.noticeQAInfo.compileBuildId = parseInt($scope.compileBuildId, 0);
            }
            if (self.confirmTestsPass === 'PASS') {
                self.noticeQAInfo.noticeQaStatus = 'SUCC';
            } else if (self.confirmTestsPass === 'CANT_PASS') {
                self.noticeQAInfo.noticeQaStatus = 'FAIL';
            } else {
                self.noticeQAInfo.noticeQaStatus = 'INPROGRESS';
            }
            return $http.post(constants.api('plugin/job/noticeqa/update/' + $scope.jobBuildId), self.noticeQAInfo)
                .then(function (data) {
                    if (data.data === 'success') {
                        self.noticeQAStatus = 'SUCC';
                        self.cancel();
                    } else {
                        self.noticeQAStatus = 'FAIL';
                    }
                });


        };

        self.isManual = function () {
            if ($scope.context.currentStageTriggerMode === 'MANUAL') {
                return false;
            }
            return true;
        };


    }
});
