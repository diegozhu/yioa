var g_pre = "/a";
//本地调试状态，是空的
// var g_pre = "";
var g_home_url = "home.html";

var g_tArr = [];
g_tArr.push('<div class="time-line"><ul><li><div class="time-line-circle"></div><div class="time-line-info">');
g_tArr.push('<p class="time-line-title">开始</p>');
g_tArr.push('</div></li></ul></div>');

var g_tArr2 = [];
g_tArr2.push('<div class="time-line"><ul><li><div class="time-line-circle"></div><div class="time-line-info">');
g_tArr2.push('<div class="time-line-line"></div>');
g_tArr2.push('<p class="time-line-title">已归档</p>');
g_tArr2.push('</div></li></ul></div>');

$.ajaxSetup({
    error: function (jqXHR, textStatus, errorThrown) {
        if (jqXHR.status == 999) {
            //999 是预制的状态吗，表示未获得openid，此时将请求重定向login，获取openid
            // location.href = jqXHR.loginHref;
            var oldUrl = encodeURIComponent(window.location.href);
            var tagUrl = makeAjaxUrl("/oa/sys/login?loginredurl=" + oldUrl) ;
            // $.alert("您的账号尚未绑定，即将跳转到绑定界面。" + tagUrl, "login", function () {
            //     window.location.href = tagUrl;
            // })
            // alert(tagUrl);
            window.location.href = tagUrl;
            return;
        } else if (jqXHR.status == 998) {
            //998 是预制的状态吗，表示未绑定1，此时将请求重定向到bind界面
            // location.href = jqXHR.loginHref;
            var oldUrl = encodeURIComponent(window.location.href);
            var tagUrl = "/b/yioa/bindUser.html?bindredurl=" + oldUrl;
            // $.alert("您尚未登录，将为您登录" + tagUrl, "bind", function () {
            //     window.location.href = tagUrl;
            // })
            // alert(tagUrl);
            window.location.href = tagUrl;
            return;
        }
    }
});

var makeAjaxUrl = function (url) {
    if (url.charAt(0) != "/") {
        url = "/" + url;
    }
    return g_pre + url;
};

//登陆检查
// (function () {
//     $.getJSON(makeAjaxUrl("/oa/core/check"), function (data, textStatus, jqXHR) {
//         console.log("check pass");
//     });
// })();

$.toast.prototype.defaults.duration = 1000;
var watchOrderFun = function (orderId, restUrl, cb) {
    cb = cb || function(){};
    restUrl = restUrl.replace('{workOrderId}', orderId);
    $.post(makeAjaxUrl(restUrl), {}, function () {
        $.toptip('提交成功', 'success', cb);
    }, "json");
}

var checkOutOrderFun = function (restUrl) {

    $.post(makeAjaxUrl(restUrl), {}, function (data) {
        $.toptip('提交成功', 'success', function () {
        });

    }, "json");
}

var getUrlParameter = function (name) {
    var file = location.pathname.split('/').pop(), i;
    var param = JSON.parse(localStorage.getItem(file)) || {};
    var paramLowercase = {};
    for(i in param){
        param[i] = decodeURI(param[i]);
        paramLowercase[i.toLowerCase()] = param[i];
    }
    var pairs = location.search.substring(1) == '' ? [] : location.search.substring(1).split('&');
    for(i in pairs) {
        var pair = pairs[i].split('=');
        param[pair[0]] = decodeURI(pair[1]);
        paramLowercase[pair[0].toLowerCase()] = param[pair[0]];
    }
    for(i in param){
        param[i] = param[i] == "undefined" || param[i] == "null" ? '' : decodeURI(param[i]);
        paramLowercase[i.toLowerCase()] = param[i];
    }
    var res = name ? param[name] || paramLowercase[name.toLowerCase()] : param;
    return res;
};

var UrlParam = getUrlParameter();

$(document).on("click", '.yioa-popup-ok', function () {
    $(".weui-search-bar__input").val('');
});

$(document).on("click", '.close-popup', function () {
    $(".weui-search-bar__input").val('');
});

/**
 * 这个函数需要优化下，根据当前环节，订单id（），订单类型，决定能提供什么操作
 * 环节信息其实不需要
 * @param flowStage
 * @param orderId
 * @param orderType
 * @returns {[*,*,*]}
 */
var getOperationByFlowStage = function (flowStage, orderId, orderType) {
    // var reArr = [{"title": "评论", "event": "comment", "restUrl": "/oa/workorder/comment/{workOrderId}"}, {
    //     "title": "关注",
    //     "event": "watch",
    //     "restUrl": "/oa/core/watch/{workOrderId}"
    // }, {"title": "取消关注", "event": "unwatch", "restUrl": "/oa/core/unwatch/{workOrderId}"}];

    var reArr = [{"title": "评论", "event": "comment", "restUrl": "/oa/workorder/comment/{workOrderId}"}];


    var r = getUrlParameter("r");
    $.ajax({
        async: false,
        dataType: "json",
        url: makeAjaxUrl("/"+orderType+"/workorder/candeal/" + orderId),
        data: {},
        success: function (data, textStatus, jqXHR) {
            r = data == "1" ? "0" : "1";
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("error "+ jqXHR);
        }
    });

    if (r == "1") {
        return reArr;
    }

    if (flowStage == "FLOW_MILESTONE") {
        reArr.push({
            "title": "阶段回单",
            "event": "MILESTONE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
        reArr.push({
            "title": "回单",
            "event": "COMPLETE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
    }
    else if (flowStage == "FLOW_COMPLETE") {
        reArr.push({
            "title": "回单",
            "event": "COMPLETE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
    }
    else if (flowStage == "FLOW_AUDIT") {
        reArr.push({
            "title": "回单审核",
            "event": "AUDIT_PASS",
            "auditNoEvent": "AUDIT_NO",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
    }
    else if (flowStage == "CAR_FLOW_AUDIT") {
        reArr.push({
            "title": "回单审核",
            "event": "CAR_AUDIT_PASS",
            "auditNoEvent": "CAR_AUDIT_NO",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
    }
    else if (flowStage == "CAR_FLOW_DRIVER_COMPLETE") {
        reArr.push({
            "title": "阶段回单",
            "event": "CAR_DRIVER_COMPLETE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
    }
    else if (flowStage == "CAR_FLOW_COMPLETE") {
        reArr.push({
            "title": "回单",
            "event": "CAR_COMPLETE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
    } else if(flowStage == "MARKET_FLOW_MILESTONE"){
        reArr.push({
            "title": "阶段回单",
            "event": "MARKET_MILESTONE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
        reArr.push({
            "title": "回单",
            "event": "MARKET_COMPLETE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
        reArr.push({
           "title": "签到",
            "event": "signIn"
        });
    } else if(flowStage == "MARKET_FLOW_COMPLETE"){
        reArr.push({
            "title": "回单",
            "event": "MARKET_COMPLETE",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
        reArr.push({
            "title": "签到",
            "event": "signIn"
        });
    } else if(flowStage == "MARKET_FLOW_AUDIT"){
        reArr.push({
            "title": "回单审核",
            "event": "MARKET_AUDIT_PASS",
            "auditNoEvent": "MARKET_AUDIT_NO",
            "restUrl": "/oa/workorder/flow/submit/{workOrderId}/{event}/{orderType}"
        });
        reArr.push({
            "title": "签到",
            "event": "signIn"
        });
    }
    return reArr;
}

function makeExpandingArea(el) {
    var setStyle = function (el) {
        el.style.height = 'auto';
        el.style.height = el.scrollHeight + 'px';
        // console.log(el.scrollHeight);
    }
    var delayedResize = function (el) {
        window.setTimeout(function () {
                setStyle(el)
            },
            0);
    }
    if (el.addEventListener) {
        el.addEventListener('input', function () {
            setStyle(el)
        }, false);
        setStyle(el)
    } else if (el.attachEvent) {
        el.attachEvent('onpropertychange', function () {
            setStyle(el)
        });
        setStyle(el)
    }
    if (window.VBArray && window.addEventListener) { //IE9
        el.attachEvent("onkeydown", function () {
            var key = window.event.keyCode;
            if (key == 8 || key == 46) delayedResize(el);

        });
        el.attachEvent("oncut", function () {
            delayedResize(el);
        }); //处理粘贴
    }
}
$(function () {
    if ($(".yioa-textarea").length > 0) {
        makeExpandingArea($(".yioa-textarea")[0]);
    }
});

var bindTextChange = function (selector, callback) {
    var $selector = $(selector);
    var _IE = (function () {
        var v = 3,
            div = document.createElement('div'),
            all = div.getElementsByTagName('i');
        while (
            div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->',
                all[0]
            );
        return v > 4 ? v : false;
    })();

    if (_IE && _IE < 9) {
        $selector.on('propertychange', function () {
            window.event.propertyName === 'value' && callback.call(this);
        });
        $selector.on('input', function () {
            window.event.propertyName === 'value' && callback.call(this);
        });

    } else {
        $selector.on('input', callback);
        $selector.on('propertychange', callback);
        if (_IE === 9) {
            var callbackWrapper = function () {
                callback.call($selector[0]);
            }
            $selector.on('focus', function () {
                document.addEventListener('selectionchange', callbackWrapper);
            }).on('blur', function () {
                document.removeEventListener('selectionchange', callbackWrapper);
            });
        }
    }
};

typeof console == "undefined" && (console = {log:function(){},error:function(){}});
String.prototype.stripTags =  function() {return this.replace(/<\/?[^>]+>/gi, '');}
String.prototype.toDate = function(){ return new Date(this.replace(/-/gi,"/")); }
Date.prototype.toDate = function(){return this;}
String.prototype.String = function(){return this;}
String.prototype.trim=function(s){ if(s==undefined || s == null||s =="") return this.replace(/(^\s*)|(\s*$)/g, "");  var res = this;  while(res.endsWith(s))  res = res.substring(0,res.length-s.length); while(res.startsWith(s)) res = res.substring(s.length,res.length); return res;}
String.prototype.contains = function(s){return this.indexOf(s) !== -1;}
String.prototype.startsWith = function(prefix) { return prefix == undefined || prefix == null || prefix =="" ? false : this.slice(0, prefix.length) == prefix; }
String.prototype.endsWith = function(suffix) { return suffix == undefined || suffix == null || suffix =="" ? false : this.indexOf(suffix, this.length - suffix.length) !== -1;};
String.prototype.join = function(){return this;};
Date.prototype.toString = function (format){ 
	var d = this;
	var res =  (format || "yyyy-MM-dd hh:mm:ss").replace("yyyy",d.getFullYear()).replace("YYYY",d.getFullYear());
	res = res.replace("MM",d.getMonth() < 9 ? "0"+(d.getMonth()+1) : (d.getMonth()+1));
	res = res.replace("dd",d.getDate()<10?"0"+d.getDate() : d.getDate()).replace("DD",d.getDate()<10?"0"+d.getDate() : d.getDate());
	res = res.replace("hh",d.getHours()<10?"0"+d.getHours():d.getHours()).replace("HH",d.getHours()<10?"0"+d.getHours():d.getHours());
	res = res.replace("mm",d.getMinutes()<10?"0"+d.getMinutes():d.getMinutes());
	res = res.replace("ss",d.getSeconds()<10?"0"+d.getSeconds():d.getSeconds());
	var millisecond = d.getMilliseconds();
	millisecond = millisecond<10 ? "00"+millisecond : millisecond;
	millisecond = millisecond<100 ? "0"+millisecond : millisecond;
	res = res.replace("SSS",millisecond);
	return res;
};

String.prototype.offsetDay = function(day){
    return this.toDate().offsetDay(day);
};

String.prototype.offsetMinute = function(day){
    return this.toDate().offsetMinute(day);
};

Date.prototype.offsetDay = function(day){
	return new Date(this.getTime() + day * 24 * 60 * 60 * 1000);
};

Date.prototype.offsetMinute = function(minute){
	return new Date(this.getTime() + minute * 60 * 1000);
};

Date.valid = function(date){
	return date != null && date != undefined && typeof date.getTime == "function" && typeof date.getTime() == "number" && !isNaN(date.getTime());
}
Date.prototype.valid = function(){
	return Date.valid(this);
}
Date.prototype.format = Date.prototype.toString;
String.prototype.format = function(format){return this.toDate().toString(format);}
String.prototype.containAll = function(){
	args = arguments[0] instanceof Array ? arguments[0] : arguments
	for(var i =0;i<args.length;i++)
		if(!this.contains(args[i]))
			return false;
	return true;
}
String.prototype.containAny = function(){
	args = arguments[0] instanceof Array ? arguments[0] : arguments
	for(var i =0;i<args.length;i++)
		if(this.contains(args[i]))
			return true;
	return false;
}


var TaskLevels = ['重要-紧急','重要-不紧急','不重要-紧急','不重要-不紧急'];


$(document).ajaxError(function(res, ajax) {
    console.error(res, ajax);
    // $.modal({ text: ajax.responseJSON.message, title: '出错啦' });
    // setTimeout(function() {
    //     $.closeModal();
    // }, 2000);
});


window.onerror = function(errorMessage, scriptURI, lineNumber,columnNumber,errorObj) {
    if(!debug){
        return ;
    }
    var res = [];
    res.push("错误信息：" , errorMessage);
    res.push("出错文件：" , scriptURI);
    res.push("出错行号：" , lineNumber);
    res.push("出错列号：" , columnNumber);
    res.push("错误详情：" , errorObj);
    $.modal({ text: res.join('<br >'), title: '出错啦' });
    setTimeout(function() {
        $.closeModal();
    }, 5000);
};

debug = false;

function updateUrl(params) {
    var file = location.pathname.split('/').pop();
    params = params || {};
    var orignalParams = JSON.parse(localStorage.getItem(file)) || {};
    var ps = location.search.substring(1) == '' ? [] : location.search.substring(1).split('&');
    for(var i in ps) {
        var p = ps[i].split('=');
        orignalParams[p[0]] = p[1];
    }
    for(var j in params) {
        orignalParams[j] = params[j] === undefined || params[j] === null ? '' : params[j];
    }
    var search = [];
    for(var m in orignalParams) {
        search.push(m + '=' + orignalParams[m]);
    }
    var url = file + location.hash + '?' + search.join('&');
    localStorage.setItem(file, JSON.stringify(orignalParams));
    window.history.replaceState('',"", url);  
}

function propose(title, selected, options, cb) {
    var actionsArr = [];
    for(var i in options) {
        (function(opt){
            if(typeof opt == 'string') {
                actionsArr.push({
                    text: (selected == opt ? '✓&nbsp;' : '') + opt,
                    className: selected == opt ? "color-primary" : "",
                    onClick: function () {
                        cb(opt); 
                    }
                });
            } else {
                actionsArr.push({
                    text: (selected == opt.k || selected == opt.v ? '✓&nbsp;' : '') + opt.k,
                    className: selected == opt.k || selected == opt.v ? "color-primary" : "",
                    onClick: function () {
                        cb(opt.v || opt.k); 
                    }
                });
            }
        })(options[i]);
    }
    $.actions({ title: title, actions: actionsArr});
    if(window.top !== window) {
      $('.weui-actionsheet').css('bottom', '50px');   
    }
}


function go(url) {
    getWindow().location.href = url;
}

function getDocument() {
    return getWindow().document;
}

function getWindow() {
    var win = window.top;
    while(win != win.top) {
        win = win.top;
    }
    return win;
}

function isIniframe() {
    return window.top !== window;
}