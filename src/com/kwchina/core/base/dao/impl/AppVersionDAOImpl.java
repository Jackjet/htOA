package com.kwchina.core.base.dao.impl;

import org.springframework.stereotype.Repository;

import com.kwchina.core.base.dao.AppVersionDAO;
import com.kwchina.core.base.entity.AppVersion;
import com.kwchina.core.common.dao.BasicDaoImpl;

@Repository
public class AppVersionDAOImpl extends BasicDaoImpl<AppVersion> implements AppVersionDAO {
	
}