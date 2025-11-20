package com.taoge.api.article.save;

import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.util.Date;


@Data
public class ArticleSaveParam extends BaseParam {

    private Long id;
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;
    private String cover;
    private String title;
    private String subTitle;
    private Date publishTime;
    private Long authorId;
    private String authorName;
    private String status;
    private String content;

}
