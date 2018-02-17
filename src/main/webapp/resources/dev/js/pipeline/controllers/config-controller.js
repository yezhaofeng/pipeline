/**
 * @file 流水线配置
 *
 * @author langshiquan
 */
define(['app', 'angular', 'constants'], function (app, angular, constants) {
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

        self.initConf = function (branchType) {
            pipelineDataService.getPluginInfos().then(function (data) {
                $scope.pluginInfo = data;
                pipelineDataService.getPipelineConf(self.currentModule, branchType).then(function (data) {
                    self.paramMap2Entries(data);
                    self.config = data;
                    self.toggleActiveJob(0);
                    $scope.$applyAsync();
                });
            });
        };

        self.addJob = function () {
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
                pluginConf: {},
                parameterMap: {},
                inParamsEntries: []
            };
            var length = self.config.jobConfs.push(newJobConf);
            self.toggleActiveJob(length - 1);
            $scope.cancelWindow();
        };

        self.toggleActiveJob = function (index) {
            self.activeJob = self.config.jobConfs[index];
            var pluginType = self.activeJob.pluginType;
            for (var x in $scope.pluginInfo) {
                if ($scope.pluginInfo[x].pluginType == pluginType) {
                    $scope.activeJobPluginInfo = $scope.pluginInfo[x];
                    break;
                }
            }
        };

        self.initPluginInfo = function () {
            if ($scope.pluginInfo != undefined) {
                return;
            }
            pipelineDataService.getPluginInfos().then(function (data) {
                $scope.pluginInfo = data;
            });
        };


        self.deleteJob = function (index) {
            self.config.jobConfs.splice(index, 1);
            self.toggleActiveJob(index - 1 >= 0 ? index - 1 : 0);
        };


        self.addJobInParam = function () {
            self.activeJob.inParamsEntries.push({});
        };
        self.delJobInParamByIndex = function (index) {
            self.activeJob.inParamsEntries.splice(index, 1);
        };
        self.submit = function () {
            $scope.saving = true;
            self.paramEntries2Map(self.config);
            pipelineDataService.savePipelineConf(self.currentModule, $scope.branchType, self.config).then(function (response) {
                if (response.success == true) {
                    $scope.saved = true;
                    $scope.cancel();
                } else {
                    $scope.saving = false;
                }
            });
        };
        self.sleep = function (n) {
            var start = new Date().getTime();
            while (true)  if (new Date().getTime() - start > n) break;

        };
        $scope.cancelWindow = function () {
            return $scope.uibModalInstance && $scope.uibModalInstance.dismiss();
        };

        self.paramMap2Entries = function (data) {
            angular.forEach(data.jobConfs, function (jobConf, index) {
                jobConf.inParamsEntries = self.map2Entries(jobConf.parameterMap);
            });
        };

        self.map2Entries = function (map) {
            var entries = [];
            map && angular.forEach(map, function (value, key) {
                entries.push({key: key, value: value});
            });
            return entries;
        };

        self.paramEntries2Map = function (data) {
            angular.forEach(data.jobConfs, function (jobConf, index) {
                jobConf.parameterMap = self.entries2Map(jobConf.inParamsEntries);
            });
        };
        self.entries2Map = function (entries) {
            var map = {};
            entries && angular.forEach(entries, function (entry, index) {
                map[entry.key] = entry.value || '';
            });
            return map;
        };
        $scope.cancel = function () {
            $state.go(
                'builds.trunk',
                {
                    module: self.currentModule
                }
            );
        };

        $scope.$watch(function () {
            return $scope.branchType;
        }, function (branchType) {
            self.initConf(branchType);
        });

    }
});