package com.kwchina.extend.template.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.template.dao.TemplateInfoDAO;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;
import com.kwchina.extend.template.service.TemplateInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateInfoManagerImpl extends BasicManagerImpl<ZhaotouTemplateInfo> implements TemplateInfoManager {
	
	private TemplateInfoDAO templateInfoDAO;
	
	@Autowired
	public void setTemplateInfoDAO(TemplateInfoDAO templateInfoDAO) {
		this.templateInfoDAO = templateInfoDAO;
		super.setDao(templateInfoDAO);
	}

	@Override
	public List<ZhaotouTemplateInfo> getAllInfo() {
		String queryString = "from ZhaotouTemplateInfo template where valid=1";
		List list = this.getResultByQueryString(queryString);
		return list;
	}
}
