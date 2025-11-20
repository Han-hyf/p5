/*
 * @ClassName OrderMapper
 * @Description 
 * @version 1.0
 * @Date 2023-10-29 18:10:50
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.Order;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface OrderMapper {
    @Delete({
        "delete from `order`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `order` (user_id, order_no, ",
        "total_price, goods_price, ",
        "pay_price, goods_count, ",
        "pay_time, pay_date, ",
        "pay_expired_time, order_status, ",
        "create_time, update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{orderNo,jdbcType=VARCHAR}, ",
        "#{totalPrice,jdbcType=DECIMAL}, #{goodsPrice,jdbcType=DECIMAL}, ",
        "#{payPrice,jdbcType=DECIMAL}, #{goodsCount,jdbcType=INTEGER}, ",
        "#{payTime,jdbcType=TIMESTAMP}, #{payDate,jdbcType=DATE}, ",
        "#{payExpiredTime,jdbcType=TIMESTAMP}, #{orderStatus,jdbcType=CHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(Order record);

    int insertSelective(Order record);

    @Select({
        "select",
        "id, user_id, order_no, total_price, goods_price, pay_price, goods_count, pay_time, ",
        "pay_date, pay_expired_time, order_status, create_time, update_time",
        "from `order`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    @Update({
        "update `order`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "order_no = #{orderNo,jdbcType=VARCHAR},",
          "total_price = #{totalPrice,jdbcType=DECIMAL},",
          "goods_price = #{goodsPrice,jdbcType=DECIMAL},",
          "pay_price = #{payPrice,jdbcType=DECIMAL},",
          "goods_count = #{goodsCount,jdbcType=INTEGER},",
          "pay_time = #{payTime,jdbcType=TIMESTAMP},",
          "pay_date = #{payDate,jdbcType=DATE},",
          "pay_expired_time = #{payExpiredTime,jdbcType=TIMESTAMP},",
          "order_status = #{orderStatus,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Order record);

    List<Order> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<Order> selectByIds(List<?> list);
}