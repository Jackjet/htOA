package com.kwchina.oa.workflow.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.json.JSONNotAware;


@Entity
@Table(name = "Workflow_InstanceInfor", schema = "dbo")
public class FlowInstanceInfor implements JSONNotAware{

	private Integer instanceId; 				// 审核实例Id

	private String instanceTitle; 				// 实例名称
	private Timestamp updateTime;				// 创建时间
	private Timestamp startTime; 				// 开始时间
	private Timestamp endTime; 					// 结束时间

	private String attach; 						// 申请人提交的附件
	private String formalAttach; 				// 最终正式文件
	private String contentPath; 				// 正文对应的html表单路径
	private boolean suspended; 					// 是否暂停(0-否 1-是)
	private String suspendedReason;             // 新增字段 --- 暂停理由
	private int deleteFlag; 					// 删除标志
	private int lastNode; 						// 已执行的最后一个预设节点

	private SystemUserInfor applier; 			// 申请人
	private OrganizeInfor department; 			// 所属部门
	private FlowDefinition flowDefinition; 		// 所属流程
	
	private SystemUserInfor charger;			// 主办人
	
    //部门审核相关字段
    private String managerWord;					// 审核人一的审核意见
    private Timestamp checkTime;				// 审核人一的审核时间
    private boolean managerChecked; 			// 审核人一是否审核
    private String managerAttachment;			// 审核人一的附件
    
    private String viceManagerWord;				// 审核人二的审核意见
    private Timestamp viceCheckTime;			// 审核人二的审核时间
    private boolean viceManagerChecked;			// 审核人二是否审核
    private String viceManagerAttachment;		// 审核人二的附件
        
    private SystemUserInfor manager;			// 审核人一
    private SystemUserInfor viceManager;		// 审核人二
    private SystemUserInfor modifyer;			//修改人
 
    private String submiterWord;				// 提交人意见(中止核稿)
    //-------------
    
    //董事会文件相关字段
    private String resAttach;					// 决议附件
    private String attachMemo;					// 备注
    //-------------
    
    //合同相关字段
    private int stamped;					// 是否盖章
    //-------------
    
    //行政发文相关字段
    private boolean handOut;              //是否下发给分公司为收文
    
    private boolean filed;						// 是否归档：0-否;1-是.
    
    private Integer oldInstanceId; 				// 废止审核实例Id
	
	private Set<InstanceToken> tokens = new HashSet<InstanceToken>(0);								//Token信息
	private Set<InstanceLayerInfor> layers = new HashSet<InstanceLayerInfor>(0);					//审核层次信息
	private Set<InstanceTransitionInfor> transitions = new HashSet<InstanceTransitionInfor>(0);		//Transition信息
	private Set<InstanceInforRight> rights = new HashSet<InstanceInforRight>(0);


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}
	
	@Column(columnDefinition = "nvarchar(800)")
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	@Column(columnDefinition = "nvarchar(500)")
	public String getContentPath() {
		return contentPath;
	}
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	
	
	@Column(columnDefinition = "nvarchar(2000)")
	public String getSuspendedReason() {
		return suspendedReason;
	}
	public void setSuspendedReason(String suspendedReason) {
		this.suspendedReason = suspendedReason;
	}
	
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	@Column(columnDefinition = "nvarchar(500)")
	public String getFormalAttach() {
		return formalAttach;
	}
	public void setFormalAttach(String formalAttach) {
		this.formalAttach = formalAttach;
	}
	
	@Column(columnDefinition = "nvarchar(200)",nullable =false)
	public String getInstanceTitle() {
		return instanceTitle;
	}
	public void setInstanceTitle(String instanceTitle) {
		this.instanceTitle = instanceTitle;
	}
	
	public int getLastNode() {
		return lastNode;
	}
	public void setLastNode(int lastNode) {
		this.lastNode = lastNode;
	}
	
	@Column(nullable =false)
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	//@Column(nullable =false)
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="applier")
	public SystemUserInfor getApplier() {
		return applier;
	}
	public void setApplier(SystemUserInfor applier) {
		this.applier = applier;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="department")
	public OrganizeInfor getDepartment() {
		return department;
	}
	public void setDepartment(OrganizeInfor department) {
		this.department = department;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flowId")
	public FlowDefinition getFlowDefinition() {
		return flowDefinition;
	}
	public void setFlowDefinition(FlowDefinition flowDefinition) {
		this.flowDefinition = flowDefinition;
	}

	@OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
	public Set<InstanceToken> getTokens() {
		return tokens;
	}
	public void setTokens(Set<InstanceToken> tokens) {
		this.tokens = tokens;
	}
	
	@OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
	@OrderBy("layer")
	public Set<InstanceLayerInfor> getLayers() {
		return layers;
	}
	public void setLayers(Set<InstanceLayerInfor> layers) {
		this.layers = layers;
	}
	
	@OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)	
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
	public Set<InstanceTransitionInfor> getTransitions() {
		return transitions;
	}
	public void setTransitions(Set<InstanceTransitionInfor> transitions) {
		this.transitions = transitions;
	}
	public Timestamp getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="managerId")
	public SystemUserInfor getManager() {
		return manager;
	}
	public void setManager(SystemUserInfor manager) {
		this.manager = manager;
	}
	
	@Column(columnDefinition = "nvarchar(500)")
	public String getManagerAttachment() {
		return managerAttachment;
	}
	public void setManagerAttachment(String managerAttachment) {
		this.managerAttachment = managerAttachment;
	}
	public boolean isManagerChecked() {
		return managerChecked;
	}
	public void setManagerChecked(boolean managerChecked) {
		this.managerChecked = managerChecked;
	}
	
	@Column(columnDefinition = "nText")
	public String getManagerWord() {
		return managerWord;
	}
	public void setManagerWord(String managerWord) {
		this.managerWord = managerWord;
	}
	
	@Column(columnDefinition = "nvarchar(200)")
	public String getSubmiterWord() {
		return submiterWord;
	}
	public void setSubmiterWord(String submiterWord) {
		this.submiterWord = submiterWord;
	}
	public Timestamp getViceCheckTime() {
		return viceCheckTime;
	}
	public void setViceCheckTime(Timestamp viceCheckTime) {
		this.viceCheckTime = viceCheckTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="viceManagerId")
	public SystemUserInfor getViceManager() {
		return viceManager;
	}
	public void setViceManager(SystemUserInfor viceManager) {
		this.viceManager = viceManager;
	}
	
	@Column(columnDefinition = "nvarchar(500)")
	public String getViceManagerAttachment() {
		return viceManagerAttachment;
	}
	public void setViceManagerAttachment(String viceManagerAttachment) {
		this.viceManagerAttachment = viceManagerAttachment;
	}
	public boolean isViceManagerChecked() {
		return viceManagerChecked;
	}
	public void setViceManagerChecked(boolean viceManagerChecked) {
		this.viceManagerChecked = viceManagerChecked;
	}
	
	@Column(columnDefinition = "nText")
	public String getViceManagerWord() {
		return viceManagerWord;
	}
	public void setViceManagerWord(String viceManagerWord) {
		this.viceManagerWord = viceManagerWord;
	}
	public boolean isFiled() {
		return filed;
	}
	public void setFiled(boolean filed) {
		this.filed = filed;
	}
	
	@Column(columnDefinition = "nvarchar(500)")
	public String getResAttach() {
		return resAttach;
	}
	public void setResAttach(String resAttach) {
		this.resAttach = resAttach;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="chargerId",nullable = false)
	public SystemUserInfor getCharger() {
		return charger;
	}
	public void setCharger(SystemUserInfor charger) {
		this.charger = charger;
	}
	
	@Column(columnDefinition = "nText")
	public String getAttachMemo() {
		return attachMemo;
	}
	public void setAttachMemo(String attachMemo) {
		this.attachMemo = attachMemo;
	}

	
	public int getStamped() {
		return stamped;
	}
	public void setStamped(int stamped) {
		this.stamped = stamped;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="modifyerId")
	public SystemUserInfor getModifyer() {
		return modifyer;
	}
	public void setModifyer(SystemUserInfor modifyer) {
		this.modifyer = modifyer;
	}
	
	@OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<InstanceInforRight> getRights() {
		return rights;
	}

	public void setRights(Set<InstanceInforRight> rights) {
		this.rights = rights;
	}
	public Integer getOldInstanceId() {
		return oldInstanceId;
	}
	public void setOldInstanceId(Integer oldInstanceId) {
		this.oldInstanceId = oldInstanceId;
	}
	public boolean isHandOut() {
		return handOut;
	}
	public void setHandOut(boolean handOut) {
		this.handOut = handOut;
	}

	
}
