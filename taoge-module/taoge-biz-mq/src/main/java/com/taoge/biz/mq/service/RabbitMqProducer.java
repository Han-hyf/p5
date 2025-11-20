package com.taoge.biz.mq.service;

import com.taoge.biz.mq.enums.MqBusinessTypeEnum;
import com.taoge.biz.mq.enums.MqExchangeEnum;
import com.taoge.biz.mq.msg.BaseMqMsg;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RabbitMqProducer {

    @Resource
    RabbitTemplate rabbitTemplate;

    //发送消息
    public <T extends BaseMqMsg>  void sendMessage(T msg, MqExchangeEnum exchange, MqBusinessTypeEnum mqBusinessType){
        rabbitTemplate.convertAndSend(exchange.getExchange(),mqBusinessType.getRoutingKey(),msg);
    }

}
