package com.kwchina.core.base.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.RoleInforDAO;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service("roleManager")
public class RoleManagerImpl extends BasicManagerImpl<RoleInfor> implements RoleManager {
	
	private RoleInforDAO roleDAO;

	@Autowired
	public void setRoleDAO(RoleInforDAO roleDAO) {
		this.roleDAO = roleDAO;
		super.setDao(roleDAO);
		//此句重要！否则DAO在超类里就不会注入，返回null的错误。
	}

	//获取的角色信息通过orderNo排序
	public List getRoleOrderBy() {
		List ls = this.roleDAO.getAll();
		//按照orderNo排序
		Collections.sort(ls, new BeanComparator("orderNo"));
		return ls;
	}
	
	//判断某个用户是否属于某个角色
	public boolean belongRole(SystemUserInfor user,RoleInfor role){
		boolean belong = false;
		int personId = user.getPersonId().intValue();
		
		if (role != null) {
			Set users = role.getUsers();
			for(Iterator it = users.iterator();it.hasNext();){
				SystemUserInfor tempUser = (SystemUserInfor)it.next();
				int tempPersonId = tempUser.getPersonId().intValue();
				if(tempPersonId == personId){
					belong = true;
					break;
				}
			}
		}
		return belong;
	}
	
	//批量获取角色信息
	public List getRoles(String roleIds) {
		List roles = null;
		
		String queryString = "from RoleInfor role where roleId in (" + roleIds + ")";
		roles = this.roleDAO.getResultByQueryString(queryString);
		
		return roles;
	}

	/**
	 * 根据角色名称指获取角色信息
	 */
	public List getRolesByName(String roleNameStr) {
		List roles = null;
		
		String queryString = "from RoleInfor role where role.roleName like '%"+roleNameStr+"%'";
		roles = this.roleDAO.getResultByQueryString(queryString);
		
		return roles;
	}

}
