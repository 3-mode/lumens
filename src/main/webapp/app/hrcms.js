$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    var SysModuleID = Hrcms.SysModuleID;
    Hrcms.create = function(containerObj) {
        var tThis = {};
        var rootContainer = containerObj;
        var headerContainer;
        var moduleViewMap = {};
        rootContainer.addClass("hrcms");
        var rootLayout = Hrcms.RootLayout.create(rootContainer);
        var theLayout = Hrcms.SplitLayout.create(rootLayout);
        theLayout.configure({
            mode: "vertical",
            part1Size: 86
        });
        function layoutHeader(parentContainer) {
            return $('<div class="hrcms-header-constainer"/>').appendTo(parentContainer);
        }
        tThis.load = function() {
            if (Hrcms.debugEnabled)
                console.log("Loading HRCMS !");
            headerContainer = layoutHeader(theLayout.getPart1Element());
            /** Header begin */
            Hrcms.Header.create(headerContainer).setSysTitle(I18N.SystemTitle);
            // TODO use ajax to load the toolbar button strings
            var sysToolbar = Hrcms.SysToolbar.create(headerContainer);
            sysToolbar.configure(Hrcms.SysToolbar_Config);
            //-------------- Sys toolbar configuration Begine --------------------------------------
            sysToolbar.onButtonClick(function(event) {
                var curSysModuleID = event.moduleID;
                if (Hrcms.debugEnabled) {
                    console.log(curSysModuleID);
                }
                // Remove the old view
                if (moduleViewMap.activeView)
                    moduleViewMap.activeView.remove();
                // Switch to a new view
                if (SysModuleID.SysToolbar_Home === curSysModuleID) {
                    moduleViewMap.activeView = Hrcms.HomeView.create(theLayout.getPart2Element());
                }
                else if (SysModuleID.SysToolbar_Information === curSysModuleID) {
                    moduleViewMap.activeView = Hrcms.InfoManageView.create(theLayout.getPart2Element());
                }
                else if (SysModuleID.SysToolbar_Statistic_Analysis === curSysModuleID) {
                    moduleViewMap.activeView = Hrcms.StatisticsView.create(theLayout.getPart2Element());
                }
                else if (SysModuleID.SysToolbar_ApproveProgress === curSysModuleID) {
                    // TODO
                }
                else if (SysModuleID.SysToolbar_MessageCenter === curSysModuleID) {
                    // TODO
                }
                else if (SysModuleID.SysToolbar_SystemManage === curSysModuleID) {
                    moduleViewMap.activeView = Hrcms.SystemView.create(theLayout.getPart2Element());
                }
            });
            //-------------- Sys toolbar configuration End------------------------------------------
            // Go to the default home view
            sysToolbar.activeButton(SysModuleID.SysToolbar_Home);
            // TODO Begin page workspace building here, default is home page
            return this;
        }
        // end
        return tThis;
    }
});