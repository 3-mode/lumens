$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var I18N = Hrcms.I18N;
   
    Hrcms.create = function(containerObj) {
        var tThis = {};
        var rootContainer = containerObj;
        var headerContainer = null;
        rootContainer.addClass("hrcms");
        function layoutHeader(parentContainer) {
            return $('<div class="hrcms-header-constainer"/>').appendTo(parentContainer);
        }
        tThis.load = function() {
            console.log("Loading HRCMS !");
            headerContainer = layoutHeader(rootContainer);
            /** Header begin */
            Hrcms.Header.create(headerContainer).setSysTitle(I18N.SystemTitle);
            function clickToolbarButton(event) {
                console.log($(this).find('span').html());
                // TODO switch to another view here
            }
            // TODO use ajax to load the toolbar button strings
            var navToolbar = Hrcms.NavToolbar.create(headerContainer);
            Hrcms.SysToolbar_Config.event_callback = clickToolbarButton;
            navToolbar.configure(Hrcms.SysToolbar_Config);
            // TODO Begin page workspace building here, default is home page
            var view = Hrcms.ContentView.create(rootContainer);
        }
        // end
        return tThis;
    }
});