package com.taoge.service;

import com.taoge.biz.common.redis.key.ArticleRedisKey;
import com.taoge.biz.server.ArticleServer;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.util.UserContext;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StatisticsService {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ArticleServer articleServer;
    @Resource
    ArticleCacheService articleCacheService;

    /**
     * 浏览文章
     */
    public void browseArticle(Long id){

        UserInfo userInfo = UserContext.get();
        //记录浏览PV
        incrementArticleBrowsePv(id);
        //记录浏览UV
        incrementArticleBrowseUv(id, userInfo.getUserId());
        //记录浏览历史
        articleCacheService.addRecently(userInfo.getUserId(),id);


    }



    private void incrementArticleBrowsePv(Long id) {

        // 1. 记录正在累计访问pv的文章id集合（ statistics:ing:ids ->  [文章id1，id2，id3]）
        stringRedisTemplate.opsForSet().add(ArticleRedisKey.getArticleStatisticsIngPvIds(),id.toString());

        // 2. 为当前访问的文章pv数缓存，自增+1（ statistics:ing:id -> 文章id1）
        Long pvCount = stringRedisTemplate.opsForValue().increment(ArticleRedisKey.getArticleStatisticsIngPvId(id));

        if (pvCount == null){
            pvCount = 0L;
        }

        //直接更新统计数据中的browsePv
        stringRedisTemplate.opsForHash().increment(ArticleRedisKey.getArticleStatisticsKey(id),"browsePv",1);

       // 3. 校验当前累计访问的pv数量，按量级是否达到标准，去更新数据库
        if (pvCount < 1000 && pvCount % 100 == 0){
            //1000以下，每增加100次pv，更新一次数据库。10000以下，每访问1000次，更新一次数据库
            syncArticleBrowsePv(id);
        } else if (pvCount < 10000 && pvCount % 1000 == 0) {
            syncArticleBrowsePv(id);
        } else if (pvCount % 10000 == 0) {
            syncArticleBrowsePv(id);
        }
    }

    /**
     * 同步刷新文章统计pv数量
     */
    private void syncArticleBrowsePv(Long id) {
        //调用底层服务,刷新数据库
        articleServer.syncArticleBrowsePv(id);

    }


    private void incrementArticleBrowseUv(Long id,Long userId) {
        //hyperloglog
        //1. 记录正在累计访问uv的文章id集合（ statistics:ing:ids ->  [文章id1，id2，id3]）
        stringRedisTemplate.opsForSet().add(ArticleRedisKey.getArticleStatisticsIngUvIds(),id.toString());
        Long add = stringRedisTemplate.opsForHyperLogLog().add(ArticleRedisKey.getArticleStatisticsIngUvId(id),userId.toString());
        //如果add= 0,代表已经添加过

        if (add != 0){
            //更新redis中的ArticleStatistics的uv + 1
            stringRedisTemplate.opsForHash().increment(ArticleRedisKey.getArticleStatisticsKey(id),"browseUv",1);

        }
    }



}
