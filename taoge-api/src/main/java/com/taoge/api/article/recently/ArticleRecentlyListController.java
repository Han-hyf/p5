package com.taoge.api.article.recently;

import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.PageParam;
import com.taoge.framework.util.UserContext;
import com.taoge.service.ArticleCacheService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleRecentlyListController extends BaseController<PageParam> {
    @Resource
    ArticleCacheService articleCacheService;

    @Guest
    @Override
    @PostMapping("/api/article/recentlyList")
    public ResponseData<?> execute(@RequestBody PageParam param) {
        UserInfo userInfo = UserContext.get();
        return ResponseData.success("", articleCacheService.recentlyList(userInfo.getUserId(), param.getPageNum(), param.getPageSize()));
    }
}
