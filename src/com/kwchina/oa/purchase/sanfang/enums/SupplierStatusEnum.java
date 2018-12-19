package com.kwchina.oa.purchase.sanfang.enums;
/**
 * Created by asus on 2018/6/12.
 */

public enum SupplierStatusEnum implements CodeEnum{

    PENDING_REVIEW(0,"潜在待审核"),
    POTENTIAL (1,"潜在"),
    CERTIFYING  (2,"合格认证中"),
    LEADCHECK(3,"合格终审中"),
    QUALIFIED (4,"合格"),
    EXPIRED_SOON  (5,"即将过期"),
    STOP(-1,"审核未通过")
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

    SupplierStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
