package com.kwchina.oa.meeting.vo;


public class MeetInforVo {
	
	
	private int meetId=0; // 会议Id

	private String meetName; // 会议名称

	private int organizeId; // 责任部室
	
	private String organizeName; // 责任部室名称

	private String compere; // 主持人

	private String attendInfor; // 参加人员

	private String meetRoom; // 会议室

	private int bookId; // 预定人

	private String meetDate; // 会议日期
	
//	private String endMeetDate;	//会议结束日期

	private int startHour; // 会议开始小时

	private int startMinutes; // 会议开始分

	private int endHour; // 会议结束小时

	private int endMinutes; // 会议结束分

	private String content; // 会议内容

	private String attachmentStr; // 会议附件

	private int authorId; // 发布人
	
	private String authorName; // 发布人姓名

	private int recordId; // 会议记录者
	
	private String recordName; // 会议记录者姓名

	private String summary; // 会议纪要

	private String summaryAttachStr; // 纪要附件
	
	private String[] attatchmentArray = {}; 	//附件路径
	
	private String[] summaryAttachArray = {}; 	//纪要附件路径
	
	private int[] AttendIds;		//会议参加者
	
	private String personNames;		//会议者姓名
	
	private String createTimeStr;       //创建时间
	
	private String demand;		// 会议要求



	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getAttachmentStr() {
		return attachmentStr;
	}

	public void setAttachmentStr(String attachmentStr) {
		this.attachmentStr = attachmentStr;
	}

	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}

	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}

	public int[] getAttendIds() {
		return AttendIds;
	}

	public void setAttendIds(int[] attendIds) {
		AttendIds = attendIds;
	}

	public String getAttendInfor() {
		return attendInfor;
	}

	public void setAttendInfor(String attendInfor) {
		this.attendInfor = attendInfor;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getCompere() {
		return compere;
	}

	public void setCompere(String compere) {
		this.compere = compere;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public Integer getEndHour() {
		return endHour;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	public Integer getEndMinutes() {
		return endMinutes;
	}

	public void setEndMinutes(Integer endMinutes) {
		this.endMinutes = endMinutes;
	}

	public String getMeetDate() {
		return meetDate;
	}

	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}

	public Integer getMeetId() {
		return meetId;
	}

	public void setMeetId(Integer meetId) {
		this.meetId = meetId;
	}

	public String getMeetName() {
		return meetName;
	}

	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}

	public String getMeetRoom() {
		return meetRoom;
	}

	public void setMeetRoom(String meetRoom) {
		this.meetRoom = meetRoom;
	}

	public int getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(int organizeId) {
		this.organizeId = organizeId;
	}

	public String getPersonNames() {
		return personNames;
	}

	public void setPersonNames(String personNames) {
		this.personNames = personNames;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getStartMinutes() {
		return startMinutes;
	}

	public void setStartMinutes(Integer startMinutes) {
		this.startMinutes = startMinutes;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummaryAttachStr() {
		return summaryAttachStr;
	}

	public void setSummaryAttachStr(String summaryAttachStr) {
		this.summaryAttachStr = summaryAttachStr;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getOrganizeName() {
		return organizeName;
	}

	public void setOrganizeName(String organizeName) {
		this.organizeName = organizeName;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public void setEndMinutes(int endMinutes) {
		this.endMinutes = endMinutes;
	}

	public void setMeetId(int meetId) {
		this.meetId = meetId;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public void setStartMinutes(int startMinutes) {
		this.startMinutes = startMinutes;
	}

	public String[] getSummaryAttachArray() {
		return summaryAttachArray;
	}

	public void setSummaryAttachArray(String[] summaryAttachArray) {
		this.summaryAttachArray = summaryAttachArray;
	}

	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

//	public String getEndMeetDate() {
//		return endMeetDate;
//	}
//
//	public void setEndMeetDate(String endMeetDate) {
//		this.endMeetDate = endMeetDate;
//	}

	
	
	
	
	
}