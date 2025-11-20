//mathArrayCartesian.js
//笛卡尔积
$(function(){
	//多个一起做笛卡尔积
	$.multiCartesian = function (array) {
	    if (array.length < 2) return array[0] || [];
	    return [].reduce.call(array, function (col, set) {
	        var res = [];
	        col.forEach(function (c) {
	            set.forEach(function (s) {
	                var t = [].concat(Array.isArray(c) ? c : [c]);
	                t.push(s);
	                res.push(t);
	            })
	        });
	        return res;
	    });
	}
})