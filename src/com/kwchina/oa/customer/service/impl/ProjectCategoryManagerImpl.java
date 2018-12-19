package com.kwchina.oa.customer.service.impl;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.customer.dao.ProjectCategoryDAO;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectCategoryRight;
import com.kwchina.oa.customer.entity.ProjectInfor;
import com.kwchina.oa.customer.entity.ProjectRight;
import com.kwchina.oa.customer.service.ProjectCategoryManager;
import com.kwchina.oa.customer.vo.ProjectCategoryVo;

@Service
public class ProjectCategoryManagerImpl extends BasicManagerImpl<ProjectCategory> implements ProjectCategoryManager {

	private ProjectCategoryDAO projectCategoryDAO;

	@Autowired
	public void setProjectCategoryDAO(ProjectCategoryDAO projectCategoryDAO) {
		this.projectCategoryDAO = projectCategoryDAO;
		super.setDao(projectCategoryDAO);
	}

	@Resource
	private SystemUserManager systemUserManager;

	// 保存分类信息(包括权限信息)
	public void saveProjectCategroy(ProjectCategory projectCategory, ProjectCategoryVo projectCategoryVo) {
		Set oldRights = projectCategory.getCategoryRights();
		projectCategory.getCategoryRights().removeAll(oldRights);

		// 获取权限信息
		int[] editIds = projectCategoryVo.getEditIds(); // 项目的修改
		int[] deleteIds = projectCategoryVo.getDeleteIds(); // 项目的删除
		int[] createIds = projectCategoryVo.getCreateIds(); // 项目的添加

		// 分类的修改
		if (editIds != null) {
			for (int k = 0; k < editIds.length; k++) {
				int userId = editIds[k];
				SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
				ProjectCategoryRight right = new ProjectCategoryRight();
				right.setProjectCategory(projectCategory);
				right.setOperateRight(1);
				right.setUser(user);
				projectCategory.getCategoryRights().add(right);
			}
		}

		// 分类的删除
		if (deleteIds != null) {
			for (int k = 0; k < deleteIds.length; k++) {
				Set rights = projectCategory.getCategoryRights();
				int userId = deleteIds[k];
				boolean has = false;

				for (Iterator it = rights.iterator(); it.hasNext();) {
					ProjectCategoryRight right = (ProjectCategoryRight) it.next();
					if (right.getUser().getPersonId().intValue() == userId) {
						right.setOperateRight(right.getOperateRight() + 2);
						has = true;
						break;
					}
				}

				if (!has) {
					SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
					ProjectCategoryRight right = new ProjectCategoryRight();
					right.setProjectCategory(projectCategory);
					right.setOperateRight(2);
					right.setUser(user);
					projectCategory.getCategoryRights().add(right);
				}
			}
		}

		// 工作任务的添加
		if (createIds != null) {
			for (int k = 0; k < createIds.length; k++) {
				Set rights = projectCategory.getCategoryRights();
				int userId = createIds[k];
				boolean has = false;

				for (Iterator it = rights.iterator(); it.hasNext();) {
					ProjectCategoryRight right = (ProjectCategoryRight) it.next();
					if (right.getUser().getPersonId().intValue() == userId) {
						right.setOperateRight(right.getOperateRight() + 4);
						has = true;
						break;
					}
				}

				if (!has) {
					SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);
					ProjectCategoryRight right = new ProjectCategoryRight();
					right.setProjectCategory(projectCategory);
					right.setOperateRight(4);
					right.setUser(user);
					projectCategory.getCategoryRights().add(right);
				}
			}
		}

		this.projectCategoryDAO.save(projectCategory);

	}

	// 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	public boolean hasRight(ProjectCategory projectCategory, SystemUserInfor systemUser, int rightBit) {
		boolean hasRight = false;

		int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == SystemUserInfor._Type_Admin) {
			hasRight = true;
		} else {
			Set rights = projectCategory.getCategoryRights();
			if (rights == null || rights.size() == 0) {
				// 未设置任何权限
				hasRight = true;
			} else {
				for (Iterator it = rights.iterator(); it.hasNext();) {
					ProjectCategoryRight projectCategoryRight = (ProjectCategoryRight) it.next();
					if (projectCategoryRight.getUser().getPersonId() == personId) {
						int rightValue = projectCategoryRight.getOperateRight();
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
