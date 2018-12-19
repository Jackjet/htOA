package com.kwchina.core.util;

/**
 * Created by Administrator on 2017/3/12.
 */
public class Json {
    private Object object;
    private boolean success=false;
    private String msg;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
