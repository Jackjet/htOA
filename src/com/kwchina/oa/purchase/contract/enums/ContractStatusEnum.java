package com.kwchina.oa.purchase.contract.enums;

import com.kwchina.oa.purchase.sanfang.enums.CodeEnum;

/**
 * Created by asus on 2018/6/12.
 */

public enum ContractStatusEnum implements CodeEnum {
    PENDING(0,"待发起"),
    PURCHASE_LEADR (1,"采购领导审核中"),
    RELATED_DEPT (2,"相关部门审核中"),
    LEAD_GROUP (3,"领导层审核中"),
    ENDING (4,"审核完毕"),
    STOP (-1,"流程中止"),
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

    ContractStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
