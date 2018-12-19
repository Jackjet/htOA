package com.kwchina.core.base.service.impl;

import com.kwchina.core.base.dao.PersonModulesDAO;
import com.kwchina.core.base.dao.SystemUserInforDAO;
import com.kwchina.core.base.entity.PersonModules;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.PersonModulesManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.util.Iterator;
import java.util.List;

@Service("personModulesManager")
public class PersonModulesManagerImpl extends BasicManagerImpl<PersonModules> implements PersonModulesManager{
	@Resource
	private PersonModulesDAO personModulesDAO;


	@Override
	public PersonModules selectByPerson(int personId) {
		PersonModules personModules = personModulesDAO.get(personId);
		return personModules;
	}

	@Override
	public void saveModule(PersonModules personModules) {
		personModulesDAO.saveOrUpdate(personModules);
	}
}
