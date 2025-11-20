jQuery.extend({
    tools: {
        'tmpl': function() {
            var cache = {}
            return function(str, data) {
                var fn = cache[str] ? cache[str] :
                    (cache[str] = new Function("data",
                        "var p=[];" +
                        "p.push('" +
                        str
                        .replace(/&lt;!--/g, "<!--")
                        .replace(/--&gt;/g, "-->")
                        .replace(/[\r\t\n]/g, " ")
                        .split("<!--").join("\t")
                        .replace(/((^|-->)[^\t]*)'/g, "$1\r")
                        .replace(/\t=(.*?)-->/g, "',$1,'")
                        .split("\t").join("');")
                        .split("-->").join("p.push('")
                        .split("\r").join("\\'") + "');return p.join('');"))
                return data ? fn(data) : fn
            }
        }(),
        'url': function() {
            var url = location.search,
                g = {};
            if (url.indexOf("?") != -1) {
                var str = url.substr(1),
                    arr = str.indexOf("&") != -1 ? str.split("&") : [str];
                for (var i = 0; i < arr.length; i++) {
                    var tmp = arr[i].split("=");
                    g[tmp[0]] = decodeURI(tmp[1]);
                }
            }
            return g;
        },
        'cookie': function() {
            var Cookie = {
                read: function(name) {
                    var value = document.cookie.match('(?:^|;)\\s*' + name + '=([^;]*)');
                    return (value) ? decodeURIComponent(value[1]) : null;
                },
                write: function(value) {
                    var str = value.name + '=' + encodeURIComponent(value.value);
                    if (value.domain) {
                        str += '; domain=' + value.domain;
                    }
                    if (value.path) {
                        str += '; path=' + value.path;
                    }
                    if (value.day) {
                        var time = new Date();
                        time.setTime(time.getTime() + value.day * 24 * 60 * 60 * 1000);
                        str += '; expires=' + time.toGMTString();
                    }
                    document.cookie = str;
                    return;
                },
                dispose: function(name) {
                    var str = this.read(name);
                    this.write({
                        name: name,
                        value: str,
                        day: -1
                    });
                    return;
                }
            };

            function cookie(name, value, options) {
                if (typeof value != 'undefined') {
                    if (value === null) {
                        return Cookie.dispose(name);
                    } else {
                        options = options || {};
                        options.name = name;
                        options.value = value;
                        return Cookie.write(options);
                    }
                } else {
                    return Cookie.read(name);
                }
            }
            return cookie
        }(),
        'fullNumber': function(n) {
            return n > 9 ? n : '0' + n
        },
        'computeDate': function(date) {
            return {
                date: date,
                t: date.getTime(),
                Y: date.getFullYear(),
                M: date.getMonth(),
                D: date.getDate(),
                h: date.getHours(),
                m: date.getMinutes(),
                s: date.getSeconds()
            }
        },
        'getData': function(date, currentDate) {
            var arr = []
            var json = this.computeDate(date ? new Date(date) : new Date())
            json['arr'] = []
            json['currentDate'] = this.computeDate(currentDate ? new Date(currentDate) : new Date())

            /**当月一号为起始值**/
            var begin = new Date([json.Y, this.fullNumber(json.M + 1)].join('-'))
            var tmp = (new Date([json.currentDate.Y, this.fullNumber(json.currentDate.M + 1), this.fullNumber(json.currentDate.D)].join('-'))).getTime()
            for (var i = 0; i < 42; i++) {
                var n = i - begin.getDay()
                var t = this.computeDate(new Date(begin.getTime() + 86400000 * (i - begin.getDay())))
                    /**补充跨年月差**/
                var m = (t.Y - json.Y) * 12
                json.arr.push([t.date, t.M - json.M + m, tmp == t.t])
            }
            return json
        },
        /*
        options
        @starDay         最小日期，Date string,(new Date()).toJSON()
        @endDay          最大日期，Date string
        @direction       浮层定位方向，默认下方：T,上
        */
        'datePicker': function(el, tmpl, options) {
            var _this = this;
            var picker = $('<div>');
            var data;
            var hide;
            options = $.extend({}, options)
            var direction = options.direction ? options.direction.toString().toUpperCase() : '';
            $('body').append(picker.hide())
            // 清空按钮
            if($(el).attr('type')!='hidden' && el.parent().find('.glyphicon-remove.btn-close').length<1){
                el.after('<div class="glyphicon glyphicon-remove btn-close" style="right:10px;top:8px;margin-left:-10px;transform: translateY(-6px);color:#999;cursor: pointer;display: inline-block;"></div>')
                setTimeout(function(){
                    el.next().click(function(){
                        el.val('');
                    })
                },50)
            }
            function changeDate(prev) {
                if (prev) {
                    if (data.M == 0) {
                        data.M = 12
                        data.Y--
                    }
                } else {
                    if (data.M == 11) {
                        data.M = 1
                        data.Y++
                    } else {
                        data.M += 2
                    }
                }
                data = _this.getData([data.Y, _this.fullNumber(data.M)].join('-'), el.val())
                render()
            }

            function setDate(tmp) {
                var hms = []
                var YMD = [tmp.Y, _this.fullNumber(tmp.M + 1), _this.fullNumber(tmp.D)].join('-')
                picker.find('select').each(function(i, el) {
                    hms.push(el.value)
                })
                el.val(hms.length ? [YMD, hms.join(':')].join(' ') : YMD)
            }

            function render() {
                data.todayTime = new Date();
                data.todayTime.setHours(0);
                data.todayTime.setMinutes(0);
                data.todayTime.setSeconds(0);
                data.afterToday = options.afterToday;
                data.startDay = options.startDay && new Date(options.startDay).getTime();
                data.endDay = options.endDay && new Date(options.endDay).getTime();
                var top = el.offset().top + el.outerHeight();
                var myHeight = 240;
                if($(tmpl).find('.time').length){
                    myHeight += 42;
                }
                if(direction=='T'){
                    top = el.offset().top - el.outerHeight() - myHeight;
                }
                picker.html(_this.tmpl(tmpl, data)).css({
                    top: top,
                    left: el.offset().left,
                    display: 'block',
                    position: 'absolute',
                    "z-index": 1200
                })
            }
            el.on('click', function() {
                hide = false
                data = _this.getData(el.val(), el.val())
                render()
            }).on('mouseout',function(){
                hide = true
            })
            picker.on('click', 'li', function() {
                if ($(this).hasClass("disabled")) {
                    return;
                }
                setDate(_this.computeDate(data.arr[$(this).index()][0]))
                picker.hide()
            }).on('click', 'span', function() {
                changeDate(1)
            }).on('click', 'em', function() {
                changeDate(0)
            }).on('change', 'select', function() {
                setDate(_this.computeDate(new Date([data.currentDate.Y, _this.fullNumber(data.currentDate.M + 1), _this.fullNumber(data.currentDate.D)].join('-'))))
            }).hover(function(){
                hide = false
            },function(){
                hide = true
            })

            $(document).on('click', function() {
                if (hide) {
                    picker.hide()
                }
            })
            $(el).parents('.modal').scroll( function() {
                if (hide) {
                    picker.hide()
                }
            })
        }
    }
})
