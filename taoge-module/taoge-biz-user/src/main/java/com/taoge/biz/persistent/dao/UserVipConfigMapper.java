/*
 * @ClassName UserVipConfigMapper
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.UserVipConfig;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

public interface UserVipConfigMapper {
    @Delete({
        "delete from `user_vip_config`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `user_vip_config` (vip_name, vip_price, ",
        "vip_icon, vip_days, ",
        "vip_days_name, vip_level, ",
        "`status`, sort, create_time, ",
        "update_time)",
        "values (#{vipName,jdbcType=VARCHAR}, #{vipPrice,jdbcType=DECIMAL}, ",
        "#{vipIcon,jdbcType=VARCHAR}, #{vipDays,jdbcType=INTEGER}, ",
        "#{vipDaysName,jdbcType=VARCHAR}, #{vipLevel,jdbcType=INTEGER}, ",
        "#{status,jdbcType=BIT}, #{sort,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(UserVipConfig record);

    int insertSelective(UserVipConfig record);

    @Select({
        "select",
        "id, vip_name, vip_price, vip_icon, vip_days, vip_days_name, vip_level, `status`, ",
        "sort, create_time, update_time",
        "from `user_vip_config`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    UserVipConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserVipConfig record);

    @Update({
        "update `user_vip_config`",
        "set vip_name = #{vipName,jdbcType=VARCHAR},",
          "vip_price = #{vipPrice,jdbcType=DECIMAL},",
          "vip_icon = #{vipIcon,jdbcType=VARCHAR},",
          "vip_days = #{vipDays,jdbcType=INTEGER},",
          "vip_days_name = #{vipDaysName,jdbcType=VARCHAR},",
          "vip_level = #{vipLevel,jdbcType=INTEGER},",
          "`status` = #{status,jdbcType=BIT},",
          "sort = #{sort,jdbcType=INTEGER},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserVipConfig record);

    List<UserVipConfig> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<UserVipConfig> selectByIds(List<?> list);

    int sort(@Param("ids") List<Long> ids);
}