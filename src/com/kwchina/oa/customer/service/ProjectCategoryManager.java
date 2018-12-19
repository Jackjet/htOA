package com.kwchina.oa.customer.service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectInfor;
import com.kwchina.oa.customer.vo.ProjectCategoryVo;

public interface ProjectCategoryManager extends BasicManager {
	
	//保存项目信息(包括权限信息)
	public void saveProjectCategroy(ProjectCategory projectCategory,ProjectCategoryVo projectCategoryVo);
	
	//判断用户对某个信息的操作权限
	public boolean hasRight(ProjectCategory projectCategory, SystemUserInfor systemUser, int rightBit);
}
