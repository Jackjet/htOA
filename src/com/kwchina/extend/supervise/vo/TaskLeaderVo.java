package com.kwchina.extend.supervise.vo;

public class TaskLeaderVo {
	private Integer tlId;
	private int score;
	private Integer taskId;
	private int[] personIds; // 打分领导

	public Integer getTlId() {
		return tlId;
	}

	public void setTlId(Integer tlId) {
		this.tlId = tlId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public int[] getPersonIds() {
		return personIds;
	}

	public void setPersonIds(int[] personIds) {
		this.personIds = personIds;
	}

}
