$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.InfoManageView = {};
    Hrcms.InfoManageView.create = function(container) {
        var tThis = Hrcms.ContentView.create(container);
        var rightContentContainer = tThis.rightContentContainer;
        var indicator;
        var dataGrid;
        var personForm;
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
                if (personForm)
                    personForm.remove();
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
                var tableUrl, dataUrl;
                // Different module load different table info and data from different URL
                if (SysModuleID.ContentNavMenu_InfoManage_Info_Person === event.moduleID) {
                    tableUrl = "data/test/person_table.json";
                    dataUrl = "data/test/person_data_small.json";
                }
                else if (SysModuleID.ContentNavMenu_InfoManage_Info_ContactInfo === event.moduleID) {
                    tableUrl = "data/test/contact_table.json";
                    dataUrl = "data/test/contact_data.json";
                } else if (SysModuleID.ContentNavMenu_InfoManage_Info_PersonNature === event.moduleID) {
                    tableUrl = "data/test/person_properties_table.json";
                    dataUrl = "data/test/person_properties_data.json";
                } else if (SysModuleID.ContentNavMenu_InfoManage_Info_Evaluation === event.moduleID) {
                    tableUrl = "data/test/evaluation_table.json";
                    dataUrl = "data/test/evaluation_data.json";
                } else if (SysModuleID.ContentNavMenu_InfoManage_Info_Family === event.moduleID) {
                    tableUrl = "data/test/family_members_table.json";
                    dataUrl = "data/test/family_members_data.json";
                } else if (SysModuleID.ContentNavMenu_InfoManage_Records_JobExperience === event.moduleID) {
                    tableUrl = "data/test/info_before_college_table.json";
                    dataUrl = "data/test/info_before_college_data_small.json";
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
                    function(record) {
                        dataGrid.data(record);
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
                    function(record) {
                        dataGrid.data(record);
                    });
                }
                function rowdblclick(event) {
                    if (Hrcms.debugEnabled)
                        console.log(event);
                    indicator.toggle();
                    dataGrid.toggle();
                    personForm = Hrcms.PersonForm.create({
                        container: rightContentContainer,
                        navigator: [currentModuleName, $($(this).find('td')[1]).find('div').html()],
                        goBack: function(currentTag) {
                            if (currentTag.html() === currentModuleName && personForm) {
                                indicator.toggle();
                                dataGrid.toggle();
                                personForm.remove();
                                delete personForm;
                            }
                        }
                    });

                    $.ajaxSetup({cache: false});
                    $.ajax({
                        type: "GET",
                        url: "app/profile/html/basic/personInfo.html",
                        dataType: "html",
                        success: function(personInfoTempl) {
                            personForm.configure({
                                personFormTempl: personInfoTempl,
                                reportTemplURL: "app/profile/html/basic/personSummary.html",
                                reportDataURL: "data/test/person_summary.json"
                            });
                        }
                    });
                }
                $.ajaxSetup({cache: false});
                $.getJSON(tableUrl,
                function(table) {
                    dataGrid.configure({
                        columns: table.columns,
                        event: {
                            sortup: sortup,
                            sortdown: sortdown,
                            rowdblclick: rowdblclick
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
            if (personForm)
                personForm.remove();
            __superRemove();
        }

        tThis.initialize();

        // end
        return tThis;
    }
});