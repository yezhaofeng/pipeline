/**
 * Created by Wonpang New on 2016/9/10.
 *
 *  单个分支流水线
 */

define(['app', 'angular'], function (app, angular) {
    'use strict';

    app.controller('BranchController', [
        'pipelineDataService',
        '$scope',
        '$location',
        '$state',
        'pipelineContextService',
        BranchController
    ]);

    function BranchController(pipelineDataService, $scope, $location, $state, pipelineContextService) {
        var self = this;
        pipelineContextService.setBranchType('BRANCH');
        self.context = pipelineContextService.context;
        self.module = $state.params.module || self.context.module;
        self.branchName = $state.params.branch;
        pipelineContextService.setModule(self.module);
        self.showLoadMoreBuildsLoader = false;
        self.noMoreBuildsToLoad = false;
        self.initBuildsDone = false;

        self.initBuilds = pipelineDataService.getPipelineBuildsByBranch(self.module, "BRANCH", self.branchName)
            .then(function (data) {
                self.pipelineBuilds = data instanceof Array ? data : [];
                self.initBuildsDone = true;
            })
            .then(function () {
                pipelineDataService.getBranches(self.module,"BRANCH")
                    .then(function (data) {
                        var branches = [];
                        angular.forEach(data, function (branchObj, index) {
                            branches.push(branchObj.branchName);
                        });
                        self.branches = branches;
                    });
            });

        self.loadMoreBuilds = function () {
            var lastPipelineId = self.pipelineBuilds[self.pipelineBuilds.length - 1].pipelineBuildId;
            if (angular.isDefined(lastPipelineId)) {
                self.showLoadMoreBuildsLoader = true;
                pipelineDataService.getBranchPipelines(self.context.username, self.module, self.branchName, lastPipelineId)
                    .then(function (data) {
                        if (data instanceof  Array && data.length > 0) {
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