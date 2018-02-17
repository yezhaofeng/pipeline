/**
 * Created by niuwanpeng on 17/4/27.
 *
 * pipeline构建指令
 */
define(['app', 'constants', 'angular'], function (app, constants, angular) {
    app.directive(
        'pipelineBuild',
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
                        currentBuild: '=',
                        context: '=appContext',
                        index: '=index'
                    },
                    templateUrl: constants.resource('directive/pipeline-build.html'),
                    replace: true,
                    link: function (scope, el) {
                        scope.tool = {
                            buildManual: function (jobBuild) {
                                scope.jobBuild = jobBuild;
                                scope.runtimeParam = {};
                                scope.runtimeParam.execParam = {};
                                scope.runtimeParam.runtimePluginParam = {};

                                // 需要找用户自定义参数
                                for (var key in jobBuild.inParameterMap) {
                                    if (key.indexOf("PIPELINE_") !== 0) {
                                        scope.runtimeParam.execParam[key] = jobBuild.inParameterMap[key];
                                    }
                                }
                                pipelineDataService.getJobRuntimeRequire(jobBuild.id).then(function (runtimeRequireList) {
                                    console.log(runtimeRequireList);
                                    scope.runtimeRequireList = runtimeRequireList;
                                    angular.forEach(runtimeRequireList, function (runtimeRequire, index) {
                                        scope.runtimeParam.runtimePluginParam[runtimeRequire.name] = runtimeRequire.defaultValue;
                                    });
                                    // 如果无运行时依赖参数和用户自定义参数则直接执行，不弹窗
                                    if (JSON.stringify(scope.runtimeParam.runtimePluginParam) == "{}" && JSON.stringify(scope.runtimeParam.execParam) == "{}") {
                                        scope.tool.doBuild(jobBuild.id);
                                    } else {
                                        scope.uibModalInstance = $uibModal.open({
                                            scope: scope,
                                            templateUrl: constants.resource('config/job.build.runtime.html'),
                                            size: 'lg',
                                            windowClass: 'zoom'
                                        });
                                    }
                                });
                            },
                            doBuild: function (id) {
                                pipelineDataService.doJobBuild(id, scope.runtimeParam).then(function (response) {
                                    if (response.success == true) {
                                        scope.tool.cancelWindow();
                                    } else {
                                        alert(response.message);
                                    }
                                });
                            },
                            cancelWindow: function () {
                                return scope.uibModalInstance && scope.uibModalInstance.dismiss();
                            },
                            updateJobBuild: function () {
                                var promises = [];
                                return $q.all(promises);
                            },
                            alertInfo: function (message) {
                                window.alert(message);
                            }
                        };

                        changePipelineStatus(scope.currentBuild.pipelineStatus);
                        scope.expandClass = 'normal';

                        /* 详情展开页 start*/
                        var toggleExpand = false;
                        var previousJobIndex = -2;
                        // 控制显示向下箭头
                        scope.currentJobExpandPointers = new Array(scope.currentBuild.jobBuildBeanList.length);
                        // 初始化currentJobExpandPointers
                        for (var i = 0; i < scope.currentJobExpandPointers.length; i++) {
                            scope.currentJobExpandPointers[i] = false;
                        }


                        scope.cancelParameterDiffWindow = function () {
                            return scope.parameterDiffModalInstance && scope.parameterDiffModalInstance.dismiss();
                        };
                        scope.showBuildParam = function (jobBuildInfo) {
                            var parameterDiffList = [];
                            var param;
                            if (jobBuildInfo.jobStatus == "SUCCESS") {
                                param = jobBuildInfo.outParameterMap;
                            } else {
                                param = jobBuildInfo.inParameterMap;
                            }
                            for (var key in param) {
                                var parameterDiff = {};
                                parameterDiff.name = key;
                                parameterDiff.inValue = jobBuildInfo.inParameterMap[key] === undefined ? '-' : jobBuildInfo.inParameterMap[key];
                                parameterDiff.outValue = jobBuildInfo.outParameterMap[key];
                                parameterDiffList.push(parameterDiff);
                            }
                            scope.parameterDiffList = parameterDiffList;
                            scope.parameterDiffModalInstance = $uibModal.open({
                                scope: scope,
                                templateUrl: constants.resource('config/parameter.diff.html'),
                                size: 'lg',
                                windowClass: 'zoom'
                            });
                        };
                        scope.stageTabToggle = function (jobIndex) {
                            scope.jobBuildInfo = {};
                            var templateUrl = "pipeline-plugin-info-tpl";
                            if (jobIndex == -1) {
                                templateUrl = "pipeline-commit-info-tpl";
                            } else {
                                var currentJob = scope.currentBuild.jobBuildBeanList[jobIndex];
                                var pluginType = currentJob.pluginType;
                                // 模板名字约定
                                //templateUrl = "pipeline-" + pluginType + "-info-tpl";
                                // 模板数据约定
                                pipelineDataService.getJobBuild(currentJob.id).then(function (jobBuildInfo) {
                                    scope.jobBuildInfo = jobBuildInfo;
                                });
                            }
                            if (previousJobIndex !== jobIndex) {
                                scope.currentJobExpandPointers[previousJobIndex] = false;
                                scope.currentJobExpandPointers[jobIndex] = true;
                                previousJobIndex = jobIndex;
                                toggleExpand = false;

                            } else if (previousJobIndex === jobIndex) {
                                scope.currentJobExpandPointers[previousJobIndex]
                                    = !scope.currentJobExpandPointers[previousJobIndex];
                            }

                            scope.toggleInfo(jobIndex, templateUrl);
                        };

                        scope.toggleInfo = function (stageIndex, templateUrl) {
                            if (!toggleExpand) {
                                var expandArea = el.children()[1];
                                if (angular.isDefined(expandArea)) {
                                    var template = $templateCache.get(templateUrl);
                                    if (template == null) {
                                        throw new URIError('无法找到指定模版:' + templateUrl);
                                    }
                                    var angularExpandArea = angular.element(expandArea);

                                    scope.tool.updateJobBuild().then(function () {
                                        var compiledTemplate = $compile(template)(scope);
                                        // console.log(compiledTemplate);
                                        angularExpandArea.html(compiledTemplate);

                                        // var insertedTemplate = angular.element(compiledTemplate);
                                        // see http://stackoverflow.com/questions/9911554/jquery-get-div
                                        // -width-after-document-is-ready-and-rendered

                                        /* setTimeout(function () {
                                         angularExpandArea[0].style.height = insertedTemplate[0].clientHeight + 'px';
                                         }, 0);*/
                                    });
                                } else {
                                    throw new SyntaxError('无法找到expand-area');
                                }
                                toggleExpand = true;
                                scope.expandClass = 'expand';
                            } else {
                                var expandArea = el.children()[1];
                                angular.element(expandArea).empty();
                                scope.expandClass = 'normal';
                                toggleExpand = false;
                            }
                        };
                        /* 详情展开页 end*/

                        function changePipelineStatus(newStatus) {
                            if (newStatus === 'SUCCESS') {
                                scope.pipelineStatusClass = 'success';
                            } else if (newStatus === 'FAIL') {
                                scope.pipelineStatusClass = 'fail';
                            }
                            else if (newStatus === 'BUILDING') {
                                scope.pipelineStatusClass = 'running';
                            }
                            else {
                                scope.pipelineStatusClass = 'waiting';
                            }
                        };

                        scope.$watch(function () {
                            return scope.currentBuild.pipelineStatus;
                        }, function (newStatus) {
                            changePipelineStatus(newStatus);
                        });
                    }
                };
            }
        ]
    );
});
