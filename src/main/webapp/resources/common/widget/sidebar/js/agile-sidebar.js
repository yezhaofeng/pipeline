/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @file 侧边栏封装
 * @author liming16
 */

define(['angular', 'angular-bootstrap'], function (angular) {
    'use strict';
    angular.module('agile.sidebar', ['ui.bootstrap.modal'])
        .run(['$templateCache', function ($templateCache) {
            'use strict';
            $templateCache.put('sidebar.html',
                '<div class=\"modal-body\" id=\"ettools-sidebar\">'
                + '    <ul class=\"list-unstyled\">\n'
                + '      <li><a href=\"http://icode.baidu.com/\" target="_blank">iCode</a></li>'
                + '      <li><a href=\"http://newicafe.baidu.com/\" target="_blank">iCafe</a></li>'
                + '      <li><a href=\"http://yeying.baidu.com\" target="_blank">夜莺</a></li>'
                + '      <li><a href=\"http://hetu.baidu.com/\" target="_blank">河图</i></a></li>'
                + '      <li><a href=\"http://hetu.baidu.com/api/platform/index?platformId=1596\" target="_blank">Build<br/>Cloud</a></li>'
                + '      <li><a href=\"http://kaopu.baidu.com/\" target="_blank">靠谱</a></li>'
                + '    </ul>\n'
                + '</div>'
            );
        }])
        .factory('agileSidebar', ['$uibModal', function ($uibModal) {
            var defaults = this.defaults = {
                placement: 'left'
            };
            var sideFactory = {
                // override open method
                open: function (config) {
                    var options = angular.extend({}, defaults, config);
                    // check placement is set correct
                    if (['left', 'right', 'bottom', 'top'].indexOf(options.placement) === -1) {
                        options.placement = defaults.placement;
                    }
                    var vertHoriz = ['left', 'right'].indexOf(options.placement) === -1 ? 'vertical' : 'horizontal';
                    // set aside classes
                    options.windowClass = 'agile-sidebar ' + vertHoriz + ' ' + options.placement
                        + (options.windowClass ? ' ' + options.windowClass : '');
                    delete options.placement;
                    return $uibModal.open(options);
                }
            };
            // create $sidebar as extended $uibModal
            var $sidebar = angular.extend({}, $uibModal, sideFactory);
            return $sidebar;
        }]);
});