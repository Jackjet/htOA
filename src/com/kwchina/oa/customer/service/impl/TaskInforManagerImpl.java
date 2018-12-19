package com.kwchina.oa.customer.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.TaskInforDAO;
import com.kwchina.oa.customer.entity.TaskInfor;
import com.kwchina.oa.customer.service.TaskInforManager;
import com.kwchina.oa.customer.vo.TaskInforVo;

@Service
public class TaskInforManagerImpl extends BasicManagerImpl<TaskInfor> implements TaskInforManager {
	private TaskInforDAO taskInforDAO;

	@Autowired
	public void setTaskInforDAO(TaskInforDAO taskInforDAO) {
		this.taskInforDAO = taskInforDAO;
		super.setDao(taskInforDAO);
	}

	// 获取用户有权限看到的任务信息
	public List getTaskInfor(SystemUserInfor user) {
		return this.taskInforDAO.getAll();
	}

	// 转化TaskInfor为TaskInforVo
	public TaskInforVo transPOToVO(TaskInfor taskInfor) {
		TaskInforVo taskInforVo = new TaskInforVo();
		try {
			BeanUtils.copyProperties(taskInforVo, taskInfor);
			taskInforVo.setCategoryName(taskInfor.getProjectCategory().getCategoryName());
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			if (taskInfor.getEndDate() != null) {
				taskInforVo.setEndDateStr(sim.format(taskInfor.getEndDate()));
			}
			if (taskInfor.getStartDate() != null) {
				taskInforVo.setStartDateStr(sim.format(taskInfor.getStartDate()));
			}
			if (taskInfor.getCheckTime() != null) {
				taskInforVo.setCheckTimeStr(sim.format(taskInfor.getCheckTime()));
			}
			if (taskInfor.getUpdateDate() != null) {
				taskInforVo.setUpdateDateStr(sim.format(taskInfor.getUpdateDate()));
			}

		} catch (Exception ex) {
		}
		return taskInforVo;
	}

	// 自动获取任务编号
	public String getAutoTaskCode(int year) {
		String hql = "select max(taskCode) from TaskInfor taskInfor where YEAR(updateDate) = " + year;
		List list = this.taskInforDAO.getResultByQueryString(hql);
		String taskCode = "";
		if (list != null && list.size() != 0) {
			taskCode = (String) list.get(0);
			if (taskCode != null) {
				taskCode = String.valueOf(Integer.parseInt(taskCode) + 1);
				char[] taskCodeArray = taskCode.toCharArray();
				int codeLength = taskCodeArray.length;
				if (codeLength != 5) {
					for (int i = 0; i < 5 - codeLength; i++) {
						taskCode = "0" + taskCode;
					}
				}
			} else {
				taskCode = "00001";
			}
		} else {
			taskCode = "00001";
		}

		return taskCode;
	}

}
