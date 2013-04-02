/*
 * jQuery.splitter.js - two-pane splitter window plugin
 *
 * version 1.51 (2009/01/09)
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */

/**
 * The splitter() plugin implements a two-pane resizable splitter window.
 * The selected elements in the jQuery object are converted to a splitter;
 * each selected element should have two child elements, used for the panes
 * of the splitter. The plugin adds a third child element for the splitbar.
 *
 * For more details see: http://methvin.com/splitter/
 *
 *
 * @example $('#MySplitter').splitter();
 * @desc Create a vertical splitter with default settings
 *
 * @example $('#MySplitter').splitter({type: 'h', accessKey: 'M'});
 * @desc Create a horizontal splitter resizable via Alt+Shift+M
 *
 * @name splitter
 * @type jQuery
 * @param Object options Options for the splitter (not required)
 * @cat Plugins/Splitter
 * @return jQuery
 * @author Dave Methvin (dave.methvin@gmail.com)
 */
(function($){
    $.fn.splitter = function(args){
        args = args || {};
        return this.each(function() {
            // left-behind splitbar for outline resizes
            var zombie;
            function startSplitMouse(evt) {
                if ( opts.outline )
                    zombie = zombie || bar.clone(false).insertAfter(A);
                panes.css("-webkit-user-select", "none");  // Safari selects A/B text on a move
                bar.addClass(opts.activeClass);
                A._posSplit = A[0][opts.pxSplit] - evt[opts.eventPos];
                $(document)
                .bind("mousemove", doSplitMouse)
                .bind("mouseup", endSplitMouse);
            }
            function doSplitMouse(evt) {
                var newPos = A._posSplit+evt[opts.eventPos];
                if ( opts.outline ) {
                    newPos = Math.max(0, Math.min(newPos, splitterPane._DA - bar._DA));
                    bar.css(opts.origin, newPos);
                } else
                    resplit(newPos);
            }
            function endSplitMouse(evt) {
                bar.removeClass(opts.activeClass);
                var newPos = A._posSplit+evt[opts.eventPos];
                if ( opts.outline ) {
                    zombie.remove();
                    zombie = null;
                    resplit(newPos);
                }
                panes.css("-webkit-user-select", "text");  // let Safari select text again
                $(document)
                .unbind("mousemove", doSplitMouse)
                .unbind("mouseup", endSplitMouse);
            }
            function resplit(newPos) {
                // Constrain new splitbar position to fit pane size limits
                newPos = Math.max(A._min, splitterPane._DA - B._max,
                    Math.min(newPos, A._max, splitterPane._DA - bar._DA - B._min));
                // Resize/position the two panes
                bar._DA = bar[0][opts.pxSplit];		// bar size may change during dock
                bar.css(opts.origin, newPos).css(opts.fixed, splitterPane._DF);
                A.css(opts.origin, 0).css(opts.split, newPos).css(opts.fixed,  splitterPane._DF);
                B.css(opts.origin, newPos+bar._DA).css(opts.split, splitterPane._DA-bar._DA-newPos).css(opts.fixed,  splitterPane._DF);
                A.trigger("resize");
                B.trigger("resize");
            }
            function dimSum(jq, dims) {
                // Opera returns -1 for missing min/max width, turn into 0
                var sum = 0;
                for ( var i=1; i < arguments.length; i++ )
                    sum += Math.max(parseInt(jq.css(arguments[i])) || 0, 0);
                return sum;
            }

            // Determine settings based on incoming opts, element classes, and defaults
            var vh = (args.splitHorizontal? 'h' : args.splitVertical? 'v' : args.type) || 'v';
            var opts = $.extend({
                activeClass: 'active', // class name for active splitterPane
                pxPerKey: 8,           // splitterPane px moved per keypress
                tabIndex: 0,           // tab order indicator
                accessKey: ''          // accessKey for splitbar
            },
            {
                v: {  // Vertical splitters:
                    keyLeft: 39,
                    keyRight: 37,
                    cursor: "e-resize",
                    splitbarClass: "vsplitbar",
                    outlineClass: "voutline",
                    type: 'v',
                    eventPos: "pageX",
                    origin: "left",
                    split: "width",
                    pxSplit: "offsetWidth",
                    side1: "Left",
                    side2: "Right",
                    fixed: "height",
                    pxFixed: "offsetHeight",
                    side3: "Top",
                    side4: "Bottom",
                    anchorToWindow: true
                },
                h: {  // Horizontal splitters:
                    keyTop: 40,
                    keyBottom: 38,
                    cursor: "n-resize",
                    splitbarClass: "hsplitbar",
                    outlineClass: "houtline",
                    type: 'h',
                    eventPos: "pageY",
                    origin: "top",
                    split: "height",
                    pxSplit: "offsetHeight",
                    side1: "Top",
                    side2: "Bottom",
                    fixed: "width",
                    pxFixed: "offsetWidth",
                    side3: "Left",
                    side4: "Right",
                    anchorToWindow: true
                }
            }
            [vh], args);

            // Create jQuery object closures for splitterPane and both panes
            var splitterPane = $(this).css({
                position: "relative"
            });
            var panes = $(">*", splitterPane[0]).css({
                position: "absolute",         // positioned inside splitterPane container
                "z-index": "1",               // splitbar is positioned above
                "-moz-outline-style": "none"  // don't show dotted outline
            });
            var A = $(panes[0]);              // left  or top
            var B = $(panes[1]);              // right or bottom

            // Focuser element, provides keyboard support; title is shown by Opera accessKeys
            var focuser = $('<a href="javascript:void(0)"></a>')
            .attr({
                accessKey: opts.accessKey,
                tabIndex: opts.tabIndex,
                title: opts.splitbarClass
            })
            .bind("focus", function(){
                this.focus();
                bar.addClass(opts.activeClass)
            })
            .bind("keydown", function(e){
                var key = e.which || e.keyCode;
                var dir = key==opts["key"+opts.side1]? 1 : key==opts["key"+opts.side2]? -1 : 0;
                if ( dir )
                    resplit(A[0][opts.pxSplit]+dir*opts.pxPerKey, false);
            })
            .bind("blur", function(){
                bar.removeClass(opts.activeClass)
            });

            // Splitbar element, can be already in the doc or we create one
            var bar = $(panes[2] || '<div></div>')
            .insertAfter(A).css("z-index", "100").append(focuser)
            .attr({
                "class": opts.splitbarClass,
                unselectable: "on"
            })
            .css({
                position: "absolute",
                "user-select": "none",
                "-webkit-user-select": "none",
                "-khtml-user-select": "none",
                "-moz-user-select": "none"
            })
            .bind("mousedown", startSplitMouse);
            // Use our cursor unless the style specifies a non-default cursor
            if ( /^(auto|default|)$/.test(bar.css("cursor")) )
                bar.css("cursor", opts.cursor);

            // Cache several dimensions for speed, rather than re-querying constantly
            bar._DA = bar[0][opts.pxSplit];
            splitterPane._PBF = $.boxModel? dimSum(splitterPane, "border"+opts.side3+"Width", "border"+opts.side4+"Width") : 0;
            splitterPane._PBA = $.boxModel? dimSum(splitterPane, "border"+opts.side1+"Width", "border"+opts.side2+"Width") : 0;
            A._pane = opts.side1;
            B._pane = opts.side2;
            $.each([A,B], function(){
                this._min = opts["min"+this._pane] || dimSum(this, "min-"+opts.split);
                this._max = opts["max"+this._pane] || dimSum(this, "max-"+opts.split) || 9999;
                this._init = opts["size"+this._pane]===true ?
                parseInt($.css(this[0],opts.split)) : opts["size"+this._pane];
            });

            // Determine initial position, get from cookie if specified
            var initPos = A._init;
            if ( !isNaN(B._init) )	// recalc initial B size as an offset from the top or left side
                initPos = splitterPane[0][opts.pxSplit] - splitterPane._PBA - B._init - bar._DA;
            if ( opts.cookie ) {
                if ( !$.cookie )
                    alert('jQuery.splitter(): jQuery cookie plugin required');
                var ckpos = parseInt($.cookie(opts.cookie));
                if ( !isNaN(ckpos) )
                    initPos = ckpos;
                $(window).bind("unload", function(){
                    var state = String(bar.css(opts.origin));	// current location of splitbar
                    $.cookie(opts.cookie, state, {
                        expires: opts.cookieExpires || 365,
                        path: opts.cookiePath || document.location.pathname
                    });
                });
            }
            if ( isNaN(initPos) )  // King Solomon's algorithm
                initPos = Math.round((splitterPane[0][opts.pxSplit] - splitterPane._PBA - bar._DA)/2);

            function splitterResize(e, size) {
                // Custom events bubble in jQuery 1.3; don't get into a Yo Dawg
                if ( e.target != this ) return;
                // Determine new width/height of splitterPane container
                splitterPane._DF = splitterPane[0][opts.pxFixed] - splitterPane._PBF;
                splitterPane._DA = splitterPane[0][opts.pxSplit] - splitterPane._PBA;
                // Bail if splitterPane isn't visible or content isn't there yet
                if ( splitterPane._DF <= 0 || splitterPane._DA <= 0 ) return;
                // Re-divvy the adjustable dimension; maintain size of the preferred pane
                resplit(!isNaN(size)? size :
                    (!(opts.sizeRight||opts.sizeBottom)? A[0][opts.pxSplit] : splitterPane._DA-B[0][opts.pxSplit]-bar._DA));
            }
            // Resize event handler; triggered immediately to set initial position
            splitterPane.bind("resize", splitterResize).trigger("resize" , [initPos]);

            // Resize event propagation and splitterPane sizing
            if ( opts.anchorToWindow ) {
                // Account for margin or border on the splitterPane container and enforce min height
                splitterPane._hadjust = dimSum(splitterPane, "borderTopWidth", "borderBottomWidth", "marginBottom");
                splitterPane._hmin = Math.max(dimSum(splitterPane, "minHeight"), 20);
                function windowResize(e) {
                    if ( e.target != this ) return;
                    var top = splitterPane.offset().top;
                    var wh = $(window).height();
                    splitterPane.css("height", Math.max(wh-top-splitterPane._hadjust, splitterPane._hmin)+"px");
                    splitterPane.trigger("resize");
                }
                $(window).bind("resize", windowResize).trigger("resize");
            }
        });
    };
})(jQuery);