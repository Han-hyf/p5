package com.taoge.service;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.taoge.api.article.detail.ArticleDetailVO;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.common.redis.key.ArticleRedisKey;
import com.taoge.biz.server.ArticleServer;
import com.taoge.biz.server.vo.article.ArticleStatisticsVO;
import com.taoge.framework.util.BeanMapUtil;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleCacheService {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate<String,ArticleStatisticsVO> articleStatisticsRedisTemplate;
    @Resource
    ArticleServer articleServer;

    private final Cache<Long, ArticleDetailVO> articleCache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    private final Cache<Long, List<ArticleDetailVO>> articleListCache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    //从内存缓存查询数据
    public ArticleDetailVO getArticleDetailByCache(Long id){
        ArticleDetailVO vo = articleCache.getIfPresent(id);
        if (vo == null){
            //从redis中查询数据
            vo = getArticleDetailByRedis(id);
            if (vo != null){
                //set到缓存
                articleCache.put(id,vo);
            }
        }
        return vo;
    }

    //从redis查询数据
    public ArticleDetailVO getArticleDetailByRedis(Long id){
        String articleDetailKey = ArticleRedisKey.getArticleDetailKey(id);
        String s = stringRedisTemplate.opsForValue().get(articleDetailKey);

        if (StringUtils.isEmpty(s)){
            return null;
        }
        return JSON.parseObject(s,ArticleDetailVO.class);
    }

    //添加cache和redis缓存
    public void setArticleDetailCache(ArticleDetailVO vo){
        if (vo == null || vo.getId() == null){
            return;
        }
        setArticleDetailByRedis(vo);
        articleCache.put(vo.getId(), vo);
    }


    //添加redis缓存
    public void setArticleDetailByRedis(ArticleDetailVO vo){
        if (vo == null || vo.getId() == null){
            return;
        }
        stringRedisTemplate.opsForValue().set(ArticleRedisKey.getArticleDetailKey(vo.getId()),JSON.toJSONString(vo),10,TimeUnit.MINUTES);

    }

    //更新缓存(后台修改数据),依赖redis的pub/sub
    public void refreshArticleDetailCache(Long id){
        stringRedisTemplate.delete(ArticleRedisKey.getArticleDetailKey(id));
        articleCache.invalidate(id);
    }

    //从redis里查articleStatistics
    public ArticleStatisticsVO getArticleStatisticsByRedis(Long articleId){
        Map<Object, Object> entries = articleStatisticsRedisTemplate.opsForHash().entries(ArticleRedisKey.getArticleStatisticsKey(articleId));
        if (CollectionUtils.isEmpty(entries)){
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(entries),ArticleStatisticsVO.class);
    }

    //像redis里插入articleStatistics
    public void setArticleStatisticsByRedis(ArticleStatisticsVO vo){
        articleStatisticsRedisTemplate.opsForHash().putAll(ArticleRedisKey.getArticleStatisticsKey(vo.getArticleId()),
                BeanMapUtil.toHashMap(vo));
        articleStatisticsRedisTemplate.expire(ArticleRedisKey.getArticleStatisticsKey(vo.getArticleId()),10,TimeUnit.MINUTES);

    }


    /**
     * 浏览历史
     */
    public void addRecently(Long userId, Long id) {

        //用zset类型,score是时间戳
        String key  = ArticleRedisKey.getArticleRecentlyKey(userId);
        stringRedisTemplate.opsForZSet().add(key,id.toString(),System.currentTimeMillis());
        stringRedisTemplate.expire(key,30L,TimeUnit.DAYS);

    }

    public List<ArticleDetailVO> recentlyList(Long userId,int pageNum,int pageSize){
        String key = ArticleRedisKey.getArticleRecentlyKey(userId);
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize - 1;
        Set<String> ids = stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
        return articleListByIds(ids);
    }

    private List<ArticleDetailVO> articleListByIds(Set<String> ids) {
        //去查缓存和redis
        List<ArticleDetailVO> voList = selectArticleDetailListByCache(ids);

        if (voList.size() == ids.size()){
            //全部命中缓存和redis
            return voList;
        }

        //保留未命中的id
        for (ArticleDetailVO vo : voList) {
            ids.remove(vo.getId().toString());
        }

        for (String noCacheID : ids) {
            //未命中缓存和redis,从数据库查询
            IdParam idParam = new IdParam();
            idParam.setId(Long.parseLong(noCacheID));
            com.taoge.biz.server.vo.article.ArticleDetailVO articleDetailVO = articleServer.articleDetail(idParam);
            if (articleDetailVO != null){
                ArticleDetailVO dbVo = articleDetailVO.convertTo(ArticleDetailVO.class);
                voList.add(dbVo);
                //放在缓存中
                setArticleDetailCache(dbVo);
            }
        }


        return voList;
    }

    private List<ArticleDetailVO> selectArticleDetailListByCache(Set<String> ids){

        Map<@NonNull Long, @NonNull ArticleDetailVO> cacheMap = articleCache.getAllPresent(ids);
        ArrayList<@NonNull ArticleDetailVO> resultList = new ArrayList<>(cacheMap.values());
        if (cacheMap.size() == ids.size()){
            //两个size相等,表示全部命中缓存,直接返回
            return resultList;
        }

        //记录未命中的id
        List<Long> noCacheIds = new ArrayList<>();
        for (String id : ids) {
            if (cacheMap.get(Long.parseLong(id)) == null){
                noCacheIds.add(Long.parseLong(id));
            }
        }

        //没有命中缓存的,去查redis

        for (Long noCacheId : noCacheIds) {
            ArticleDetailVO redisCacheVo = getArticleDetailByRedis(noCacheId);
            if (null != redisCacheVo){
                resultList.add(redisCacheVo);
                //从redis 中拿到后,放进内存缓存
                articleCache.put(noCacheId,redisCacheVo);
            }
        }
        return resultList;
    }

}
