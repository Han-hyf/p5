package com.taoge.biz.server.vo.article;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleListVO extends BaseVO {
    private Long id;
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;
    private String cover;
    private String title;
    private String subTitle;
    private Date publishTime;
}
