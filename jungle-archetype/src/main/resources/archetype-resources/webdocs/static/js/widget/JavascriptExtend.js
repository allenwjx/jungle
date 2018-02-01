Date.prototype.format = function (format) {
    var o =
               {
                   "M+": this.getMonth() + 1, //month   
                   "d+": this.getDate(),    //day   
                   "h+": this.getHours(),   //hour   
                   "m+": this.getMinutes(), //minute   
                   "s+": this.getSeconds(), //second   
                   "q+": Math.floor((this.getMonth() + 3) / 3), //quarter  
                   "S": this.getMilliseconds() //millisecond  
               }

    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

Date.prototype.JSONDateToJSDate =function(jsondate) {
    var date = new Date(parseInt(jsondate.replace("/Date(", "").replace(")/", ""), 10));
    return date;
}

String.prototype.toJSDate = function () {
    var date = new Date(parseInt(this.replace("/Date(", "").replace(")/", ""), 10));
    return date;
}


Date.prototype.maxDayOfDate = function () {
    var myDate = this;
    var ary = myDate.toArray();
    var date1 = (new Date(ary[0], ary[1], 1));
    var date2 = date1.dateAdd('m', 1);
    var dd = date1.getMonth() + 1;
    var date2 = (new Date(ary[0], dd, 1));
    var result = parseInt((date2 - date1) / 86400000);
    return result;
}

Date.prototype.toArray = function (date2) {
    var myDate = date2;
    var myArray = Array();
    myArray[0] = myDate.getFullYear();
    myArray[1] = myDate.getMonth();
    myArray[2] = myDate.getDate();
    myArray[3] = myDate.getHours();
    myArray[4] = myDate.getMinutes();
    myArray[5] = myDate.getSeconds();
    return myArray;
}

Date.prototype.dateAdd = function (strInterval, Number) {
    var dtTmp = new Date();
    switch (strInterval) {
        case 's': return new Date(Date.parse(dtTmp) + (1000 * Number));
        case 'n': return new Date(Date.parse(dtTmp) + (60000 * Number));
        case 'h': return new Date(Date.parse(dtTmp) + (3600000 * Number));
        case 'd': return new Date(Date.parse(dtTmp) + (86400000 * Number));
        case 'w': return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));
        case 'q': return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number * 3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'm': return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'y': return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    }
}

Date.prototype.dateDiff = function (strInterval, dtStart, dtEnd) {
    if (typeof dtEnd == 'string')//如果是字符串转换为日期型
    {
        dtEnd = StringToDate(dtEnd);
    }
    if (typeof dtStart == 'string')//如果是字符串转换为日期型
    {
        dtStart = StringToDate(dtStart);
    }
    switch (strInterval) {
        case 's': return parseInt((dtEnd - dtStart) / 1000);
        case 'n': return parseInt((dtEnd - dtStart) / 60000);
        case 'h': return parseInt((dtEnd - dtStart) / 3600000);
        case 'd': return parseInt((dtEnd - dtStart) / 86400000);
        case 'w': return parseInt((dtEnd - dtStart) / (86400000 * 7));
        case 'm': return (dtEnd.getMonth() + 1) + ((dtEnd.getFullYear() - dtStart.getFullYear()) * 12) - (dtStart.getMonth() + 1);
        case 'y': return dtEnd.getFullYear() - dtStart.getFullYear();
    }
}

// 给日期类对象添加日期差方法，返回日期与diff参数日期的时间差，单位为天
Date.prototype.diff = function (date) {
    return (this.getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
}

Date.prototype.diffminute = function (date) {
    return (this.getTime() - date.getTime()) / (60 * 1000);
}