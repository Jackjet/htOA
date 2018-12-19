package com.kwchina.oa.purchase.sanfang.utils;

import com.kwchina.oa.purchase.sanfang.enums.CodeEnum;

/**
 * Created by 廖师兄
 * 2017-07-16 18:36
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
    public static <T extends CodeEnum> T getByMsg(String msg, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (msg.equals(each.getMsg())) {
                return each;
            }
        }
        return null;
    }
}
