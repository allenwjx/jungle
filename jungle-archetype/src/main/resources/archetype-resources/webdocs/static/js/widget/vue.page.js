/*
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
Vue.component('vue-page', {
    template:
    '<ul class="pagination pull-right no-margin">' +
        '<li v-if="currentPage > 1" >' +
            '<a v-on:click="paginator.currentPage-- ,pageClick($event)">上一页</a>' +
        '</li>' +
        '<li v-else class="disabled">' +
            '<a>上一页</a>' +
        '</li>' +
        '<li v-for="index in indexs"  v-bind:class="{\'active\': currentPage === index }">' +
            '<a v-on:click="btnClick(index, $event)" v-html="index"></a>' +
        '</li>' +
        '<li v-if="currentPage != totalPage" >' +
            '<a v-on:click="paginator.currentPage++,pageClick($event)">下一页</a>' +
        '</li>' +
        '<li v-else class="disabled">' +
            '<a>下一页</a>' +
        '</li>' +
        '<li class="disabled">' +
            '<a>共<i v-html="totalPage"></i>页</a>' +
        '</li>' +
        '<li class="disabled">' +
            '<a>共<i class="total-record" v-text="totalCount"></i>条</a>' +
        '</li>' +
        '<li  class="disabled">' +
            '<a>下一页</a>' +
        '</li>' +
        '<li class="disabled"> <span  style="margin-left: 5px;padding:0px;border:0px;">'+
    '跳转到：<input style="width: 61px;height: 34px;margin-left:5px;font-size: 14px;font-size: 16px;border: 1px solid #ccc; -webkit-border-radius: 4px; -moz-border-radius: 4px;border-radius: 4px;text-align: center;" type="number" min="1"  v-model="paginator.changePage" />'+
'</span>'+'<span v-on:click="jumpClick($event)" class="" style="font-size:13px;border: 1px solid #ccc;padding: 7px 13px; -webkit-border-radius: 4px; -moz-border-radius: 4px;border-radius: 4px;cursor: pointer;margin-left: 5px;">'+
        'GO</span></li>'+
    '</ul>',
    props: ["paginator", "query", "size"],
    computed: {
        indexs: function(){
            var left = 1;
            if (!this.paginator) {
                return [];
            }
            var right = this.paginator.totalPage;
            var ar = [];
            if(this.paginator.totalPage>= 7){
                if(this.paginator.currentPage > 5 && this.paginator.currentPage < this.paginator.totalPage-4){
                    left = this.paginator.currentPage - 3;
                    right = parseInt(this.paginator.currentPage)+ 3;
                }else{
                    if(this.paginator.currentPage<=5){
                        left = 1;
                        right = 7;
                    }else{
                        right = this.paginator.totalPage;
                        left = this.paginator.totalPage - 6;
                    }
                }
            }
            while (left <= right){
                ar.push(left);
                left ++;
            }
            return ar;
        },
        currentPage: function() {
            if (!this.paginator) {
                return 0;
            }
            return this.paginator.currentPage;
        },
        totalPage: function() {
            if (!this.paginator) {
                return 0;
            }
            return this.paginator.totalPage;
        },
        totalCount: function() {
            if (!this.paginator) {
                return 0;
            }
            return this.paginator.totalCount;
        },
    },
    methods: {
        btnClick: function (value, event) {
            if(value != this.paginator.currentPage){
                this.paginator.currentPage = value;
                this.query(event, {page: this.paginator.currentPage, size: this.size});
            }

        },
        pageClick: function (event) {
            this.query(event, {page: this.paginator.currentPage, size: this.size});
        },
        jumpClick: function (event) {
            if(this.paginator.changePage==""){
                this.paginator.changePage=this.paginator.currentPage;
            }
            this.paginator.currentPage=this.paginator.changePage;
                this.query(event, {page: this.paginator.currentPage, size: this.size});
        }
    }
});