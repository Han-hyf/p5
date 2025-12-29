package com.taoge.biz.config.redis;

import com.taoge.biz.server.vo.article.ArticleStatisticsVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
public class ServerRedisConfig {
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, ArticleStatisticsVO> articleStatisticsRedisTemplate() {
        RedisTemplate<String, ArticleStatisticsVO> redisTemplate = new RedisTemplate<>();
        // string 类型
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(ArticleStatisticsVO.class));

        // hash 类型
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // redisTemplate.opsForValue() -> redis set , string 数据类型
        return redisTemplate;
    }
}
