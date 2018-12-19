package com.kwchina.core.base.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.base.dao.PersonInforDAO;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.common.dao.BasicDaoImpl;

@Repository
public class PersonInforDAOImpl extends BasicDaoImpl<PersonInfor> implements PersonInforDAO{
	
	//根据职级获取人员(非注销的)
	public List getPersonByPosition(int positionTag) {
		String hql = "from PersonInfor personInfor where personInfor.user.invalidate=0 and personInfor.positionLayer = " + positionTag + " order by personInfor.positionLayer";
		List personList = getResultByQueryString(hql);
		return personList;
	}
	
    //获取职级大于一定值的人员(非注销的)
	public List getOtherPositionPerson(int positionTag) {
		String hql = "from PersonInfor personInfor where personInfor.user.invalidate=0 and personInfor.positionLayer >= " + positionTag + " order by personInfor.positionLayer";
		List personList = getResultByQueryString(hql);
		return personList;
	}
	
	
	//获取部门内大于某一职级的人员(非注销的)
	public List getDepartmentOtherPositionPerson(int organizeId,int positionTag){
		String hql = "from PersonInfor personInfor where personInfor.user.invalidate=0 and personInfor.department.organizeId ='" + organizeId + "' and personInfor.positionLayer <= " + positionTag; 
		hql += " order by personInfor.positionLayer";
		List personList = getResultByQueryString(hql);
		return personList;
	}
	
}

