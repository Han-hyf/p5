/*
 * @ClassName ArticleStatisticsService
 * @Description
 * @version 1.0
 * @Date 2023-11-08 20:24:44
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.ArticleStatisticsMapper;
import com.taoge.biz.persistent.entity.ArticleStatistics;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public class ArticleStatisticsService extends BaseService<ArticleStatistics, ArticleStatisticsMapper> {

    public void init(Long articleId) {
        ArticleStatistics articleStatistics = new ArticleStatistics();
        articleStatistics.setArticleId(articleId);
        insertSelective(articleStatistics);
    }

    /**
     * 累加浏览pv
     *
     * @param articleId 文章id
     * @param browsePv  浏览pv
     */
    public void addBrowsePv(Long articleId, Long browsePv) {
        getMapper().addBrowsePv(articleId, browsePv);
    }

    /**
     * 更新浏览uv
     *
     * @param articleId 文章id
     * @param browseUv  浏览uv
     */
    public void updateBrowseUv(Long articleId, Long browseUv) {
        getMapper().updateBrowseUv(articleId, browseUv);
    }
}