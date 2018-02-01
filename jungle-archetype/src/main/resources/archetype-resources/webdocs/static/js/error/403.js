var errorDesc;
$(document).ready(function () {
    errorDesc = new Vue({
        el: '#permissionDetail',
        data: {
            //权限信息
            permissionDesc: '向磊34839'
        },
        ready: function () {
            this.init();
        },
        methods: {
            init: function () {
                var vm = this;
                $.ajax({
                    type: 'GET',
                    url: __ctx + '/common/getPermissionDesc',
                    dataType: 'json',
                    success: function (data) {
                        $("#permissionDesc").html(data);
                    }
                });
            }
        }
    });
});