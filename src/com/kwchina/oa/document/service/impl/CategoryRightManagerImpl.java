package com.kwchina.oa.document.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.document.dao.CategoryRightDAO;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentCategoryRight;
import com.kwchina.oa.document.service.CategoryRightManager;

@Service
public class CategoryRightManagerImpl extends BasicManagerImpl<DocumentCategoryRight> implements CategoryRightManager {

	private CategoryRightDAO documentCategoryRightDAO;

	//注入的方法
	@Autowired
	public void setDocumentCategoryRightDAO(CategoryRightDAO documentCategoryRightDAO) {
		this.documentCategoryRightDAO = documentCategoryRightDAO;
		super.setDao(documentCategoryRightDAO);
	}

	//获取某个分类的分类权限
	public List getCategoryRights(int categoryId) {
		return this.documentCategoryRightDAO.getCategoryRights(categoryId);
	}

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(DocumentCategoryRight right, int rightDigit) {
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
	public boolean hasRight(DocumentCategory category, SystemUserInfor systemUser, int rightBit) {
		boolean hasRight = false;

		int userId = systemUser.getPersonId().intValue();

		Set rights = category.getRights();
		
		//判断是否对该类权限做过设置
		List typeRights = new ArrayList();
		for (Iterator it = rights.iterator(); it.hasNext();) {
			DocumentCategoryRight categoryRight = (DocumentCategoryRight) it.next();
			int rightValue = categoryRight.getOperateRight();

			int a = rightValue >> (rightBit - 1);
			int result = (a & 1);
			if (result == 1) {
				typeRights.add(categoryRight);
			}
		}
		
		/**
		if (rights == null || rights.size() == 0) {
			// 未设置任何权限
			hasRight = true;
		} else
		*/ 
			
		if (systemUser.getUserType() == 1) {
			// 系统管理员
			/** @todo change the following sentence */
			hasRight = true;
		} else {
			if(typeRights.isEmpty()){
				//无此类权限设置,默认为有权限
				hasRight = true;
			}else{
				//有此类权限设置,判断是否有该用户的权限
				for (Iterator it = typeRights.iterator(); it.hasNext();) {
					DocumentCategoryRight categoryRight = (DocumentCategoryRight) it.next();
					int rightValue = categoryRight.getOperateRight();
					int tempUserId = categoryRight.getUser().getPersonId().intValue();
					if (userId == tempUserId) {
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
		
		/**
		if (rights == null || rights.size() == 0) {
			// 未设置任何权限
			hasRight = true;
		} else if (systemUser.getUserType() == 1) {
			// 系统管理员
			/** @todo change the following sentence 
			hasRight = true;
		} else {
			for (Iterator it = rights.iterator(); it.hasNext();) {
				DocumentCategoryRight right = (DocumentCategoryRight) it.next();
				int tempUserId = right.getUser().getPersonId().intValue();
				if (userId == tempUserId) {
					int rightValue = right.getOperateRight();
					int a = rightValue >> (rightBit - 1);
					int result = (a & 1);
					if (result == 1) {
						hasRight = true;
					}

					break;
				}
			}
		}
		*/

		return hasRight;

	}

	//通过personId获得分类权限
	public List getRightsByPersonId(int personId) {
		return this.documentCategoryRightDAO.getRightsByPersonId(personId);
	}
	
	
	// 通过personId和categoryId获得分类权限
	public DocumentCategoryRight getRightsByCondition(int personId, int categoryId) {
		DocumentCategoryRight categoryRight = null;
		
		String queryString ="from DocumentCategoryRight categoryRight where categoryRight.user.personId = " + personId + " and categoryRight.category.categoryId = " + categoryId;
		List list = this.documentCategoryRightDAO.getResultByQueryString(queryString);
		
		if (list != null && list.size() > 0) {
			categoryRight = (DocumentCategoryRight)list.get(0);
		}
		
		return categoryRight;
	}
}
