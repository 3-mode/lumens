$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.InfoManageView = {};
    Hrcms.InfoManageView.create = function(config) {
        var tThis = ContentView.create(config);

        // end
        return tThis;
    }
});