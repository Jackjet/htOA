package com.kwchina.extend.supervise.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;

public interface TaskCategoryRightDAO extends BasicDao<TaskCategoryRight> {

	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId);
	
    //通过personId获得分类权限
	public List getRightsByPersonId(int personId);
}
