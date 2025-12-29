/*
 * @ClassName UserAccountMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserAccount;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserAccountMapper {
    @Delete({
        "delete from `user_account`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_account` (total_recharge, total_consume, ",
        "balance, `freeze`, ",
        "total_earn_point, total_consume_point, ",
        "point_balance, point_freeze, ",
        "`status`, create_time, ",
        "update_time)",
        "values (#{totalRecharge,jdbcType=DECIMAL}, #{totalConsume,jdbcType=DECIMAL}, ",
        "#{balance,jdbcType=DECIMAL}, #{freeze,jdbcType=DECIMAL}, ",
        "#{totalEarnPoint,jdbcType=INTEGER}, #{totalConsumePoint,jdbcType=INTEGER}, ",
        "#{pointBalance,jdbcType=INTEGER}, #{pointFreeze,jdbcType=INTEGER}, ",
        "#{status,jdbcType=BIT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserAccount record);

    int insertSelective(UserAccount record);

    @Select({
        "select",
        "id, total_recharge, total_consume, balance, `freeze`, total_earn_point, total_consume_point, ",
        "point_balance, point_freeze, `status`, create_time, update_time",
        "from `user_account`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAccount record);

    @Update({
        "update `user_account`",
        "set total_recharge = #{totalRecharge,jdbcType=DECIMAL},",
          "total_consume = #{totalConsume,jdbcType=DECIMAL},",
          "balance = #{balance,jdbcType=DECIMAL},",
          "`freeze` = #{freeze,jdbcType=DECIMAL},",
          "total_earn_point = #{totalEarnPoint,jdbcType=INTEGER},",
          "total_consume_point = #{totalConsumePoint,jdbcType=INTEGER},",
          "point_balance = #{pointBalance,jdbcType=INTEGER},",
          "point_freeze = #{pointFreeze,jdbcType=INTEGER},",
          "`status` = #{status,jdbcType=BIT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserAccount record);

    List<UserAccount> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserAccount> selectByIds(List<?> list);
}