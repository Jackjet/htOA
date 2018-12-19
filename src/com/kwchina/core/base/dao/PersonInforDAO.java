package com.kwchina.core.base.dao;

import java.util.List;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.common.dao.BasicDao;

public interface PersonInforDAO  extends BasicDao<PersonInfor> {
		
	//根据职级获取人员
	public List getPersonByPosition(int positionTag);
	
	//获取职级大于一定值的人员
	public List getOtherPositionPerson(int positionTag);
	
	//获取部门内大于某一职级的人员
	public List getDepartmentOtherPositionPerson(int organizeId,int positionTag);
}

