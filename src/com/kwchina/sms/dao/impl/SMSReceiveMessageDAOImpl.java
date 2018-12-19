package com.kwchina.sms.dao.impl;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.sms.dao.SMSReceiveMessageDAO;
import com.kwchina.sms.entity.SMSMessagesReceived;

@Repository
public class SMSReceiveMessageDAOImpl extends BasicDaoImpl<SMSMessagesReceived> implements SMSReceiveMessageDAO{

}
