package com.kwchina.oa.workflow.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.workflow.entity.FlowTransition;
import com.kwchina.oa.workflow.entity.InstanceTransitionInfor;

public interface InstanceTransitionDAO extends BasicDao<InstanceTransitionInfor>{

	/**
	 * 查找某个实例的全部连接信息
	 * @param flowId
	 * @return
	 */
	public List findInstanceTransition(int instanceId);
	

	/**	
	 * 获取链接到某个层次的所有Transition信息,即toLayer为该节点的连接信息	
	 * @param layerId
	 * @return
	 */
	public List findBeforeLayerTransition(int layerId);
	
	
	/**
	 * 获取某个节点链接到其他层次的所有Transition信息,即fromLayer为该节点的连接信息	
	 * @param layerId
	 * @return
	 */
	public List findAfterLayerTransition(int layerId);
	
	
	/**
	 * 获取两个节点之间的链接信息
	 * @param fromLayerId
	 * @param toLayerId
	 * @return
	 */
	public InstanceTransitionInfor findLayerTransition(int fromLayerId,int toLayerId);
}
