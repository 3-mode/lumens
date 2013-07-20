$(function() {
    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var theLayout = Hrcms.SplitLayout.create(container)
        .configure({
            mode: "horizontal",
            part1Size: 180
        });
        /*
         var htmlTpl =
         '<div class="SplitterPane layout-content splitter-pane-container" style="overflow:hidden;">' +
         '  <div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 160px; height: 100%;"/>' +
         '  <div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>' +
         '</div>'
         var splitterContainer = $(htmlTpl).appendTo(contentContainer);
         splitterContainer.splitter({
         splitVertical: true,
         sizeLeft: true
         });
         var leftContainer = tThis.leftContainer = splitterContainer.find('#LeftPane');
         var rightContainer = tThis.rightContainer = splitterContainer.find('#RightPane');*/
        var leftContainer = Hrcms.Panel.create(theLayout.getPart1())
        .configure({
            panelClass: ["hrcms-menu-container"],
            panelStyle: {width: "100%", height: "100%"}
        });
        var rightContainer = theLayout.getPart2();
        tThis.rightContentContainer = rightContainer;
        /*rightContainer.bind("resize", function(event) {
         if (event.target !== this)
         return;
         rightContentContainer.trigger("resize");
         });*/
        var layoutNavMenu = function(parentContainer) {
            return Hrcms.NavMenu.create({
                container: parentContainer,
                width: "100%",
                height: "auto"
            });
        }
        tThis.remove = function() {
            theLayout.remove();
        }
        tThis.initialize = function() {
            var menu = layoutNavMenu(leftContainer.getElement());
            tThis.loadLeftNavMenu(menu);
            return this;
        }
        return tThis;
    }
});