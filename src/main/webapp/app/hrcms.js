$(function() {
    if (window.Hrcms === undefined)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    Hrcms.create = function(containerObj) {
        var tThis = {};
        var rootContainer = containerObj;
        var heaerContainer = null;
        rootContainer.attr("class", "hrcms");
        function layoutHeader(parentContainer) {
            var headerCon = $('<div class="hrcms-header-constainer"/>');
            headerCon.appendTo(parentContainer);
            return headerCon;
        }
        tThis.load = function() {
            console.log("Loading HRCMS !");
            heaerContainer = layoutHeader(rootContainer);
            /** Header begin */
            Hrcms.Header.create(heaerContainer).setSysTitle(I18N.SystemTitle);
            function clickToolbarButton(event) {
                console.log($(this).find('span').html());
            }
            // TODO use ajax to load the toolbar button strings
            var toolbar = Hrcms.Toolbar.create(heaerContainer);
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
        var header = $('<div class="hrcms-header"/>');
        header.appendTo(containerObj);
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
        var toolbar = $('<div class="hrcms-toolbar"/>');
        toolbar.appendTo(container);
        var toolbarContent = $('<ul/>');
        toolbarContent.appendTo(toolbar);
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
                var button = $('<li><a><span class="hrcms-toolbar-button-text"></span></a></li>');
                button.appendTo(toolbarContent);
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
        var nav = $('<div class="hrcms-secondary-menu"/>');
        nav.appendTo(args.container);
        nav.css("width", args.width);
        nav.css("height", args.height);
        var activeItem = null;
        var Section = {};
        Section.create = function(sectionName) {
            var tThis = {};
            var section = $('<div/>');
            section.appendTo(nav);
            var sectionTitle = $('<div class="hrcms-secondary-menu-section"></div>');
            sectionTitle.appendTo(section);
            sectionTitle.html(sectionName);
            var sectionContent = $('<ul class="hrcms-secondary-submenu"/>');
            sectionContent.appendTo(section);
            //Operation functions
            tThis.addItem = function(itemName) {
                var item = $('<li><a><span class="hrcms-secondary-menu-text"/></a></li>');
                item.appendTo(sectionContent);
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

    Hrcms.DataGrid = {}
    Hrcms.DataGrid.create = function(args) {
        var tThis = {};
        var container = args.container;
        var gridContainer = $('<div class="hrcms-datagrid-container" />')
        gridContainer.appendTo(container);
        var table = $('<table border="0" class="hrcms-datagrid"/>');
        table.appendTo(gridContainer);
        table.css("width", args.width);
        table.css("height", args.height);
        tThis.configure = function(args) {
            var thead = $('<thead><tr></tr></thead>');
            thead.appendTo(table);
            thead = thead.find('tr');
            var columns = args.columns;
            for (var i = 0; i < columns.length; ++i) {
                thead.append('<th><div style="width:' + columns[i].width + '">' + columns[i].name + '</div></th>');
            }
            table.append('<tfoot><tr><td colspan="' + columns.length + '"></td></tr></tfoot>');
        }
        // end
        return tThis;

    }

    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var layoutContent = tThis.layoutContent = function(parentContainer) {
            var contentCon = $('<div class="hrcms-content-constainer"/>');
            contentCon.appendTo(parentContainer);
            return contentCon;
        }
        var layoutNavMenu = tThis.layoutNavMenu = function(parentContainer) {
            var menuContainer = $('<div class="hrcms-secondary-menu-container"/>');
            menuContainer.appendTo(parentContainer);
            menuContainer.append('<div id="place-holder" style="width:220px;height:10px;float:top;"/>');
            return Hrcms.NavMenu.create({
                container: menuContainer,
                width: "100%",
                height: "auto"
            });
        }

        var contentContainer = layoutContent(container);
        var splitterContainer = $('<div class="SplitterPane layout-content splitter-pane-container" style="overflow:hidden;"></div>');
        splitterContainer.appendTo(contentContainer);
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
        var workspaceContainer = $('<div class="hrcms-workspace-container"/>');
        workspaceContainer.appendTo(rightContainer);

        var dataGrid = Hrcms.DataGrid.create({
            container: workspaceContainer,
            width: "100%",
            height: "auto"
        });
        dataGrid.configure({
            columns: [
                {
                    name: "test 1",
                    width: "100px",
                    click: function(event) {
                    }
                },
                {
                    name: "test 2",
                    width: "100px",
                    click: function(event) {
                    }
                },
                {
                    name: "test 3",
                    width: "100px",
                    click: function(event) {
                    }
                }
            ]
        }); //*/
        // TODO ContentTitle
        /** Content end */
        // TODO ContentTable
        // end
        return tThis;
    }
});