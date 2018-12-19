package com.kwchina.extend.supervise.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.extend.supervise.entity.TaskCategory;


public interface TaskCategoryDAO extends BasicDao<TaskCategory> {

	//获取某个层次的文档分类
	public List getLayerCategorys(int layer);
	
    //获取所有父分类
	public List getParentCategory();
	
}
