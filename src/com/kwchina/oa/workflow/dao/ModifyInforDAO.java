package com.kwchina.oa.workflow.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.workflow.entity.ModifyInfor;


public interface ModifyInforDAO extends BasicDao<ModifyInfor>{
	
	public List getModifyInfor(int instanceId);
	
}