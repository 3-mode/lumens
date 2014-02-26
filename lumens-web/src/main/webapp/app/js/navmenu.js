/**
 * Parameter Example:
 {
 container: __this.leftPanel.getElement(),
 width: "100%",
 height: "auto",
 itemSelected: true
 }
 */
Lumens.NavMenu = Class.$extend({
    __init__: function(config) {
        var menuContainer = this.menuContainer = $('<div class="lumens-secondary-menu-container"/>').appendTo(config.container);
        var menuContainer = this.menuContainer.append('<div id="place-holder" class="lumens-menu-placeholder"/>');
        var nav = this.nav = $('<div class="lumens-secondary-menu"/>').appendTo(menuContainer);
        nav.css("width", config.width);
        nav.css("height", config.height);
        var activeItem = null;
        var configuration = null;
        var Section = Class.$extend({
            __init__: function(sectionName) {
                var section = $('<div/>').appendTo(nav);
                var sectionTitle = $('<div class="lumens-secondary-menu-section"></div>').appendTo(section);
                sectionTitle.html(sectionName);
                var sectionContent = $('<ul class="lumens-secondary-submenu"/>').appendTo(section);
                var items = [];
                //Operation functions
                this.addItem = function(itemName) {
                    var item = $('<li><a><img/><span class="lumens-secondary-menu-text"/></a></li>').appendTo(sectionContent);
                    if (config.itemSelected) {
                        item.on('click', function(event, ui) {
                            if (activeItem !== null)
                                activeItem.toggleClass('lumens-h-active');
                            activeItem = $(this);
                            activeItem.toggleClass('lumens-h-active');
                        });
                    }
                    var itemTitle = item.find('span');
                    itemTitle.html(itemName);
                    items.push(item);
                    return item;
                };
            }
        });
        function clickCallBack(event) {
            menuContainer.trigger(jQuery.Event(configuration.eventType, {
                module_id: $(this).attr("module-id"),
                name: $(this).find('span').html()
            }));
        }
        this.onItemClick = function(callback) {
            menuContainer.on(configuration.eventType, callback);
        };
        this.configure = function(config) {
            configuration = config;
            var sections = config.sections;
            for (var i = 0; i < sections.length; ++i) {
                var section = new Section(sections[i].name);
                var items = sections[i].items;
                for (var j = 0; j < items.length; ++j) {
                    var item = section.addItem(items[j].name);
                    item.attr("module-id", items[j].module_id).on('click', clickCallBack);
                    item.find('img').attr('src', 'data:image/png;base64,' + items[j].item_icon);
                }
            }
        }
    }
});