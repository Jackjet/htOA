package com.kwchina.oa.workflow.service;

import java.sql.Timestamp;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;

public interface FlowCheckInforManager extends BasicManager{
	
	/**
	 * 保存审核信息
	 * @param checkInfor 审核信息
	 * @param checkDate 审核时间
	 * @return
	 */
	public InstanceCheckInfor saveCheckInfor(InstanceCheckInfor checkInfor, Timestamp checkDate);
	
	
	/**
	 * 删除某个审核信息
	 * @param checkInfor
	 */
	public void deleteCheckInfor(InstanceCheckInfor checkInfor);
}
