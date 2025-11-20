package com.taoge.api.articleCategory.save;

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
public class ArticleCategorySaveController extends BaseController<ArticleCategorySaveParam> {

    @Resource
    ArticleServer articleServer;


    @Override
    @PostMapping("/api/articleCategory/save")
    public ResponseData<?> execute(@RequestBody ArticleCategorySaveParam param) {
        UserInfo userInfo = UserContext.get();
        SaveArticleCategoryParam saveArticleCategoryParam = param.convertTo(SaveArticleCategoryParam.class);
        saveArticleCategoryParam.setCreateBy(userInfo.getUserId());
        articleServer.saveArticleCategory(saveArticleCategoryParam);
        return ResponseData.success();
    }
}
