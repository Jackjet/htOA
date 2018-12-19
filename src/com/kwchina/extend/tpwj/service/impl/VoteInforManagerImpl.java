package com.kwchina.extend.tpwj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.tpwj.dao.VoteInforDAO;
import com.kwchina.extend.tpwj.entity.VoteInfor;
import com.kwchina.extend.tpwj.service.VoteInforManager;

@Service
public class VoteInforManagerImpl extends BasicManagerImpl<VoteInfor> implements VoteInforManager {

	private VoteInforDAO voteInforDAO;

	@Autowired
	public void setVoteInforDAO(VoteInforDAO voteInforDAO) {
		this.voteInforDAO = voteInforDAO;
		super.setDao(voteInforDAO);
	}
	
	
	
}
