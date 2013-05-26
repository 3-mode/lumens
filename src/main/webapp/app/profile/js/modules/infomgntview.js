$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    Hrcms.InfoManageView = {};
    Hrcms.InfoManageView.create = function(container) {
        var tThis = Hrcms.ContentView.create(container);
        var rightContentContainer = tThis.rightContentContainer;
        var indicator;
        var dataGrid;
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
                indicator = Hrcms.NavIndicator.create(rightContentContainer);
                indicator.goTo(event.moduleName);
                dataGrid = Hrcms.DataGrid.create({
                    container: rightContentContainer,
                    offsetHeight: 82
                });
                indicator.configure({
                    barType: Hrcms.Tablebar,
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
                    if (column)
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
                    if (column)
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
                    console.log('"' + $(this).attr("row-number") + '" is clicked !');
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
                    console.log(result.error());
                });
            });
        }

        tThis.initialize();

        // end
        return tThis;
    }
});