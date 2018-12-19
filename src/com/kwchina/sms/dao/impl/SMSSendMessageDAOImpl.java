package com.kwchina.sms.dao.impl;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.sms.dao.SMSSendMessageDAO;
import com.kwchina.sms.entity.SMSMessagesToSend;

@Repository
public class SMSSendMessageDAOImpl extends BasicDaoImpl<SMSMessagesToSend> implements SMSSendMessageDAO{

}
