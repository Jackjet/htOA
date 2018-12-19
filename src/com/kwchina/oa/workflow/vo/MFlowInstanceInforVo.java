package com.kwchina.oa.workflow.vo;

import java.util.List;

import com.kwchina.oa.workflow.entity.InstanceLayerInfor;




public class MFlowInstanceInforVo {

	private Integer instanceId; 				// 审核实例Id
	private String instanceTitle; 				// 实例名称
	private String updateTimeStr;				// 创建时间
	private String startTimeStr; 				// 开始时间
	private String endTimeStr; 					// 结束时间
	
	private String attach; 						// 申请人提交的附件
	private String formalAttach; 				// 最终正式文件
	private String contentPath; 				// 正文对应的html表单路径
	
	private int applierId;                      //申请人ID
	private String applierName;                 //申请人姓名
	private int departmentId;                   //所属部门ID
	private String departmentName;              //所属部门名称
	private int flowId;                         //所属流程ID
	private String flowName;                    //所属流程名称
	
	private int chargerId;                      //主办人ID
	private String chargerName;                    //主办人姓名
	private String submiterWord;				// 提交人意见(中止核稿)

	private int managerId;
	private String managerName;                 
    private String managerWord;					// 审核人一的审核意见
    private String checkTimeStr;				// 审核人一的审核时间
    private boolean managerChecked; 			// 审核人一是否审核
    private String managerAttachment;			// 审核人一的附件
    

    private int viceManagerId; 
    private String viceManagerName;
    private String viceManagerWord;				// 审核人二的审核意见
    private String viceCheckTimeStr;			// 审核人二的审核时间
    private boolean viceManagerChecked;			// 审核人二是否审核
    private String viceManagerAttachment;		// 审核人二的附件
	
    //董事会文件相关字段
    private String resAttach;					// 决议附件
    private String attachMemo;					// 备注
    
    //合同相关字段
    private int stamped;					// 是否盖章
    
    //行政发文相关字段
    private boolean handOut;              //是否下发给分公司为收文
    private boolean filed;						// 是否归档：0-否;1-是.
    private Integer oldInstanceId; 				// 废止审核实例Id	
    
    private List<MLayerInforVo> mLayers;          //审核层
    
	public List<MLayerInforVo> getMLayers() {
		return mLayers;
	}
	public void setMLayers(List<MLayerInforVo> layers) {
		mLayers = layers;
	}
	public Integer getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstanceTitle() {
		return instanceTitle;
	}
	public void setInstanceTitle(String instanceTitle) {
		this.instanceTitle = instanceTitle;
	}
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getFormalAttach() {
		return formalAttach;
	}
	public void setFormalAttach(String formalAttach) {
		this.formalAttach = formalAttach;
	}
	public String getContentPath() {
		return contentPath;
	}
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	public int getApplierId() {
		return applierId;
	}
	public void setApplierId(int applierId) {
		this.applierId = applierId;
	}
	public String getApplierName() {
		return applierName;
	}
	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public int getFlowId() {
		return flowId;
	}
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public int getChargerId() {
		return chargerId;
	}
	public void setChargerId(int chargerId) {
		this.chargerId = chargerId;
	}
	public String getChargerName() {
		return chargerName;
	}
	public void setChargerName(String chargerName) {
		this.chargerName = chargerName;
	}
	public String getSubmiterWord() {
		return submiterWord;
	}
	public void setSubmiterWord(String submiterWord) {
		this.submiterWord = submiterWord;
	}
	public int getManagerId() {
		return managerId;
	}
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerWord() {
		return managerWord;
	}
	public void setManagerWord(String managerWord) {
		this.managerWord = managerWord;
	}
	public String getCheckTimeStr() {
		return checkTimeStr;
	}
	public void setCheckTimeStr(String checkTimeStr) {
		this.checkTimeStr = checkTimeStr;
	}
	public boolean isManagerChecked() {
		return managerChecked;
	}
	public void setManagerChecked(boolean managerChecked) {
		this.managerChecked = managerChecked;
	}
	public String getManagerAttachment() {
		return managerAttachment;
	}
	public void setManagerAttachment(String managerAttachment) {
		this.managerAttachment = managerAttachment;
	}
	public int getViceManagerId() {
		return viceManagerId;
	}
	public void setViceManagerId(int viceManagerId) {
		this.viceManagerId = viceManagerId;
	}
	public String getViceManagerName() {
		return viceManagerName;
	}
	public void setViceManagerName(String viceManagerName) {
		this.viceManagerName = viceManagerName;
	}
	public String getViceManagerWord() {
		return viceManagerWord;
	}
	public void setViceManagerWord(String viceManagerWord) {
		this.viceManagerWord = viceManagerWord;
	}
	public String getViceCheckTimeStr() {
		return viceCheckTimeStr;
	}
	public void setViceCheckTimeStr(String viceCheckTimeStr) {
		this.viceCheckTimeStr = viceCheckTimeStr;
	}
	public boolean isViceManagerChecked() {
		return viceManagerChecked;
	}
	public void setViceManagerChecked(boolean viceManagerChecked) {
		this.viceManagerChecked = viceManagerChecked;
	}
	public String getViceManagerAttachment() {
		return viceManagerAttachment;
	}
	public void setViceManagerAttachment(String viceManagerAttachment) {
		this.viceManagerAttachment = viceManagerAttachment;
	}
	public String getResAttach() {
		return resAttach;
	}
	public void setResAttach(String resAttach) {
		this.resAttach = resAttach;
	}
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
	public boolean isHandOut() {
		return handOut;
	}
	public void setHandOut(boolean handOut) {
		this.handOut = handOut;
	}
	public boolean isFiled() {
		return filed;
	}
	public void setFiled(boolean filed) {
		this.filed = filed;
	}
	public Integer getOldInstanceId() {
		return oldInstanceId;
	}
	public void setOldInstanceId(Integer oldInstanceId) {
		this.oldInstanceId = oldInstanceId;
	}
	
}
