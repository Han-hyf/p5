package com.taoge.api.common.redis;

import com.alibaba.fastjson.JSON;
import com.taoge.biz.common.redis.msg.ArticleRedisMsg;
import com.taoge.service.ArticleCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class ArticleSubscribe implements MessageListener {
    @Resource
    ArticleCacheService articleCacheService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        ArticleRedisMsg msg = JSON.parseObject(new String(message.getBody()),ArticleRedisMsg.class);
        log.info("onMessage message:{}",msg);
        articleCacheService.refreshArticleDetailCache(msg.getId());
    }
}
