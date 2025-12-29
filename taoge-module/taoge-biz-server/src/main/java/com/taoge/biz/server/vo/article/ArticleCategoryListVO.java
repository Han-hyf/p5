package com.taoge.biz.server.vo.article;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

@Data
public class ArticleCategoryListVO extends BaseVO {
    private Long id;
    private Long rootId;
    private Long parentId;
    private Integer level;
    private String name;
}
