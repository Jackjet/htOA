package com.kwchina.extend.supervise.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
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
 * 报告内容修改记录
 * @author suguan
 *
 */
@Entity
@Table(name = "Supervise_TaskLog", schema = "dbo")
public class TaskLog {

    private Integer logId;
    private String logContent;   //修改内容
    private Timestamp operateDate;    //操作时间
    
    private SuperviseReport superviseReport;  //所属报告
    private SystemUserInfor operator;       //责任人
    
    public TaskLog(){}
    public TaskLog(String logContent,Timestamp operateDate,SuperviseReport report,SystemUserInfor operator) {
		 this.logContent = logContent;
		 this.operateDate = operateDate;
		 this.superviseReport = report;
		 this.operator = operator;
	}
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId")
	public SuperviseReport getSuperviseReport() {
		return superviseReport;
	}
	public void setSuperviseReport(SuperviseReport superviseReport) {
		this.superviseReport = superviseReport;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operatorId")
	public SystemUserInfor getOperator() {
		return operator;
	}
	public void setOperator(SystemUserInfor operator) {
		this.operator = operator;
	}
	
	public Timestamp getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Timestamp operateDate) {
		this.operateDate = operateDate;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	

    

}


