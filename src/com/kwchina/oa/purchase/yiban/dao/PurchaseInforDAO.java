package com.kwchina.oa.purchase.yiban.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;


import java.util.List;

public interface PurchaseInforDAO extends BasicDao<PurchaseInfor>{
	//查找回收站的实例信息
	/**
	 * public List getTrashInstances(){
	 *  }
	 */
	
	
	/**
	 * 获取某个流程下的所有实例
	 * @param flowId
	 * @return
	 */
	public List getInstancesByFlow(int flowId);
	
	
	/**
	 * 获取某个流程下,未完成的所有实例 
	 * @param flowId
	 * @return
	 */
	public List getUnfinishInstances(int flowId);
	
}
