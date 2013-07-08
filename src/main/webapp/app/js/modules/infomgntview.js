$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.InfoManageView = {};
    Hrcms.InfoManageView.create = function(container) {
        var tThis = Hrcms.ContentView.create(container);
        var rightContentContainer = tThis.rightContentContainer;
        var indicator;
        var dataGrid;
        var recordForm;
        var currentModuleName;
        var SysModuleID = Hrcms.SysModuleID;
        tThis.loadLeftNavMenu = function(menu) {
            menu.configure(Hrcms.NavMenu_InfoManage_Config);
            menu.onItemClick(function(event) {
                if (Hrcms.debugEnabled)
                    console.log(event);
                if (indicator)
                    indicator.remove();
                if (dataGrid)
                    dataGrid.remove();
                if (recordForm)
                    recordForm.remove();
                currentModuleName = event.moduleName;
                indicator = Hrcms.NavIndicator.create(rightContentContainer);
                indicator.goTo(currentModuleName);
                dataGrid = Hrcms.DataGrid.create({
                    container: rightContentContainer,
                    offsetHeight: 82
                });
                indicator.configure({
                    toolbar: {barType: Hrcms.Tablebar},
                    tableNavTarget: dataGrid
                });
                var talbeUrl, dataUrl;
                // Different module load different table info and data from different URL
                if (SysModuleID.ContentNavMenu_InfoManage_Info_Person === event.moduleID) {
                    talbeUrl = "data/test/person_table.json";
                    dataUrl = "data/test/person_data_small.json";
                }
                else if (SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo === event.moduleID) {
                    talbeUrl = "data/test/contact_table.json";
                    dataUrl = "data/test/contact_data.json";
                }
                // Event function
                function sortup(event) {
                    var tbody = event ? event.tbody : null;
                    var column = event ? event.column : null;
                    if (Hrcms.debugEnabled)
                        console.log(column);
                    // Remove all data rows first
                    if (tbody)
                        tbody.empty();
                    $.getJSON(dataUrl,
                    function(result) {
                        dataGrid.data(result);
                    });
                }
                function sortdown(event) {
                    var tbody = event ? event.tbody : null;
                    var column = event ? event.column : null;
                    if (Hrcms.debugEnabled)
                        console.log(column);
                    // Remove all data rows first
                    if (tbody)
                        tbody.empty();
                    $.getJSON(dataUrl,
                    function(result) {
                        dataGrid.data(result);
                    });
                }
                function rowclick(event) {
                    if (Hrcms.debugEnabled)
                        console.log(event);
                    indicator.toggle();
                    dataGrid.toggle();
                    recordForm = Hrcms.RecordForm.create({
                        container: rightContentContainer,
                        navigator: [currentModuleName, $($(this).find('td')[1]).find('div').html()],
                        goBack: function(currentTag) {
                            if (currentTag.html() === currentModuleName && recordForm) {
                                indicator.toggle();
                                dataGrid.toggle();
                                recordForm.remove();
                                delete recordForm;
                            }
                        }
                    });

                    $.ajaxSetup({cache: false});
                    $.ajax({
                        type: "GET",
                        url: "app/profile/html/tabsTempl.html",
                        dataType: "html",
                        success: function(tabsTempl) {
                            recordForm.configure({
                                tabsTempl: tabsTempl,
                                reportTemplURL: "app/profile/html/personSummary.html",
                                reportDataURL: "data/test/person_summary.json"
                            });
                        }
                    });
                }
                $.ajaxSetup({cache: false});
                $.getJSON(talbeUrl,
                function(data) {
                    dataGrid.configure({
                        columns: data.columns,
                        event: {
                            sortup: sortup,
                            sortdown: sortdown,
                            rowclick: rowclick
                        }
                    });
                    sortdown();
                }).fail(function(result) {
                    if (Hrcms.debugEnabled)
                        console.log(result);
                });
            });
        }

        var __superRemove = tThis.remove;
        tThis.remove = function() {
            if (recordForm)
                recordForm.remove();
            __superRemove();
        }

        tThis.initialize();

        // end
        return tThis;
    }
});