package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.InstanceTransitionDAO;
import com.kwchina.oa.workflow.entity.InstanceTransitionInfor;

@Repository
public class InstanceTransitionDAOImpl extends BasicDaoImpl<InstanceTransitionInfor> implements InstanceTransitionDAO{
	
	/**
	 * 查找某个实例的全部连接信息
	 * @param flowId
	 * @return
	 */
	public List findInstanceTransition(int instanceId){
		String sql = "from InstanceTransitionInfor  transition where  transition.instance.instanceId = " + instanceId + " order by transId";
		return getResultByQueryString(sql);
	}
	

	/**	
	 * 获取链接到某个层次的所有Transition信息,即toLayer为该节点的连接信息	
	 * @param layerId
	 * @return
	 */
	public List findBeforeLayerTransition(int layerId){
		String sql = "from InstanceTransitionInfor  transition where  transition.toLayer.layerId = " + layerId;
		return getResultByQueryString(sql);
	}
	
	
	/**
	 * 获取某个节点链接到其他层次的所有Transition信息,即fromLayer为该节点的连接信息	
	 * @param layerId
	 * @return
	 */
	public List findAfterLayerTransition(int layerId){
		String sql = "from InstanceTransitionInfor  transition where  transition.fromLayer.layerId = " + layerId;
		return getResultByQueryString(sql);
	}
	
	
	/**
	 * 获取两个节点之间的链接信息
	 * @param fromLayerId
	 * @param toLayerId
	 * @return
	 */
	public InstanceTransitionInfor findLayerTransition(int fromLayerId,int toLayerId){
		String sql = "from InstanceTransitionInfor  transition where  transition.fromLayer.layerId = " + fromLayerId + " and transition.toLayer.layerId=" + toLayerId;
		List ls = getResultByQueryString(sql);
		if(!ls.isEmpty()){
			return (InstanceTransitionInfor)ls.get(0);
		}
		
		return null;
	}	
	
}
