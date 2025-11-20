var constants = {
    "status": {
        1: "启用",
        0: "禁用"
    },
    "statusBtnStr": {
        0: "启用",
        1: "禁用"
    },
    "isActivity": {
        0: "未激活",
        1: "已激活"
    },
    "sysResourceType": {
        1: "页面",
        2: "接口"
    },
    "top": {
        1: "页面",
        2: "接口"
    },
    "isLock": {
        0: "未锁定",
        1: "已锁定"
    },
    "isLockBtnStr": {
        0: "锁定",
        1: "解锁"
    },
    "isBanned": {
        0: "未禁播",
        1: "已禁播"
    },
    "isBannedBtnStr": {
        0: "禁播",
        1: "解禁"
    },
    "gender": {
        0: "女",
        1: "男"
    },
    "isDelete": {
        0: "未删除",
        1: "已删除"
    },
    // 直播房间类型
    roomType:{
        PUBLIC:'普通房间',
        PRIVATE:'密码房间',
        TICKET:'门票房间',
        TIMER:'计时房间'
    },
    // 直播状态
    "liveStatus" : {
        PREV:'预开播',
        ING:'直播中',
        CANCEL:'直播取消',
        LEAVE:'离开',
        EXPIRED:'已过期',
        OVER:'直播结束'
    },
    "liveAuditStatus": {
        'INIT': "未提交",
        'WAIT': "待审核",
        'PASS': "审核通过",
        'REJECT': "审核拒绝",
        'CANCEL':"取消"
    },
    "isTop": {
        0: "未置顶",
        1: "已置顶"
    },
    "isYN":{
        1:'是',
        0:'否'
    },
    "isSell": {
        1: '是',
        0: '否',
        2: '否'
    },
    "isHot": {
        0: "未热门",
        1: "已热门"
    },
    "isShare": {
        0: "未分享",
        1: "已分享"
    },
    "isMargin": {
        0: "否",
        1: "是"
    },
    "marginStatus": {
        0: "未缴费",
        1: "已缴费"
    },
    "auditStatus": {
        'AUDIT_TOAUDIT': "待审核",
        'AUDIT_PASS': "审核成功",
        'AUDIT_REJECT': "审核失败"
    },
    // 主播审核状态
    anchor_auditStatus:{
        INIT:'初始化',
        WAIT_AUDIT:'等待审核',
        AUDIT_SUCCESS:'审核通过',
        AUDIT_REJECT:'审核拒绝'
    },
    // 主播认证类型
    anchor_auditType:{
        ENTERPRISE:'企业认证',
        PERSONAL:'个人认证'
    },
    // 主播直播类型
    anchor_liveStatus:{
        'NOT':'未开播',
        'BAN':'禁播',
        'ING':'开播中'
    },
    "statusCategory": {
        'AUDIT_TOAUDIT': "待审核",
        'AUDIT_PASS': "通过",
        'AUDIT_REJECT': "不通过"
    },
    "appAuditStatus": {
        'WAIT': "待审核",
        'PASS': "通过",
        'REJECT': "不通过"
    },

    "appAccessStatus": {
        'WAIT': "待接入",
        'ACCESS': "接入中",
        'AUDIT': "待审核",
        'PASS': "通过",
        'REJECT': "不通过"
    },
    "storeStatus": {
        0: "经营异常",
        1: "正常运营"
    },
//  文章状态
    "isArticle":{
        0: "不可用",
        1: "可用"
    },
//    是否发布
    "isPublish":{
        0: "未发布",
        1: "已发布"
    },
// 订单状态
    "orderStatus":{
        PENDING_REVIEW:'待审核',
        PENDING_DELIVERY:'待发货',
        DELIVERY:'已发货',
        RECEIVED:'已收货',
        COMPLETED:'已完成',
        CANCELED_BUYERS:'买家已取消',
        CANCELED_SELLERS:'卖家已取消',
        REJECTED:'已拒绝',
        NOT_SIGNED:'已拒签',
        CLOSED:'已关闭'
    },
    // 退货状态
    "returnStatus":{
        PENDING_REVIEW:"待审核",
        PENDING_BUYERS_DELIVERY:"待买家发货",
        PENDING_SELLERS_RECEIPT:"待卖家收货",
        RETURN_COMPLETED:"退货已完成",
        COMPLETED:"已完成",
        CLOSED:"已关闭"
    },
    // 换货状态
    "exchangeStatus":{
        PENDING_REVIEW:"待审核",
        PENDING_BUYERS_DELIVERY:"待买家发货",
        PENDING_SELLERS_RECEIPT:"待卖家收货",
        PENDING_SELLERS_DELIVERY:"待卖家发货",
        PENDING_BUYERS_RECEIPT:"待买家收货",
        COMPLETED:"已完成",
        CLOSED:"已关闭"
    },
    //退款方式
    "refundType": {
        0: '微信',
        1: '支付宝'
    },
    //售后类型
    "serviceType": {
        0: '换货',
        1: '退货'
    },
    //售后状态
    "afterServeStatus": {
        WAIT: '待审核',
        BUYER_WAIT_DELIVERED: '买家待发货',
        SELLER_WAIT_RECEIVED: '卖家待收货',
        SELLER_WAIT_DELIVERED: '卖家待发货',
        BUYER_WAIT_RECEIVED: '买家待收货',
        BUYER_REJECT_RECEIVED: '买家拒绝收货',
        REFUN: '待退款',
        COMPLETE: '完成',
        AUDIT_REJECT: '审核拒绝',
        SELLER_REJECT_RECEIVED: '拒绝收货',
        CANCEL: '撤销申请'
    },
    // 付款方式
    "payType": {
        COD: '货到付款',
    },
    //退款方式
    "refundType":{
        0:'微信',
        1:'支付宝'
							   
    },
    "noticeStatus": {
        0: "失败",
        1: "成功"
    },
    "isIndustryAptitude": {
        1: "是",
        0: "否"
    },
    "mcnAuditStatus" : {
        'INIT':'未提交',
        'WAIT':'待审核',
        'PASS':'审核通过',
        'REJECT':'审核拒绝'
    },
     "anchorAuditStatus" : {
         'INIT': "未提交",
         'WAIT': "待审核",
         'PASS': "审核通过",
         'REJECT': "审核拒绝"
     },
     "anchorNotInitAuditStatus" : {
         'WAIT': "待审核",
         'PASS': "审核通过",
         'REJECT': "审核拒绝"
     },
     "anchorAcceptStatus" : {
         'WAIT': "待接收",
         'PASS': "已接收",
         'REJECT': "拒绝接收"
     },
     "isShow" : {
         0: "不显示",
         1: "显示"
     },
    "userType" : {
        0: "普通用户",
        1: "微信小程序用户",
        2: "APP商家用户",
        3: "主播用户",
        9: "游客",
        99: "无效游客"
    },
    "payType" : {
        'BANKPAY': "银行",
        'WECHATPAY': "微信",
        'ALIPAY': "支付宝"
    },
    "heeType" : {
        'SMALL': "小微",
        'ENTERPRISE': "企业"
    },
    "heeStatus" : {
        'INIT': "未提交",
        'WAIT': "等待审核",
        'HEE_SUCCESS': "汇元网审核通过",
        'HEE_FAIL': "汇元网审核失败",
        'BIND_SUCCESS': "用户绑定服务商成功",
        'SUCCESS': "审核成功"
    }
};
$.constants = function(field, key) {
    key = key != null ? key += "" : key;
    return key ? constants[field][key] || "" : constants[field];
};

/**
* @param field
* @param v
* @param isReverse 是否翻转，按常量顺序翻转
**/
$.constantsOption = function (field, v, isReverse) {
    field = $.constants(field);
    var optionStr = "";
    if (field) {
        for (var key in field) {
            if (isReverse) {
                optionStr = '<option value="' + key + '" ' + (v == key ? "selected" : "") + '>' + field[key] + '</option>' + optionStr;
            } else {
                optionStr += '<option value="' + key + '" ' + (v == key ? "selected" : "") + '>' + field[key] + '</option>';
            }
        }
    }
    return optionStr;
};

$(function () {
    $("[yq-select]").each(function (i, n) {
        $(n).append($.constantsOption($(n).attr("yq-select")));
    });
});