(function($) {
    var $WebUpload = function(element, options) {
        this.options = options || {};

        this.$wrap = element;
        this.$ul = $('<ul class="filelist"></ul>');
        // 文件容器
        this.$queue = this.$ul.appendTo(this.$wrap.find('.queueList')),

            // 状态栏，包括进度和控制按钮
            this.$statusBar = this.$wrap.find('.statusBar'),

            // 文件总体选择信息。
            this.$info = this.$statusBar.find('.info'),

            // 上传按钮
            this.$upload = this.$wrap.find('.uploadBtn'),

            // 没选择文件之前的内容。
            this.$placeHolder = this.$wrap.find('.placeholder'),
            //上传按钮
            this.$filePicker = this.$wrap.find('.filePicker'),

            //继续上传按钮
            this.$filePickerJx = this.$wrap.find('.filePickerJx'),

            // 总体进度条
            this.$progress = this.$statusBar.find('.progress').hide(),

            // 添加的文件数量
            this.fileCount = 0;

        // 添加的文件总大小
        this.fileSize = 0;

        // 优化retina, 在retina下这个值是2
        this.ratio = window.devicePixelRatio || 1;

        // 缩略图大小
        this.thumbnailWidth = 110 * this.ratio;
        this.thumbnailHeight = 110 * this.ratio;

        // 可能有pedding, ready, uploading, confirm, done.
        this.state = 'pedding';

        // 所有文件的进度信息，key为file id
        this.percentages = {};

        this.supportTransition = (function() {
            var s = document.createElement('p').style,
                r = 'transition' in s ||
                    'WebkitTransition' in s ||
                    'MozTransition' in s ||
                    'msTransition' in s ||
                    'OTransition' in s;
            s = null;
            return r;
        })();
        // this.$wrap.sortable();
        // this.$ul.sortable();
    };

    $WebUpload.prototype = {
        /**
         * 初始化webUploader
         */
        init: function() {
            if (!WebUploader.Uploader.support()) {
                alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
                throw new Error('WebUploader does not support the browser you are using.');
            }
            var uploader = this.create();
            // 添加“添加文件”的按钮，
            uploader.addButton({
                id: this.$filePickerJx,
                label: '继续添加'
            });


            this.bindEvent(uploader);
            var _this = this;
            this.$upload.on('click', function() {
                if ($(this).hasClass('disabled')) {
                    return false;
                }

                if (_this.state === 'ready') {
                    uploader.upload();
                } else if (_this.state === 'paused') {
                    uploader.upload();
                } else if (_this.state === 'uploading') {
                    uploader.stop();
                }
            });

            this.$info.on('click', '.retry', function() {
                uploader.retry();
            });

            this.$info.on('click', '.ignore', function() {
                // alert( 'todo' );
                alert('删除照片，重新上传');
            });

            this.$upload.addClass('state-' + this.state);

            return uploader;
        },
        /**
         * 创建webuploader对象
         */
        create: function() {
            var defaultSettings = {
                pick: {
                    id: this.$filePicker,
                    label: '点击选择文件'
                },
                dnd: '#' + this.$wrap.attr('id') + ' .queueList',
                paste: this.$wrap,
                //去重， 根据文件名字、文件大小和最后修改时间来生成hash Key.
                duplicate: false,
                // swf文件路径
                swf: '/LieYuanQu/css/lib/webuploader/Uploader.swf',
                //-------------------------------以上参数不建议覆盖--------------------------
                accept: {
                    title: 'Images',
                    extensions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: '.gif,.jpg,.jpeg,.bmp,.png'
                },
                //是否禁掉整个页面的拖拽功能
                disableGlobalDnd: true,
                //是否要分片处理大文件上传
                chunked: true,
                //文件上传地址
                server: '',
                //文件最大个数
                fileNumLimit: 10,
                //总文件大小限制 默认值【undefined】
                fileSizeLimit: 5 * 1024 * 1024, // 200 M
                //验证单个文件大小是否超出限制，默认值【undefined】
                fileSingleSizeLimit: 50 * 1024 * 1024, // 50 M
                threads: 1

            };
            var _thisSettings = $.extend(defaultSettings, this.options);
            var webUploader = WebUploader.create(defaultSettings);
            return webUploader;
        },
        /**
         * 绑定事件
         */
        bindEvent: function(bindedObj) {
            var _this = this;
            var ff = {
                // 当有文件添加进来时执行，负责view的创建
                addFile: function(file) {
                    var $li = $('<li id="' + file.id + '">' +
                        '<p class="title">' + file.name + '</p>' +
                        '<p class="imgWrap"></p>' +
                        '<p class="progress"><span></span></p>' +
                        '</li>'),
                        $btns = $('<div class="file-panel">' +
                            '<span class="cancel">删除</span>' +
                            '<span class="rotateRight">向右旋转</span>' +
                            '<span class="rotateLeft">向左旋转</span></div>').appendTo($li),
                        $prgress = $li.find('p.progress span'),
                        $wrap = $li.find('p.imgWrap'),
                        $info = $('<p class="error"></p>'),

                        showError = function(code, file) {
                            console.log(file);
                            switch (code) {
                                case 'exceed_size':
                                    text = '文件大小超出';
                                    break;

                                case 'interrupt':
                                    text = '上传暂停';
                                    break;
                                default:
                                    text = '上传失败，请重试';
                                    break;
                            }
                            $info.text(text).appendTo($li);
                        };
                    if (file.getStatus() === 'invalid') {
                        showError(file.statusText);
                    } else {
                        // @todo lazyload
                        $wrap.text('预览中');
                        bindedObj.makeThumb(file, function(error, src) {
                            if (error) {
                                $wrap.text('不能预览');
                                return;
                            }
                            var img = $('<img src="' + src + '">');
                            $wrap.empty().append(img);
                        }, _this.thumbnailWidth, _this.thumbnailHeight);
                        _this.percentages[file.id] = [file.size, 0];
                        file.rotation = 0;
                    }
                    file.on('statuschange', function(cur, prev) {
                        if (prev === 'progress') {
                            $prgress.hide().width(0);
                        }

                        if (cur === 'error' || cur === 'invalid') {
                            showError(file.statusText);
                            _this.percentages[file.id][1] = 1;
                        } else if (cur === 'interrupt') {
                            showError('interrupt');
                        } else if (cur === 'queued') {
                            _this.percentages[file.id][1] = 0;
                        } else if (cur === 'progress') {
                            $info.remove();
                            $prgress.css('display', 'block');
                        } else if (cur === 'complete') {
                            $li.append('<span class="success"></span>');
                        }
                        $li.removeClass('state-' + prev).addClass('state-' + cur);
                    });

                    $li.on('mouseenter', function() {
                        $btns.stop().animate({
                            height: 30
                        });
                    });

                    $li.on('mouseleave', function() {
                        $btns.stop().animate({
                            height: 0
                        });
                    });

                    $btns.on('click', 'span', function() {
                        var index = $(this).index(),
                            deg;
                        switch (index) {
                            case 0:
                                bindedObj.removeFile(file);
                                return;

                            case 1:
                                file.rotation += 90;
                                break;

                            case 2:
                                file.rotation -= 90;
                                break;
                        }

                        if (_this.supportTransition) {
                            deg = 'rotate(' + file.rotation + 'deg)';
                            $wrap.css({
                                '-webkit-transform': deg,
                                '-mos-transform': deg,
                                '-o-transform': deg,
                                'transform': deg
                            });
                        } else {
                            $wrap.css('filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation=' + (~~((file.rotation / 90) % 4 + 4) % 4) + ')');
                            // use jquery animate to rotation
                            // $({
                            //     rotation: rotation
                            // }).animate({
                            //     rotation: file.rotation
                            // }, {
                            //     easing: 'linear',
                            //     step: function( now ) {
                            //         now = now * Math.PI / 180;

                            //         var cos = Math.cos( now ),
                            //             sin = Math.sin( now );

                            //         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
                            //     }
                            // });
                        }


                    });
                    $li.appendTo(_this.$queue);
                    // console.log("看看这里啥："+_this.$queue);
                    // $('.filelist').sortable();
                    _this.$queue.sortable();

                },
                // 负责view的销毁
                removeFile: function(file) {
                    var $li = $('#' + file.id);
                    delete _this.percentages[file.id];
                    ff.updateTotalProgress();
                    $li.off().find('.file-panel').off().end().remove();
                },
                updateTotalProgress: function() {
                    var loaded = 0,
                        total = 0,
                        spans = _this.$progress.children(),
                        percent;
                    $.each(_this.percentages, function(k, v) {
                        total += v[0];
                        loaded += v[0] * v[1];
                    });

                    percent = total ? loaded / total : 0;

                    spans.eq(0).text(Math.round(percent * 100) + '%');
                    spans.eq(1).css('width', Math.round(percent * 100) + '%');
                    ff.updateStatus();
                },
                updateStatus: function() {
                    var text = '',
                        stats;
                    if (_this.state === 'ready') {
                        text = '选中' + _this.fileCount + '个文件，共' +
                            WebUploader.formatSize(_this.fileSize) + '。';
                    } else if (_this.state === 'confirm') {
                        stats = bindedObj.getStats();
                        if (stats.uploadFailNum) {
                            text = '已成功上传' + stats.successNum + '个文件，' +
                                stats.uploadFailNum + '个文件上传失败，<a class="retry" href="javascript:void(0);">重新上传</a>失败文件或<a class="ignore" href="javascript:void(0);">忽略</a>'
                        }

                    } else {
                        stats = bindedObj.getStats();
                        text = '共' + _this.fileCount + '个（' +
                            WebUploader.formatSize(_this.fileSize) +
                            '），已上传' + stats.successNum + '个';

                        if (stats.uploadFailNum) {
                            text += '，失败' + stats.uploadFailNum + '个';
                        }
                    }
                    _this.$info.html(text);
                },
                setState: function(val) {
                    var file, stats;
                    if (val === _this.state) {
                        return;
                    }
                    _this.$upload.removeClass('state-' + _this.state);
                    _this.$upload.addClass('state-' + val);
                    _this.state = val;

                    switch (_this.state) {
                        case 'pedding':
                            _this.$placeHolder.removeClass('element-invisible');
                            _this.$queue.parent().removeClass('filled');
                            _this.$queue.hide();
                            _this.$statusBar.addClass('element-invisible');
                            bindedObj.refresh();
                            break;

                        case 'ready':
                            _this.$placeHolder.addClass('element-invisible');
                            _this.$filePickerJx.removeClass('element-invisible');
                            _this.$queue.parent().addClass('filled');
                            _this.$queue.show();
                            _this.$statusBar.removeClass('element-invisible');
                            bindedObj.refresh();
                            break;

                        case 'uploading':
                            _this.$filePickerJx.addClass('element-invisible');
                            _this.$progress.show();
                            _this.$upload.text('暂停上传');
                            break;

                        case 'paused':
                            _this.$progress.show();
                            _this.$upload.text('继续上传');
                            break;

                        case 'confirm':
                            _this.$progress.hide();
                            _this.$upload.text('开始上传');

                            stats = bindedObj.getStats();
                            if (stats.successNum && !stats.uploadFailNum) {
                                _this.setState('finish');
                                return;
                            }
                            break;
                        case 'finish':
                            stats = bindedObj.getStats();
                            if (stats.successNum) {
                                alert('上传成功');
                            } else {
                                // 没有成功的图片，重设
                                _this.state = 'done';
                                location.reload();
                            }
                            break;
                    }
                    ff.updateStatus();
                },
            };

            bindedObj.onUploadProgress = function(file, percentage) {
                var $li = $('#' + file.id),
                    $percent = $li.find('.progress span');

                $percent.css('width', percentage * 100 + '%');
                _this.percentages[file.id][1] = percentage;
                ff.updateTotalProgress();
            };

            bindedObj.onFileQueued = function(file) {
                _this.fileCount++;
                _this.fileSize += file.size;

                if (_this.fileCount === 1) {
                    _this.$placeHolder.addClass('element-invisible');
                    _this.$statusBar.show();
                }

                ff.addFile(file);
                ff.setState('ready');
                ff.updateTotalProgress();
            };

            bindedObj.onFileDequeued = function(file) {
                _this.fileCount--;
                _this.fileSize -= file.size;

                if (!_this.fileCount) {
                    ff.setState('pedding');
                }

                ff.removeFile(file);
                ff.updateTotalProgress();

            };
            // 文件上传成功后触发
            bindedObj.on('uploadSuccess', function(file, ret) {
                console.log(file);
                console.log(ret);
                console.log(ret._raw);
                // if (ret.status == 1) {
                //     // 上传成功
                //     $('#img_box').attr('src', "__ROOT__" + ret.url);
                // }
            });

            bindedObj.on('all', function(type) {
                var stats;
                switch (type) {
                    case 'uploadFinished':
                        ff.setState('confirm');
                        break;

                    case 'startUpload':
                        ff.setState('uploading');
                        break;

                    case 'stopUpload':
                        ff.setState('paused');
                        break;

                }
            });

            bindedObj.onError = function(code) {
                // alert('Eroor: ' + code);

                if (code == "F_DUPLICATE") {
                    alert("文件重复");
                } else if (code == 'Q_EXCEED_NUM_LIMIT') {
                    alert('超出最大文件数')
                } else {
                    alert('Eroor: ' + code);
                }
            };

        }
    };

    $.fn.WebUploaderFile = function(options) {
        var WebUpload = new $WebUpload($(this), options);
        WebUpload.init();
        return WebUpload;
    };

})(jQuery);
