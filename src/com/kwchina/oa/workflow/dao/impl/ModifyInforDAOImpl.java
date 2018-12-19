package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.ModifyInforDAO;
import com.kwchina.oa.workflow.entity.ModifyInfor;
@Repository
public class ModifyInforDAOImpl extends BasicDaoImpl<ModifyInfor> implements ModifyInforDAO{
	
	public List getModifyInfor(int instanceId){
		String hql = " from ModifyInfor modifyInfor where modifyInfor.instance.instanceId='"+instanceId+"' order by modifyInfor.modifyTime desc";
		List list = getResultByQueryString(hql);
		return list;
	}
	
	
}