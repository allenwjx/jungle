/*
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */

/**
 * Vuejs 封装的select和duallistbox控件.默认使用bootstrap风格.
 * 依赖jquery, select2, duallistbox.
 * select2的参数配置参见: https://select2.github.io/options.html
 * duallistbox的参数配置,参见: https://github.com/istvan-ujjmeszaros/bootstrap-duallistbox
 *
 * @param id {String} 控件的id.
 * @param name {String} 控件的name.
 * @param disabled {Boolean} 是否禁用.
 * @param class {String}
 *    样式字符串,默认使用bootstrap, select2自带的有default, classic样式.
 * @param model {Object}
 *    绑定值, 默认是单向绑定,如果需要同步更新其他绑定该model的控件,需要在component上设置绑定类型sync, 例如 :model.sync="model".
 * @param transfer {Boolean} 是否为transfer控件.
 * @param options {Array}
 *    可选参数, 可以通过select的自己的data参数进行设置.
 *    select选项的数组, 格式如下:"[{text: 'name1', value: 'val1'}, {text: 'name2', value: 'val2'}]".
 * @param configs
 *    select2的选项, 如果options参数没有设置,那必须设置configs的data参数, 默认会进行绑定数据,目前还没有测试url方式.
 * @param multiple 是否为多选.
 *
 * @param size 控件显示数量
 *
 * @author huzhongyuan 2016-04-23
 */
Vue.component('vue-select', {
    template: '<select v-bind:class="class" v-model="model" v-bind:multiple="multiple" v-bind:size="size" v-bind:name="name" v-bind:id="id" v-bind:disabled="disabled">' +
     '<option></option> <option v-for="item in options" v-text="item.text" value="{{item.value}}"></option>' +
    '</select>',
    props: {
        id: {
            type: String,
            required: true
        },
        name: {
            type: String,
            required: false
        },
        disabled: {
            type: Boolean,
            required: false
        },
        class: {
            type: String,
            required: false,
            default: function () {
                return "js-states form-control";
            }
        },
        model: {
            required: true
        },
        transfer: {
            type: Boolean,
            required: false,
            default: function () {
                return false;
            }
        },
        options: {
            type: Array,
            required: false
        },
        configs: {
            type: Object,
            required: false
        },
        multiple: {
            type: Boolean,
            required: false,
            default: function () {
                return false;
            }
        },
        size: {
            type: Number,
            required: false,
            default: function () {
                return 8;
            }
        }
    },
    beforeCompile: function () {
        this.isChanging = false;
        this.control = null;
        this.isModelChanging = false;
    },
    watch: {
        "options": function (val, oldVal) {
            this.control.trigger('change');
            if (this.transfer) {
                this.control.bootstrapDualListbox('refresh');
            }
        },
        "model": function (val, oldVal) {
            if (!this.isChanging) {
                this.isChanging = true;
                this.control.val(val).trigger("change");
                this.isChanging = false;
            }
            if (!val) {
                val = '';
                this.control.val(val).trigger("change");
            }
        }
    },
    ready: function () {
        var defaultOptions = {};
        if (this.transfer) {
            debugger;
            //TODO duallistbox 的默认参数,待确认.
        } else {
            defaultOptions = {
                theme: "bootstrap",
                placeholder: "请选择...",
                allowClear: true
            };
        }
        $.extend(defaultOptions, this.configs);
        this.control = $(this.$el);


        if (this.transfer) {
            this.control.bootstrapDualListbox(defaultOptions);
        } else {
            this.control.select2(defaultOptions);
        }

        var me = this;
        this.control.on("change", function (e) {
            if (!me.isChanging || me.isModelChanging) {
                me.isChanging = true;
                me.model = me.control.val();
                me.$nextTick(function () {
                    me.isChanging = false;
                });
                me.$dispatch(this.id + '-change', me.model);
            }
        });
    }
});