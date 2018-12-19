package com.kwchina.oa.workflow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.NodeCheckerRoleDAO;
import com.kwchina.oa.workflow.entity.NodeCheckerRole;
import com.kwchina.oa.workflow.service.NodeCheckerRoleManager;

@Service 
public class NodeCheckerRoleManagerImpl extends BasicManagerImpl<NodeCheckerRole> implements NodeCheckerRoleManager{
	private NodeCheckerRoleDAO nodeCheckerRoleDAO;

	@Autowired
	public void setNodeCheckerRoleDAO(NodeCheckerRoleDAO nodeCheckerRoleDAO) {
		this.nodeCheckerRoleDAO = nodeCheckerRoleDAO;
		super.setDao(nodeCheckerRoleDAO);
	}

}
