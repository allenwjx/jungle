jQuery.extend({

    /** 消除空格方法 */
    trimObject: function (data) {
        $.each(data, function (i, n) {
            data[i] = $.trim(n);
        });
        return data;
    },

    /** 重置分页对象 */
    resetCurrentPage: function (paginator) {
        paginator.currentPage = 1;
    },

    /** 校正新增/修改标题 */
    correctTitle: function (id) {
        if (id) {
            $('#myModalLabel').prepend('编辑');
        } else {
            $('#myModalLabel').prepend('新增');
        }
    },

    /** 监听回车事件 */
    enterKey: function (toBeFunc) {
        $('.enterKey').keydown(function (e) {
            if (e.keyCode == 13) {
                toBeFunc();
            }
        });
    },

    /** 获取URL参数 */
    getUrlParam: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }

});