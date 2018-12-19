package com.kwchina.oa.sys;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

import com.kwchina.core.common.vo.BaseVo;



public class LoginVo extends BaseVo{

	@NotBlank(message = "[请输入用户名!]")
    private String userName;
	@NotBlank(message = "[请输入密码!]")
    private String password;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
