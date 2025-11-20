/*
 * @ClassName BusinessOrderMapper
 * @Description 
 * @version 1.0
 * @Date 2025-11-10 14:03:16
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.BusinessOrder;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface BusinessOrderMapper {
    @Delete({
        "delete from `business_order`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `business_order` (business_order_sn, user_id, ",
        "sign_key, total_money, ",
        "pay_money, pay_type, ",
        "business_type, business_param, ",
        "`status`, pay_time, ",
        "pay_expire_time, create_time, ",
        "update_time)",
        "values (#{businessOrderSn,jdbcType=VARCHAR}, #{userId,jdbcType=BIGINT}, ",
        "#{signKey,jdbcType=VARCHAR}, #{totalMoney,jdbcType=DECIMAL}, ",
        "#{payMoney,jdbcType=DECIMAL}, #{payType,jdbcType=VARCHAR}, ",
        "#{businessType,jdbcType=VARCHAR}, #{businessParam,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=CHAR}, #{payTime,jdbcType=TIMESTAMP}, ",
        "#{payExpireTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(BusinessOrder record);

    int insertSelective(BusinessOrder record);

    @Select({
        "select",
        "id, business_order_sn, user_id, sign_key, total_money, pay_money, pay_type, ",
        "business_type, business_param, `status`, pay_time, pay_expire_time, create_time, ",
        "update_time",
        "from `business_order`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    BusinessOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessOrder record);

    @Update({
        "update `business_order`",
        "set business_order_sn = #{businessOrderSn,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=BIGINT},",
          "sign_key = #{signKey,jdbcType=VARCHAR},",
          "total_money = #{totalMoney,jdbcType=DECIMAL},",
          "pay_money = #{payMoney,jdbcType=DECIMAL},",
          "pay_type = #{payType,jdbcType=VARCHAR},",
          "business_type = #{businessType,jdbcType=VARCHAR},",
          "business_param = #{businessParam,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=CHAR},",
          "pay_time = #{payTime,jdbcType=TIMESTAMP},",
          "pay_expire_time = #{payExpireTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(BusinessOrder record);

    List<BusinessOrder> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<BusinessOrder> selectByIds(List<?> list);

    BusinessOrder getInitOrderBySignKey(@Param("signKey")String signKey);

    BusinessOrder getInitOrderByBusinessOrderSn(@Param("businessSn")String businessSn);

    int paySuccess(@Param("businessOrderSn") String businessOrderSn);
}