/*
 * @ClassName UserBaseMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-02 20:11:42
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserBase;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserBaseMapper {
    @Delete({
        "delete from `user_base`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_base` (id, username, ",
        "`password`, salt, mobile, ",
        "wx_open_id, wx_union_id, ",
        "`status`, auth_status, auth_type, ",
        "register_time, last_login_time, ",
        "create_time, update_time)",
        "values (#{id,jdbcType=BIGINT}, #{username,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{salt,jdbcType=CHAR}, #{mobile,jdbcType=VARCHAR}, ",
        "#{wxOpenId,jdbcType=VARCHAR}, #{wxUnionId,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=CHAR}, #{authStatus,jdbcType=CHAR}, #{authType,jdbcType=CHAR}, ",
        "#{registerTime,jdbcType=TIMESTAMP}, #{lastLoginTime,jdbcType=TIMESTAMP}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(UserBase record);

    int insertSelective(UserBase record);

    @Select({
        "select",
        "id, username, `password`, salt, mobile, wx_open_id, wx_union_id, `status`, auth_status, ",
        "auth_type, register_time, last_login_time, create_time, update_time",
        "from `user_base`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserBase selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBase record);

    @Update({
        "update `user_base`",
        "set username = #{username,jdbcType=VARCHAR},",
          "`password` = #{password,jdbcType=VARCHAR},",
          "salt = #{salt,jdbcType=CHAR},",
          "mobile = #{mobile,jdbcType=VARCHAR},",
          "wx_open_id = #{wxOpenId,jdbcType=VARCHAR},",
          "wx_union_id = #{wxUnionId,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=CHAR},",
          "auth_status = #{authStatus,jdbcType=CHAR},",
          "auth_type = #{authType,jdbcType=CHAR},",
          "register_time = #{registerTime,jdbcType=TIMESTAMP},",
          "last_login_time = #{lastLoginTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserBase record);

    List<UserBase> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserBase> selectByIds(List<?> list);

    /**
     * 按手机号查找用户
     */
    UserBase getByMobile(@Param("mobile") String mobile);

    /**
     * 按用户名查找用户
     */
    UserBase getByUsername(@Param("username") String username);
}