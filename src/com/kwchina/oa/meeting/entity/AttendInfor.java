package com.kwchina.oa.meeting.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Infor_Meet_AttendInfor", schema = "dbo")
public class AttendInfor {
	private Integer attendId; // 数据Id

	private MeetInfor meet;// 会议Id

	private SystemUserInfor person;// 参加人员Id

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getAttendId() {
		return attendId;
	}

	public void setAttendId(Integer attendId) {
		this.attendId = attendId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meetId", nullable = false)
	public MeetInfor getMeet() {
		return meet;
	}

	public void setMeet(MeetInfor meet) {
		this.meet = meet;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId", nullable = false)
	public SystemUserInfor getPerson() {
		return person;
	}

	public void setPerson(SystemUserInfor person) {
		this.person = person;
	}

}