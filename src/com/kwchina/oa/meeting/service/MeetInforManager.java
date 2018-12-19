package com.kwchina.oa.meeting.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.meeting.entity.MeetInfor;
import com.kwchina.oa.meeting.vo.MeetInforVo;

public interface MeetInforManager extends BasicManager {

	//转化MessageInfor为MessageInforVO
	public MeetInforVo transPOToVO(MeetInfor meet);
	
	//取到某天会议
	public List getDayMeets(String day);
	
}
