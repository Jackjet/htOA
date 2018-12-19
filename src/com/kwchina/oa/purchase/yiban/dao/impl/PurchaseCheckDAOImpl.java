package com.kwchina.oa.purchase.yiban.dao.impl;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;
import com.kwchina.oa.workflow.dao.InstanceCheckDAO;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseCheckDAOImpl extends BasicDaoImpl<PurchaseCheckInfor> implements PurchaseCheckDAO {
	/**
	 * 查找某个实例的全部审核信息
	 * @param instanceId
	 * @return
	 */
	public List findChecksByInstance(int instanceId){
		String sql = "from PurchaseCheckInfor  check where  check.layerInfor.purchase.purchaseId =" + instanceId;
		return getResultByQueryString(sql);
	}
	
	/**
	 * 查找某个实例，某个层次的全部审核信息
	 */
	public List findChecksByLayer(int layerId){
		String sql = "from InstanceCheckInfor  check where  check.layerInfor.layerId =" + layerId;
		return getResultByQueryString(sql);
	}
	
	//查找某个人全部审核信息
	public List findPersonCheckInfor(int personId){
		String sql = "from InstanceCheckInfor  check where  check.checker.personId =" + personId;
		return getResultByQueryString(sql);
	}
	
	//查找某个人未审核信息
	public List findPersonUncheckInfor(int personId){
		String sql = "from ReportCheckInfor  check where  check.user.personId =" + personId + " and check.checked = 0";
		return getResultByQueryString(sql);
	}
	
}
