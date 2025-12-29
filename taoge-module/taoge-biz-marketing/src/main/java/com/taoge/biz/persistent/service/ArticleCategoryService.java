/*
 * @ClassName ArticleCategoryService
 * @Description 
 * @version 1.0
 * @Date 2023-11-08 20:24:44
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.ArticleCategoryMapper;
import com.taoge.biz.persistent.entity.ArticleCategory;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ArticleCategoryService extends BaseService<ArticleCategory ,ArticleCategoryMapper> {

    public void updateStatus(Long id, Boolean status) {
        ArticleCategory update = new ArticleCategory();
        update.setId(id);
        update.setStatus(status);
        updateByPrimaryKeySelective(update);
    }
}