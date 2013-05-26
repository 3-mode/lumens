$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
    Hrcms.HomeView = {};
    Hrcms.HomeView.create = function(container) {
        var tThis = {};
        var portlet = Hrcms.Portlet.create(container);

        tThis.remove = function() {
            portlet.remove();
        }
        // end
        return tThis;
    }
});
