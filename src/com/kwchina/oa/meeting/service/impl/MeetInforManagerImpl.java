package com.kwchina.oa.meeting.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.meeting.dao.MeetDAO;
import com.kwchina.oa.meeting.entity.MeetInfor;
import com.kwchina.oa.meeting.service.MeetInforManager;
import com.kwchina.oa.meeting.vo.MeetInforVo;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobDAO;

@Service
public class MeetInforManagerImpl extends BasicManagerImpl<MeetInfor> implements MeetInforManager {

	private MeetDAO meetDAO;

	@Autowired
	public void setMeetDAO(MeetDAO meetDAO) {
		this.meetDAO = meetDAO;
		super.setDao(meetDAO);
	}

	// 转化MeetInfor为MeetInforVo
	public MeetInforVo transPOToVO(MeetInfor meet) {
		MeetInforVo meetVo = new MeetInforVo();

		try {
			BeanUtils.copyProperties(meetVo, meet);
			meetVo.setAuthorId(meet.getAuthor().getPersonId());
			meetVo.setAttachmentStr(meet.getAttachment());
			meetVo.setAuthorName(meet.getAuthor().getPerson().getPersonName());
			if (meet.getOrganize() != null) {
				meetVo.setOrganizeId(meet.getOrganize().getOrganizeId());
				meetVo.setOrganizeName(meet.getOrganize().getOrganizeName());
			}
			// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss");
			String startH = meet.getStartHour().toString();
			startH = startH.length() == 1?"0"+startH:startH;
			String startM = meet.getStartMinutes().toString();
			if (startM.equals("0")) {
				startM = "00";
			}
			String endH = meet.getEndHour().toString();
			endH = endH.length() == 1?"0"+endH:endH;
			String endM = meet.getEndMinutes().toString();
			if (endM.equals("0")) {
				endM = "00";
			}
			String meetDate = "";
			if (meet.getEndMeetDate() == null) {
				if (startH.equals("0") && endH.equals("0")&&startM.equals("00")&&endM.equals("00")) {
					meetDate = meet.getMeetDate().toString();
				} else {
					meetDate = meet.getMeetDate().toString() + " " + startH + ":" + startM + "-" + endH + ":" + endM;
				}
			} else {
				if(startH.equals("0")&&endH.equals("0")&&startM.equals("00")&&endM.equals("00")){
					meetDate = meet.getMeetDate().toString() + " " + "至" + " " + meet.getEndMeetDate().toString();
				}else{
					meetDate = meet.getMeetDate().toString() + " " + startH + ":" + startM + " 至 " + meet.getEndMeetDate().toString()+ " " + endH + ":" + endM;
				}
			}
			meetVo.setMeetDate(meetDate);
			meetVo.setCreateTimeStr(meet.getCreateTime().toString());
		} catch (Exception ex) {

		}

		return meetVo;
	}
	
	//取到某天会议
	public List getDayMeets(String day){
		List list = new ArrayList();
		String hql = "from MeetInfor meet where meetDate='"+day+"'";
		list = this.getResultByQueryString(hql);
		return list;
	}
}
