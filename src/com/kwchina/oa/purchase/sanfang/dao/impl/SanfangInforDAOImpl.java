package com.kwchina.oa.purchase.sanfang.dao.impl;
import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.purchase.sanfang.dao.SanfangInforDAO;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SanfangInforDAOImpl extends BasicDaoImpl<SanfangInfor> implements SanfangInforDAO{

	@Override
	public List<SanfangInfor> getAllSanfang() {
		String sql ="from SanfangInfor sanfangInfor  where 1=1";
		List list = getResultByQueryString(sql);

		return list;
	}

	@Override
	public SanfangInfor getSanfangInfoByFlowId(String flowId) {
		String sql="from SanfangInfor sanfangInfor where sanfangInfor.sanfangFlowId="+"'"+flowId+"'";
		List<SanfangInfor> list = getResultByQueryString(sql);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
