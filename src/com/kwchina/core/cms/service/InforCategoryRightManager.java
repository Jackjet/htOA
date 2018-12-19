package com.kwchina.core.cms.service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.entity.CategoryUserRight;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.common.service.BasicManager;

public interface InforCategoryRightManager extends BasicManager{
	
	//通过系统用户获取CategoryUserRight
	public CategoryUserRight getCategoryRightByUser(SystemUserInfor systemUser);
	
	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(InforCategoryRight right, int rightDigit);
	
	//判断用户对某个分类的操作权限
	public boolean hasRight(InforCategory category, SystemUserInfor systemUser, int rightBit);
	
}
