package com.kwchina.oa.document.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.document.dao.DocumentCategoryDAO;
import com.kwchina.oa.document.entity.DocumentCategory;


@Repository
public class DocumentCategoryDAOImpl extends BasicDaoImpl<DocumentCategory> implements DocumentCategoryDAO{
	
	//获取某个层次的文档分类
	public List getLayerCategorys(int layer){
		String sql = "from DocumentCategory  category where  category.layer = " + layer;
		return getResultByQueryString(sql);
	}

	
	//获取所有父分类
	public List getParentCategory(){
		String sql = "from DocumentCategory category where category.leaf = 0 and category.categoryId <> 1 order by displayNo";
		return getResultByQueryString(sql);
	}


	//获取用于归档的分类
	public List getCategoryFromReport() {
		String sql = "from DocumentCategory category where category.categoryId <> 1 and fromReport = 1 order by displayNo";
		return getResultByQueryString(sql);
	}
}
