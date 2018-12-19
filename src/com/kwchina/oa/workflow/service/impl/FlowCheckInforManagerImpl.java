package com.kwchina.oa.workflow.service.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.workflow.dao.InstanceCheckDAO;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.service.FlowCheckInforManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.FlowLayerInforManager;

@Service
public class FlowCheckInforManagerImpl extends BasicManagerImpl<InstanceCheckInfor> implements FlowCheckInforManager {
	
	@Autowired
	private InstanceCheckDAO instanceCheckDAO;

	@Autowired
	private FlowLayerInforManager flowLayerInforManager;
	
	@Autowired
	private FlowInstanceManager flowInstanceManager;
	
	@Autowired
	public void setInstanceCheckDAO(InstanceCheckDAO instanceCheckDAO) {
		this.instanceCheckDAO = instanceCheckDAO;
		super.setDao(instanceCheckDAO);
	}

	/**
	 * 保存审核信息
	 * 1. 如果是预设节点：
	 *    A.则审核完成后，需要判断该节点是否是一个人审核，即可结束
	 *    B.全部审核完成后，也需要结束审核层
	 * @param checkInfor 审核信息
	 * @param checkDate 审核时间
	 * @return
	 */
	public InstanceCheckInfor saveCheckInfor(InstanceCheckInfor checkInfor, Timestamp checkDate){
		
		//Integer checkId = checkInfor.getCheckId();
		Timestamp current = null;
		if (checkDate != null) {
			current = checkDate;
		}else {
			current = new java.sql.Timestamp(System.currentTimeMillis());
		}
		
		checkInfor.setEndDate(current);
		checkInfor.setStatus(InstanceCheckInfor.Check_Status_Checked);
		this.instanceCheckDAO.save(checkInfor);
		
		InstanceLayerInfor layerInfor = checkInfor.getLayerInfor();
		if(this.flowLayerInforManager.finishedCheck(layerInfor)){
			//该层都审核完毕，则更新其状态
			Set checkInfors = layerInfor.getCheckInfors();
			for(Iterator it = checkInfors.iterator();it.hasNext();){
				InstanceCheckInfor tempCheckInfor = (InstanceCheckInfor)it.next();
				if(tempCheckInfor.getStatus() == InstanceCheckInfor.Check_Status_Unckeck){
					tempCheckInfor.setStatus(InstanceCheckInfor.Check_Status_Canceled);
					this.instanceCheckDAO.save(tempCheckInfor);
				}
			}
			
			//更新审核层状态和结束时间
			layerInfor.setStatus(InstanceLayerInfor.Layer_Status_Finished);
			layerInfor.setEndTime(current);
			this.flowLayerInforManager.save(layerInfor);
			
			//如果该层已经完成审核，自动流转到下一层次
			//this.flowInstanceManager.flowToNextNode(layerInfor);
		}
		
		return checkInfor;
	}
	
	/**
	 * 删除某个审核信息
	 * @param checkInfor
	 */
	public void deleteCheckInfor(InstanceCheckInfor checkInfor){
		
		
		//删除
		this.instanceCheckDAO.remove(checkInfor);
	}
}
