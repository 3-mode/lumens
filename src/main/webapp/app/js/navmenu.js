$(function() {
    Hrcms.NavMenu = {};
    Hrcms.NavMenu.create = function(config) {
        var tThis = {};
        var menuContainer = $('<div class="hrcms-secondary-menu-container"/>').appendTo(config.container);
        menuContainer.append('<div id="place-holder" style="width:100%;height:10px;float:top;"/>');
        var nav = $('<div class="hrcms-secondary-menu"/>').appendTo(menuContainer);
        nav.css("width", config.width);
        nav.css("height", config.height);
        var activeItem = null;
        var configuration = null;
        var Section = {};
        Section.create = function(sectionName) {
            var tThis = {};
            var section = $('<div/>').appendTo(nav);
            var sectionTitle = $('<div class="hrcms-secondary-menu-section"></div>').appendTo(section);
            sectionTitle.html(sectionName);
            var sectionContent = $('<ul class="hrcms-secondary-submenu"/>').appendTo(section);
            //Operation functions
            tThis.addItem = function(itemName) {
                var item = $('<li><a><span class="hrcms-secondary-menu-text"/></a></li>').appendTo(sectionContent);
                item.on('click', function(event, ui) {
                    if (activeItem !== null)
                        activeItem.toggleClass('hrcms-h-active');
                    activeItem = $(this);
                    activeItem.toggleClass('hrcms-h-active');
                });
                var itemTitle = item.find('span');
                itemTitle.html(itemName);
                return item;
            }
            //end
            return tThis;
        }
        function clickCallBack(event) {
            menuContainer.trigger(jQuery.Event(configuration.eventType, {
                moduleID: $(this).attr("module-id"),
                moduleName: $(this).find('span').html()
            }));
        }
        tThis.onItemClick = function(callback) {
            menuContainer.on(configuration.eventType, callback);
        }
        tThis.configure = function(config) {
            configuration = config;
            var sections = config.sections;
            for (var i = 0; i < sections.length; ++i) {
                var section = Section.create(sections[i].title);
                var items = sections[i].items;
                for (var j = 0; j < items.length; ++j) {
                    var item = section.addItem(items[j].title);
                    item.attr("module-id", items[j].moduleID);
                    item.on('click', clickCallBack);
                }
            }
        }
        //end
        return tThis;
    }
});