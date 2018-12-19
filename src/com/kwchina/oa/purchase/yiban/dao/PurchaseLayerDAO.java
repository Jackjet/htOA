package com.kwchina.oa.purchase.yiban.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.purchase.yiban.entity.PurchaseLayerInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */

public interface PurchaseLayerDAO extends BasicDao<PurchaseLayerInfor>{
	/**
	 * 查找某个实例的审核层次信息
	 * @param instanceId
	 * @return
	 */
	public List findInstanceCheckLayers(int instanceId);

	/**
	 * 查找某个实例,某个审核层次的层次信息
	 * 考虑到分叉，某个layer下具有多个审核层次信息
	 * @param instanceId
	 * @param layer
	 * @return
	 */
	public List findInstanceCheckLayers(int instanceId, int layer);


	/**
	 * 查找某个实例，以某个审核层次为from的层次信息
	 * @param instanceId: 实例Id
	 * @param fromLayer: 其实Layer的Id
	 * @return
	 */
	public List findLayersByFromLayer(int instanceId, int fromLayer);
}
