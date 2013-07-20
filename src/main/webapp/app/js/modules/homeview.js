$(function() {
    Hrcms.HomeView = {};
    Hrcms.HomeView.create = function(container) {
        var tThis = {};
        var portlet = Hrcms.Portlet.create({
            container: container
        });
        $.ajaxSetup({cache: false});
        $.getJSON("data/test/home_info_data.json",
        function(data) {
            portlet.configure(data);
        }).fail(function(result) {
            if (Hrcms.debugEnabled)
                console.log(result.error());
        });

        tThis.remove = function() {
            portlet.remove();
        }
        // end
        return tThis;
    }
});
