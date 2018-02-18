/**
 * Created by Wonpang New on 2016/9/10.
 *
 */

define(['app', 'constants'], function (app, constants) {
    'use strict';

    app.controller('BuildsController', [
        '$scope',
        '$location',
        '$state',
        '$uibModal',
        'pipelineContextService',
        'pipelineDataService',
        BuildsController
    ]);

    function BuildsController($scope, $location, $state, $uibModal, pipelineContextService, pipelineDataService) {
        var self = this;
        self.context = pipelineContextService.context;

        $scope.hasDefaultPipeline = false;
        $scope.opening = false;

        pipelineDataService.getPipelineConf(self.context.module, self.context.branchType)
            .then(function (reponse) {
                if (reponse == null || reponse == '' || reponse == undefined) {
                    $scope.hasDefaultPipeline = true;
                }
            });

        $scope.openDefaultPipeline = function () {
            $scope.opening = true;
            pipelineDataService.openDefaultPipelineConf(self.context.module).then(function (response) {
                if (response.success == true) {
                    $state.reload();
                } else {
                    alert(response.message);
                }
            });
        };

        $scope.buildPipeline = function () {
            var module = self.context.module;
            var branchType = self.context.branchType;
            if (branchType === 'TRUNK') {
                pipelineDataService.buildPipeline(module, branchType, 0).then(function (response) {
                    if (response.success == true) {
                        alert("任务提交成功");
                        $state.reload();
                    } else {
                        alert(response.message);
                    }
                });
            } else if (branchType === 'BRANCH') {
                pipelineDataService.getCommits(module, branchType).then(function (commits) {
                    $scope.commits = commits;
                    $scope.buildModal = $uibModal.open({
                        scope: $scope,
                        templateUrl: constants.resource('pipeline/modal-build.html'),
                        size: 'sm',
                        windowClass: 'zoom'
                    });
                });
            }
        };

        $scope.selectedBuildPipeline = function (triggerId) {
            var module = self.context.module;
            var branchType = self.context.branchType;
            pipelineDataService.buildPipeline(module, branchType, triggerId).then(function (response) {
                if (response.success == true) {
                    alert("任务提交成功");
                    $scope.cancelWindow();
                    $state.reload();
                } else {
                    alert(response.message);
                }
            });
        };

        $scope.cancelWindow = function () {
            return $scope.buildModal && $scope.buildModal.dismiss();
        }
    }
});