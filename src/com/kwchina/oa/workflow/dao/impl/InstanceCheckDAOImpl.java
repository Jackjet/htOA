package com.kwchina.oa.workflow.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.workflow.dao.InstanceCheckDAO;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;

@Repository
public class InstanceCheckDAOImpl extends BasicDaoImpl<InstanceCheckInfor> implements InstanceCheckDAO {
	/**
	 * 查找某个实例的全部审核信息
	 * @param instanceId
	 * @return
	 */
	public List findChecksByInstance(int instanceId){
		String sql = "from InstanceCheckInfor  check where  check.layerInfor.instance.instanceId =" + instanceId;
		return getResultByQueryString(sql);
	}
	
	/**
	 * 查找某个实例，某个层次的全部审核信息
	 */
	public List findChecksByLayer(int layerId){
		String sql = "from InstanceCheckInfor  check where  check.layerInfor.layerId =" + layerId;
		return getResultByQueryString(sql);
	}
	
	//查找某个人全部审核信息
	public List findPersonCheckInfor(int personId){
		String sql = "from InstanceCheckInfor  check where  check.checker.personId =" + personId;
		return getResultByQueryString(sql);
	}
	
	//查找某个人未审核信息
	public List findPersonUncheckInfor(int personId){
		String sql = "from ReportCheckInfor  check where  check.user.personId =" + personId + " and check.checked = 0";
		return getResultByQueryString(sql);
	}
	
}
