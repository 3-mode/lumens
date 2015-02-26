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
    $scope.job_create_template = "app/templates/manage/job_modal_tmpl.html";
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
.controller("JobManagementCtrl", function ($scope, $compile, JobService, ProjectList, Notifier) {
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
    //$scope.jobManagePanel.getPart2Element().append($compile('<div ng-include="job_config_template" class="lumens-scroll-panel"></div>')($scope));
    // Job command bar function
    $scope.onCommand = function (id_btn) {
        LumensLog.log("in Job onCommand");
        if ('id_new' === id_btn) {
            $scope.job = {};
        }
        else if ('id_edit' === id_btn) {
            if ($scope.selectJobIndex >= 0 && $scope.jobs && $scope.jobs.length > 0)
                $scope.job = $scope.jobs[$scope.selectJobIndex];
        }
    };
    $scope.openJobDetail = function (index, job_item, evt) {
        $scope.selectJobIndex = index;
        LumensLog.log("Event", index, job_item, evt);
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
    JobService.list(function (result) {
        $scope.jobs = result.content.jobs;
        LumensLog.log("JobList:", result);
    });
})
.controller("ServerMonitorCtrl", function ($scope, CpuPerc, CpuCount, MemPerc, Disk) {

    // Line charts of CPU
    function getRedrawData(historyData) {
        var data = [];
        for (var i = 0; i < historyData.length; ++i)
            data.push([i + 1, historyData[i]]);
        return data;
    }

    function putInfoIntoCache(history, value) {
        for (var i = 0; i < (history.length - 1); ++i)
            history[i] = history[i + 1];
        history[i] = value;
    }
    // { "combined"  : 6,  "sys" : 3,  "user" : 3,  "idle" : 94  },{ "combined"  : 0,  "sys" : 0,  "user" : 0,  "idle" : 100  },{ "combined"  : 3,  "sys" : 0,  "user" : 3,  "idle" : 97  },{ "combined"  : 0,  "sys" : 0,  "user" : 0,  "idle" : 100  },{ "combined"  : 28,  "sys" : 16,  "user" : 12,  "idle" : 72  },{ "combined"  : 3,  "sys" : 3,  "user" : 0,  "idle" : 97  },{ "combined"  : 9,  "sys" : 3,  "user" : 6,  "idle" : 91  },{ "combined"  : 0,  "sys" : 0,  "user" : 0,  "idle" : 100  }
    CpuCount.get(function (result) {
        $scope.cpuCount = result.cpu_count
        $scope.cpuCountArray = [];
        for (var i = 0; i < result.cpu_count; ++i)
            $scope.cpuCountArray.push(i);
        CpuPerc.get(function (cpu_perc) {
            var cpuTotalHistory = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cpu_perc.cpu_usage];
            var cpuTotalPolt = $.jqplot('cpuInfoTotal', [cpuTotalHistory], {
                title: 'CPU Total Usage ( ' + cpu_perc.cpu_usage + '% )',
                stackSeries: true,
                showMarker: false,
                grid: {
                    backgroundColor: 'rgba(57, 57, 57, 1.0)'
                },
                axes: {
                    xaxis: {
                        renderer: $.jqplot.CategoryAxisRenderer,
                        ticks: [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50],
                        min: 0,
                        max: 50
                    },
                    yaxis: {
                        min: 0,
                        max: 100,
                        tickInterval: 20
                    }
                }
            });
            var cpuPlotList = [];
            var cpuHistoryList = [];
            for (var i = 0; i < result.cpu_count; ++i) {
                var cpuHistory = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cpu_perc.cpu_perc_list[i].combined];
                var cpuPolt = $.jqplot('cpuInfo_' + i, [cpuHistory], {
                    stackSeries: true,
                    showMarker: false,
                    axes: {
                        xaxis: {
                            renderer: $.jqplot.CategoryAxisRenderer,
                            ticks: [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50],
                            min: 0,
                            max: 50
                        },
                        yaxis: {
                            min: 0,
                            max: 100,
                            tickInterval: 20
                        }
                    }
                });
                cpuHistoryList.push(cpuHistory);
                cpuPlotList.push(cpuPolt);
            }
            if ($scope.cpuTimer)
                clearTimeout($scope.cpuTimer);
            $scope.$parent.cpuTimer = setInterval(function () {
                CpuPerc.get(function (cpu_perc) {
                    putInfoIntoCache(cpuTotalHistory, cpu_perc.cpu_usage);
                    cpuTotalPolt.series[0].data = getRedrawData(cpuTotalHistory);
                    cpuTotalPolt.title.text = 'CPU Total Usage ( ' + cpu_perc.cpu_usage + '% )';
                    cpuTotalPolt.replot();
                    for (var i = 0; i < cpuPlotList.length; ++i) {
                        putInfoIntoCache(cpuHistoryList[i], cpu_perc.cpu_perc_list[i].combined);
                        cpuPlotList[i].series[0].data = getRedrawData(cpuHistoryList[i]);
                        cpuPlotList[i].redraw();
                    }
                });
            }, 5000);
        });
    });
    //{ "memory" : { "used"  : 40,  "free" : 60,  "ram" : 11.932617  } }
    MemPerc.get(function (memInfo) {
        // { "memory" : { "used"  : 40,  "free" : 60,  "ram" : 12224  }}
        $scope.RAM = Math.round((memInfo.memory.ram / 1024.00) * 100) / 100;
        var memTotalHistory = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, memInfo.memory.used];
        var titleTextTempl = 'Memory Total Usage ( ';
        var memUsed = Math.round((memInfo.memory.ram * memInfo.memory.used / 100.0 / 1024) * 100) / 100;
        var memTotalPolt = $.jqplot('memInfo', [memTotalHistory], {
            title: titleTextTempl + memInfo.memory.used + '% --- ' + memUsed + 'G )',
            stackSeries: true,
            showMarker: false,
            grid: {
                backgroundColor: 'rgba(57, 57, 57, 1.0)'
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    ticks: [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50],
                    min: 0,
                    max: 50
                },
                yaxis: {
                    min: 0,
                    max: 100,
                    tickInterval: 20
                }
            }
        });
        if ($scope.memTimer)
            clearTimeout($scope.memTimer);
        $scope.$parent.memTimer = setInterval(function () {
            MemPerc.get(function (memInfo) {
                putInfoIntoCache(memTotalHistory, memInfo.memory.used);
                memTotalPolt.series[0].data = getRedrawData(memTotalHistory);
                memUsed = Math.round((memInfo.memory.ram * memInfo.memory.used / 100.0 / 1024) * 100) / 100;
                memTotalPolt.title.text = titleTextTempl + memInfo.memory.used + '% --- ' + memUsed + 'G )';
                memTotalPolt.replot();
            });
        }, 5000);
    });

    //{ "disk_list" : [{ "name": "B:\",  "total": 465,  "use_perc": 0.72 },{ "name": "C:\",  "total": 465,  "use_perc": 0.72 },{ "name": "R:\",  "total": 465,  "use_perc": 0.72 },{ "name": "X:\",  "total": 465,  "use_perc": 0.72 }] }
    Disk.get(function (diskInfo) {
        $scope.diskCountArray = [];
        for (var i in diskInfo.disk_list)
            $scope.diskCountArray.push(i);
        Disk.get(function (diskInfo) {
            var diskPlotList = [];
            for (var i = 0; i < diskInfo.disk_list.length; ++i) {
                var data = [
                    ['Used', diskInfo.disk_list[i].use_perc],
                    ['Free', 100 - diskInfo.disk_list[i].use_perc]
                ];
                var diskPlot = $.jqplot('diskInfo_' + i, [data], {
                    title: '( ' + diskInfo.disk_list[i].name + ' --- ' + diskInfo.disk_list[i].total + 'G ) Usage ( ' + diskInfo.disk_list[i].use_perc + '% )',
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
                    grid: {borderColor: '#999999', borderWidth: 1, shadow: false, shadowColor: 'transparent'},
                    legend: {show: true, location: 'e', xoffset: 25, yoffset: 25, border: 'none'}
                });
                diskPlotList.push(diskPlot);
            }
        });
    });

});
