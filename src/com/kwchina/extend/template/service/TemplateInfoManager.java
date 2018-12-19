package com.kwchina.extend.template.service;


import com.kwchina.core.common.service.BasicManager;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;

import java.util.List;


public interface TemplateInfoManager extends BasicManager{
   List<ZhaotouTemplateInfo> getAllInfo();
	
}
