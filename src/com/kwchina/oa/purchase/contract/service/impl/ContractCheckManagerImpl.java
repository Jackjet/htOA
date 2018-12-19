package com.kwchina.oa.purchase.contract.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.contract.dao.ContractCheckDAO;
import com.kwchina.oa.purchase.contract.entity.ContractCheckInfo;
import com.kwchina.oa.purchase.contract.service.ContractCheckManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractCheckManagerImpl extends BasicManagerImpl<ContractCheckInfo> implements ContractCheckManager {
	private ContractCheckDAO contractCheckDAO;
	@Autowired
	public void setContractCheckDAO(ContractCheckDAO contractCheckDAO) {
		this.contractCheckDAO= contractCheckDAO;
		super.setDao(contractCheckDAO);
	}

}

