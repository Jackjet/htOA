package com.kwchina.oa.purchase.yiban.dao.impl;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseInforDAO;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseInforDAOImpl extends BasicDaoImpl<PurchaseInfor> implements PurchaseInforDAO {

	
	/**
	 * 获取某个流程下的所有实例
	 * @param flowId
	 * @return
	 */
	public List getInstancesByFlow(int flowId) {

		String sql = "from PurchaseInfor  instance where  instance.flowId.flowId = " + flowId
				+ " order by purchaseId";
		return getResultByQueryString(sql);

	}
	
	
	/**
	 * 获取某个流程下,未完成的所有实例 
	 * @param flowId
	 * @return
	 */
	public List getUnfinishInstances(int flowId){
		String sql = "from FlowInstanceInfor  instance where  instance.flowDefinition.flowId = " + flowId
				+ " and instance.endTime = null"
				+ " order by instanceId";
		return getResultByQueryString(sql);
	}

	

	
 
}
