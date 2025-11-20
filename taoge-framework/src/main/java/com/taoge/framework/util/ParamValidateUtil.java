package com.taoge.framework.util;

import com.taoge.framework.annotation.IdCard;
import com.taoge.framework.annotation.Number;
import com.taoge.framework.annotation.*;
import com.taoge.framework.common.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验工具
 * Created by xuejingtao
 */
@Slf4j
public class ParamValidateUtil {
    private static final String primaryIdRegex;
    private static final String mobileRegex;
    private static final String emailRegex;
    private static final String userNameRegex;
    private static final String passwordRegex;

    static {
        primaryIdRegex = "^[1-9]\\d{13,19}$";
        mobileRegex = "^1[0-9]\\d{9}$";
        emailRegex = "^[a-zA-Z0-9_\\-]+@[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)+$";
        userNameRegex = "[A-Za-z]{1}[A-Za-z0-9]*";
        passwordRegex = "^[^\\s\\u4e00-\\u9fa5]*";
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 验证参数,并返回错误消息,用isSuccess()方法判断验证是否成功
     *
     * @param o 参数
     * @return
     */
    public static ResponseData<?> validateParam(Object o) {
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            if (fields.length == 0) {
                return ResponseData.success();
            }
            for (Field field : fields) {
                Method getMethod = o.getClass().getDeclaredMethod("get" + StringUtils.capitalize(field.getName()));
                Object value = getMethod.invoke(o);
                ResponseData<?> responseData = validateParam(field, value);
                if (!responseData.isSuccess()) {
                    return responseData;
                }
            }
        } catch (Exception e) {
            log.error("请求参数验证失败, e:", e);
            return ResponseData.paramValidateFail("请求参数验证失败");
        }
        return ResponseData.success();
    }

    public static ResponseData<?> validateParam(Field field, Object value) throws Exception {
        if (field.isAnnotationPresent(NotNull.class)) {
            NotNull notNull = field.getAnnotation(NotNull.class);
            if (value == null || (notNull.notEmpty() && StringUtils.isEmpty(value.toString()))) {
                return ResponseData.paramValidateFail(getMessage(notNull.errorMsg(), "请填写 " + getFieldName(notNull.name(), getFieldName(notNull.name(), field.getName()))));
            }
        }

        if (field.isAnnotationPresent(ID.class)) {
            ID id = field.getAnnotation(ID.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validatePrimaryId(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(id.errorMsg(), getFieldName(id.name(), field.getName()) + " 不符合要求"));
                }
            }
        }

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (null != value && StringUtils.isNotEmpty(value.toString()) && (value.toString().length() < size.min() || value.toString().length() > size.max())) {
                return ResponseData.paramValidateFail(getMessage(size.errorMsg(), getFieldName(size.name(), getFieldName(size.name(), field.getName())) + " 长度应在" + size.min() + "~" + size.max() + "之间"));
            }
        }

        if (field.isAnnotationPresent(Digits.class)) {
            Digits digits = field.getAnnotation(Digits.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateNumber(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(digits.errorMsg(), getFieldName(digits.name(), field.getName()) + " 必须为数字"));
                }

                if (!digits.negative()) {
                    try {
                        if (Double.parseDouble(value.toString()) < 0) {
                            return ResponseData.paramValidateFail(getMessage(digits.errorMsg(), getFieldName(digits.name(), field.getName()) + " 必须为正整数"));
                        }
                    } catch (NumberFormatException e) {
                        return ResponseData.paramValidateFail(getMessage(digits.errorMsg(), getFieldName(digits.name(), field.getName()) + " 必须为数字"));
                    }
                }

                String[] values = value.toString().split("\\.");
                boolean flag = false;
                if (values.length == 2) {
                    flag = values[0].length() <= digits.integer() && values[1].length() <= digits.fraction();
                } else if (values.length == 1) {
                    flag = values[0].length() <= digits.integer();
                }
                if (!flag) {
                    return ResponseData.paramValidateFail(getMessage(digits.errorMsg(), getFieldName(digits.name(), field.getName()) + " 小数位最多" + digits.fraction() + "位,整数位最多" + digits.integer() + "位"));
                }
            }
        }

        if (field.isAnnotationPresent(Min.class)) {
            Min min = field.getAnnotation(Min.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                try {
                    Double v = Double.parseDouble(value.toString());
                    if (v < min.value()) {
                        return ResponseData.paramValidateFail(getMessage(min.errorMsg(), getFieldName(min.name(), field.getName()) + " 不能小于" + min.value()));
                    }
                } catch (NumberFormatException e) {
                    return ResponseData.paramValidateFail(getMessage(min.errorMsg(), getFieldName(min.name(), field.getName()) + " 不能小于" + min.value()));
                }
            }
        }

        if (field.isAnnotationPresent(Max.class)) {
            Max max = field.getAnnotation(Max.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                try {
                    double v = Double.parseDouble(value.toString());
                    if (v > max.value()) {
                        return ResponseData.paramValidateFail(getMessage(max.errorMsg(), getFieldName(max.name(), field.getName()) + " 不能超过" + max.value()));
                    }
                } catch (NumberFormatException e) {
                    return ResponseData.paramValidateFail(getMessage(max.errorMsg(), getFieldName(max.name(), field.getName()) + " 不能超过" + max.value()));
                }
            }
        }

        if (field.isAnnotationPresent(Email.class)) {
            Email email = field.getAnnotation(Email.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateEmail(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(email.errorMsg(), getFieldName(email.name(), field.getName()) + " 不符合邮箱规则"));
                }
            }
        }

        if (field.isAnnotationPresent(Mobile.class)) {
            Mobile mobile = field.getAnnotation(Mobile.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateMobile(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(mobile.errorMsg(), getFieldName(mobile.name(), field.getName()) + " 不符合手机号码规则"));
                }
            }
        }

        if (field.isAnnotationPresent(com.taoge.framework.annotation.Number.class)) {
            com.taoge.framework.annotation.Number number = field.getAnnotation(Number.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (number.isInteger()) {
                    if (!validateInteger(value.toString())) {
                        return ResponseData.paramValidateFail(getMessage(number.errorMsg(), getFieldName(number.name(), field.getName()) + " 必须为整数"));
                    }
                } else {
                    if (!validateNumber(value.toString())) {
                        return ResponseData.paramValidateFail(getMessage(number.errorMsg(), getFieldName(number.name(), field.getName()) + " 必须为数字"));
                    }
                }
            }
        }

        if (field.isAnnotationPresent(Date.class)) {
            Date date = field.getAnnotation(Date.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateDate(value.toString(), date.format())) {
                    return ResponseData.paramValidateFail(getMessage(date.errorMsg(), getFieldName(date.name(), field.getName()) + " 格式不正确"));
                }
            }
        }

        if (field.isAnnotationPresent(UserName.class)) {
            UserName userName = field.getAnnotation(UserName.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateUserName(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(userName.errorMsg(), getFieldName(userName.name(), field.getName()) + " 必须以英文开头，只能包含字母和数字"));
                }
            }
        }

        if (field.isAnnotationPresent(Password.class)) {
            Password password = field.getAnnotation(Password.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validatePassword(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(password.errorMsg(), getFieldName(password.name(), field.getName()) + "包含数字、字母、字符至少两种,不能包含中文和空格"));
                }
            }
        }

        if (field.isAnnotationPresent(Range.class)) {
            Range range = field.getAnnotation(Range.class);
            Class<?> clazz = range.clazz();
            String key = range.key();
            Method getName = null;
            if (StringUtils.isNotEmpty(key)) {
                String names = "get" + StringUtils.capitalize(key);
                getName = clazz.getDeclaredMethod(names);
            }
            Object[] objects = clazz.getEnumConstants();
            boolean type = false;
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                for (Object enumClass : objects) {
                    if (null != getName) {
                        Object invokeValue = getName.invoke(enumClass);
                        if (invokeValue.toString().equals(value.toString())) {
                            type = true;
                            break;
                        }
                    } else {
                        if (enumClass.toString().equals(value.toString())) {
                            type = true;
                            break;
                        }
                    }
                }
            } else {
                type = true;
            }
            if (!type) {
                return ResponseData.paramValidateFail(getMessage(range.errorMsg(), getFieldName(range.clazz().getName(), field.getName()) + "未查到此状态"));
            }
        }

        if (field.isAnnotationPresent(com.taoge.framework.annotation.IdCard.class)) {
            com.taoge.framework.annotation.IdCard idCard = field.getAnnotation(IdCard.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateIdCard(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(idCard.errorMsg(), getFieldName(idCard.name(), field.getName()) + " 格式不正确"));
                }
            }
        }

        if (field.isAnnotationPresent(BankCode.class)) {
            BankCode idCard = field.getAnnotation(BankCode.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateBankCode(value.toString())) {
                    return ResponseData.paramValidateFail(getMessage(idCard.errorMsg(), getFieldName(idCard.name(), field.getName()) + " 格式不正确"));
                }
            }
        }

        if (field.isAnnotationPresent(RegExp.class)) {
            RegExp regExp = field.getAnnotation(RegExp.class);
            if (null != value && StringUtils.isNotEmpty(value.toString())) {
                if (!validateRegExp(value.toString(), regExp.pattern())) {
                    return ResponseData.paramValidateFail(getMessage(regExp.errorMsg(), getFieldName(regExp.name(), field.getName()) + " 格式不正确"));
                }
            }
        }

        return ResponseData.success();
    }

    /**
     * 验证正则表达式
     *
     * @param value   要验证的值
     * @param pattern 正则表达式
     * @return
     */
    public static boolean validateRegExp(String value, String pattern) {
        if (StringUtils.isNotEmpty(pattern)) {
            try {
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(value);
                return m.matches();
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 验证是否为数字
     *
     * @param number 参数
     * @return
     */
    public static boolean validateNumber(String number) {
        return NumberUtils.isParsable(number);
    }

    /**
     * 验证是否为整数
     *
     * @param number 参数
     * @return
     */
    public static boolean validateInteger(String number) {
        return NumberUtils.isDigits(number);
    }

    /**
     * 验证邮箱
     *
     * @param email 参数
     * @return
     */
    public static boolean validateEmail(String email) {
        return null != email && email.matches(emailRegex);
    }

    /**
     * 验证时间
     *
     * @param date   时间值
     * @param format 格式化，不传默认yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean validateDate(String date, String format) {
        if (StringUtils.isNotEmpty(format)) {
            try {
                return DateFormatUtils.format(new SimpleDateFormat(format).parse(date), format).equals(date);
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                dateFormat.parse(date);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证主键id
     *
     * @param primaryId 参数
     * @return
     */
    public static boolean validatePrimaryId(String primaryId) {
        return null != primaryId && primaryId.matches(primaryIdRegex);
    }


    /**
     * 验证手机号
     *
     * @param mobile 参数
     * @return
     */
    public static boolean validateMobile(String mobile) {
        return null != mobile && mobile.matches(mobileRegex);
    }

    /**
     * 验证用户名
     *
     * @param userName 参数
     * @return
     */
    public static boolean validateUserName(String userName) {
        return null != userName && userName.matches(userNameRegex);
    }

    /**
     * 验证密码
     *
     * @param password 参数
     * @return
     */
    public static boolean validatePassword(String password) {
        return null != password && password.matches(passwordRegex);
    }

    private static String getMessage(String annotationMessage, String defaultMessage) {
        return StringUtils.isNotEmpty(annotationMessage) ? annotationMessage : defaultMessage;
    }

    private static String getFieldName(String annotationFieldName, String defaultFieldName) {
        return StringUtils.isNotEmpty(annotationFieldName) ? annotationFieldName : defaultFieldName;
    }

    /**
     * 验证身份证
     *
     * @param idCard 参数
     */
    public static boolean validateIdCard(String idCard) {
        return Utils.checkIdentifyNumber(idCard);
    }

    /**
     * 验证银行卡号
     *
     * @param bankCode 银行卡号
     */
    public static boolean validateBankCode(String bankCode) {
        return Utils.checkBankCode(bankCode);
    }

}
