/**
 * @file pipelines构建指令
 */
define(['app', 'constants'], function (app, constants) {
    app.directive(
        'pipelineBuilds',
        [
            'pipelineDataService',
            '$templateCache',
            '$compile',
            '$uibModal',
            '$q',
            function (pipelineDataService, $templateCache, $compile, $uibModal, $q) {
                return {
                    restrict: 'E',
                    scope: {
                        builds: '=appPipelineBuilds',
                        context: '=context',
                        fold: '=appFold'
                    },
                    templateUrl: constants.resource('directive/pipeline-builds.html'),
                    replace: true,
                    link: function (scope) {
                        //var pipelineConfId = scope.appPipelineBuilds[0].pipelineConfId;
                        scope.doPipelineBuildByTriggerId = function(triggerId){
                            pipelineDataService.buildPipeline(pipelineConfId, triggerId).then(function(response){
                                if (response.success == true) {
                                    alert("任务提交成功");
                                } else {
                                    alert(response.message);
                                }
                            });
                        }
                    }
                };
            }

        ]
    );
});
