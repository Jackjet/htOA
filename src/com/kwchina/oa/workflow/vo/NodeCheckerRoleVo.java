package com.kwchina.oa.workflow.vo;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.oa.workflow.entity.FlowNode;

public class NodeCheckerRoleVo {
	private String dataId;	    //数据Id
	private int flowNodeId;		//所属节点
	private int roleId;			//角色
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public int getFlowNodeId() {
		return flowNodeId;
	}
	public void setFlowNodeId(int flowNodeId) {
		this.flowNodeId = flowNodeId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
