package com.kwchina.core.base.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.ApproveSentenceDAO;
import com.kwchina.core.base.entity.ApproveSentence;
import com.kwchina.core.base.service.ApproveSentenceManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class ApproveSentenceManagerImpl extends BasicManagerImpl<ApproveSentence> implements ApproveSentenceManager{
	private ApproveSentenceDAO approveSentenceDAO;

	@Autowired
	public void setRoleDAO(ApproveSentenceDAO approveSentenceDAO) {
		this.approveSentenceDAO = approveSentenceDAO;
		super.setDao(approveSentenceDAO);
		//此句重要！否则DAO在超类里就不会注入，返回null的错误。
	}

//	获取的角色信息通过orderNo排序
	public List getApproveSentenceOrderBy() {
		List ls = this.approveSentenceDAO.getAll();
		//按照orderNo排序
		Collections.sort(ls, new BeanComparator("orderNo"));
		return ls;
	}
	
	
}