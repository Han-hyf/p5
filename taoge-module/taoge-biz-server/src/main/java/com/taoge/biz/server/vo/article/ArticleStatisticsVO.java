package com.taoge.biz.server.vo.article;

import com.taoge.framework.annotation.PrimaryKey;
import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.util.Date;


@Data
public class ArticleStatisticsVO extends BaseVO {

    private Long articleId;
    private Long browsePv;
    private Long browseUv;
    private Long commentPv;
    private Long commentUv;
    private Long favorite;
    private Long like;
    private Long sharePv;
    private Long shareUv;

}
