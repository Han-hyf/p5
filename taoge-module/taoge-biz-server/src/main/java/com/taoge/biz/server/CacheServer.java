package com.taoge.biz.server;


import com.alibaba.fastjson.JSON;
import com.taoge.biz.common.redis.RedisTopicEnum;
import com.taoge.biz.common.redis.msg.BaseRedisMsg;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CacheServer {

    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**
     * 发送pub/sub消息
     */
    public <T extends BaseRedisMsg> void sendTopic(RedisTopicEnum redisTopic, T msg){
        stringRedisTemplate.convertAndSend(redisTopic.name(), JSON.toJSONString(msg));
    }

}
