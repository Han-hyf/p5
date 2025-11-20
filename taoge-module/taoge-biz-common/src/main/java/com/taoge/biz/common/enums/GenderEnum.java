package com.taoge.biz.common.enums;

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


    /**
     * 根据枚举值找到性别
     * @param gender
     * @return
     */
    public static GenderEnum getByGender(Integer gender){
        if (gender == null){
            return null;
        }
        for (GenderEnum value : GenderEnum.values()){
            if (value.getGender() == gender){
                return value;
            }
        }
        return null;
    }
}
