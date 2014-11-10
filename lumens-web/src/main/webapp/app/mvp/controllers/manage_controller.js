/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers
.controller("ManageViewCtrl", function ($scope, $route, $http, $compile) {
    LumensLog.log("in ManageViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    Lumens.system.switchTo(Lumens.system.AngularJSView);
    $scope.i18n = Lumens.i18n;
    $scope.job_bar_template = "app/templates/manage/job_command_tmpl.html";
    $scope.job_create_template = "app/templates/manage/job_create_modal_tmpl.html";
    // Job command bar function
    $scope.onCommand = function (id_btn) {
        LumensLog.log("in Job onCommand");
        $scope.job = {Name: "", Description: ""};
    };
    // Create job function
    $scope.createJob = function () {
        LumensLog.log("in createJob");
        LumensLog.log("Job", $scope.job);
    };

    /*
     $scope.job_list = [
     {
     Name: "Test1",
     Description: "This is a Test1"
     },
     {
     Name: "Test2",
     Description: "This is a Test2"
     },
     {
     Name: "Test3",
     Description: "This is a Test3"
     },
     {
     Name: "Test4",
     Description: "This is a Test4"
     }
     ];
     $scope.jobListHolder = new Lumens.List($("#jobList")).configure({
     IdList: [
     $scope.job_list[0].Name,
     $scope.job_list[1].Name,
     $scope.job_list[2].Name
     ],
     titleList: [
     $scope.job_list[0].Name,
     $scope.job_list[1].Name,
     $scope.job_list[2].Name
     ],
     buildContent: function (itemContent, isExpand, title) {
     if (isExpand) {
     var itemID = title.attr("id");
     if (itemID === $scope.job_list[0].Name) {
     itemContent.append($scope.job_list[0].Description);
     }
     else if (itemID === $scope.job_list[1].Name) {
     itemContent.append($scope.job_list[1].Description);
     }
     else if (itemID === $scope.job_list[2].Name) {
     itemContent.append($scope.job_list[2].Description);
     }
     }
     }
     }); //*/
});
