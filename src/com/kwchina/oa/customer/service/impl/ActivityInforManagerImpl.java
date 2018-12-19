package com.kwchina.oa.customer.service.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.weaver.patterns.IfPointcut.IfFalsePointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ActivityInforDAO;
import com.kwchina.oa.customer.entity.ActivityInfor;
import com.kwchina.oa.customer.service.ActivityInforManager;
import com.kwchina.oa.customer.vo.ActivityInforVo;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.vo.MessageInforVO;

@Service
public class ActivityInforManagerImpl extends BasicManagerImpl<ActivityInfor> implements ActivityInforManager {

	private ActivityInforDAO activityInforDAO;

	@Autowired
	public void setActivityInforDAO(ActivityInforDAO activityInforDAO) {
		this.activityInforDAO = activityInforDAO;
		super.setDao(activityInforDAO);
	}
	
	//转化ActivityInfor为ActivityInforVo
	public ActivityInforVo transPOToVO(ActivityInfor activityInfor) {
		ActivityInforVo activityInforVo = new ActivityInforVo();

		try {
			BeanUtils.copyProperties(activityInforVo, activityInfor);
			activityInforVo.setActivityId(activityInfor.getActivityId());
			activityInforVo.setAttachmentStr(activityInfor.getAttachment());
			activityInforVo.setCompanyName(activityInfor.getCustomer().getCompanyName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if(activityInfor.getPlanDate()!=null){
				String planDateStr = dateFormat.format(activityInfor.getPlanDate());
				activityInforVo.setPlanDateStr(planDateStr);
			}
			if(activityInfor.getActivityDate()!=null){
				String activityDateStr = dateFormat.format(activityInfor.getActivityDate());
				activityInforVo.setActivityDateStr(activityDateStr);
				activityInforVo.setActivityType("1");
			}
		} catch (Exception ex) {

		}

		return activityInforVo;
	}
	
	
}
