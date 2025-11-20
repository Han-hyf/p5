package com.taoge.biz.mq.enums;

/**
 * mq业务类型枚举
 */
public enum MqBusinessTypeEnum {
    SYNC_ARTICLE_STATISTICS_BROWSE_PV("sync.article.statistics.browsePv"),
    SYNC_ARTICLE_STATISTICS_BROWSE_UV("sync.article.statistics.browseUv");
    private final String routingKey ;


    MqBusinessTypeEnum(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRoutingKey(){
        return routingKey;
    }

    /**
     * 根据routingKey获得enum
     */
    public static MqBusinessTypeEnum getByRoutingKey(String routingKey){
        if (null == routingKey){
            return null;
        }
        for (MqBusinessTypeEnum value : MqBusinessTypeEnum.values()){
            if (value.getRoutingKey().equals(routingKey)){
                return value;
            }
        }
        return null;
    }
}
