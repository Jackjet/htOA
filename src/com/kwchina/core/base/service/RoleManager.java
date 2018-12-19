package com.kwchina.core.base.service;

import java.util.List;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;


public interface RoleManager extends BasicManager{
	
	//获取的角色信息通过orderNo排序
	public List getRoleOrderBy();
	
	//判断某个用户是否属于某个角色
	public boolean belongRole(SystemUserInfor user,RoleInfor role);
	
	//批量获取角色信息
	public List getRoles(String roleIds);
	
	//根据角色名称指获取角色信息
	public List getRolesByName(String roleNameStr);
}
