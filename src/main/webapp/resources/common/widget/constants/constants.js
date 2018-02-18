/**
 * @file 
 */
define([], function () {
    'use strict';
    var formatUrl = function (url) {
        return url;
    };
    var exports = {
        api: function (path) {
            return formatUrl(path);
        },
        resource: function (path) {
            return formatUrl("resources/dev/tpl/" + path);
        },
        trunk:function(){
            return "TRUNK"
        },
        branch:function(){
            return "BRANCH"
        }
    };
    
    return exports;
});
