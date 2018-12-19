package com.kwchina.oa.meeting.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.meeting.entity.AttendInfor;

@Entity
@Table(name = "Infor_Meet_MeetInfor", schema = "dbo")
public class MeetInfor {
	private Integer meetId; // 会议Id

	private String meetName; // 会议名称

	private OrganizeInfor organize; // 责任部室

	private String compere; // 主持人

	private String attendInfor; // 参加人员

	private String meetRoom; // 会议室

	private SystemUserInfor bookPerson; // 预定人

	private Date meetDate; // 会议日期
	
	private Date endMeetDate;	//会议结束日期

	private Integer startHour; // 会议开始小时

	private Integer startMinutes; // 会议开始分

	private Integer endHour; // 会议结束小时

	private Integer endMinutes; // 会议结束分

	private String content; // 会议内容

	private String attachment; // 会议附件

	private SystemUserInfor author; // 发布人

	private Date createTime; // 发布时间

	private SystemUserInfor recordUser; // 会议记录者

	private String summary; // 会议日志

	private String summaryAttach; // 日志附件
	
	private String demand;		// 会议要求

	private Set<AttendInfor> attendInfors = new HashSet<AttendInfor>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getMeetId() {
		return meetId;
	}

	public void setMeetId(Integer meetId) {
		this.meetId = meetId;
	}

	@Column(columnDefinition = "nvarchar(2000)")
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Column(columnDefinition = "nvarchar(500)")
	public String getAttendInfor() {
		return attendInfor;
	}

	public void setAttendInfor(String attendInfor) {
		this.attendInfor = attendInfor;
	}

	@OneToMany(mappedBy = "meet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Cascade(value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN,
			org.hibernate.annotations.CascadeType.ALL })
	public Set<AttendInfor> getAttendInfors() {
		return attendInfors;
	}

	public void setAttendInfors(Set<AttendInfor> attendInfors) {
		this.attendInfors = attendInfors;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authorId", nullable = false)
	public SystemUserInfor getAuthor() {
		return author;
	}

	public void setAuthor(SystemUserInfor author) {
		this.author = author;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookId")
	public SystemUserInfor getBookPerson() {
		return bookPerson;
	}

	public void setBookPerson(SystemUserInfor bookPerson) {
		this.bookPerson = bookPerson;
	}

	@Column(columnDefinition = "nvarchar(80)")
	public String getCompere() {
		return compere;
	}

	public void setCompere(String compere) {
		this.compere = compere;
	}

	@Column(columnDefinition = "ntext")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getMeetDate() {
		return meetDate;
	}

	public void setMeetDate(Date meetDate) {
		this.meetDate = meetDate;
	}
	
	@Column(columnDefinition = "nvarchar(200)",nullable = false)
	public String getMeetName() {
		return meetName;
	}

	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}
	
	@Column(columnDefinition = "nvarchar(100)",nullable = false)
	public String getMeetRoom() {
		return meetRoom;
	}

	public void setMeetRoom(String meetRoom) {
		this.meetRoom = meetRoom;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizeId", nullable = true)
	public OrganizeInfor getOrganize() {
		return organize;
	}

	public void setOrganize(OrganizeInfor organize) {
		this.organize = organize;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recordId", nullable = true)
	public SystemUserInfor getRecordUser() {
		return recordUser;
	}

	public void setRecordUser(SystemUserInfor recordUser) {
		this.recordUser = recordUser;
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

	@Column(columnDefinition = "ntext")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Column(columnDefinition = "nvarchar(2000)")
	public String getSummaryAttach() {
		return summaryAttach;
	}

	public void setSummaryAttach(String summaryAttach) {
		this.summaryAttach = summaryAttach;
	}

	public Date getEndMeetDate() {
		return endMeetDate;
	}

	public void setEndMeetDate(Date endMeetDate) {
		this.endMeetDate = endMeetDate;
	}

	@Column(columnDefinition = "ntext")
	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

}