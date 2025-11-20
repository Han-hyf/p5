package com.taoge.mq;


import com.taoge.biz.mq.enums.MqBusinessTypeEnum;
import com.taoge.biz.mq.msg.SyncArticleStatisticsBrowsePv;
import com.taoge.biz.server.ArticleServer;
import com.taoge.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RabbitMqDefaultConsumer {

    @Resource
    ArticleServer articleServer;

    @RabbitListener(queues = {RabbitMqConfig.DEFAULT_QUEUE})
    public void message(Message<?> message){
        log.info("{}",message);
        Object routingKey = message.getHeaders().get("amqp_receivedRoutingKey");
        if (null != routingKey){
            MqBusinessTypeEnum businessType = MqBusinessTypeEnum.getByRoutingKey(routingKey.toString());
            switch (businessType){
                case SYNC_ARTICLE_STATISTICS_BROWSE_PV:
                    //
                    SyncArticleStatisticsBrowsePv msg = (SyncArticleStatisticsBrowsePv) message.getPayload();
                    articleServer.doSyncArticleBrowsePv(msg.getId());
            }

        }

    }
}
