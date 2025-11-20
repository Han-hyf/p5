/*
 * @ClassName ArticleService
 * @Description 
 * @version 1.0
 * @Date 2025-11-13 11:08:48
 */
package com.taoge.biz.persistent.service;

import com.taoge.biz.persistent.dao.ArticleMapper;
import com.taoge.biz.persistent.entity.Article;
import com.taoge.framework.service.BaseService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public class ArticleService extends BaseService<Article ,ArticleMapper> {



    public int publish(Long id){
        return getMapper().publish(id);
    }

    public int offline(Long id){
        return getMapper().offline(id);
    }

}