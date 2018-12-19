package com.kwchina.extend.supervise.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.entity.CategoryRoleRight;
import com.kwchina.core.cms.entity.CategoryUserRight;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.supervise.dao.TaskCategoryRightDAO;
import com.kwchina.extend.supervise.entity.TaskCategory;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;
import com.kwchina.extend.supervise.service.TaskCategoryRightManager;

@Service
public class TaskCategoryRightManagerImpl extends BasicManagerImpl<TaskCategoryRight> implements TaskCategoryRightManager {

	private TaskCategoryRightDAO taskCategoryRightDAO;

	//注入的方法
	@Autowired
	public void setTaskCategoryRightDAO(TaskCategoryRightDAO taskCategoryRightDAO) {
		this.taskCategoryRightDAO = taskCategoryRightDAO;
		super.setDao(taskCategoryRightDAO);
	}

	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId) {
		return this.taskCategoryRightDAO.getCategoryRights(categoryId);
	}

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(TaskCategoryRight right, int rightDigit) {
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

	//判断用户对某个分类的操作权限
	public boolean hasRight(TaskCategory category, SystemUserInfor systemUser, int rightBit) {
		/*boolean hasRight = false;

		int userId = systemUser.getPersonId().intValue();

		Set rights = category.getRights();
		
		//判断是否对该类权限做过设置
		List typeRights = new ArrayList();
		for (Iterator it = rights.iterator(); it.hasNext();) {
			TaskCategoryRight categoryRight = (TaskCategoryRight) it.next();
			int rightValue = categoryRight.getOperateRight();

			int a = rightValue >> (rightBit - 1);
			int result = (a & 1);
			if (result == 1) {
				typeRights.add(categoryRight);
			}
		}
		
			
		if (systemUser.getUserType() == 1) {
			// 系统管理员
			*//** @todo change the following sentence *//*
			hasRight = true;
		} else {
			if(typeRights.isEmpty()){
				//无此类权限设置,默认为有权限
				hasRight = true;
			}else{
				//有此类权限设置,判断是否有该用户的权限
				for (Iterator it = typeRights.iterator(); it.hasNext();) {
					TaskCategoryRight categoryRight = (TaskCategoryRight) it.next();
					int rightValue = categoryRight.getOperateRight();
					int tempUserId = categoryRight.getUser().getPersonId().intValue();
					if (userId == tempUserId) {
						int a = rightValue >> (rightBit - 1);
						int result = (a & 1);
						if (result == 1) {
							hasRight = true;
						}
	
						break;
					}
				}
			}
		}
		

		return hasRight;*/
		
		boolean hasRight = false;

		int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == SystemUserInfor._Type_Admin) {
			hasRight = true;
		} else {
			Set rights = category.getRights();
			if (rights == null || rights.size() == 0) {
				// 未设置任何权限
				hasRight = true;
			} else {
				for (Iterator it = rights.iterator(); it.hasNext();) {
					TaskCategoryRight tempRight = (TaskCategoryRight)it.next();
					//对人员的授权
					int tempPersonId = tempRight.getUser().getPersonId().intValue();
					if (personId == tempPersonId) {
						int rightValue = tempRight.getOperateRight();
						int a = rightValue >> (rightBit - 1);
						int result = (a & 1);
						if (result == 1) {
							hasRight = true;
						}

						break;
					}						
				}
			}
		}

		return hasRight;

	}

	//通过personId获得分类权限
	public List getRightsByPersonId(int personId) {
		return this.taskCategoryRightDAO.getRightsByPersonId(personId);
	}
	
	
	// 通过personId和categoryId获得分类权限
	public TaskCategoryRight getRightsByCondition(int personId, int categoryId) {
		TaskCategoryRight categoryRight = null;
		
		String queryString ="from TaskCategoryRight categoryRight where categoryRight.user.personId = " + personId + " and categoryRight.category.categoryId = " + categoryId;
		List list = this.taskCategoryRightDAO.getResultByQueryString(queryString);
		
		if (list != null && list.size() > 0) {
			categoryRight = (TaskCategoryRight)list.get(0);
		}
		
		return categoryRight;
	}
}
