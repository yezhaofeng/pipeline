/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
/**
 * @file Popover功能封装
 *
 * @author liming16
 *
 * @see agilePopover
 */
define(['angular'], function (angular) {
    'use strict';

    var $el = angular.element;
    var $popovers = [];
    var globalId = 0;
    var isDef = angular.isDefined;
    var module = angular.module('agile.popover', []);

    module.provider('agilePopover', function () {
        var defaults = {
            angularEvent: null,
            container: 'body',
            hideOnButtonClick: true,
            hideOnInsideClick: false,
            hideOnOutsideClick: true,
            mouseRelative: '',
            onClose: angular.noop,
            onOpen: angular.noop,
            placement: 'bottom|left',
            plain: 'false',
            popupDelay: 0,
            restrictBounds: false,
            scopeEvent: null,
            template: '',
            theme: 'agile-popover-list-theme',
            timeout: 1.5,
            trigger: 'click',
            triggerPrevent: true
        };

        this.setDefaults = function (newDefaults) {
            angular.extend(defaults, newDefaults);
        };

        this.$get = function () {
            return {
                getDefaults: function () {
                    return defaults;
                }
            };
        };
    });

    module.directive('agilePopover', [
        'agilePopover',
        '$rootScope',
        '$timeout',
        '$templateCache',
        '$q',
        '$http',
        '$compile',
        '$document',
        '$parse',
        function (agilePopover,
                  $rootScope,
                  $timeout,
                  $templateCache,
                  $q,
                  $http,
                  $compile,
                  $document,
                  $parse) {
            return {
                restrict: 'A',
                scope: true,
                link: function (scope, elm, attrs) {
                    var $container;
                    var $popover;
                    var $triangle;
                    var alignP;
                    var defaults = agilePopover.getDefaults();
                    var popoverDisplayer;
                    var popoverHider;
                    var match;
                    var options = {
                        angularEvent: attrs.agilePopoverAngularEvent || defaults.angularEvent,
                        container: attrs.agilePopoverContainer || defaults.container,
                        group: attrs.agilePopoverGroup,
                        hideOnButtonClick: toBoolean(attrs.agilePopoverHideOnButtonClick
                            || defaults.hideOnButtonClick),
                        hideOnInsideClick: toBoolean(attrs.agilePopoverHideOnInsideClick
                            || defaults.hideOnInsideClick),
                        hideOnOutsideClick: toBoolean(attrs.agilePopoverHideOnOutsideClick
                            || defaults.hideOnOutsideClick),
                        mouseRelative: attrs.agilePopoverMouseRelative,
                        onClose: $parse(attrs.agilePopoverOnClose) || defaults.onClose,
                        onOpen: $parse(attrs.agilePopoverOnOpen) || defaults.onOpen,
                        placement: attrs.agilePopoverPlacement || defaults.placement,
                        plain: toBoolean(attrs.agilePopoverPlain || defaults.plain),
                        popupDelay: attrs.agilePopoverPopupDelay || defaults.popupDelay,
                        restrictBounds: Boolean(attrs.agilePopoverRestrictBounds) || defaults.restrictBounds,
                        scopeEvent: attrs.agilePopoverScopeEvent || defaults.scopeEvent,
                        template: attrs.agilePopoverTemplate || defaults.template,
                        theme: attrs.agilePopoverTheme || defaults.theme,
                        timeout: attrs.agilePopoverTimeout || defaults.timeout,
                        trigger: attrs.agilePopoverTrigger || defaults.trigger,
                        triggerPrevent: attrs.agilePopoverTriggerPrevent || defaults.triggerPrevent
                    };
                    var placementP;
                    var unregisterActivePopoverListeners;
                    var unregisterDisplayMethod;

                    if (options.mouseRelative) {
                        options.mouseRelativeX = options.mouseRelative.indexOf('x') !== -1;
                        options.mouseRelativeY = options.mouseRelative.indexOf('y') !== -1;
                    }

                    function addEventListeners() {
                        function cancel() {
                            popoverHider.cancel();
                        }

                        function hide() {
                            popoverHider.hide(options.timeout);
                        }

                        elm.on('mouseout', hide).on('mouseover', cancel);
                        $popover.on('mouseout', hide).on('mouseover', cancel);
                        unregisterActivePopoverListeners = function () {
                            elm.off('mouseout', hide).off('mouseover', cancel);
                            $popover.off('mouseout', hide).off('mouseover', cancel);
                        };
                    }


                    function adjustRect(rect, adjustX, adjustY, ev) {

                        var localRect = {
                            bottom: rect.bottom,
                            height: rect.height,
                            left: rect.left,
                            right: rect.right,
                            top: rect.top,
                            width: rect.width
                        };

                        if (adjustX) {
                            localRect.left = ev.pageX;
                            localRect.right = ev.pageX;
                            localRect.width = 0;
                        }

                        if (adjustY) {
                            localRect.top = ev.pageY;
                            localRect.bottom = ev.pageY;
                            localRect.height = 0;
                        }

                        return localRect;
                    }

                    function buttonClickHandler() {
                        if ($popover.isOpen) {
                            scope.hidePopover();
                        }
                    }

                    function display(e) {
                        if (angular.isObject(e) && false !== options.triggerPrevent) {
                            e.preventDefault();
                        }

                        popoverHider.cancel();
                        popoverDisplayer.display(options.popupDelay, e);
                    }

                    function getBoundingClientRect(elm) {
                        var w = window;
                        var doc = document.documentElement || document.body.parentNode || document.body;
                        var x = (isDef(w.pageXOffset)) ? w.pageXOffset : doc.scrollLeft;
                        var y = (isDef(w.pageYOffset)) ? w.pageYOffset : doc.scrollTop;
                        var rect = elm.getBoundingClientRect();


                        if (x || y) {
                            return {
                                bottom: rect.bottom + y,
                                left: rect.left + x,
                                right: rect.right + x,
                                top: rect.top + y,
                                height: rect.height,
                                width: rect.width
                            };
                        }
                        return rect;
                    }

                    function insideClickHandler() {
                        if ($popover.isOpen) {
                            scope.hidePopover();
                        }
                    }


                    function loadTemplate(template, plain) {
                        if (!template) {
                            return '';
                        }

                        if (angular.isString(template) && plain) {
                            return template;
                        }

                        return $templateCache.get(template) || $http.get(template, {cache: true});
                    }


                    function move(popover, placement, align, rect, triangle) {
                        var containerRect;
                        var popoverRect = getBoundingClientRect(popover[0]);
                        var popoverRight;
                        var top;
                        var left;

                        var positionX = function () {
                            if (align === 'center') {
                                return Math.round(rect.left + rect.width / 2 - popoverRect.width / 2);
                            } else if (align === 'right') {
                                return rect.right - popoverRect.width;
                            }
                            return rect.left;
                        };

                        var positionY = function () {
                            if (align === 'center') {
                                return Math.round(rect.top + rect.height / 2 - popoverRect.height / 2);
                            } else if (align === 'bottom') {
                                return rect.bottom - popoverRect.height;
                            }
                            return rect.top;
                        };

                        if (placement === 'top') {
                            top = rect.top - popoverRect.height;
                            left = positionX();
                        } else if (placement === 'right') {
                            top = positionY();
                            left = rect.right;
                        } else if (placement === 'bottom') {
                            top = rect.bottom;
                            left = positionX();
                        } else if (placement === 'left') {
                            top = positionY();
                            left = rect.left - popoverRect.width;
                        }


                        if (true === options.restrictBounds) {
                            containerRect = getBoundingClientRect($container[0]);

                            left = Math.max(containerRect.left, left);

                            popoverRight = left + popoverRect.width;
                            if (popoverRight > containerRect.width) {
                                left = left - (popoverRight - containerRect.width);
                            }
                        }

                        popover
                            .css('top', top.toString() + 'px')
                            .css('left', left.toString() + 'px');

                        if (triangle && triangle.length) {
                            if (placement === 'top' || placement === 'bottom') {
                                left = rect.left + rect.width / 2 - left;
                                triangle.css('left', left.toString() + 'px');
                            } else {
                                top = rect.top + rect.height / 2 - top;
                                triangle.css('top', top.toString() + 'px');
                            }
                        }
                    }

                    function outsideClickHandler(e) {
                        var id;
                        if ($popover.isOpen && e.target !== elm[0]) {
                            id = $popover[0].id;

                            if (!isInPopover(e.target)) {
                                scope.hidePopover();
                            }
                        }

                        function isInPopover(el) {
                            if (el.id === id) {
                                return true;
                            }

                            var parent = angular.element(el).parent()[0];

                            if (!parent) {
                                return false;
                            }

                            if (parent.id === id) {
                                return true;
                            }
                            else {
                                return isInPopover(parent);
                            }
                        }

                    }

                    function removeEventListeners() {
                        unregisterActivePopoverListeners();
                    }

                    function toBoolean(value) {
                        if (value && value.length !== 0) {
                            var v = ('' + value).toLowerCase();
                            value = (v === 'true');
                        } else {
                            value = false;
                        }
                        return value;
                    }


                    popoverDisplayer = {
                        id: undefined,


                        display: function (delay, e) {
                            // Disable popover if agile-popover value is false
                            if ($parse(attrs.agilePopover)(scope) === false) {
                                return;
                            }

                            $timeout.cancel(popoverDisplayer.id);

                            if (!isDef(delay)) {
                                delay = 0;
                            }

                            // hide any popovers being displayed
                            if (options.group) {
                                $rootScope.$broadcast('agile:popover:hide', options.group);
                            }

                            popoverDisplayer.id = $timeout(function () {
                                if (true === $popover.isOpen) {
                                    return;
                                }

                                $popover.isOpen = true;
                                $popover.css('display', 'block');

                                // position the popover accordingly to the defined placement around the
                                // |elm|.
                                var elmRect = getBoundingClientRect(elm[0]);

                                // If the mouse-relative options is specified we need to adjust the
                                // element client rect to the current mouse coordinates.
                                if (options.mouseRelative) {
                                    elmRect = adjustRect(elmRect, options.mouseRelativeX, options.mouseRelativeY, e);
                                }

                                move($popover, placementP, alignP, elmRect, $triangle);
                                addEventListeners();

                                // Hide the popover without delay on the popover click events.
                                if (true === options.hideOnInsideClick) {
                                    $popover.on('click', insideClickHandler);
                                }

                                // Hide the popover without delay on outside click events.
                                if (true === options.hideOnOutsideClick) {
                                    $document.on('click', outsideClickHandler);
                                }

                                // Hide the popover without delay on the button click events.
                                if (true === options.hideOnButtonClick) {
                                    elm.on('click', buttonClickHandler);
                                }

                                // Call the open callback
                                options.onOpen(scope);
                            }, delay * 1000);
                        },

                        cancel: function () {
                            $timeout.cancel(popoverDisplayer.id);
                        }
                    };

                    /**
                     * Responsible for hiding of popover.
                     * @type {Object}
                     */
                    popoverHider = {
                        id: undefined,

                        hide: function (delay) {
                            $timeout.cancel(popoverHider.id);

                            // do not hide if -1 is passed in.
                            if (delay !== '-1') {
                                // delay the hiding operation for 1.5s by default.
                                if (!isDef(delay)) {
                                    delay = 1.5;
                                }

                                popoverHider.id = $timeout(function () {
                                    $popover.off('click', insideClickHandler);
                                    $document.off('click', outsideClickHandler);
                                    elm.off('click', buttonClickHandler);
                                    $popover.isOpen = false;
                                    popoverDisplayer.cancel();
                                    $popover.css('display', 'none');
                                    removeEventListeners();

                                    // Call the close callback
                                    options.onClose(scope);
                                }, delay * 1000);
                            }
                        },

                        cancel: function () {
                            $timeout.cancel(popoverHider.id);
                        }
                    };


                    $container = $document.find(options.container);
                    if (!$container.length) {
                        $container = $document.find('body');
                    }

                    // Parse the desired placement and alignment values.
                    var placeReg = new RegExp(
                        [
                            '^(top|bottom|left|right)$|((top|bottom)\|',
                            '(center|left|right)+)|((left|right)\|(center|top|bottom)+)'
                        ].join('')
                    );
                    match = options
                        .placement
                        .match(placeReg)
                    ;
                    if (!match) {
                        throw new Error(
                            '"' + options.placement + '" is not a valid placement or has '
                            + 'an invalid combination of placements.'
                        );
                    }
                    placementP = match[6] || match[3] || match[1];
                    alignP = match[7] || match[4] || match[2] || 'center';

                    // Create the popover element and add it to the cached list of all
                    // popovers.
                    globalId += 1;
                    $popover = $el('<div id="agilepopover-' + globalId + '"></div>')
                        .addClass('agile-popover-' + placementP + '-placement')
                        .addClass('agile-popover-' + alignP + '-align')
                        .css('position', 'absolute')
                        .css('display', 'none')
                    ;
                    $popovers.push($popover);

                    // Allow closing the popover programatically.
                    scope.hidePopover = function () {
                        popoverHider.hide(0);
                    };

                    // Hide popovers that are associated with the passed group.
                    scope.$on('agile:popover:hide', function (ev, group) {
                        if (options.group === group) {
                            scope.hidePopover();
                        }
                    });

                    // Clean up after yourself.
                    scope.$on('$destroy', function () {
                        $popover.remove();
                        unregisterDisplayMethod();
                    });

                    // Display the popover when a message is broadcasted on the
                    // $rootScope if `angular-event` was given.
                    if (angular.isString(options.angularEvent)) {
                        unregisterDisplayMethod = $rootScope.$on(
                            options.angularEvent,
                            display
                        );

                        // Display the popover when a message is broadcasted on the
                        // scope if `scope-event` was given.
                    } else if (angular.isString(options.scopeEvent)) {
                        unregisterDisplayMethod = scope.$on(
                            options.scopeEvent,
                            display
                        );

                        // Otherwise just display the popover whenever the event that was
                        // passed to the `trigger` attribute occurs on the element.
                    } else {
                        elm.on(options.trigger, display);
                        unregisterDisplayMethod = function () {
                            elm.off(options.trigger, display);
                        };
                    }

                    // Load the template and compile the popover.
                    $q
                        .when(loadTemplate(options.template, options.plain))
                        .then(function (template) {
                            if (angular.isObject(template)) {
                                template = angular.isString(template.data) ?
                                    template.data : ''
                                ;
                            }
                            // Set the popover element HTML.
                            $popover.html(template);

                            // Add the "theme" class to the element.
                            if (options.theme) {
                                $popover.addClass(options.theme);
                            }

                            // Compile the element.
                            $compile($popover)(scope);

                            // Cache the triangle element (works in ie8+).
                            $triangle = $el(
                                $popover[0].querySelectorAll('.triangle')
                            );

                            // Append it to the DOM
                            $container.append($popover);
                        })
                    ;
                }
            };
        }


    ]);

    module.directive('agileCommitInfo', ['$compile', function ($compile) {
        return {
            restrict: 'A',
            scope: {
                template: '=',
                currentBuild: '='
            },
            link: function (scope, el, attrs) {

                scope.initDone = false;

                function initPopover() {
                    var popover = '<span agile-popover\n'
                        + 'agile-popover-template="'
                        + attrs.template
                        + '" agile-popover-trigger="mouseenter"'
                        + 'agile-popover-placement="right"'
                        + 'agile-popover-theme="agile-popover-tooltip-theme"'
                        + 'agile-popover-timeout="0.3">'
                        + el.html()
                        + '</span>';

                    var newElWithDirective = $compile(popover)(scope);
                    el.empty();
                    el.append(newElWithDirective);
                    scope.initDone = true;
                }

                el.mouseenter(function () {
                    if (!scope.initDone) {
                        initPopover();
                    }
                });

            }
        };

    }]);

    module.directive('agileStageJobBuildInfo', ['$compile', function ($compile) {
        return {
            restrict: 'A',
            scope: {
                template: '=',
                stageBuild: '=',
                currentBuild: '=',
                context: '=appContext'
            },
            link: function (scope, el, attrs) {

                scope.initDone = false;

                scope.generateContext4RealJob = function (jobBuildId) {
                    return angular.extend({}, scope.context, {
                        jobBuildId: jobBuildId,
                        pipelineBuildId: scope.currentBuild.pipelineBuildBean.id
                    });
                };

                function initPopover() {
                    var popover = '<span agile-popover\n'
                        + 'agile-popover-template="' + attrs.template + '" agile-popover-trigger="mouseenter"'
                        + 'agile-popover-placement="bottom"'
                        + 'agile-popover-theme="agile-popover-tooltip-theme"'
                        + 'agile-popover-timeout="0.3">'
                        + el.html()
                        + '</span>';

                    var newElWithDirective = $compile(popover)(scope);
                    el.empty();
                    el.append(newElWithDirective);
                    scope.initDone = true;
                }

                el.mouseenter(function () {
                    if (!scope.initDone) {
                        initPopover();
                    }
                });

            }
        };

    }]);









});

