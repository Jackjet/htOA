package com.kwchina.oa.personal.schedule.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobDAO;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;
import com.kwchina.oa.personal.schedule.service.ScheduleJobManager;
import com.kwchina.oa.personal.schedule.vo.ScheduleJobInforVo;
import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Service
public class ScheduleJobManagerImpl extends BasicManagerImpl<ScheduleJobInfor>
		implements ScheduleJobManager {

	private ScheduleJobDAO scheduleJobDAO;

	private RoleManager roleManager;

	// 注入的方法
	@Autowired
	public void setScheduleJobDAO(ScheduleJobDAO scheduleJobDAO) {
		this.scheduleJobDAO = scheduleJobDAO;
		super.setDao(scheduleJobDAO);
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void deleteSchedule(int scheduleId, SystemUserInfor user) {
		ScheduleJobInfor schedule = this.scheduleJobDAO.get(scheduleId);
		this.scheduleJobDAO.remove(schedule);
	}

	// 判断权限
	public boolean judgeRight(ScheduleJobInfor scheduleJobInfor,
			SystemUserInfor user, int rightType) {
		boolean hasRight = false;
		boolean isAssigner = false; // 是安排者
		int userId = user.getPersonId().intValue();
		int assignerId = scheduleJobInfor.getAssigner().getPersonId()
				.intValue();

		if (userId == assignerId) {
			isAssigner = true;
		}

		/*if (rightType == SystemConstant._Right_View || user.getUserType() == SystemConstant._User_Type_Admin) {
			// 只有安排者,工作执行人员和管理员才可以看
			if (isAssigner) { // 未完成
				hasRight = true;
			}
		}
		if (rightType == SystemConstant._Right_Edit || rightType == SystemConstant._Right_Delete
				|| user.getUserType() == SystemConstant._User_Type_Admin) {
			// 只有安排者和管理员才可以编辑删除
			if (isAssigner) {
				hasRight = true;
			}
		}*/

		return hasRight;
	}

	// 转化MessageInfor为MessageInforVO
	public ScheduleJobInforVo transPOToVO(ScheduleJobInfor schedule) {
		ScheduleJobInforVo ScheduleVo = new ScheduleJobInforVo();

		try {
			BeanUtils.copyProperties(ScheduleVo, schedule);

			ScheduleVo.setPersonId(schedule.getAssigner().getPersonId());
			ScheduleVo.setPersonName(schedule.getAssigner().getPerson()
					.getPersonName());
			ScheduleVo.setWriteTimeStr(schedule.getWriteTime().toString());
			ScheduleVo.setStartTimeStr(schedule.getStartDate().toString());
			ScheduleVo.setEndTimeStr(schedule.getEndDate().toString());
			ScheduleVo.setAttachmentStr(schedule.getAttachment());

		} catch (Exception ex) {

		}

		return ScheduleVo;
	}

	// 通过scheduleId获取日程信息
	public ScheduleJobInfor getEditSchedule(int scheduleId) {
		ScheduleJobInfor scheduleJobInfor = this.scheduleJobDAO.get(scheduleId);
		return scheduleJobInfor;
	}

	/***************************************************************************
	 * 判断用户是否有权限对讯息进行修改或者删除（如果是讯息作者或者是管理员，则有权限对其进行操作）
	 */
	public boolean canDeleteOrEditSchedule(SystemUserInfor user,
			ScheduleJobInfor schedule) {
		boolean flage = false;
		// 用户为发送者或管理员
		int sendId = schedule.getAssigner().getPersonId();
		int personId = user.getPersonId();
		if (sendId == personId
				|| user.getUserType() == SystemConstant._User_Type_Admin) {
			flage = true;
		}

		return flage;
	}

	// 获取任务督办的语句
	public String taskCondition(HttpServletRequest request, String method,
			Pages pages) {
		StringBuffer condition = new StringBuffer();

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int userId = systemUser.getPersonId().intValue();

		String fileName = "scheduleJobInfor" + SystemConstant.FILEPREFIX + "?method=" + method;

		// 判断用户是否为任务督办人员
		boolean isTaskManager = false;
		/*RoleInfor taskRole = (RoleInfor) this.roleManager.get(SystemConstant._Role_Task);
		if (taskRole != null) {
			isTaskManager = this.roleManager.belongRole(systemUser, taskRole);
		}*/

		String startTime = "";
		String endTime = "";
		String searchKey = "";

		/**
		 * 判断用户是否为任务督办人员： a.为督办人员时，显示所有的督办信息； b.不为督办人员时，只显示自己安排的、他人安排给自己的 或
		 * 他人公开的督办信息.
		 */
		if (isTaskManager) {
			condition.append(" (scheduleJobInfor.scheduleId in (select scheduleExcuter.jobInfor.scheduleId from ScheduleExcuter scheduleExcuter where scheduleExcuter.executor.personId != scheduleExcuter.jobInfor.assigner.personId))");
		} else {
			// 任务督办(自己安排的、他人安排给自己的 或 他人公开的)
			condition
					.append(" (scheduleJobInfor.scheduleId in (select scheduleExcuter.jobInfor.scheduleId from ScheduleExcuter scheduleExcuter where scheduleExcuter.executor.personId != scheduleExcuter.jobInfor.assigner.personId"
							+ " and (scheduleExcuter.jobInfor.assigner.personId = "
							+ userId
							+ " or scheduleExcuter.executor.personId = "
							+ userId
							+ " or scheduleExcuter.jobInfor.openType = "
							+ ScheduleJobInfor.Type_Public + ")))");
		}

		// 安排时间
		if (startTime != null && startTime.length() > 0 && endTime != null
				&& endTime.length() > 0) {
			condition.append(" and (scheduleJobInfor.writeTime between '"
					+ startTime + "' and '" + endTime + "')");
			if (fileName != null) {
				fileName += "&startTime=" + startTime + "&endTime=" + endTime;
			}
		}

		// 搜索关键字
		if (searchKey != null && searchKey.length() > 0) {
			condition.append(" and scheduleJobInfor.jobTitle like '%"
					+ searchKey.trim() + "%'");
			if (fileName != null) {
				fileName += "&searchKey=" + searchKey.trim();
			}
		}
		if (pages != null) {
			pages.setFileName(fileName);
		}

		return condition.toString();
	}
}