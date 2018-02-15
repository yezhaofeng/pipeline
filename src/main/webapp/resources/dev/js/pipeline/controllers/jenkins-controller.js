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

        pipelineDataService.getJenkinsConfs(self.username).then(function (data) {
            self.jenkinsConfs = data;
        });
        self.addJenkinsConf = function () {
            pipelineDataService.addJenkinsConf($scope.jenkinsConf).then(function (data) {
                alert(data);
                $window.location.reload();
            });
        };
        self.delete = function (id) {
            pipelineDataService.deleteJenkinsConf(id).then(function (data) {
                $window.location.reload();
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

