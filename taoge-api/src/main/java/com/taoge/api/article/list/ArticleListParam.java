package com.taoge.api.article.list;

import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class ArticleListParam extends BaseParam {

    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;

}
