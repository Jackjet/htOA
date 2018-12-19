package com.kwchina.oa.personal.message.service;


import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.vo.MessageInforVO;

public interface MessageInforManager extends BasicManager {
	//转化MessageInfor为MessageInforVO
	public MessageInforVO transPOToVO(MessageInfor message);
	
	
	//判断是否能删除或修改消息
	public boolean canDeleteOrEditMessage(SystemUserInfor user,MessageInfor message);
	
	
}
