/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file 送测确认执行controller
 * @author liming16
 */
define(['app-plugin-job', 'constants', 'css!./noticeQA'], function (app, constants) {
    app.controller(
        'NoticeQAExecuteController',
        [
            '$http', '$scope', 'pipelineDataService', 'pipelineContextService', NoticeQAExecuteController
        ]
    );

    function NoticeQAExecuteController($http, $scope, pipelineDataService, pipelineContextService) {
        var self = this;

        self.context = $scope.context;
        self.noticeQATesters = [];
        self.noticeQAContents = '';
        self.issueCardIds = '';

        self.jobBuild = $scope.job;
        self.checkAll = false;
        self.bindCards = true;
        self.hasMoreCards = false;
        self.loadingCards = false;
        self.startRevisionWithCard = 0;
        self.svnType = pipelineContextService.context.svnType;

        // 后端 svnType 为 'CHANGE', 前后端不一致导致 BUG
        if (self.svnType === 'CHANGES') {
            self.svnType = 'CHANGE';
        }

        // 构建页面执行时候弹窗
        if (angular.isDefined(self.jobBuild)) {
            if (self.jobBuild.realJobBuild !== null) {
                if (angular.isString(self.jobBuild.realJobBuild.testEmail)) {
                    var qaEmails = self.jobBuild.realJobBuild.testEmail.split(',');
                    angular.forEach(qaEmails, function (email) {
                        if (email !== '') {
                            self.noticeQATesters.push({
                                name: email
                            });
                        }
                    });
                }
                self.issueCards = [];
                self.loadingCards = true;
                pipelineDataService.getStorys2Bind(pipelineContextService.context.module, self.context.revision,
                    self.svnType)
                    .then(function (data) {
                        self.loadingCards = false;
                        if (data instanceof Object) {
                            self.hasMoreCards = data.hasMore;
                            self.startRevisionWithCard = data.revision;
                            if (data.issues) {
                                for (var i in data.issues) {
                                    if (data.issues.hasOwnProperty(i)) {
                                        var issue = data.issues[i];
                                        if (issue && !self.containsIssue(issue)) {
                                            self.issueCards.push(issue);
                                        }
                                    }
                                }
                            }
                        }
                    });
            }
        }

        self.toggleCheckAll = function () {
            for (var i in self.issueCards) {
                if (self.issueCards.hasOwnProperty(i)) {
                    self.issueCards[i].isChecked = self.checkAll;
                }

            }
        };

        self.getStorys2Bind = function (module, revision, svnType) {
            self.loadingCards = true;
            pipelineDataService.getStorys2Bind(module, revision, svnType)
                .then(function (data) {
                    self.loadingCards = false;
                    if (data instanceof Object) {
                        self.hasMoreCards = data.hasMore;
                        self.startRevisionWithCard = data.revision;
                        if (data.issues) {
                            for (var i in data.issues) {
                                if (data.issues.hasOwnProperty(i)) {
                                    var issue = data.issues[i];
                                    if (issue && !self.containsIssue(issue)) {
                                        self.issueCards.push(issue);
                                    }
                                }
                            }
                        }
                    }
                });
        };
        self.moreIssueCards = function () {
            self.getStorys2Bind(pipelineContextService.context.module, self.startRevisionWithCard,
                self.svnType);
        };

        self.containsIssue = function (issue) {
            var key = issue.prefixCode + '-' + issue.sequence;
            for (var i in self.issueCards) {
                if (self.issueCards.hasOwnProperty(i)) {
                    var issue = self.issueCards[i];
                    if (issue.prefixCode + '-' + issue.sequence === key) {
                        return true;
                    }
                }
            }
            return false;
        };

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

        /*self.testersChanged = function () {
         if (!angular.isDefined(self.jobBuild.realJobBuild)) {
         self.jobBuild.realJobBuild = {};
         }
         self.jobBuild.realJobBuild.testEmail = '';
         console.log(self.noticeQATesters);
         angular.forEach(self.noticeQATesters, function (tester) {

         self.jobBuild.realJobBuild.testEmail += tester.name + ',';
         });
         };*/

        self.checkedChanged = function () {
            var length = 0;
            for (var i in self.issueCards) {
                if (self.issueCards.hasOwnProperty(i)) {
                    var issue = self.issueCards[i];
                    if (issue.isChecked === true) {
                        length++;
                    }
                }
            }
            return length;
        };

        self.cardsBind = function () {
            var issueCardIds = '';
            if (self.bindCards) {
                for (var i in self.issueCards) {
                    if (self.issueCards.hasOwnProperty(i)) {
                        var issue = self.issueCards[i];
                        if (issue.isChecked === true) {
                            issueCardIds += issue.prefixCode + '-'
                                + issue.sequence + ',';
                        }
                    }
                }
            }
            self.jobBuild.realJobBuild.issueCardIds = issueCardIds;
        };
        $scope.$watch(self.checkedChanged, self.cardsBind);


        $scope.$watchCollection(function () {
            return self.noticeQATesters;
        }, function (newVal) {
            if (!angular.isDefined(self.jobBuild.realJobBuild)) {
                self.jobBuild.realJobBuild = {};
            }
            self.jobBuild.realJobBuild.testEmail = '';
            angular.forEach(newVal, function (tester) {

                self.jobBuild.realJobBuild.testEmail += tester.name + ',';
            });
        });

        $scope.$watch(function () {
            return self.jobBuild.realJobBuild.testEmail;
        }, function (newVal) {
            // TODO 改为消息机制
            if (!self.jobBuild.realJobBuild.testEmail) {
                $scope.$parent.$parent.$parent.$parent.disableFlag = true;
            } else {
                $scope.$parent.$parent.$parent.$parent.disableFlag = false;
            }
        });
    }
});
