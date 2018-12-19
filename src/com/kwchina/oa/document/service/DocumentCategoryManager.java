package com.kwchina.oa.document.service;

import java.util.ArrayList;
import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.vo.DocumentCategoryVo;

public interface DocumentCategoryManager extends BasicManager {

	//把某个分类的所有子类id按照1,2,3的格式组合
	public String getChildIds(Integer categoryId);
	
	//获取全部叶文档分类
	public List getAllLeafCategory();
	
	//获取某个层次的文档分类
	public List getLayerCategorys(int layer);
	
	 //获取所有父分类
	public List getParentCategory();
	
	//获取用于归档的分类
	public List getCategoryFromReport();
	
	//判断用户对某个分类是否具有操作权限
	public boolean hasRight(DocumentCategory category, SystemUserInfor systemUser);
	
	//获取列表，树状显示
	public ArrayList getCategoryAsTree(Integer categoryId);
	
	//获取用户有权限看到的分类列表，树状显示
	public ArrayList getCategoryAsTree(Integer categoryId, SystemUserInfor user);
	
	/**
	 * 找到某个分类所有父分类信息(从最顶层到该层的序列)
	 */
	public ArrayList getAllParentCategory(DocumentCategory category);
	
	//获取全部 某个分类下所有分类的Id,并构造为(1,2,3)的格式
	public String getSubCategoryIds(SystemUserInfor user, int categoryId);
	
	//保存分类信息(包括权限信息)
	public void saveCategory(DocumentCategory category, DocumentCategoryVo vo);
	
}
