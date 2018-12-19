package com.kwchina.oa.workflow.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.InstanceTokenDAO;
import com.kwchina.oa.workflow.entity.InstanceToken;
import com.kwchina.oa.workflow.service.InstanceTokenManager;

@Service
public class InstanceTokenManagerImpl extends BasicManagerImpl<InstanceToken> implements InstanceTokenManager {
	@Autowired
	private InstanceTokenDAO instanceTokenDAO;
	
	/**
	 * 获取某个实例的主Token
	 * @param instanceId
	 * @return
	 */
	public InstanceToken getMainToken(int instanceId){
		List tokens = this.instanceTokenDAO.findInstanceTokens(instanceId);		
		for(Iterator it = tokens.iterator();it.hasNext();){
			InstanceToken token = (InstanceToken)it.next();
			if(token.getParent()==null)
				return token;
		}
		
		return null;
	}
	
	
	/**
	 * 获取某个实例的主Token
	 * @param tokens:某个实例所有的Token
	 * @return
	 */
	public InstanceToken getMainToken(List tokens){
		if(!tokens.isEmpty()){
			for(Iterator it = tokens.iterator();it.hasNext();){
				InstanceToken token = (InstanceToken)it.next();
				if(token.getParent()==null)
					return token;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取某个实例的子Token
	 * @param instanceId
	 * @return
	 */
	public List getChildTokens(int instanceId){
		List childTokens = new ArrayList();
		
		List tokens = this.instanceTokenDAO.findInstanceTokens(instanceId);		
		for(Iterator it = tokens.iterator();it.hasNext();){
			InstanceToken token = (InstanceToken)it.next();
			if(token.getParent()!=null)
				childTokens.add(token);
		}
		
		return childTokens;
	}
	
	
	/**
	 * 获取某个实例的子Token
	 * @param tokens:某个实例所有的Token
	 * @return
	 */
	public List getChildTokens(List tokens){
		List childTokens = new ArrayList();
		
		for(Iterator it = tokens.iterator();it.hasNext();){
			InstanceToken token = (InstanceToken)it.next();
			if(token.getParent()!=null)
				childTokens.add(token);
		}
		
		return childTokens;		
	}
}
