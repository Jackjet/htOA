package com.kwchina.oa.purchase.yiban.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;


import java.sql.Timestamp;

public interface PurchaseCheckInforManager extends BasicManager{
	
	/**
	 * 保存审核信息
	 * @param checkInfor 审核信息
	 * @param checkDate 审核时间
	 * @return
	 */
	public PurchaseCheckInfor saveCheckInfor(PurchaseCheckInfor checkInfor, Timestamp checkDate);
	
	
	/**
	 * 删除某个审核信息
	 * @param checkInfor
	 */
	public void deleteCheckInfor(PurchaseCheckInfor checkInfor);
}
