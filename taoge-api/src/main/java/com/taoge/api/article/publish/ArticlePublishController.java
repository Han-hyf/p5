package com.taoge.api.article.publish;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticlePublishController extends BaseController<IdParam> {

    @Resource
    ArticleServer articleServer;

    @Override
    @PostMapping("/api/article/publish")
    public ResponseData<?> execute(IdParam param) {
        articleServer.publishArticle(param);
        return ResponseData.success();
    }
}
