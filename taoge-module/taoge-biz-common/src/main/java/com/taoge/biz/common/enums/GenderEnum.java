package com.taoge.biz.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别枚举
 */
public enum GenderEnum {
    MALE(1),
    FEMALE(2),
    UNKNOWN(0),
    ;

    private final int gender;

    GenderEnum(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    private static final Map<Integer, GenderEnum> enumsMap = new HashMap<>();

    static {
        for (GenderEnum value : GenderEnum.values()) {
            enumsMap.put(value.getGender(), value);
        }
    }

    /**
     * 根据性别值，查找枚举
     */
    public static GenderEnum getByGender(Integer gender) {
        if (gender == null) {
            return null;
        }
        for (GenderEnum value : GenderEnum.values()) {
            if (value.getGender() == gender) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据性别值，查找枚举
     */
    public static GenderEnum getByGenderFromMap(Integer gender) {
        if (gender == null) {
            return null;
        }
        return enumsMap.get(gender);
    }
}
