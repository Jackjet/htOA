package com.kwchina.oa.workflow.service;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceInforRight;
import com.kwchina.oa.workflow.entity.InstanceInforUserRight;

public interface InstanceInforRightManager extends BasicManager{
	
	//通过系统用户获取InstanceInforUserRight
	public InstanceInforUserRight getInstanceRightByUser(SystemUserInfor systemUser);

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(InstanceInforRight right, int rightDigit);
	
	
	//判断用户对某个信息的操作权限
	public boolean hasRight(FlowInstanceInfor instance, SystemUserInfor systemUser, int rightBit);
	
	
	//根据inforId,roleId,userId获取权限
	public List getRightsByID(int documentId,int rightId,int flag);
}
