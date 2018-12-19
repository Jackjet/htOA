package com.kwchina.oa.purchase.yiban.dao.impl;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseLayerDAO;
import com.kwchina.oa.purchase.yiban.entity.PurchaseLayerInfor;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseLayerDAOImpl extends BasicDaoImpl<PurchaseLayerInfor> implements PurchaseLayerDAO {
	/**
	 * 查找某个实例的审核层次信息
	 */		
	public List findInstanceCheckLayers(int instanceId){
		String sql = "from PurchaseLayerInfor  layerInfor where  layerInfor.purchase.purchaseId = " + instanceId + " order by layer desc";
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
