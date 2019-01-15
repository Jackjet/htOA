package com.kwchina.oa.purchase.zhaotou.enums;

import com.kwchina.oa.purchase.sanfang.enums.CodeEnum;

/**
 * Created by asus on 2018/6/12.
 */

public enum ZhaotouStatusEnum implements CodeEnum {
    /**待发起*/
    PENDING(0,"待发起"),
    /**评审小组审核中*/
    CHECK_GROUP (1,"评审小组审核中"),
    /**定标中*/
    SET_BID(2,"定标中"),
    /**评审委员会审核中*/
    CHECK_COMMITTEE  (3,"评审委员会审核中"),
    /**已完成*/
    COMPLETE (4,"已完成"),
    /**终止*/
    STOP (-1,"终止"),
    ;
    private Integer code;
    private String msg;

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ZhaotouStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
