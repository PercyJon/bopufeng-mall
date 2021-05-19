var App = function () {

    // IE mode
    var isRTL = false;
    var isIE8 = false;
    var isIE9 = false;
    var isIE10 = false;

    var resizeHandlers = [];

    // initializes main settings
    var handleInit = function () {

        if ($('body').css('direction') === 'rtl') {
            isRTL = true;
        }

        isIE8 = !!navigator.userAgent.match(/MSIE 8.0/);
        isIE9 = !!navigator.userAgent.match(/MSIE 9.0/);
        isIE10 = !!navigator.userAgent.match(/MSIE 10.0/);

        if (isIE10) {
            $('html').addClass('ie10'); // detect IE10 version
        }

        if (isIE10 || isIE9 || isIE8) {
            $('html').addClass('ie'); // detect IE10 version
        }
    };

    // runs callback functions set by App.addResponsiveHandler().
    var _runResizeHandlers = function () {
        // reinitialize other subscribed elements
        for (var i = 0; i < resizeHandlers.length; i++) {
            var each = resizeHandlers[i];
            each.call();
        }
    };

    //初始化iframe内容页高度
    var handleIframeContent = function () {
        var ht = $(window).height();//获取浏览器窗口的整体高度；

        var $footer = $(".main-footer");
        var $header = $(".main-header");
        var $tabs = $(".content-tabs");

        var height = App.getViewPort().height - $footer.outerHeight() - $header.outerHeight();
        if ($tabs.is(":visible")) {
            height = height - $tabs.outerHeight();
        }

        $(".tab_iframe").css({
            height: height - 5,
            width: "100%"
        });

    };
    
    //初始化内容页layout组件高度
    var handleIframeLayoutHeight = function () {
        var height = App.getViewPort().height - $('.page-footer').outerHeight() - $('.page-header').outerHeight() - $(".content-tabs").height();
        return height;
    };

    var handleSiderBarmenu = function () {
        jQuery('.page-sidebar-menu').on('click', ' li > a.iframeOpen', function (e) {
            e.preventDefault();
            App.scrollTop();
            $("#iframe-main").attr("src", $(this).attr('href'));
        });
    };

    var isFullScreen = false;

    var requestFullScreen = function () {
        var de = document.documentElement;

        if (de.requestFullscreen) {
            de.requestFullscreen();
        } else if (de.mozRequestFullScreen) {
            de.mozRequestFullScreen();
        } else if (de.webkitRequestFullScreen) {
            de.webkitRequestFullScreen();
        } else {
            alert("该浏览器不支持全屏！");
        }

    };

    var requestFullScreen2 = function (element) {
        // 判断各种浏览器，找到正确的方法
        var requestMethod = element.requestFullScreen || //W3C
            element.webkitRequestFullScreen ||    //Chrome等
            element.mozRequestFullScreen || //FireFox
            element.msRequestFullScreen; //IE11
        if (requestMethod) {
            requestMethod.call(element);
        }
        else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
            var wscript = new ActiveXObject("WScript.Shell");
            if (wscript !== null) {
                wscript.SendKeys("{F11}");
            }
        }
    };

    //退出全屏 判断浏览器种类
    var exitFull = function () {
        // 判断各种浏览器，找到正确的方法
        var exitMethod = document.exitFullscreen || //W3C
            document.mozCancelFullScreen ||    //Chrome等
            document.webkitExitFullscreen || //FireFox
            document.webkitExitFullscreen; //IE11
        if (exitMethod) {
            exitMethod.call(document);
        }
        else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
            var wscript = new ActiveXObject("WScript.Shell");
            if (wscript !== null) {
                wscript.SendKeys("{F11}");
            }
        }
    };

    // handle the layout reinitialization on window resize
    var handleOnResize = function () {
        var resize;
        if (isIE8) {
            var currheight;
            $(window).resize(function () {
                if (currheight == document.documentElement.clientHeight) {
                    return; //quite event since only body resized not window.
                }
                if (resize) {
                    clearTimeout(resize);
                }
                resize = setTimeout(function () {
                    _runResizeHandlers();
                    handleIframeContent();
                }, 50); // wait 50ms until window resize finishes.                
                currheight = document.documentElement.clientHeight; // store last body client height
            });
        } else {
            $(window).resize(function () {
                if (resize) {
                    clearTimeout(resize);
                }
                resize = setTimeout(function () {
                    _runResizeHandlers();
                    handleIframeContent();
                }, 50); // wait 50ms until window resize finishes.
            });
        }
    };

    // Handles scrollable contents using jQuery SlimScroll plugin.
    var handleScrollers = function () {
        App.initSlimScroll('.scroller');
    };

    // Fix input placeholder issue for IE8 and IE9
    var handleFixInputPlaceholderForIE = function () {
        //fix html5 placeholder attribute for ie7 & ie8
        if (isIE8 || isIE9) { // ie8 & ie9
            // this is html5 placeholder fix for inputs, inputs with placeholder-no-fix class will be skipped(e.g: we need this for password fields)
            $('input[placeholder]:not(.placeholder-no-fix), textarea[placeholder]:not(.placeholder-no-fix)').each(function () {
                var input = $(this);

                if (input.val() === '' && input.attr("placeholder") !== '') {
                    input.addClass("placeholder").val(input.attr('placeholder'));
                }

                input.focus(function () {
                    if (input.val() == input.attr('placeholder')) {
                        input.val('');
                    }
                });

                input.blur(function () {
                    if (input.val() === '' || input.val() == input.attr('placeholder')) {
                        input.val(input.attr('placeholder'));
                    }
                });
            });
        }
    };

    // handle group element heights
    var handleHeight = function () {
        $('[data-auto-height]').each(function () {
            var parent = $(this);
            var items = $('[data-height]', parent);
            var height = 0;
            var mode = parent.attr('data-mode');
            var offset = parseInt(parent.attr('data-offset') ? parent.attr('data-offset') : 0);

            items.each(function () {
                if ($(this).attr('data-height') == "height") {
                    $(this).css('height', '');
                } else {
                    $(this).css('min-height', '');
                }

                var height_ = (mode == 'base-height' ? $(this).outerHeight() : $(this).outerHeight(true));
                if (height_ > height) {
                    height = height_;
                }
            });

            height = height + offset;

            items.each(function () {
                if ($(this).attr('data-height') == "height") {
                    $(this).css('height', height);
                } else {
                    $(this).css('min-height', height);
                }
            });

            if (parent.attr('data-related')) {
                $(parent.attr('data-related')).css('height', parent.height());
            }
        });
    };
    //* END:CORE HANDLERS *//

    return {

        init: function () {
        	//Core handlers
            handleInit(); // initialize core variables
            handleOnResize(); // set and handle responsive    
            //UI Component handlers     
            handleScrollers(); // handles slim scrolling contents 
            handleSiderBarmenu();
            //Handle group element heights
            this.addResizeHandler(handleHeight); // handle auto calculating height on window resize
            // Hacks
            handleFixInputPlaceholderForIE(); //IE8 & IE9 input placeholder issue fix
        },

        //main function to initiate core javascript after ajax complete
        initAjax: function () {
            handleScrollers(); // handles slim scrolling contents 
        },
        
        handleFullScreen: function () {
            if (isFullScreen) {
                exitFull();
                isFullScreen = false;
            } else {
                requestFullScreen();
                isFullScreen = true;
            }
        },
        
        fixIframeTab: function () {
            handleIframeContent();
        },
        getIframeLayoutHeight: function () {
            return handleIframeLayoutHeight();
        },
        
        //init main components 
        initComponents: function () {
            this.initAjax();
        },
        
        fixIframeCotent: function () {
            setTimeout(function () {
                //_runResizeHandlers();
                handleIframeContent();
            }, 50);
        },

        //public function to add callback a function which will be called on window resize
        addResizeHandler: function (func) {
            resizeHandlers.push(func);
        },

        //public functon to call _runresizeHandlers
        runResizeHandlers: function () {
            _runResizeHandlers();
        },

        // wrApper function to scroll(focus) to an element
        scrollTo: function (el, offeset) {
            var pos = (el && el.size() > 0) ? el.offset().top : 0;

            if (el) {
                if ($('body').hasClass('page-header-fixed')) {
                    pos = pos - $('.page-header').height();
                } else if ($('body').hasClass('page-header-top-fixed')) {
                    pos = pos - $('.page-header-top').height();
                } else if ($('body').hasClass('page-header-menu-fixed')) {
                    pos = pos - $('.page-header-menu').height();
                }
                pos = pos + (offeset ? offeset : -1 * el.height());
            }

            $('html,body').animate({
                scrollTop: pos
            }, 'slow');
        },

        initSlimScroll: function (el) {
            $(el).each(function () {
                if ($(this).attr("data-initialized")) {
                    return; // exit
                }

                var height;

                if ($(this).attr("data-height")) {
                    height = $(this).attr("data-height");
                } else {
                    height = $(this).css('height');
                }

                $(this).slimScroll({
                    allowPageScroll: true, // allow page scroll when the element scroll is ended
                    size: '7px',
                    color: ($(this).attr("data-handle-color") ? $(this).attr("data-handle-color") : '#bbb'),
                    wrapperClass: ($(this).attr("data-wrapper-class") ? $(this).attr("data-wrapper-class") : 'slimScrollDiv'),
                    railColor: ($(this).attr("data-rail-color") ? $(this).attr("data-rail-color") : '#eaeaea'),
                    position: isRTL ? 'left' : 'right',
                    height: height,
                    alwaysVisible: ($(this).attr("data-always-visible") == "1" ? true : false),
                    railVisible: ($(this).attr("data-rail-visible") == "1" ? true : false),
                    disableFadeOut: true
                });

                $(this).attr("data-initialized", "1");
            });
        },

        destroySlimScroll: function (el) {
            $(el).each(function () {
                if ($(this).attr("data-initialized") === "1") { // destroy existing instance before updating the height
                    $(this).removeAttr("data-initialized");
                    $(this).removeAttr("style");

                    var attrList = {};

                    // store the custom attribures so later we will reassign.
                    if ($(this).attr("data-handle-color")) {
                        attrList["data-handle-color"] = $(this).attr("data-handle-color");
                    }
                    if ($(this).attr("data-wrapper-class")) {
                        attrList["data-wrapper-class"] = $(this).attr("data-wrapper-class");
                    }
                    if ($(this).attr("data-rail-color")) {
                        attrList["data-rail-color"] = $(this).attr("data-rail-color");
                    }
                    if ($(this).attr("data-always-visible")) {
                        attrList["data-always-visible"] = $(this).attr("data-always-visible");
                    }
                    if ($(this).attr("data-rail-visible")) {
                        attrList["data-rail-visible"] = $(this).attr("data-rail-visible");
                    }

                    $(this).slimScroll({
                        wrapperClass: ($(this).attr("data-wrapper-class") ? $(this).attr("data-wrapper-class") : 'slimScrollDiv'),
                        destroy: true
                    });

                    var the = $(this);

                    // reassign custom attributes
                    $.each(attrList, function (key, value) {
                        the.attr(key, value);
                    });

                }
            });
        },

        // function to scroll to the top
        scrollTop: function () {
            App.scrollTo();
        },

        // To get the correct viewport width based on  http://andylangton.co.uk/articles/javascript/get-viewport-size-javascript/
        getViewPort: function () {
            var e = window,
                a = 'inner';
            if (!('innerWidth' in window)) {
                a = 'client';
                e = document.documentElement || document.body;
            }

            return {
                width: e[a + 'Width'],
                height: e[a + 'Height']
            };
        },

        // check IE8 mode
        isIE8: function () {
            return isIE8;
        },

        // check IE9 mode
        isIE9: function () {
            return isIE9;
        },

        //check RTL mode
        isRTL: function () {
            return isRTL;
        },

        // check IE8 mode
        isAngularJsApp: function () {
            return (typeof angular == 'undefined') ? false : true;
        },

        getResponsiveBreakpoint: function (size) {
            // bootstrap responsive breakpoints
            var sizes = {
                'xs': 480,     // extra small
                'sm': 768,     // small
                'md': 992,     // medium
                'lg': 1200     // large
            };

            return sizes[size] ? sizes[size] : 0;
        }
    };

}();

jQuery(document).ready(function () {
    App.init(); // init metronic core componets
});

//保存页面id的field
var pageIdField = "data-pageId";

function getPageId(element) {
    if (element instanceof jQuery) {
        return element.attr(pageIdField);
    } else {
        return $(element).attr(pageIdField);
    }
}

function findTabTitle(pageId) {
    var $ele = null;
    $(".page-tabs-content").find("a.menu_tab").each(function () {
        var $a = $(this);
        if ($a.attr(pageIdField) == pageId) {
            $ele = $a;
            return false;//退出循环
        }
    });
    return $ele;
}

function findTabPanel(pageId) {
    var $ele = null;
    $("#tab-content").find("div.tab-pane").each(function () {
        var $div = $(this);
        if ($div.attr(pageIdField) == pageId) {
            $ele = $div;
            return false;//退出循环
        }
    });
    return $ele;
}

function findIframeById(pageId) {
    return findTabPanel(pageId).children("iframe");
}

function getActivePageId() {
    var $a = $('.page-tabs-content').find('.active');
    return getPageId($a);
}

function canRemoveTab(pageId) {
    return findTabTitle(pageId).find('.fa-times-circle').size() > 0;
}

//添加tab
var addTabs = function (options) {
	
    var defaultTabOptions = {
        id: Math.random() * 200,
        urlType: "relative",
        title: "新页面"
    };

    options = $.extend(true, defaultTabOptions, options);

    if (options.urlType == "relative") {
        var basePath = window.location.protocol + '//' + window.location.host + "/";
        options.url = basePath + options.url;
    }

    var pageId = options.id;

    //判断这个id的tab是否已经存在,不存在就新建一个
    if (findTabPanel(pageId) == null) {

        //创建新TAB的title
        var $title = $('<a href="javascript:void(0);"></a>').attr(pageIdField, pageId).addClass("menu_tab");
        // title = '<a  id="tab_' + pageId + '"  data-id="' + pageId + '"  class="menu_tab" >';

        var $text = $("<span class='page_tab_title'></span>").text(options.title).appendTo($title);
        // title += '<span class="page_tab_title">' + options.title + '</span>';

        //是否允许关闭
        if (options.close) {
            var $i = $("<i class='fa fa-times-circle page_tab_close' style='cursor: pointer' onclick='closeTab(this);'></i>").attr(pageIdField, pageId).appendTo($title);
            // title += ' <i class="fa fa-remove page_tab_close" style="cursor: pointer;" data-id="' + pageId + '" onclick="closeTab(this)"></i>';
        }

        //加入TABS
        $(".page-tabs-content").append($title);

        var $tabPanel = $('<div role="tabpanel" class="tab-pane"></div>').attr(pageIdField, pageId);

        if (options.content) {
            //是否指定TAB内容
            $tabPanel.append(options.content);
        } else {
            //没有内容，使用IFRAME打开链接
            var $iframe = $("<iframe></iframe>").attr("src", options.url).css("width", "100%").attr("frameborder", "no").attr("id", "iframe_" + pageId).addClass("tab_iframe").attr(pageIdField, pageId);
            //frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes"  allowtransparency="yes"
            //iframe 加载完成事件
            $iframe.load(function () {
                //$.modal.closeLoading();
                App.fixIframeCotent();//修正高度
            });
            $tabPanel.append($iframe);
        }
        $("#tab-content").append($tabPanel);
    }
    activeTabByPageId(pageId);
};

//关闭tab
var closeTab = function (item) {
    //item可以是a标签,也可以是i标签
    //它们都有data-id属性,获取完成之后就没事了
    var pageId = getPageId(item);
    closeTabByPageId(pageId);
};

function closeTabByPageId(pageId) {
    var $title = findTabTitle(pageId);//有tab的标题
    var $tabPanel = findTabPanel(pageId);//装有iframe

    if ($title.hasClass("active")) {
        //要关闭的tab处于活动状态
        //要把active class传递给其它tab
        //优先传递给后面的tab,没有的话就传递给前一个
        var $nextTitle = $title.next();
        var activePageId;
        if ($nextTitle.size() > 0) {
            activePageId = getPageId($nextTitle);
        } else {
            activePageId = getPageId($title.prev());
        }

        setTimeout(function () {
            //某种bug，需要延迟执行
            activeTabByPageId(activePageId);
        }, 100);

    }
    $title.remove();
    $tabPanel.remove();
}

function closeTabOnly(pageId) {
    var $title = findTabTitle(pageId);//有tab的标题
    var $tabPanel = findTabPanel(pageId);//装有iframe
    $title.remove();
    $tabPanel.remove();
}

var closeCurrentTab = function () {
    var pageId = getActivePageId();
    if (canRemoveTab(pageId)) {
        closeTabByPageId(pageId);
    }
};

// 关闭商品添加、编辑并刷新商品列表
var closeChildAndActiveParentTab = function (parentPageId, isRefresh) {
    var pageId = getActivePageId();
    if (canRemoveTab(pageId)) {
    	var $title = findTabTitle(pageId);//有tab的标题
        var $tabPanel = findTabPanel(pageId);//装有iframe
        setTimeout(function () {
            activeTabByPageId(parentPageId);
            if(isRefresh) {
            	var topWindow = $(window.parent.document);
                var $contentWindow = $('.tab_iframe[data-pageid="' + parentPageId + '"]', topWindow)[0].contentWindow;
                $contentWindow.reloadTable();
            }
        }, 100);
        $title.remove();
        $tabPanel.remove();
    }
};

function refreshTabById(pageId) {
    var $iframe = findIframeById(pageId);
    var url = $iframe.attr('src');
    $iframe[0].contentWindow.location.reload(true);//带参数刷新
}

var refreshTab = function () {
    //刷新当前tab
    var pageId = getActivePageId();
    refreshTabById(pageId);
};

function getTabUrlById(pageId) {
    var $iframe = findIframeById(pageId);
    return $iframe[0].contentWindow.location.href;
}

function getTabUrl(element) {
    var pageId = getPageId(element);
    getTabUrlById(pageId);
}

//计算多个jq对象的宽度和
var calSumWidth = function (element) {
    var width = 0;
    $(element).each(function () {
        width += $(this).outerWidth(true);
    });
    return width;
};

//滚动到指定选项卡
var scrollToTab = function (element) {
    //element是tab(a标签),装有标题那个
    //div.content-tabs > div.page-tabs-content
    var marginLeftVal = calSumWidth($(element).prevAll()),//前面所有tab的总宽度
        marginRightVal = calSumWidth($(element).nextAll());//后面所有tab的总宽度
    //一些按钮(向左,向右滑动)的总宽度
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    // tab(a标签)显示区域的总宽度
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //将要滚动的长度
    var scrollVal = 0;
    if ($(".page-tabs-content").outerWidth() < visibleWidth) {
        //所有的tab都可以显示的情况
        scrollVal = 0;
    } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
        //向右滚动
        //marginRightVal(后面所有tab的总宽度)小于可视区域-(当前tab和下一个tab的宽度)
        if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
            scrollVal = marginLeftVal;
            var tabElement = element;
            while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                scrollVal -= $(tabElement).prev().outerWidth();
                tabElement = $(tabElement).prev();
            }
        }
    } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
        //向左滚动
        scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    //执行动画
    $('.page-tabs-content').animate({
        marginLeft: 0 - scrollVal + 'px'
    }, "fast");
};

//滚动条滚动到左边
var scrollTabLeft = function () {
    var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
        return false;
    } else {
        var tabElement = $(".menu_tab:first");
        var offsetVal = 0;
        while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
            offsetVal += $(tabElement).outerWidth(true);
            tabElement = $(tabElement).next();
        }
        offsetVal = 0;
        if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).prev();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
        }
    }
    $('.page-tabs-content').animate({
        marginLeft: 0 - scrollVal + 'px'
    }, "fast");
};

//滚动条滚动到右边
var scrollTabRight = function () {
    var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
        return false;
    } else {
        var tabElement = $(".menu_tab:first");
        var offsetVal = 0;
        while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
            offsetVal += $(tabElement).outerWidth(true);
            tabElement = $(tabElement).next();
        }
        offsetVal = 0;
        while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
            offsetVal += $(tabElement).outerWidth(true);
            tabElement = $(tabElement).next();
        }
        scrollVal = calSumWidth($(tabElement).prevAll());
        if (scrollVal > 0) {
            $('.page-tabs-content').animate({
                marginLeft: 0 - scrollVal + 'px'
            }, "fast");
        }
    }
};

//关闭其他选项卡
var closeOtherTabs = function (isAll) {
    if (isAll) {
        //关闭全部
        $('.page-tabs-content').children("[" + pageIdField + "]").find('.fa-times-circle').parents('a').each(function () {
            var $a = $(this);
            var pageId = getPageId($a);
            closeTabOnly(pageId);

        });
        var firstChild = $(".page-tabs-content").children().eq(0); //选中那些删不掉的第一个菜单
        if (firstChild) {
            //激活这个选项卡
            activeTabByPageId(getPageId(firstChild));
        }
    } else {
        //除此之外全部删除
        $('.page-tabs-content').children("[" + pageIdField + "]").find('.fa-times-circle').parents('a').not(".active").each(function () {
            var $a = $(this);
            var pageId = getPageId($a);
            closeTabOnly(pageId);
            var firstChild = $(".page-tabs-content").children(); //选中那些删不掉的第一个菜单
            if (firstChild.length > 1) {
                activeTabByPageId(getPageId(firstChild.eq(1)));
            }else {
                activeTabByPageId(getPageId(firstChild.eq(0)));
            }
        });
    }
};

//激活Tab,通过id
function activeTabByPageId(pageId) {
    $(".menu_tab").removeClass("active");
    $("#tab-content").find(".active").removeClass("active");
    //激活TAB
    var $title = findTabTitle(pageId).addClass('active');
    findTabPanel(pageId).addClass("active");
    scrollToTab($title[0]);
    //激活菜单
    activeMenuByPageId(pageId);
}

$(function () {
    var $tabs = $(".menuTabs");
    //点击选项卡的时候就激活tab
    $tabs.on("click", ".menu_tab", function () {
        var pageId = getPageId(this);
        activeTabByPageId(pageId);
    });

    //双击选项卡刷新页面
    $tabs.on("dblclick", ".menu_tab", function () {
        var pageId = getPageId(this);
        refreshTabById(pageId);
    });
});

// 菜单渲染
(function ($) {
    $.fn.sidebarMenu = function (options) {
        options = $.extend({}, $.fn.sidebarMenu.defaults, options || {});
        var $menu_ul = $(this);
        var level = 0;
        if (options.data) {
            init($menu_ul, options.data, level);
        }
        else {
            if (!options.url) return;
            $.getJSON(options.url, options.param, function (data) {
                init($menu_ul, data, level);
            });
        }

        function init($menu_ul, data, level) {
            $.each(data, function (i, item) {
                //如果标签是isHeader
                var $header = $('<li class="header"></li>');
                if (item.isHeader != null && item.isHeader === true) {
                    $header.append(item.text);
                    $menu_ul.append($header);
                    return;
                }

                //如果不是header
                var li = $('<li id="menu_' + item.id + '"  class="treeview " data-level="' + level + '"></li>');

                //a标签
                var $a;
                if (level > 0) {
                    $a = $('<a style="padding-left:' + (level * 20) + 'px"></a>');
                } else {
                    $a = $('<a></a>');
                }

                //图标
                var $icon = $('<i></i>');
                $icon.addClass(item.icon);

                //标题
                var $title = $('<span class="title"></span>');
                $title.addClass('menu-text').text(item.text);

                $a.append($icon);
                $a.append($title);
                $a.addClass("nav-link");

                var isOpen = item.isOpen;

                if (isOpen === true) {
                    li.addClass("active");
                }
                if (item.children && item.children.length > 0) {
                    var pullSpan = $('<span class="pull-right-container"></span>');
                    var pullIcon = $('<i class="fa fa-angle-left pull-right"></i>');
                    pullSpan.append(pullIcon);
                    $a.append(pullSpan);
                    li.append($a);

                    var menus = $('<ul></ul>');
                    menus.addClass('treeview-menu');
                    if (isOpen === true) {
                        menus.css("display", "block");
                        menus.addClass("menu-open");
                    } else {
                        menus.css("display", "none");
                    }
                    init(menus, item.children, level + 1);
                    li.append(menus);
                }
                else {

                    if (item.targetType != null && item.targetType === "blank") //代表打开新页面
                    {
                        $a.attr("href", item.url);
                        $a.attr("target", "_blank");
                    }
                    else if (item.targetType != null && item.targetType === "ajax") { //代表ajax方式打开页面
                        $a.attr("href", item.url);
                        $a.addClass("ajaxify");
                    }
                    else if (item.targetType != null && item.targetType === "iframe-tab") {
                        var href = 'addTabs({id:\'' + item.id + '\',title: \'' + item.text + '\',close: true,url: \'' + item.url + '\'});';
                        $a.attr('onclick', href);
                    }
                    else if (item.targetType != null && item.targetType === "iframe") { //代表单iframe页面
                        $a.attr("href", item.url);
                        $a.addClass("iframeOpen");
                        $("#iframe-main").addClass("tab_iframe");
                    } else {
                        $a.attr("href", item.url);
                        $a.addClass("iframeOpen");
                        $("#iframe-main").addClass("tab_iframe");
                    }
                    $a.addClass("nav-link");
                    var badge = $("<span></span>");
                    // <span class="badge badge-success">1</span>
                    if (item.tip != null && item.tip > 0) {
                        badge.addClass("label").addClass("label-success").text(item.tip);
                    }
                    $a.append(badge);
                    li.append($a);
                    
                    li.on("click", function() {
                    	var $a = $(this);
                    	$a.parent().parent().find("li.treeview").removeClass("active");
                        $a.addClass("active");
                    });
                }
                
                $menu_ul.append(li);
            });
        }

        //另外绑定菜单被点击事件,做其它动作
        $menu_ul.on("click", "li.treeview", function () {
            var $a = $(this);
            if ($a.next().size()==0) {//如果size>0,就认为它是可以展开的
                if ($(window).width() < $.AdminLTE.options.screenSizes.sm) {//小屏幕
                    //触发左边菜单栏按钮点击事件,关闭菜单栏
                    $($.AdminLTE.options.sidebarToggleSelector).click();
                }
            }
        });
    };

    $.fn.sidebarMenu.defaults = {
        url: null,
        param: null,
        data: null,
        isHeader: false
    };
})(jQuery);

/**
 * 通过ID激活菜单
 */
function activeMenuByPageId(pageId) {
	var $menu = $("#menu_" + pageId);
	var level = $menu.data("level");
	if("1" == level) {
		//同级别菜单都取消激活
		$menu.parent().parent().parent().find("li.treeview[data-level='1']").removeClass("active").removeClass("menu-open");
		//同级别菜单的上级都折叠
		$menu.parent().parent().parent().find(".treeview-menu").css("display", "none");
		//当前菜单的内容块展开
		$menu.parent().css("display", "block");
		//除当前菜单的父级按钮展开，其他父级的按钮都折叠
		$menu.parent().parent().siblings().removeClass('menu-open').end().addClass('menu-open');
		//激活当前菜单
		$menu.addClass("active");
	} else if("2" == level) {
		//当前菜单同级别菜单、无下级的菜单清除激活状态，按钮置为折叠状态
		$menu.parent().parent().parent().parent().parent().find("li.treeview[data-level='1']").removeClass("active").removeClass("menu-open");
		$menu.parent().parent().parent().parent().parent().find("li.treeview[data-level='2']").removeClass("active").removeClass("menu-open");
		//所有子菜单的内容块部分都隐藏
		$menu.parent().parent().parent().parent().parent().find(".treeview-menu").css("display", "none");
		//除当前菜单的父级按钮展开，其他父级的按钮都折叠
		$menu.parent().parent().parent().parent().parent().find("li.treeview[data-level='0']").removeClass("menu-open");
		//当前菜单的逐级上级都激活
		$menu.parent().css("display", "block").parent().addClass("menu-open active").parent().css("display", "block").parent().addClass("menu-open");
		//当前菜单激活
		$menu.addClass("active");
	}
	
}