$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    Hrcms.ApproveView = {};
    Hrcms.ApproveView.create = function(config) {
        var tThis = Hrcms.ContentView.create(config);

        // end
        return tThis;
    }
});
