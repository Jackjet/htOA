package com.kwchina.core.cms.service;

import java.util.ArrayList;


import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.common.service.BasicManager;

public interface InforCategoryManager extends BasicManager{
	
	/**
	 * 按照树状结构组织InforCategory信息 
	 * 也是获取某个分类所有子分类的方法
	 * @param organizeId:分类Id
	 */
	public ArrayList getCategoryAsTree(Integer categoryId);
	
	// 获取用户有权限看到的分类列表，树状显示
	public ArrayList getCategoryAsTree(Integer categoryId, SystemUserInfor user);
	
	//获取所有叶分类
	public ArrayList getLeafCategory();
	
	//把某个分类的所有子类id按照1,2,3的格式组合
	public String getChildIds(Integer categoryId);
	
	//获取某个分类的父分类信息(从根开始)
	public ArrayList getParents(Integer categoryId);
	
	public void save(InforCategory category);
	
}
