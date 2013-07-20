$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.PersonForm = {};
    Hrcms.PersonForm.create = function(config) {
        var tThis = {};
        var container = config.container;
        var formContainer = $('<div class="hrcms-form-container"/>').appendTo(container);
        var workspaceHeader = Hrcms.NavIndicator.create(formContainer);
        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);
        workspaceHeader.configure({
            goBack: config.goBack
        });
        var mainForm = $(
        '<div class="hrcms-record-main-form">' +
        '</div>'
        ).appendTo(formContainer);

        var offsetHeight = config.offsetHeight ? config.offsetHeight : 0;
        var offsetWidth = config.offsetWidth ? config.offsetWidth : 0;
        function resize(event) {
            var w = container.width() - offsetWidth;
            var h = container.height() - offsetHeight;
            formContainer.css("width", w);
            formContainer.css("height", h);
            var size = workspaceHeader.size();
            mainForm.css("height", h - size.height);
        }
        container.bind("resize", resize);
        resize();
        var formEntry;

        // Member methods
        tThis.configure = function(config) {
            // TODO HTML template configuration here
            formEntry = $(config.personFormTempl).appendTo(mainForm);
            formEntry.find("#tabs").tabs({
                activate: function(event, ui) {
                    if (Hrcms.debugEnabled) {
                        console.log(event);
                        console.log(ui);
                    }
                    var id = ui.newPanel.attr("id");
                    if (id === "basicInfo") {
                        if (ui.newPanel.children().length > 0)
                            return;
                        var list = Hrcms.List.create(formEntry.find("#basicInfo"));
                        list.configure({
                            formTitleList: [
                                I18N.ContentNavMenu.InfoManage_Info_PersonNature,
                                I18N.ContentNavMenu.InfoManage_Info_ContactInfo,
                                I18N.ContentNavMenu.InfoManage_Info_Evaluation,
                                I18N.ContentNavMenu.InfoManage_Info_Family
                            ],
                            formURLList: [
                                "app/profile/html/basic/nature.html",
                                "app/profile/html/basic/contactInfo.html",
                                "app/profile/html/basic/evaluate.html",
                                "app/profile/html/basic/family.html"
                            ],
                            callback: function(accordion) {
                                var natureInfo = accordion.find("#natureInfo");
                                var dataGrid0 = Hrcms.DataGrid.create({
                                    container: accordion.find("#natureInfoGrid")
                                });
                                $.ajaxSetup({cache: false});
                                $.getJSON("data/test/person_nature_table.json",
                                function(table) {
                                    dataGrid0.configure({
                                        columns: table.columns,
                                        event: {
                                            rowclick: function(event) {
                                                var tdList = $(this).find("td");
                                                tdList.each(function(index, td) {
                                                    var fieldName = $(td).attr("field-name");
                                                    natureInfo.find('input[field-name="' + fieldName + '"]').val($(td).find("div").html());
                                                });
                                            }
                                        }
                                        // TODO add sort function
                                    });
                                    $.getJSON("data/test/person_nature_data.json",
                                    function(record) {
                                        dataGrid0.data(record);
                                    });
                                }).fail(function(result) {
                                    if (Hrcms.debugEnabled)
                                        console.log(result);
                                });

                                var contactInfo = accordion.find("#contactInfo");
                                var dataGrid1 = Hrcms.DataGrid.create({
                                    container: accordion.find("#contactInfoGrid")
                                });
                                $.ajaxSetup({cache: false});
                                $.getJSON("data/test/person_contact_table.json",
                                function(table) {
                                    dataGrid1.configure({
                                        columns: table.columns,
                                        event: {
                                            rowclick: function(event) {
                                                var tdList = $(this).find("td");
                                                tdList.each(function(index, td) {
                                                    var fieldName = $(td).attr("field-name");
                                                    contactInfo.find('input[field-name="' + fieldName + '"]').val($(td).find("div").html());
                                                });
                                            }
                                        }
                                        // TODO add sort function
                                    });
                                    $.getJSON("data/test/person_contact_data.json",
                                    function(record) {
                                        dataGrid1.data(record);
                                    });
                                }).fail(function(result) {
                                    if (Hrcms.debugEnabled)
                                        console.log(result);
                                });

                                var evaluateInfo = accordion.find("#evaluateInfo");
                                var dataGrid3 = Hrcms.DataGrid.create({
                                    container: accordion.find("#evaluateGrid")
                                });
                                $.ajaxSetup({cache: false});
                                $.getJSON("data/test/person_evaluation_table.json",
                                function(table) {
                                    dataGrid3.configure({
                                        columns: table.columns,
                                        event: {
                                            rowclick: function(event) {
                                                var tdList = $(this).find("td");
                                                tdList.each(function(index, td) {
                                                    var fieldName = $(td).attr("field-name");
                                                    evaluateInfo.find('input[field-name="' + fieldName + '"]').val($(td).find("div").html());
                                                });
                                            }
                                        }
                                        // TODO add sort function
                                    });
                                    $.getJSON("data/test/person_evaluation_data.json",
                                    function(recrod) {
                                        dataGrid3.data(recrod);
                                    });
                                }).fail(function(result) {
                                    if (Hrcms.debugEnabled)
                                        console.log(result);
                                });

                                var familyInfo = accordion.find("#familyInfo");
                                var dataGrid4 = Hrcms.DataGrid.create({
                                    container: accordion.find("#familyGrid")
                                });
                                $.ajaxSetup({cache: false});
                                $.getJSON("data/test/person_family_table.json",
                                function(table) {
                                    dataGrid4.configure({
                                        columns: table.columns,
                                        event: {
                                            rowclick: function(event) {
                                                var tdList = $(this).find("td");
                                                tdList.each(function(index, td) {
                                                    var fieldName = $(td).attr("field-name");
                                                    familyInfo.find('input[field-name="' + fieldName + '"]').val($(td).find("div").html());
                                                });
                                            }
                                        }
                                        // TODO add sort function
                                    });
                                    $.getJSON("data/test/person_family_data.json",
                                    function(recrod) {
                                        dataGrid4.data(recrod);
                                    });
                                }).fail(function(result) {
                                    if (Hrcms.debugEnabled)
                                        console.log(result);
                                });
                            }
                        });
                    }
                }
            });
            var reportFactory = Hrcms.ReportFactory.create();
            reportFactory.load({
                contentHolder: formEntry.find("#personSummaryReport").find(".hrcms-tab-content"),
                reportTemplURL: config.reportTemplURL,
                reportDataURL: config.reportDataURL
            });
        }
        tThis.remove = function() {
            formContainer.remove();
        }
        // end
        return tThis;
    }
});
