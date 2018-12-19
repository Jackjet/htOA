package com.kwchina.core.cms.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.cms.dao.InforCategoryRightDAO;
import com.kwchina.core.cms.entity.CategoryRoleRight;
import com.kwchina.core.cms.entity.CategoryUserRight;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.cms.service.InforCategoryRightManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class InforCategoryRightManagerImpl extends BasicManagerImpl<InforCategoryRight> implements InforCategoryRightManager {

	private InforCategoryRightDAO inforCategoryRightDAO;
	
	@Resource
	private RoleManager roleManager;
	

	//注入的方法
	@Autowired
	public void setInforCategoryRightDAO(InforCategoryRightDAO inforCategoryRightDAO) {
		this.inforCategoryRightDAO = inforCategoryRightDAO;
		super.setDao(inforCategoryRightDAO);
	}
	
	
	//通过系统用户获取CategoryUserRight
	public CategoryUserRight getCategoryRightByUser(SystemUserInfor systemUser){
		String sql = "from CategoryUserRight userRight ";
		sql += " where userRight.systemUser.personId=?";
		
		Session session = this.inforCategoryRightDAO.openSession();
		Query query = session.createQuery(sql);
		int personId = systemUser.getPersonId().intValue();
		query.setInteger(0, personId);
		
		List list = query.list();
		if(list!=null && list.size()>0){
			return (CategoryUserRight)list.get(0);
		}else{
			return null;
		}				
	}
	
	
	//通过系统用户获取CategoryRoleRight
	

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(InforCategoryRight right, int rightDigit) {
		boolean booleanRight = false;
		int rightValue = right.getOperateRight();

		int a = rightValue >> (rightDigit - 1);
		int result = (a & 1);

		if (result == 0) {
			booleanRight = false;
		} else {
			booleanRight = true;
		}

		return booleanRight;

	}

	//判断用户对某个分类的操作权限
	public boolean hasRight(InforCategory category, SystemUserInfor systemUser, int rightBit) {
		boolean hasRight = false;

		int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == SystemUserInfor._Type_Admin) {
			hasRight = true;
		} else {
			Set rights = category.getRights();
			if (rights == null || rights.size() == 0) {
				// 未设置任何权限
				hasRight = true;
			} else {
				for (Iterator it = rights.iterator(); it.hasNext();) {
					Object tempRight = it.next();
					if(tempRight instanceof CategoryRoleRight){
						//对角色的授权,首先需要判断该用户是否属于该角色
						CategoryRoleRight roleRight = (CategoryRoleRight)tempRight;
						RoleInfor role = roleRight.getRole();
						if(roleManager.belongRole(systemUser, role)){
							int rightValue = roleRight.getOperateRight();
							int a = rightValue >> (rightBit - 1);
							int result = (a & 1);
							if (result == 1) {
								hasRight = true;
								break;
							}

							
						}
						
					}else if(tempRight instanceof CategoryUserRight){
						//对人员的授权
						CategoryUserRight userRight = (CategoryUserRight)tempRight;
						int tempPersonId = userRight.getSystemUser().getPersonId().intValue();
						if (personId == tempPersonId) {
							int rightValue = userRight.getOperateRight();
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
		}

		return hasRight;

	}

}
