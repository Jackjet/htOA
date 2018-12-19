package com.kwchina.extend.tpwj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.tpwj.dao.VoteItemInforDAO;
import com.kwchina.extend.tpwj.entity.VoteItemInfor;
import com.kwchina.extend.tpwj.service.VoteItemInforManager;

@Service
public class VoteItemInforManagerImpl extends BasicManagerImpl<VoteItemInfor> implements VoteItemInforManager {

	private VoteItemInforDAO voteItemInforDAO;

	@Autowired
	public void setVoteItemInforDAO(VoteItemInforDAO voteItemInforDAO) {
		this.voteItemInforDAO = voteItemInforDAO;
		super.setDao(voteItemInforDAO);
	}
	
	
	
}
