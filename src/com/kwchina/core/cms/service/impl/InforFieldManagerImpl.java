package com.kwchina.core.cms.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.cms.dao.InforFieldDAO;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.cms.service.InforFieldManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class InforFieldManagerImpl extends BasicManagerImpl<InforField> implements InforFieldManager {

	private InforFieldDAO inforFieldDAO;
		
	//注入的方法
	@Autowired
	public void setInforFieldDAO(InforFieldDAO inforFieldDAO) {
		this.inforFieldDAO = inforFieldDAO;
		super.setDao(inforFieldDAO);
	}
	
	
	//根据category及fieldName获取InforField
	public InforField getFieldByName(String fieldName,int categoryId){
		String sql = "from InforField field ";
		sql += " where field.category.categoryId=? and field.fieldName=?";

		Session session = this.inforFieldDAO.openSession();
		Query query = session.createQuery(sql);
		query.setInteger(0,categoryId);
		query.setString(1, fieldName);
		

		List list = query.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (InforField) list.get(0);
		} else {
			return null;
		}		
	}
	
}
