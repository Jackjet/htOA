package com.kwchina.extend.evaluationvote.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
@Entity
@Table(name = "PY_TopicInforRight", schema = "dbo")
public class PyTopicInforRight{
	
	  	private Integer rightId;
	    private PyTopicInfor topicInfor;//评优信息
	    private SystemUserInfor user;	//用户
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		public Integer getRightId() {
			return rightId;
		}
		public void setRightId(Integer rightId) {
			this.rightId = rightId;
		}
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="topicId", nullable = false)
		public PyTopicInfor getTopicInfor() {
			return topicInfor;
		}
		public void setTopicInfor(PyTopicInfor topicInfor) {
			this.topicInfor = topicInfor;
		}
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name="personId", nullable = false)
		public SystemUserInfor getUser() {
			return user;
		}
		public void setUser(SystemUserInfor user) {
			this.user = user;
		}
	    
	    

}