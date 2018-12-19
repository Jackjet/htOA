package com.kwchina.core.base.service.impl;

import com.kwchina.core.base.dao.ModulesDAO;
import com.kwchina.core.base.dao.PersonModulesDAO;
import com.kwchina.core.base.entity.Modules;
import com.kwchina.core.base.entity.PersonModules;
import com.kwchina.core.base.service.ModulesManager;
import com.kwchina.core.base.service.PersonModulesManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("modulesManager")
public class ModulesManagerImpl extends BasicManagerImpl<Modules> implements ModulesManager {
	@Resource
	private ModulesDAO modulesDAO;

	@Override
	public Modules getByName(String name) {
		String sql = "from Modules modules where modules.name='"+name+"'";
		List<Modules> modules = modulesDAO.getResultByQueryString(sql);
		if(modules==null && modules.isEmpty()){
			return null;
		}
		return modules.get(0);
	}
}
