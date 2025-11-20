/*
 * @ClassName UserAccountFlowMapper
 * @Description 
 * @version 1.0
 * @Date 2025-11-06 17:52:35
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserAccountFlow;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserAccountFlowMapper {
    @Delete({
        "delete from `user_account_flow`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_account_flow` (user_id, amount, ",
        "business_type, business_param, ",
        "`identity`, remark, ",
        "flow_time, create_time, ",
        "update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{amount,jdbcType=DECIMAL}, ",
        "#{businessType,jdbcType=VARCHAR}, #{businessParam,jdbcType=VARCHAR}, ",
        "#{identity,jdbcType=VARCHAR}, #{remark,jdbcType=VARCHAR}, ",
        "#{flowTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserAccountFlow record);

    int insertSelective(UserAccountFlow record);

    @Select({
        "select",
        "id, user_id, amount, business_type, business_param, `identity`, remark, flow_time, ",
        "create_time, update_time",
        "from `user_account_flow`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserAccountFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAccountFlow record);

    @Update({
        "update `user_account_flow`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "amount = #{amount,jdbcType=DECIMAL},",
          "business_type = #{businessType,jdbcType=VARCHAR},",
          "business_param = #{businessParam,jdbcType=VARCHAR},",
          "`identity` = #{identity,jdbcType=VARCHAR},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "flow_time = #{flowTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserAccountFlow record);

    List<UserAccountFlow> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserAccountFlow> selectByIds(List<?> list);
}