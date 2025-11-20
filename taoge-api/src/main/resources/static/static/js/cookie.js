!function() {
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
    !$.cookie && ($['cookie'] = cookie)
}()
