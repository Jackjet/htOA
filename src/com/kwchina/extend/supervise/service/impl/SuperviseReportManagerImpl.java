package com.kwchina.extend.supervise.service.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.supervise.dao.SuperviseReportDAO;
import com.kwchina.extend.supervise.entity.SuperviseReport;
import com.kwchina.extend.supervise.service.SuperviseReportManager;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;

@Service
public class SuperviseReportManagerImpl extends BasicManagerImpl<SuperviseReport> implements SuperviseReportManager {

	private SuperviseReportDAO superviseReportDAO;


	//注入的方法
	@Autowired
	public void setSuperviseReportDAO(SuperviseReportDAO superviseReportDAO) {
		this.superviseReportDAO = superviseReportDAO;
		super.setDao(superviseReportDAO);
	}
	

}
