package com.kwchina.core.base.service.impl;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.ViewLogicRightDAO;
import com.kwchina.core.base.entity.FunctionRightInfor;
import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.entity.ViewLogicRight;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.OperationDefinitionManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.ViewLogicRightManager;
import com.kwchina.core.base.service.VirtualResourceManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.util.SysCommonMethod;

@Service
public class ViewLogicRightManagerImpl extends BasicManagerImpl<ViewLogicRight> implements ViewLogicRightManager {
	
	private ViewLogicRightDAO viewLogicRightDAO;
	
	@Resource
	private VirtualResourceManager resourceManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private OperationDefinitionManager operationDefinitionManager;

	@Autowired
	public void setViewLogicRightDAO(ViewLogicRightDAO viewLogicRightDAO) {
		this.viewLogicRightDAO = viewLogicRightDAO;
		super.setDao(viewLogicRightDAO);
	}

	//组合浏览权限中的sql语句及ViewLogicRight表中的sql语句
	public String getViewRightSql(HttpServletRequest request, String compareMethodName, String[] params) {
		String returnSql = "";
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		/** 判断该模块的功能权限中是否设置了如:浏览自己发布的、浏览本部门的权限 */
		String pathURI = request.getRequestURI();
		VirtualResource virtualResource = resourceManager.getVirtualResource(pathURI);
		if (virtualResource != null) {
			Set<FunctionRightInfor> functionRights = virtualResource.getFunctionRights();
			for (FunctionRightInfor functionRight : functionRights) {
				RoleInfor role = functionRight.getRole();
				long rightData = functionRight.getRightData();
				//判断用户对该操作是否拥有权限(通过对权限数据进行移位来判断)
				if (this.roleManager.belongRole(systemUser, role)) {
					//判断是否设置了该权限(浏览自己发布的、浏览本部门的)
					long a = rightData >> (OperationDefinition.RightPosition_ListSel - 1);
					long resultA = (a & 1);
					if (resultA == 1) {
						returnSql += " and " + params[0] + "=" + systemUser.getPersonId();
					}
					long b = rightData >> (OperationDefinition.RightPosition_ListDep - 1);
					long resultB = (b & 1);
					if (resultB == 1) {
						int departmentId = 0;
						if (systemUser.getPerson().getDepartment() != null) {
							departmentId = systemUser.getPerson().getDepartment().getOrganizeId();
						}
						returnSql += " and " + params[1] + "=" + departmentId;
					}
					if (resultA == 1 && resultB == 1) {
						break;
					}
				}
			}
		}
		
		/** 判断是否设置了浏览逻辑权限(仅对父操作数据有效) */
		OperationDefinition od = this.operationDefinitionManager.getOperationByMethod(compareMethodName);
		if (od != null && od.getParent() == null) {
			Set<ViewLogicRight> viewRights = od.getViewRights();
			boolean belongRight = false;
			//判断是否设置了该权限,是的话则加上逻辑判断sql语句
			for (ViewLogicRight viewRight : viewRights) {
				if (viewRight.getRole() != null) {
					RoleInfor role = viewRight.getRole();
					belongRight = this.roleManager.belongRole(systemUser, role);
				}else if (viewRight.getUser() != null) {
					if (viewRight.getUser().getPersonId().intValue() == systemUser.getPersonId().intValue()) {
						belongRight = true;
					}
				}
				if (belongRight) {
					returnSql += viewRight.getSql();
				}
			}
		}
		
		return returnSql;
	}
	
}
