/** 设置全局ajax处理 */
$.ajaxSetup({
	complete : function(XMLHttpRequest, textStatus) {
		if (textStatus == 'timeout') {
			$.modal.alertWarning("服务器超时，请稍后再试！");
			$.modal.closeLoading();
		} else if (textStatus == "parsererror" || textStatus == "error") {
			$.modal.alertWarning("服务器错误，请联系管理员！");
			$.modal.closeLoading();
		}
	}
});

layer.config({
	extend: 'moon/style.css',
	skin: 'layer-ext-moon'
});

/**
 * [通过参数名获取url中的参数值]
 * 示例URL:http://htmlJsTest/getrequest.html?uid=admin&rid=1&fid=2&name=小明
 * @param  {[string]} queryName [参数名]
 * @return {[string]}           [参数值]
 */
function GetQueryValue(queryName) {
	var reg = new RegExp("(^|&)" + queryName + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return decodeURI(r[2]);
	} else {
		return null;
	}
}

// 页面跳转
$("body").delegate(".toNewTab", "click", function() {
	var tabUrl = $(this).attr('tab-url');
	window.location.href = tabUrl + "?r=" + new Date().getTime();
});

//带有保存取消按钮
$("body").delegate(".dialog", "click", function() {
	var me = this;
	var url = $(this).attr('data-url');
	var width = $(me).attr('data-width');
	var height = $(me).attr('data-height');
	var title = $(me).attr('data-title');
	$.modal.dialog_open(title, url, width, height);
});

//带有保存取消按钮
$("body").delegate(".dialogFull", "click", function() {
	var me = this;
	var url = $(this).attr('data-url');
	var width = $(me).attr('data-width');
	var height = $(me).attr('data-height');
	var title = $(me).attr('data-title');
	$.modal.dialog_openFull(title, url, width, height);
});

//自定义保存取消
$("body").delegate(".dialog_detail", "click", function() {
	var me = this;
	var url = $(this).attr('data-url');
	var width = $(me).attr('data-width');
	var height = $(me).attr('data-height');
	var title = $(me).attr('data-title');
	$.modal.dialog_detail(title, url, width, height, false);
});

//多选删除
$("body").delegate("*[delete-batch-url]", "click", function() {
	var deleteBatchUrl = $(this).attr('delete-batch-url');
	var ids = [];
	$.each($("input:checked"), function(n, i) {
		if ($(this).val() != 'root') {
			ids.push($(this).val());
		}
	});
	if (ids.length == 0) {
		$.modal.msgWarning("请选择要删除的记录!");
	} else {
		$.modal.confirm('确认删除选中的【' + ids.length + '】条记录?', function(index){
			$.post(deleteBatchUrl, {id : ids.join()}, function(json) {
				if (json.code == 200) {
					window.location.reload();
				} else {
					$.modal.msgWarning(json.msg);
				}
			});
		});
	}
});

var now = new Date();
$('.daterang').daterangepicker({
	"showWeekNumbers" : true,
	"showISOWeekNumbers" : true,
	"ranges" : {
		"今天" : [ now, now ],
		"昨天" : [ getBoforeDate(1), getBoforeDate(1) ],
		"最近7天" : [ getBoforeDate(7), now ],
		"最近30天" : [ getBoforeDate(30), now ],
		"本月" : [ getBoforeMonth(0, 1), getBoforeMonth(0, 31) ],
		"上个月" : [ getBoforeMonth(1, 1), getBoforeMonth(1, 31) ],
		"最近三个月" : [ getBoforeMonth(2, 1), getBoforeMonth(0, 31) ]
	},
	"locale" : {
		"format" : "YYYY/MM/DD",
		"separator" : "-",
		"applyLabel" : "应用",
		"cancelLabel" : "取消",
		"fromLabel" : "From",
		"toLabel" : "To",
		"customRangeLabel" : "自定义",
		"weekLabel" : "W",
		"daysOfWeek" : [ "日", "一", "二", "三", "四", "五", "六" ],
		"monthNames" : [ "一月", "二月", "三月", "四月", "五月", "六月",
				"七月", "八月", "九月", "十月", "十一月", "十二月" ],
		"firstDay" : 1
	},
	"alwaysShowCalendars" : true,
	"autoUpdateInput" : false,
	"opens" : "right",
	"buttonClasses" : "btn btn-sm"
},
function(start, end, label) {
	console.log("New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')");
});

$('.daterang').on('apply.daterangepicker', function(ev, picker) {
	$(this).val(picker.startDate.format('YYYY.MM.DD') + ' - ' + picker.endDate.format('YYYY.MM.DD'));
});

$('.daterang').on('cancel.daterangepicker', function(ev, picker) {
	$(this).val('');
});

function getBoforeDate(before) {
	var now = new Date();
	now.setDate(now.getDate() - before);
	return now;
}

function getBoforeMonth(beforeMonth, day) {
	var now = new Date();
	now.setDate(day);
	now.setMonth(now.getMonth() - beforeMonth);
	return now;
}

$('.form_datetime').datetimepicker({
	startDate : pd_getDateStr(new Date()),
	format : 'yyyy-mm-dd hh:ii',
	autoclose : true,
	todayBtn : true,
	minuteStep : 5,
	minView : 0,
	language : 'zh-CN'
});

function pd_getDateStr(date) {
	var year = date.getFullYear(), month = date.getMonth() + 1, day = date
			.getDate(), hour = date.getHours(), min = date.getMinutes(), sec = date
			.getSeconds();
	if (min > 30) {
		++hour;
		min = 0;
		sec = 0;
	}
	var newTime = year + '-' + (month < 10 ? ("0" + month) : month) + '-'
			+ (day < 10 ? ("0" + day) : day) + ' '
			+ (hour < 10 ? ("0" + hour) : hour) + ':'
			+ (min < 10 ? ("0" + min) : min) + ':'
			+ (sec < 10 ? ("0" + sec) : sec);
	return newTime;
}

// 导出报表
function exportTo(fileName) {
	var date = new Date();
	$(".table").table2excel({
		exclude : ".noExl",
		name : "Excel Document Name",
		filename : fileName,
		exclude_img : true,
		exclude_links : true,
		exclude_inputs : true
	});
}

// select2插件全局初始化
$(".select2").select2();
$(".select2").on("select2:select", function(e) {
	var id = $(this).attr("id");
	$("#" + id + "-error").remove();
});

// icheck插件全局初始化
$('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
	checkboxClass : 'icheckbox_minimal-blue',
	radioClass : 'iradio_minimal-blue'
});

//回到顶部绑定
$(function() {
	if ($.fn.toTop !== undefined) {
		$('#scroll-up').toTop();
	}
});

(function($) {
	'use strict';
	$.fn.toTop = function(opt) {
		var elem = this;
		var win = $(window);
		var doc = $('html, body');
		var options = $.extend({
			autohide : true,
			offset : 50,
			speed : 500,
			position : true,
			right : 15,
			bottom : 5
		}, opt);
		elem.css({
			'cursor' : 'pointer'
		});
		if (options.autohide) {
			elem.css('display', 'none');
		}
		if (options.position) {
			elem.css({
				'position' : 'fixed',
				'right' : options.right,
				'bottom' : options.bottom,
			});
		}
		elem.click(function() {
			doc.animate({
				scrollTop : 0
			}, options.speed);
		});
		win.scroll(function() {
			var scrolling = win.scrollTop();
			if (options.autohide) {
				if (scrolling > options.offset) {
					elem.fadeIn(options.speed);
				} else
					elem.fadeOut(options.speed);
			}
		});
	};
})(jQuery);