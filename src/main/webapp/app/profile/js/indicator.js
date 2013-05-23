$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.NavIndicator = {};
    Hrcms.NavIndicator.create = function(args) {
        var tThis = {};
        var container = args.container;
        var workspaceHeader = $('<div class="hrcms-workspace-header"/>').appendTo(container);
        var headerNav = $('<div class="hrcms-workspace-nav-padding"/>').appendTo(workspaceHeader);
        var workspaceToolbar = $('<div class="hrcms-workspace-toolbar"/>').appendTo(workspaceHeader);
        var nav = $('<div class="hrcms-workspace-nav"><span class="hrcms-workspace-nav-current">'
        + args.title + '</span></div>').appendTo(headerNav);
        tThis.goTo = function(text) {
            var last = nav.find('span').last();
            last.toggleClass('hrcms-workspace-nav-back');
            last.toggleClass('hrcms-workspace-nav-current');
            last.on('click', function(event) {
                tThis.goBack();
            });
            nav.append('<span style="padding-left: 4px; padding-right:4px;">/</span><span class="hrcms-workspace-nav-current">'
            + text + '</span>');
        }
        tThis.goBack = function() {
            var canGoBack = nav.find('.hrcms-workspace-nav-back');
            if (canGoBack.length === 0)
                return;
            nav.find('span').last().remove();
            nav.find('span').last().remove();
            var last = nav.find('span').last();
            last.toggleClass('hrcms-workspace-nav-back');
            last.toggleClass('hrcms-workspace-nav-current');
        }
        tThis.configure = function(buttons) {
            for (var i = 0; i < buttons.length; ++i) {
                var btn = $('<button class="hrcms-button"></button>')
                .appendTo($('<div style="margin-left:15px;"/>')
                .appendTo(workspaceToolbar));
                btn.button();
                btn.text(buttons[i].title);
                btn.on("click", buttons[i].click);
            }
        }
        // end
        return tThis;
    }
})