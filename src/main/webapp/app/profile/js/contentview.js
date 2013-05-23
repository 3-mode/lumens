$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    // TODO why I18N here ?
    var I18N = Hrcms.I18N;
    var SyncGet = Hrcms.SyncGet;
    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var layoutContent = tThis.layoutContent = function(parentContainer) {
            return $('<div class="hrcms-content-constainer"/>').appendTo(parentContainer);
        }
        var layoutNavMenu = tThis.layoutNavMenu = function(parentContainer) {
            var menuContainer = $('<div class="hrcms-secondary-menu-container"/>').appendTo(parentContainer);
            menuContainer.append('<div id="place-holder" style="width:220px;height:10px;float:top;"/>');
            return Hrcms.NavMenu.create({
                container: menuContainer,
                width: "100%",
                height: "auto"
            });
        }

        var contentContainer = layoutContent(container);
        var htmlTpl = SyncGet({
            url: "html/splitter-tpl.html",
            dataType: "html"
        });

        var splitterContainer = $(htmlTpl).appendTo(contentContainer);
        splitterContainer.splitter({
            splitVertical: true,
            sizeLeft: true
        });
        var leftContainer = tThis.leftContainer = splitterContainer.find('#LeftPane');
        var menu = tThis.navMenu = layoutNavMenu(leftContainer);
        // TODO Need a real event function
        function itemDemoClick(event, ui) {
            console.log($(this).find('span').html());
        }
        Hrcms.NavMenu_Config.event_callback = itemDemoClick;
        menu.configure(Hrcms.NavMenu_Config);
        var rightContainer = tThis.rightContainer = splitterContainer.find('#RightPane');
        var workspaceContainer = $('<div class="hrcms-workspace-container"/>').appendTo(rightContainer);
        rightContainer.resize(function(event) {
            if (event.target !== this)
                return;
            workspaceContainer.trigger("resize");
        });
        var workspaceHeader = Hrcms.NavIndicator.create({
            container: workspaceContainer,
            title: I18N.ContentNavMenu.Info_Person
        });
        // TODO
        workspaceHeader.configure([
            {
                title: "添加",
                click: function(event) {
                    console.log(this);
                }
            },
            {
                title: "删除",
                click: function(event) {
                    console.log(this);
                }
            }
        ]);
        workspaceHeader.goTo('NO10001');
        var dataGrid = Hrcms.DataGrid.create({
            container: workspaceContainer,
            offsetHeight: 82
        });

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