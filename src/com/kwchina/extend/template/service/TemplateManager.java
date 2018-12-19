package com.kwchina.extend.template.service;


import com.kwchina.core.common.service.BasicManager;
import com.kwchina.extend.template.entity.ZhaotouTemplate;

import java.util.List;


public interface TemplateManager extends BasicManager{
    List<ZhaotouTemplate> getAllTemplates();
    ZhaotouTemplate findByTemplateName(String name);
	
}
