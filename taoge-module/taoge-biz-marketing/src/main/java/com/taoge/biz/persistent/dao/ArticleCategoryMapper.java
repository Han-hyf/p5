/*
 * @ClassName ArticleCategoryMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-08 20:24:44
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.ArticleCategory;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface ArticleCategoryMapper {
    @Delete({
        "delete from `article_category`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `article_category` (root_id, parent_id, ",
        "`level`, `name`, sort, ",
        "`status`, create_by, create_time, ",
        "update_time)",
        "values (#{rootId,jdbcType=BIGINT}, #{parentId,jdbcType=BIGINT}, ",
        "#{level,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{sort,jdbcType=INTEGER}, ",
        "#{status,jdbcType=BIT}, #{createBy,jdbcType=BIGINT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ArticleCategory record);

    int insertSelective(ArticleCategory record);

    @Select({
        "select",
        "id, root_id, parent_id, `level`, `name`, sort, `status`, create_by, create_time, ",
        "update_time",
        "from `article_category`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    ArticleCategory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleCategory record);

    @Update({
        "update `article_category`",
        "set root_id = #{rootId,jdbcType=BIGINT},",
          "parent_id = #{parentId,jdbcType=BIGINT},",
          "`level` = #{level,jdbcType=INTEGER},",
          "`name` = #{name,jdbcType=VARCHAR},",
          "sort = #{sort,jdbcType=INTEGER},",
          "`status` = #{status,jdbcType=BIT},",
          "create_by = #{createBy,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ArticleCategory record);

    List<ArticleCategory> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<ArticleCategory> selectByIds(List<?> list);
}