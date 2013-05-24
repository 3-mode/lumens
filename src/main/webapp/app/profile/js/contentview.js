$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    // TODO why I18N here ?
    var I18N = Hrcms.I18N;
    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var layoutContent = tThis.layoutContent = function(parentContainer) {
            return $('<div class="hrcms-content-constainer"/>').appendTo(parentContainer);
        }
        var layoutNavMenu = tThis.layoutNavMenu = function(parentContainer) {
            return Hrcms.NavMenu.create({
                container: parentContainer,
                width: "100%",
                height: "auto"
            });
        }

        var contentContainer = layoutContent(container);
        var htmlTpl =
        '<div class="SplitterPane layout-content splitter-pane-container" style="overflow:hidden;">' +
        '  <div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 200px; height: 100%;"/>' +
        '  <div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>' +
        '</div>'
        var splitterContainer = $(htmlTpl).appendTo(contentContainer);
        splitterContainer.splitter({
            splitVertical: true,
            sizeLeft: true
        });
        var leftContainer = tThis.leftContainer = splitterContainer.find('#LeftPane');
        var rightContainer = tThis.rightContainer = splitterContainer.find('#RightPane');
        var workspaceContainer = $('<div class="hrcms-workspace-container"/>').appendTo(rightContainer);
        rightContainer.resize(function(event) {
            if (event.target !== this)
                return;
            workspaceContainer.trigger("resize");
        });
        var menu = tThis.navMenu = layoutNavMenu(leftContainer);
        // TODO Need a real event function
        //==========================================================================================
        function itemDemoClick(event, ui) {
            console.log($(this).find('span').html());
        }
        Hrcms.NavMenu_InfoManage_Config.event_callback = itemDemoClick;
        menu.configure(Hrcms.NavMenu_InfoManage_Config);
        //==========================================================================================
        //==========================================================================================
        var workspaceHeader = Hrcms.NavIndicator.create({
            container: workspaceContainer,
            title: I18N.ContentNavMenu.InfoManage_Info_Person
        });
        // TODO
        var dataGrid = Hrcms.DataGrid.create({
            container: workspaceContainer,
            offsetHeight: 82
        });

        workspaceHeader.configure({
            tableNavTarget: dataGrid
        });
        workspaceHeader.goTo('NO10001');

        function sortup(event) {
            var tbody = event ? event.tbody : null;
            var column = event ? event.column : null;
            if (column)
                console.log(column);
            // Remove all data rows first
            if (tbody)
                tbody.empty();
            $.getJSON("data/test/person_info_small.json",
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
            $.getJSON("data/test/person_info_small.json",
            function(result) {
                dataGrid.data(result);
            });
        }
        function rowclick(event) {
            console.log('"' + $(this).attr("row-number") + '" is clicked !');
        }
        function prevpage(event) {
        }
        function nextpage(event) {
        }
        function firstpage(event) {
        }
        function lastpage(event) {
        }
        $.ajaxSetup({cache: false});
        $.getJSON("data/test/person_table.json",
        function(data) {
            dataGrid.configure({
                columns: data.columns,
                event: {
                    sortup: sortup,
                    sortdown: sortdown,
                    prevpage: prevpage,
                    nextpage: nextpage,
                    firstpage: firstpage,
                    lastpage: lastpage,
                    rowclick: rowclick
                }
            });
            sortdown();
        }).fail(function(result) {
            console.log(result.error());
        });

        return tThis;
    }
});