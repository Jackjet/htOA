package com.kwchina.core.base.service;

import java.util.ArrayList;
import java.util.List;

import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.common.service.BasicManager;

public interface StructureManager extends BasicManager{
	
	//获取的岗位信息通过orderNo排序
	public List getStructureOrderBy();
	
	public void save(StructureInfor structure);
	
	/**
	 * 按照树状结构组织StructureInfor信息
	 * @param structrueId:
	 * 根分类Id
	 */
	public ArrayList getStructureAsTree(Integer structureId);
	
	//获取某个岗位的父岗位信息(从根开始)
	public ArrayList getParents(Integer structureId);
	
	//某个岗位的父岗位信息+自身
	public ArrayList getParentsAndSelf(Integer structureId);
	

		
}
