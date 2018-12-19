package com.kwchina.core.cms.service;


import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.personal.address.entity.PersonalAddress;

public interface InforDocumentManager extends BasicManager{
	
	//判断权限
	public boolean judgeRight(InforDocument inforDocument,SystemUserInfor user);
	


	
}
