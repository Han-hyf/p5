/*
 * @ClassName ArticleService
 * @Description
 * @version 1.0
 * @Date 2023-11-08 20:24:44
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.ArticleMapper;
import com.taoge.biz.persistent.entity.Article;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ArticleService extends BaseService<Article, ArticleMapper> {

    public int publish(Long id) {
        return getMapper().publish(id);
    }

    public void offline(Long id) {
        getMapper().offline(id);
    }
}