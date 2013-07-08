$(function() {
    Hrcms.ReportFactory = {};
    Hrcms.ReportFactory.create = function() {
        var tThis = {};
        function setEntityTableData(fieldList, entityHolder) {
            if (entityHolder.length !== 0) {
                for (var i = 0; i < fieldList.length; ++i) {
                    var field = fieldList[i];
                    var td = entityHolder.find('td[field-name="' + field.field_name + '"]');
                    if (td.length)
                        td.html(field.field_value);
                }
            }
        }
        function setRecordTableData(recordList, listHolder) {
            if (listHolder.length !== 0) {
                var entityHolder = listHolder.find('tr[is-record="true"]');
                for (var i = 1; i < recordList.length; ++i) {
                    entityHolder.clone().appendTo(listHolder);
                }
                var entityHolderList = listHolder.find('tr[is-record="true"]');
                for (var i = 0; i < recordList.length; ++i) {
                    var record = recordList[i];
                    setEntityTableData(record.field_list, $(entityHolderList[i]));
                }
            }
        }
        tThis.load = function(config) {
            if (Hrcms.debugEnabled) {
                console.log(config.contentHolder);
                console.log(config.reportTemplURL);
                console.log(config.reportDataURL);
            }
            $.ajaxSetup({cache: false});
            $.ajax({
                type: "GET",
                url: config.reportTemplURL,
                dataType: "html",
                success: function(reportTempl) {
                    var report = $(reportTempl).appendTo(config.contentHolder).removeClass("hrcms-table-holder-box");
                    $.getJSON(config.reportDataURL,
                    function(entities) {
                        if (Hrcms.debugEnabled) {
                            console.log(report);
                            console.log(entities);
                        }
                        // TODO fill data into report
                        var length = entities.length;
                        for (var i = 0; i < length; ++i) {
                            var entity = entities[i];
                            if (entity.is_list) {
                                var listTable = report.find('table[entity-name="' + entity.entity_name + '"][is-list="true"]');
                                var recordList = entity.record_list;
                                setRecordTableData(recordList, listTable);
                            } else {
                                var entityTable = report.find('table[entity-name="' + entity.entity_name + '"]');
                                var fieldList = entity.field_list;
                                setEntityTableData(fieldList, entityTable);
                            }
                        }
                    }).fail(function(result) {
                        if (Hrcms.debugEnabled) {
                            console.log(report);
                            console.log(result);
                            console.log("ERROR! when get report data ...")
                        }
                    });
                }
            });
        }
        // end
        return tThis;
    }
})
