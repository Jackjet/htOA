package com.kwchina.oa.meeting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.meeting.dao.MeetDAO;
import com.kwchina.oa.meeting.entity.AttendInfor;
import com.kwchina.oa.meeting.service.AttendInforManager;

@Service
public class AttendInforManagerImpl extends BasicManagerImpl<AttendInfor> implements AttendInforManager {
	
	
	private MeetDAO meetDAO;

	@Autowired
	public void setMeetDAO(MeetDAO meetDAO) {
		this.meetDAO = meetDAO;
		super.setDao(meetDAO);
	}


}
