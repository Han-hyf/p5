package com.taoge.api.article.detail;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.biz.server.vo.article.ArticleDetailVO;
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
        // 先查询缓存
        com.taoge.api.article.detail.ArticleDetailVO detailVo = articleCacheService.getArticleDetailByCache(param.getId());
        if (detailVo == null) {
            // 未查到缓存，调用底层服务（查询数据库）
            ArticleDetailVO vo = articleServer.articleDetail(param);
            detailVo = vo.convertTo(com.taoge.api.article.detail.ArticleDetailVO.class);
            // 放到redis中
            articleCacheService.setArticleDetailCache(detailVo);
        }

        if (null != detailVo) {
            // 查文章统计数据
            ArticleStatisticsVO articleStatisticsVo = articleCacheService.getArticleStatisticsByRedis(detailVo.getId());
            if (null == articleStatisticsVo) {
                IdParam idParam = new IdParam();
                idParam.setId(detailVo.getId());
                // 缓存没有，查库
                articleStatisticsVo = articleServer.getArticleStatistics(idParam);
                articleCacheService.setArticleStatisticsByRedis(articleStatisticsVo);
            }
            detailVo.setArticleStatistics(articleStatisticsVo);
            // 浏览文章
            statisticsService.browseArticle(detailVo.getId());
        }

        return ResponseData.success("", detailVo);
    }
}
