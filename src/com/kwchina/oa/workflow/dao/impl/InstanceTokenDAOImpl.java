package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.InstanceTokenDAO;
import com.kwchina.oa.workflow.entity.InstanceToken;

@Repository
public class InstanceTokenDAOImpl extends BasicDaoImpl<InstanceToken> implements InstanceTokenDAO {
	
	/**
	 * 获取某个实例的Token信息
	 * @param instanceId
	 * @return
	 */
	public List findInstanceTokens(int instanceId){
		String sql = "from InstanceToken  token where  token.instance.instanceId =" + instanceId;
		return getResultByQueryString(sql);
	}
	
	
	/**
	 * 删除某个实例
	 */
	public void deleteInstanceTokens(int instanceId){
		
	}
}
