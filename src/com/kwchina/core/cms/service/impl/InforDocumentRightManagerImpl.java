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
import com.kwchina.core.cms.dao.InforDocumentRightDAO;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforDocumentRight;
import com.kwchina.core.cms.entity.InforDocumentRoleRight;
import com.kwchina.core.cms.entity.InforDocumentUserRight;
import com.kwchina.core.cms.service.InforDocumentRightManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class InforDocumentRightManagerImpl extends BasicManagerImpl<InforDocumentRight> implements InforDocumentRightManager {

	private InforDocumentRightDAO inforDocumentRightDAO;
	
	@Resource
	private RoleManager roleManager;
	
	//注入的方法
	@Autowired
	public void setInforDocumentRightDAO(InforDocumentRightDAO inforDocumentRightDAO) {
		this.inforDocumentRightDAO = inforDocumentRightDAO;
		super.setDao(inforDocumentRightDAO);
	}
	
	
	//通过系统用户获取InforDocumentUserRight
	public InforDocumentUserRight getDocumentRightByUser(SystemUserInfor systemUser){
		String sql = "from InforDocumentUserRight userRight ";
		sql += " where userRight.systemUser.personId=?";
		
		Session session = this.inforDocumentRightDAO.openSession();
		Query query = session.createQuery(sql);
		int personId = systemUser.getPersonId().intValue();
		query.setInteger(0, personId);
		
		List list = query.list();
		if(list!=null && list.size()>0){
			return (InforDocumentUserRight)list.get(0);
		}else{
			return null;
		}				
	}
	

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(InforDocumentRight right, int rightDigit){
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
	
	
	//判断用户对某个信息的操作权限
	public boolean hasRight(InforDocument document, SystemUserInfor systemUser, int rightBit){
		boolean hasRight = false;

		int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == SystemUserInfor._Type_Admin) {
			hasRight = true;
		} else {
			Set rights = document.getRights();
			if (rights == null || rights.size() == 0) {
				//未设置任何权限
				hasRight = true;
			} else {
				for (Iterator it = rights.iterator(); it.hasNext();) {
					Object tempRight = it.next();
					if(tempRight instanceof InforDocumentRoleRight){
						//对角色的授权,首先需要判断该用户是否属于该角色
						InforDocumentRoleRight roleRight = (InforDocumentRoleRight)tempRight;
						RoleInfor role = roleRight.getRole();
						if(roleManager.belongRole(systemUser, role)){
							int rightValue = roleRight.getOperateRight();
							int a = rightValue >> (rightBit - 1);
							int result = (a & 1);
							if (result == 1) {
								hasRight = true;
							}

							break;
						}
						
					}else if(tempRight instanceof InforDocumentUserRight){
						//对用户的授权
						InforDocumentUserRight userRight = (InforDocumentUserRight)tempRight;
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

	/*public List getRightsByInfor(int inforId,boolean isRole) {
		String hql = "";
		if(isRole){
			hql = "from InforDocumentRoleRight roleRight where roleRight.document.inforId="+inforId;
		}else{
			hql = "from InforDocumentUserRight userRight where userRight.document.inforId="+inforId;
		}
		List returnList = this.getResultByQueryString(hql);
		return returnList;
	}*/

	/**
	 * @param flag 标识符:0-role;1-user;2-获取所有关于infor的list.
	 */
	public List getRightsByID(int inforId, int rightId, int flag) {
		String hql = "";
		String condition = "";
		if(flag == 0){
			hql = "from InforDocumentRoleRight inforRight where 1=1";
			if(rightId > 0){
				condition += " and inforRight.role.roleId="+rightId;
			}
		}else if(flag == 1){
			hql = "from InforDocumentUserRight inforRight where 1=1";
			if(rightId > 0){
				condition += " and inforRight.systemUser.personId="+rightId;
			}
		}else{
			hql = "from InforDocumentRight inforRight where 1=1";
		}
		condition += " and inforRight.document.inforId="+inforId;
		hql += condition;
		List returnList = this.getResultByQueryString(hql);
		return returnList;
	}

}
