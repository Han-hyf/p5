/*
 * @ClassName ArticleMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-08 20:24:44
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.Article;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.*;

public interface ArticleMapper {
    @Delete({
        "delete from `article`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `article` (category_1_id, category_2_id, ",
        "category_3_id, category_4_id, ",
        "cover, title, sub_title, ",
        "publish_time, author_id, ",
        "author_name, `status`, ",
        "create_time, update_time, ",
        "content)",
        "values (#{category1Id,jdbcType=BIGINT}, #{category2Id,jdbcType=BIGINT}, ",
        "#{category3Id,jdbcType=BIGINT}, #{category4Id,jdbcType=BIGINT}, ",
        "#{cover,jdbcType=VARCHAR}, #{title,jdbcType=VARCHAR}, #{subTitle,jdbcType=VARCHAR}, ",
        "#{publishTime,jdbcType=TIMESTAMP}, #{authorId,jdbcType=BIGINT}, ",
        "#{authorName,jdbcType=VARCHAR}, #{status,jdbcType=CHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ",
        "#{content,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Article record);

    int insertSelective(Article record);

    @Select({
        "select",
        "id, category_1_id, category_2_id, category_3_id, category_4_id, cover, title, ",
        "sub_title, publish_time, author_id, author_name, `status`, create_time, update_time, ",
        "content",
        "from `article`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("ResultMapWithBLOBs")
    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article record);

    @Update({
        "update `article`",
        "set category_1_id = #{category1Id,jdbcType=BIGINT},",
          "category_2_id = #{category2Id,jdbcType=BIGINT},",
          "category_3_id = #{category3Id,jdbcType=BIGINT},",
          "category_4_id = #{category4Id,jdbcType=BIGINT},",
          "cover = #{cover,jdbcType=VARCHAR},",
          "title = #{title,jdbcType=VARCHAR},",
          "sub_title = #{subTitle,jdbcType=VARCHAR},",
          "publish_time = #{publishTime,jdbcType=TIMESTAMP},",
          "author_id = #{authorId,jdbcType=BIGINT},",
          "author_name = #{authorName,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "content = #{content,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(Article record);

    @Update({
        "update `article`",
        "set category_1_id = #{category1Id,jdbcType=BIGINT},",
          "category_2_id = #{category2Id,jdbcType=BIGINT},",
          "category_3_id = #{category3Id,jdbcType=BIGINT},",
          "category_4_id = #{category4Id,jdbcType=BIGINT},",
          "cover = #{cover,jdbcType=VARCHAR},",
          "title = #{title,jdbcType=VARCHAR},",
          "sub_title = #{subTitle,jdbcType=VARCHAR},",
          "publish_time = #{publishTime,jdbcType=TIMESTAMP},",
          "author_id = #{authorId,jdbcType=BIGINT},",
          "author_name = #{authorName,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Article record);

    List<Article> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<Article> selectByIds(List<?> list);

    int publish(@Param("id") Long id);

    int offline(@Param("id") Long id);
}