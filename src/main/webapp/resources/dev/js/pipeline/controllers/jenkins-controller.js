/**
 * Created by niuwanpeng on 17/5/5.
 *
 * jenkins管理 Controller
 */

define(['app', 'constants'], function (app, constants) {
    'use strict';
    app.controller('JenkinsController', [
        'pipelineDataService',
        'pipelineContextService',
        '$scope',
        '$state',
        '$timeout',
        '$uibModal',
        '$window',
        JenkinsController
    ]);

    function JenkinsController(pipelineDataService, pipelineContextService, $scope, $state, $timeout, $uibModal, $window) {
        var self = this;
        self.currentModule = pipelineContextService.context.module;
        self.username = pipelineContextService.context.username;
        self.context = pipelineContextService;
        self.showLoading = false;
        $scope.checked = false;
        $scope.jenkinsConf = {};

        pipelineDataService.getJenkinsConfs(self.username).then(function (data) {
            self.jenkinsConfs = data;
        });
        self.addJenkinsConf = function () {
            pipelineDataService.addJenkinsConf($scope.jenkinsConf).then(function (response) {
                if (response.success == true) {
                    alert("添加成功");
                    $state.reload();
                } else {
                    alert(response.message);
                }
            });
        };
        self.checkConf = function () {
            pipelineDataService.checkJenkinsConf($scope.jenkinsConf).then(function (response) {
                $scope.checked = response.success;
                $scope.message = response.message;
            });
        };
        self.delete = function (id) {
            pipelineDataService.deleteJenkinsConf(id).then(function (data) {
                $state.reload();
            })
        };
        self.cancel = function () {
            $state.go(
                'builds.trunk',
                {
                    module: self.currentModule
                }
            );
        };

        $scope.cancelWindow = function () {
            self.showLoading = false;
            return $scope.uibModalInstance && $scope.uibModalInstance.dismiss();
        };

        $scope.goNewModule = function (newModule) {
            $state.go(
                'builds.trunk',
                {
                    module: newModule
                }
            );
        }


    }
});

