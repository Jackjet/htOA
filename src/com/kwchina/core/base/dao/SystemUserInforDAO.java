package com.kwchina.core.base.dao;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.dao.BasicDao;

public interface SystemUserInforDAO extends BasicDao<SystemUserInfor> {
	
	//根据职级获取用户
	public List getUserByPosition(int positionTag);
	
	//获取职级大于一定值的用户
	public List getOtherPositionUser(int positionTag);
}
