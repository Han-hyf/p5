package com.taoge.api.article.detail;

import com.taoge.biz.server.vo.article.ArticleStatisticsVO;
import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleDetailVO extends BaseVO {

    private Long id;
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;
    private String cover;
    private String title;
    private String subTitle;
    private Date publishTime;
    private String authorName;
    private String content;

    private ArticleStatisticsVO articleStatisticsVO;
}
