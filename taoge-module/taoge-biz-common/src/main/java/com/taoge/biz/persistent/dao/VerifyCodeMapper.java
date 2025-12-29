/*
 * @ClassName VerifyCodeMapper
 * @Description 
 * @version 1.0
 * @Date 2023-10-31 21:36:54
 */
package com.taoge.biz.persistent.dao;

import com.taoge.biz.persistent.entity.VerifyCode;
import org.apache.ibatis.annotations.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

public interface VerifyCodeMapper {
    @Delete({
        "delete from `verify_code`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into `verify_code` (user_id, mobile, ",
        "code, action_type, ",
        "`status`, fail_count, ",
        "expire_time)",
        "values (#{userId,jdbcType=BIGINT}, #{mobile,jdbcType=VARCHAR}, ",
        "#{code,jdbcType=VARCHAR}, #{actionType,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=CHAR}, #{failCount,jdbcType=INTEGER}, ",
        "#{expireTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(VerifyCode record);

    int insertSelective(VerifyCode record);

    @Select({
        "select",
        "id, user_id, mobile, code, action_type, `status`, fail_count, expire_time",
        "from `verify_code`",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    VerifyCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VerifyCode record);

    @Update({
        "update `verify_code`",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "mobile = #{mobile,jdbcType=VARCHAR},",
          "code = #{code,jdbcType=VARCHAR},",
          "action_type = #{actionType,jdbcType=VARCHAR},",
          "`status` = #{status,jdbcType=CHAR},",
          "fail_count = #{failCount,jdbcType=INTEGER},",
          "expire_time = #{expireTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(VerifyCode record);

    List<VerifyCode> list(HashMap<String, ?> map);

    Long count(HashMap<String, ?> map);

    List<VerifyCode> selectByIds(List<?> list);

    /**
     * 查询待验证code
     * 用户id、手机号、业务类型
     */
    VerifyCode selectValidCode(@Param("userId") Long userId, @Param("mobile") String mobile,
                               @Param("actionType") String actionType, @Param("statusList") List<String> statusList);

    /**
     * 验证失败
     */
    int validateFail(@Param("id") Long id, @Param("status") String status);
}