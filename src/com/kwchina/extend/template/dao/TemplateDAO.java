package com.kwchina.extend.template.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.extend.template.entity.ZhaotouTemplate;

public interface TemplateDAO extends BasicDao<ZhaotouTemplate> {
	ZhaotouTemplate findByName(String name);
}