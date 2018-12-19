package com.kwchina.extend.tpwj.entity;


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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


import com.kwchina.core.base.entity.SystemUserInfor;

/**
 * 主题权限信息
 * @author suguan
 *
 */
@Entity
@Table(name = "TP_TopicRight", schema = "dbo")
public class TopicRight {

    private Integer rightId;
    
    private TopicInfor topic;
    private SystemUserInfor systemUser;		//用户信息
    


    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getRightId() {
        return this.rightId;
    }
    
    public void setRightId(Integer rightId) {
        this.rightId = rightId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
	public TopicInfor getTopic() {
		return topic;
	}

	public void setTopic(TopicInfor topic) {
		this.topic = topic;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
	public SystemUserInfor getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUserInfor systemUser) {
		this.systemUser = systemUser;
	}

    

}


