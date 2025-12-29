/*
 * @ClassName ArticleStatistics
 * @Description 
 * @version 1.0
 * @Date 2023-11-08 20:24:44
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import java.util.Date;
import lombok.Data;

@Data
public class ArticleStatistics {
    @PrimaryKey
    private Long articleId;
    private Long browsePv;
    private Long browseUv;
    private Long commentPv;
    private Long commentUv;
    private Long favorite;
    private Long like;
    private Long sharePv;
    private Long shareUv;
    private Date createTime;
    private Date updateTime;
}