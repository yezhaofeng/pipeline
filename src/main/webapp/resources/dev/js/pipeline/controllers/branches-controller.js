/**
 * Created by Wonpang New on 2016/9/10.
 *
 *  全部分支流水线
 */

define(['app', 'angular'], function (app, angular) {
    'use strict';

    app.controller('BranchesController', [
        'pipelineDataService',
        '$scope',
        '$location',
        '$state',
        'pipelineContextService',
        BranchesController
    ]);

    function BranchesController(pipelineDataService, $scope, $location, $state, pipelineContextService) {
        var self = this;
        pipelineContextService.setBranchType('BRANCH');
        self.context = pipelineContextService.context;
        self.module = $state.params.module || self.context.module;
        pipelineContextService.setModule(self.module);
        self.showLoadMoreBuildsLoader = false;
        self.noMoreBuildsToLoad = false;
        self.initBuildsDone = false;

        self.initBuilds = pipelineDataService.getPipelineBuilds(self.module, "BRANCH")
            .then(function (data) {
                var branchPipelines = {};
                var otherBranchPipelines = {};
                angular.forEach(data,function(pipelineBuild,index){
                    var branch = pipelineBuild.branch;
                    if(branchPipelines[branch] == undefined){
                        branchPipelines[branch] = [];
                        otherBranchPipelines[branch] = [];
                        branchPipelines[branch].push(pipelineBuild);
                    }else{
                        otherBranchPipelines[branch].push(pipelineBuild);
                    }
                });
                self.branchPipelines = branchPipelines;
                self.otherBranchPipelines = otherBranchPipelines;
                self.initBuildsDone = true;
            })
            .then(function () {
                pipelineDataService.getBranches(self.module)
                    .then(function (data) {
                        var branches = [];
                        angular.forEach(data, function (branchObj, index) {
                            branches.push(branchObj.branchName);
                        });
                        self.branches = branches;
                    });
            });

        self.loadSingleBranchPipelines = function (fold, branch) {
            if (!fold) {
                angular.forEach(self.otherBranchPipelines[branch],function(otherPipelines,index){
                    self.branchPipelines[branch].push(otherPipelines);
                });
            }
        };

        self.loadMoreBuilds = function () {
            var lastBranchId = self.branchPipelines[self.branchPipelines.length - 1].branchId;
            if (angular.isDefined(lastBranchId)) {
                self.showLoadMoreBuildsLoader = true;
                pipelineDataService.getTrunkPipelines(self.context.username, self.module, lastBranchId)
                    .then(function (data) {
                        if (data instanceof Array && data.length > 0) {
                            angular.forEach(data, function (data) {
                                self.pipelineBuilds.push(data);
                            });
                        }
                        else {
                            self.noMoreBuildsToLoad = true;
                        }
                        self.showLoadMoreBuildsLoader = false;
                    });
            }
        };
    }
});