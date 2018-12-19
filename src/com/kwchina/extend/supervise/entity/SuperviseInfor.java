package com.kwchina.extend.supervise.entity;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
/**
 * 督办内容
 * @author suguan
 *
 */
@Entity
@Table(name = "Supervise_BaseInfor", schema = "dbo")
@ObjectId(id="taskId")
public class SuperviseInfor implements JSONNotAware{
	public static int Task_Status_SetCharger = 1;       //任务下达，待部门负责人指定负责人
	public static int Task_Status_Processed = 2;        //已指定，工作进行中
	public static int Task_Status_ToFinish = 3;         //已提交完成报告，部门审核中
	//public static int Task_Status_ManagerCheck = 4;     //
	public static int Task_Status_Check = 4;            //行政助理审核中
	public static int Task_Status_LeaderCheck = 5;      //领导审核中
	public static int Task_Status_Delay = 6;            //延迟工作进行中
	public static int Task_Status_Done = 7;             //已结束

    private Integer taskId;
    private String taskName;		//督办事项
    private String content;         //督办内容
    private Date createDate;        //下达时间
    private Date finishDate;        //要求完成时间
    private int status;             //状态1-任务下达，待部门负责人指定负责人 2-	已指定，工作进行中 3-已到期，完成报告制作中 4-行政助理审核中 6-领导审核中 7-延迟工作进行中  8-已结束
    
    private String workType;        //工作类别
    private String dutyDepartment;  //责任部门
    private String contactPerson;   //联系人
    private String reportPeriod;    //汇报周期

    private int isDeleted;          //是否删除 0-否，1-是
    private String attachment;      //附件
    
    private Date endTime;           //实际结束时间
    
    private int remindStatus;       //提醒类型，用于1-修改督办内容（未改部门）时，提示部门负责人内容已改变，2-修改责任部门时，提示原部门负责人任务已修改
    private int readStatus;         //提醒是否已读
    private Date delayDate;        //延迟时间
    
    private Date delayDate2;        //延迟时间2
    private Date delayDate3;        //延迟时间3



    private String memo;           //备注
    
    private TaskCategory taskCategory; //类别
    private SystemUserInfor creater; //创建者
    private SystemUserInfor leader;  //下达任务的领导
    private OrganizeInfor organizeInfor;  //责任部门（改为执行部门）
    private SystemUserInfor manager;   //责任部门经理
    private SystemUserInfor operator;       //责任人

    private SystemUserInfor formerManager;   //原责任部门经理
	@Column(name = "nameScore", columnDefinition = "nvarchar(400)")
	private String nameScore; //打分

    private Set<SuperviseReport> reports = new HashSet<SuperviseReport>(0);    //进度及过程报告
    private Set<TaskLeader> leaders = new HashSet<TaskLeader>(0);    //负责打分的领导

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getTaskId() {
        return this.taskId;
    }
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
    
    
    @Column(columnDefinition = "nvarchar(400)", nullable = false)
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId")
	public TaskCategory getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(TaskCategory taskCategory) {
		this.taskCategory = taskCategory;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createrId")
	public SystemUserInfor getCreater() {
		return creater;
	}

	public void setCreater(SystemUserInfor creater) {
		this.creater = creater;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="leaderId")
	public SystemUserInfor getLeader() {
		return leader;
	}

	public void setLeader(SystemUserInfor leader) {
		this.leader = leader;
	}
	
	@Column(columnDefinition = "ntext", nullable = true)
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
	public OrganizeInfor getOrganizeInfor() {
		return organizeInfor;
	}
	public void setOrganizeInfor(OrganizeInfor organizeInfor) {
		this.organizeInfor = organizeInfor;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managerId")
	public SystemUserInfor getManager() {
		return manager;
	}
	public void setManager(SystemUserInfor manager) {
		this.manager = manager;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operatorId")
	public SystemUserInfor getOperator() {
		return operator;
	}
	public void setOperator(SystemUserInfor operator) {
		this.operator = operator;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@OneToMany(mappedBy = "superviseInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("reportId")
	public Set<SuperviseReport> getReports() {
		return reports;
	}
	public void setReports(Set<SuperviseReport> reports) {
		this.reports = reports;
	}
	
	@OneToMany(mappedBy = "superviseInfor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@OrderBy("tlId")
	public Set<TaskLeader> getLeaders() {
		return leaders;
	}
	public void setLeaders(Set<TaskLeader> leaders) {
		this.leaders = leaders;
	}
	
	
	public int getRemindStatus() {
		return remindStatus;
	}
	public void setRemindStatus(int remindStatus) {
		this.remindStatus = remindStatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formerManagerId")
	public SystemUserInfor getFormerManager() {
		return formerManager;
	}
	public void setFormerManager(SystemUserInfor formerManager) {
		this.formerManager = formerManager;
	}
	public int getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
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
	public Date getDelayDate2() {
		return delayDate2;
	}
	public void setDelayDate2(Date delayDate2) {
		this.delayDate2 = delayDate2;
	}
	public Date getDelayDate3() {
		return delayDate3;
	}
	public void setDelayDate3(Date delayDate3) {
		this.delayDate3 = delayDate3;
	}
	
	@Column(columnDefinition = "nvarchar(400)")
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}

	@Column(columnDefinition = "nvarchar(400)", nullable = true)
	public String getDutyDepartment() {
		return dutyDepartment;
	}
	public void setDutyDepartment(String dutyDepartment) {
		this.dutyDepartment = dutyDepartment;
	}
	

	@Column(columnDefinition = "nvarchar(400)")
	public String getReportPeriod() {
		return reportPeriod;
	}
	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}
	
	@Column(columnDefinition = "nvarchar(400)")
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}




	public String getNameScore() {
		return nameScore;
	}
	public void setNameScore(String nameScore) {
		this.nameScore = nameScore;
	}
}


