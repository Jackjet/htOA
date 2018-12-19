package com.kwchina.oa.customer.service;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectInfor;
import com.kwchina.oa.customer.vo.ProjectInforVo;
import com.kwchina.oa.document.entity.DocumentInfor;

public interface ProjectInforManager extends BasicManager {
	
	//获取用户有权限看到的项目信息
	public List getProjectInfor(SystemUserInfor user);
	
	//select为0时：转化ProjectInfor为ProjectInforVo;select为1时：转化ProjectCategory为ProjectInforVo
	public ProjectInforVo transPOToVO(ProjectInfor projectInfor,ProjectCategory projectCategory,int select,int leftIndex,int rightIndex);
	
	// 转化ProjectInfor为ProjectInforVo
	public ProjectInforVo transPOToVO(ProjectInfor projectInfor);
		
	//保存项目信息(包括权限信息)
	public void saveProject(ProjectInfor projectInfor,ProjectInforVo projectInforVo);
	
	//判断用户对某个信息的操作权限
	public boolean hasRight(ProjectInfor projectInfor, SystemUserInfor systemUser, int rightBit);
}
