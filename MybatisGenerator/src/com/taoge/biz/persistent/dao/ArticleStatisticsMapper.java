/*
 * @ClassName ArticleStatisticsMapper
 * @Description 
 * @version 1.0
 * @Date 2025-11-13 11:08:48
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.ArticleStatistics;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ArticleStatisticsMapper {
    @Delete({
        "delete from `article_statistics`",
        "where article_id = #{articleId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long articleId);

    @Insert({
        "insert into `article_statistics` (article_id, browse_pv, ",
        "browse_uv, comment_pv, ",
        "comment_uv, favorite, ",
        "`like`, share_pv, share_uv, ",
        "create_time, update_time)",
        "values (#{articleId,jdbcType=BIGINT}, #{browsePv,jdbcType=BIGINT}, ",
        "#{browseUv,jdbcType=BIGINT}, #{commentPv,jdbcType=BIGINT}, ",
        "#{commentUv,jdbcType=BIGINT}, #{favorite,jdbcType=BIGINT}, ",
        "#{like,jdbcType=BIGINT}, #{sharePv,jdbcType=BIGINT}, #{shareUv,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(ArticleStatistics record);

    int insertSelective(ArticleStatistics record);

    @Select({
        "select",
        "article_id, browse_pv, browse_uv, comment_pv, comment_uv, favorite, `like`, ",
        "share_pv, share_uv, create_time, update_time",
        "from `article_statistics`",
        "where article_id = #{articleId,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    ArticleStatistics selectByPrimaryKey(Long articleId);

    int updateByPrimaryKeySelective(ArticleStatistics record);

    @Update({
        "update `article_statistics`",
        "set browse_pv = #{browsePv,jdbcType=BIGINT},",
          "browse_uv = #{browseUv,jdbcType=BIGINT},",
          "comment_pv = #{commentPv,jdbcType=BIGINT},",
          "comment_uv = #{commentUv,jdbcType=BIGINT},",
          "favorite = #{favorite,jdbcType=BIGINT},",
          "`like` = #{like,jdbcType=BIGINT},",
          "share_pv = #{sharePv,jdbcType=BIGINT},",
          "share_uv = #{shareUv,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where article_id = #{articleId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ArticleStatistics record);

    List<ArticleStatistics> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<ArticleStatistics> selectByIds(List<?> list);
}