package com.kwchina.extend.supervise.service;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.extend.supervise.entity.TaskCategory;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;


public interface TaskCategoryRightManager extends BasicManager {
		
	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId);
	
	// 判断用户对某个分类的操作权限
	public boolean hasRight(TaskCategory category, SystemUserInfor systemUser, int rightBit);
	
	/**
	 * 判断某个权限信息中是否包含某种操作的权限
	 * 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(TaskCategoryRight right,int rightDigit);	

	
    //通过personId获得分类权限
	public List getRightsByPersonId(int personId);
	
	
	//通过personId和categoryId获得分类权限
	public TaskCategoryRight getRightsByCondition(int personId, int categoryId);
	
}
