package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.InstanceInforDAO;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;

@Repository
public class InstanceInforDAOImpl extends BasicDaoImpl<FlowInstanceInfor> implements InstanceInforDAO {

	
	/**
	 * 获取某个流程下的所有实例
	 * @param flowId
	 * @return
	 */
	public List getInstancesByFlow(int flowId) {

		String sql = "from FlowInstanceInfor  instance where  instance.flowDefinition.flowId = " + flowId
				+ " order by instanceId";
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
