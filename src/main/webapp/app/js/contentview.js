$(function() {
    Hrcms.ContentView = {};
    Hrcms.ContentView.create = function(container) {
        var tThis = {};
        var theLayout = Hrcms.SplitLayout.create(container)
        .configure({
            mode: "horizontal",
            part1Size: 180
        });

        var leftPanel = Hrcms.Panel.create(theLayout.getPart1())
        .configure({
            panelClass: ["hrcms-menu-container"],
            panelStyle: {width: "100%", height: "100%"}
        });

        var rightPanel = Hrcms.Panel.create(theLayout.getPart2())
        .configure({
            panelClass: ["hrcms-workspace-container"],
            panelStyle: {width: "100%", height: "100%"}
        }).getElement();

        // Memeber functions
        tThis.getLeftPanel = function() {
            return leftPanel;
        }

        tThis.getRightPanel = function() {
            return rightPanel;
        }

        function createNavMenu(parentContainer) {
            return Hrcms.NavMenu.create({
                container: parentContainer,
                width: "100%",
                height: "auto"
            });
        }

        tThis.remove = function() {
            theLayout.remove();
        }

        tThis.initialize = function(buildNavMenuHandler) {
            var menu = createNavMenu(leftPanel.getElement());
            buildNavMenuHandler(menu);
            return this;
        }

        // End
        return tThis;
    }
});