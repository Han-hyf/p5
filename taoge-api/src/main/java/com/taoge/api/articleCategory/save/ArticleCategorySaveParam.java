package com.taoge.api.articleCategory.save;

import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleCategorySaveParam extends BaseParam {

    private Long id;
    private Long rootId;
    private Long parentId;
    private Integer level;
    private String name;
    private Integer sort;
    private Boolean status;

}
