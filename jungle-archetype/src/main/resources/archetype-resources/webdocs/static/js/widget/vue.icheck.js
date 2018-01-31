/*
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */

/**
 * Created by hzy24985 on 2016/5/30.
 */
/**
 * 封装icheck控件.
 * @param value 绑定值, 默认是单向绑定,如果需要同步更新其他绑定该model的控件,需要在component上设置绑定类型sync, 例如 :model.sync="model".
 * @param type {String} {checkbox|radio} 控件类型,默认是checkbox.
 * @param options {Object} icheck的options, 默认设置了checkboxClass和radioClass,使用了minimal-orange样式.
 * @param disabled {String} 只要设置值了，就会认为是disabled.
 * @param trueValue {String} 必须是String类型，否则icheck识别不出来.
 * @param falseValue {String} 必须是String类型，否则icheck识别不出来, 默认是空字符串.
 */
Vue.component('vue-icheck', {
    template: "<input v-bind:type='type' v-model='value' name='{{name}}' :true-value='trueValue' :false-value='falseValue' value='{{trueValue}}' />",
    props: {
        value: [String, Number, Array],
        type: {
            type: String,
            default: "checkbox"
        },
        options: {
            type: Object,
            default: {}
        },
        name: String,
        disabled: String,
        trueValue: String,
        falseValue: {
            type: String,
            required: false,
            default: ""
        }
    },
    ready: function () {
        var $checkbox = $(this.$el);
        var vm = this;
        if (typeof(this.disabled) != "undefined" || this.disabled == "true") {
            $checkbox.prop('disabled', true);
        }

        var defaultOptions = {
            checkboxClass: 'icheckbox_minimal-orange',
            radioClass: 'iradio_minimal-orange',
            increaseArea: '20%'
        };

        $.extend(vm.options, defaultOptions);

        $checkbox.iCheck(vm.options);

        $checkbox.on('ifToggled', function () {
            if ($checkbox.prop('checked')) {
                vm.checked();
            } else {
                vm.unchecked();
            }
        });
    },
    watch: {
        'value': function (val, oldVal) {
            var $checkbox = $(this.$el);
            $checkbox.iCheck('update');
        }
    },
    methods: {
        checked: function () {
            var vm = this;
            if (vm.value instanceof Array) {
                var value = vm.value.find(function (e, i) {
                    if (e == vm.trueValue)
                        return e
                });
                if (!value) {
                    vm.value.$set(vm.value.length, vm.trueValue);
                }
            } else {
                vm.value = vm.trueValue;
            }
        },
        unchecked: function () {
            var vm = this;
            if (vm.value instanceof Array) {
                var index = vm.value.findIndex(function (e, i) {
                    if (e == vm.trueValue)
                        return e
                });
                if (index >= 0) {
                    vm.value.$remove(vm.value[index]);
                }
                return;
            } else {
                vm.value = vm.falseValue;
            }
        },
        disable: function () {
            var $checkbox = $(this.$el);
            $checkbox.iCheck('disable');
        },
        enable: function () {
            var $checkbox = $(this.$el);
            $checkbox.iCheck('enable');
        }
    }

});

