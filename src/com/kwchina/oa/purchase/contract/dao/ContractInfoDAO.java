package com.kwchina.oa.purchase.contract.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.purchase.contract.entity.ContractInfo;

import java.util.List;


public interface ContractInfoDAO extends BasicDao<ContractInfo> {
    /**
     * 获取所有合同信息
     */
    public List<ContractInfo> getAllContract();
    /**
     * 根据id获取合同信息
     */
    public ContractInfo getContractByFlowId(String contractId);
}
