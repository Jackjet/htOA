package com.kwchina.core.base.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwchina.core.common.service.BasicManager;


public interface DataRightInforManager extends BasicManager{
	
	//判断是否具有数据权限
	public void haveDataRight(HttpServletRequest request, HttpServletResponse response, String comparePrimarykey, int compareDataId, String compareMethodName);
}
