package com.kwchina.core.cms.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.cms.dao.InforPraiseDAO;
import com.kwchina.core.cms.entity.InforPraise;
import com.kwchina.core.cms.service.InforPraiseManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service("inforPraiseManager")
public class InforPraiseManagerImpl extends BasicManagerImpl<InforPraise> implements InforPraiseManager{
	
	private InforPraiseDAO inforPraiseDAO;
	 
	//注入的方法
	@Autowired
	public void setInforPraiseDAO(InforPraiseDAO inforPraiseDAO){
		this.inforPraiseDAO = inforPraiseDAO;
	    super.setDao(inforPraiseDAO);
	}
	    
	

	 
}
