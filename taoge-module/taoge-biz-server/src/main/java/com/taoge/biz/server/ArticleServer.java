package com.taoge.biz.server;

import com.github.pagehelper.PageInfo;
import com.taoge.biz.common.enums.ArticleStatusEnum;
import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.common.redis.RedisTopicEnum;
import com.taoge.biz.common.redis.key.ArticleRedisKey;
import com.taoge.biz.common.redis.msg.ArticleRedisMsg;
import com.taoge.biz.mq.enums.MqBusinessTypeEnum;
import com.taoge.biz.mq.enums.MqExchangeEnum;
import com.taoge.biz.mq.msg.SyncArticleStatisticsBrosePv;
import com.taoge.biz.mq.service.RabbitMqProducer;
import com.taoge.biz.persistent.entity.Article;
import com.taoge.biz.persistent.entity.ArticleCategory;
import com.taoge.biz.persistent.service.ArticleCategoryService;
import com.taoge.biz.persistent.service.ArticleService;
import com.taoge.biz.persistent.service.ArticleStatisticsService;
import com.taoge.biz.server.param.article.ArticleListParam;
import com.taoge.biz.server.param.article.SaveArticleCategoryParam;
import com.taoge.biz.server.param.article.SaveArticleParam;
import com.taoge.biz.server.vo.article.ArticleCategoryListVO;
import com.taoge.biz.server.vo.article.ArticleDetailVO;
import com.taoge.biz.server.vo.article.ArticleListVO;
import com.taoge.biz.server.vo.article.ArticleStatisticsVO;
import com.taoge.framework.util.BeanMapUtil;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServer {
    @Resource
    ArticleCategoryService articleCategoryService;
    @Resource
    ArticleService articleService;
    @Resource
    CacheServer cacheServer;
    @Resource
    ArticleStatisticsService articleStatisticsService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, ArticleStatisticsVO> articleStatisticsRedisTemplate;
    @Resource
    RabbitMqProducer rabbitMqProducer;

    public void saveArticleCategory(SaveArticleCategoryParam param) {
        ArticleCategory category = param.convertTo(ArticleCategory.class);
        if (param.getId() == null && param.getSort() == null) {
            category.setSort(9999);
        }
        articleCategoryService.save(category);
    }

    public void enableArticleCategory(IdParam param) {
        articleCategoryService.updateStatus(param.getId(), true);
    }

    public void disableArticleCategory(IdParam param) {
        articleCategoryService.updateStatus(param.getId(), false);
    }

    public List<ArticleCategory> allArticleCategory() {
        return articleCategoryService.all();
    }

    public List<ArticleCategoryListVO> validArticleCategoryList() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", 1);
        return articleCategoryService.voList(paramMap, ArticleCategoryListVO.class);
    }

    @Transactional
    public void saveArticle(SaveArticleParam param) {
        Article article = param.convertTo(Article.class);
        if (param.getId() == null) {
            article.setStatus(ArticleStatusEnum.INIT.name());
        }
        articleService.save(article);

        // 修改文章后，刷新缓存
        if (param.getId() != null) {
            refreshArticleCache(param.getId());
        } else {
            // 添加一条文章统计记录
            articleStatisticsService.init(article.getId());
        }
    }

    public void publishArticle(IdParam param) {
        articleService.publish(param.getId());
        refreshArticleCache(param.getId());
    }

    public void offlineArticle(IdParam param) {
        articleService.offline(param.getId());
        refreshArticleCache(param.getId());
    }

    public PageInfo<ArticleListVO> articleList(ArticleListParam param) {
        return articleService.pageVOList(param, ArticleListVO.class);
    }

    public ArticleDetailVO articleDetail(IdParam param) {
        Article article = articleService.get(param.getId());
        if (!ArticleStatusEnum.PUBLISH.name().equals(article.getStatus())) {
            return null;
        }
        return ArticleDetailVO.convertFrom(article, ArticleDetailVO.class);
    }

    public void refreshArticleCache(Long id) {
        ArticleRedisMsg msg = new ArticleRedisMsg();
        msg.setId(id);
        // 发送 pub/sub 信息
        cacheServer.sendTopic(RedisTopicEnum.article, msg);
    }

    public ArticleStatisticsVO getArticleStatistics(IdParam param) {
        return articleStatisticsService.getVO(param.getId(), ArticleStatisticsVO.class);
    }

    /**
     * 同步文章浏览pv数量
     *
     * @param id 文章id
     */
    public void syncArticleBrowsePv(Long id) {
        // 发送mq，异步刷新
        SyncArticleStatisticsBrosePv msg = new SyncArticleStatisticsBrosePv();
        msg.setId(id);
        rabbitMqProducer.sendMessage(msg, MqExchangeEnum.DEFAULT, MqBusinessTypeEnum.SYNC_ARTICLE_STATISTICS_BROWSE_PV);
    }

    public void doSyncArticleBrowsePv(Long id) {
//        1.从redis中获取已累计的访问pv数
        String pvCount = stringRedisTemplate.opsForValue().get(ArticleRedisKey.getArticleStatisticsIngPvId(id));
        if (StringUtils.isEmpty(pvCount)) {
            return;
        }

//        2. 修改数据库统计表，browse_pv + 累计pv数量
        articleStatisticsService.addBrowsePv(id, Long.valueOf(pvCount));

//        3. 删除已更新的pv统计数量缓存
        stringRedisTemplate.delete(ArticleRedisKey.getArticleStatisticsIngPvId(id));

//        4. 刷新统计数量缓存
        refreshArticleStatisticsCache(id);
    }

    /**
     * 刷新统计数据缓存
     */
    private void refreshArticleStatisticsCache(Long id) {
        IdParam idParam = new IdParam();
        idParam.setId(id);
        ArticleStatisticsVO vo = getArticleStatistics(idParam);
        articleStatisticsRedisTemplate.opsForHash().putAll(ArticleRedisKey.getArticleStatisticsKey(id), BeanMapUtil.toHashMap(vo));
    }

    /**
     * 刷新所有在统计浏览pv的文章
     */
    public void syncArticleBrowsePv() {
        while (true) {
            String id = stringRedisTemplate.opsForSet().pop(ArticleRedisKey.getArticleStatisticsIngPvIds());
            if (StringUtils.isEmpty(id)) {
                break;
            }
            doSyncArticleBrowsePv(Long.valueOf(id));
        }
    }

    /**
     * 刷新所有在统计浏览uv的文章
     */
    public void syncArticleBrowseUv() {
        while (true) {
            String id = stringRedisTemplate.opsForSet().pop(ArticleRedisKey.getArticleStatisticsIngUvIds());
            if (StringUtils.isEmpty(id)) {
                break;
            }
            doSyncArticleBrowseUv(Long.valueOf(id));
        }
    }

    private void doSyncArticleBrowseUv(Long id) {
//        1.从redis中获取已累计的访问uv数
        Long uvCount = stringRedisTemplate.opsForHyperLogLog().size(ArticleRedisKey.getArticleStatisticsIngUvId(id));

//        2. 修改数据库统计表，browse_uv
        articleStatisticsService.updateBrowseUv(id, Long.valueOf(uvCount));

//        3. 刷新统计数量缓存
        refreshArticleStatisticsCache(id);
    }

}
