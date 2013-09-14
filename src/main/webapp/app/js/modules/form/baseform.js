$(function() {
    Hrcms.BaseForm = {};
    Hrcms.BaseForm.create = function(config) {
        var tThis = {};
        var container = config.container;
        var formContainerPanel = Hrcms.Panel.create(container)
        .configure({
            panelClass: ["hrcms-form-container"]
        });
        var formSplitPanel = Hrcms.SplitLayout.create(formContainerPanel.getElement())
        .configure({
            mode: "vertical",
            part1Size: 46
        });
        var workspaceHeader = Hrcms.NavIndicator.create(formSplitPanel.getPart1Element());
        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);
        workspaceHeader.configure({
            goBack: config.goBack
        });
        var formContainer = formSplitPanel.getPart2Element();
        var mainFormPanel = Hrcms.Panel.create(formContainer).configure({
            panelClass: ["hrcms-record-main-form"]
        });
        var mainForm = mainFormPanel.getElement();

        tThis.getMainForm = function() {
            return mainForm;
        }

        tThis.buildGrid = function(gridHolder, tableDefineUrl, gridDataUrl, eventHandler) {
            var dataGrid = Hrcms.DataGrid.create({
                container: gridHolder
            });
            $.ajaxSetup({cache: false});
            $.getJSON(tableDefineUrl, function(table) {
                dataGrid.configure({
                    columns: table.columns,
                    event: eventHandler
                    ,
                    header_filter: function(column) {
                        // filter the ex field out, the "ex" fields like "pk", "fk" and etc.
                        if (column.field_type === "ex")
                            return false;
                        return true;
                    }
                    // TODO add sort function
                });
                $.getJSON(gridDataUrl, function(record) {
                    dataGrid.data(record);
                });
            }).fail(function(result) {
                if (Hrcms.debugEnabled)
                    console.log(result);
            });
            
        }

        // Utility function
        tThis.buildFormGrid = function(formsHolder, formHolderId, formTemplUrl, gridHolderId, tableDefineUrl, gridDataUrl) {
            $.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: formTemplUrl,
                dataType: "html",
                success: function(formTempl) {
                    $(formTempl).appendTo(formsHolder.find("li")).find("[field-name]").attr("disabled", true);
                    // Load data
                    var dataGrid = Hrcms.DataGrid.create({
                        container: formsHolder.find(gridHolderId)
                    });
                    $.ajaxSetup({cache: false});
                    $.getJSON(tableDefineUrl, function(table) {
                        var formHolder = formsHolder.find(formHolderId);
                        // Configure the form field label according the tabe json description
                        for (var i = 0; i < table.columns.length; ++i) {
                            var column = table.columns[i];
                            var fieldLabel = formHolder.find('td[field-label="' + column.field + '"]');
                            if (fieldLabel.length > 0)
                                fieldLabel.html(column.label);
                        }
                        dataGrid.configure({
                            columns: table.columns,
                            event: {
                                rowclick: function(event) {
                                    var tdList = $(this).find("td");
                                    tdList.each(function(index, td) {
                                        var fieldName = $(td).attr("field-name");
                                        formHolder.find('[field-name="' + fieldName + '"]').val($(td).find("div").html());
                                    });
                                }
                            },
                            header_filter: function(column) {
                                // filter the ex field out, the "ex" fields like "pk", "fk" and etc.
                                if (column.field_type === "ex")
                                    return false;
                                return true;
                            }
                            // TODO add sort function
                        });
                        $.getJSON(gridDataUrl, function(record) {
                            dataGrid.data(record);
                        });
                    }).fail(function(result) {
                        if (Hrcms.debugEnabled)
                            console.log(result);
                    });
                }
            });
        }
        tThis.remove = function() {
            mainFormPanel.remove();
            formContainerPanel.remove();
        }
        // end
        return tThis;
    }
});
