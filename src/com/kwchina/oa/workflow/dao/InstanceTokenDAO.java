package com.kwchina.oa.workflow.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.workflow.entity.InstanceToken;

public interface InstanceTokenDAO  extends BasicDao<InstanceToken>{
	/**
	 * 获取某个实例的Token信息
	 * @param instanceId
	 * @return
	 */
	public List findInstanceTokens(int instanceId);
	
	/**
	 * 删除某个实例
	 */
	public void deleteInstanceTokens(int instanceId);
}
