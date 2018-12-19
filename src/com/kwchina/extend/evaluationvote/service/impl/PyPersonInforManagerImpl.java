package com.kwchina.extend.evaluationvote.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyPersonInforDAO;
import com.kwchina.extend.evaluationvote.entity.PyPersonInfor;
import com.kwchina.extend.evaluationvote.service.PyPersonInforManager;

@Service
public class PyPersonInforManagerImpl extends BasicManagerImpl<PyPersonInfor> implements PyPersonInforManager {
	private PyPersonInforDAO pyPersonInforDAO;
	
	@Autowired
	public void setPyPersonInforDAO(PyPersonInforDAO pyPersonInforDAO) {
		this.pyPersonInforDAO = pyPersonInforDAO;
		super.setDao(pyPersonInforDAO);
		//此句重要！否则DAO在超类里就不会注入，返回null的错误。
	}
	

	//获取的角色信息通过displayOrder排序
	public List getPersonOrderBy() {
		List ls = this.pyPersonInforDAO.getAll();
		//按照orderNo排序
		Collections.sort(ls, new BeanComparator("displayOrder"));
		return ls;
	}
	
}