/*
 * @ClassName ArticleCategory
 * @Description 
 * @version 1.0
 * @Date 2025-11-13 11:08:48
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import java.util.Date;
import lombok.Data;

@Data
public class ArticleCategory {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long rootId;
    private Long parentId;
    private Integer level;
    private String name;
    private Integer sort;
    private Boolean status;
    private Long createBy;
    private Date createTime;
    private Date updateTime;
}