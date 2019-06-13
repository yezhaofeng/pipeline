/**
 * Created by Wonpang New on 2019/9/10.
 */
// requirejs的配置文件
require.config ({
    // baseUrl:'', // 模块查找的根目录，默认情况与data-main所赋值处于同一目录
    // 指定配置模块的加载路径
    // online环境下，要去掉，太慢了
    // urlArgs:'v='+new Date().getTime(),  // 加载的时候带的参数
    paths: {    // 模块名字：模块路径
        'app': 'startup/app',
        'router': 'startup/router',
        'start': 'startup/start',
        'ctrls': 'startup/controllers',
        'servs': 'startup/services',
        'dirs': 'startup/directives',
        'require-css': '../common/lib/require-css/css',
        'jquery': '../common/lib/jquery/js/jquery.min',
        'angular': '../common/lib/angular/js/angular.min',
        'angular-ui-router': '../common/lib/angular-ui-router/js/angular-ui-router',
        'angular-bindonce': '../common/lib/angular-bindonce/js/bindonce',
        'angular-animate': '../common/lib/angular-animate/js/angular-animate.min',
        'angular-cookies': '../common/lib/angular-cookies/js/angular-cookies.min',
        'angular-resource': '../common/lib/angular-resource/js/angular-resource.min',
        'angular-sanitize': '../common/lib/angular-sanitize/js/angular-sanitize.min',
        'angular-bootstrap': '../common/lib/angular-bootstrap/js/ui-bootstrap-tpls',
        'ng-tags-input': '../common/lib/ng-tags-input/js/ng-tags-input',
        'constants': '../common/widget/constants/constants',

        'codemirror': '../common/lib/codemirror/js/codemirror',
        'clipboard': '../common/lib/clipboard/js/clipboard.min',
        'agile-sidebar': '../common/widget/sidebar/js/agile-sidebar',
        'agile-navbar': '../common/widget/navbar/js/agile-navbar',
        'agile-clipboard': '../common/widget/clipboard/js/agile-clipboard',
        'agile-popover': '../common/widget/popover/js/agile-popover',
        'agile-codemirror': '../common/widget/realtimelog/js/agile-codemirror',
        'bcloud-mode': '../common/widget/realtimelog/mode/bcloud-mode',
        'agile-loading': '../common/widget/loader/js/agile-loading',
        'agile-dimmer': '../common/widget/dimmer/js/agile-dimmer',
        'app-plugin-job': 'plugin/job/app',
        'init-plugin-job': 'plugin/job/app-init'
    },
    map: {  //
        '*': {
            css: 'require-css'
        }
    },
    shim: { // 定义不支持AMD的模块和插件
        'angular': {
            deps: ['jquery'],   // 依赖的模块
            exports: 'angular'  // 全局变量作为模块名字
        },
        'angular-route': {
            deps: ['angular'],
            exports: 'angularRoute'
        },
        'angular-animate': {
            deps: ['angular'],
            exports: 'angularAnimate'
        },
        'angular-resource': {
            deps: ['angular'],
            exports: 'angularResource'
        },
        'angular-cookies': {
            deps: ['angular'],
            exports: 'angularCookies'
        },
        'angular-sanitize': {
            deps: ['angular'],
            exports: 'angularSanitize'
        },
        'angular-ui-router': {
            deps: ['angular'],
            exports: 'angularUIRouter'
        },
        'angular-bindonce': {
            deps: ['angular'],
            exports: 'angularBindonce'
        },
        'angular-bootstrap': {
            deps: ['angular'],
            exports: 'angularBootstrap'
        },
        'ng-tags-input': {
            deps: ['angular'],
            exports: 'ngTagsInput'
        }
    },
    deps: ['start', 'require-css']
});
