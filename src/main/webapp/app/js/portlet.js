$(function() {
    Hrcms.Portlet = {};
    Hrcms.Portlet.create = function(config) {
        var tThis = {};
        var container = config.container;
        var portletRootContainer = $('<div class="hrcms-portlet-root-container"><table class="hrcms-portlet-frame-container">' +
        '<tr style="height: 10px;"><td style="height:10px;width:10px;"></td><td style="height:10px;"></td></tr>' +
        '<tr><td style="width:10px;"></td><td><div class="hrcms-portlet-container"/></td>' +
        '</table></div>').appendTo(container);
        var portletContainer = portletRootContainer.find(".hrcms-portlet-container");

        var htmlColumnTpl =
        '<div class="hrcms-portlet-column">' +
        '</div>';
        var portletTpl =
        '   <div class="hrcms-portlet hrcms-portlet-minheight">' +
        '       <div class="hrcms-portlet-header"></div>' +
        '       <div class="hrcms-portlet-content"></div>' +
        '   </div>';

        // Member methods
        tThis.configure = function(config) {
            var column = parseInt(config.column);
            var portletsColumns = [];
            for (var i = 0; i < column; ++i)
                portletsColumns.push($(htmlColumnTpl).appendTo(portletContainer));
            for (var i = 0; i < portletsColumns.length; ++i)
                portletsColumns[i].sortable({
                    containment: portletContainer,
                    connectWith: ".hrcms-portlet-column"
                });
            var messages = config.messages;
            var portlets = [];
            if (messages) {
                for (var i = 0; i < messages.length; ++i) {
                    var portlet = $(portletTpl).appendTo(portletsColumns[i % column]);
                    portlet.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".hrcms-portlet-header").html(messages[i].title)
                    .addClass("ui-corner-all")
                    .prepend("<span class='hrcms-portlet-icon'></span>")
                    .prepend("<span class='ui-icon ui-icon-minusthick'></span>")
                    .end()
                    .find(".hrcms-portlet-content").html(messages[i].content);
                    portlets.push(portlet);
                }
            }
            portletContainer.find(".hrcms-portlet-header .ui-icon").click(function() {
                $(this).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
                var portlet = $(this).parents(".hrcms-portlet:first");
                portlet.toggleClass("hrcms-portlet-minheight");
                portlet.find(".hrcms-portlet-content").toggle();
            });
            portletContainer.find(".hrcms-portlet-column").disableSelection();
        }
        tThis.remove = function() {
            portletRootContainer.remove();
        }
        // end
        return tThis;
    }
});