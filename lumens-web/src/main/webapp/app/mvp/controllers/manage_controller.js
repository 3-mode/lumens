/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers
.controller("ManageViewCtrl", function ($scope, $route, $compile, JobList, SyncGet) {
    LumensLog.log("in ManageViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    Lumens.system.switchTo(Lumens.system.AngularJSView);
    $scope.i18n = Lumens.i18n;
    $scope.manage_side_bar_template = "app/templates/manage/manage_side_bar_tmpl.html";
    $scope.job_bar_template = "app/templates/manage/job_command_tmpl.html";
    $scope.job_config_template = "app/templates/manage/job_config_tmpl.html";
    $scope.job_create_template = "app/templates/manage/job_create_modal_tmpl.html";
    $scope.job_function_tool_template = "app/templates/manage/job_function_tool_tmpl.html";
    $scope.jobListHolder = new Lumens.List($("#jobList"));
    $scope.job_config = SyncGet.get("app/config/json/job_config.json", "text/json");
    // Job command bar function
    $scope.onCommand = function (id_btn) {
        LumensLog.log("in Job onCommand");
        $scope.job = {name: "", description: ""};
    };
    // Create job function
    $scope.createJob = function () {
        LumensLog.log("in createJob");
        LumensLog.log("Job", $scope.job);
        $scope.job.id = $.currentTime();
        $scope.jobListHolder.addItem({
            IdList: [
                $scope.job.id
            ],
            titleList: [
                $scope.job.name + " [" + $scope.job.id + "]"
            ],
            buildContent: function (itemContent) {
                itemContent.append($compile('<div ng-include="job_config_template" />')($scope));
            }
        }, 0, true);
    };
    JobList.get(function (result) {
        LumensLog.log(result);
        for (var i = 0; i < result.jobs.length; ++i) {
            $scope.jobListHolder.addItem({
                IdList: [
                    result.jobs[i].id
                ],
                titleList: [
                    result.jobs[i].name + " [" + result.jobs[i].id + "]"
                ],
                buildContent: function (itemContent) {
                    itemContent.append($compile('<div ng-include="job_config_template" />')($scope));
                }
            }, i);
        }
    });
});
