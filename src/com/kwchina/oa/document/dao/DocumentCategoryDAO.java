package com.kwchina.oa.document.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.document.entity.DocumentCategory;


public interface DocumentCategoryDAO extends BasicDao<DocumentCategory> {

	//获取某个层次的文档分类
	public List getLayerCategorys(int layer);
	
    //获取所有父分类
	public List getParentCategory();
	
	//获取用于归档的分类
	public List getCategoryFromReport();
}
