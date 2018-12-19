package com.kwchina.core.base.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.DataRightInforDAO;
import com.kwchina.core.base.entity.DataRightInfor;
import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.DataRightInforManager;
import com.kwchina.core.base.service.OperationDefinitionManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.VirtualResourceManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Service
public class DataRightInforManagerImpl extends BasicManagerImpl<DataRightInfor> implements DataRightInforManager {
	
	private DataRightInforDAO dataRightInforDAO;
	
	@Resource
	private VirtualResourceManager resourceManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private OperationDefinitionManager operationDefinitionManager;

	@Autowired
	public void setDataRightInforDAO(DataRightInforDAO dataRightInforDAO) {
		this.dataRightInforDAO = dataRightInforDAO;
		super.setDao(dataRightInforDAO);
	}

	//判断是否具有数据权限
	public void haveDataRight(HttpServletRequest request, HttpServletResponse response, String comparePrimarykey, int compareDataId, String compareMethodName) {
		boolean hasRight = false;
		
		String pathURI = request.getRequestURI();
		VirtualResource virtualResource = resourceManager.getVirtualResource(pathURI);
		if (virtualResource != null) {
			/** 根据指定资源查找是否存在指定主键的权限信息(存在:则需要进行权限判断;不存在:则说明没有设定权限.) */
			String primarykeyName = virtualResource.getPrimarykeyName();
			if (comparePrimarykey.equals(primarykeyName)) {
				//进行权限判断
				Set<DataRightInfor> dataRights = virtualResource.getDataRights();
				boolean hasDataId = false;
				for (DataRightInfor dataRight : dataRights) {
					int dataId = dataRight.getDataId();
					if (dataId == compareDataId) {
						hasDataId = true;
						//判断用户是否在该dataId的权限信息内
						boolean belongRight = false;
						SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
						if (dataRight.getRole() != null) {
							RoleInfor role = dataRight.getRole();
							belongRight = this.roleManager.belongRole(systemUser, role);
						}else if (dataRight.getUser() != null) {
							if (systemUser.getPersonId().intValue() == dataRight.getUser().getPersonId().intValue()) {
								belongRight = true;
							}
						}
						//当用户在该dataId的权限信息内时,才进行权限判断
						if (belongRight) {
							OperationDefinition od = this.operationDefinitionManager.getOperationByMethod(compareMethodName);
							if (od != null) {
								long rightData = dataRight.getRightData();
								int position = od.getPosition();
								long a = rightData >> (position - 1);
								long result = (a & 1);
								if (result == 1) {
									hasRight = true;
									break;
								}
							}
						}
					}
				}
				//若不存在该dataId的权限信息,则权限值为true
				if (!hasDataId) {
					hasRight = true;
				}
			}else {
				//未设定权限,则权限值为true
				hasRight = true;
			}
		}else {
			hasRight = true;
		}
		
		if (!hasRight) {
			response.setContentType(CoreConstant.CONTENT_TYPE);
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.print("<script language='javascript'>");
			out.print("alert('无权进行该操作,请与系统管理员联系!');");
			out.print("window.history.go(-1)");
			out.print("</script>");
		}
	}
	
}
