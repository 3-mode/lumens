$(function() {
    if (window.Hrcms === undefined)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    Hrcms.create = function(containerObj) {
        var tThis = {};
        var rootContainer = containerObj;
        var headerContainer = null;
        rootContainer.addClass("hrcms");
        function layoutHeader(parentContainer) {
            return $('<div class="hrcms-header-constainer"/>').appendTo(parentContainer);
        }
        tThis.load = function() {
            console.log("Loading HRCMS !");
            headerContainer = layoutHeader(rootContainer);
            /** Header begin */
            Hrcms.Header.create(headerContainer).setSysTitle(I18N.SystemTitle);
            function clickToolbarButton(event) {
                console.log($(this).find('span').html());
            }
            // TODO use ajax to load the toolbar button strings
            var toolbar = Hrcms.Toolbar.create(headerContainer);
            toolbar.configure([
                {
                    title: I18N.Toolbar.Home,
                    click: clickToolbarButton
                },
                {
                    title: I18N.Toolbar.Information,
                    click: clickToolbarButton
                },
                {
                    title: I18N.Toolbar.Statistic_Analysis,
                    click: clickToolbarButton
                },
                {
                    title: I18N.Toolbar.WorkflowCheck,
                    click: clickToolbarButton
                },
                {
                    title: I18N.Toolbar.MessageCenter,
                    click: clickToolbarButton
                },
                {
                    title: I18N.Toolbar.SystemManage,
                    click: clickToolbarButton
                }
            ]);
            /** Header end */
            /** Content begin */
            var view = Hrcms.ContentView.create(rootContainer);
        }
        // end
        return tThis;
    }

    Hrcms.Header = {};
    Hrcms.Header.create = function(containerObj) {
        var tThis = {};
        var header = $('<div class="hrcms-header"/>').appendTo(containerObj);
        header.append('<table><tr><td><div class="hrcms-logo"/></td><td><div id="hrcms-system-title"></div></td></tr></table>');
        var sysTitle = $("#hrcms-system-title");
        tThis.setSysTitle = function(text) {
            sysTitle.html(text);
        }
        // end
        return tThis;

    }

    Hrcms.Toolbar = {};
    Hrcms.Toolbar.create = function(containerObj) {
        var tThis = {};
        var container = containerObj;
        var toolbar = $('<div class="hrcms-toolbar"/>').appendTo(container);
        var toolbarContent = $('<ul/>').appendTo(toolbar);
        var buttonList = [];
        var activeItem = null;
        function activeButton(event) {
            if (activeButton !== null)
                activeItem.toggleClass("hrcms-v-active");
            activeItem = $(this);
            activeItem.toggleClass("hrcms-v-active");
        }
        tThis.configure = function(buttons) {
            for (var i = 0; i < buttons.length; ++i) {
                var button = $('<li><a><span class="hrcms-toolbar-button-text"></span></a></li>').appendTo(toolbarContent);
                button.find('span').html(buttons[i].title);
                button.on('click', activeButton);
                button.on('click', buttons[i].click);
                buttonList.push(button);
            }
            activeItem = buttonList[0];
            activeItem.toggleClass("hrcms-v-active");
        }
        // end
        return tThis;
    }

    Hrcms.NavMenu = {};
    Hrcms.NavMenu.create = function(args) {
        var tThis = {};
        var nav = $('<div class="hrcms-secondary-menu"/>').appendTo(args.container);
        nav.css("width", args.width);
        nav.css("height", args.height);
        var activeItem = null;
        var Section = {};
        Section.create = function(sectionName) {
            var tThis = {};
            var section = $('<div/>').appendTo(nav);
            var sectionTitle = $('<div class="hrcms-secondary-menu-section"></div>').appendTo(section);
            sectionTitle.html(sectionName);
            var sectionContent = $('<ul class="hrcms-secondary-submenu"/>').appendTo(section);
            //Operation functions
            tThis.addItem = function(itemName) {
                var item = $('<li><a><span class="hrcms-secondary-menu-text"/></a></li>').appendTo(sectionContent);
                item.on('click', function(event, ui) {
                    if (activeItem !== null)
                        activeItem.toggleClass('hrcms-h-active');
                    activeItem = $(this);
                    activeItem.toggleClass('hrcms-h-active');
                });
                var itemTitle = item.find('span');
                itemTitle.html(itemName);
                return item;
            }
            //end
            return tThis;
        }
        tThis.configure = function(sections) {
            for (var i = 0; i < sections.length; ++i) {
                var section = Section.create(sections[i].title);
                var items = sections[i].items;
                for (var j = 0; j < items.length; ++j) {
                    section.addItem(items[j].title).on('click', items[j].click);
                }
            }
        }
        //end
        return tThis;
    }

    Hrcms.DataNavBar = {};
    Hrcms.DataNavBar.create = function(args) {
        var tThis = {};
        var container = args.container;
        var workspaceHeader = $('<div class="hrcms-workspace-header"/>').appendTo(container);
        var headerNav = $('<div class="hrcms-workspace-nav-padding"/>').appendTo(workspaceHeader);
        var workspaceToolbar = $('<div class="hrcms-workspace-toolbar"/>').appendTo(workspaceHeader);
        var nav = $('<div class="hrcms-workspace-nav"><span class="hrcms-workspace-nav-current">'
        + args.title + '</span></div>').appendTo(headerNav);
        tThis.goTo = function(text) {
            var last = nav.find('span').last();
            last.toggleClass('hrcms-workspace-nav-back');
            last.toggleClass('hrcms-workspace-nav-current');
            last.on('click', function(event) {
                tThis.goBack();
            });
            nav.append('<span style="padding-left: 4px; padding-right:4px;">/</span><span class="hrcms-workspace-nav-current">'
            + text + '</span>');
        }
        tThis.goBack = function() {
            var canGoBack = nav.find('.hrcms-workspace-nav-back');
            if (canGoBack.length === 0)
                return;
            nav.find('span').last().remove();
            nav.find('span').last().remove();
            var last = nav.find('span').last();
            last.toggleClass('hrcms-workspace-nav-back');
            last.toggleClass('hrcms-workspace-nav-current');
        }
        tThis.configButtons = function(buttons) {
            for (var i = 0; i < buttons.length; ++i) {
                var btn = $('<button class="hrcms-button"></button>')
                .appendTo($('<div style="margin-left:15px;"/>')
                .appendTo(workspaceToolbar));
                btn.button();
                btn.text(buttons[i].title);
                btn.on("click", buttons[i].click);
            }
        }
        // end
        return tThis;
    }

    Hrcms.DataGrid = {}
    Hrcms.DataGrid.create = function(containerObj) {
        var tThis = {};
        var container = containerObj;
        var resizeHook = container.parent('#RightPane');
        var gridContainer = $('<div class="hrcms-datagrid-container" />').appendTo(container);
        var table = $('<table class="hrcms-datagrid"/>').appendTo(gridContainer);
        var tableBody = null;
        var dataFieldNames = null;
        var currentSortTh = null;
        gridContainer.css("width", container.width());
        gridContainer.css("height", container.height() - 82);
        resizeHook.resize(function(evt) {
            gridContainer.css("width", container.width());
            gridContainer.css("height", container.height() - 82);
        });
        tThis.configure = function(args) {
            var sortup = args.sortup;
            var sortdown = args.sortdown;
            var thead = $('<thead><tr></tr></thead>').appendTo(table);
            thead = thead.find('tr');
            var columns = args.columns;
            dataFieldNames = [];
            function sort() {
                var clickedTh = $(this);
                if (currentSortTh !== null) {
                    if (clickedTh.get(0) === currentSortTh.get(0)) {
                        if (currentSortTh.hasClass("sortdown")) {
                            currentSortTh.removeClass("sortdown");
                            currentSortTh.addClass("sortup");
                            if (sortup !== undefined)
                                sortup(currentSortTh);
                        }
                        else if (currentSortTh.hasClass("sortup")) {
                            currentSortTh.removeClass("sortup");
                            currentSortTh.addClass("sortdown");
                            if (sortdown !== undefined)
                                sortdown(currentSortTh);
                        }
                    }
                    else {
                        if (currentSortTh.hasClass("sortdown")) {
                            currentSortTh.removeClass("sortdown");
                            clickedTh.addClass("sortdown");
                            if (sortdown !== undefined)
                                sortdown(clickedTh);
                        }
                        else if (currentSortTh.hasClass("sortup")) {
                            currentSortTh.removeClass("sortup");
                            clickedTh.addClass("sortup");
                            if (sortup !== undefined)
                                sortup(currentSortTh);
                        }
                        currentSortTh = clickedTh;
                    }
                }
                else {
                    currentSortTh = clickedTh;
                    currentSortTh.addClass("sortdown");
                }
            }
            for (var i = 0; i < columns.length; ++i) {
                var th = $('<th/>').appendTo(thead);
                th.addClass("hrcms-datagrid-header sortable");
                th.css("padding-left", "8px");
                th.css("padding-right", "20px");
                th.attr("field-name", columns[i].field);
                th.on('click', sort);
                var div = $('<div class="hrcms-datagrid-header-text"></div>').appendTo(th);
                div.html(columns[i].name);
                dataFieldNames.push(columns[i].field);
            }
            tableBody = $('<tbody/>').appendTo(table);
            table.append('<tfoot><tr><td colspan="' + columns.length + '"><div style="height:25px;"></div></td></tr></tfoot>');
        }
        tThis.data = function(records) {
            for (var i = 0; i < records.length; ++i) {
                var tr = $('<tr/>').appendTo(tableBody);
                tr.addClass("hrcms-datagrid-row");
                tr.attr('row-number', i);
                var record = records[i].record;
                for (var j = 0; j < dataFieldNames.length; ++j) {
                    td = $('<td/>').appendTo(tr);
                    td.attr('field-name', dataFieldNames[j]);
                    td.html(record[j]);
                }
            }
        }
        // end
        return tThis;
    }

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
        var splitterContainer = $('<div class="SplitterPane layout-content splitter-pane-container" style="overflow:hidden;"></div>').appendTo(contentContainer);
        splitterContainer.append('<div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 200px; height: 100%;"/>');
        splitterContainer.append('<div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>');
        splitterContainer.splitter({
            splitVertical: true,
            sizeLeft: true
        });
        var leftContainer = tThis.leftContainer = splitterContainer.find('#LeftPane');
        var rightContainer = tThis.rightContainer = splitterContainer.find('#RightPane');
        var menu = tThis.navMenu = layoutNavMenu(leftContainer);

        // TODO Need a real event function
        function itemDemoClick(event, ui) {
            console.log($(this).find('span').html());
        }
        // TODO it should load the json configure
        menu.configure([{
                title: I18N.ContentNavMenu.Info.Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Info.Person,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Info.ContactInfo,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Info.PersonNature,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Info.Evaluation,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Info.Family,
                        click: itemDemoClick
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.Records.Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Records.JobExperience,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.Degree,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.TrainingInLand,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.TrainingOutLand,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.Award,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.Punishment,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.AnomalyInCollege,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.InfoToJoinCollege,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Records.LeaveOffice,
                        click: itemDemoClick
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.JobInfo.Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.JobInfo.Unit,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo.Politics,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo.JobOfPolitics,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo.TechnicalTitles,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.JobInfo.TechnicalLevel,
                        click: itemDemoClick
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.Qualification.Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Qualification.PostdoctoralTeacher,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Qualification.VisitingScholarTeacher,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Qualification.ExpertJob,
                        click: itemDemoClick
                    },
                    {
                        title: I18N.ContentNavMenu.Qualification.TalentsFunding,
                        click: itemDemoClick
                    }
                ]
            },
            {
                title: I18N.ContentNavMenu.Contract.Title,
                items: [
                    {
                        title: I18N.ContentNavMenu.Contract.EmploymentContract,
                        click: itemDemoClick
                    }
                ]
            }
        ]);
        var workspaceContainer = $('<div class="hrcms-workspace-container"/>').appendTo(rightContainer);
        var workspaceHeader = Hrcms.DataNavBar.create({
            container: workspaceContainer,
            title: I18N.ContentNavMenu.Info.Person
        });
        workspaceHeader.configButtons([
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
        var dataGrid = Hrcms.DataGrid.create(workspaceContainer);
        // TODO
        function sortup(column) {
            console.log(column);
        }
        function sortdown(column) {
            console.log(column);
        }
        dataGrid.configure({
            columns: [
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                }, {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                }, {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                }, {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                }, {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "员工号",
                    field: "field-2",
                    sortup: sortup,
                    sortdown: sortdown
                },
                {
                    name: "身份证号",
                    field: "field-3",
                    sortup: sortup,
                    sortdown: sortdown
                }, {
                    name: "员工名字",
                    field: "field-1",
                    sortup: sortup,
                    sortdown: sortdown
                }
            ]
        });
        dataGrid.data([
            {record: ["张三", "NO1001", "12121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]},
            {record: ["李四", "NO1002", "22121212121212"]},
            {record: ["刘五", "NO1003", "32121212121212"]}//*/
        ]);
        //*/
        // TODO ContentTitle
        /** Content end */
        // TODO ContentTable
        // end
        return tThis;
    }
});