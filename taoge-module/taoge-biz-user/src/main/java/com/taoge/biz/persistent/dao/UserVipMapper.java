/*
 * @ClassName UserVipMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserVip;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserVipMapper {
    @Delete({
        "delete from `user_vip`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_vip` (user_id, `level`, ",
        "level_name, `type`, ",
        "expire_time, create_time, ",
        "update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{level,jdbcType=INTEGER}, ",
        "#{levelName,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, ",
        "#{expireTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserVip record);

    int insertSelective(UserVip record);

    @Select({
        "select",
        "id, user_id, `level`, level_name, `type`, expire_time, create_time, update_time",
        "from `user_vip`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserVip selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserVip record);

    @Update({
        "update `user_vip`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "`level` = #{level,jdbcType=INTEGER},",
          "level_name = #{levelName,jdbcType=VARCHAR},",
          "`type` = #{type,jdbcType=VARCHAR},",
          "expire_time = #{expireTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserVip record);

    List<UserVip> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserVip> selectByIds(List<?> list);

    UserVip getByUserId(@Param("userId") Long userId);
}