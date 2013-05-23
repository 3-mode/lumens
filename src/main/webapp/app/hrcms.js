$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
   
    Hrcms.create = function(containerObj) {
        var tThis = {};
        var rootContainer = containerObj;
        var headerContainer = null;
        rootContainer.addClass("hrcms");
        function layoutHeader(parentContainer) {
            return $('<div class="hrcms-header-constainer"/>').appendTo(parentContainer);
        }
        tThis.load = function() {
            console.log("Loading HRCMS !");
            headerContainer = layoutHeader(rootContainer);
            /** Header begin */
            Hrcms.Header.create(headerContainer).setSysTitle(I18N.SystemTitle);
            function clickToolbarButton(event) {
                console.log($(this).find('span').html());
                // TODO switch to another view here
            }
            // TODO use ajax to load the toolbar button strings
            var toolbar = Hrcms.Toolbar.create(headerContainer);
            Hrcms.SysToolbar_Config.event_callback = clickToolbarButton;
            toolbar.configure(Hrcms.SysToolbar_Config);
            // TODO Begin page workspace building here, default is home page
            var view = Hrcms.ContentView.create(rootContainer);
        }
        // end
        return tThis;
    }

    Hrcms.DataNavBar = {};
    Hrcms.DataNavBar.create = function(args) {
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
        tThis.configButtons = function(buttons) {
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
});