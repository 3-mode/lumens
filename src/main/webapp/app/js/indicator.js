$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.NavIndicator = {};
    Hrcms.NavIndicator.create = function(containerObj) {
        var tThis = {};
        var container = containerObj;
        var workspaceHeader = $('<div class="hrcms-workspace-header"/>').appendTo(container);
        var headerNav = $('<div class="hrcms-workspace-nav-padding"/>').appendTo(workspaceHeader);
        var nav = $('<div class="hrcms-workspace-nav"></div>').appendTo(headerNav);
        var configuration;
        tThis.toggle = function() {
            workspaceHeader.toggle();
        }
        tThis.remove = function() {
            workspaceHeader.remove();
        }
        tThis.size = function() {
            return {
                height: workspaceHeader.height(),
                width: workspaceHeader.width()
            };
        }
        // Methods
        tThis.goTo = function(text) {
            var span = nav.find('span');
            if (span.length === 0) {
                nav.append('<span class="hrcms-workspace-nav-current">' + text + '</span>');
            }
            else {
                var last = span.last();
                last.toggleClass('hrcms-workspace-nav-back');
                last.toggleClass('hrcms-workspace-nav-current');
                last.on('click', function(event) {
                    tThis.goBack();
                });
                nav.append('<span style="padding-left: 4px; padding-right:4px;">/</span><span class="hrcms-workspace-nav-current">'
                + text + '</span>');
            }
            return this;
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
            if (configuration && configuration.goBack)
                configuration.goBack(last);
            return this;
        }
        tThis.configure = function(config) {
            configuration = config;
            if (config.toolbar && config.toolbar.barType) {
                var workspaceToolbar = $('<div class="hrcms-workspace-toolbar"/>').appendTo(workspaceHeader);
                var bar = config.toolbar.barType.create({container: workspaceToolbar});
                bar.configure(config.toolbar);
            }
            return this;
        }
        // end
        return tThis;
    }
});