package com.kwchina.extend.supervise.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.extend.supervise.dao.TaskCategoryDAO;
import com.kwchina.extend.supervise.entity.TaskCategory;


@Repository
public class TaskCategoryDAOImpl extends BasicDaoImpl<TaskCategory> implements TaskCategoryDAO{
	
	//获取某个层次的文档分类
	public List getLayerCategorys(int layer){
		String sql = "from TaskCategory  category where  category.layer = " + layer;
		return getResultByQueryString(sql);
	}

	
	//获取所有父分类
	public List getParentCategory(){
		String sql = "from TaskCategory category where category.leaf = 0 and category.categoryId <> 1 order by displayNo";
		return getResultByQueryString(sql);
	}

}
