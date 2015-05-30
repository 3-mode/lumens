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
    $scope.job_log_bar_template = "app/templates/manage/job_log_bar_tmpl.html";
    $scope.job_list_log_template = "app/templates/manage/job_log_list_tmpl.html";
    $scope.job_create_template = "app/templates/manage/job_modal_tmpl.html";
    $scope.job_function_tool_template = "app/templates/manage/job_function_tool_tmpl.html";
    $scope.job_add_proj_manage_template = "app/templates/manage/job_add_proj_manage_tmpl.html";
    $scope.jobListHolder = new Lumens.List($("#jobList"));
    $scope.job_config = SyncGet.get("app/config/json/job_config.json", "application/json");
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
.controller("JobManagementCtrl", function ($scope, $compile, TemplateService, JobService, LogFileService, ProjectList, Notifier) {
    if (sessionStorage.local_log_storage) {
        var local_log_storage = angular.fromJson(sessionStorage.local_log_storage);
        $scope.currentLogType = local_log_storage.current_log_type;
    }
    else {
        $scope.currentLogType = "id_server_log";
    }
    if (sessionStorage.local_select_job_index_storage)
        $scope.selectJobIndex = angular.fromJson(sessionStorage.local_select_job_index_storage).current_job_index;
    else
        $scope.selectJobIndex = 0;

    if (sessionStorage.local_log_count_storate)
        $scope.logItemCount = 0 + angular.fromJson(sessionStorage.local_log_count_storate).log_count;
    else
        $scope.logItemCount = 50;

    $scope.$watch('logItemCount', function (newValue, oldValue) {
        console.log("newValue: " + newValue + "; oldValue: " + oldValue);
    });

    function buildQueryLogParam() {
        console.log("Log count: ", $scope.logItemCount);
        if ($scope.currentLogType === "id_job_log" && $scope.selectJobIndex >= 0 && $scope.selectJobIndex < $scope.jobs.length)
            return {
                job_id: $scope.jobs[$scope.selectJobIndex].id, count: $scope.logItemCount
            };
        else
            return {
                count: $scope.logItemCount
            };
    }
    var jobManagementHolder = $("#jobManagementHolder");
    $(window).resize(function (e) {
        if (e && e.target !== this)
            return;
        jobManagementHolder.trigger("resize");
    });
    $scope.jobManagePanel = new Lumens.ResizableVSplitLayoutExt(jobManagementHolder)
    .configure({
        mode: "vertical",
        useRatio: true,
        part1Size: "40%"
    });
    $scope.jobManagePanel.getPart1Element().append($compile('<div ng-include="job_list_template" class="lumens-scroll-panel"></div>')($scope));
    $scope.jobLogBar = new Lumens.SplitLayout($scope.jobManagePanel.getPart2Element()).configure({
        mode: "vertical",
        part1Size: 48
    });
    $scope.jobLogBar.getPart1Element().append($compile(TemplateService.get($scope.job_log_bar_template))($scope));
    $scope.jobLogBar.getPart2Element().append($compile('<div ng-include="job_list_log_template" style="overflow: auto; position: relative; width: 100%; height: 100%;"></div>')($scope));
    JobService.list(function (result) {
        if (result.status === 'OK') {
            $("#jobLoading").hide();
            $scope.jobs = result.result_content.jobs;
            LumensLog.log("JobList:", result);
        } else {
            $("#jobLoading").hide();
            Notifier.message("error", "Error", "Failed to get jobs '" + result.error_message + "'");
        }
        LogFileService.log(buildQueryLogParam(), function (result) {
            $scope.jobLogContent = null;
            if (result.status === 'OK') {
                $("#jobLogLoading").hide();
                $scope.jobLogContent = result.result_content;
            } else {
                Notifier.message("error", "Error", "Start the job '" + result.error_message + "'");
                $("#jobLogLoading").hide();
            }
        }, function (error) {
            Notifier.message("error", "Error", error);
            $("#jobLogLoading").hide();
        });
    });
    // Job command bar function
    $scope.onCommand = function (id_btn) {
        LumensLog.log("in Job onCommand");
        if ('id_new' === id_btn) {
            $scope.job = {};
        }
        else if ('id_delete' === id_btn) {
            var job = $scope.jobs[$scope.selectJobIndex];
            JobService.delete({id: job.id}, function (result) {
                if (result.status === 'OK') {
                    $scope.jobs.splice($scope.selectJobIndex, 1);
                    Notifier.message("info", "Success", "Deleted the job");
                }
                else
                    Notifier.message("error", "Error", "Failed to delete the job '" + result.error_message + "'");
            }, function (error) {
                Notifier.message("error", "Error", error);
            });
        }
        else if ('id_edit' === id_btn) {
            if ($scope.selectJobIndex >= 0 && $scope.jobs && $scope.jobs.length > 0)
                $scope.job = $scope.jobs[$scope.selectJobIndex];
        }
        else if ('id_start' === id_btn) {
            var job = $scope.jobs[$scope.selectJobIndex];
            if (job && job.id) {
                JobService.exec({id: job.id, action: "start"}, function (result) {
                    if (result.status === 'OK')
                        Notifier.message("info", "Success", "Start the job '" + result.result_content.do + "'");
                    else
                        Notifier.message("error", "Error", "Start the job '" + result.error_message + "'");
                }, function (error) {
                    Notifier.message("error", "Error", error);
                });
            }
        }
        else if ('id_stop' === id_btn) {
            var job = $scope.jobs[$scope.selectJobIndex];
            if (job && job.id) {
                JobService.exec({id: job.id, action: "stop"}, function (result) {
                    if (result.status === 'OK')
                        Notifier.message("info", "Success", "Stop the job '" + result.result_content.do + "'");
                    else
                        Notifier.message("error", "Error", "Start the job '" + result.error_message + "'");
                }, function (error) {
                    Notifier.message("error", "Error", error);
                });
            }
        }
        else if ("id_exec_log_refresh" === id_btn) {
            $scope.jobLogContent = null;
            $("#jobLogLoading").show();
            LogFileService.log(buildQueryLogParam(), function (result) {
                if (result.status === 'OK') {
                    $("#jobLogLoading").hide();
                    $scope.jobLogContent = result.result_content;
                } else {
                    Notifier.message("error", "Error", "Start the job '" + result.error_message + "'");
                    $("#jobLogLoading").hide();
                }
            }, function (error) {
                Notifier.message("error", "Error", error);
                $("#jobLogLoading").hide();
            });
            sessionStorage.local_log_count_storate = angular.toJson({log_count: $scope.logItemCount});
        }
        else if ("id_server_log" === id_btn) {
            $scope.currentLogType = id_btn;
            sessionStorage.local_log_storage = angular.toJson({current_log_type: $scope.currentLogType});
            Notifier.message("info", "Success", "Server Log display is used");
            $scope.onCommand("id_exec_log_refresh");
        }
        else if ("id_job_log" === id_btn) {
            if ($scope.selectJobIndex >= 0) {
                $scope.currentLogType = id_btn;
                sessionStorage.local_log_storage = angular.toJson({current_log_type: $scope.currentLogType});
                Notifier.message("info", "Success", "Job Log display is used");
                $scope.onCommand("id_exec_log_refresh");
            }
            else {
                delete $scope.jobLogContent;
                Notifier.message("warn", "Warning", "No job is selected !");
            }
        }
    };
    $scope.selectJob = function (index) {
        $scope.selectJobIndex = index;
        sessionStorage.local_select_job_index_storage = angular.toJson({current_job_index: $scope.selectJobIndex});
        console.log("In select Job", index);
    };
    $scope.selectProjItem = function (index) {
        $scope.selectProjIndex = index;
    };
    $scope.selectAddedProject = function (index) {
        $scope.selectAddedProjIndex = index;
    };
    $scope.listProject = function () {
        ProjectList.get(function (projectsResponse) {
            $scope.projects = projectsResponse.content.project;
        });
    };
    $scope.addProject = function () {
        console.log("added project into job");
        if ($scope.job && !$scope.job.projects)
            $scope.job.projects = [];
        var project = $scope.projects[$scope.selectProjIndex];
        $scope.job.projects.push({
            "project_id": project.id,
            "project_name": project.name,
            "project_description": project.description});
    }
    $scope.saveJob = function () {
        console.log("Saved job: ", {content: $scope.job});
        if ($scope.job && $scope.job.id) {
            JobService.update({id: $scope.job.id}, {content: $scope.job}, function (result) {
                if (result.status === 'OK')
                    Notifier.message("info", "Success", "Save the job '" + $scope.job.name + "'");
            });
        }
        else if ($scope.job) {
            $scope.job.id = $.currentTime();
            if (!$scope.jobs)
                $scope.jobs = [];
            $scope.jobs.push($scope.job);
            JobService.save({content: $scope.job}, function (result) {
                if (result.status === 'OK')
                    Notifier.message("info", "Success", "Save the job '" + $scope.job.name + "'");
            });
        }
    };
});
