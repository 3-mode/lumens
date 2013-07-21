$(function() {
    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var theLayout = Hrcms.SplitLayout.create(container)
        .configure({
            mode: "horizontal",
            part1Size: 180
        });
        var leftContainer = Hrcms.Panel.create(theLayout.getPart1())
        .configure({
            panelClass: ["hrcms-menu-container"],
            panelStyle: {width: "100%", height: "100%"}
        });
        tThis.rightContentContainer = Hrcms.Panel.create(theLayout.getPart2())
        .configure({
            panelClass: ["hrcms-workspace-container"],
            panelStyle: {width: "100%", height: "100%"}
        }).getElement();
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