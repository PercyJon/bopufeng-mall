if (typeof Util === "undefined") {
	Util = {};
}

/**
 * 在框架打开新的页面，如果id一样的话，就会切换到该页面并替换url
 * 
 * @param {string}
 *            url 地址，必须
 * @param {string}
 *            id [可选]菜单id，如果一样就替换现有的，否则开新的
 * @param {string}
 *            title [可选]标题
 * @param {boolean}
 *            isOutLink [可选]是否外链，默认是当前站点的地址
 */
Util.addTab = function(url, title, id, isOutLink) {
	var opt = {
		id : id,
		title : title,
		url : url,
		urlType : isOutLink ? 'abosulte' : 'relative'
	};
	Util.addTab2(opt);
};

/**
 * 在框架打开新的页面，如果id一样的话，就会切换到该页面并替换url
 * 
 * @param {object}
 *            options 配置项
 */
Util.addTab2 = function(options) {
	var defaultTabOptions = {
		id : Math.random() * 2000,
		title : "新页面",
		urlType : "relative",
		close : true,
		targetType : "iframe-tab"
	};
	options = $.extend(true, defaultTabOptions, options);
	if (Util.callParentMethod('addTabs', options) !== 0) {
		var url = options.url;
		window.open(url);
	}
};

/**
 * 关闭当前标签
 */
Util.closeTab = function() {
	Util.callParentMethod('closeCurrentTab');
};

/**
 * 刷新当前标签
 */
Util.reflushTab = function() {
	Util.callParentMethod('refreshTab');
};

/**
 * 获取当前标签的Id
 */
Util.getTabId = function() {
	return window.parent.getActivePageId();
};

/**
 * 获取当前标签的Id
 */
Util.closeChildAndActiveParentTab = function(pageId, isRefresh) {
	return window.parent.closeChildAndActiveParentTab(pageId, isRefresh);
};

/**
 * 调用父级窗口的方法，一般用在弹出窗口处理完毕之后调用。
 * 
 * @param {string}
 *            funcName 方法名称
 * @param {object}
 *            arg0 【可选】参数1
 * @param {object}
 *            arg1 【可选】参数2
 * @param {object}
 *            arg2 【可选】参数3
 * @returns {number} 0=正常，-1=未在嵌套页面使用，-2=父窗口没定义接收方法，-9=异常(日志打印到控制台)
 */
Util.callParentMethod = function(funcName, arg0, arg1, arg2) {
	if (!funcName || funcName === '') {
		throw '方法名称不能为空！';
	}
	if (window.parent === null || window.parent === window.self) {
		throw '方法名称不能为空！';
	}
	try {
		var win = window.parent;
		var n = 'win.' + funcName;
		var fn = eval(n);
		if (typeof fn === 'undefined' || fn === undefined || fn === null || typeof fn !== 'function') {
			return -2;
		}
		fn(arg0, arg1, arg2);
	} catch (e) {
		console.error(e);
		return -9;
	}
	return 0;
};