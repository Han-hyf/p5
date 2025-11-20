package com.taoge.api.article.list;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.ArticleServer;
import com.taoge.biz.server.param.article.ArticleListParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleListController extends BaseController<com.taoge.api.article.list.ArticleListParam> {

    @Resource
    ArticleServer articleServer;

    @Override
    @PostMapping("/api/article/list")
    public ResponseData<?> execute(@RequestBody com.taoge.api.article.list.ArticleListParam param) {
        ArticleListParam articleListParam = param.convertTo(ArticleListParam.class);
        return ResponseData.success("",articleServer.articleList(articleListParam));
    }
}
