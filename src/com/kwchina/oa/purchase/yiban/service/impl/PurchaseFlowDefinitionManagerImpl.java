package com.kwchina.oa.purchase.yiban.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseFlowDefinitionDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseInforDAO;
import com.kwchina.oa.purchase.yiban.entity.PurchaseFlowDefinition;
import com.kwchina.oa.purchase.yiban.service.PurchaseFlowDefinitionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("purchaseflowManager")
public class PurchaseFlowDefinitionManagerImpl extends BasicManagerImpl<PurchaseFlowDefinition> implements PurchaseFlowDefinitionManager {
	
	@Autowired
	private PurchaseInforDAO purchaseInforDAO;

	@Autowired
	private PurchaseFlowDefinitionDAO purchaseflowDefinitionDAO;

	@Autowired
	public void setFlowDefinitionDAO(PurchaseFlowDefinitionDAO purchaseflowDefinitionDAO) {
		this.purchaseflowDefinitionDAO = purchaseflowDefinitionDAO;
		super.setDao(purchaseflowDefinitionDAO);
	}
	
	/**
	 * 判断是否可以修改流程（主要是指增加或者删除节点),如果该流程下已经有未完成的实例，则不能修改
	 * @param flowId
	 * @return
	 */
	public boolean canModifyFlow(int flowId){
		List ls = this.purchaseInforDAO.getUnfinishInstances(flowId);
		if(!ls.isEmpty()){
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 判断是否可以删除流程,如果该流程下已经实例，则不能删除
	 * @param flowId
	 * @return
	 */
	public boolean canDeleteFlow(int flowId){
		List ls = this.purchaseInforDAO.getInstancesByFlow(flowId);
		if(!ls.isEmpty()){
			return false;
		}
		
		return true;
	}

	
}
