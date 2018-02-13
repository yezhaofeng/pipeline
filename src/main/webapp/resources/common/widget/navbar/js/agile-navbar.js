/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file AGILE Navbar Module
 *
 * @author liming16
 */
define(
    [
        'constants',
        'jquery',
        'angular',
        'angular-ui-router',
        'angular-bootstrap',
        'agile-sidebar'
    ], function (constants, $, angular) {

        angular.module('agile.navbar', [
            'ui.bootstrap',
            'agile.sidebar'
        ]).run(['$templateCache', function ($templateCache) {
            $templateCache.put('template/navbar-li.html',
                '<li bo-class=\"{divider: leaf.name == \'divider\'}\">\n'
                + '<a ui-sref=\"{{leaf.link}}\" bo-if=\"leaf.name !== \'divider\'\ && leaf.link">{{::leaf.name}}</a>\n'
                + '<a href=\"{{leaf.href}}\" bo-if=\"leaf.name !== \'divider\'\ && leaf.href">{{::leaf.name}}</a>\n'
                + '</li>');
            $templateCache.put('template/navbar-ul.html',
                '<ul class=\'dropdown-menu\'>\n'
                + '<leaf bindonce ng-repeat=\'leaf in tree\' leaf=\'leaf\'></leaf>\n'
                + '</ul>');

        }]).controller('NavbarController', ['agileSidebar', 'pipelineContextService', 'pipelineDataService', '$state',
            function (agileSidebar, pipelineContextService, pipelineDataService, $state) {
                var self = this;

                self.logoIcon = constants.isOutside() ? 'ipipe-logo.svg' : 'logo.png';
                self.currentLoginUserAvatar = constants.avatorApi + constants.agile.user.name + '.jpg';

                if (constants.xiaolvyun && constants.xiaolvyun.page) {
                    self.xiaolvyunDashBoard = constants.xiaolvyun.page.dashboard;
                    self.xiaolvyuniPipe = constants.xiaolvyun.page.ipipe;
                }

                self.freqFuncMenu = [{
                    name: '模块集',
                    link: 'moduleGroup'
                }, {
                    name: '查看依赖',
                    link: 'showDepends'
                }, {
                    name: '返回旧版',
                    link: 'old'

                }];

                self.userFuncMenu = [{
                    name: '退出',
                    href: constants.passportLogoutUrl()
                }];

                // 初始化下拉列表
                angular.element('.dropdown').hover(
                    function () {
                        $('.dropdown-menu', this).not('.in .dropdown-menu').stop(true, true).slideDown('400');
                        $(this).toggleClass('open');
                    },
                    function () {
                        $('.dropdown-menu', this).not('.in .dropdown-menu').stop(true, true).slideUp('400');
                        $(this).toggleClass('open');
                    }
                );

                angular.element('#agile-search-input').focus(function (e) {
                    angular.element('#agile-search').addClass('agile-search-focus');
                    $('.agile-search-icon').addClass('agile-search-icon-focus');
                    angular.element(this).addClass('agile-search-input-focus');
                });
                angular.element('#agile-search-input').focusout(function (e) {
                    angular.element('#agile-search').removeClass('agile-search-focus');
                    angular.element('.agile-search-icon').removeClass('agile-search-icon-focus');
                    angular.element(this).removeClass('agile-search-input-focus');
                });
                angular.element('#AGILE_USERNAME').text(constants.agile.user.name);

                angular.element('#agile-search-input').keyup(function (e) {
                    if (e.keyCode === 13) {
                        var inputModule = angular.element(this).val();
                        if (inputModule !== pipelineContextService.context.module) {
                            pipelineDataService.getSearchModules(inputModule).then(function (modules) {
                                angular.forEach(modules, function (module) {
                                    if (module.path === inputModule) {
                                        $state.go(
                                            'builds.trunk',
                                            {
                                                module: inputModule
                                            }
                                        );
                                        // pipelineContextService.initContext(inputModule);
                                        return;
                                    }
                                });
                            });
                        }
                    }
                });


                self.openCustomerService = function (hi, hiId, webAddress) {
                    function validataOS() {
                        if (navigator.userAgent.indexOf('Window') > 0) {
                            return 'Windows';
                        } else if (navigator.userAgent.indexOf('Mac OS X') > 0) {
                            return 'Mac ';
                        } else if (navigator.userAgent.indexOf('Linux') > 0) {
                            return 'Linux';
                        } else {
                            return 'NUll';
                        }
                    }

                    if (validataOS() === 'Windows') {
                        var f = document.createElement('form');
                        document.body.appendChild(f);
                        f.setAttribute('action', 'baidu://message');
                        var input = document.createElement('input');
                        input.setAttribute('name', 'appid');
                        input.setAttribute('value', hiId);
                        input.setAttribute('type', 'hidden');
                        f.appendChild(input);
                        f.submit();
                        document.body.removeChild(f);
                    } else {
                        if (webAddress === '') {
                            // IIT.dialog.alert('系统提示', '该系统暂未支持mac', true);
                        } else {
                            window.open(webAddress);
                        }
                    }
                    return false;
                };

                self.isOutside = function () {
                    return constants.isOutside();
                };

                // 侧边栏 导航其他et产品
                self.openSideBar = function (position, backdrop) {
                    self.asideState = {
                        open: true,
                        position: position
                    };

                    function postClose() {
                        self.asideState.open = false;
                    }

                    agileSidebar.open({
                        templateUrl: 'sidebar.html',
                        placement: position,
                        size: 'sm',
                        backdrop: backdrop
                        /*controller: function ($scope, $uibModalInstance) {
                         $scope.ok = function (event) {
                         $uibModalInstance.close();
                         event.stopPropagation();
                         };
                         $scope.cancel = function (event) {
                         $uibModalInstance.dismiss();
                         event.stopPropagation();
                         };
                         }*/
                    }).result.then(postClose, postClose);
                };
            }]).directive('tree', function () {
            return {
                restrict: 'E',
                replace: true,
                scope: {
                    tree: '='
                },
                templateUrl: 'template/navbar-ul.html'
            };
        }).directive('leaf', ['$compile', function ($compile) {
            return {
                restrict: 'E',
                replace: true,
                scope: {
                    leaf: '='
                },
                templateUrl: 'template/navbar-li.html',
                link: function (scope, element, attrs) {
                    if (angular.isArray(scope.leaf.subtree)) {
                        element.append('<tree tree=\"leaf.subtree\"></tree>');

                        var parent = element.parent();
                        var classFound = false;

                        while (parent.length > 0 && !classFound) {

                            if (parent.hasClass('navbar-right')) {
                                classFound = true;
                            }
                            parent = parent.parent();
                        }

                        if (classFound) {
                            element.addClass('dropdown-submenu-right');
                        } else {
                            element.addClass('dropdown-submenu');
                        }

                        $compile(element.contents())(scope);
                    }
                }
            };
        }]);
    });
