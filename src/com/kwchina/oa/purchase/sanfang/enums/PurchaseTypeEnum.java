package com.kwchina.oa.purchase.sanfang.enums;
/**
 * Created by asus on 2018/6/12.
 */

public enum PurchaseTypeEnum implements CodeEnum{
    GENERAL(0,"一般采购"),
    BUSINESS (1,"业务采购"),
    PROJECT  (2,"工程采购"),
    PARTIAL (3,"零星采购"),
    ;
    private Integer code;
    private String msg;

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    PurchaseTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
