$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.SystemView = {};
    Hrcms.SystemView.create = function(container) {
        var tThis = Hrcms.ContentView.create(container);
        tThis.loadLeftNavMenu = function(menu) {
            menu.configure(Hrcms.NavMenu_SystemManage_Config);
            menu.onItemClick(function(event) {
                if (Hrcms.debugEnabled)
                    console.log(event);
            });
        }

        tThis.initialize();
        // end
        return tThis;
    }
});
