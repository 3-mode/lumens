$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    Hrcms.MessageView = {};
    Hrcms.MessageView.create = function(container) {
        var tThis = Hrcms.ContentView.create(container);

        // end
        return tThis;
    }
});
