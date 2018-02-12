/**
 * @file 流水线配置
 *
 * @author langshiquan
 */
define(['app', 'constants'], function (app, constants) {
    'use strict';
    app.controller('ConfigController', [
        'pipelineDataService',
        'pipelineContextService',
        '$scope',
        '$state',
        '$timeout',
        '$uibModal',
        '$window',
        ConfigController
    ]);

    function ConfigController(pipelineDataService, pipelineContextService, $scope, $state, $timeout, $uibModal, $window) {
        var self = this;
        self.currentModule = pipelineContextService.context.module;
        self.username = pipelineContextService.context.username;
        self.context = pipelineContextService;
        self.config = {};

        // 默认是主干
        $scope.branchType = "TRUNK";
        pipelineDataService.getPipelineConf(self.currentModule, 'TRUNK').then(function (data) {
            self.config = data;
            self.activeJob = data.jobConfs[0];
        });

        self.addJob = function () {
            self.initPluginInfo();
            $scope.uibModalInstance = $uibModal.open({
                scope: $scope,
                templateUrl: constants.resource('config/plugin.select.html'),
                size: 'lg',
                windowClass: 'zoom'
            });
        };
        $scope.selectPlugin = function (index) {
            var plugin = $scope.pluginInfo[index];
            var newJobConf = {
                name: plugin.name,
                triggerMode: 'AUTO'
            };
            self.config.jobConfs.push(newJobConf);
            self.toggleActiveJob(newJobConf);
            $scope.cancelWindow();
        };

        self.toggleActiveJob = function (job) {
            self.activeJob = job;
        };

        self.initPluginInfo = function () {
            if ($scope.pluginInfo != undefined) {
                return;
            }
            pipelineDataService.getPluginInfo().then(function (data) {
                $scope.pluginInfo = data;
            });
        };
        self.addJobInParam = function () {
            // TODO
            var params = [];
            console.log(self.activeJob);
            console.log(self.activeJob.parameterMap);
            self.activeJob.parameterMap = params;
            params.push({});
        };
        self.deleteJob = function (index) {
            self.config.jobConfs.splice(index, 1);
        };
        self.delJobInParamByIndex = function () {
            // TODO
        };


        $scope.cancelWindow = function () {
            return $scope.uibModalInstance && $scope.uibModalInstance.dismiss();
        };

        $scope.$watch(function () {
            return $scope.branchType;
        }, function (branchType) {
            pipelineDataService.getPipelineConf(self.currentModule, branchType).then(function (data) {
                self.config = data;
                self.activeJob = data.jobConfs[0];
            });
        });
    }
});