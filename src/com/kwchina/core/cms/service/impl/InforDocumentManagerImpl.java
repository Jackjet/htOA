package com.kwchina.core.cms.service.impl;



import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.dao.InforDocumentDAO;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.sys.SystemConstant;

@Service("inforDocumentManager")
public class InforDocumentManagerImpl extends BasicManagerImpl<InforDocument> implements InforDocumentManager{
	
	private InforDocumentDAO inforDocumentDAO;
	 
	//注入的方法
	@Autowired
	public void setInforDocumentDAO(InforDocumentDAO inforDocumentDAO){
		this.inforDocumentDAO = inforDocumentDAO;
	    super.setDao(inforDocumentDAO);
	}
	    
	//判断权限
	public boolean judgeRight(InforDocument inforDocument,SystemUserInfor user){
		boolean hasRight = false;
		int userId = user.getPersonId().intValue();
		int authorId = inforDocument.getAuthor().getPersonId().intValue();
			
		if (userId == authorId || user.getUserType() == SystemConstant._User_Type_Admin) {
			//只有作者和管理员才有权限
			hasRight = true;
		}	
			
		return hasRight;
	}
	

	 
}
