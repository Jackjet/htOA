package com.kwchina.oa.workflow.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;

public class WorkflowBaseController extends BasicController {
	
	
	//获取审核实例相关信息
	public void getProcessInfors(HttpServletRequest request, HttpServletResponse response, FlowInstanceInfor instance) throws Exception {
		
		request.setAttribute("_Instance", instance);
		
		//所有附件
		String allAttachment = "";
		
		//对申请人附件进行处理
		String attachmentFile = instance.getAttach();
		if (attachmentFile != null && attachmentFile.length() > 0) {
			
			allAttachment += attachmentFile + "|";
			
			String[][] attachment = processFile(attachmentFile);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		
		//获取部门审核人附件
		String managerAttachment = instance.getManagerAttachment();
		if (managerAttachment != null && managerAttachment.length() > 0) {
			
			allAttachment += managerAttachment + "|";
			
			String[][] attachment = processFile(managerAttachment);
			request.setAttribute("_ManagerAttachment_Names", attachment[1]);
			request.setAttribute("_ManagerAttachments", attachment[0]);
		}
		String viceManagerAttachment = instance.getViceManagerAttachment();
		if (viceManagerAttachment != null && viceManagerAttachment.length() > 0) {
			
			allAttachment += viceManagerAttachment + "|";
			
			String[][] attachment = processFile(viceManagerAttachment);
			request.setAttribute("_ViceManagerAttachment_Names", attachment[1]);
			request.setAttribute("_ViceManagerAttachments", attachment[0]);
		}
		
		//决议附件
		if (instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Board) {
			String resAttach = instance.getResAttach();
			if (resAttach != null && resAttach.length() > 0) {
				
				allAttachment += resAttach + "|";
				
				String[][] attachment = processFile(resAttach);
				request.setAttribute("_ResAttachment_Names", attachment[1]);
				request.setAttribute("_ResAttachments", attachment[0]);
			}
		}
		//对正式附件进行处理
		String formalAttach = instance.getFormalAttach();
		if (formalAttach != null && formalAttach.length() > 0) {
			
			allAttachment += formalAttach + "|";
			
			String[][] attachment = processFile(formalAttach);
			request.setAttribute("_FormalAttachment_Names", attachment[1]);
			request.setAttribute("_FormalAttachments", attachment[0]);
		}
		
		//流转过程中的附件
		String checkAttachment = "";
		Set checkLayerInfors = instance.getLayers();
		if (checkLayerInfors != null && checkLayerInfors.size() > 0) {
			for (Iterator it=checkLayerInfors.iterator();it.hasNext();) {
				InstanceLayerInfor tmpCheckLayer = (InstanceLayerInfor)it.next();
				
				Set checkInfors = tmpCheckLayer.getCheckInfors();
				if (checkInfors != null && checkInfors.size() > 0) {
					for (Iterator its=checkInfors.iterator();its.hasNext();) {
						InstanceCheckInfor tmpCheckInfor = (InstanceCheckInfor)its.next();
						String tmpAttatchment = tmpCheckInfor.getAttatchment();
						
						if (tmpAttatchment!= null && !("").equals(tmpAttatchment)) {
							
							allAttachment += tmpAttatchment + "|";
							
//							String[][] attachment = processFile(tmpAttatchment);
							
						}						
					}
				}
			}
		}
		
		
		//所有附件
		if(StringUtil.isNotEmpty(allAttachment)){
			String[][] allAttaArray = processFile(allAttachment);
			request.setAttribute("_AllAttachment_Names", allAttaArray[1]);
			request.setAttribute("_AllAttachments", allAttaArray[0]);
		}
		
		//获取流程信息
		FlowDefinition flow = (FlowDefinition)instance.getFlowDefinition();
		request.setAttribute("_Flow", flow);
		
	}
	
	
}