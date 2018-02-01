(function($){
    $.fn.tfh = function(){

        var method = (arguments.length === 2) ? arguments[0] : ((arguments.length === 1 && typeof arguments[0] === 'string' ? arguments[0] : undefined));
        var options = $.extend({
            trigger: 100,
            top: 50
        },(arguments.length === 2) ? arguments[1] : ((arguments.length === 1 && typeof arguments[0] === 'object' ? arguments[0] : {} )));
        this.width = function(){
            return this.find('thead').attr('data-tmp-width',parseInt(this.find('thead').css('width'))).find('*').each(function(){
                $(this).attr('data-tmp-width',parseInt($(this).css('width')));
            }).end().end();
        };

        this.fix = function(){
            return this.find('.table-fixed-head-thead').css({
                'top': options.top + 'px',
                'position': 'fixed'
            }).end();
        };

        this.clone = function(){
            return this.find('thead').clone(true).prependTo(this).addClass('table-fixed-head-thead')
                .addClass('table')
                .addClass('table-hover')
                .addClass('table-bordered')
                .addClass('table-striped').end().end().removeAttr('data-tmp-width').find('*').removeAttr('data-tmp-width').end().end();
        };

        this.build = function(){
            return this.tfh('width').tfh('clone').find('[data-tmp-width]').each(function(){
                $(this).css({
                    'width': $(this).data('tmp-width') + 'px',
                    'minWidth': $(this).data('tmp-width') + 'px',
                    'maxWidth': $(this).data('tmp-width') + 'px',
                });
            }).removeAttr('data-tmp-width').end().tfh('fix', options);
        };

        this.kill = function(){
            this.find('.table-fixed-head-thead').remove();
        };

        this.show = function(){
            return this.addClass('fixed').find('thead').css('visibility','visible').not('.table-fixed-head-thead').css('visibility','hidden').end().end();
        };

        this.hide = function(){
            return this.removeClass('fixed').find('thead').css('visibility','hidden').not('.table-fixed-head-thead').css('visibility','visible').end().end();
        };

        if(method !== undefined){
            return this[method].call($(this));
        } else {
            var table = this.build.call($(this),options);
            var tableWidth = table.css('width');
            var tableScrollLeft = table.position().left;

            if(options.trigger != 0 && $(document).scrollTop() > options.trigger) {
                table.tfh('show');
            } else {
                table.tfh('hide');
            }

            var resizeTimer;
            var scrollTimer;
            $(window).resize(function(){
                window.clearInterval(resizeTimer);
                resizeTimer = window.setInterval(function() {
                    window.clearInterval(resizeTimer);
                    if(tableWidth !== table.css('width')) {
                        tableWidth = table.css('width');
                        table.tfh('kill');
                        table.tfh(options);
                    }
                }, 1000);
            }).scroll(function(){
                window.clearInterval(scrollTimer);
                scrollTimer = window.setInterval(function() {
                    window.clearInterval(scrollTimer);
                    if(tableWidth !== table.css('width')) {
                        tableWidth = table.css('width');
                        table.tfh('kill');
                        table.tfh(options);
                    }
                }, 100);

                if(options.trigger!=0 && $(document).scrollTop() > options.trigger) {
                    table.tfh('show');
                    table.find('.table-fixed-head-thead').css('left',(tableScrollLeft - $(document).scrollLeft()) + 'px');
                } else {
                    table.tfh('hide');
                }
            });
        }
    }
    $(document).ready(function(){
        $('table.table-fixed-head').each(function(){
            $(this).tfh({
                trigger: ($(this).data('table-fixed-head-trigger') !== undefined ? $(this).data('table-fixed-head-trigger') : 0),
                top: ($(this).data('table-fixed-head-top') !== undefined ? $(this).data('table-fixed-head-top') : $(this).position().top)
            });
        });
    });
}(jQuery));