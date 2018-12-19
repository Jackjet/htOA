package com.kwchina.core.cms.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.cms.dao.InforCommentDAO;
import com.kwchina.core.cms.entity.InforComment;
import com.kwchina.core.cms.service.InforCommentManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service("inforCommentManager")
public class InforCommentManagerImpl extends BasicManagerImpl<InforComment> implements InforCommentManager{
	
	private InforCommentDAO inforCommentDAO;
	 
	//注入的方法
	@Autowired
	public void setInforCommentDAO(InforCommentDAO inforCommentDAO){
		this.inforCommentDAO = inforCommentDAO;
	    super.setDao(inforCommentDAO);
	}
	    
	

	 
}
