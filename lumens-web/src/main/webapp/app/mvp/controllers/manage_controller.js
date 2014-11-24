/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers
.controller("ManageViewCtrl", function ($scope, $route, SyncGet, ManageNavMenu) {
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
    ManageNavMenu.get(function (menu) {
        var manageMavMenu = $("#manageNavMenu");
        $(window).resize(function (e) {
            if (e && e.target !== this)
                return;
            manageMavMenu.trigger("resize");
        })
        $scope.navMenu = new Lumens.NavMenu({
            container: manageMavMenu,
            width: "100%",
            height: "auto",
            item_selected: true
        }).configure(menu)
        .onItemClick(function (evt) {
            LumensLog.log(evt);
            $scope.$apply(function () {
                $scope.manageSelection = evt.module_id;
            })
        });
    })
})
.controller("JobManagementCtrl", function ($scope, $compile, JobList, ProjectList) {
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
})
.controller("ServerMonitorCtrl", function ($scope, CpuPerc, CpuCount) {
    CpuCount.get(function (result) {
        $scope.cpuCount = []
        for (var i = 0; i < result.cpu_count; ++i)
            $scope.cpuCount.push(i);
        var plotList = [];
        CpuPerc.get(function (cpu_perc) {
            for (var i = 0; i < result.cpu_count; ++i) {
                var data = [
                    ['Idle', cpu_perc.cpu_perc_list[i].idle],
                    ['System', cpu_perc.cpu_perc_list[i].sys],
                    ['User', cpu_perc.cpu_perc_list[i].user]
                ];
                var plot = $.jqplot('cpuInfo_' + i, [data], {
                    seriesDefaults: {
                        // make this a donut chart.
                        renderer: $.jqplot.DonutRenderer,
                        rendererOptions: {
                            // Donut's can be cut into slices like pies.
                            sliceMargin: 6,
                            // Pies and donuts can start at any arbitrary angle.
                            startAngle: -90,
                            showDataLabels: true,
                            // By default, data labels show the percentage of the donut/pie.
                            // You can show the data 'value' or data 'label' instead.
                            dataLabels: 'value'
                        }
                    },
                    legend: {show: true, location: 'e', xoffset: 25, yoffset: 25, border: 'none'}
                });
                plotList.push(plot);
            }

            $('#timer').timer({
                duration: '2s',
                repeat: true, //repeatedly calls the callback you specify
                callback: function () {
                    CpuPerc.get(function (cpu_perc) {
                        for (var i = 0; i < result.cpu_count; ++i) {
                            plotList[i].series[0].data = [
                                ['Idle', cpu_perc.cpu_perc_list[i].idle],
                                ['System', cpu_perc.cpu_perc_list[i].sys],
                                ['User', cpu_perc.cpu_perc_list[i].user]];
                            plotList[i].redraw();
                        }
                    });
                }
            });
        });
    })

});
