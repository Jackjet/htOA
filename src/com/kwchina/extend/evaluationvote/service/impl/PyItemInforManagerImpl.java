package com.kwchina.extend.evaluationvote.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.evaluationvote.dao.PyItemInforDAO;
import com.kwchina.extend.evaluationvote.entity.PyItemInfor;
import com.kwchina.extend.evaluationvote.service.PyItemInforManager;


@Service
public class PyItemInforManagerImpl extends BasicManagerImpl<PyItemInfor> implements PyItemInforManager {
	private PyItemInforDAO pyItemInforDAO;
	
	@Autowired
	public void setPyItemInforDAO(PyItemInforDAO pyItemInforDAO) {
		this.pyItemInforDAO = pyItemInforDAO;
		super.setDao(pyItemInforDAO);
	}
	
	//获取的角色信息通过displayOrder排序
	public List getItemOrderBy() {
		List ls = this.pyItemInforDAO.getAll();
		//按照orderNo排序
		Collections.sort(ls, new BeanComparator("displayOrder"));
		return ls;
	}


}
