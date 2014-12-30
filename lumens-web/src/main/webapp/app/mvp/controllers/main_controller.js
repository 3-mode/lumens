/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers.controller("MainViewCtrl", function ($scope, $route, $http, $compile) {
    // Set the default page view as dashboard view
    Lumens.system.rootLayout = new Lumens.RootLayout($('#id-main-view')).configure();
    Lumens.system.theLayout = new Lumens.SplitLayout(Lumens.system.rootLayout).configure({
        mode: "vertical",
        part1Size: 52
    });
    Lumens.system.sysHeader = new Lumens.Header(Lumens.system.theLayout.getPart1Element()).setSysTitle("LUMENS");
    Lumens.system.navToolbar = new Lumens.NavToolbar(Lumens.system.sysHeader.getElement()).configure(Lumens.SysToolbar_Config);
    Lumens.system.theLayout.getPart2Element().append($compile('<div ng-view id="theWorkspaceContainer" style="width: 100%;"/>')($scope));
});
