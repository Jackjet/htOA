package com.kwchina.oa.workflow.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;

public interface InstanceInforDAO extends BasicDao<FlowInstanceInfor>{
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
