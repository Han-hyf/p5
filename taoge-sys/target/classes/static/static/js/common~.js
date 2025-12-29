var currentPageNum = 1; // 公用分页当前页号
var currentPageSize = 10; // 公用分页当前每页显示条数
var _globalCurrentName = ""; // 页面菜单名称

function getUrlParam(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r!=null) return unescape(r[2]); return null; //返回参数值
}
/*
设置form值
*/
$.fn.setFormValue = function(param) {
    var form = $(this);
    $(this).find("[name!='']").each(function(i, ele){
        ele = $(ele);
        var name = ele.attr("name")
            ,value = param[name];
        if (name&&value!=undefined) {
            ele.val(value);
            if (ele[0].tagName.toLowerCase()=='select') {
                // select
                ele.attr("value", value);
            }
        }
    });
    return param;
};
function getParam(paramName) {
    paramValue = "";
    isFound = false;
    paramName = paramName.toLowerCase();
    var arrSource = this.location.search.substring(1, this.location.search.length).split("&");
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        if (paramName == "returnurl") {
            var retIndex = this.location.search.toLowerCase().indexOf('returnurl=');
            if (retIndex > -1) {
                var returnUrl = unescape(this.location.search.substring(retIndex + 10, this.location.search.length));
                if ((returnUrl.indexOf("http") != 0) && returnUrl != "" && returnUrl.indexOf(location.host.toLowerCase()) == 0) returnUrl = "http://" + returnUrl;
                return returnUrl;
            }
        }
        i = 0;
        while (i < arrSource.length && !isFound) {
            if (arrSource[i].indexOf("=") > 0) {
                if (arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase()) {
                    paramValue = arrSource[i].toLowerCase().split(paramName + "=")[1];
                    paramValue = arrSource[i].substr(paramName.length + 1, paramValue.length);
                    isFound = true;
                }
            }
            i++;
        }
    }
    return paramValue;
}

//js中的字符串正常显示在HTML页面中
String.prototype.displayHtml = function () {
    //将字符串转换成数组
    var strArr = this.split('');
    //HTML页面特殊字符显示，空格本质不是，但多个空格时浏览器默认只显示一个，所以替换
    var htmlChar = "&<>";
    for (var i = 0; i < str.length; i++) {
        //查找是否含有特殊的HTML字符
        if (htmlChar.indexOf(str.charAt(i)) != -1) {
            //如果存在，则将它们转换成对应的HTML实体
            switch (str.charAt(i)) {
                case '<':
                    strArr.splice(i, 1, '<');
                    break;
                case '>':
                    strArr.splice(i, 1, '>');
                    break;
                case '&':
                    strArr.splice(i, 1, '&');
            }
        }
    }
    return strArr.join('');
};

//被改的字符包括单引号 (')、双引号 (")、反斜线 backslash (\) 以及空字符NULL
function strReplace(v) {
    var str = v.toString().replace(new RegExp('(["\"])', 'g'), "\\\"");
    return str;
}

/* 下拉选择框的时候需要切换name值
    e为当前对象
   *如果默认值value为空，text值为全部，去掉name属性和值
*/
function changeName(e,nameVal) {
    if($(e).find("option:selected").val() == ""){
        $(e).removeAttr('name');
    }else{
        $(e).attr('name',nameVal);
    }
}
/*价格转换
--显示的时候除，后台传传递过来的值除100；显示元的数字
--提交的时候，把输入的数乘100，发送给后台
number是值，type是类型判断，需要 0--乘|| 1--除
*/
function priceConversion(number,type) {
    var priceNumber=0;
    if(typeof number !== 'number'){
        return false;
    }
     if(type){
         if(number==0) return 0;
         priceNumber = __accDiv(number,100).toFixed(2);
     }else{
         priceNumber = __accMul(number,100);
     }
     return priceNumber
}
function numberAccMul(arg1, arg2){
    return __accMul(arg1, arg2);
}
// 乘法
function __accMul(arg1, arg2) {
    var m = 0,
        s1 = arg1.toString(),
        s2 = arg2.toString();
    try {
        m += s1.split(".")[1].length
    } catch (e) {}
    try {
        m += s2.split(".")[1].length
    } catch (e) {}
    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
}
// 除法
function __accDiv(arg1, arg2) {
    var t1 = 0,
        t2 = 0,
        r1, r2;
    try {
        t1 = arg1.toString().split(".")[1].length
    } catch (e) {}
    try {
        t2 = arg2.toString().split(".")[1].length
    } catch (e) {}
    r1 = Number(arg1.toString().replace(".", ""))
    r2 = Number(arg2.toString().replace(".", ""))
    return (r1 / r2) * Math.pow(10, t2 - t1);
}

/**
 * 公用ajax方法
 *
 * @param url 请求地址
 * @param data json格式数据
 * @param success function类型,成功回调方法
 * @param option 其他ajax请求参数
 */
function ajax(url, data, success, option) {
    var option = $.extend({
      url: url,
      type: 'POST',
      data: data,
      dataType: 'json',
      success: function(data){
        success(data);
      }
    }, option);

    $.ajax(option);
}

/**
 * 格式化日期
 * @param fmt 格式化表达式
 * @returns
 */
Date.prototype.Format = function (fmt) {
	if (!this || !this.toJSON()) {
		return "";
	}
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

! function() {
    var cache = {};

    function tmpl(str, data) {
        var fn = cache[str] ? cache[str] :
            (cache[str] = new Function("data",
                "var p=[];" +
                "p.push('" +
                str
                .replace(/&lt;!--/g, "<!--")
                .replace(/--&gt;/g, "-->")
                .replace(/[\r\t\n]/g, " ")
                .split("<!--").join("\t")
                .replace(/((^|-->)[^\t]*)'/g, "$1\r")
                .replace(/\t=(.*?)-->/g, "',$1,'")
                .split("\t").join("');")
                .split("-->").join("p.push('")
                    .split("\r").join("\\'") + "');return p.join('');"));
        return data ? fn(data) : fn
    }

    $ && ($.tmpl = tmpl)
}();

function goBack() {
    history.back(-1);
}

function resetPage() {
    location.href = location.href;
}

//数字限制
function oninputNumber(e) {
    $(e).val($(e).val().replace(/[^\d-]/g,''));
}
/**
 * 将form表单转换为 json对象
 * @param form form的jquery对象
 * @returns {___anonymous6302_6303}
 */
$.fn.getFormParam = function() {
	var form = $(this);
	var param = {};
    $(this).find("[name!='']").each(function(i, n){
        if ($(n).attr("name")) {
            if ($(n)[0].type.indexOf("select") == -1) {
                param[$(n).attr("name")] = $(n).val();
            }else {
                param[$(n).attr("name")] = $(n).find("option:selected").val();
            }
        }
    });
	return param;
};
$.fn.getFormParamV2 = function() {
    var form = $(this);
    var param = {};
    form.find('input,select,[name!=""]').each(function(i, n){
        if ($(n).attr("name")) {
            if ($(n)[0].type.indexOf("select") == -1) {
                param[$(n).attr("name")] = $(n).val();
            }else {
                param[$(n).attr("name")] = $(n).find("option:selected").val();
            }
        }
    });
    return param;
};
$.getCheckedValues = function(option) {
	var param = $.extend({
		attr : "yk-chkId",
		chkId : null,
		checkList : null,
		not : null,
		valAttr : null,
		obj : false
	},option);
	var values = [];
	if (!param.checkList) {
		checkList = $("[" + param.attr + "='" + param.chkId + "']");
	}
	if (option.not) {
		checkList = checkList.not(option.not);
	}
	checkList.each(function(i, n){
		if (!!$(n)[0].checked) {
			if (option.obj) {
				values[values.length] = $(n);
			}else if (param.valAttr&& $(n).attr(param.valAttr)) {
				values[values.length] = $(n).attr(param.valAttr);
			}else if ($(n).val()) {
				values[values.length] = $(n).val();
			}
		}
	});
	return values;
};
/**
 * 全选
 */
$.fn.checkAll = function() {
	$(this).click(function(){
		var checked = !!$(this)[0].checked;
		var chkId = $(this).attr("yk-chkId");
		$("[yk-chkId='" + chkId + "']").not(this).each(function(i, n){
			$(n)[0].checked = checked;
		});
	});
};
/**
 * 获取选中值
 */
$.fn.getCheckedValues = function(option) {
	var param = $.extend({
		not : this
	}, option);
	return $.getCheckedValues(param);
};

/**
 * 判断是否为对象
 */
$.isObject = function() {
	if (obj) for (var key in obj) if (key) return true;
	return false;
};

/**
 * 判断是否为对象
 */
function isObject(obj) {
	if (obj) for (var key in obj) if (key) return true;
	return false;
}

/**
 * 格式化为年月日
 */
$.formatToYMD = function(value) {
	return value ? new Date(value).Format("yyyy-MM-dd") : "";
};

/**
 * 格式化为年月日 时分秒
 */
$.formatToYMDHMS = function(value) {
	return value ? new Date(value).Format("yyyy-MM-dd") : "";
};

/**
 * 设置页面title
 */
$.setRcitTitle = function(title) {
	$("#mainContentTitle").html(title);
};

/**
 * 设置Host
 */
$.setHost = function(url){
	$.Host = url;
};
/**
 * 判断对象是否为空
 * @param obj
 * @returns {Boolean}
 */
function commonIsEmpty(obj)
{
    for (var name in obj)
    {
        return false;
    }
    return true;
}
$.setCurrentMenuName = function(name) {
    _globalCurrentName = name;
    $("#currentMenuName").html(name);
};

$.getCurrentMenuName = function () {
    return _globalCurrentName;
};

/**
 * 公共上传组件
 * @param fileId 上传的文件id
 * @param imgId 显示图片id
 * @param urlId 提交后台的input的id
 */
function upload(fileId, callback,option) {
    if (!$("#" + fileId).val() || $("#" + fileId).val() == "") {
        $.alert("请选择上传文件");
        return;
    }
    $.ajaxFileUpload(
        {
            url: '/api/upload', //用于文件上传的服务器端请求地址
            fileElementId:fileId,
            success: function (data, status)  //服务器成功响应处理函数
            {
                //回填及回显图片地址
                //callback(data)
                callback(data, option)
            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
                $.alert(e);
            }
        }
    );
}

function checkNum(event) {
    var event = event || window.event;  // 标准化事件对象
    return event.keyCode >= 48 && event.keyCode <= 57;
}


