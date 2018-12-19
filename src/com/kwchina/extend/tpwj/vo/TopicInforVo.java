package com.kwchina.extend.tpwj.vo;


public class TopicInforVo {

	private Integer topicId;
    private String topicName;		    //投票标题
    private String descrip;		        //描述
    private String rules;                //投票规则
    private String voStartTime;			    //开始时间
    private String voEndTime;               //结束时间
    private String createTime;            //创建时间
    private boolean valid;			        //是否有效:0-无效;1-有效.
    private boolean checkCount;         //是否允许投票者查看统计结果
    private int openType;				//开放类型:0-自定义;1-全体用户.
    
    private boolean voter;           //是否是本投票的投票人
    
    private int voterId;         //投票者
    
    private int type;           //类型  0-投票 1-问卷
    
    private boolean display;              //是否首页公示  0-否，1-是
    
    private int createrId;	//创建者
    private int[] personIds;
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
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public int getCreaterId() {
		return createrId;
	}
	public void setCreaterId(int createrId) {
		this.createrId = createrId;
	}
	public int[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public int getVoterId() {
		return voterId;
	}
	public void setVoterId(int voterId) {
		this.voterId = voterId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getVoStartTime() {
		return voStartTime;
	}
	public void setVoStartTime(String voStartTime) {
		this.voStartTime = voStartTime;
	}
	public String getVoEndTime() {
		return voEndTime;
	}
	public void setVoEndTime(String voEndTime) {
		this.voEndTime = voEndTime;
	}
	public boolean isVoter() {
		return voter;
	}
	public void setVoter(boolean voter) {
		this.voter = voter;
	}
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public boolean isCheckCount() {
		return checkCount;
	}
	public void setCheckCount(boolean checkCount) {
		this.checkCount = checkCount;
	}
    
    
}
