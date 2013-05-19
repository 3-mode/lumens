/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 * Author     : shaofeng wang (shaofeng.cjpw@gmail.com)
 */
var Lumens = {
    version: 1.0
};

Lumens.Tabbar = {};
Lumens.Tabbar.create = function(parentObj) {
    var tThis = {};
    var tabItems = [];
    var activeIndex = -1;

    tThis.addTabTitles = function(tabTitles) {
        function addTab(tabTitle) {
            tabItems.push({
                tab: $('<div class="header-tab-button" style="opacity: .5;"/>'),
                title: $('<div style="padding-top: 5px;"/>')
            });
            var tabItem = tabItems[tabItems.length - 1];
            tabItem.tab.prependTo(parentObj);
            tabItem.title.appendTo(tabItem.tab);
            tabItem.title.html(tabTitle);
            tabItem.title.get(0).myIndex = tabItems.length - 1;
            tabItem.getTabTitle = function() {
                return tabItem.title.html();
            }
            tabItem.setTabTitle = function(title) {
                tabItem.title.html(title);
            }
            tabItem.addEvent = function(event, evtFunction) {
                tabItem.title.bind(event, evtFunction);
            }
            tabItem.setActive = function(active) {
                tabItem.tab.css("opacity", active ? "1" : ".5");
                activeIndex = this.title.get(0).myIndex;
            }
            tabItem.title.bind("click", function() {
                if (activeIndex >= 0) {
                    var active = tabItems[activeIndex];
                    active.setActive(false);
                }
                activeIndex = $(this).get(0).myIndex;
                active = tabItems[activeIndex];
                active.setActive(true);
            })
        }
        for (var i = 0; i < tabTitles.length; ++i)
            addTab(tabTitles[i]);
    }

    tThis.getTab = function(index) {
        return tabItems[index];
    }

    tThis.getTabCount = function() {
        return tabItems.length;
    }

    return tThis;
}

// Initialize the header class component first
Lumens.Header = {};
Lumens.Header.create = function(myId, parentObj) {
    var tThis = {};
    var headerId = myId;
    var parent = parentObj;
    var headerDiv = $('<div id="' + myId + '" class="layout-header"/>');
    headerDiv.appendTo(parent);
    headerDiv.append('<div class="header-logo"><div class="header-logo-text">LUMENS<div class="logo-bag"/></div></div>');
    tThis.getId = function() {
        return headerId;
    }
    tThis.getHtmlElement = function() {
        return headerDiv;
    }
    return tThis;
}

// Initialize the body class component
Lumens.Accordian = {};
Lumens.Accordian.create = function(parentObj, titleText, accordionIdText, itemObjList) {
    var tThis = {};
    var parent = parentObj;
    var itemList = null;
    var title = titleText;
    var accordionId = accordionIdText;
    var itemPrefix = accordionIdText + '-item';
    var itemCount = itemObjList.length;

    tThis.itemCount = function() {
        return itemCount;
    }

    tThis.item = function(index) {
        return itemList.find('#' + itemPrefix + index);
    }

    // Build container
    parent.append('<div id="' + accordionId + '"><div class="accordian-item-title">' + title + '</div><ul style="margin: 0px; padding: 0px"/></div>');
    var accordianElement = $('#' + accordionId);
    accordianElement.addClass('accordian');
    var ul = accordianElement.find('ul');
    itemList = ul;
    // Build all item of the accordian
    for (var i = 0; i < itemObjList.length; ++i) {
        var itemHtml = '<li><div style="padding-top: 5px; position: absolute;">' + itemObjList[i] + '</div></li>'
        + '<li><div id="' + itemPrefix + i + '" style="width: 100%; height:100%"/></li>';
        ul.append(itemHtml);
    }
    // Hide all the content except the first
    var liodd = ul.find('li:odd');
    var lieven = ul.find('li:even');
    liodd.hide();

    // Add a padding to the first link
    var expand = ul.find('li:first-child');
    expand.animate({
        "padding-left": "30px"
    });
    expand.toggleClass('item-active');
    expand.next().stop().slideToggle(300);

    // Add the dimension class to all the content
    liodd.addClass('dimension');

    // Set the even links with an 'even' class
    ul.find('li:even:even').addClass('even');

    // Set the odd links with a 'odd' class
    ul.find('li:even:odd').addClass('even');

    // Show the correct cursor for the links
    lieven.css({
        'cursor': 'pointer',
        'padding-left': '10px'
    });

    // Handle the click event
    lieven.click(function() {
        // Get the content that needs to be shown
        var cur_section = $(this);
        var cur = cur_section.next();

        // Get the content that needs to be hidden
        var old_section = ul.find('li.item-active');
        var old = old_section.next();

        // Make sure the content that needs to be shown
        // isn't already visible
        if (cur_section.is('.item-active'))
            return;

        old_section.toggleClass('item-active');
        cur_section.toggleClass('item-active');

        // Hide the old content
        old.slideToggle(300);

        // Show the new content
        cur.stop().slideToggle(300);

        // Animate (add) the padding in the new link
        $(this).stop().animate({
            paddingLeft: "30px"
        });

        // Animate (remove) the padding in the old link
        old_section.stop().animate({
            paddingLeft: "10px"
        });
    });

    return tThis;
}

Lumens.ComponentTree = {};
Lumens.ComponentTree.create = function(parentObj, strTreeIdText, strDragDropParentId) {
    var tThis = {};
    var parent = parentObj;
    var strTreeId = strTreeIdText;

    parent.append('<div id="' + strTreeId.substring(1) + '"/>');
    var dsTree = tThis.dsTree = parent.find(strTreeId);
    _canLog = false;
    tThis.loadData = function() {
        // Make the data loading as a json
        $.getJSON("./json/ds.json", function(json) {
            dsTree.dynatree({
                onCreate: function(node) {
                    if (!node.data.isFolder) {
                        var clone_obj = $(node.span);
                        function item_clone() {
                            var cloned = clone_obj.clone();
                            cloned.css({
                                "z-index": 1000
                            });
                            return cloned;
                        }
                        clone_obj.draggable({
                            appendTo: strDragDropParentId,
                            helper: item_clone
                        });
                    }
                },
                persist: true,
                children: json.ds,
                debugLevel: 0
            });
        });
    }

    return tThis;
}

Lumens.ComponentPane = {};
Lumens.ComponentPane.create = function(holder, width, height) {
    var tThis = {};
    var COMP_List = [];
    var currentDraggableLinkNode = null;
    holder.append('<div id="holderElement"/>');
    var holderElement = holder.find("#holderElement");
    var svgHolderElement = d3.select("#holderElement");

    holderElement.attr({
        style: "height:" + height + ";width:" + width + ";overflow:auto;position:absolute;"
    });
    holder.droppable({
        accept: ".component-node",
        drop: function(event, ui) {
            event.preventDefault();
            var paneOffset = $(this).offset();
            var offset = ui.helper.offset();
            tThis.addComponent(
            {
                name: ui.helper.find('a').html(),
                label: "Untitled " + COMP_List.length,
                x: offset.left - paneOffset.left,
                y: offset.top - paneOffset.top
            });
        }
    });

    tThis.getComponentList = function() {
        return COMP_List;
    }

    tThis.removeAll = function() {
        svgHolderElement.selectAll('svg').remove();
        holderElement.find(".component").remove();
    }

    tThis.addComponent = function(args) {
        return addComponentV1(args);
    }

    function addComponentV1(args) {
        var name = args.name;
        var label = args.label;
        var x = args.x;
        var y = args.y;
        var status_disconnect_icon = "lumens/images/status/16/disconnect.png";
        var status_connect_icon = "lumens/images/status/16/connect.png";
        var component_icon = args.component_icon !== undefined ? args.component_icon : "lumens/images/component/" + name.toLowerCase() + ".png";
        var ComponentBase = {};
        ComponentBase.create = function(domObj) {
            var tThis = {};
            var links = tThis.links = [];
            var dom = tThis.dom = domObj;
            tThis.setPosition = function(x, y) {
                dom.css("left", x + "px");
                dom.css("top", y + "px");
            }
            tThis.getPosition = function() {
                var px = dom.css("left");
                var py = dom.css("top");
                if (px === "")
                    px = "0px";
                if (py === "")
                    py = "0px";
                return {
                    x: parseFloat(px.substring(0, px.length - 2)),
                    y: parseFloat(py.substring(0, py.length - 2))
                };
            }
            tThis.getSize = function() {
                return  {
                    width: dom.width(),
                    height: dom.height()
                };
            }

            dom.on("drag", function(event, ui) {
                for (var i = 0; i < links.length; ++i)
                    links[i].update();
            });
            return tThis;
        }

        var Component = {};
        Component.create = function() {
            var width_constant = 140; // Default value
            var height_constant = 57; // Default value
            var componentInstance = $('<div class="component"><table border="0" class="component-layout">'
            + '<tr><td>'
            + '<div class="component-title-container"><table id="component-title" border="0"><tr>'
            + '<td><img id="component-status-img"/></td>'
            + '<td><div id="component-title-text"></div></td>'
            + '<td><div id="component-link-img" class="component-link-img-normal"/></td>'
            + '</tr></table></div>'
            + '</td></tr>'
            + '<tr><td>'
            + '<div id="component-body"><img id="component-img"/><div id="component-lablel"></div></div>'
            + '</td></tr></table></div>'
            );
            componentInstance.appendTo(holderElement);
            //============================================================
            // Initialize this object to expand the position parent class
            var tThis = ComponentBase.create(componentInstance);
            var links = tThis.links;
            var virtualLinkObj = null;
            //============================================================
            var compTitle = componentInstance.find('#component-title');
            var compStatusImg = componentInstance.find("#component-status-img");
            var compImg = componentInstance.find("#component-img");
            var compLinkImg = componentInstance.find("#component-link-img");
            var compTitleText = componentInstance.find("#component-title-text");
            var compLabel = componentInstance.find("#component-lablel");
            compStatusImg.attr("src", status_disconnect_icon);
            compImg.attr("src", component_icon);
            compTitleText.text(name);
            compLabel.text(label);
            componentInstance.droppable({
                drop: function(event, ui) {
                    event.preventDefault();
                    if (!ui.helper.hasClass("component-link-node"))
                        return;
                    var draggable = ui.helper.data("draggable");
                    if (draggable === tThis)
                        return;
                    draggable.link(tThis);
                }
            });
            function linkDraggable() {
                var draggable = $('<div class="component-link-node"></div>');
                draggable.data("draggable", tThis);
                var draggableElem = ComponentBase.create(draggable);
                tThis.virtualLink(draggableElem);
                return draggable;
            }
            compLinkImg.draggable({
                appendTo: holderElement,
                helper: linkDraggable
            });
            width_constant = componentInstance.width();
            height_constant = componentInstance.height();
            componentInstance.draggable({
                disabled: false,
                scroll: true,
                handle: compTitle,
                stack: ".component"
            });
            //Initailize the SVG object for links
            var line = d3.svg.line()
            .x(function(d) {
                return d.x;
            })
            .y(function(d) {
                return d.y;
            })
            .interpolate("linear");
            tThis.link = function(t) {
                var thisSVG = svgHolderElement.append("svg")
                .style({
                    "position": "absolute",
                    "z-index": "-100"
                });
                var link = {
                    source: tThis,
                    target: t,
                    L: thisSVG.append("svg:path"),
                    update: function() {
                        var s = this.source.getPosition();
                        var t = this.target.getPosition();
                        var size = this.target.getSize();
                        var svg_xy = {};
                        svg_xy.left = s.x < t.x ? (s.x + size.width / 2) - 5 : (t.x + size.width / 2) - 5;
                        svg_xy.top = s.y < t.y ? (s.y + size.height / 2) - 5 : (t.y + size.height / 2) - 5;
                        svg_xy.width = (Math.abs(s.x - t.x)) + 10;
                        svg_xy.height = (Math.abs(s.y - t.y)) + 10;
                        thisSVG.style({
                            "left": svg_xy.left + "px",
                            "top": svg_xy.top + "px"
                        })
                        .attr({
                            "width": svg_xy.width,
                            "height": svg_xy.height
                        });
                        this.L.attr("d", line(Lumens.Utils.buildPathV1(s, t, size, svg_xy)));
                        return this;
                    }
                }
                links.push(link);
                t.links.push(link);
                link.L.style({
                    "stroke-width": .5,
                    "stroke": "rgb(170, 170, 170)",
                    "fill": "none"
                });
                return link.update();
            }

            tThis.virtualLink = function(virtualObj) {
                this.link(virtualObj);
                virtualLinkObj = virtualObj;
            }
            //========End===============================
            return tThis;
        }
        var component = Component.create();
        component.setPosition(x, y);
        COMP_List.push(component);
        return component;
    }

    function addComponentV0(args) {
        var name = args.name;
        var label = args.label;
        var x = args.x;
        var y = args.y;
        var status_icon = args.status_icon !== undefined ? args.status_icon : "lumens/images/status/16/disconnect.png";
        var component_icon = args.component_icon !== undefined ? args.component_icon : "lumens/images/component/" + name.toLowerCase() + ".png";

        var Component = {};
        Component.create = function(SVG, name) {
            var tThis = {};
            var width_constant = 140;
            var height_title_constant = 25;
            var height_body_constant = 32;
            var height_constant = height_title_constant + height_body_constant;
            var parentSVG = SVG;
            var thisG = parentSVG.append('svg:g');
            var statusImg = null;
            var links = tThis.links = [];

            //======================================Init========================================
            // Build a demo draggable rect
            var draggable = false;
            function updateSVG(x, y) {
                var width = Number(SVG.attr("width"));
                var height = Number(SVG.attr("height"));
                var v = x + width_constant * 2;
                if (width < v)
                    SVG.attr("width", v);
                v = y + height_constant * 2;
                if (height < v)
                    SVG.attr("height", v);
            }
            function dragmove(d) {
                if (!draggable)
                    return;
                d.x += d3.event.dx;
                d.y += d3.event.dy;
                updateSVG(d.x, d.y);
                thisG.attr("transform", "translate(" + d.x + "," + d.y + ")");
                for (var i = 0; i < links.length; ++i)
                    links[i].update();
            }
            // Build composition component UI unit
            thisG.call(d3.behavior.drag()
            .on("dragstart", function(d) {
                // Move the G to front
                this.parentNode.appendChild(this);
                // event handling
                var y = d3.event.sourceEvent.layerY;
                var x = d3.event.sourceEvent.layerX;
                var y_boundary = d.y + height_title_constant;
                var x_boundary = d.x + (width_constant - 20);
                if (y > y_boundary || (y < y_boundary && x > x_boundary))
                    draggable = false;
                else
                    draggable = true;
            })
            .on("drag", dragmove));
            // Body
            thisG.append('svg:rect')
            .attr({
                "height": height_body_constant,
                "width": width_constant,
                "y": height_title_constant
            })
            .style({
                "fill": "rgb(236, 236, 236)",
                "stroke": "rgb(177, 177, 177)",
                "stroke-width": .3
            });

            // Title
            var titleBar = thisG.append('svg:rect')
            .attr({
                "height": height_title_constant,
                "width": width_constant
            })
            .style({
                "fill": "rgb(214, 214, 214)",
                "stroke": "rgb(177, 177, 177)",
                "stroke-width": .5
            });
            holderElement.droppable({
                accept: ".link-node",
                drop: function(event, ui) {
                    console.log("Stop");
                    console.log(this);
                }
            });

            statusImg = thisG.append("svg:image")
            .attr({
                "xlink:href": status_icon,
                "x": 8,
                "y": 4,
                "width": 16,
                "height": 16
            });

            var linkImg = thisG.append("svg:image")
            .attr({
                "xlink:href": function(n) {
                    function linkDraggable() {
                        // Cache the source component
                        currentDraggableLinkNode = tThis;
                        console.log("Start");
                        console.log(tThis);
                        return $('<div class="link-node"></div>');
                    }
                    $(this).draggable({
                        appendTo: holderElement,
                        helper: linkDraggable,
                        stop: function(event, ui) {
                            console.log(tThis);
                        }
                    });
                    return "lumens/images/component/link-inactive.png"
                },
                "x": width_constant - 20,
                "y": 5,
                "width": 14,
                "height": 14
            });
            linkImg
            .on("mouseout", function(e) {
                linkImg.attr("xlink:href", "lumens/images/component/link-inactive.png");
            })
            .on("mouseover", function(e) {
                linkImg.attr("xlink:href", "lumens/images/component/link.png");
            });

            thisG.append("svg:image")
            .attr({// TODO here need to refine to load the image dynamicly
                "xlink:href": component_icon,
                "x": 4,
                "y": 28,
                "width": 24,
                "height": 24
            });
            thisG.append("svg:text")
            .attr({
                "x": 36,
                "y": 18
            })
            .style({
                "font-size": "14px",
                "cursor": "move"
            })
            .text(name);

            thisG.append("svg:text")
            .attr({
                "x": 40,
                "y": 46
            })
            .style({
                "font-size": "12px",
                "overflow": "hidden",
                "cursor": "default"
            })
            .text(label);
            // TODO if the label is a long text, need to trim it as a correct length
            //======================================End=========================================
            tThis.getSize = function() {
                return {
                    width: width_constant,
                    height: height_constant
                };
            }
            tThis.link = function(c) {
                var s = tThis.getPosition();
                var t = c.getPosition();
                var size = tThis.getSize();
                var link = {
                    source: tThis,
                    target: c,
                    G: parentSVG.append('g'),
                    L: {},
                    update: function() {
                        var s = this.source.getPosition();
                        var t = this.target.getPosition();
                        var size = this.source.getSize();
                        this.L.attr("d", line(Lumens.Utils.buildPathV0(s, t, size)))
                    }
                };
                links.push(link);
                c.links.push(link);
                var line = d3.svg.line()
                .x(function(d) {
                    return d.x;
                })
                .y(function(d) {
                    return d.y;
                })
                .interpolate("linear");
                link.L = link.G.append("svg:path");
                link.L.attr("d", line(Lumens.Utils.buildPathV0(s, t, size)))
                .style({
                    "stroke-width": .5,
                    "stroke": "rgb(170, 170, 170)",
                    "fill": "none"
                });
                return link;
            }

            tThis.getPosition = function() {
                return thisG.datum();
            }
            tThis.setPosition = function(x, y) {
                updateSVG(x, y);
                thisG.data([{
                        x: x,
                        y: y
                    }])
                .attr("transform", function(d) {
                    return "translate(" + d.x + "," + d.y + ")";
                });
            }

            return tThis;
        }
        var component = Component.create(SVG, name, label);
        component.setPosition(x, y);
        COMP_List.push(component);
        return component;
    }

    return tThis;
}

Lumens.RuleTreeEditor = {
    version: 1.0
};
Lumens.RuleTreeEditor.create = function(args) {
    var RuleTreeEditorScrollBar = {};
    var _duration = 200;
    var _barHeight = 20;
    var _barWidth = 200;
    var _scrollbar_width = 10;
    /**
     * args = {
     *   _holderId: "#yourId",
     *   rightItemDropped: function(data_src, data_dst) {},
     *   rightItemSelected: function(data_dst) {},
     *   leftItemSelected: function(data_src) {}
     * }
     */
    var _rightItemDropped = args.rightItemDropped;
    var _rightItemSelected = args.rightItemSelected;
    var _leftItemSelected = args.leftItemSelected;
    var _holderId = args.holderId;
    var _d3Holder = d3.select(_holderId);
    var _domHolder = $(_d3Holder.node());

    RuleTreeEditorScrollBar.create = function(args) {
        var tThis = {};
        // private memeber of scrollbar class
        var scrollInfo = args;
        var treeG = scrollInfo.treeG;
        var paneG = scrollInfo.paneG;
        var vBarG = paneG.append('svg:g');
        var vGripBackground = vBarG.append('svg:rect');
        var hBarG = paneG.append('svg:g');
        var hGripBackground = hBarG.append('svg:rect');
        var vGripG = null;
        var vGrip = null;
        var hGripG = null;
        var hGrip = null;

        var scb = null;
        var bVDragging = false;
        var bHDragging = false;
        var bOut = true;
        var grip_w = 7;
        var gripbag_w = _scrollbar_width;

        function scrollbarRemove() {
            if (!scb.have_scrollbar_v && vGripG !== null) {
                vGripG.remove();
                vGripG = null;
            }
            if (!scb.have_scrollbar_h && hGripG !== null) {
                hGripG.remove();
                hGripG = null;
            }
        }

        tThis.update = function() {
            scb = scrollSize();
            scrollbarRemove();
            if (scb.have_scrollbar_v && vGripG === null) {
                vGripG = vBarG.append('svg:g');
                vGrip = vGripG.append('svg:rect');
                vGripG.call(d3.behavior.drag()
                .on("dragstart", dragstart_v)
                .on("drag", dragmove_vbar_grip)
                .on("dragend", dragend_v))
                .on("mouseover", function(e) {
                    vGrip.attr("class", "grip-active");
                    bOut = false;
                })
                .on("mouseout", function(e) {
                    if (!bVDragging)
                        vGrip.attr("class", "grip-normal");
                    bOut = true;
                });
                vGripBackground.on("mouseover", function(e) {
                    vGrip.attr("class", "grip-active");
                    bOut = false;
                })
                .on("mouseout", function(e) {
                    if (!bVDragging)
                        vGrip.attr("class", "grip-normal");
                    bOut = true;
                });
            }
            if (scb.have_scrollbar_h && hGripG === null) {
                hGripG = hBarG.append('svg:g');
                hGrip = hGripG.append('svg:rect');
                hGripG.call(d3.behavior.drag()
                .on("dragstart", dragstart_h)
                .on("drag", dragmove_hbar_grip)
                .on("dragend", dragend_h))
                .on("mouseover", function(e) {
                    hGrip.attr("class", "grip-active");
                    bOut = false;
                })
                .on("mouseout", function(e) {
                    if (!bHDragging)
                        hGrip.attr("class", "grip-normal");
                    bOut = true;
                });
                hGripBackground.on("mouseover", function(e) {
                    hGrip.attr("class", "grip-active");
                })
                .on("mouseout", function(e) {
                    if (!bHDragging)
                        hGrip.attr("class", "grip-normal");
                });
            }

            scrollbarLayout();

            //======================================================================
            // event function definitons
            function dragstart_v(d) {
                bVDragging = true;
                vGrip.attr("class", "grip-active");
            }
            function dragstart_h(d) {
                bHDragging = true;
                hGrip.attr("class", "grip-active");
            }
            function dragend_v(d) {
                bVDragging = false;
                if (bOut)
                    vGrip.attr("class", "grip-normal");
            }
            function dragend_h(d) {
                bHDragging = false;
                if (bOut)
                    hGrip.attr("class", "grip-normal");
            }
            // build a demo draggable rect
            function dragmove_vbar_grip(d) {
                var y = d.y + d3.event.dy;
                if (y < (scb.scroll_v_y_limit)
                && y >= scb.scroll_origin_v_y) {
                    d.y = y;
                    vGripG.attr("transform", "translate(" + d.x + "," + d.y + ")");
                    treeG.attr("transform", function(n) {
                        // Check bottom edge
                        if (scb.scroll_v_y_limit - y < 2)
                            n.y = scb.height - treeG.height;
                        else
                            n.y = scb.view_origin_y - Math.round((y - scb.scroll_origin_v_y) * treeG.height / scb.scroll_v_range);
                        return "translate(" + n.x + "," + n.y + ")";
                    });
                    // Update the relation link if it has
                    editorUpdateRelationshipLinks();
                }
            }
            function check_in_scroll_range_x(x) {
                if (scrollInfo.isLeft
                && x <= scb.scroll_h_x_limit
                && x >= scb.scroll_origin_h_x)
                    return true;
                else if (!scrollInfo.isLeft
                && x <= (scb.scroll_origin_h_x)
                && x >= scb.scroll_h_x_limit)
                    return true;
                return false;
            }
            function dragmove_hbar_grip(d) {
                var x = d.x + d3.event.dx;
                if (check_in_scroll_range_x(x)) {
                    d.x = x;
                    hGripG.attr("transform", "translate(" + d.x + "," + d.y + ")");
                    var view_origin_x = scrollInfo.isLeft ? scb.view_origin_left_x : scb.view_origin_right_x;
                    treeG.attr("transform", function(n) {
                        // Check left or right edge
                        n.x = view_origin_x - Math.round((x - scb.scroll_origin_h_x) * treeG.width / scb.scroll_h_range);
                        return "translate(" + n.x + "," + n.y + ")";
                    });
                }
            }
        }

        function scrollSize() {
            var s = editorAxisOrign();
            var p = treeG.datum();
            // frame
            // v
            s.scrollbag_v_x = scrollInfo.isLeft ? 0 : (s.width - gripbag_w);
            s.scrollbag_v_y = 0;
            // h
            s.scrollbag_h_x = 0;
            s.scrollbag_h_y = 0;
            // scrollbar
            s.have_scrollbar_v = treeG.height > s.height;
            s.have_scrollbar_h = treeG.width > s.width;
            // v
            if (s.have_scrollbar_v) {
                s.grip_v_l = Math.round(Math.pow(s.height - s.view_origin_y, 2) / treeG.height);

                s.scroll_v_x = scrollInfo.isLeft ? 1 : s.width - grip_w - 1;
                s.scroll_origin_v_y = gripbag_w;
                // Need to compute the scroll position
                s.scroll_v_y_limit = s.height - s.grip_v_l - 1;
                s.scroll_v_range = s.height - gripbag_w - 1;
                // Need a origin x,y
                if (p.y === s.view_origin_y)
                    s.scroll_v_y = s.scroll_origin_v_y;
                else
                    s.scroll_v_y = Math.round(Math.abs(p.y) * s.height / treeG.height);
            }
            // h
            if (s.have_scrollbar_h) {
                s.grip_h_l = Math.round(Math.pow(s.width - s.view_origin_left_x, 2) / treeG.width);
                // the scroll base range is the same between left and right, so use left_x for both
                s.scroll_origin_h_x = scrollInfo.isLeft ? gripbag_w : (s.width - s.grip_h_l - gripbag_w);
                s.scroll_h_y = 1;
                s.scroll_h_x_limit = scrollInfo.isLeft ? (s.width - s.grip_h_l - 1) : 1;
                s.scroll_h_range = s.width - gripbag_w - 1;
                var view_origin_x = scrollInfo.isLeft ? s.view_origin_left_x : s.view_origin_right_x;
                if (p.x === view_origin_x)
                    s.scroll_h_x = s.scroll_origin_h_x;
                else
                    s.scroll_h_x = Math.round(Math.abs(p.x) * s.width / treeG.width);
            }
            return s;
        }

        function scrollbarLayout() {
            scb = scrollSize();
            vGripBackground.attr({
                "class": "scrollbar-bag-normal",
                "x": scb.scrollbag_v_x,
                "y": scb.scrollbag_v_y,
                "width": gripbag_w,
                "height": scb.height
            });

            hGripBackground.attr({
                "class": "scrollbar-bag-normal",
                "x": scb.scrollbag_h_x,
                "y": scb.scrollbag_h_y,
                "width": scb.width,
                "height": gripbag_w
            });
            if (scb.have_scrollbar_v) {
                vGripG.data([{
                        x: scb.scroll_v_x,
                        y: scb.scroll_v_y
                    }])
                .attr("transform", "translate(" + scb.scroll_v_x + "," + scb.scroll_v_y + ")");
                vGrip.attr({
                    "width": grip_w,
                    "height": scb.grip_v_l,
                    "class": "grip-normal"
                });
            }
            if (scb.have_scrollbar_h) {
                hGripG.data([{
                        x: scb.scroll_h_x,
                        y: scb.scroll_h_y
                    }])
                .attr("transform", "translate(" + scb.scroll_h_x + "," + scb.scroll_h_y + ")")
                hGrip.attr({
                    "width": scb.grip_h_l,
                    "height": grip_w,
                    "class": "grip-normal"
                })
            }
        }
        // event function definitons end
        //======================================================================

        return tThis;
    }

    var tThis = {};

    function editorPaneSize() {
        var center_width = 60;
        var width = (_domHolder.width() - center_width) / 2;
        var height = _domHolder.height();
        return {
            center_width: center_width,
            width: width - 2,
            height: height - 2
        }
    }

    function editorAxisOrign() {
        var size = editorPaneSize();
        size.view_origin_left_x = _scrollbar_width + 2;
        size.view_origin_right_x = size.width - _scrollbar_width - 2,
        size.view_origin_y = _scrollbar_width + 2;
        return size;
    }

    var d3DomTable = _d3Holder.append('table');
    d3DomTable.attr({
        "class": "table-style"
    });
    var d3Tr = d3DomTable.append('tr');
    var leftDiv = d3Tr.append('td').append('div');
    var centerDiv = d3Tr.append('td').append('div');
    var rightDiv = d3Tr.append('td').append('div');
    var leftSVG = leftDiv.append('svg:svg');
    var centerSVG = centerDiv.append('svg:svg');
    var rightSVG = rightDiv.append('svg:svg');
    var leftG = leftSVG.append('svg:g');
    var leftTreeG = leftG.append('svg:g');
    var rightG = rightSVG.append('svg:g');
    var rightTreeG = rightG.append('svg:g');
    var centerG = centerSVG.append('svg:g');
    leftTreeG.height = 0;
    leftTreeG.width = 0;
    rightTreeG.height = 0;
    rightTreeG.width = 0;
    leftTreeG.scrollbar = null;
    rightTreeG.scrollbar = null;

    function editorLayout() {
        var axis = editorAxisOrign();
        leftDiv.attr({
            "id": "leftDiv",
            "class": "table-td table-td-border-side",
            "style": "width:" + axis.width + "px; height:" + axis.height + "px;"
        });
        centerDiv.attr({
            "id": "centerDiv",
            "class": "table-td table-td-border-center",
            "style": "width:" + axis.center_width + "px; height:" + axis.height + "px"
        });
        rightDiv.attr({
            "id": "rightDiv",
            "class": "table-td table-td-border-side",
            "style": "width:" + axis.width + "px; height:" + axis.height + "px"
        });

        // SVG size settings here
        leftSVG.attr({
            "width": axis.width,
            "height": axis.height
        });
        centerSVG.attr({
            "width": axis.center_width,
            "height": axis.height
        });
        rightSVG.attr({
            "width": axis.width,
            "height": axis.height
        });

        centerG.data([{
                x: 0,
                y: 0
            }]);

        leftTreeG.data([{
                x: axis.view_origin_left_x,
                y: axis.view_origin_y
            }]);
        rightTreeG.data([{
                x: axis.view_origin_right_x,
                y: axis.view_origin_y
            }]);
        leftTreeG.attr("transform", "translate(" + axis.view_origin_left_x + "," + axis.view_origin_y + ")");
        rightTreeG.attr("transform", "translate(" + axis.view_origin_right_x + "," + axis.view_origin_y + ")");

        if (leftTreeG.scrollbar !== null)
            leftTreeG.scrollbar.update();
        if (rightTreeG.scrollbar !== null)
            rightTreeG.scrollbar.update();
    }

    editorLayout();
    _domHolder.on("resize", function(e) {
        if (e.target !== this)
            return;
        editorLayout();
    });

    var _currentDragElement = null;
    var line = d3.svg.line()
    .x(function(d) {
        return d.x;
    })
    .y(function(d) {
        return d.y;
    })
    .interpolate("basis");
    function editorShowRelationshipLinks(data) {
        if (data.path_array !== undefined) {
            var have_right_link = false;
            var r_y = null;
            var size = editorAxisOrign();
            for (var i = 0; i < data.path_array.length; ++i) {
                var path = data.path_array[i];
                var tokens = path.split('.');
                var cur = leftTreeG.root;
                for (var j = 0; j < tokens.length; ++j) {
                    cur = cur.children_map[tokens[j]];
                }
                // Layout left tree relation links
                var l_y = cur.x + _barHeight / 2;
                var end_x = cur.y + leftTreeG.width;
                if (Math.abs(end_x) < size.width)
                    end_x = size.width;
                leftTreeG.append("svg:path")
                .attr({
                    "class": "relation",
                    "d": line([
                        {
                            x: (cur.y + cur.bbox_width + _barHeight + 2),
                            y: l_y
                        },
                        {
                            x: end_x,
                            y: l_y
                        }
                    ])
                });
                // Layout right tree relation links
                if (!have_right_link) {
                    r_y = data.x + _barHeight / 2;
                    end_x = -rightTreeG.width - 2;
                    if (Math.abs(end_x) < size.width)
                        end_x = -size.width - 2;
                    rightTreeG.append("svg:path")
                    .attr({
                        "class": "relation",
                        "d": line([
                            {
                                x: (data.y + _barWidth - data.bbox_width - _barHeight - 4),
                                y: r_y
                            },
                            {
                                x: end_x,
                                y: r_y
                            }
                        ])
                    });
                    have_right_link = true;
                }

                // Layout the center links
                if (r_y !== null) {
                    var dat = [
                        {
                            x: 0,
                            y: (l_y + size.view_origin_y)
                        },
                        {
                            x: size.center_width,
                            y: (r_y + size.view_origin_y)
                        }
                    ];
                    centerG.append('svg:path')
                    .data([dat])
                    .attr({
                        "class": "relation",
                        "d": line(dat)
                    })
                    editorUpdateRelationshipLinks();
                }
            }
        }
    }

    function editorUpdateRelationshipLinks() {
        // TODO when collpase a node, its children link position should be updated
        var allCenterPaths = centerG.selectAll("path.relation");
        if (allCenterPaths[0].length === 0)
            return;
        var size = editorAxisOrign();
        var l_dat = leftTreeG.datum();
        var r_dat = rightTreeG.datum();
        allCenterPaths.attr("d", function(d) {
            var l_dy = l_dat.y - size.view_origin_y;
            var r_dy = r_dat.y - size.view_origin_y;
            return line([
                {
                    x: d[0].x,
                    y: d[0].y + l_dy
                },
                {
                    x: d[1].x,
                    y: d[1].y + r_dy
                }
            ])
        });

    }

    function editorRemoveRelationshipLinks() {
        var node = _d3Holder.selectAll("path.relation");
        node.remove();
    }

    var tree_item_selected = null;
    function loadTree(treeG, jsonData) {
        var size = editorAxisOrign();
        var isLeft = (leftTreeG === treeG);
        var diagonal = d3.svg.diagonal()
        .projection(function(d) {
            return [(isLeft ? d.y : (d.y + _barWidth)), d.x + _barHeight];
        });
        var tree = d3.layout.tree();
        var x = isLeft ? size.view_origin_left_x : size.view_origin_right_x;
        var vis = treeG.attr("transform", "translate(" + x + "," + size.view_origin_y + ")"),
        i = 0, btn_size = 8,
        root, to_map_children = true;

        d3.json(jsonData, function(json) {
            json.x0 = 0;
            json.y0 = 0;
            layout(treeG.root = root = json);
            to_map_children = false;
        });

        function layout(source) {
            //======================================================================
            // Build hash table for children of a json node, in order to search the
            // child node by its name value. Every node has a name field
            //======================================================================
            size = editorAxisOrign();
            // Compute the flattened node list. TODO use d3.layout.hierarchy.
            var nodes = tree.nodes(root);
            // Compute the "layout".
            nodes.forEach(function(n, i) {
                n.x = (i - 1) * _barHeight;
                n.y = isLeft ? (n.depth - 1) * _barHeight : (-n.depth + 1) * _barHeight - _barWidth;
                if (to_map_children) {
                    n.children_map = {};
                    for (var idx = 0; (n.children !== null && n.children !== undefined
                    && idx < n.children.length); ++idx)
                        n.children_map[n.children[idx].name] = n.children[idx];
                }
            });

            // Update the nodes
            var node = vis.selectAll("g.node")
            .data(nodes, function(d) {
                return d.id || (d.id = ++i);
            });

            var nodeEnter = node.enter().append("svg:g")
            .attr("class", "node")
            .attr("transform", function(d) {
                return "translate(" + source.y0 + "," + source.x0 + ")";
            })
            .style("opacity", 1e-6);

            // Enter any new nodes at the parent's previous position.
            nodeEnter.append("svg:rect")
            .attr({
                "height": _barHeight,
                "width": _barHeight,
                "transform": "translate(" + (isLeft ? 0 : _barWidth - _barHeight) + ", 0)"
            })
            .style({
                "fill": color,
                "cursor": "pointer"
            })
            .on("click", click);

            nodeEnter.append("svg:rect")
            .attr({
                "height": btn_size,
                "width": btn_size,
                "y": _barHeight - btn_size,
                "x": isLeft ? 0 : _barWidth - btn_size
            })
            .style({
                "fill": "gray",
                "opacity": .5,
                "cursor": "pointer"
            })
            .on("click", click);

            // =====================================================================
            // TODO need to display data format data type
            // Build data node and event
            nodeEnter.append("svg:rect")
            .style({
                "fill": "rgba(206, 206, 206, .4)",
                "opacity": 1e-6,
                "cursor": "pointer"
            })
            .attr({
                "height": _barHeight,
                "width": function(d) {
                    //------------------------------------------------------------------
                    var t_w = 0;
                    var bagNode = d3.select(this);
                    this.text = d3.select(this.parentNode).append("svg:text")
                    .attr({
                        "dy": 12.5,
                        "class": function(d) {
                            return d.script !== undefined ? "item-text-used-style" : "item-text-normal-style";
                        }
                    }).text(function(d) {
                        if (!d.children)
                            return isLeft ? "[string] " + d.name : d.name + " [string]";
                        return d.name;
                    })
                    .attr("dx", function(d) {
                        t_w = d.bbox_width = this.getBBox().width + 2;
                        var t_x = isLeft ? (_barHeight + 2) : (_barWidth - t_w - _barHeight - 2);
                        bagNode.attr({
                            "x": isLeft ? t_x : t_x - 2
                        });
                        return t_x;
                    })
                    .node();

                    if (isLeft) {
                        function formatDraggable() {
                            return $('<div id="draggable-item" style="width:' + (d.bbox_width + 2) + 'px;">'
                            + (!d.children ? "[string] " + d.name : d.name) + '</>');
                        }
                        $(this).draggable({
                            appendTo: _domHolder,
                            containment: _domHolder,
                            helper: formatDraggable,
                            stop: function(event, ui) {
                                _currentDragElement = {
                                    data: d3.select(event.target).datum(),
                                    text: this.text
                                }
                            }
                        });
                    }
                    //------------------------------------------------------------------
                    return t_w;
                }
            })
            .on("mouseover", function(n) {
                /** Note: It is a workaround, d3 doesn't support drop down event
                 * use _currentDragElement to save the drag stop information
                 **/
                if (_currentDragElement !== null && !isLeft) {
                    d3.select(this.text)
                    .attr({
                        "class": "item-text-used-style"
                    });
                    d3.select(_currentDragElement.text)
                    .attr({
                        "class": "item-text-used-style"
                    });
                    if (_rightItemDropped !== undefined) {
                        _rightItemDropped(_currentDragElement.data, n);
                    }

                    _currentDragElement = null;
                    tree_item_selected = null;
                    editorRemoveRelationshipLinks();
                }
                d3.select(this).style("opacity", 1);
            })
            .on("mouseout", function(n) {
                if (tree_item_selected !== this)
                    d3.select(this).style("opacity", 1e-6);
            })
            .on("click", function(n) {
                if (tree_item_selected !== null) {
                    // cancel current selected
                    d3.select(tree_item_selected).style("opacity", 1e-6);
                    editorRemoveRelationshipLinks();
                }
                if (tree_item_selected !== this) {
                    // set as selected status
                    tree_item_selected = this;
                    d3.select(tree_item_selected).style("opacity", 1);
                    // TODO need to display the links which are used by this item field
                    editorShowRelationshipLinks(n);
                }
                else {
                    // click twice, the cancel selected and set the selected flag as undefined
                    tree_item_selected = null;
                    editorRemoveRelationshipLinks();
                }
            });
            // =====================================================================

            function opacityFilter(d) {
                if (d.id > 1)
                    return 1;
                return 1e-6;
            }
            // Transition nodes to their new position.
            nodeEnter.transition()
            .duration(_duration)
            .attr("transform", function(d) {
                return "translate(" + d.y + "," + d.x + ")";
            })
            .style("opacity", opacityFilter);

            node.transition()
            .duration(_duration)
            .attr("transform", function(d) {
                return "translate(" + d.y + "," + d.x + ")";
            })
            .style("opacity", opacityFilter)
            .select("rect")
            .style("fill", color);

            // Transition exiting nodes to the parent's new position.
            node.exit().transition()
            .duration(_duration)
            .attr("transform", function(d) {
                return "translate(" + source.y + "," + source.x + ")";
            })
            .style("opacity", 1e-6)
            .remove();

            // Stash the old positions for transition.
            nodes.forEach(function(d) {
                d.x0 = d.x;
                d.y0 = d.y;
            });

            // Update the links
            var link = vis.selectAll("path.link")
            .data(tree.links(nodes), function(d) {
                return d.target.id;
            });

            // Enter any new links at the parent's previous position.
            link.enter().insert("svg:path", "g")
            .attr("class", function(d) {
                if (d.source.id === 1)
                    return "linkhidden";
                return "link";
            })
            .attr("d", function(d) {
                var o = {
                    x: source.x0,
                    y: source.y0
                };
                return diagonal({
                    source: o,
                    target: o
                });
            })
            .transition()
            .duration(_duration)
            .attr("d", diagonal);

            // Transition links to their new position.
            link.transition()
            .duration(_duration)
            .attr("d", diagonal);

            // Transition exiting nodes to the parent's new position.
            link.exit().transition()
            .duration(_duration)
            .attr("d", function(d) {
                var o = {
                    x: source.x,
                    y: source.y
                };
                return diagonal({
                    source: o,
                    target: o
                });
            })
            .remove();
            //= Compute tree bbox size =============================================
            treeG.width = 0;
            treeG.height = 0;
            var minx, miny, maxx, maxy;
            minx = miny = Number.MAX_VALUE;
            maxx = maxy = -Number.MAX_VALUE;
            var fx = isLeft ? function(n) {
                return (n.bbox_width + 2);
            }
            : function(n) {
                return -(n.bbox_width + 2);
            };
            if (isLeft) {
                nodes.forEach(function(n) {
                    var y = n.y + fx(n);
                    if (minx > n.y)
                        minx = n.y;
                    if (maxx < y)
                        maxx = y;
                    if (maxy < n.x)
                        maxy = n.x;
                    if (miny > n.x)
                        miny = n.x;
                });
            } else {
                nodes.forEach(function(n) {
                    var y = n.y + fx(n);
                    if (minx > y)
                        minx = y;
                    if (maxx < n.y)
                        maxx = n.y;
                    if (maxy < n.x)
                        maxy = n.x;
                    if (miny > n.x)
                        miny = n.x;
                });

            }
            treeG.width = (maxx - minx);
            treeG.height = (maxy - miny);
            //= Compute tree bbox size end =========================================
            if (treeG.scrollbar !== null)
                treeG.scrollbar.update();
            else {
                treeG.scrollbar = RuleTreeEditorScrollBar.create({
                    treeEditor: tThis,
                    treeG: treeG,
                    paneG: isLeft ? leftG : rightG,
                    isLeft: isLeft
                });
                treeG.scrollbar.update();
            } //*/
            if (tree_item_selected !== null) {
                // cancel current selected
                d3.select(tree_item_selected).style("opacity", 1e-6);
                editorRemoveRelationshipLinks();
                tree_item_selected = null;
            }
        }

        // Toggle children on click.
        function click(d) {
            if (d.children) {
                d._children = d.children;
                d.children = null;
            } else {
                d.children = d._children;
                d._children = null;
                // TODO need to read data format from server here if the current node
                // is a struct or struct array type
                // ------ here ----
            }
            layout(d);
        }

        function color(d) {
            return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fdf5ef";
        }
    }

    tThis.loadData = function() {
        loadTree(leftTreeG, "demo_1.json");
        loadTree(rightTreeG, "demo.json");
    }

    // End
    return tThis;
};

Lumens.Navigator = {};
Lumens.Navigator.create = function(id, holder) {
    // Private members
    var tThis = {};
    var parent = holder;
    // Initailize controls of navigator
    var navigatorBar = $(('<div id="' + id + '"/>'));
    navigatorBar.appendTo(parent);
    navigatorBar.attr("class", "navigator-bar");
    var label = $('<div/>');
    label.appendTo(navigatorBar);
    label.attr("class", "navigator-text");
    // ================= Public methods =================
    tThis.setText = function(text) {
        label.html(text);
    }
    // =================Navigator End===============================================================
    return tThis;
}

var DatasourceCreator = {};
DatasourceCreator.create = function(id, parentHolder) {
    var tThis = {};
    var parent = parentHolder;
    var pages = [];
    var wizard = $("<div id=" + id + "/>");
    wizard.appendTo(parent);
    wizard.attr("class", "datasource-wizard");
    //==================DatasourceCreator End=======================================================
    return tThis;
}

Lumens.Utils = {}
Lumens.Utils.buildPathV0 = function(s, t, size) {
    var s_left = s.x;
    var s_top = s.y;
    var s_width = size.width;
    var s_height = size.height;
    var s_right = s.x + size.width;
    var s_bottom = s.y + size.height;

    var t_left = t.x;
    var t_top = t.y;
    var t_width = size.width;
    var t_height = size.height;
    var t_right = t.x + size.width;
    var t_bottom = t.y + size.height;

    var s_center_x = s.x + s_width / 2;
    var s_center_y = s.y + s_height / 2;
    var t_center_x = t_left + t_width / 2;
    var t_center_y = t_top + t_height / 2;
    var delta = 0;

    if (s_right < t_center_x &&
    s_center_y > (t_bottom + delta)) {
        // s_right --> t_bottom
        return [{
                x: s_right,
                y: s_center_y
            },
            {
                x: t_center_x,
                y: s_center_y
            },
            {
                x: t_center_x,
                y: t_bottom
            }];
    }
    else if (s_right <= t_center_x &&
    s_center_y <= (t_bottom + delta) &&
    s_center_y >= (t_top - delta)) {
        // s_right --> t_left
        var ty = (s_center_y + t_center_y) / 2;
        return [{
                x: s_right,
                y: ty
            },
            {
                x: t_left,
                y: ty
            }];
    }
    else if (s_right < t_center_x &&
    s_center_y < t_top) {
        //s_right --> t_top
        return  [{
                x: s_right,
                y: s_center_y
            }, {
                x: t_center_x,
                y: s_center_y
            }, {
                x: t_center_x,
                y: t_top
            }];
    } else if (s_bottom <= t_top &&
    s_right >= t_center_x &&
    s_left <= t_center_x) {
        // s_bottom --> t_top
        var tx = (s_center_x + t_center_x) / 2;
        return [{
                x: tx,
                y: s_bottom
            }, {
                x: tx,
                y: t_top
            }];
    } else if (t_center_x < s_left &&
    t_top > (s_center_y + delta)) {
        // s_left --> t_top
        return [{
                x: s_left,
                y: s_center_y
            }, {
                x: t_center_x,
                y: s_center_y
            }, {
                x: t_center_x,
                y: t_top
            }];
    } else if (t_right <= s_left &&
    s_center_y <= (t_bottom + delta) &&
    s_center_y >= (t_top - delta)) {
        // s_left --> out_right
        ty = (s_center_y + t_center_y) / 2;
        return [{
                x: s_left,
                y: ty
            }, {
                x: t_right,
                y: ty
            }];
    } else if (t_center_x < s_left &&
    t_top < s_center_y) {
        // s_left --> t_bottom
        return [{
                x: s_left,
                y: s_center_y
            }, {
                x: t_center_x,
                y: s_center_y
            }, {
                x: t_center_x,
                y: t_bottom
            }];
    } else if (s_top >= t_bottom &&
    t_center_x >= s_left &&
    t_center_x <= s_right) {
        // s_top --> t_bottom
        tx = (s_center_x + t_center_x) / 2;
        return [{
                x: tx,
                y: s_top
            }, {
                x: tx,
                y: t_bottom
            }];
    }
    return [{
            x: 0,
            y: 0
        }, {
            x: 0,
            y: 0
        }];
}

Lumens.Utils.buildPathV1 = function(s, t, size, svg_xy) {
    var p_array = Lumens.Utils.buildPathV0(s, t, size);
    for (var i = 0; i < p_array.length; ++i) {
        var p = p_array[i];
        p.x -= svg_xy.left;
        p.y -= svg_xy.top;
    }
    return p_array;
}
