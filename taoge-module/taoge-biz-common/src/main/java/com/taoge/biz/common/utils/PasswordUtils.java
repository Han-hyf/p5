package com.taoge.biz.common.utils;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class PasswordUtils {

    private static final String SALT_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 使用md5进行密码加密
     */
    public static String encryptionPassword(String password,String salt){
        return DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成salt
     */
    public static String generateSalt(){
        //随机几个字母
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            salt.append(SALT_STR.charAt(RandomUtils.nextInt(0,51)));
        }
        return salt.toString();
    }

}
