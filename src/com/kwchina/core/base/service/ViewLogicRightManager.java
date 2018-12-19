package com.kwchina.core.base.service;


import javax.servlet.http.HttpServletRequest;

import com.kwchina.core.common.service.BasicManager;


public interface ViewLogicRightManager extends BasicManager{
	
	//组合浏览权限中的sql语句及ViewLogicRight表中的sql语句
	public String getViewRightSql(HttpServletRequest request, String compareMethodName, String[] params);
}
