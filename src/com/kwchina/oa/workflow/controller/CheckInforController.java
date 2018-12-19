package com.kwchina.oa.workflow.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwchina.sms.service.SmsManager;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.ApproveSentenceManager;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.service.FlowCheckInforManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.ModifyInforManager;
import com.kwchina.oa.workflow.vo.InstanceCheckInforVo;
import com.kwchina.sms.entity.SMSMessagesToSend;
//import com.kwchina.sms.service.SmsManager;

@Controller
@RequestMapping("/workflow/checkInfor.do")
public class CheckInforController extends WorkflowBaseController {
	
	@Resource
	private FlowCheckInforManager flowCheckInforManager;
	
	@Resource
	private FlowInstanceManager flowInstanceManager;
	
	@Resource
	private ApproveSentenceManager approveSentenceManager;
	
	@Resource
	private SmsManager smsManager;
	
	@Resource
	private ModifyInforManager modifyInforManager;
	
	
	
	//编辑审核信息
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, InstanceCheckInforVo vo) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		Integer checkId = vo.getCheckId();
		
		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);
		
		if (instanceId != null && instanceId.length() > 0) {
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			//主办人
			//SystemUserInfor charger = instance.getFlowDefinition().getCharger();
			SystemUserInfor charger = instance.getCharger();
			int chargerId = charger.getPersonId().intValue();
			boolean isCharger = false;
			if (userId == chargerId) {
				isCharger = true;
			}
			request.setAttribute("_IsCharger", isCharger);
			
			//获取审核实例相关信息
			getProcessInfors(request, response, instance);
			
			if (checkId != null && checkId.intValue() > 0) {
				/** 主办人修改审核信息时 */
				InstanceCheckInfor check = (InstanceCheckInfor)this.flowCheckInforManager.get(checkId);
				
				BeanUtils.copyProperties(vo, check);
				
				//审核时间
				request.setAttribute("_CheckDate", check.getEndDate());
				
				//附件
				request.setAttribute("_CanUpload", true);
				if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
					String attachmentFile = check.getAttatchment();
					if (attachmentFile != null && !attachmentFile.equals("")) {
						String[][] attachment = processFile(attachmentFile);
						request.setAttribute("_CheckAttachment_Names", attachment[1]);
						request.setAttribute("_CheckAttachments", attachment[0]);
					}
				}
			}else {
				/** 审核人审核时 */
				//获取该审核实例目前正在处理的审核层
				List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
				
				if (rLayers != null && rLayers.size() > 0) {
					for (Iterator it=rLayers.iterator();it.hasNext();) {
						InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
						Set checks = layer.getCheckInfors();
						
						//判断是否存在该用户需要审核的信息
						boolean isChecker = false;
						for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
							InstanceCheckInfor check = (InstanceCheckInfor)itCheck.next();
							
							if(check.getStatus()==0){
								int ss=layer.getStatus();
								int checkerId = check.getChecker().getPersonId().intValue();
								if (userId == checkerId && ss !=3) {
									BeanUtils.copyProperties(vo, check);
									
									//审核时间
									request.setAttribute("_CheckDate", check.getEndDate());
									
									//判断是否可以上传附件
									boolean canUpload = false;
									InstanceLayerInfor layerInfor = check.getLayerInfor();
									if (layerInfor.getFlowNode() != null) {
										if (layerInfor.getFlowNode().isUpload()) {
											canUpload = true;
										}
									}else {
										canUpload = true;
									}
									request.setAttribute("_CanUpload", canUpload);
									
									//处理附件信息
									if (canUpload) {
										if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
											String attachmentFile = check.getAttatchment();
											if (attachmentFile != null && !attachmentFile.equals("")) {
												String[][] attachment = processFile(attachmentFile);
												request.setAttribute("_CheckAttachment_Names", attachment[1]);
												request.setAttribute("_CheckAttachments", attachment[0]);
											}
										}
									}
									isChecker = true;
									break;
								}
							}
						
							
							if (isChecker) {
								break;
							}
						}
					}
				}
			}
		}
		
		
		return "editCheckInfor";
	}
	
	//保存审核信息
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, 
			InstanceCheckInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		Integer checkId = vo.getCheckId();
		Integer instanceId = null;
		boolean isCharger = false;
		int flowType = 0;
		//是否为主办人修改审核信息
		String chargerEdit = request.getParameter("chargerEdit");
		
		if (checkId != null && checkId.intValue() > 0) {
			InstanceCheckInfor check = (InstanceCheckInfor)this.flowCheckInforManager.get(checkId);
			
			BeanUtils.copyProperties(check, vo);
			
			String oldFiles = "";
			//对原附件进行处理
			String filePaths = check.getAttatchment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			
			//新提交的附件
			String attachment = this.uploadAttachment(multipartRequest, "workflow");
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			check.setAttatchment(attachment);
			
			String checkDateStr = request.getParameter("checkDate");
			Timestamp checkDate = null;
			if (checkDateStr != null && checkDateStr.length() > 0) {
				checkDate = Timestamp.valueOf(checkDateStr);
			}
			
			check = (InstanceCheckInfor)this.flowCheckInforManager.saveCheckInfor(check, checkDate);
			
			instanceId = check.getLayerInfor().getInstance().getInstanceId();
			
			//判断是否为主办人
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			FlowInstanceInfor instance = check.getLayerInfor().getInstance();
			//int chargerId = instance.getFlowDefinition().getCharger().getPersonId();
			int chargerId = instance.getCharger().getPersonId();
			if (chargerId == userId) {
				isCharger = true;
			}

			//判断流程类型为'固定'还是'人工'
			FlowDefinition flow = instance.getFlowDefinition();
			flowType = flow.getFlowType();
		}
		
		if (("true").equals(chargerEdit)) {
			/** 如果为主办人修改审核信息,则不作下一步操作 */
			return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
		}else {
			if (flowType == 0) {
				if (isCharger) {
					//为主办人时发送标志符needValidate到页面,用于调用方法nextNode
					return "redirect:instanceInfor.do?method=view&needValidate=true&instanceId=" + instanceId;
				}else {
					//非主办人则跳转到下一步操作(也包含验证处理)
					return flowToNextNode(request, response, instanceId);
				}
			}else {
				//flowType为1时需要主办人手动处理
				return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
			}
		}
			
	}
	
	/**
	 * 保存审核信息(移动端)
	 * @param request
	 * @param response
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save_m")
	public void save_m(HttpServletRequest request, HttpServletResponse response, 
			InstanceCheckInforVo vo) throws Exception {
		JSONObject jsonObj = new JSONObject();
		Integer checkId = vo.getCheckId();
		Integer instanceId = null;
		
		try {
			if (checkId != null && checkId.intValue() > 0) {
				InstanceCheckInfor check = (InstanceCheckInfor)this.flowCheckInforManager.get(checkId);
				
				BeanUtils.copyProperties(check, vo);
				
				check.setAttatchment("");
				
				Timestamp checkDate = new Timestamp(System.currentTimeMillis());
				
				check = (InstanceCheckInfor)this.flowCheckInforManager.saveCheckInfor(check, checkDate);
				
				instanceId = check.getLayerInfor().getInstance().getInstanceId();
				
				//非主办人则跳转到下一步操作(也包含验证处理)
				if (instanceId != null && instanceId.intValue() > 0) {
					FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
					if(instance != null && instance.getFlowDefinition().getFlowType() == 0){
						this.flowInstanceManager.nextValidate(instance);
					}
					
				}
				
				//是否成功标志
				jsonObj.put("_RtnTag", 1);
				
			}
		} catch (RuntimeException e) {
			//是否成功标志
			jsonObj.put("_RtnTag",  0);
			e.printStackTrace();
		}
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
		
	}
	
	

	//跳转到下一节点
	public String flowToNextNode(HttpServletRequest request, HttpServletResponse response, Integer instanceId) {
		
		if (instanceId != null && instanceId.intValue() > 0) {
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			
			this.flowInstanceManager.nextValidate(instance);
		}
		
		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
	}
	
	
	//编辑短息
	@RequestMapping(params = "method=sms")
	public String sms(HttpServletRequest request, HttpServletResponse response, InstanceCheckInforVo vo) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		Integer checkId = vo.getCheckId();
		
		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);
		
		if (instanceId != null && instanceId.length() > 0) {
			
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			//主办人
			//SystemUserInfor charger = instance.getFlowDefinition().getCharger();
			SystemUserInfor charger = instance.getCharger();
			int chargerId = charger.getPersonId().intValue();
			boolean isCharger = false;
			if (userId == chargerId) {
				isCharger = true;
			}
			request.setAttribute("_IsCharger", isCharger);
			
			//获取审核实例相关信息
			getProcessInfors(request, response, instance);
			
			String content = "请您及时审批文件："+instance.getInstanceTitle();
			request.setAttribute("_Content", content);
			
		}
		
		
		return "editSms";
	}
	
	

	//编辑短息
	@RequestMapping(params = "method=saveSms")
	public String saveSms(HttpServletRequest request, HttpServletResponse response, InstanceCheckInforVo vo) throws Exception {
		SMSMessagesToSend sms = new SMSMessagesToSend();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String content = request.getParameter("content");
		String mobiles = request.getParameter("mobiles").trim();
		sms.setMessageText(content);
		sms.setMobileNos(mobiles);
		sms.setScheduleDate(new Timestamp(System.currentTimeMillis()));
		sms.setStatus(0);
		sms.setTransmitStatus(0);
		sms.setApplier(systemUser.getPerson().getPersonName());
		this.smsManager.save(sms);
		
		

		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		// out.print("window.returnValue = 'Y';");
		//out.print("window.opener.location.reload();");
		out.print("window.close();");
		out.print("</script>");

		return null;
		
		
	}
	
//	记录
	@RequestMapping(params = "method=getModify")
	public String getModify(HttpServletRequest request, HttpServletResponse response, InstanceCheckInforVo vo) throws Exception {
		
		String instanceId = request.getParameter("instanceId");
		
		Integer checkId = vo.getCheckId();
		
		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);
		if (instanceId != null && instanceId.length() > 0) {
		
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();
			
			FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			
			//主办人
			//SystemUserInfor charger = instance.getFlowDefinition().getCharger();
			SystemUserInfor charger = instance.getCharger();
			int chargerId = charger.getPersonId().intValue();
			boolean isCharger = false;
			if (userId == chargerId) {
				isCharger = true;
			}
			request.setAttribute("_IsCharger", isCharger);
			
			//获取审核实例相关信息
			getProcessInfors(request, response, instance);
			


			
//			 查看回复的信息
			List list = this.modifyInforManager.getModifyInfor(Integer.valueOf(instanceId));
			request.setAttribute("_replyList", list);
			
		}
		return "viewModify";
	}
	
	
}