package com.kwchina.extend.loginLog.dao.impl;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.extend.loginLog.dao.LoginLogDAO;
import com.kwchina.extend.loginLog.entity.LoginLog;

@Repository
public class LoginLogDAOImpl extends BasicDaoImpl<LoginLog> implements LoginLogDAO {
	
}