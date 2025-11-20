package com.taoge.biz.server.param.article;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.annotation.PrimaryKey;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.util.Date;

@Data
public class SaveArticleCategoryParam extends BaseParam {


    private Long id;
    private Long rootId;
    private Long parentId;
    @NotNull
    private Integer level;
    @NotNull
    private String name;
    private Integer sort;
    private Boolean status;
    private Long createBy;

}
