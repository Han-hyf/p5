/*
 * @ClassName UserVipRecordMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserVipRecord;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserVipRecordMapper {
    @Delete({
        "delete from `user_vip_record`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_vip_record` (user_id, business_order_sn, ",
        "vip_name, vip_price, ",
        "vip_icon, vip_days, ",
        "vip_days_name, vip_level, ",
        "pay_amount, pay_date, ",
        "remark, create_time, ",
        "update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{businessOrderSn,jdbcType=VARCHAR}, ",
        "#{vipName,jdbcType=VARCHAR}, #{vipPrice,jdbcType=DECIMAL}, ",
        "#{vipIcon,jdbcType=VARCHAR}, #{vipDays,jdbcType=INTEGER}, ",
        "#{vipDaysName,jdbcType=VARCHAR}, #{vipLevel,jdbcType=INTEGER}, ",
        "#{payAmount,jdbcType=DECIMAL}, #{payDate,jdbcType=TIMESTAMP}, ",
        "#{remark,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserVipRecord record);

    int insertSelective(UserVipRecord record);

    @Select({
        "select",
        "id, user_id, business_order_sn, vip_name, vip_price, vip_icon, vip_days, vip_days_name, ",
        "vip_level, pay_amount, pay_date, remark, create_time, update_time",
        "from `user_vip_record`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserVipRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserVipRecord record);

    @Update({
        "update `user_vip_record`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "business_order_sn = #{businessOrderSn,jdbcType=VARCHAR},",
          "vip_name = #{vipName,jdbcType=VARCHAR},",
          "vip_price = #{vipPrice,jdbcType=DECIMAL},",
          "vip_icon = #{vipIcon,jdbcType=VARCHAR},",
          "vip_days = #{vipDays,jdbcType=INTEGER},",
          "vip_days_name = #{vipDaysName,jdbcType=VARCHAR},",
          "vip_level = #{vipLevel,jdbcType=INTEGER},",
          "pay_amount = #{payAmount,jdbcType=DECIMAL},",
          "pay_date = #{payDate,jdbcType=TIMESTAMP},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserVipRecord record);

    List<UserVipRecord> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserVipRecord> selectByIds(List<?> list);

    int paySuccess(@Param("businessOrderSn") String businessOrderSn);

    UserVipRecord getByBusinessOrderSn(@Param("businessOrderSn") String businessOrderSn);
}