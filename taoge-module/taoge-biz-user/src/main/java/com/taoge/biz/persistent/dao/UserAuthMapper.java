/*
 * @ClassName UserAuthMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-02 20:11:42
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserAuth;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserAuthMapper {
    @Delete({
        "delete from `user_auth`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_auth` (id, user_id, ",
        "id_card_front, id_card_back, ",
        "id_card_no, real_name, ",
        "id_card_front_status, id_card_back_status, ",
        "auth_status, reason, ",
        "auth_time, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, ",
        "#{idCardFront,jdbcType=VARCHAR}, #{idCardBack,jdbcType=VARCHAR}, ",
        "#{idCardNo,jdbcType=VARCHAR}, #{realName,jdbcType=VARCHAR}, ",
        "#{idCardFrontStatus,jdbcType=CHAR}, #{idCardBackStatus,jdbcType=CHAR}, ",
        "#{authStatus,jdbcType=CHAR}, #{reason,jdbcType=VARCHAR}, ",
        "#{authTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    @Select({
        "select",
        "id, user_id, id_card_front, id_card_back, id_card_no, real_name, id_card_front_status, ",
        "id_card_back_status, auth_status, reason, auth_time, create_time, update_time",
        "from `user_auth`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAuth record);

    @Update({
        "update `user_auth`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "id_card_front = #{idCardFront,jdbcType=VARCHAR},",
          "id_card_back = #{idCardBack,jdbcType=VARCHAR},",
          "id_card_no = #{idCardNo,jdbcType=VARCHAR},",
          "real_name = #{realName,jdbcType=VARCHAR},",
          "id_card_front_status = #{idCardFrontStatus,jdbcType=CHAR},",
          "id_card_back_status = #{idCardBackStatus,jdbcType=CHAR},",
          "auth_status = #{authStatus,jdbcType=CHAR},",
          "reason = #{reason,jdbcType=VARCHAR},",
          "auth_time = #{authTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserAuth record);

    List<UserAuth> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserAuth> selectByIds(List<?> list);
}