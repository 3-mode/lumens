/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers.controller("ManageViewCtrl", function ($scope, $route, $http, $compile) {
    LumensLog.log("in ManageViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    if (Lumens.system.designView.workspaceLayout) {
        Lumens.system.designView.workspaceLayout.remove();
        Lumens.system.designView.workspaceLayout = null;
    }
});
