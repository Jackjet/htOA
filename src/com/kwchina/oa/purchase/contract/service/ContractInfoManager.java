package com.kwchina.oa.purchase.contract.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.purchase.contract.VO.ContractListVo;
import com.kwchina.oa.purchase.contract.entity.ContractInfo;

import java.util.List;


public interface ContractInfoManager extends BasicManager {

    /**
     * 获取所有已发起三方
     */
    public List getAllContractInfo();

    ContractInfo getByContractId(String contractId);
    public ContractListVo transPOToVO(ContractInfo contractInfo);
}

