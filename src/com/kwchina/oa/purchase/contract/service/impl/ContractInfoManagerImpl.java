package com.kwchina.oa.purchase.contract.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.contract.VO.ContractListVo;
import com.kwchina.oa.purchase.contract.dao.ContractInfoDAO;
import com.kwchina.oa.purchase.contract.entity.ContractInfo;
import com.kwchina.oa.purchase.contract.enums.ContractStatusEnum;
import com.kwchina.oa.purchase.contract.service.ContractInfoManager;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractInfoManagerImpl extends BasicManagerImpl<ContractInfo> implements ContractInfoManager {
	private ContractInfoDAO contractInfoDAO;
	@Autowired
	public void setContractInfoDAO(ContractInfoDAO contractInfoDAO) {
		this.contractInfoDAO= contractInfoDAO;
		super.setDao(contractInfoDAO);
	}
	@Override
	public ContractListVo transPOToVO(ContractInfo contractInfo) {
		ContractListVo contractListVo=new ContractListVo();
		contractListVo.setApplierName(contractInfo.getContractApplier().getPerson().getPersonName());
		contractListVo.setContractCode(contractInfo.getContractCode());
		contractListVo.setContractID(contractInfo.getContractID());
		contractListVo.setContractName(contractInfo.getContractName());
		contractListVo.setDepartmentName(contractInfo.getSubmitDepartment().getOrganizeName());
		contractListVo.setContractStatus(EnumUtil.getByCode(contractInfo.getContractStatus(), ContractStatusEnum.class).getMsg());
		contractListVo.setStartTime(contractInfo.getSubmitDate());
		return contractListVo;
	}

	@Override
	public List getAllContractInfo() {
		return contractInfoDAO.getAllContract();
	}

	@Override
	public ContractInfo getByContractId(String contractId) {
		return contractInfoDAO.getContractByFlowId(contractId);
	}
}

