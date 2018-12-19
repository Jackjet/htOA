package com.kwchina.oa.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ProjectCategoryRightDAO;
import com.kwchina.oa.customer.entity.ProjectCategoryRight;
import com.kwchina.oa.customer.service.ProjectCategoryRightManager;

@Service
public class ProjectCategoryRightManagerImpl extends BasicManagerImpl<ProjectCategoryRight> implements ProjectCategoryRightManager{
	
	private ProjectCategoryRightDAO projectCategoryRightDAO;

	@Autowired
	public void setProjectCategoryRightDAO(ProjectCategoryRightDAO projectCategoryRightDAO) {
		this.projectCategoryRightDAO = projectCategoryRightDAO;
		super.setDao(projectCategoryRightDAO);
	}

	//判断某个权限信息中是否包含某种操作的权限 
	public boolean hasRight(ProjectCategoryRight right, int rightDigit) {
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
