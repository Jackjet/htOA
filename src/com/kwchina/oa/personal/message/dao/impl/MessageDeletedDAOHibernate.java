package com.kwchina.oa.personal.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.personal.message.dao.MessageDeletedDAO;
import com.kwchina.oa.personal.message.entity.MessageDeleted;


@Repository
public class MessageDeletedDAOHibernate extends BasicDaoImpl<MessageDeleted>
		implements MessageDeletedDAO {

}
