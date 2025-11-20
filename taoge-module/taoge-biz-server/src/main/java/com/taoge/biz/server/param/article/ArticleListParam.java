package com.taoge.biz.server.param.article;


import com.taoge.framework.controller.PageParam;
import lombok.Data;


@Data
public class ArticleListParam extends PageParam {


    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;
    private Long authorId;
    private String status;


}
