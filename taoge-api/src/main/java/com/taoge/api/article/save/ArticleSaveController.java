package com.taoge.api.article.save;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.biz.server.param.article.SaveArticleParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleSaveController extends BaseController<ArticleSaveParam> {

    @Resource
    ArticleServer articleServer;

    @Override
    @PostMapping("/api/article/save")
    public ResponseData<?> execute(@RequestBody ArticleSaveParam param) {
        SaveArticleParam saveArticleParam = param.convertTo(SaveArticleParam.class);
        articleServer.saveArticle(saveArticleParam);
        return ResponseData.success();
    }
}
