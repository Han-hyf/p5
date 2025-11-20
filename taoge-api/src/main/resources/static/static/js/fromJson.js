(function($) {
    $.fn.serializeJson = function() {
    // 定义对象
    var serializeObj = {};
    //获取选择元素 转化成-数组
    var array = this.serializeArray();
    //循环对应数组
    $(array).each(function() {
        // 如果这个对象的名称存在
        if (serializeObj[this.name]) {
            if ($.isArray(serializeObj[this.name])) {
                // 取得当前的名称对应的值放到对象里
                serializeObj[this.name].push(this.value);
            } else {
                serializeObj[this.name] = [serializeObj[this.name], this.value];
            }
        } else {
            serializeObj[this.name] = this.value;
        }
    });
    return serializeObj;
    };
})(jQuery);