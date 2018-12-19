package com.kwchina.extend.template.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.template.dao.TemplateDAO;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.extend.template.service.TemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateManagerImpl extends BasicManagerImpl<ZhaotouTemplate> implements TemplateManager {
	
	private TemplateDAO templateDAO;
	
	@Autowired
	public void setTemplateDAO(TemplateDAO templateDAO) {
		this.templateDAO = templateDAO;
		super.setDao(templateDAO);
	}

	@Override
	public List<ZhaotouTemplate> getAllTemplates() {
		String queryString = "from ZhaotouTemplate template where valid=1";
		List list = this.getResultByQueryString(queryString);
		return list;
	}

	@Override
	public ZhaotouTemplate findByTemplateName(String name) {
		return templateDAO.findByName(name);
	}
}
