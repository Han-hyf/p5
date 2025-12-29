package com.taoge.api.articleCategory.disable;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleCategoryDisableController extends BaseController<IdParam> {
    @Resource
    ArticleServer articleServer;

    @Override
    @PostMapping("/api/articleCategory/disable")
    public ResponseData<?> execute(@RequestBody IdParam param) {
        articleServer.disableArticleCategory(param);
        return ResponseData.success();
    }
}
