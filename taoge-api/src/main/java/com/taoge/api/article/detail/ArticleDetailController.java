package com.taoge.api.article.detail;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.biz.server.vo.article.ArticleStatisticsVO;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.service.ArticleCacheService;
import com.taoge.service.StatisticsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleDetailController extends BaseController<IdParam> {

    @Resource
    ArticleServer articleServer;
    @Resource
    ArticleCacheService articleCacheService;
    @Resource
    StatisticsService statisticsService;

    @Guest
    @Override
    @PostMapping("/api/article/detail")
    public ResponseData<?> execute(@RequestBody IdParam param) {

        //先查询缓存,包括Caffeine内存和redis
        com.taoge.api.article.detail.ArticleDetailVO detailvo = articleCacheService.getArticleDetailByCache(param.getId());
        if (detailvo == null){
            //如果不在缓存,调用底层方法查数据库
            com.taoge.biz.server.vo.article.ArticleDetailVO vo = articleServer.articleDetail(param);
            detailvo = vo.convertTo(ArticleDetailVO.class);
            //添加到redis中
            articleCacheService.setArticleDetailCache(detailvo);

        }

        if (detailvo != null){
            //添加统计数据(从redis)
            ArticleStatisticsVO articleStatisticsVO = articleCacheService.getArticleStatisticsByRedis(param.getId());
            if (articleStatisticsVO == null){
                //redis没有,查库
                IdParam idParam = new IdParam();
                idParam.setId(detailvo.getId());
                articleStatisticsVO = articleServer.getArticleStatistics(idParam);
                articleCacheService.setArticleStatisticsByRedis(articleStatisticsVO);
            }
            detailvo.setArticleStatisticsVO(articleStatisticsVO);
            //浏览文章
            statisticsService.browseArticle(detailvo.getId());

        }

        return ResponseData.success("",detailvo);
    }
}
