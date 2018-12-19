package com.kwchina.oa.document.service.impl;

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
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.document.dao.DocumentInforRightDAO;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.document.entity.DocumentInforRight;
import com.kwchina.oa.document.entity.DocumentInforRoleRight;
import com.kwchina.oa.document.entity.DocumentInforUserRight;
import com.kwchina.oa.document.service.DocumentInforRightManager;

@Service
public class DocumentInforRightManagerImpl extends BasicManagerImpl<DocumentInforRight> implements DocumentInforRightManager {

	private DocumentInforRightDAO documentInforRightDAO;
	
	@Resource
	private RoleManager roleManager;
	
	//注入的方法
	@Autowired
	public void setDocumentInforRightDAO(DocumentInforRightDAO documentInforRightDAO) {
		this.documentInforRightDAO = documentInforRightDAO;
		super.setDao(documentInforRightDAO);
	}
	
	
	//通过系统用户获取DocumentInforUserRight
	public DocumentInforUserRight getDocumentRightByUser(SystemUserInfor systemUser){
		String sql = "from DocumentInforUserRight userRight ";
		sql += " where userRight.systemUser.personId=?";
		
		Session session = this.documentInforRightDAO.openSession();
		Query query = session.createQuery(sql);
		int personId = systemUser.getPersonId().intValue();
		query.setInteger(0, personId);
		
		List list = query.list();
		if(list!=null && list.size()>0){
			return (DocumentInforUserRight)list.get(0);
		}else{
			return null;
		}				
	}
	

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(DocumentInforRight right, int rightDigit){
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
	public boolean hasRight(DocumentInfor document, SystemUserInfor systemUser, int rightBit){
		boolean hasRight = false;

		int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == SystemUserInfor._Type_Admin) {
			hasRight = true;
		} else {
			Set rights = document.getRights();
			if (rights == null || rights.size() == 0) {
				// 未设置任何权限
				hasRight = true;
			} else {
				for (Iterator it = rights.iterator(); it.hasNext();) {
					Object tempRight = it.next();
					if(tempRight instanceof DocumentInforRoleRight){
						//对角色的授权,首先需要判断该用户是否属于该角色
						DocumentInforRoleRight roleRight = (DocumentInforRoleRight)tempRight;
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
						
					}else if(tempRight instanceof DocumentInforUserRight){
						//对人员的授权
						DocumentInforUserRight userRight = (DocumentInforUserRight)tempRight;
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
			hql = "from DocumentInforRoleRight roleRight where roleRight.document.inforId="+inforId;
		}else{
			hql = "from DocumentInforUserRight userRight where userRight.document.inforId="+inforId;
		}
		List returnList = this.getResultByQueryString(hql);
		return returnList;
	}*/

	/**
	 * flag 
	 * 0-role
	 * 1-user
	 * 2-获取所有关于infor的list
	 */
	public List getRightsByID(int documentId, int rightId, int flag) {
		String hql = "";
		String condition = "";
		if(flag == 0){
			hql = "from DocumentInforRoleRight inforRight where 1=1";
			if(rightId > 0){
				condition += " and inforRight.role.roleId="+rightId;
			}
		}else if(flag == 1){
			hql = "from DocumentInforUserRight inforRight where 1=1";
			if(rightId > 0){
				condition += " and inforRight.systemUser.personId="+rightId;
			}
		}else{
			hql = "from DocumentInforRight inforRight where 1=1";
		}
		condition += " and inforRight.document.documentId="+documentId;
		hql += condition;
		List returnList = this.getResultByQueryString(hql);
		return returnList;
	}


}
