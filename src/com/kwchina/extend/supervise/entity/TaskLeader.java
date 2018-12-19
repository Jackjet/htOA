package com.kwchina.extend.supervise.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;
/**
 * 负责打分的领导
 * @author suguan
 *
 */
@Entity
@Table(name = "Supervise_TaskLeader", schema = "dbo")
public class TaskLeader {

    private Integer tlId;
//    private int score;      //所打分数
    private Timestamp operateDate;    //操作时间
    
    private SuperviseInfor superviseInfor;  //所属督办
    private SystemUserInfor leader;       //领导
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTlId() {
		return tlId;
	}
	public void setTlId(Integer tlId) {
		this.tlId = tlId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
	public SuperviseInfor getSuperviseInfor() {
		return superviseInfor;
	}
	public void setSuperviseInfor(SuperviseInfor superviseInfor) {
		this.superviseInfor = superviseInfor;
	}
	
	
//	public int getScore() {
//		return score;
//	}
//	public void setScore(int score) {
//		this.score = score;
//	}
	public Timestamp getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Timestamp operateDate) {
		this.operateDate = operateDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId")
	public SystemUserInfor getLeader() {
		return leader;
	}
	public void setLeader(SystemUserInfor leader) {
		this.leader = leader;
	}
	

    

}


