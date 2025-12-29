/*
 * @ClassName SmsRecordMapper
 * @Description 
 * @version 1.0
 * @Date 2023-10-31 20:28:12
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.SmsRecord;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.*;

public interface SmsRecordMapper {
    @Delete({
        "delete from `sms_record`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `sms_record` (user_id, mobile, ",
        "mobile_prefix, iso, ",
        "original_mobile, action_type, ",
        "content, template_code, ",
        "template_param, ip, ",
        "send_status, send_message, ",
        "send_time, send_day, ",
        "create_time, update_time)",
        "values (#{userId,jdbcType=BIGINT}, #{mobile,jdbcType=VARCHAR}, ",
        "#{mobilePrefix,jdbcType=VARCHAR}, #{iso,jdbcType=VARCHAR}, ",
        "#{originalMobile,jdbcType=VARCHAR}, #{actionType,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR}, #{templateCode,jdbcType=VARCHAR}, ",
        "#{templateParam,jdbcType=VARCHAR}, #{ip,jdbcType=VARCHAR}, ",
        "#{sendStatus,jdbcType=BIT}, #{sendMessage,jdbcType=VARCHAR}, ",
        "#{sendTime,jdbcType=TIMESTAMP}, #{sendDay,jdbcType=DATE}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(SmsRecord record);

    int insertSelective(SmsRecord record);

    @Select({
        "select",
        "id, user_id, mobile, mobile_prefix, iso, original_mobile, action_type, content, ",
        "template_code, template_param, ip, send_status, send_message, send_time, send_day, ",
        "create_time, update_time",
        "from `sms_record`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    SmsRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsRecord record);

    @Update({
        "update `sms_record`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "mobile = #{mobile,jdbcType=VARCHAR},",
          "mobile_prefix = #{mobilePrefix,jdbcType=VARCHAR},",
          "iso = #{iso,jdbcType=VARCHAR},",
          "original_mobile = #{originalMobile,jdbcType=VARCHAR},",
          "action_type = #{actionType,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "template_code = #{templateCode,jdbcType=VARCHAR},",
          "template_param = #{templateParam,jdbcType=VARCHAR},",
          "ip = #{ip,jdbcType=VARCHAR},",
          "send_status = #{sendStatus,jdbcType=BIT},",
          "send_message = #{sendMessage,jdbcType=VARCHAR},",
          "send_time = #{sendTime,jdbcType=TIMESTAMP},",
          "send_day = #{sendDay,jdbcType=DATE},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(SmsRecord record);

    List<SmsRecord> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<SmsRecord> selectByIds(List<?> list);

    /**
     * 统计用户、手机号、当天发送次数
     */
    Long countByMobileInDay(@Param("userId") Long userId, @Param("mobile") String mobile, @Param("actionType") String actionType);
}