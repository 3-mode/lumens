$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var layoutContent = function(parentContainer) {
            return $('<div class="hrcms-content-constainer"/>').appendTo(parentContainer);
        }
        var contentContainer = layoutContent(container);
        var htmlTpl =
        '<div class="SplitterPane layout-content splitter-pane-container" style="overflow:hidden;">' +
        '  <div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 200px; height: 100%;"/>' +
        '  <div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>' +
        '</div>'
        var splitterContainer = $(htmlTpl).appendTo(contentContainer);
        splitterContainer.splitter({
            splitVertical: true,
            sizeLeft: true
        });
        var leftContainer = tThis.leftContainer = splitterContainer.find('#LeftPane');
        var rightContainer = tThis.rightContainer = splitterContainer.find('#RightPane');
        var rightContentContainer = tThis.rightContentContainer = $('<div class="hrcms-workspace-container"/>').appendTo(rightContainer);
        rightContainer.bind("resize", function(event) {
            if (event.target !== this)
                return;
            rightContentContainer.trigger("resize");
        });
        var layoutNavMenu = function(parentContainer) {
            return Hrcms.NavMenu.create({
                container: parentContainer,
                width: "100%",
                height: "auto"
            });
        }
        tThis.remove = function() {
            rightContainer.unbind();
            contentContainer.remove();
        }
        tThis.initialize = function() {
            var menu = layoutNavMenu(leftContainer);
            tThis.loadLeftNavMenu(menu);
        }
        return tThis;
    }
});