/**
 * smartPop
 *
 * Copyright (c) 2011 Cho Yong Gu (@inidu2)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */

/**
 * 레이어로 팝업 띄우기 - html or url
 *
 * 특징
 * 1. 브라우저 호환
 * 2. 깔끔한 스크롤바 처리
 * 3. 브라우저 크기 변경시 레이어 팝업 자동 중앙 정렬
 * 4. url 페이지를 띄울 경우 프레임 크기 자동 조절
 * 
 * 목적
 * 내용에 상관없이 깔끔한 스크롤바 처리하기
 * 브라우저 크기에 상관없이 중앙에 띄우기
 * 
 * 제한사항
 * 가로 최대 1300, 세로 5000 - 더 크게할 수 있으나 테두리 이미지 깨짐
 * 
 * 사용법
 * 1. html 내용 보여주기
 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, html: '<h1>smartPop</h1> 여기에 보여줄 내용' });
 * 2. url 페이지 띄우기
 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, url: 'smartPop.html 여기에 보여줄 페이지' });
 *      세로 크기는 불러오는 페이지 크기에 맞게 자동으로 저절됨
 * 3. 추가 여백을 줘서 띄우기 - 페이지 특성에 따라 자동조절이 안맞을 경우 사용
 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, revise: 150, url: 'smartPop.html 여기에 보여줄 페이지' });
 *      revise - 추가 보정 높이값
 * 4. 높이값 확인 로그
 *      $.smartPop.open({title: '스마트팝', width: 500, height: 500, log: true, url: 'smartPop.html 여기에 보여줄 페이지' });
 *      log: true 설정
 *
 * 기본 옵션
 * $.smartPop.defaults = {
 *     title       : 'smartPop',
 *     background  : '#fff',
 *     opacity     : .7,
 *     width       : '720px',
 *     height      : '500px',
 *     html        : '',
 *     url         : '',
 *     revise      : 0,
 *     log         : false
 * };
 * 
 * 
 * 
 * @name $.smartPop
 * @author Pharos @inidu2
 */
    
;(function($) {
    var ie6     = $.browser.msie && ($.browser.version < 8);
    var innerH  = window.innerHeight;
    
    $.smartPop = {
        isInstall : false,
        opts : {},
        open : function(options) {
            this.opts = $.extend({}, $.smartPop.defaults, options);
            this.install();
            this.resize();

            $('html').css({ marginRight: '15px', display: 'block', overflow: 'hidden', overflowY: 'hidden' });
            $('#smartPop').show();
            if(this.opts.log) $('#smartPop_log').show();
        },
        resize : function() {
            this.log(this.opts.width + ' x ' + this.opts.height);
            this.log('revise : ' + this.opts.revise);
            this.log('background : ' + this.opts.background);
            this.log('opacity : ' + this.opts.opacity);
            this.log('');

            $('#smartPop_container').width(this.opts.width);
            
            this.resizeHeight(this.opts.height);
        },
        resizeHeight : function(h) {
            this.log('resizeHeight : ' + h);
            if(ie6) {
                $('body').attr({ scroll: 'no' }); // ie7에서 overflow 적용안됨 
                innerH = document.documentElement.clientHeight;
            }
            
            // 위치설정
            if(this.opts.position == 'center') {
                var t;
                t = (h < innerH) ? (innerH - h) / 2 : 10;
                $('#smartPop_container').css({ marginLeft: 'auto', marginTop: t + 'px' });
            } else {
                $('#smartPop_container').css({ marginLeft: this.opts.left, marginTop: this.opts.top });
            }
            
            // 높이설정
            h = h + this.opts.revise;
            this.log('revise + : ' + h);
            $('#smartPop_container').height(h);
            
            h = h - parseInt($('#smartPop_top').height());   
            this.log('smartPop_top - : ' + h);         
            $('#smartPop_bottom').height(h);

            var margin = parseInt($('#smartPop_bottom_content').css('paddingTop')) + parseInt($('#smartPop_bottom_content').css('paddingBottom'));
            h = h - margin;
            this.log('margin - : ' + h);
            $('#smartPop_bottom_content').height(h);
            if(this.opts.url == '') {
                $('#smartPop_content').html(this.opts.html).height(h).show();
                $('#smartPop_frame').hide();
            } else {
                $('#smartPop_content').hide();
                $('#smartPop_frame').height(h).show();
            }
            $('#smartPop_loading').hide();
            this.log('');
        },
        install : function() {
            if(this.isInstall == false) {
                var body                    = $('body');
                var smartPop_overlay        = $('<div />').attr('id', 'smartPop_overlay').css({ opacity: this.opts.opacity, background: this.opts.background });
                var smartPop                = $('<div />').attr('id', 'smartPop');
                var smartPop_container      = $('<div />').attr('id', 'smartPop_container');
                var smartPop_top            = $('<div />').attr('id', 'smartPop_top');
                var smartPop_top_content    = $('<div />').attr('id', 'smartPop_top_content');
                var smartPop_bottom         = $('<div />').attr('id', 'smartPop_bottom');
                var smartPop_bottom_content = $('<div />').attr('id', 'smartPop_bottom_content');
                var smartPop_content        = $('<div />').attr('id', 'smartPop_content');
                var smartPop_close          = $('<div />').attr('id', 'smartPop_close');
                var smartPop_loading        = $('<div />').attr('id', 'smartPop_loading');
                var smartPop_frame          = $('<iframe />').attr({ id: 'smartPop_frame', frameBorder: 0, scrolling: 'no', name: 'smartPop_frame' });
                
                smartPop_top.append(smartPop_close).append(smartPop_top_content).appendTo(smartPop_container);
                                
                smartPop.append($('<div />').attr('id', 'smartPop_log'));            
                smartPop_bottom_content.append(smartPop_content).append(smartPop_frame);
                smartPop_bottom.append(smartPop_bottom_content).appendTo(smartPop_container);
                smartPop_container.append(smartPop_loading).appendTo(smartPop);
                body.append(smartPop_overlay).append(smartPop);
                this.isInstall = true;
            } else {
                //$('#smartPop_frame').attr({ src : this.opts.url });
                $('#smartPop').show();
                $('#smartPop_overlay').show();
            }
            
            if(this.opts.url != '') {
                $.smartPop.resizeHeight(400);   // 사이즈 보정 - 새로고침할 경우 크기 늘어나는거 방지
                $('#smartPop_frame').attr({ src : this.opts.url }).load(function () {
                
                    var h = $(this).contents().height() - 2;
                    $.smartPop.resizeHeight(h + 50);
                });
            }

            if(this.opts.bodyClose) {
                $('body').bind('click', function(event) {
                    if (!event) event = window.event;
                    var target = (event.target) ? event.target : event.srcElement;
                    event.stopPropagation(); // 이벤트 버블링 전파를 막음
                    if(target.id == 'smartPop') { $.smartPop.close(); }
                });
            }
            
            $('#smartPop_close').click(function() {
                $.smartPop.close();
            });
        },
        close : function() {
            if(ie6) {
                $('body').attr({ scroll: 'yes' });
            }
            $('html').css({ marginRight: 0, display: '', overflowY: 'scroll'});

            $('#smartPop_frame').attr('src', '');
            $('#smartPop').hide();
            $('#smartPop_overlay').hide();            
        },
        log : function(msg) {
            var log = $('#smartPop_log').html();
            $('#smartPop_log').html(log + '<br />' + msg);
        }
    };
    
    $.smartPop.defaults = {
        position    : 'center',
        left        : '310px',
        top         : '10px',
        bodyClose   : true,
        title       : 'smartPop',
        background  : '#fff',
        opacity     : .7,
        width       : '720px',
        height      : '500px',
        html        : '',
        url         : '',
        revise      : 0,
        log         : false
    };
})(jQuery);
