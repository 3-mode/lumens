/**
 * Parameter Example:
 {
 container: __this.leftPanel.getElement(),
 width: "100%",
 height: "auto",
 item_selected: true
 }
 */
Lumens.NavMenu = Class.$extend({
    __init__: function(config) {
        this.$menuContainer = $('<div class="lumens-secondary-menu-container"/>').appendTo(config.container);
        this.$menuContainer.append('<div id="place-holder" class="lumens-menu-placeholder"/>');
        this.$nav = $('<div class="lumens-secondary-menu"/>').appendTo(this.$menuContainer);
        this.$nav.css("width", config.width);
        this.$nav.css("height", config.height);
        var __this = this;
        this.Section = Class.$extend({
            __init__: function(sectionName) {
                var section = $('<div/>').appendTo(__this.$nav);
                var sectionTitle = $('<div class="lumens-secondary-menu-section"></div>').appendTo(section);
                sectionTitle.html(sectionName);
                var sectionContent = $('<ul class="lumens-secondary-submenu"/>').appendTo(section);
                var items = [];
                //Operation functions
                this.addItem = function(itemName) {
                    var item = $('<li><a><img/><span class="lumens-secondary-menu-text"/></a></li>').appendTo(sectionContent);
                    if (config.item_selected) {
                        item.on('click', function(event, ui) {
                            if (__this.activeItem !== null)
                                __this.activeItem.toggleClass('lumens-h-menu-selected');
                            __this.activeItem = $(this);
                            __this.activeItem.toggleClass('lumens-h-menu-selected');
                        });
                    }
                    var itemTitle = item.find('span');
                    itemTitle.html(itemName);
                    items.push(item);
                    return item;
                };
            }
        });
        this.clickCallBack = function(event) {
            __this.$menuContainer.trigger(jQuery.Event(__this.configuration.event_type, {
                module_id: $(this).attr("module-id"),
                name: $(this).find('span').html(),
                object: $(this)
            }));
        }
    },
    onItemClick: function(callback) {
        this.$menuContainer.on(this.configuration.event_type, callback);
        return this;
    },
    configure: function(config) {
        this.configuration = config;
        var sections = config.sections;
        for (var i = 0; i < sections.length; ++i) {
            var section = new this.Section(sections[i].name);
            var items = sections[i].items;
            for (var j = 0; j < items.length; ++j) {
                var item = section.addItem(items[j].name);
                item.attr("module-id", items[j].module_id).on('click', this.clickCallBack);
                if (items[j].item_icon)
                    item.find('img').attr('src', 'data:image/png;base64,' + items[j].item_icon);
                else if (items[j].item_icon_url)
                    item.find('img').attr('src', items[j].item_icon_url);
                // Store the item information in the item dom node
                $.data(item.get(0), "item-data", items[j]);
            }
        }
        return this;
    }
});