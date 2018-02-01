/**
 * Created by hzy24985 on 2016/4/25.
 */

/**
 * 将后台传来的数字型日期转成日期字符串, 格式按照momentjs类库做转换.
 * eg. data | toDate 'YYYY-MM-DD'
 *
 * @param format {String} 日期格式, 参照momentjs.
 * @author hzy24985
 */
Vue.filter('toDate', {
    read: function (value, format) {
        if (value) {
            return moment(value).format(format);
        } else {
            return value;
        }
    },
    write: function (value, format) {
        return value;
    }
});
/**
 * datas arr
 * 将 datas 里面对应的value显示为对应的text 页面过滤
 */
Vue.filter('toViewStr', {
    read: function (value, datas) {
        var text;
        $(datas).each(function (i, e) {
            if (value == e.value) {
                text = e.text;
            }
        });
        if (!text) {
            text = value;
        }
        return text;
    }
});
/**
 * 枚举值转换, 单项绑定，不会回写值.
 * @param value     {String}        传过来的值
 * @param all       {Array}         列表数据
 * @param feild     {String}  可选  列表中的属性名称, default: value
 * @param showFeild {String}  可选  显示成列表中的属性名称  default: text
 * @author hzy24985
 */
Vue.filter('enumFormat', function (value, all, feild, showFeild) {
    var text, feildName = feild, showName = showFeild;
    if (!feild) {
        feildName = 'value';
    }
    if (!showFeild) {
        showName = 'text';
    }
    $(all).each(function (index, item) {
        if (item[feildName] == value) {
            text = item[showName];
        }
    });
    return text;
});

/** 售卖班期可读化 */
Vue.filter('saleDayReadable', {
	read : function(value) {
		var resp = '';
		for (var i = 0; i < value.length; i++) {
			if (value.charAt(i) == '1') {
				switch (i) {
				case 0:
					resp += '/日';
					break;
				case 1:
					resp += '/一';
					break;
				case 2:
					resp += '/二';
					break;
				case 3:
					resp += '/三';
					break;
				case 4:
					resp += '/四';
					break;
				case 5:
					resp += '/五';
					break;
				case 6:
					resp += '/六';
					break;
				}
			}
		}
		return resp.substr(1);
	}
});

//定价配置 留点返点显示
Vue.filter('adjustQuantityReadable', function (adjustPriceType, adjustPriceQuantity) {
    if (adjustPriceType == "留点") {
        return "留点:" + adjustPriceQuantity + "%";
    }
    if (adjustPriceType == "返点") {
        return "返点:" + adjustPriceQuantity + "%";
    }
    if (adjustPriceType == "直减") {
        return "直减:" + adjustPriceQuantity;
    }
    if (adjustPriceType == "直加") {
        return "直加:" + adjustPriceQuantity;
    }
    return "";
});

//订单详情，变更信息
Vue.filter('reverseOrderFilter', function (type) {
    if (type == "1") {
        return "退票";
    }
    if (type == "2") {
        return "改签";
    }
    return "";
});

//订单详情，变更信息
Vue.filter('stopoverFilter', function (stop) {
    var result = '';
    if (stop != null && stop != undefined) {
        [].forEach.call(stop, function (item) {
            result = result + item.cityName;
        });
    }
    return result;
});

//过滤指定字符传串
Vue.filter('replaceFilter', function (content, mark) {
    if (content != null && content != undefined) {
        return content.replace(mark, "");
    }
    return content;
});

//航变来源
Vue.filter('convertDelaySource', function (type) {
    if (type == "0") {
        return "默认";
    }
    if (type == "1") {
        return "清Q";
    }
    if (type == "2") {
        return "Crtip";
    }
    if (type == "3") {
        return "供应商平台录入格式化航段";
    }
    if (type == "4") {
        return "业务后台录入格式化航段";
    }
    if (type == "5") {
        return "供应商平台录入PNR内容";
    }
    if (type == "6") {
        return "测试";
    }
    return type;
});

//航变类型
Vue.filter('convertDelayType', function (type) {
    if (type == "0") {
        return "默认";
    }
    if (type == "1") {
        return "延误";
    }
    if (type == "2") {
        return "取消";
    }
    if (type == "3") {
        return "未知";
    }
    return type;
});

//变更单  变更类型（退-1，改-2，航-3）
Vue.filter('convertReverseType', function (type) {
    if (type == "1") {
        return "退";
    }
    if (type == "2") {
        return "改";
    }
    if (type == "3") {
        return "变";
    }
    return type;
});

//变更单  	子类型（机票-1，邮寄-2，保险-3，卡券-4)
Vue.filter('convertReverseSubType', function (type) {
    if (type == "1") {
        return "机票";
    }
    if (type == "2") {
        return "邮寄";
    }
    if (type == "3") {
        return "保险";
    }
    if (type == "4") {
        return "卡券";
    }
    return type;
});


/**
 * 过滤掉供应商名称前面的冒号和id
 * @author wyy32641
 */
Vue.filter('supplierName', function (value) {
    var values;
    if (value !='' && value != undefined) {
        values = value.split(':');
        if(values.length >1){
            return values[1];
        }
    }
    return value;
});