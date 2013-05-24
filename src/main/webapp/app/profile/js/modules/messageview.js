$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.MessageView = {};
    Hrcms.MessageView.create = function(config) {
        var tThis = ContentView.create(config);

        // end
        return tThis;
    }
});
