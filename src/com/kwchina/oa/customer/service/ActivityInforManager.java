package com.kwchina.oa.customer.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.ActivityInfor;
import com.kwchina.oa.customer.vo.ActivityInforVo;

public interface ActivityInforManager extends BasicManager {

	//转化MessageInfor为MessageInforVO
	public ActivityInforVo transPOToVO(ActivityInfor activityInfor);
}
