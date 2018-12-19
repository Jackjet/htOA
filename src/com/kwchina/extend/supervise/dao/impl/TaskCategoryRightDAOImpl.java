package com.kwchina.extend.supervise.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.extend.supervise.dao.TaskCategoryRightDAO;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;

@Repository
public class TaskCategoryRightDAOImpl extends BasicDaoImpl<TaskCategoryRight> implements TaskCategoryRightDAO{

		
	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId){
		String sql ="from TaskCategoryRight categoryRight where categoryRight.category.categoryId = " + categoryId;		
		List list = getResultByQueryString(sql);

		return list;
	}	

	//通过personId获得分类权限
	public List getRightsByPersonId(int personId){
		String sql ="from TaskCategoryRight categoryRight  where categoryRight.user.personId = " + personId;
		List list = getResultByQueryString(sql);

		return list;
    }

}
