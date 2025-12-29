/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(1);


/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	var _plugin = __webpack_require__(2);

	var _plugin2 = __webpack_require__(3);

	var _plugin3 = __webpack_require__(5);

	var _plugin4 = _interopRequireDefault(_plugin3);

	var _plugin5 = __webpack_require__(4);

	var _plugin6 = __webpack_require__(6);

	var _plugin7 = _interopRequireDefault(_plugin6);

	var _plugin8 = __webpack_require__(7);

	var _plugin9 = _interopRequireDefault(_plugin8);

	var _plugin10 = __webpack_require__(8);

	var _plugin11 = _interopRequireDefault(_plugin10);

	var _plugin12 = __webpack_require__(9);

	var _plugin13 = _interopRequireDefault(_plugin12);

	var _plugin14 = __webpack_require__(10);

	var _plugin15 = _interopRequireDefault(_plugin14);

	var _Global = __webpack_require__(11);

	var _Global2 = _interopRequireDefault(_Global);

	var _plugin16 = __webpack_require__(12);

	var _plugin17 = _interopRequireDefault(_plugin16);

	var _plugin18 = __webpack_require__(13);

	var _plugin19 = _interopRequireDefault(_plugin18);

	var _plugin20 = __webpack_require__(14);

	var _plugin21 = _interopRequireDefault(_plugin20);

	var _plugin22 = __webpack_require__(15);

	var _plugin23 = _interopRequireDefault(_plugin22);

	var _plugin24 = __webpack_require__(16);

	var _plugin25 = _interopRequireDefault(_plugin24);

	var _plugin26 = __webpack_require__(17);

	var _plugin27 = _interopRequireDefault(_plugin26);

	var _createConnect = __webpack_require__(18);

	var _createConnect2 = _interopRequireDefault(_createConnect);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	/**
	 * 绑定事件
	 * @return {[type]} [description]
	 */
	var containerBindEvent = function containerBindEvent() {
	    _Global2.default.container.on('mousedown', function (event) {
	        (0, _plugin2.mouseDown)(_Global2.default, event);
	    }).on('mousemove', function (event) {
	        // 设置鼠标在当前层，则键盘方向键可用
	        _Global2.default['is_hover'] = true;
	        _Global2.default.render_data_length && (0, _plugin2.mouseMove)(_Global2.default, event);
	    }).on("mouseout", function (event) {
	        // 设置鼠标离开当前层，则键盘方向键失效
	        _Global2.default['is_hover'] = false;

	        // 设置鼠标离开当前层，设置默认下标为渲染数据的长度，(避免在非当前区域内触犯←→键报错) 使用Global.render_data_length 而非 Global.render_length主要因为有可能渲染数据过少，无法满屏渲染
	        _Global2.default['index'] = _Global2.default.render_data_length;

	        (0, _plugin2.mouseOut)(_Global2.default, event);
	    }).on('mousewheel', function (event, delta) {
	        event.preventDefault();
	        (0, _plugin5.zoomView)(_Global2.default, delta);
	    });

	    win.on('keydown', function (event) {
	        if (_Global2.default.keyboard_operation && _Global2.default.is_hover) {
	            event.preventDefault();
	            var code = event.keyCode;
	            if (code == 37 || code == 39) {
	                (0, _plugin2.keyboardMoveingCross)(_Global2.default, code == 37 ? 1 : 0);
	            }
	            if (code == 38 || code == 40) {
	                (0, _plugin5.zoomView)(_Global2.default, code == 38 ? 1 : 0);
	            }
	        }
	    });

	    $(document).on('mouseup', function () {
	        _Global2.default['view_start_left'] = null;
	        if (_Global2.default.render_data_last_index >= _Global2.default.full_data_length) {
	            _Global2.default['draw_stop'] = false;
	        }
	    });
	};

	/**
	 * 绘图
	 * @return {[type]} [description]
	 */
	_Global2.default['draw'] = function () {

	    if (_Global2.default.render_data_last_index >= _Global2.default.full_data_length) {
	        _Global2.default['draw_stop'] = false;
	    } else {
	        _Global2.default['draw_stop'] = true;
	    }

	    _Global2.default['render_data'] = (0, _plugin.getDataPart)(_Global2.default.full_data, Math.max(_Global2.default.render_data_last_index, _Global2.default.render_length), _Global2.default.render_length);

	    _Global2.default['render_data_length'] = _Global2.default.render_data.length;

	    _Global2.default.canvas.main.ctx.clearRect(0, 0, _Global2.default.offset.width, _Global2.default.offset.height);

	    _Global2.default.DRAW.forEach(function (fn) {
	        fn && fn();
	    });
	};

	/**
	 * 窗口resize
	 * @return {[type]} [description]
	 */
	var resize = function resize() {
	    _Global2.default['offset'] = (0, _plugin.computeSize)(_Global2.default.container, _Global2.default.HD);
	    _Global2.default['level'] = (0, _plugin.levelResize)(_Global2.default.offset.kline.width, _Global2.default.full_data_length, _Global2.default.level_source);
	    //如果设定比例不存在就使用最小比例
	    if (!_Global2.default.level[_Global2.default.level_index]) {
	        _Global2.default['level_index'] = _Global2.default.level.length - 1;
	    }
	    _Global2.default['level_current'] = _Global2.default.level[_Global2.default.level_index];
	    _Global2.default['render_length'] = parseInt(_Global2.default.offset.kline.width / _Global2.default.level_current[0]);
	    for (var key in _Global2.default.canvas) {
	        _Global2.default.canvas[key].canvas[0].width = _Global2.default.offset.width;
	        _Global2.default.canvas[key].canvas[0].height = _Global2.default.offset.height;
	        _Global2.default.canvas[key].canvas.css({
	            height: _Global2.default.offset.height / _Global2.default.HD,
	            width: _Global2.default.offset.width / _Global2.default.HD
	        });
	    }
	    _Global2.default.draw();
	};

	/*********************************************
	 *******          增加扩展              *******
	 ********************************************/
	var win = $(window);

	var push = function push(arr, history) {
	    //return;
	    if (_Global2.default.full_data_length && arr[1][0] == _Global2.default.full_data.pop()[0]) {
	        _Global2.default.full_data.push(arr[1]);
	    } else {
	        //历史数据无数据兼容处理
	        if (_Global2.default.full_data_length) {
	            _Global2.default.full_data.push(arr[0], arr[1]);
	        } else {
	            //如果无历史数据，再次初始化
	            init(arr);
	        }
	    }
	    _Global2.default['full_data_length'] = _Global2.default.full_data.length;
	    if (!_Global2.default.draw_stop) {
	        _Global2.default['render_data_last_index'] = _Global2.default.full_data_length;
	        _Global2.default.draw();
	    }
	};

	var init = function init(data) {
	    //划线工具
	    _Global2.default['drawing_type_data'] = [{
	        start_price: 5449.325335570469,
	        start_time: 1481717984,
	        end_price: 5443.3884395973155,
	        end_time: 1481718592,
	        type: "RayLine"
	    }];
	    _Global2.default['full_data'] = data;
	    _Global2.default['full_data_length'] = _Global2.default.full_data.length;
	    _Global2.default['render_data_last_index'] = _Global2.default.full_data_length;
	    _Global2.default.full_data_length && resize();
	};

	window['KLINE'] = function (container) {
	    _Global2.default['container'] = container;
	    _Global2.default['canvas'] = {
	        main: (0, _plugin.createCanvas)(_Global2.default, [_plugin13.default, _plugin23.default, _plugin25.default, _plugin27.default, _plugin9.default, _plugin21.default, _plugin11.default, _plugin15.default]),
	        tools: (0, _plugin.createCanvas)(_Global2.default, [_plugin4.default]),
	        cross: (0, _plugin.createCanvas)(_Global2.default, [_plugin19.default, _plugin17.default, _plugin7.default])
	    };
	    containerBindEvent();
	    return {
	        init: init,
	        push: push,
	        option: _Global2.default.option,
	        resize: resize,
	        redraw: _Global2.default.draw,
	        globalPush: function globalPush(type, val) {
	            _Global2.default[type] = val;
	        }
	    };
	};

	(0, _createConnect2.default)();

/***/ }),
/* 2 */
/***/ (function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	/**计算MA**/
	var cache = {};

	var Ma = function Ma(N, arr) {
	  var E = 0;
	  if (arr.length == N) {
	    var tmp = ['MA', N, arr[N - 1][0], arr[N - 1][5]].join('-');
	    if (!cache[tmp]) {
	      arr.forEach(function (item, i) {
	        return E += item[4];
	      });
	      cache[tmp] = E / N;
	    }
	    return cache[tmp];
	  } else {
	    return null;
	  }
	};

	var computeMa = function computeMa(global, N) {
	  var tmp = [];
	  for (var i = 0; i < global.render_data_length; i++) {
	    //截取数据:以此下标截取往前推的N条数据，然后再用Ma算出当前下标的ma
	    var _arr = getDataPart(global.full_data, global.render_data_last_index - i, N);
	    tmp.unshift(Ma(N, _arr));
	  }
	  return tmp;
	};

	//计算MD
	var Md = function Md(N, arr, mb) {
	  var tmp = ['BOLL', N, arr[N - 1][0], arr[N - 1][5]].join('-');
	  if (!cache[tmp]) {
	    var E = 0;
	    arr.forEach(function (item, i) {
	      E += Math.pow(item[4] - mb, 2);
	    });
	    cache[tmp] = Math.sqrt(E / N);
	  }
	  return cache[tmp];
	};

	var computeMd = function computeMd(global, N, mb) {
	  var start = global.render_data_last_index - global.render_data_length;
	  var md = [];
	  mb.forEach(function (item, i) {
	    if (item == null) {
	      md.push(null);
	    } else {
	      //截取数据:以此下标截取往前推的N条数据，然后再用Ma算出当前下标的ma
	      var _arr = getDataPart(global.full_data, start + i + 1, N);
	      md.push(Md(N, _arr, item));
	    }
	  });
	  return md;
	};

	//K线区域由价格转坐标
	var price2Y = function price2Y(global, price) {
	  return global.offset.kline.view_height * (global.render_data_price.max - price) / global.render_data_price.range;
	};

	//补全数字为两位数
	var fullNumber = function fullNumber(n) {
	  return n < 10 ? '0' + n : n;
	};

	//创建canvas
	var createCanvas = function createCanvas(global, ext) {
	  var canvas = $('<canvas style="position: absolute; left: 0; top: 0">');
	  var context = canvas[0].getContext('2d');
	  global.container.append(canvas);
	  ext.forEach(function (item, i) {
	    return global.DRAW.push(item(context, global));
	  });
	  return {
	    ctx: context,
	    canvas: canvas
	  };
	};

	//格式化时间
	var formatDate = function formatDate(str) {
	  var t = new Date(str * 1000);
	  var week = ['日', '一', '二', '三', '四', '五', '六'];
	  return [[t.getFullYear(), fullNumber(t.getMonth() + 1), fullNumber(t.getDate())].join('/'), [fullNumber(t.getHours()), fullNumber(t.getMinutes())].join(':')].join(' ');
	};

	//获取最大最小值
	var getMaxMin = function getMaxMin(data, index) {
	  var len = data.length;
	  var max = data[0][index];
	  var min = max;
	  while (len--) {
	    var tmp = data[len][index];
	    if (tmp > max) {
	      max = tmp;
	    } else if (tmp < min) {
	      min = tmp;
	    }
	  }
	  return {
	    max: max,
	    min: min
	  };
	};

	//获取指定范围内的数据
	var getDataPart = function getDataPart(arr, last, size) {
	  return arr.slice(Math.max(0, last - size), last);
	};

	//计算K线布局
	var computeSize = function computeSize(container, HD) {
	  var config = {
	    // 左、右侧边宽(固定值)
	    side_width: 60 * HD,
	    // 底部时间轴高(固定值)
	    foot_date_height: 32 * HD,
	    // 底部小地图高(百分比)
	    foot_minmap_height: 0.1,
	    // 主体K线区域高(百分比)
	    main_kline_height: 0.7, //0.6,
	    // 主体成交量区域高(百分比)
	    main_amount_height: 0.2, //0.15,
	    // 主体MACD区域高(百分比)
	    main_macd_height: 0, //0.15,
	    //视图占比（量，KLINE视图占用窗口比例）
	    view_HD: 0.75
	  };

	  var offset = container.offset();
	  offset['height'] = container.height() * HD;
	  offset['width'] = container.width() * HD;

	  var _height = offset.height - config.foot_date_height;

	  var kline = {
	    top: 0,
	    left: config.side_width,
	    width: parseInt(offset.width - config.side_width * 2),
	    height: parseInt(_height * config.main_kline_height)
	  };
	  kline['view_height'] = parseInt(kline.height * config.view_HD);
	  kline['view_top'] = parseInt(kline.height * (1 - config.view_HD) / 2);

	  var amount = {
	    top: kline.height,
	    left: kline.left,
	    width: kline.width,
	    height: parseInt(_height * config.main_amount_height)
	  };
	  amount['view_height'] = parseInt(amount.height * config.view_HD);
	  amount['view_top'] = parseInt(amount.top + amount.height * (1 - config.view_HD));

	  var macd = {
	    top: kline.height + amount.height,
	    left: kline.left,
	    width: kline.width,
	    height: parseInt(_height * config.main_macd_height)
	  };

	  var date = {
	    top: macd.top + macd.height,
	    left: kline.left,
	    width: kline.width,
	    height: config.foot_date_height
	  };

	  var minmap = {
	    top: date.top + date.height,
	    left: kline.left,
	    width: kline.width,
	    height: offset.height - date.top - date.height
	  };
	  offset['macd'] = macd;
	  offset['date'] = date;
	  offset['kline'] = kline;
	  offset['amount'] = amount;
	  offset['minmap'] = minmap;
	  return offset;
	};

	/** * 计算缩放 * @return {[type]} [description] */
	var levelResize = function levelResize(kline_width, full_data_length, level_source) {
	  var len = Math.ceil(full_data_length / kline_width);
	  var min = kline_width / full_data_length;
	  var level = [];
	  if (min < 1) {
	    level = [].concat(level_source);
	    for (var i = 2; i < len; i++) {
	      level.push([1 / i, 1, 0]);
	    }
	    level.push([min, 1, 0]);
	  } else {
	    level_source.forEach(function (item, i) {
	      if (item[0] * full_data_length >= kline_width) {
	        level.push(item);
	      } else {
	        // 数据不能满屏时，添加最大值为默认值
	        i == 0 && level.push(item);
	      }
	    });
	  }
	  return level;
	};

	exports.createCanvas = createCanvas;
	exports.getMaxMin = getMaxMin;
	exports.formatDate = formatDate;
	exports.getDataPart = getDataPart;
	exports.computeSize = computeSize;
	exports.levelResize = levelResize;
	exports.price2Y = price2Y;
	exports.computeMa = computeMa;
	exports.computeMd = computeMd;

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});
	exports.keyboardMoveingCross = exports.mouseMove = exports.mouseOut = exports.mouseDown = undefined;

	var _plugin = __webpack_require__(2);

	var _plugin2 = __webpack_require__(4);

	var y2Price = function y2Price(global, top) {
	    return global.render_data_price.max - global.render_data_price.range * (top - global.offset.kline.view_top) / global.offset.kline.view_height;
	};

	var keyboardMoveingCross = function keyboardMoveingCross(global, s) {
	    if (s) {
	        global.index--;
	    } else {
	        global.index++;
	    }
	    if (global.index < 0) {
	        global["index"] = 0;
	        (0, _plugin2.viewMove)(global, -1);
	    }
	    if (global.index > global.render_data_length - 1) {
	        global["index"] = global.render_data_length - 1;
	        (0, _plugin2.viewMove)(global, 1);
	    }
	    if (global.render_data[global.index]) {
	        moveEvent(global);
	    }
	};

	var mouseMove = function mouseMove(global, event) {
	    var top = parseInt(event.pageY - global.offset.top);
	    var left = parseInt(event.pageX - global.offset.left);

	    global.canvas.cross.ctx.crossClear(global.offset.width, global.offset.height);

	    global.canvas.cross.ctx.rect(global.offset.kline.left, 0, global.offset.kline.width, global.offset.height - global.offset.minmap.height);

	    // 判断在k线区域
	    if (global.canvas.cross.ctx.isPointInPath(left * global.HD, top * global.HD)) {
	        var index = Math.floor((left * global.HD - global.offset.kline.left) / global.level_current[0]);

	        //当前区域未填满蜡烛图，鼠标移动到最右侧时取最后一根
	        if (index > global.render_data_length - 1) {
	            index = global.render_data_length - 1;
	        }
	        global['index'] = index;

	        moveEvent(global, event);
	    }
	};

	var mouseDown = function mouseDown(global, event) {
	    //当前区域内鼠标按下，禁止视图渲染
	    global['draw_stop'] = true;
	    if (!global.drawingType || global.drawingType == 'Cursor') {
	        global['view_start_left'] = event.pageX;
	    } else {
	        global['drawingPoint'] = {
	            type: global.drawingType,
	            start_time: global.render_data[global.index][0],
	            start_price: global.current_price
	        };
	    }
	    console.log(global);
	};

	var mouseOut = function mouseOut(global, event) {};

	var moveEvent = function moveEvent(global, event) {
	    global.canvas.cross.ctx.crossClear();

	    //开放至全局，方便画图工具使用坐标
	    var top = event ? parseInt(event.pageY - global.offset.top) * global.HD : parseInt((0, _plugin.price2Y)(global, global.current_price) + global.offset.kline.view_top);
	    var left = parseInt(global.index * global.level_current[0] + global.level_current[1] / 2 + global.offset.kline.left + global.HD) - .5;

	    global['current_price'] = event ? y2Price(global, top) : global.render_data[global.index][4];

	    // 如果获取不到 画图工具类型（默认为十字线），或 光标类型则能拖动视图
	    if (!global.drawingType || global.drawingType == 'Cursor') {

	        //如果获取不到 画图工具类型（默认为十字线），且不为拖动操作显示十字线
	        if (!global.drawingType && global.view_start_left == null) {
	            global.canvas.cross.ctx.crossMove(top, left);
	        }

	        if (global.view_start_left != null) {
	            //计算移动距离(烛距)
	            var range = parseInt((global.view_start_left - event.pageX) / global.level_current[0] * global.HD);
	            if (Math.abs(range) > 0) {
	                (0, _plugin2.viewMove)(global, range);
	                global['view_start_left'] = event.pageX;
	            }
	        }
	    } else {
	        global.canvas.cross.ctx.drawingType(event, top, left);
	    }
	};

	exports.mouseDown = mouseDown;
	exports.mouseOut = mouseOut;
	exports.mouseMove = mouseMove;
	exports.keyboardMoveingCross = keyboardMoveingCross;

/***/ }),
/* 4 */
/***/ (function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});
	var timer = function timer(global) {
	    if (global.event_timer) {
	        return true;
	    } else {
	        global['event_timer'] = setTimeout(function () {
	            return global['event_timer'] = null;
	        }, 20);
	        return false;
	    }
	};

	/**
	 * 视图位移(用于鼠标拖动，与←→方向键，触摸板)
	 * @param  {[type]} global [description]
	 * @param  {[type]} range  [视图移动的距离，当前蜡烛宽的倍数]
	 * @return {[type]}        [description]
	 */
	var viewMove = function viewMove(global, range) {
	    var tmp = global.render_data_last_index + range;

	    //如果移动后的数据截取长度小于屏幕容纳数据数量，说明数据已移动到最左端
	    if (tmp < global.render_length) {
	        tmp = global.render_length;
	    }

	    if (tmp >= global.full_data_length) {
	        tmp = global.full_data_length;
	    }

	    //如果移动后的数据截取长度大于总数据数量，说明数据已移动到最右端
	    global['render_data_last_index'] = tmp;

	    global.draw();
	};

	/*********************************************
	 *******         放大 && 缩放           *******
	 ********************************************/
	/**
	 * 视图放大与缩放(用于鼠标滚轮，与↑↓方向键，触摸板)
	 * @param  {[type]} global [description]
	 * @param  {[type]} s      [大于0缩放视图，小于0放大视图]
	 * @return {[type]}        [description]
	 */
	var zoomView = function zoomView(global, s) {

	    if (timer(global)) return;
	    if (s > 0) {
	        global.level_index--;
	    } else {
	        global.level_index++;
	    }

	    if (global.level[global.level_index]) {
	        // let cache_render_length = global.render_length
	        var full_data_start_index = global.render_data_last_index - global.render_length + global.index;

	        //算出当前坐标右侧空间（用于计算右侧需要补充多少条数据）
	        var right = (global.render_length - global.index) * global.level_current[0];

	        global['level_current'] = global.level[global.level_index];

	        global['render_data_last_index'] = full_data_start_index + Math.round(right / global.level_current[0]);

	        // 更新当前等级蜡烛宽度的存放条数
	        global['render_length'] = parseInt(global.offset.kline.width / global.level_current[0]);

	        //修正数据，最大不能超过所有数据
	        if (global.render_data_last_index > global.full_data_length) {
	            global['render_data_last_index'] = global.full_data_length;
	        }

	        //最小不能小于当前窗口容纳数据

	        if (global.render_data_last_index < global.render_length) {
	            global['render_data_last_index'] = global.render_length;
	        }

	        global['index'] = global.render_length - Math.round(right / global.level_current[0]);

	        global.draw();
	    } else {
	        if (s > 0) {
	            global.level_index++;
	        } else {
	            global.level_index--;
	        }
	    }
	};
	exports.viewMove = viewMove;
	exports.zoomView = zoomView;

/***/ }),
/* 5 */
/***/ (function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	exports.default = function (ctx, global) {
	    return function () {
	        ctx.translate(global.offset.minmap.left, global.offset.minmap.top + global.HD);
	        ctx.clearRect(0, 0, global.offset.width, global.offset.minmap.height);
	        ctx.strokeStyle = '#54FFFF';
	        ctx.fillStyle = 'rgba(255,255,255,.5)';
	        ctx.fillRect(0, 0, global.offset.minmap.width, global.offset.minmap.height);

	        var width = 10 * global.HD;
	        var height = global.offset.minmap.height - 2 * global.HD;
	        var top = 0.5 * global.HD;
	        var left1 = global.render_data_last_index / global.full_data_length * global.offset.minmap.width - width / 2;
	        var left2 = left1 - global.render_data_length / global.full_data_length * global.offset.minmap.width;

	        ctx.clearRect(left1, top, left2 - left1, height);

	        ctx.lineWidth = global.HD;
	        ctx.fillStyle = global.color_border;
	        ctx.fillRect(left1, top, width, height);
	        ctx.strokeRect(left1, top, width, height);
	        ctx.fillRect(left2, top, width, height);
	        ctx.strokeRect(left2, top, width, height);

	        ctx.translate(-global.offset.minmap.left, -global.offset.minmap.top - global.HD);
	    };
	};

/***/ }),
/* 6 */
/***/ (function(module, exports) {

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	exports.default = function (ctx, global) {

	    ctx.drawPoint = function (top, left) {
	        ctx.fillStyle = "#1987da";
	        ctx.strokeStyle = "#1987da";
	        ctx.fillRect(left - 9 * global.HD, top, 19 * global.HD, global.HD);
	        ctx.fillRect(left, top - 9 * global.HD, global.HD, 19 * global.HD);
	        ctx.beginPath();
	        ctx.arc(left + global.HD / 2, top + global.HD / 2, 2 * global.HD, 0, 2 * Math.PI);
	        ctx.fill();
	        ctx.beginPath();
	        ctx.arc(left + global.HD / 2, top + global.HD / 2, 7 * global.HD, 0, 2 * Math.PI);
	        ctx.stroke();
	    };

	    ctx.drawType = function (event, top, left) {
	        if (!global.drawingPoint) {
	            ctx.drawPoint(top, left);
	        } else {
	            if (global.drawingPoint.start_time) {}
	        }
	    };

	    return function () {
	        global.drawing_type_data.forEach(function (item, i) {});
	    };
	};

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {
	    var drawAmount = function drawAmount(type, left, top, height, width, space) {
	        ctx.lineWidth = global.HD;
	        if (type > 0) {
	            ctx.fillStyle = global.color_up;
	            ctx.strokeStyle = global.color_up;
	            if (space > 1) {
	                ctx.strokeRect(left + .5, top, width, height);
	            } else {
	                ctx.fillRect(left, top, width, height);
	            }
	        } else {
	            ctx.fillStyle = global.color_down;
	            ctx.strokeStyle = global.color_down;
	            if (space > 1) {
	                ctx.fillRect(left, top, width, height);
	            } else {
	                ctx.fillRect(left, top, width, height);
	            }
	        }
	    };

	    var drawRule = function drawRule() {
	        var line_offset = 5 * global.HD;
	        var text_offset = 7 * global.HD;

	        // 最高位虚线
	        ctx.setLineDash([5 * global.HD]);
	        ctx.strokeStyle = global.color_dashed;
	        ctx.beginPath();
	        ctx.lineWidth = line_offset / 5;
	        ctx.moveTo(-line_offset, 0 - line_offset / 10);
	        ctx.lineTo(global.offset.amount.width + line_offset, 0 - line_offset / 10, line_offset);
	        ctx.stroke();
	        ctx.setLineDash([10000]);

	        // 两侧刻度值
	        ctx.fillStyle = global.color_font;
	        ctx.textAlign = 'left';
	        ctx.textBaseline = 'middle';
	        ctx.font = 12 * global.HD + 'px arial';
	        ctx.fillText(global.render_data_amount.max.toFixed(2), global.offset.amount.width + text_offset, 0);
	        ctx.textAlign = 'right';
	        ctx.fillText(global.render_data_amount.max.toFixed(2), -text_offset, 0);
	    };

	    return function () {
	        global['render_data_amount'] = (0, _plugin.getMaxMin)(global.render_data, 5);
	        var level = global.level_current[0];
	        var width = global.level_current[1];
	        var space = width / 2;
	        ctx.translate(global.offset.amount.left + global.HD, global.offset.amount.view_top);
	        global.render_data.forEach(function (item, i) {
	            var left = parseInt(i * level);
	            var height = parseInt(item[5] * global.offset.amount.view_height / global.render_data_amount.max);
	            drawAmount(item[4] - item[1], left, global.offset.amount.view_height - height, height, width, space);
	        });
	        drawRule();
	        ctx.translate(-global.offset.amount.left - global.HD, -global.offset.amount.view_top);
	    };
	};

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {
	    return function () {
	        var mm = (0, _plugin.getMaxMin)(global.full_data, 4);
	        var space = global.offset.minmap.width / global.full_data_length;
	        var range = mm.max - mm.min;

	        ctx.translate(global.offset.minmap.left, global.offset.minmap.top);
	        ctx.beginPath();
	        ctx.moveTo(global.offset.minmap.width, global.offset.minmap.height);
	        ctx.lineTo(0, global.offset.minmap.height);
	        global.full_data.forEach(function (item, i) {
	            return ctx.lineTo(parseInt(i * space), global.offset.minmap.height * (mm.max - item[4]) / range);
	        });
	        ctx.lineTo(global.offset.minmap.width, global.offset.minmap.height);
	        ctx.lineWidth = 1;
	        ctx.strokeStyle = '#54FFFF';
	        ctx.fillStyle = 'rgba(84,255,255,.5)';
	        ctx.stroke();
	        ctx.fill();

	        /**纵线补全**/
	        ctx.fillStyle = global.color_border;
	        ctx.fillRect(0, 0, global.HD, global.offset.minmap.height);
	        ctx.fillRect(global.offset.minmap.width, 0, global.HD, global.offset.minmap.height);

	        ctx.translate(-global.offset.minmap.left, -global.offset.minmap.top);
	    };
	};

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {

	    // let max_price, min_price

	    // //最后一条价格浮标
	    // let lastCursor = (last) => {
	    //     ctx.beginPath()
	    //     ctx.moveTo(global.offset.kline.width + global.HD, last[0])
	    //     ctx.lineTo(global.offset.kline.width + global.HD + 6 * global.HD, last[0] - 10 * global.HD)
	    //     ctx.lineTo(global.offset.kline.width + global.offset.kline.left, last[0] - 10 * global.HD)
	    //     ctx.lineTo(global.offset.kline.width + global.offset.kline.left, last[0] + 10 * global.HD)
	    //     ctx.lineTo(global.offset.kline.width + global.HD + 6 * global.HD, last[0] + 10 * global.HD)
	    //     ctx.moveTo(global.offset.kline.width + global.HD, last[0])
	    //     ctx.fill()
	    //     ctx.textAlign = 'left'
	    //     ctx.fillStyle = '#fff'
	    //     ctx.fillText(last[2].toFixed(2), global.offset.kline.width + global.HD + 10 * global.HD, last[0])
	    // }

	    // ///最高最低价
	    // let drawMaxMin = (type, x, y2, h2, index) => {
	    //     ctx.strokeStyle = global.color_font
	    //     ctx.fillStyle = global.color_font
	    //     ctx.textBaseline = 'middle'
	    //     ctx.beginPath()
	    //     if (type == 'max') {
	    //         ctx.moveTo(x, y2)
	    //         y2 -= 15 * global.HD
	    //     } else {
	    //         y2 += h2
	    //         ctx.moveTo(x, y2)
	    //         y2 += 15 * global.HD
	    //     }
	    //     if (index * global.level_current[0] < global.offset.kline.width / 2) {
	    //         x += 20 * global.HD
	    //         ctx.lineTo(x, y2)
	    //         ctx.textAlign = 'left'
	    //     } else {
	    //         x -= 20 * global.HD
	    //         ctx.lineTo(x, y2)
	    //         ctx.textAlign = 'right'
	    //     }
	    //     ctx.stroke()
	    //     ctx.fillText((type == 'max' ? max_price : min_price).toFixed(2), x, y2)
	    // }

	    // ///绘制蜡烛
	    // let drawCandle = (left, close, min_range, max, max_range, width, lineWidth, lineLeft) => {

	    //     ctx.lineWidth = lineWidth

	    //     if (min_range > 0) {
	    //         ctx.fillStyle = global.color_up
	    //         ctx.strokeStyle = global.color_up
	    //         ctx.fillRect(left + lineLeft, max, lineWidth, max_range)
	    //         if (width >= global.HD) {
	    //             ctx.clearRect(left, close, width, min_range)
	    //             ctx.strokeRect(left + .5, close, width, min_range)
	    //         }
	    //     } else {
	    //         ctx.fillStyle = global.color_down
	    //         ctx.strokeStyle = global.color_down
	    //         ctx.fillRect(left + lineLeft, max, lineWidth, max_range)
	    //         if (width >= global.HD) {
	    //             ctx.fillRect(left, close, width, min_range)
	    //             ctx.strokeRect(left + .5, close, width, min_range)
	    //         }
	    //     }
	    // }

	    // //绘制刻度
	    // let drawRule = () => {
	    //     let rows = parseInt(global.offset.kline.view_height / global.kline_row_minspace / global.HD)
	    //     let rows_space = global.offset.kline.view_height / rows
	    //     let average = (global.render_data_price.max - global.render_data_price.min) / rows
	    //     let width = global.offset.kline.width
	    //     let line_offset = 5 * global.HD
	    //     let text_offset = 7 * global.HD
	    //     ctx.fillStyle = global.color_font
	    //     ctx.strokeStyle = global.color_dashed
	    //     ctx.font = 12 * global.HD + 'px arial'
	    //     ctx.textBaseline = 'middle'
	    //     ctx.lineWidth = global.HD
	    //     ctx.setLineDash([5 * global.HD])
	    //     while (rows >= 0) {
	    //         let beginY = parseInt(rows * rows_space) + 0.5
	    //         let price = (global.render_data_price.max - average * rows).toFixed(2)
	    //         ctx.beginPath()
	    //         ctx.moveTo(-line_offset, beginY)
	    //         ctx.lineTo(width + line_offset, beginY, line_offset)
	    //         ctx.stroke()
	    //         ctx.textAlign = 'left'
	    //         ctx.fillText(price, width + text_offset, beginY)
	    //         ctx.textAlign = 'right'
	    //         ctx.fillText(price, -text_offset, beginY)
	    //         rows--
	    //     }
	    //     ctx.setLineDash([10000])
	    // }

	    // let int = (number) => {
	    //     return parseInt(number).toString().replace(/\d/g,'0').replace(/^\d/g,'1') / 100
	    // }

	    // return () => {
	    //     max_price = getMaxMin(global.render_data, 2).max
	    //     min_price = getMaxMin(global.render_data, 3).min
	    //     let range = max_price - min_price

	    //     //通过最大最小值转换，两者差距越大区波动间越大，区间越小波动区间越小
	    //     let tmp = 1 - (max_price - min_price) / max_price

	    //     global['render_data_price']['max'] = max_price * (1 + tmp * .01)
	    //     global['render_data_price']['min'] = min_price * (1 - tmp * .01)

	    //     // 标尺刻度转换为整数，通过divisor转换为整数倍的除数，在对最大值向上取整，最小值向下取整。
	    //     let divisor = int(global.render_data_price.max)
	    //     global['render_data_price']['max'] = Math.ceil(global.render_data_price.max / divisor) * divisor
	    //     global['render_data_price']['min'] = Math.floor(global.render_data_price.min / divisor) * divisor

	    //     global['render_data_price']['range'] = global.render_data_price.max - global.render_data_price.min


	    //     let level = global.level_current[0]
	    //     let width = global.level_current[1]
	    //     let draw_max = false
	    //     let draw_min = false
	    //     let last = []

	    //     ctx.translate(global.offset.kline.left + global.HD, global.offset.kline.view_top)
	    //     drawRule()
	    //     let lineWidth = 1
	    //     let lineLeft = 0
	    //     if (width >= global.HD) {
	    //         lineWidth = global.HD
	    //         lineLeft = width / 2 - global.HD / 2
	    //     }

	    //     global.render_data.forEach((item, i) => {
	    //         let left = parseInt(i * level)
	    //         let open = price2Y(global, item[1])
	    //         let max = price2Y(global, item[2])
	    //         let min = price2Y(global, item[3])
	    //         let close = price2Y(global, item[4])

	    //         //最高价
	    //         if (!draw_max && item[2] == max_price) {
	    //             (draw_max = true) && drawMaxMin('max', left + width / 2, max, min - max, i)
	    //         }

	    //         //最低价
	    //         if (!draw_min && item[3] == min_price) {
	    //             (draw_min = true) && drawMaxMin('min', left + width / 2, max, min - max, i)
	    //         }
	    //         drawCandle(left, close, open - close, max, min - max, width, lineWidth, lineLeft)
	    //         last = [close, i, item[4]]
	    //     })

	    //     lastCursor(last)
	    //     ctx.translate(-global.offset.kline.left - global.HD, -global.offset.kline.view_top)
	    // }
	};

/***/ }),
/* 10 */
/***/ (function(module, exports) {

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	exports.default = function (ctx, global) {
	    return function () {
	        ctx.fillStyle = global.color_border;

	        //纵向分隔线
	        ctx.fillRect(global.offset.kline.left, 0, global.HD, global.offset.date.top);
	        ctx.fillRect(global.offset.kline.left + global.offset.kline.width, 0, global.HD, global.offset.date.top);

	        // 横向分隔线
	        ctx.fillRect(0, global.offset.amount.top, global.offset.width, global.HD);
	        ctx.fillRect(0, global.offset.macd.top, global.offset.width, global.HD);
	        ctx.fillRect(0, global.offset.minmap.top, global.offset.width, global.HD);
	        ctx.fillRect(0, global.offset.date.top, global.offset.width, global.HD);
	    };
	};

/***/ }),
/* 11 */
/***/ (function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});
	exports.default = {
	    keyboard_operation: true,
	    option: {}, //指标开关
	    HD: 2,
	    DRAW: [], //绘制列表，例如 蜡烛图，成交量，macd等
	    color_up: '#a1120b', // 上涨颜色与蜡烛颜色
	    color_down: '#44c239', // 下跌颜色与蜡烛颜色
	    color_font: '#999', // 字体颜色
	    color_border: '#ccc', // 实线颜色
	    color_dashed: '#ddd', // 虚线颜色
	    render_data_amount: {}, //存放当前屏幕内渲染数据量的最大最小值
	    render_data_price: {}, //存放当前屏幕内渲染数据量的最高最低价
	    kline_row_minspace: 50, //K线区域两条价格之间的最小距离
	    level_index: 16, //蜡烛默认使用的缩放等级下标
	    level_source: [//蜡烛缩放等级
	    [84, 75, 9], [78, 69, 9], [72, 63, 9], [66, 57, 9], [58, 51, 7], [54, 47, 7], [50, 43, 7], [46, 39, 7], [40, 35, 5], [36, 31, 5], [32, 27, 5], [30, 25, 5], [28, 23, 5], [26, 21, 5], [22, 19, 3], [20, 17, 3], [18, 15, 3], [16, 13, 3], [14, 11, 3], [12, 9, 3], [10, 7, 3], [8, 5, 3], [6, 3, 1], [4, 1, 2], [2, 1, 1], [1, 1, 0]],
	    ma: [7, 30],
	    ma_color: ['#a6cee3', '#fdbf6f', '#df8adf', "#ffff00"],
	    boll: 20,
	    boll_color: ['#a8a8a8', '#bdb526', '#bb05c2'],
	    hover_callback_list: [] //划过当前屏幕执行的回调，比如 量、MA的浮窗信息
	};

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {
	    ctx.layer = function () {
	        var tmp = global.render_data[global.index];
	        var str = [];
	        str.push(['时间', (0, _plugin.formatDate)(tmp[0])].join(':'));
	        str.push(['开', tmp[1].toFixed(2)].join(':'));
	        str.push(['高', tmp[2].toFixed(2)].join(':'));
	        str.push(['低', tmp[3].toFixed(2)].join(':'));
	        str.push(['收', tmp[4].toFixed(2)].join(':'));
	        str.push(['量', tmp[5].toFixed(2)].join(':'));
	        ctx.fillStyle = global.color_font;
	        ctx.textAlign = 'left';
	        ctx.textBaseline = 'top';
	        var text = str.join('  ');
	        var left = global.offset.kline.left + global.HD * 3;
	        var top = global.offset.kline.top + global.HD * 3;

	        // kline开高收低量
	        ctx.fillText(text, left, top);

	        // amount量
	        ctx.fillText(['量', tmp[5].toFixed(2)].join(':'), left, global.offset.amount.top + global.HD * 3);

	        // kline涨幅
	        left += ctx.measureText(text).width;
	        text = ['  涨幅', ((tmp[4] - tmp[1]) / tmp[1] * 100).toFixed(2) + '%'].join(':');
	        if (tmp[4] - tmp[1] > 0) {
	            ctx.fillStyle = global.color_up;
	        } else {
	            ctx.fillStyle = global.color_down;
	        }
	        ctx.fillText(text, left, top);

	        // kline MA
	        left += ctx.measureText(text).width;

	        global.hover_callback_list.forEach(function (fn) {
	            if (typeof fn == 'function') {
	                left = fn(ctx, left, top);
	            }
	        });
	    };
	};

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {
	    /**
	     * 纵向十字线
	     * @param  {[type]} left [定位横线的X轴坐标]
	     * @return {[type]}      [none]
	     */
	    var drawCrossY = function drawCrossY(left) {
	        var height = global.offset.date.top;
	        ctx.fillStyle = global.color_border;
	        ctx.fillRect(left, 0, global.HD, global.offset.date.top);

	        //日期
	        ctx.fillRect(left - global.offset.kline.left, height + global.HD, global.offset.kline.left * 2, global.offset.date.height);
	        ctx.font = 12 * global.HD + 'px arial';
	        ctx.textAlign = 'center';
	        ctx.fillStyle = '#fff';
	        ctx.fillText((0, _plugin.formatDate)(global.render_data[global.index][0]), left, height + global.offset.date.height / 2);
	    };

	    /**
	     * 横向十字线
	     * @param  {[type]} top    [定位横线的Y轴坐标]
	     * @param  {[type]} price [横线两端显示的价格]
	     * @return {[type]}        [none]
	     */
	    var drawCrossX = function drawCrossX(top, price) {
	        ctx.fillStyle = global.color_border;
	        ctx.fillRect(global.offset.kline.left, top, global.offset.kline.width, global.HD);

	        // 横向价格
	        ctx.fillRect(global.offset.kline.width + global.offset.kline.left, top - 10 * global.HD, global.offset.kline.left, 20 * global.HD);
	        ctx.fillRect(0, top - 10 * global.HD, global.offset.kline.left, 20 * global.HD);
	        ctx.fillStyle = '#fff';

	        ctx.textAlign = 'left';
	        ctx.fillText(price, global.offset.kline.width + global.offset.kline.left + 7 * global.HD, top);
	        ctx.textAlign = 'right';
	        ctx.fillText(price, global.offset.kline.left - 7 * global.HD, top);
	    };

	    ctx.crossClear = function () {
	        return ctx.clearRect(0, 0, global.offset.width, global.offset.height);
	    };

	    ctx.crossMove = function (top, left) {
	        var price = global.current_price.toFixed(2);
	        ctx.textBaseline = 'middle';

	        //纵向十字线
	        drawCrossY(left);
	        if (top < global.offset.macd.top) {
	            // 超出k线区域，显示交易量
	            if (top > global.offset.amount.top) {
	                price = (global.render_data_amount.max * (global.offset.macd.top - global.HD - top) / global.offset.amount.view_height).toFixed(2);
	            }
	            //横向十字线
	            drawCrossX(top, price);
	        }
	        //浮窗信息
	        ctx.layer(global);
	    };
	};

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {

	    var drawDate = function drawDate(left, top, str) {
	        ctx.fillStyle = global.color_border;
	        ctx.fillRect(left, top, global.HD, 8 * global.HD);
	        ctx.fillStyle = global.color_font;
	        ctx.textBaseline = 'top';
	        ctx.textAlign = 'center';
	        ctx.fillText((0, _plugin.formatDate)(str), left, top + 10 * global.HD);
	    };

	    return function () {
	        var base = Math.ceil(global.offset.kline.left * 2 / global.level_current[0]);
	        ctx.translate(global.offset.date.left, global.offset.date.top);
	        ctx.font = 12 * global.HD + 'px arial';
	        global.render_data.forEach(function (item, i) {
	            if (i % base == 1) {
	                var x = parseInt(i * global.level_current[0] + global.level_current[1] / 2 + global.HD) - .5;
	                drawDate(x, 0, item[0]);
	            }
	        });
	        ctx.translate(-global.offset.date.left, -global.offset.date.top);
	    };
	};

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {

	    var max_price = void 0,
	        min_price = void 0;

	    //最后一条价格浮标
	    var lastCursor = function lastCursor(last) {
	        ctx.beginPath();
	        ctx.moveTo(global.offset.kline.width + global.HD, last[0]);
	        ctx.lineTo(global.offset.kline.width + global.HD + 6 * global.HD, last[0] - 10 * global.HD);
	        ctx.lineTo(global.offset.kline.width + global.offset.kline.left, last[0] - 10 * global.HD);
	        ctx.lineTo(global.offset.kline.width + global.offset.kline.left, last[0] + 10 * global.HD);
	        ctx.lineTo(global.offset.kline.width + global.HD + 6 * global.HD, last[0] + 10 * global.HD);
	        ctx.moveTo(global.offset.kline.width + global.HD, last[0]);
	        ctx.fill();
	        ctx.textAlign = 'left';
	        ctx.fillStyle = '#fff';
	        ctx.fillText(last[2].toFixed(2), global.offset.kline.width + global.HD + 10 * global.HD, last[0]);
	    };

	    ///绘制蜡烛
	    var drawCandle = function drawCandle(left, close, min_range, max, max_range, width, lineWidth, lineLeft) {

	        ctx.lineWidth = lineWidth;

	        if (min_range > 0) {
	            ctx.fillStyle = global.color_up;
	            ctx.strokeStyle = global.color_up;
	            ctx.fillRect(left + lineLeft, max, lineWidth, max_range);
	            if (width >= global.HD) {
	                ctx.clearRect(left, close, width, min_range);
	                ctx.strokeRect(left + .5, close, width, min_range);
	            }
	        } else {
	            ctx.fillStyle = global.color_down;
	            ctx.strokeStyle = global.color_down;
	            ctx.fillRect(left + lineLeft, max, lineWidth, max_range);
	            if (width >= global.HD) {
	                ctx.fillRect(left, close, width, min_range);
	                ctx.strokeRect(left + .5, close, width, min_range);
	            }
	        }
	    };

	    //绘制刻度
	    var drawRule = function drawRule() {
	        var rows = parseInt(global.offset.kline.view_height / global.kline_row_minspace / global.HD);
	        var rows_space = global.offset.kline.view_height / rows;
	        var average = (global.render_data_price.max - global.render_data_price.min) / rows;
	        var width = global.offset.kline.width;
	        var line_offset = 5 * global.HD;
	        var text_offset = 7 * global.HD;
	        ctx.fillStyle = global.color_font;
	        ctx.strokeStyle = global.color_dashed;
	        ctx.font = 12 * global.HD + 'px arial';
	        ctx.textBaseline = 'middle';
	        ctx.lineWidth = global.HD;
	        ctx.setLineDash([5 * global.HD]);
	        while (rows >= 0) {
	            var beginY = parseInt(rows * rows_space) + 0.5;
	            var price = (global.render_data_price.max - average * rows).toFixed(2);
	            ctx.beginPath();
	            ctx.moveTo(-line_offset, beginY);
	            ctx.lineTo(width + line_offset, beginY, line_offset);
	            ctx.stroke();
	            ctx.textAlign = 'left';
	            ctx.fillText(price, width + text_offset, beginY);
	            ctx.textAlign = 'right';
	            ctx.fillText(price, -text_offset, beginY);
	            rows--;
	        }
	        ctx.setLineDash([10000]);
	    };

	    var int = function int(number) {
	        return parseInt(number).toString().replace(/\d/g, '0').replace(/^\d/g, '1') / 100;
	    };

	    return function () {
	        max_price = (0, _plugin.getMaxMin)(global.render_data, 2).max;
	        min_price = (0, _plugin.getMaxMin)(global.render_data, 3).min;
	        var range = max_price - min_price;

	        //通过最大最小值转换，两者差距越大区波动间越大，区间越小波动区间越小
	        var tmp = 1 - (max_price - min_price) / max_price;

	        global['render_data_price']['max'] = max_price * (1 + tmp * .01);
	        global['render_data_price']['min'] = min_price * (1 - tmp * .01);

	        // 标尺刻度转换为整数，通过divisor转换为整数倍的除数，在对最大值向上取整，最小值向下取整。
	        var divisor = int(global.render_data_price.max);
	        global['render_data_price']['max'] = Math.ceil(global.render_data_price.max / divisor) * divisor;
	        global['render_data_price']['min'] = Math.floor(global.render_data_price.min / divisor) * divisor;

	        global['render_data_price']['range'] = global.render_data_price.max - global.render_data_price.min;

	        var level = global.level_current[0];
	        var width = global.level_current[1];
	        var draw_max = false;
	        var draw_min = false;
	        var last = [];

	        ctx.translate(global.offset.kline.left + global.HD, global.offset.kline.view_top);
	        drawRule();
	        var lineWidth = 1;
	        var lineLeft = 0;
	        if (width >= global.HD) {
	            lineWidth = global.HD;
	            lineLeft = width / 2 - global.HD / 2;
	        }

	        global.render_data.forEach(function (item, i) {
	            var left = parseInt(i * level);
	            var open = (0, _plugin.price2Y)(global, item[1]);
	            var max = (0, _plugin.price2Y)(global, item[2]);
	            var min = (0, _plugin.price2Y)(global, item[3]);
	            var close = (0, _plugin.price2Y)(global, item[4]);

	            drawCandle(left, close, open - close, max, min - max, width, lineWidth, lineLeft);
	            last = [close, i, item[4]];
	        });

	        lastCursor(last);
	        ctx.translate(-global.offset.kline.left - global.HD, -global.offset.kline.view_top);
	    };
	};

	// export default (ctx) => {


	//     // let offset
	//     // let config
	//     // ctx.time = function(_offset, _config, data) {
	//     //     offset = _offset
	//     //     config = _config
	//     //     ctx.translate(offset.kline.left, offset.kline.top)
	//     //     time_sharing_rule()
	//     //     let gradient = ctx.createLinearGradient(0, 0, 0, offset.kline.height)
	//     //     let level = config.level[config.level_current_index]
	//     //     gradient.addColorStop("0", config.up)
	//     //     gradient.addColorStop("0.49999", config.up)
	//     //     gradient.addColorStop("0.5", "#fff")
	//     //     gradient.addColorStop("0.50001", config.down)
	//     //     gradient.addColorStop("1.0", config.down)
	//     //     ctx.beginPath()
	//     //     ctx.lineWidth = 2

	//     //     data.forEach((item, i) => {
	//     //         let x = parseInt(i * level[0])
	//     //         let y = config.priceToy(item[4])
	//     //         ctx.lineTo(x + level[3], y);
	//     //     })

	//     //     ctx.strokeStyle = gradient;
	//     //     ctx.stroke();
	//     //     ctx.translate(-offset.kline.left, -offset.kline.top);
	//     // }

	//     // function time_sharing_rule() {
	//     //     let height = offset.kline.height
	//     //     let row = parseInt(height / (config.price_row_space / 2))
	//     //     let rows = row % 2 == 0 ? row : row + 1
	//     //     let space = height / rows
	//     //     let end = offset.kline.width
	//     //     let average = config.range.price / rows

	//     //     ctx.fillStyle = toConfig.skin.font;
	//     //     ctx.font = '12px';
	//     //     ctx.textBaseline = 'middle';
	//     //     ctx.lineWidth = 1;

	//     //     for (var i = 0; i <= rows; i++) {
	//     //         var beginY = parseInt(i * space),
	//     //             price = (config.max.price - average * i).toFixed(2);
	//     //         ctx.strokeStyle = toConfig.skin.space_border;
	//     //         if (price > toConfig.closePrice) {
	//     //             ctx.fillStyle = config.up;
	//     //         } else if (price < toConfig.closePrice) {
	//     //             ctx.fillStyle = config.down;
	//     //         } else {
	//     //             ctx.fillStyle = toConfig.skin.font;
	//     //             ctx.strokeStyle = toConfig.skin.font;
	//     //         }

	//     //         /**分隔线**/
	//     //         ctx.beginPath();
	//     //         ctx.moveForDashed(0, beginY);
	//     //         ctx.dashedLineTo(end, beginY, 4);
	//     //         ctx.stroke();

	//     //         /**右侧价格**/
	//     //         ctx.strokeStyle = toConfig.skin.border;
	//     //         ctx.beginPath();
	//     //         ctx.moveTo(end, beginY);
	//     //         ctx.lineTo(end + 7, beginY);
	//     //         ctx.stroke();
	//     //         ctx.textAlign = 'left';
	//     //         ctx.fillText((Math.abs(price - toConfig.closePrice) / toConfig.closePrice * 100).toFixed(2) + '%', end + 9, beginY);

	//     //         /**左侧价格**/
	//     //         ctx.beginPath();
	//     //         ctx.moveTo(0, beginY);
	//     //         ctx.lineTo(-7, beginY);
	//     //         ctx.stroke();
	//     //         ctx.textAlign = 'right';
	//     //         ctx.fillText(price, -9, beginY);
	//     //     }
	//     // }
	// }

/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {
	    var flag = 'MA';

	    var drawMa = function drawMa(N, color) {
	        var data = global[flag + N] = (0, _plugin.computeMa)(global, N);
	        var width = global.level_current[0];
	        var space = global.level_current[1] / 2;
	        var isbegin = 0;
	        ctx.lineWidth = global.HD;
	        data.forEach(function (item, i) {
	            if (!isbegin && item > 0) {
	                isbegin = 1;
	                ctx.beginPath();
	                ctx.moveTo(parseInt(i * width) + space, (0, _plugin.price2Y)(global, item));
	            }
	            item > 0 && ctx.lineTo(parseInt(i * width) + space, (0, _plugin.price2Y)(global, item));
	        });
	        ctx.strokeStyle = color;
	        ctx.stroke();
	    };

	    //划过后在K线区域，展示ma信息，触发位置在划过K线区域
	    global.hover_callback_list.push(function (ctx, left, top) {
	        global.option.MA && global.ma.forEach(function (N, i) {
	            var price = global[flag + N][global.index];
	            if (price != undefined) {
	                var text = ['  MA' + N, price.toFixed(2)].join(':');
	                ctx.fillStyle = global.ma_color[i];
	                ctx.fillText(text, left, top);
	                left += ctx.measureText(text).width;
	            }
	        });

	        //将累加后的left返回
	        return left;
	    });

	    return function () {
	        if (global.option.MA) {
	            ctx.translate(global.offset.kline.left + global.HD, global.offset.kline.view_top);
	            global.ma.forEach(function (N, i) {
	                return drawMa(N, global.ma_color[i]);
	            });
	            ctx.translate(-global.offset.kline.left - global.HD, -global.offset.kline.view_top);
	            ctx.clearRect(0, global.offset.kline.height, global.offset.width, global.offset.height);
	        }
	    };
	};

/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _plugin = __webpack_require__(2);

	exports.default = function (ctx, global) {
	    var type = ['BOLL', 'UB', 'LB'];
	    var flag = 'BOLL';
	    var drawBoll = function drawBoll(type, color, ma, md) {
	        ctx.lineWidth = global.HD;
	        var width = global.level_current[0];
	        var space = global.level_current[1] / 2;
	        var isbegin = 0;
	        var _arr = [];
	        ma.forEach(function (item, i) {
	            if (item) {
	                if (type == 'UB') {
	                    item = item + md[i] * 2;
	                }
	                if (type == 'LB') {
	                    item = item - md[i] * 2;
	                }
	                if (!isbegin) {
	                    isbegin = 1;
	                    ctx.beginPath();
	                    ctx.moveTo(parseInt(i * width) + space, (0, _plugin.price2Y)(global, item));
	                }
	                item && ctx.lineTo(parseInt(i * width) + space, (0, _plugin.price2Y)(global, item));
	            }
	            _arr.push(item);
	        });
	        global['BOLL' + type] = _arr;
	        ctx.strokeStyle = color;
	        ctx.stroke();
	    };

	    //划过后在K线区域，展示ma信息，触发位置在划过K线区域
	    global.hover_callback_list.push(function (ctx, left, top) {
	        global.option.BOLL && type.forEach(function (type, i) {
	            var price = global[flag + type][global.index];
	            if (price != undefined) {
	                var text = [' ' + type + global.boll, price.toFixed(2)].join(':');
	                ctx.fillStyle = global.boll_color[i];
	                ctx.fillText(text, left, top);
	                left += ctx.measureText(text).width;
	            }
	        });

	        //将累加后的left返回
	        return left;
	    });

	    return function () {
	        if (global.option.BOLL) {
	            var ma = (0, _plugin.computeMa)(global, global.boll);
	            var md = (0, _plugin.computeMd)(global, global.boll, ma);
	            ctx.translate(global.offset.kline.left + global.HD, global.offset.kline.view_top);
	            type.forEach(function (type, i) {
	                return drawBoll(type, global.boll_color[i], ma, md);
	            });
	            ctx.translate(-global.offset.kline.left - global.HD, -global.offset.kline.view_top);
	            ctx.clearRect(0, global.offset.kline.height, global.offset.width, global.offset.height);
	        }
	    };
	};

/***/ }),
/* 18 */
/***/ (function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	exports.default = function () {
	    var timer = null;
	    var container = $('.kline');
	    var kline = KLINE(container.find('.line'));
	    var exchange = void 0,
	        variety = void 0,
	        level = void 0,
	        loop_interval = void 0;

	    var element_exchange = container.find('.exchange');
	    var element_variety = container.find('.variety');
	    var element_level = container.find('.level li');

	    var eventFrom = function eventFrom() {
	        element_exchange.on('click', 'span', function (event) {
	            setExchange($(event.target).index());
	        });
	        element_variety.on('click', 'span', function (event) {
	            var _this = $(event.target);
	            if (!_this.hasClass('none')) {
	                setVariety(_this);
	                getHistoryDate();
	            }
	        });
	        element_level.on('click', function (event) {
	            var _this = $(event.target);
	            if (!_this.hasClass('none')) {
	                setLevel(_this);
	                getHistoryDate();
	            }
	        });
	    };

	    var setVariety = function setVariety(_this) {
	        _this.parent().find('.current').removeClass('current');
	        _this.addClass('current').parent().prev().html(_this.html());
	        variety = _this.data('variety');
	    };

	    var setLevel = function setLevel(_this) {
	        _this.addClass('current').siblings().removeClass('current');
	        level = _this.data('level');
	        loop_interval = _this.data('time');
	    };

	    var containerBindEvent = function containerBindEvent() {
	        var option = $('.option');

	        $('.tools').click(function (event) {
	            $('.toolsbar').toggleClass('show');
	        });

	        $('.toolsbar').on('click', 'li', function (event) {
	            var _this = $(event.target);
	            _this.addClass('current').siblings().removeClass('current');
	            kline.globalPush('drawingType', _this.data('type'));
	        });

	        $('.fullscreen').click(function (event) {
	            var _this = $(event.target);
	            if (_this.hasClass('current')) {
	                container.removeAttr('style');
	                $('.line').removeAttr('style');
	                kline.resize();
	                _this.removeClass('current').html('全屏');
	            } else {
	                container.css({
	                    height: '100%',
	                    width: '100%',
	                    position: 'fixed',
	                    top: 0,
	                    left: 0
	                });
	                $('.line').css({
	                    height: container.height() - $('.menu').outerHeight()
	                });
	                kline.resize();
	                _this.addClass('current').html('退出全屏');
	            }
	        });

	        option.on('click', 'span', function (event) {
	            var _this = $(event.target);
	            _this.addClass('current').siblings().removeClass('current');
	            for (var key in kline.option) {
	                kline.option[key] = false;
	            }
	            kline.option[_this.html()] = true;
	            kline.redraw();
	        }).on('change', 'input', function (event) {
	            var _arr = [];
	            var _this = $(event.target);
	            _this.parent().find('input').each(function (index, el) {
	                var _val = /\d/g.test(el.value) ? el.value : 0;
	                el.value = _val;
	                if (_val > 0) {
	                    _arr.push(_val);
	                }
	            });
	            kline.globalPush(_this.data('type'), _arr);
	            kline.redraw();
	        }).on('click', 'button', function (event) {
	            var _this = $(event.target);
	            var _arr = [];
	            _this.parent().prev().find('input').each(function (index, el) {
	                var _val = $(el).data('default');
	                el.value = _val;
	                if (_val > 0) {
	                    _arr.push(_val);
	                }
	            });
	            kline.globalPush(_this.data('type'), _arr);
	            kline.redraw();
	        }).find('span').eq(0).addClass('current');
	    };

	    var setExchange = function setExchange(n) {
	        var _this = element_exchange.find('span').eq(n);
	        var _name = _this.data('name');
	        var _exchange = _this.data('exchange');

	        //exchange
	        element_exchange.find('dt').html(_this.html());
	        element_exchange.find('span').removeClass('current').eq(n).addClass('current');
	        if (exchange == _exchange) return;
	        exchange = _exchange;

	        //filter ariety 
	        var _variety = null;
	        element_variety.find('span').removeClass('none').each(function (index, el) {
	            el = $(el);
	            if (!el.hasClass(_name)) {
	                el.addClass('none');
	            } else {
	                if (_variety == null) {
	                    setVariety(el);
	                    _variety = true;
	                }
	            }
	        });

	        //filter level 
	        var _index = null;
	        element_level.removeClass('none').each(function (index, el) {
	            el = $(el);
	            if (!el.hasClass(_name)) {
	                el.addClass('none');
	            } else {
	                if (el.hasClass('current')) {
	                    _index = index;
	                }
	            }
	        });

	        setLevel(element_level.eq(_index = null ? 0 : _index));

	        getHistoryDate();
	    };

	    var getHistoryDate = function getHistoryDate() {
	        clearTimeout(timer);
	        timer = null;
	        $.ajax({
	            type: 'post',
	            url: exchange == 0 ? '/indexmarket/kline/history' : '/market/kline/history',
	            dataType: 'json',
	            data: {
	                variety: variety,
	                exchange: exchange,
	                level: level,
	                klinenum: 2000,
	                end: Math.floor(new Date().getTime() / 1000)
	            },
	            success: function success(result) {
	                kline.init(result);
	                loopRealtime();
	            }
	        });
	    };

	    var getRealTime = function getRealTime() {
	        $.ajax({
	            type: 'post',
	            url: exchange == 0 ? '/indexmarket/kline/realtime' : '/market/kline/realtime',
	            dataType: 'json',
	            data: {
	                variety: variety,
	                exchange: exchange,
	                level: level,
	                klinenum: 2
	            },
	            success: function success(result) {
	                kline.push(result);
	                document.title = result[1][4];
	            }
	        });
	    };

	    var loopRealtime = function loopRealtime() {
	        timer = setTimeout(function () {
	            getRealTime();
	            loopRealtime();
	        }, loop_interval);
	    };

	    var chan = function chan() {};

	    var init = function init() {
	        $(window).resize(function () {
	            kline.resize();
	        });
	        eventFrom();
	        containerBindEvent();
	        setExchange(0);
	    };
	    init();
	};

/***/ })
/******/ ]);