/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers
.controller("DashboardViewCtrl", function ($scope, $route) {
    LumensLog.log("in DashboardViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    Lumens.system.switchTo(Lumens.system.AngularJSView);
    $scope.i18n = Lumens.i18n;
    $scope.monitor_template = "app/templates/dashboard/server_monitor_tmpl.html";
})
.controller("ServerMonitorCtrl", function ($scope, $interval, CpuPerc, CpuCount, MemPerc, Disk) {
    console.log("In ServerMonitorCtrl");
    var i18n = $scope.i18n;
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
        $scope.cpuCount = i18n.id_cpu_statis.format(result.cpu_count);
        $scope.cpuCountArray = [];
        for (var i = 0; i < result.cpu_count; ++i)
            $scope.cpuCountArray.push(i);
        CpuPerc.get(function (cpu_perc) {
            var cpuTotalHistory = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cpu_perc.cpu_usage];
            var cpuTotalPolt = $.jqplot('cpuInfoTotal', [cpuTotalHistory], {
                title: i18n.id_cpu_total_usage.format(cpu_perc.cpu_usage),
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
            var cpuTimer = $interval(function () {
                if (cpuTimer && $('#cpuInfoTotal').length === 0) {
                    $interval.cancel(cpuTimer);
                    cpuTimer = null;
                    return;
                }
                CpuPerc.get(function (cpu_perc) {
                    putInfoIntoCache(cpuTotalHistory, cpu_perc.cpu_usage);
                    cpuTotalPolt.series[0].data = getRedrawData(cpuTotalHistory);
                    cpuTotalPolt.title.text = i18n.id_cpu_total_usage.format(cpu_perc.cpu_usage);
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
        $scope.RAM = i18n.id_mem_statis.format(Math.round((memInfo.memory.ram / 1024.00) * 100) / 100);
        var memTotalHistory = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, memInfo.memory.used];
        var memUsed = Math.round((memInfo.memory.ram * memInfo.memory.used / 100.0 / 1024) * 100) / 100;
        var memTotalPolt = $.jqplot('memInfo', [memTotalHistory], {
            title: i18n.id_mem_usage.format(memInfo.memory.used, memUsed),
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

        var memTimer = $interval(function () {
            if (memTimer && $('#cpuInfoTotal').length === 0) {
                $interval.cancel(memTimer);
                memTimer = null;
                return;
            }

            MemPerc.get(function (memInfo) {
                putInfoIntoCache(memTotalHistory, memInfo.memory.used);
                memTotalPolt.series[0].data = getRedrawData(memTotalHistory);
                memUsed = Math.round((memInfo.memory.ram * memInfo.memory.used / 100.0 / 1024) * 100) / 100;
                memTotalPolt.title.text = i18n.id_mem_usage.format(memInfo.memory.used, memUsed);
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
                    [i18n.id_disk_chart_usage, diskInfo.disk_list[i].use_perc],
                    [i18n.id_disk_chart_free, 100 - diskInfo.disk_list[i].use_perc]
                ];
                var diskPlot = $.jqplot('diskInfo_' + i, [data], {
                    title: i18n.id_disk_usage.format(diskInfo.disk_list[i].name, diskInfo.disk_list[i].total, diskInfo.disk_list[i].use_perc),
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