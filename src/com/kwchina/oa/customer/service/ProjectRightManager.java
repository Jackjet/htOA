package com.kwchina.oa.customer.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.ProjectRight;

public interface ProjectRightManager extends BasicManager {

	
	//判断某个权限信息中是否包含某种操作的权限 
	public boolean hasRight(ProjectRight right, int rightDigit);
}
