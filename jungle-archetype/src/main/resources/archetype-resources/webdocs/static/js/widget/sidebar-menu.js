$(document).ready(function () {
    $('a[href="' + window.location.pathname + '"]').parent().addClass("active");
    $('a[href="' + window.location.pathname + '"]').parent().parent().parent().addClass("start active open");
    $('a[href="' + window.location.pathname + '"]').parent().parent().parent().parent().parent().addClass("start active open");
    $('a[href="' + window.location.pathname + '"]').parent().parent().parent().parent().parent().parent().parent().addClass("start active open");
    new Vue({
        el: "#leftHead",
        data: {
            infos: {},
            menuName: "",
        },
        watch: {
            menuName: {
                handler: function (val, oldVal) {
                    this.selectMenu();
                },
                deep: true
            }
        },
        ready: function () {
            $("#ulMenu").children("li").addClass("menu_first");
            $(".ul-sec").children("li").addClass("menu_second");
            $(".ul-third").children("li").addClass("menu_third");
            $(".ul-four").children("li").addClass("menu_four");
        },
        methods: {
            publish: function () {
                $("#publishModal").modal({
                    show: true,
                    remote: __ctx + "/publish/publishInfo",
                    backdrop: 'static'
                }).draggable({
                    cursor: "move",
                    handle: '.modal-header'
                });
            },
            /** 点击日志按钮 */
            preLog: function (id) {
                var self = this;
                $("#logModal").modal({
                    show: true,
                    remote: __ctx + "/log/PUBLISH/1",
                    backdrop: true
                });
            },
            selectMenu: function () {
                var filter = this.menuName;
                if (filter) {
                    $("li", $("#ulMenu")).hide();
                    var menu_first = $(".menu_first").find("a:Contains(" + filter + ")").parent();
                    menu_first.show();
                    $("li", menu_first).show();

                    var menu_second = $(".menu_second").find("a:Contains(" + filter + ")").parent();
                    menu_second.show();
                    $("li", menu_second).show();
                    menu_second.parent().parent().show();

                    var menu_third = $(".menu_third").find("a:Contains(" + filter + ")").parent();
                    menu_third.show();
                    $("li", menu_third).show();
                    menu_third.parent().parent().show();
                    menu_third.parent().parent().parent().parent().show();

                    var menu_four = $(".menu_four").find("a:Contains(" + filter + ")").parent();
                    menu_four.show();
                    $("li", menu_four).show();
                    menu_four.parent().parent().show();
                    menu_four.parent().parent().parent().parent().show();
                    menu_four.parent().parent().parent().parent().parent().parent().show();
                } else {
                    $("li", $("#ulMenu")).show();
                }
            }
        }
    });
    $("#publishModal").on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
    });
});