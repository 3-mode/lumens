$(function() {
    var SysModuleID = Hrcms.SysModuleID;
    Hrcms.DictForm = {};
    Hrcms.DictForm.create = function(config) {
        var tThis = Hrcms.BaseForm.create(config);
        var mainForm = tThis.getMainForm();
        var buildFormGrid = tThis.buildFormGrid;
        var buildGrid = tThis.buildGrid;
        // Member methods
        tThis.configure = function(config) {
            mainForm.append(config.personFormTempl);
            buildGrid(mainForm, "#dictListGrid", "data/test/dict_list.json", "rest/entities/dict");
            var list = Hrcms.List.create(mainForm.find("#dictForm"));
            list.configure({
                IdList: [
                    SysModuleID.ContentNavMenu_SystemManage_System_Dict
                ],
                titleList: [
                    "字典信息"
                ],
                activate: function(accordion, accordionTitle, isExpand) {
                    if (!isExpand)
                        return;
                    if (accordionTitle.attr("id") === SysModuleID.ContentNavMenu_SystemManage_System_Dict) {
                        buildFormGrid(accordion,
                        "#dictInfo", "app/html/dict/dict.html",
                        "#dictInfoGrid", "data/test/dict.json", "data/test/dummy.json");
                    }
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
