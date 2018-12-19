package com.kwchina.oa.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ProjectRightDAO;
import com.kwchina.oa.customer.entity.ProjectRight;
import com.kwchina.oa.customer.service.ProjectRightManager;

@Service
public class ProjectRightManagerImpl extends BasicManagerImpl<ProjectRight> implements ProjectRightManager{
	
	private ProjectRightDAO projectRightDAO;

	@Autowired
	public void setProjectRightDAO(ProjectRightDAO projectRightDAO) {
		this.projectRightDAO = projectRightDAO;
	}

	//判断某个权限信息中是否包含某种操作的权限 
	public boolean hasRight(ProjectRight right, int rightDigit) {
		boolean booleanRight = false;
		int rightValue = right.getOperateRight();

		int a = rightValue >> (rightDigit - 1);
		int result = (a & 1);

		if (result == 0) {
			booleanRight = false;
		} else {
			booleanRight = true;
		}

		return booleanRight;
	}
	
	
}
