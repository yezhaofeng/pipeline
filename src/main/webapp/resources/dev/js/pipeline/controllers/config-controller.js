/**
 * @file 流水线配置
 *
 * @author langshiquan
 */
define(['app','angular', 'constants'], function (app,angular, constants) {
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
            self.paramMap2Entries(data);
            self.config = data;
            self.toggleActiveJob(0);
            console.log(self.config);
            console.log(self.activeJob);
            $scope.$applyAsync();
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
                triggerMode: 'AUTO',
                pluginType: plugin.pluginType,
                pluginConf:{},
                parameterMap:{},
                inParamsEntries:[]
            };
            var length = self.config.jobConfs.push(newJobConf);
            self.toggleActiveJob(length - 1);
            $scope.cancelWindow();
        };

        self.toggleActiveJob = function (index) {
            self.activeJob = self.config.jobConfs[index];
            console.log(self.activeJob.inParamsEntries);

        };

        self.initPluginInfo = function () {
            if ($scope.pluginInfo != undefined) {
                return;
            }
            pipelineDataService.getPluginInfo().then(function (data) {
                $scope.pluginInfo = data;
            });
        };
        self.deleteJob = function (index) {
            self.config.jobConfs.splice(index, 1);
        };


        self.addJobInParam = function () {
            self.activeJob.inParamsEntries.push({});
        };
        self.delJobInParamByIndex = function (index) {
            self.activeJob.inParamsEntries.splice(index, 1);
        };
        self.submit = function () {
            console.log(self.config);
        };

        $scope.cancelWindow = function () {
            return $scope.uibModalInstance && $scope.uibModalInstance.dismiss();
        };

        self.paramMap2Entries = function (data) {
            angular.forEach(data.jobConfs, function (jobConf, index) {
                jobConf.inParamsEntries = self.map2Entries(jobConf.parameterMap);
                console.log("paramMap2Entries");
            });
        };

        self.map2Entries = function (map) {
            var entries = [];
            map && angular.forEach(map, function (value, key) {
                entries.push({key: key, value: value});
            });
            return entries;
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