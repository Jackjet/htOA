package com.kwchina.oa.workflow.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.personal.address.dao.AddressCategoryDAO;
import com.kwchina.oa.personal.address.dao.PersonalAddressDAO;
import com.kwchina.oa.personal.address.entity.AddressCategory;
import com.kwchina.oa.personal.address.entity.PersonalAddress;
import com.kwchina.oa.personal.address.service.AddressCategoryManager;
import com.kwchina.oa.personal.address.service.PersonalAddressManager;
import com.kwchina.oa.workflow.dao.NodeCheckerPersonDAO;
import com.kwchina.oa.workflow.dao.NodeCheckerRoleDAO;
import com.kwchina.oa.workflow.entity.NodeCheckerPerson;
import com.kwchina.oa.workflow.entity.NodeCheckerRole;
import com.kwchina.oa.workflow.service.NodeCheckerPersonManager;
import com.kwchina.oa.workflow.service.NodeCheckerRoleManager;

@Service 
public class NodeCheckerPersonManagerImpl extends BasicManagerImpl<NodeCheckerPerson> implements NodeCheckerPersonManager{
	private NodeCheckerPersonDAO nodeCheckerPersonDAO;

	@Autowired
	public void setNodeCheckerPersonDAO(NodeCheckerPersonDAO nodeCheckerPersonDAO) {
		this.nodeCheckerPersonDAO = nodeCheckerPersonDAO;
		super.setDao(nodeCheckerPersonDAO);
	}

}
