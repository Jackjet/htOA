package com.kwchina.oa.personal.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.message.dao.ReceiveMessageDAO;
import com.kwchina.oa.personal.message.entity.ReceiveMessage;

@Repository
public class ReceiveMessageDAOHibernate extends BasicDaoImpl<ReceiveMessage> implements ReceiveMessageDAO {
}
