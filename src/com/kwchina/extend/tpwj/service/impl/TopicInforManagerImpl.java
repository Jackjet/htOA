package com.kwchina.extend.tpwj.service.impl;

import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.tpwj.dao.TopicInforDAO;
import com.kwchina.extend.tpwj.entity.TopicInfor;
import com.kwchina.extend.tpwj.entity.VoteInfor;
import com.kwchina.extend.tpwj.service.TopicInforManager;
import com.kwchina.extend.tpwj.vo.TopicInforVo;

@Service
public class TopicInforManagerImpl extends BasicManagerImpl<TopicInfor> implements TopicInforManager {

	private TopicInforDAO topicInforDAO;

	@Autowired
	public void setTopicInforDAO(TopicInforDAO topicInforDAO) {
		this.topicInforDAO = topicInforDAO;
		super.setDao(topicInforDAO);
	}
	
	
	// 转化MeetInfor为MeetInforVo
	public TopicInforVo transPOToVO(TopicInfor topic) {
		TopicInforVo topicInforVo = new TopicInforVo();

		try {
			BeanUtils.copyProperties(topicInforVo, topic);
			topicInforVo.setTopicId(topic.getTopicId());
			topicInforVo.setTopicName(topic.getTopicName());
			topicInforVo.setDescrip(topic.getDescrip());
			topicInforVo.setRules(topic.getRules());
			topicInforVo.setVoStartTime(topic.getStartTime().toString());
			topicInforVo.setVoEndTime(topic.getEndTime().toString());
			topicInforVo.setCreateTime(topic.getCreateTime().toString());
			topicInforVo.setValid(topic.isValid());
			topicInforVo.setOpenType(topic.getOpenType());
			topicInforVo.setType(topic.getType());
			
//			//是否是本投票的投票人
//			boolean isVoter = false;
//			Set<VoteInfor> voteInfors = topic.getVoteInfors();
//			for(VoteInfor voteInfor : voteInfors){
//				SystemUserInfor voter = voteInfor.getVoter();
//				if(voter.getPersonId() == systemUserInfor.getPersonId()){
//					isVoter = true;
//					break;
//				}
//			}
			
			
			
		} catch (Exception ex) {

		}

		return topicInforVo;
	}
	
}
