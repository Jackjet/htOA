package com.kwchina.oa.customer.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ProjectInforDAO;
import com.kwchina.oa.customer.entity.ExecutorInfor;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectCategoryRight;
import com.kwchina.oa.customer.entity.ProjectInfor;
import com.kwchina.oa.customer.entity.ProjectRight;
import com.kwchina.oa.customer.entity.TaskInfor;
import com.kwchina.oa.customer.service.ProjectInforManager;
import com.kwchina.oa.customer.vo.ProjectInforVo;

@Service
public class ProjectInforManagerImpl extends BasicManagerImpl<ProjectInfor> implements ProjectInforManager {

	private ProjectInforDAO projectInforDAO;

	@Autowired
	public void setProjectInforDAO(ProjectInforDAO projectInforDAO) {
		this.projectInforDAO = projectInforDAO;
		super.setDao(projectInforDAO);
	}

	@Resource
	private SystemUserManager systemUserManager;

	// 获取用户有权限看到的项目信息
	public List getProjectInfor(SystemUserInfor user) {
		List listAll = this.projectInforDAO.getAll();
		ArrayList projectList = new ArrayList();

		// 只要是项目中某一点与用户有关系的（包括分类和任务），则显示
		Iterator itAll = listAll.iterator();
		while (itAll.hasNext()) {
			ProjectInfor projectInfor = (ProjectInfor) itAll.next();
			boolean hasRight = false;

			// 判断是否为项目的创建者或项目经理
			if (user.getPersonId().intValue() == projectInfor.getCreator().getPersonId().intValue() || user.getPersonId().intValue() == projectInfor.getManager().getPersonId().intValue()) {
				hasRight = true;
			}

			// 判断是否有项目权限
			if (!hasRight) {
				Set rights = projectInfor.getProjectRights();
				if (rights == null || rights.size() == 0) {
					// 未设置任何权限
					hasRight = true;
				} else {
					Iterator it = rights.iterator();
					while (it.hasNext()) {
						ProjectRight projectRight = (ProjectRight) it.next();
						if (projectRight.getUser().getPersonId().intValue() == user.getPersonId().intValue()) {
							hasRight = true;
						}
						break;
					}
				}

			}

			// 判断是否有分类权限
			if (!hasRight) {
				Set categoryList = projectInfor.getProjectCategorys();
				Iterator catiter = categoryList.iterator();
				while (catiter.hasNext()) {
					ProjectCategory projectCategory = (ProjectCategory) catiter.next();
					Set rights = projectCategory.getCategoryRights();
					for (Iterator it = rights.iterator(); it.hasNext();) {
						ProjectCategoryRight projectCategoryRight = (ProjectCategoryRight) it.next();
						if (projectCategoryRight.getUser().getPersonId().intValue() == user.getPersonId().intValue()) {
							hasRight = true;
						}
						break;
					}
				}

			}

			// 判断是否为任务的审核人、签核人或执行人
			if (!hasRight) {
				Set categoryList = projectInfor.getProjectCategorys();
				Iterator catiter = categoryList.iterator();
				while (catiter.hasNext()) {
					ProjectCategory projectCategory = (ProjectCategory) catiter.next();
					Set taskInfors = projectCategory.getTaskInfors();
					for (Iterator it = taskInfors.iterator(); it.hasNext();) {
						TaskInfor taskInfor = (TaskInfor) it.next();
						if (taskInfor.getSigner().getPersonId().intValue() == user.getPersonId().intValue() || taskInfor.getChecker().getPersonId().intValue() == user.getPersonId().intValue()) {
							hasRight = true;
							break;
						}
						Set executors = taskInfor.getExecutorInfors();
						if (!hasRight) {
							for (Iterator itEx = executors.iterator(); itEx.hasNext();) {
								ExecutorInfor executor = (ExecutorInfor) itEx.next();
								if (executor.getExecutor().getPersonId().intValue() == user.getPersonId().intValue()) {
									hasRight = true;
									break;
								}
							}
						}
					}
				}
			}

			if (hasRight) {
				projectList.add(projectInfor);
			}
		}

		return projectList;
	}

	// select为0时：转化ProjectInfor为ProjectInforVo;select为1时：转化ProjectCategory为ProjectInforVo
	public ProjectInforVo transPOToVO(ProjectInfor projectInfor, ProjectCategory projectCategory, int select, int leftIndex, int rightIndex) {
		ProjectInforVo projectInforVo = new ProjectInforVo();
		if (select == 0) {
			try {
				BeanUtils.copyProperties(projectInforVo, projectInfor);
				projectInforVo.setProjectId(projectInfor.getProjectId());
				projectInforVo.setLayer(1);
				projectInforVo.setLeaf(false);
				projectInforVo.setLeftIndex(leftIndex);
				projectInforVo.setRightIndex(rightIndex);
			} catch (Exception ex) {
			}
		} else {
			// 为防止树形图中项目和分类的主键冲突，树形结构中的分类的Id都要加上项目信息的最大Id的，所以作如下处理
			String hql = "select max(infor.projectId) from ProjectInfor infor";
			int maxProjectInforId = this.projectInforDAO.getResultNumByQueryString(hql);
			projectInforVo.setProjectName(projectCategory.getCategoryName());
			projectInforVo.setProjectId(projectCategory.getCategoryId() + maxProjectInforId);
			projectInforVo.setLayer(2);
			projectInforVo.setLeaf(true);
			projectInforVo.setLeftIndex(rightIndex);
			projectInforVo.setRightIndex(rightIndex + 1);
		}
		return projectInforVo;
	}

	// 转化ProjectInfor为ProjectInforVo
	public ProjectInforVo transPOToVO(ProjectInfor projectInfor) {
		ProjectInforVo projectInforVo = new ProjectInforVo();
		try {
			BeanUtils.copyProperties(projectInforVo, projectInfor);
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
			projectInforVo.setUpdateDateStr(sim.format(projectInfor.getUpdateDate()));
			projectInforVo.setCreateorName(projectInfor.getCreator().getPerson().getPersonName());
			projectInforVo.setManagerName(projectInfor.getManager().getPerson().getPersonName());
		} catch (Exception ex) {
		}
		return projectInforVo;
	}

	// 保存项目信息(包括权限信息)
	public void saveProject(ProjectInfor projectInfor, ProjectInforVo projectInforVo) {
		Set oldRights = projectInfor.getProjectRights();
		projectInfor.getProjectRights().removeAll(oldRights);

		// 获取权限信息
		int[] editIds = projectInforVo.getEditIds(); // 项目的修改
		int[] deleteIds = projectInforVo.getDeleteIds(); // 项目的删除
		int[] createIds = projectInforVo.getCreateIds(); // 项目的添加

		// 项目的修改
		if (editIds != null) {
			for (int k = 0; k < editIds.length; k++) {
				int userId = editIds[k];
				SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
				ProjectRight right = new ProjectRight();
				right.setProjectInfor(projectInfor);
				right.setOperateRight(1);
				right.setUser(user);
				projectInfor.getProjectRights().add(right);
			}
		}

		// 项目的删除
		if (deleteIds != null) {
			for (int k = 0; k < deleteIds.length; k++) {
				Set rights = projectInfor.getProjectRights();
				int userId = deleteIds[k];
				boolean has = false;

				for (Iterator it = rights.iterator(); it.hasNext();) {
					ProjectRight right = (ProjectRight) it.next();
					if (right.getUser().getPersonId().intValue() == userId) {
						right.setOperateRight(right.getOperateRight() + 2);
						has = true;
						break;
					}
				}

				if (!has) {
					SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
					ProjectRight right = new ProjectRight();
					right.setProjectInfor(projectInfor);
					right.setOperateRight(2);
					right.setUser(user);
					projectInfor.getProjectRights().add(right);
				}
			}
		}

		// 项目分类的添加
		if (createIds != null) {
			for (int k = 0; k < createIds.length; k++) {
				Set rights = projectInfor.getProjectRights();
				int userId = createIds[k];
				boolean has = false;

				for (Iterator it = rights.iterator(); it.hasNext();) {
					ProjectRight right = (ProjectRight) it.next();
					if (right.getUser().getPersonId().intValue() == userId) {
						right.setOperateRight(right.getOperateRight() + 4);
						has = true;
						break;
					}
				}

				if (!has) {
					SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
					ProjectRight right = new ProjectRight();
					right.setProjectInfor(projectInfor);
					right.setOperateRight(4);
					right.setUser(user);
					projectInfor.getProjectRights().add(right);
				}
			}
		}

		this.projectInforDAO.save(projectInfor);

	}

	// 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	public boolean hasRight(ProjectInfor projectInfor, SystemUserInfor systemUser, int rightBit) {
		boolean hasRight = false;

		int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == SystemUserInfor._Type_Admin) {
			hasRight = true;
		} else {
			Set rights = projectInfor.getProjectRights();
			if (rights == null || rights.size() == 0) {
				// 未设置任何权限
				hasRight = true;
			} else {
				for (Iterator it = rights.iterator(); it.hasNext();) {
					ProjectRight projectRight = (ProjectRight) it.next();
					if (projectRight.getUser().getPersonId() == personId) {
						int rightValue = projectRight.getOperateRight();
						int a = rightValue >> (rightBit - 1);
						int result = (a & 1);
						if (result == 1) {
							hasRight = true;
						}
						break;
					}
				}

			}

		}

		return hasRight;
	}

}
