$(function() {
    var SysModuleID = Hrcms.SysModuleID;
    var I18N = Hrcms.I18N;
    Hrcms.DictForm = {};
    Hrcms.DictForm.create = function(config) {
        var tThis = Hrcms.BaseForm.create(config);
        var mainForm = tThis.getMainForm();
        var buildFormGrid = tThis.buildFormGrid;
        var buildGrid = tThis.buildGrid;
        // Member methods
        var part1Panel, part2Panel, list;
        tThis.configure = function(config) {
            part1Panel = Hrcms.Panel.create(mainForm)
            .configure({
                panelStyle: {"float": "left", "width": "400px", "height": "100%", "min-height": "450px", "border-left": "1px solid #dddddd", "border-right": "1px solid #dddddd"}
            });
            buildGrid(part1Panel.getElement(), "data/test/dict_list.json", "rest/entities/dictlist", {
                rowclick: function() {
                    var dictName = $($(this).find('td[field-name="TABLE_NAME"]')[0]).find('div').html();
                    if (list)
                        list.remove();
                    list = Hrcms.List.create(part2Panel.getElement())
                    .configure({
                        IdList: [
                            SysModuleID.ContentNavMenu_SystemManage_System_Dict
                        ],
                        titleList: [
                            dictName
                        ],
                        activate: function(accordion, accordionTitle, isExpand) {
                            if (!isExpand)
                                return;
                            if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_SystemManage_System_Dict) {
                                buildFormGrid(accordion,
                                "#dictInfo", "app/html/dict/dict.html",
                                "#dictInfoGrid", "rest/entities/dictcolumns/" + dictName, "rest/entities/dict/" + dictName);
                            }
                        }
                    });
                }
            });
            part2Panel = Hrcms.Panel.create(mainForm)
            .configure({
                panelStyle: {"float": "left", "width": "400px", "height": "100%", "padding-left": "10px"}
            });
        }
        tThis.remove = function() {
            part1Panel.remove();
            part2Panel.remove();
        }
        // end
        return tThis;
    }
});
