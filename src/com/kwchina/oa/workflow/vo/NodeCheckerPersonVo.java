package com.kwchina.oa.workflow.vo;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.workflow.entity.FlowNode;

public class NodeCheckerPersonVo {
	private String dataId;			//数据Id
	private int userId; 	        //审核人
	private int flowNodeId;		    //所属节点
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFlowNodeId() {
		return flowNodeId;
	}
	public void setFlowNodeId(int flowNodeId) {
		this.flowNodeId = flowNodeId;
	}
	
	
}
