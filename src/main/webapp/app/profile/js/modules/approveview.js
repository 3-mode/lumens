$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.ApproveView = {};
    Hrcms.ApproveView.create = function(config) {
        var tThis = ContentView.create(config);

        // end
        return tThis;
    }
});
