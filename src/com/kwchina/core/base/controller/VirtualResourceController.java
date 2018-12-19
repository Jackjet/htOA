package com.kwchina.core.base.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.FunctionRightInfor;
import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.OperationDefinitionManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.VirtualResourceManager;

@Controller
@RequestMapping("core/virtualResource.do")
public class VirtualResourceController {

	@Resource
	private VirtualResourceManager virtualResourceManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private OperationDefinitionManager operationDefinitionManager;
	

	//显示所有权限资源
	@RequestMapping(params="method=list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//功能模块的树状构造
		ArrayList resources = this.virtualResourceManager.getResourceAsTree(null);
		request.setAttribute("_Resources", resources);
		
		//操作定义的树状构造
		ArrayList operationTree = this.operationDefinitionManager.getOperationAsTree(null);
		request.setAttribute("_OperationTREE", operationTree);

		return "base/listResource";
	}

	//新增或者修改权限资源
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String resourceId = request.getParameter("resourceId");
		String operationId = request.getParameter("operationId");
		
		if (resourceId != null && resourceId.length() > 0 && operationId != null && operationId.length() > 0) {
			VirtualResource resource = (VirtualResource)this.virtualResourceManager.get(Integer.valueOf(resourceId));
			OperationDefinition operation = (OperationDefinition)this.operationDefinitionManager.get(Integer.valueOf(operationId));
			
			//获取系统角色
			List roles = this.roleManager.getAll();
			request.setAttribute("_Roles", roles);
			
			//取功能模块的功能权限
			int[] roleIds = new int[roles.size()];
			int position = operation.getPosition();
			Set<FunctionRightInfor> functionRights = resource.getFunctionRights();
			int i = 0;
			for (FunctionRightInfor functionRight : functionRights) {
				long rightData = functionRight.getRightData();
				long result = rightData >> (position - 1);
				//若用户针对该操作具有权限,则加入roleIds
				if ((result & 1) == 1) {
					RoleInfor role = functionRight.getRole();
					roleIds[i] = role.getRoleId();
				}
				i++;
			}
			
			request.setAttribute("_RoleIds", roleIds);
			request.setAttribute("_Resource", resource);
			request.setAttribute("_Operation", operation);
		}
		
		return "base/editResource";
	}
	

	//保存权限资源
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String resourceId = request.getParameter("resourceId");
		String operationId = request.getParameter("operationId");
		
		if (resourceId != null && resourceId.length() > 0 && operationId != null && operationId.length() > 0) {
			VirtualResource resource = (VirtualResource)this.virtualResourceManager.get(Integer.valueOf(resourceId));
			OperationDefinition operation = (OperationDefinition)this.operationDefinitionManager.get(Integer.valueOf(operationId));
			
			//保存该功能模块的功能权限
			String[] roleIds = request.getParameterValues("roleIds");
			int position = operation.getPosition();
			Set<FunctionRightInfor> functionRights = resource.getFunctionRights();
			Set rightSet = new HashSet();
			
			//有角色信息时
			if (roleIds != null && roleIds.length > 0) {
				//去掉的删除
				for (FunctionRightInfor functionRight : functionRights) {
					RoleInfor role = functionRight.getRole();
					long rightData = functionRight.getRightData();
					//判断是否包含该操作的权限
					long result = rightData >> (position - 1);
					//原有权限信息中包含该权限且选中的角色中无此用户时才删除
					if ((result & 1) == 1) {
						boolean hasRole = false;
						for (int i=0;i<roleIds.length;i++) {
							if (roleIds[i].equals(String.valueOf(role.getRoleId()))) {
								hasRole = true;
								break;
							}
						}
						if (!hasRole) {
							long tmp = 1 << (position - 1);
							functionRight.setRightData(rightData - tmp);
						}
					}
					rightSet.add(functionRight);
				}
				
				//没有的加上
				for (int i=0;i<roleIds.length;i++) {
					boolean hasRight = false;
					FunctionRightInfor tmpRight = null;
					for (FunctionRightInfor functionRight : functionRights) {
						RoleInfor role = functionRight.getRole();
						boolean hasOperation = false;
						long rightData = functionRight.getRightData();
						//判断是否包含该操作的权限
						long result = rightData >> (position - 1);
						if ((result & 1) == 1) {
							hasOperation = true;
						}
						if (roleIds[i].equals(String.valueOf(role.getRoleId())) && hasOperation) {
							hasRight = true;
							break;
						}else if (roleIds[i].equals(String.valueOf(role.getRoleId()))) {
							//特殊处理包含该角色的权限信息,但权限数据不包含该操作的情况
							tmpRight = functionRight;
						}
					}
					if (!hasRight) {
						FunctionRightInfor functionRight = new FunctionRightInfor();
						if (tmpRight != null) {
							//包含该角色的权限信息,但权限数据不包含该操作的情况
							functionRight = tmpRight;
							long rightData = 1 << (position - 1);
							functionRight.setRightData(functionRight.getRightData() + rightData);
						}else {
							//该用户的权限信息根本不存在的情况
							functionRight.setResource(resource);
							RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(roleIds[i]));
							functionRight.setRole(role);
							long rightData = 1 << (position - 1);
							functionRight.setRightData(rightData);
						}
						rightSet.add(functionRight);
					}
				}
				
			}else {
				//无角色信息时,判断原权限信息中是否包含该权限信息,有的话则删除,否则不操作
				for (FunctionRightInfor functionRight : functionRights) {
					long rightData = functionRight.getRightData();
					long result = rightData >> (position - 1);
					//包含的话则删除
					if ((result & 1) == 1) {
						long tmp = 1 << (position - 1);
						functionRight.setRightData(rightData - tmp);
					}
					rightSet.add(functionRight);
				}
			}
			resource.setFunctionRights(rightSet);
			this.virtualResourceManager.save(resource);
		}
	}
	
	
}
