/*
 * @ClassName UserInfoMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-02 20:11:42
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface UserInfoMapper {
    @Delete({
        "delete from `user_info`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_info` (user_id, nickname, ",
        "gender, birthday, ",
        "birthday_day, interest, ",
        "avatar, create_time, ",
        "update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{nickname,jdbcType=VARCHAR}, ",
        "#{gender,jdbcType=BIT}, #{birthday,jdbcType=TIMESTAMP}, ",
        "#{birthdayDay,jdbcType=VARCHAR}, #{interest,jdbcType=VARCHAR}, ",
        "#{avatar,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    @Select({
        "select",
        "id, user_id, nickname, gender, birthday, birthday_day, interest, avatar, create_time, ",
        "update_time",
        "from `user_info`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    @Update({
        "update `user_info`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "nickname = #{nickname,jdbcType=VARCHAR},",
          "gender = #{gender,jdbcType=BIT},",
          "birthday = #{birthday,jdbcType=TIMESTAMP},",
          "birthday_day = #{birthdayDay,jdbcType=VARCHAR},",
          "interest = #{interest,jdbcType=VARCHAR},",
          "avatar = #{avatar,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserInfo record);

    List<UserInfo> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserInfo> selectByIds(List<?> list);

    /**
     * 更新用户信息
     */
    int updateUserInfo(@Param("userId") Long userId, @Param("nickname") String nickname, @Param("gender") Integer gender, @Param("birthday") Date birthday);
}