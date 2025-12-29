package com.taoge.biz.server.param.article;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.annotation.Size;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class SaveArticleParam extends BaseParam {
    private Long id;
    @NotNull
    private Long category1Id;
    @NotNull
    private Long category2Id;
    @NotNull
    private Long category3Id;
    @NotNull
    private Long category4Id;
    @NotNull
    @Size(max = 255)
    private String cover;
    @NotNull
    @Size(min = 2, max = 255)
    private String title;
    @Size(min = 2, max = 255)
    private String subTitle;
    private Long authorId;
    private String authorName;
    private String status;
    @NotNull
    private String content;
}
