package com.taoge.api.articleCategory.enable;

import com.taoge.api.articleCategory.save.ArticleCategorySaveParam;
import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.biz.server.param.article.SaveArticleCategoryParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.util.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleCategoryEnableController extends BaseController<IdParam> {

    @Resource
    ArticleServer articleServer;


    @Override
    @PostMapping("/api/articleCategory/enable")
    public ResponseData<?> execute(@RequestBody IdParam param) {

        articleServer.enableArticleCategory(param);
        return ResponseData.success();
    }
}
