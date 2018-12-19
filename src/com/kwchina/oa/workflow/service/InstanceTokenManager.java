package com.kwchina.oa.workflow.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.workflow.entity.InstanceToken;

public interface InstanceTokenManager extends BasicManager {

	/**
	 * 获取某个实例的主Token
	 * @param instanceId
	 * @return
	 */
	public InstanceToken getMainToken(int instanceId);
	
	
	/**
	 * 获取某个实例的主Token
	 * @param tokens:某个实例所有的Token
	 * @return
	 */
	public InstanceToken getMainToken(List tokens);
	
	
	/**
	 * 获取某个实例的子Token
	 * @param instanceId
	 * @return
	 */
	public List getChildTokens(int instanceId);
	
	
	/**
	 * 获取某个实例的子Token
	 * @param tokens:某个实例所有的Token
	 * @return
	 */
	public List getChildTokens(List tokens);
}
