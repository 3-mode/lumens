/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers
.controller("ManageViewCtrl", function ($scope, $route, $compile, JobList, SyncGet, ProjectList) {
    LumensLog.log("in ManageViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    Lumens.system.switchTo(Lumens.system.AngularJSView);
    $scope.i18n = Lumens.i18n;
    $scope.manage_side_bar_template = "app/templates/manage/manage_side_bar_tmpl.html";
    $scope.job_bar_template = "app/templates/manage/job_command_tmpl.html";
    $scope.job_list_template = "app/templates/manage/job_list_tmpl.html";
    $scope.job_config_template = "app/templates/manage/job_config_tmpl.html";
    $scope.job_create_template = "app/templates/manage/job_create_modal_tmpl.html";
    $scope.job_function_tool_template = "app/templates/manage/job_function_tool_tmpl.html";
    $scope.job_add_proj_manage_template = "app/templates/manage/job_add_proj_manage_tmpl.html";
    $scope.jobListHolder = new Lumens.List($("#jobList"));
    $scope.job_config = SyncGet.get("app/config/json/job_config.json", "text/json");
    $scope.job = {};
    var jobManagementHolder = $("#jobManagementHolder");
    $(window).resize(function (e) {
        if (e && e.target !== this)
            return;
        jobManagementHolder.trigger("resize");
    })
    $scope.jobManagePanel = new Lumens.ResizableVSplitLayoutExt(jobManagementHolder)
    .configure({
        mode: "vertical",
        useRatio: true,
        part1Size: "40%"
    });
    $scope.jobManagePanel.getPart1Element().append($compile('<div ng-include="job_list_template" class="lumens-scroll-panel"></div>')($scope));
    $scope.jobManagePanel.getPart2Element().append($compile('<div ng-include="job_config_template" class="lumens-scroll-panel"></div>')($scope));
    // Job command bar function
    $scope.onCommand = function (id_btn) {
        LumensLog.log("in Job onCommand");
    };
    $scope.openJobDetail = function (index, job_item, evt) {
        $scope.selectJobIndex = index;
        LumensLog.log("Event", index, job_item, evt);
        $scope.job = job_item;
    };
    $scope.selectProjItem = function (index) {
        $scope.selectProjIndex = index;
    };
    $scope.selectAddedProject = function (index) {
        $scope.selectAddedProjIndex = index;
    };
    $scope.AddManageProject = function () {
        ProjectList.get(function (projectsResponse) {
            $scope.projects = projectsResponse.content.project;
        });
    };
    $scope.addProject = function () {
        $("#projectListAddModal").find("#projectLoading").show();
    }
    // Create job function
    $scope.createJob = function () {
        LumensLog.log("in createJob");
        LumensLog.log("Job", $scope.job);
        $scope.job.id = $.currentTime();
        $scope.job = {};
    };
    JobList.get(function (result) {
        $scope.jobs = result.jobs;
        LumensLog.log(result);
    });
})
.controller("JobConfigInitCtrl", function ($scope) {
    $scope.jobManagePanel.getPart2Element().find('.form_time')
    .datetimepicker({
        container: $scope.jobManagePanel.getPart2Element().find("#jobCofnig"),
        language: 'en',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 1,
        minView: 0,
        maxView: 1,
        forceParse: 0,
        pickerPosition: "bottom-left"
    });
});
