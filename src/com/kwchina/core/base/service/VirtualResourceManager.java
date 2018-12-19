package com.kwchina.core.base.service;

import java.util.ArrayList;

import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.common.service.BasicManager;

public interface VirtualResourceManager extends BasicManager{
	
	//根据资源路径获取资源
	public VirtualResource  getVirtualResource(String resourcePath);
	
	//根据别名获取
	public VirtualResource  getResourceByAlias(String alias);
	
	/**
	 * 按照树状结构组织Resource信息	 * 
	 * @param resourceId:根分类Id
	 */
	public ArrayList getResourceAsTree(Integer resourceId);
	
	public void save(VirtualResource resource);
}
