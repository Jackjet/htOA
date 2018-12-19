package com.kwchina.oa.purchase.contract.dao.impl;
import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.purchase.contract.dao.ContractInfoDAO;
import com.kwchina.oa.purchase.contract.entity.ContractInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContractInfoDAOImpl extends BasicDaoImpl<ContractInfo> implements ContractInfoDAO {
	@Override
	public List<ContractInfo> getAllContract() {
		String sql ="from ContractInfo contract  where 1=1";
		List list = getResultByQueryString(sql);

		return list;
	}

	@Override
	public ContractInfo getContractByFlowId(String contractId) {
		String sql="from ContractInfo contract where contract.contractID="+"'"+contractId+"'";
		List<ContractInfo> list = getResultByQueryString(sql);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
