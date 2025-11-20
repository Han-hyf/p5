/*
 * @ClassName ArticleStatisticsService
 * @Description 
 * @version 1.0
 * @Date 2025-11-13 11:08:48
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.ArticleStatisticsMapper;
import com.taoge.biz.persistent.entity.ArticleStatistics;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ArticleStatisticsService extends BaseService<ArticleStatistics ,ArticleStatisticsMapper> {
}