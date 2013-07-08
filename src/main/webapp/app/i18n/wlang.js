$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    if (!Hrcms.I18N)
        Hrcms.I18N = {};
    if (!Hrcms.I18N.lang)
        Hrcms.I18N.lang = "zh_CN";
    var I18N = Hrcms.I18N;
    // System control widget modules
    I18N.Widget = {
        Button_Ok: "确定",
        Button_Cancel: "取消",
        Table_RowCount: "行数:",
        Table_ColCount: "列数:",
        Table_RowAbove: "上边",
        Table_RowBelow: "下边",
        Table_ColLeft: "左边",
        Table_ColRight: "右边",
        TableNavbar_Prev: "前一页",
        TableNavbar_Next: "下一页",
        TableNavbar_First: "最前页",
        TableNavbar_Last: "最后页",
        TableNavbar_New: "新建",
        TableNavbar_Remove: "删除",
        TableNavbar_Refresh: "刷新",
        TableNavbar_Search: "搜索",
        TableAdding: "新建表格",
        TableRowAdding: "添加表行",
        TableCellJoin: "表格合并",
        TableCellSettings: "表单元格设置",
        TableCellText: "表单元格文本",
        TextColor: "文本颜色",
        FontSize: "文本字体大小",
        FontStyle: "文本字体风格",
        BackgroundColor: "背景颜色",
        Border: "边框"
    };
});