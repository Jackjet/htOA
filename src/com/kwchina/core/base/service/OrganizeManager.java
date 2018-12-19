package com.kwchina.core.base.service;

import java.util.ArrayList;
import java.util.List;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.common.service.BasicManager;


public interface OrganizeManager extends BasicManager{
	//获取部门信息
	public List getDepartments();
	
	//获取班组信息
	public List getGroups();
	
	//获取未删除的所有组织结构信息
	public List getUndeleted();
	
	/**
	 * 按照树状结构组织OrganizeInfor信息 
	 * @param organizeId:
	 * 根分类Id
	 */
	public ArrayList getOrganizeAsTree(Integer organizeId);
	
	//获取指定用户的部门信息
	public List getDepFromUsers(List users);
	
	//根据名称获取部门信息
	public OrganizeInfor findByOrganizeName(String organizeName);
}
