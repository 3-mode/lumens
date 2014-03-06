Lumens.List = Class.$extend({
    __init__: function(container) {
        var accordionHolder = $('<div class="lumens-accordion"></div>').appendTo(container);

        function resizeHandler(e) {
            if (e && e.target !== this)
                return;
            accordionHolder.find(".lumens-accordion-content").trigger("resize");
        }
        container.resize(resizeHandler);

        var sectionTempl =
        '<div class="lumens-accordion-item">' +
        '  <div class="lumens-accordion-title">' +
        '    <div style="padding-top: 5px; padding-left: 10px;">' +
        '      <div class="ui-icon lumens-accordion-icon lumens-accordion-icon-collapse"></div>' +
        '      <b></b>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="lumens-accordion-content"><li></li></ul>' +
        '</div>';


        function doExpandCollapse(accordion) {
            accordion.find("ul").toggle(300);
            accordion.find(".lumens-accordion-icon")
            .toggleClass("lumens-accordion-icon-expand")
            .toggleClass("lumens-accordion-icon-collapse");
        }
        function loadForm(config, accordion) {
            if (config.activate)
                config.activate(accordion, accordion.find(".lumens-accordion-title"), accordion.find(".lumens-accordion-icon").hasClass("lumens-accordion-icon-expand"));

            /*$.ajaxSetup({cache: false});
             $.ajax({
             type: "GET",
             url: config.formURLList[i],
             dataType: "html",
             success: function(formTempl) {
             $(formTempl).appendTo(accordion.find("li"));
             // Load data
             
             }
             });*/
        }
        function addAccordion(config, i) {
            var accordion = $(sectionTempl).appendTo(accordionHolder);
            accordion.find("b").html(config.titleList[i]);
            var accordionTitle = accordion.find(".lumens-accordion-title");
            var form = accordion.find("ul");
            form.toggle(100);
            accordionTitle.attr("id", config.IdList[i])
            .click(function(event) {
                doExpandCollapse(accordion);
                // Load data
                if (accordion.find("li").children().length === 0 && config.activate) {
                    if (config.activate)
                        config.activate(accordion, accordion.find(".lumens-accordion-title"),
                        accordion.find(".lumens-accordion-icon").hasClass("lumens-accordion-icon-expand"));
                }

            });
        }
        tThis.configure = function(config) {
            for (var i = 0; i < config.titleList.length; ++i) {
                addAccordion(config, i);
            }
            var accordions = accordionHolder.find(".lumens-accordion-item");
            if (accordions.length) {
                var accordion = $(accordions[0]);
                doExpandCollapse($(accordions[0]));
                if (config.activate)
                    config.activate(accordion, accordion.find(".lumens-accordion-title"),
                    accordion.find(".lumens-accordion-icon").hasClass("lumens-accordion-icon-expand"));

            }

            return this;
        }

        tThis.remove = function() {
            accordionHolder.unbind();
            accordionHolder.remove();
            container.unbind("resize", resizeHandler);
        }
        // end
        return tThis;
    }
});


