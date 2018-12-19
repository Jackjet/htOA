package com.kwchina.extend.evaluationvote.vo;

public class PyTopicInforVo {

	private Integer topicId=0;
	private String topicName; //投票标题
	private String descrip;//描述
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String createTime;//创建时间
	private int personId;//创建者
	private boolean isValid;//有效性 0-无效 1-有效
	private String ruler;//投票规则
	private int openType;	
    private int[] personIds;
    private int voterId;
    
  
    private boolean homepage;              //是否首页公示  0-否，1-是
    private boolean  sameDept;
    
	public boolean isSameDept() {
		return sameDept;
	}
	public void setSameDept(boolean sameDept) {
		this.sameDept = sameDept;
	}

	public int getVoterId() {
		return voterId;
	}
	public void setVoterId(int voterId) {
		this.voterId = voterId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}
	public String getRuler() {
		return ruler;
	}
	public void setRuler(String ruler) {
		this.ruler = ruler;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Integer getTopicId() {
		return topicId;
	}
	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public boolean isHomepage() {
		return homepage;
	}
	public void setHomepage(boolean homepage) {
		this.homepage = homepage;
	}	
	
	
}