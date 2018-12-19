package com.kwchina.core.base.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.base.dao.SystemUserInforDAO;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.dao.BasicDaoImpl;

@Repository
public class SystemUserInforDAOImpl extends BasicDaoImpl<SystemUserInfor> implements SystemUserInforDAO{

	//根据职级获取用户
	public List getUserByPosition(int positionTag) {
		String hql = "from SystemUserInfor user where user.invalidate = 0 and user.person.positionLayer <= " + positionTag + " order by user.person.positionLayer,user.person.personName";
		List userList = getResultByQueryString(hql);
		return userList;
	}
	
	//获取职级大于一定值的用户
	public List getOtherPositionUser(int positionTag) {
		String hql = "from SystemUserInfor user where user.invalidate = 0 and user.person.positionLayer > " + positionTag + " order by user.person.positionLayer,user.person.personName";
		List userList = getResultByQueryString(hql);
		return userList;
	}
	
}
