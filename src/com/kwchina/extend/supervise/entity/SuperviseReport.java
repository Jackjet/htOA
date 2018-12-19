package com.kwchina.extend.supervise.entity;

import java.sql.Date;
import java.sql.Timestamp;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;
/**
 * 任务报告
 * @author suguan
 *
 */
@Entity
@Table(name = "Supervise_TaskReport", schema = "dbo")
public class SuperviseReport {
	public static int Report_Type_Ing = 1;     //进度报告
	public static int Report_Type_Done = 2;    //完成报告
	public static int Report_Type_Delay = 3;   //延期报告
	public static int Report_Type_Check = 4;   //行政/党群意见
	public static int Report_Type_Leader = 5;  //领导意见
	public static int Report_Type_CheckAdvice = 6;  //行政是否满意意见

    private Integer reportId;
    private int reportType;      //报告类型 1-进度报告 2-完成报告 3-延期报告  4-行政/党群意见 5-领导意见  6-行政是否满意意见
    private String content;      //报告内容
    private String managerAdvice;  //部门负责人意见
    private int isPassed;          //是否通过 0-否，1-是
    private Timestamp operateDate;      //操作时间
    private Timestamp createDate;    //创建时间
    private Timestamp checkDate;     //部门经理操作时间
    private Date delayDate;        //延迟时间
    private String memo;           //备注
    private int isDeleted;         //是否删除 0-否，1-是
    private String attachment;     //附件 
    
    private int isDone;            //此层报告是否结束（不再有往复，针对进度报告及完成报告的部门审核）
    private int isJudgePassed;       //预判是否通过（针对完成报告）
    private int isJudged;          //预判是否完成（针对完成报告）
    private Timestamp judgeDate;     //助理预判时间
    private int score;      //所打分数
    
    private SuperviseInfor superviseInfor;  //所属督办任务
    private SystemUserInfor operator;       //责任人
    
    private Set<TaskLog> logs = new HashSet<TaskLog>(0);    //修改记录
//    private Set<TaskLeader> leaders = new HashSet<TaskLeader>(0);    //本次领导
    
    private SuperviseReport parent;	//父分类
    private Set<SuperviseReport> childs = new HashSet<SuperviseReport>(0);
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getManagerAdvice() {
		return managerAdvice;
	}
	public void setManagerAdvice(String managerAdvice) {
		this.managerAdvice = managerAdvice;
	}
	
	public int getIsPassed() {
		return isPassed;
	}
	public void setIsPassed(int isPassed) {
		this.isPassed = isPassed;
	}
	
	public Timestamp getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Timestamp operateDate) {
		this.operateDate = operateDate;
	}
	
	public Date getDelayDate() {
		return delayDate;
	}
	public void setDelayDate(Date delayDate) {
		this.delayDate = delayDate;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="taskId")
	public SuperviseInfor getSuperviseInfor() {
		return superviseInfor;
	}
	public void setSuperviseInfor(SuperviseInfor superviseInfor) {
		this.superviseInfor = superviseInfor;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operatorId")
	public SystemUserInfor getOperator() {
		return operator;
	}
	public void setOperator(SystemUserInfor operator) {
		this.operator = operator;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	@OneToMany(mappedBy = "superviseReport",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("logId")
	public Set<TaskLog> getLogs() {
		return logs;
	}
	public void setLogs(Set<TaskLog> logs) {
		this.logs = logs;
	}
	public Timestamp getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Timestamp checkDate) {
		this.checkDate = checkDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
	public SuperviseReport getParent() {
		return parent;
	}
	public void setParent(SuperviseReport parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy = "parent",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @OrderBy("reportId")
	public Set<SuperviseReport> getChilds() {
		return childs;
	}
	public void setChilds(Set<SuperviseReport> childs) {
		this.childs = childs;
	}
	public int getIsDone() {
		return isDone;
	}
	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}
	public int getIsJudged() {
		return isJudged;
	}
	public void setIsJudged(int isJudged) {
		this.isJudged = isJudged;
	}
	public Timestamp getJudgeDate() {
		return judgeDate;
	}
	public void setJudgeDate(Timestamp judgeDate) {
		this.judgeDate = judgeDate;
	}
	public int getIsJudgePassed() {
		return isJudgePassed;
	}
	public void setIsJudgePassed(int isJudgePassed) {
		this.isJudgePassed = isJudgePassed;
	}
	
	
//	@OneToMany(mappedBy = "report",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@OrderBy("tlId")
//	public Set<TaskLeader> getLeaders() {
//		return leaders;
//	}
//	public void setLeaders(Set<TaskLeader> leaders) {
//		this.leaders = leaders;
//	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
    

}


