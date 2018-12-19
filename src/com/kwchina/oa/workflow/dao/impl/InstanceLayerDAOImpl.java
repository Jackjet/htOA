package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.InstanceLayerDAO;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;

@Repository
public class InstanceLayerDAOImpl extends BasicDaoImpl<InstanceLayerInfor> implements InstanceLayerDAO {
	/**
	 * 查找某个实例的审核层次信息
	 */		
	public List findInstanceCheckLayers(int instanceId){
		String sql = "from InstanceLayerInfor  layerInfor where  layerInfor.instance.instanceId = " + instanceId + " order by layer";
		return getResultByQueryString(sql);
	}
	
	/**
	 * 查找某个实例,某个审核层次的层次信息
	 * 考虑到分叉，某个layer下具有多个审核层次信息
	 */	
	public List findInstanceCheckLayers(int instanceId,int layer){
		String sql = "from InstanceLayerInfor  layerInfor where  layerInfor.instance.instanceId = " + instanceId + " and layerInfor.layer=" + layer;
		return getResultByQueryString(sql);		
	}
	
	
	/**
	 * 查找某个实例，以某个审核层次为from的层次信息
	 * @param instanceId: 实例Id
	 * @param fromLayer: 其实Layer的Id
	 * @return
	 */
	public List findLayersByFromLayer(int instanceId,int fromLayer){
		String sql = "from InstanceLayerInfor  layerInfor where  layerInfor.instance.instanceId = " + instanceId + " and layerInfor.fromLayer=" + fromLayer;
		return getResultByQueryString(sql);		
	}
}
