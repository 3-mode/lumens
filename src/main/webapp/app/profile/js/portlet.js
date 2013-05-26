$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.Portlet = {};
    Hrcms.Portlet.create = function(container) {
        var tThis = {};
        var htmlPortletContainer = $('<div class="hrcms-portlet-container"/>').appendTo(container);
        var htmlColumnTpl =
        '<div class="hrcms-portlet-column">' +
        '</div>';
        var portletTpl =
        '   <div class="hrcms-portlet">' +
        '       <div class="hrcms-portlet-header">Feeds</div>' +
        '       <div class="hrcms-portlet-content">Lorem ipsum dolor sit amet, consectetuer adipiscing elit</div>' +
        '   </div>';

        var portletsColumns = [];
        portletsColumns.push($(htmlColumnTpl).appendTo(htmlPortletContainer));
        portletsColumns.push($(htmlColumnTpl).appendTo(htmlPortletContainer));
        portletsColumns.push($(htmlColumnTpl).appendTo(htmlPortletContainer));
        var portlets = [];
        portlets.push($(portletTpl).appendTo(portletsColumns[0]));
        portlets.push($(portletTpl).appendTo(portletsColumns[1]));
        portlets.push($(portletTpl).appendTo(portletsColumns[2]));
        for (var i = 0; i < portletsColumns.length; ++i)
            portletsColumns[i].sortable({
                connectWith: ".hrcms-portlet-column"
            });
        for (var i = 0; i < portlets.length; ++i)
            portlets[i].addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .find(".hrcms-portlet-header")
            .addClass("ui-widget-header ui-corner-all")
            .prepend("<span class='ui-icon ui-icon-minusthick'></span>")
            .end()
            .find(".hrcms-portlet-content");

        $(".hrcms-portlet-header .ui-icon").click(function() {
            $(this).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
            $(this).parents(".hrcms-portlet:first").find(".hrcms-portlet-content").toggle();
        });

        $(".hrcms-portlet-column").disableSelection();
        tThis.remove = function() {
            htmlPortletContainer.remove();
        }
        // end
        return tThis;
    }
});