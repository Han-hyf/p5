/*
 * @ClassName Article
 * @Description 
 * @version 1.0
 * @Date 2025-11-13 11:08:48
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import java.util.Date;
import lombok.Data;

@Data
public class Article {
    @PrimaryKey(autoIncrement = true)
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
    private Date createTime;
    private Date updateTime;
    private String content;
}