package com.kwchina.oa.purchase.yiban.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseLayerInfor;
import com.kwchina.oa.purchase.yiban.service.PurchaseCheckInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseLayerInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

@Service
public class PurchaseCheckInforManagerImpl extends BasicManagerImpl<PurchaseCheckInfor> implements PurchaseCheckInforManager {

	@Autowired
	private PurchaseCheckDAO purchaseCheckDAO;

	@Autowired
	private PurchaseLayerInforManager purchaseLayerInforManager;

	@Autowired
	private PurchaseManager purchaseManager;

	@Autowired
	public void setPurchaseCheckDAO(PurchaseCheckDAO purchaseCheckDAO) {
		this.purchaseCheckDAO = purchaseCheckDAO;
		super.setDao(purchaseCheckDAO);
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
	public PurchaseCheckInfor saveCheckInfor(PurchaseCheckInfor checkInfor, Timestamp checkDate){

		//Integer checkId = checkInfor.getCheckId();
		Timestamp current = null;
		if (checkDate != null) {
			current = checkDate;
		}else {
			current = new Timestamp(System.currentTimeMillis());
		}
		
		checkInfor.setEndDate(current);
//		checkInfor.setStatus(1);
		this.purchaseCheckDAO.save(checkInfor);
		PurchaseInfor purchase = checkInfor.getLayerInfor().getPurchase();
		boolean hasAllchecked = this.purchaseManager.hasAllchecked(purchase, checkInfor);
		if (hasAllchecked) {
			PurchaseLayerInfor layerInfor = checkInfor.getLayerInfor();
			//更新审核层状态和结束时间
			layerInfor.setEndTime(current);
			this.purchaseLayerInforManager.save(layerInfor);
		}


		
		return checkInfor;
	}
	
	/**
	 * 删除某个审核信息
	 * @param checkInfor
	 */
	public void deleteCheckInfor(PurchaseCheckInfor checkInfor){
		
		
		//删除
		this.purchaseCheckDAO.remove(checkInfor);
	}
}
