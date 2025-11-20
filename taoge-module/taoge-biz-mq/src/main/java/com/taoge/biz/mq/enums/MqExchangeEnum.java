package com.taoge.biz.mq.enums;

public enum MqExchangeEnum {

    DEFAULT("default_exchange");

    private final String exchange;

    MqExchangeEnum(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange(){
        return exchange;
    }
}
