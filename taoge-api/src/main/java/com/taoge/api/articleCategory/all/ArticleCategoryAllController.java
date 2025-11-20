package com.taoge.api.articleCategory.all;

import com.taoge.biz.server.ArticleServer;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleCategoryAllController extends BaseController {

    @Resource
    ArticleServer articleServer;


    @Override
    @PostMapping("/api/articleCategory/all")
    public ResponseData<?> execute(BaseParam param) {
        return ResponseData.success("",articleServer.allArticleCategory());
    }
}
