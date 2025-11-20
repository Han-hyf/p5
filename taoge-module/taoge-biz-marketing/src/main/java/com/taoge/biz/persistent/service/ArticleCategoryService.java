/*
 * @ClassName ArticleCategoryService
 * @Description 
 * @version 1.0
 * @Date 2025-11-13 11:08:48
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.ArticleCategoryMapper;
import com.taoge.biz.persistent.entity.ArticleCategory;
import com.taoge.framework.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ArticleCategoryService extends BaseService<ArticleCategory ,ArticleCategoryMapper> {

    public void updateStatus(Long id ,Boolean status){
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setId(id);
        articleCategory.setStatus(status);
        updateByPrimaryKeySelective(articleCategory);
    }
}