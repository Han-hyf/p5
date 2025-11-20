package com.taoge.framework.util;

import com.alibaba.fastjson.JSONObject;
import com.taoge.framework.util.crypto.Cryptos;
import com.taoge.framework.util.crypto.Digests;
import com.taoge.framework.util.crypto.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class Utils {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String genOrderSn(String orderSnType, Long number) {
        return orderSnType + formatOrderSn(number);
    }

    public static String formatOrderSn(Long number) {
        return String.format("%08d", number);
    }

    public static boolean checkChineseName(String name) {
        if (!name.matches("[\\u4e00-\\u9fa5]{2,10}")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证Email
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     * 居民身份证号码15位或18位，最后一位可能是数字或字母
     *
     * @param identifyNumber
     * @return
     */
    public static boolean checkIdentifyNumber(String identifyNumber) {
        if (identifyNumber == null) {
            return false;
        }
        IdCard icu = new IdCard(identifyNumber);
        return icu.validate();
    }

    public static boolean checkBankCode(String bankCode) {
        if(bankCode == null) {
            return false;
        }
        BankCard bankCard = new BankCard(bankCode);
        return bankCard.verify();
    }

    /**
     * 验证手机号码，11位数字
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        String regex = "\\d{11}";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 得到显示时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    /**
     * 根据显示时间得到日期
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date longToDate(long time) {
        return new Date(time * 1000);
    }

    /**
     * 得到当前显示时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String beijingTime(Date date) {
//        return sdf.format(new Date(date.getTime() + 1000 * 60 * 60 * 8));
        return sdf.format(new Date(date.getTime()));
    }

    /**
     * 根据日期格式得到显示时间
     *
     * @param format yyyy-MM-dd HH:mm:ss
     *               MM月dd日 HH时mm分
     * @return
     */
    public static String beijingTimeByFormat(Date date, String format) {
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
//        return df.format(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8));
        return df.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 对称算法加密
     *
     * @param str
     * @return
     */
    public static String encodeHex(String str, String key) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            return Encodes.encodeHex(Cryptos.aesEncrypt(str.getBytes(), Encodes.decodeHex(key)));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 对称算法解密
     *
     * @param str
     * @return
     */
    public static String decodeHex(String str, String key) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            return Cryptos.aesDecrypt(Encodes.decodeHex(str), Encodes.decodeHex(key));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 验证密码
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }
//        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";//数字、字母组合
        String regex = "^[0-9A-Za-z~!@#$%^&*()_+:\"|<>?`=;',./-]{6,18}$";//字母、数字、特殊字符
        return password.matches(regex);
    }

    public static void main(String[] strings) {
        System.out.println(checkPassword("dfgsdfg#%^!#$"));
    }

    /**
     * 检查验证码
     *
     * @param code
     * @return
     */
    public static boolean checkVerifyCode(String code) {
        if (code == null) {
            return false;
        }
        String regex = "^[0-9A-Za-z]{6}$";//字母或数字
        return code.matches(regex);
    }

    /**
     * 检查ip
     *
     * @param ip
     * @return
     */
    public static boolean checkIp(String ip) {
        if (ip == null) {
            return false;
        }
        String regex = "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$";
        return ip.matches(regex);
    }

    /**
     * 将对象转成json串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null)
            return null;
        JSONObject result = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            // 字段名
            String key = fields[j].getName();
            // 字段类型
            String type = fields[j].getType().getName();
            try {
                // 字段值
                Object value = fields[j].get(obj);
                if (value != null) {
                    //如果是Date类型，转换成 yyyy-MM-dd HH:mm:ss
                    if (type.equalsIgnoreCase("java.util.Date")) {
                        value = Utils.dateToString((Date) value);
                    }
                    result.put(key, value.toString());
                } else {
                    //如果是Integer类型，转换成0
                    if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("java.lang.Long")) {
                        value = 0;
                        result.put(key, value.toString());
                    }
                }
            } catch (Exception e) {
                System.out.println("object field transfer failed, but it`s ok. field = " + key + ", type = " + type);
            }
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 将json串转成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static Object fromJson(String json, Class clazz) {
        if (StringUtils.isNotEmpty(json)) {
            return JSONObject.parseObject(json, clazz);
        }
        return null;
    }

    /**
     * 将newObj中待更新的字段值，填充到oldObj中做替换
     *
     * @param oldObj
     * @param newObj
     * @return 更新后的obj
     */
    public static Object fillObj(Object oldObj, Object newObj) {
        Field[] oldf = oldObj.getClass().getDeclaredFields();
        Field[] newf = newObj.getClass().getDeclaredFields();
        for (int j = 0; j < oldf.length; j++) {
            try {
                newf[j].setAccessible(true);
                if (newf[j].get(newObj) != null) {
                    oldf[j].setAccessible(true);
                    oldf[j].set(oldObj, newf[j].get(newObj));
                }
            } catch (IllegalAccessException e) {
                System.out.println("object field transfer failed, but it`s ok. field = " + newf[j].getName() + ", type = " + newf[j].getType().getName());
            }
        }
        return oldObj;
    }

    /**
     * 生成随机盐
     *
     * @return
     */
    public static String genSalt() {
        return Encodes.encodeHex(Digests.generateSalt(4));
    }

    /**
     * 生成随机盐
     *
     * @return
     */
    public static String generateSalt() {
        return Encodes.encodeHex(Digests.generateSalt(4));
    }

    /**
     * 生成随机推荐码
     *
     * @return
     */
    public static String genRecommendCode() {
        return Encodes.encodeHex(Digests.generateSalt(4));
    }

    /**
     * 密码加密
     *
     * @param salt
     * @param password
     * @return
     */
    public static String encryptPassword(String salt, String password) {
        return MD5Util.sha256Hex(salt, password);
    }

    /*public static void main(String[] args) {
        String salt = generateSalt();
        String password = encryptPassword(salt, "111111");

        System.out.println("salt="+salt);
        System.out.println("password="+password);
    }*/

    /**
     * 转换String to int 转换失败返回默认defVal
     *
     * @param intStr
     * @param defVal
     * @return
     */
    public static int toInteger(String intStr, int defVal) {
        try {
            return !StringUtils.isEmpty(intStr) ? Integer.parseInt(intStr) : defVal;
        } catch (NumberFormatException e) {
        }
        return defVal;
    }

    /**
     * 转换String to long 转换失败返回默认defVal
     *
     * @param intStr
     * @param defVal
     * @return
     */
    public static Long toLong(String intStr, long defVal) {
        try {
            return !StringUtils.isEmpty(intStr) ? Long.parseLong(intStr) : defVal;
        } catch (NumberFormatException e) {
        }
        return defVal;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将request请求参数统一接收Map格式，并重新request.setAttribute 返回到页面
     *
     * @author tpeng
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> initParamMap(HttpServletRequest request) {
        Map<String, String> rmap = new HashMap<String, String>();
        Map paramMap = request.getParameterMap();
        for (Object obj : paramMap.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            String name = (String) entry.getKey();
            String value = "";
            Object valObj = entry.getValue();
            if (valObj != null) {
                if (valObj instanceof String[]) {
                    String[] values = (String[]) valObj;
                    for (int i = 0; i < values.length; i++) {
                        value += values[i] + ",";
                    }
                    value = value.substring(0, value.length() - 1);
                } else {
                    value = valObj.toString();
                }
                request.setAttribute(name, value);
                rmap.put(name, value);
            }
        }
        return rmap;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static String filterNick(String nick) {
        if (StringUtils.isBlank(nick)) {
            return "";
        }
        StringBuilder nicksb = new StringBuilder();
        int l = nick.length();
        for (int i = 0; i < l; i++) {
            char _s = nick.charAt(i);
            if (isEmojiCharacter(_s)) {
                nicksb.append(_s);
            }
        }
        return nicksb.toString();
    }

    /**
     * 格式化金额转成元
     *
     * @param amount 金额(分)
     * @return
     */
    public static String formatAmountToYuan(Long amount) {
        if (null == amount) {
            return "0.00";
        }
        return new BigDecimal(amount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    /**
     * 将金额转成分
     *
     * @param amount 金额(元)
     * @return
     */
    public static Long convertAmountToFen(String amount) {
        if (null == amount) {
            return 0L;
        }
        return new BigDecimal(amount).multiply(new BigDecimal(100)).longValue();
    }

    /**
     * 格式化日期到秒，判断为空
     * @param date
     * @return
     */
    public static String formatDateYMDHMS(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期到分，判断为空
     * @param date
     * @return
     */
    public static String formatDateYMDHM(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化日期到天，判断为空
     * @param date
     * @return
     */
    public static String formatDateYMD(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }


    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (null == date) {
            return "";
        }
        return DateFormatUtils.format(date, pattern);
    }


    /**
     * 在没有配置外网ip的情况下获取内网ip
     * @return
     * @throws SocketException
     */
    public static String getInnerIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        &&!ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1){// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        &&!ip.isLoopbackAddress()
                        &&ip.getHostAddress().indexOf(":") == -1){// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }



}
