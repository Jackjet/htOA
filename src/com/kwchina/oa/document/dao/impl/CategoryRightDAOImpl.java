package com.kwchina.oa.document.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.document.dao.CategoryRightDAO;
import com.kwchina.oa.document.entity.DocumentCategoryRight;

@Repository
public class CategoryRightDAOImpl extends BasicDaoImpl<DocumentCategoryRight> implements CategoryRightDAO{

		
	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId){
		String sql ="from DocumentCategoryRight categoryRight where categoryRight.category.categoryId = " + categoryId;		
		List list = getResultByQueryString(sql);

		return list;
	}	

	//通过personId获得分类权限
	public List getRightsByPersonId(int personId){
		String sql ="from DocumentCategoryRight categoryRight  where categoryRight.user.personId = " + personId;
		List list = getResultByQueryString(sql);

		return list;
    }

}
