package com.taoge.scheduled;

import com.taoge.biz.server.ArticleServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ArticleScheduledService {
    @Resource
    ArticleServer articleServer;

    @Scheduled(initialDelay = 2000,fixedDelay = 60 * 1000 )
    public void syncArticleBrowsePv(){
        log.info("定时同步文章pv开始..");
        articleServer.syncArticleBrowsePv();
        log.info("定时同步文章pv结束,,,");
    }

    @Scheduled(initialDelay = 2000,fixedDelay = 60 * 1000 )
    public void syncArticleBrowseUv(){
        log.info("定时同步文章uv开始..");
        articleServer.syncArticleBrowseUv();
        log.info("定时同步文章uv结束,,,");
    }


}
