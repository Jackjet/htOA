package com.kwchina.oa.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.ModifyInforDAO;
import com.kwchina.oa.workflow.entity.ModifyInfor;
import com.kwchina.oa.workflow.service.ModifyInforManager;
@Service
public class ModifyInforManagerImpl extends BasicManagerImpl<ModifyInfor> implements ModifyInforManager{
	
	@Autowired
	private ModifyInforDAO modifyInforDAO;
	
	@Autowired
	public void setModifyInforDAO(ModifyInforDAO modifyInforDAO) {
		this.modifyInforDAO = modifyInforDAO;
		super.setDao(modifyInforDAO);
	}
	
	
	public List getModifyInfor(int instanceId){
		List list = this.modifyInforDAO.getModifyInfor(instanceId);
		return list;
	}
}