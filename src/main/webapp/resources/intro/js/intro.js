var Agileplus = Agileplus || {};
$(function () {
    Agileplus.introPage.fullPage();
    $(window).resize();
});

Agileplus.introPage = {
    moduleSearch: function () {
        var module = $("#module-search").val();
        var url =  '/#/builds/' + module;
        window.location.href = url;
    },
    fullPage: function () {
        $('#fullpage').fullpage({
            anchors: ['1stPage', '2ndPage', '3rdPage', '4thPage', 'lastPage'],
            navigation: true,
            navigationTooltips: ['持续交付', '简单', '灵活', '可依赖', '立即体验'],
            sectionsColor: ['#0F1E43', '#0F1E43', '#0F1E43', '#0F1E43', '#0F1E43'],
            keyboardScrolling: false,
            afterLoad: function (anchorLink, index) {
                var searchWrapper = $('#search-wrapper');
                var currentClass = searchWrapper.attr('class');
                // using index
                if ((index === 2 || index === 3 || index === 4) && currentClass !== 'search-wrapper-top-right') {
                    searchWrapper.fadeOut(200).switchClass(currentClass, 'search-wrapper-top-right').fadeIn(200);
                } else if (index === 1 && currentClass !== 'search-wrapper-top') {
                    searchWrapper.fadeOut(200).switchClass(currentClass, 'search-wrapper-top').fadeIn(200);
                } else if (index === 5 && currentClass !== 'search-wrapper-mid') {
                    searchWrapper.fadeOut(200).switchClass(currentClass, 'search-wrapper-mid').fadeIn(200);
                }
            }
        });
    },
    upgradeDialog: function () {
        var dialogOpts = {
            minWidth: 400,
            modal: true,
            resizable: false,
            draggable: false
        };
        $('#dialog').dialog(dialogOpts);
        $('.ui-dialog-titlebar').hide();
        $('.ui-dialog-buttonpane').hide();
    },
    closeDialog: function () {
        $('#dialog').dialog('close');
    }
};
