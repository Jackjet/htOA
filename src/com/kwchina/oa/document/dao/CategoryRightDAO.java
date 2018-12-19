package com.kwchina.oa.document.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.document.entity.DocumentCategoryRight;

public interface CategoryRightDAO extends BasicDao<DocumentCategoryRight> {

	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId);
	
    //通过personId获得分类权限
	public List getRightsByPersonId(int personId);
}
