package com.kwchina.oa.customer.service;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.TaskInfor;
import com.kwchina.oa.customer.vo.TaskInforVo;

public interface TaskInforManager extends BasicManager {

	//获取用户有权限看到的任务信息
	public List getTaskInfor(SystemUserInfor user);
	
	//转化TaskInfor为TaskInforVo
	public TaskInforVo transPOToVO(TaskInfor taskInfor);
	
	//自动获取任务编号
	public String getAutoTaskCode(int year);
}
