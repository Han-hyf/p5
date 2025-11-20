package com.taoge.config;

import com.taoge.biz.mq.enums.MqExchangeEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //定义queue名字
    public static final String DEFAULT_QUEUE = "default_queue";
    //routing key
    private static final String DEFAULT_ROUTING_KEY = "#";


    //定义队列
    @Bean
    public Queue defaultQueue(){
        return new Queue(DEFAULT_QUEUE);
    }

    //定义交换
    @Bean
    public TopicExchange defaultExchange(){
        return new TopicExchange(MqExchangeEnum.DEFAULT.getExchange());
    }

    //绑定队列到交换机
    @Bean
    public Binding bindDefaultQueue(){
        return BindingBuilder.bind(defaultQueue()).to(defaultExchange()).with(DEFAULT_ROUTING_KEY);
    }

}
