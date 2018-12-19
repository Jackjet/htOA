package com.kwchina.core.cms.service;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforDocumentRight;
import com.kwchina.core.cms.entity.InforDocumentUserRight;
import com.kwchina.core.common.service.BasicManager;

public interface InforDocumentRightManager extends BasicManager{
	
	//通过系统用户获取InforDocumentUserRight
	public InforDocumentUserRight getDocumentRightByUser(SystemUserInfor systemUser);
	

	/**
	 * 判断某个权限信息中是否包含某种操作的权限 这里的operateRight表示bit位的右移位数
	 */
	public boolean hasRight(InforDocumentRight right, int rightDigit);
	
	
	//判断用户对某个信息的操作权限
	public boolean hasRight(InforDocument document, SystemUserInfor systemUser, int rightBit);
	
	//根据某个信息获取所有的操作权限
	//public List getRightsByInfor(int inforId,boolean isRole);
	
	//根据inforId,roleId,userId获取权限
	public List getRightsByID(int inforId,int rightId,int flag);
}
