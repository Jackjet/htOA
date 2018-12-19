package com.kwchina.oa.personal.message.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.entity.ReceiveMessage;
import com.kwchina.oa.personal.message.service.MessageInforManager;
import com.kwchina.oa.personal.message.service.MessageReplyManager;
import com.kwchina.oa.personal.message.service.ReceiveMessageManager;
import com.kwchina.oa.personal.message.vo.MessageInforVO;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/personal/messageInfor.do")
public class MessageInforController extends BasicController {

	@Resource
	private InforDocumentManager inforDocumentManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private MessageInforManager messageInforManager = null;

	@Resource
	private ReceiveMessageManager receiveMessageManager = null;

	@Resource
	private OrganizeManager organizeManager;

	@Resource
	private MessageReplyManager messageReplyManager = null;
	
	@Resource
	private RoleManager roleManager = null;

	/***************************************************************************
	 * 显示用户发出去的讯息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listSend")
	public void list(@ModelAttribute("messageInforVo")
	MessageInforVO messageInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		queryString[1] = "select count(messageId) from MessageInfor  where 1=1";
		queryString[0] = "from MessageInfor messageInfor where 1=1";
		condition = " and sender.personId='" + user.getPersonId() + "' and isDelete = 0";
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.messageInforManager.generateQueryString(queryString, getSearchParams(request));
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.messageInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();

		// 把查询到的结果转化为VO
		List messageVos = new ArrayList();
		if (list.size() > 0) {
			String messageIds = "";
			for (Iterator it = list.iterator(); it.hasNext();) {
				MessageInfor message = (MessageInfor) it.next();

				messageIds += message.getMessageId() + ",";
			}
			messageIds = messageIds.substring(0, messageIds.length() - 1);

			// 判断各个消息是否有回复
			for (Iterator it = list.iterator(); it.hasNext();) {
				MessageInfor message = (MessageInfor) it.next();
				int tempId = message.getMessageId();
				List replys = this.messageReplyManager.getReplyMessage(tempId);
				// 把查询到的结果转化为VO
				messageInforVo = this.messageInforManager.transPOToVO(message);

				if (replys.size() != 0) {
					messageInforVo.setIsReply(1);
				} else {
					messageInforVo.setIsReply(0);
				}
				// 查出接收者
				if (message.getReceiveType() == 0) {
					String receivePersonName = "公司全体";
					messageInforVo.setPersonNames(receivePersonName);
				} else {
					// +" order by receive.receiver.person.personpositionLayer"
					String SQL = " from ReceiveMessage receive where 1=1 and receive.message.messageId =" + tempId;
					List receivesName = this.receiveMessageManager.getResultByQueryString(SQL);
					String receivePersonName = "";
					
					//如果是分组收件人，刚显示角色名
					if(message.getReceiveType() == 2){
						List roleNameList = new ArrayList();
						for(int i=0;i<receivesName.size();i++){
							ReceiveMessage receive = (ReceiveMessage) receivesName.get(i);
							String tmpRoleName = receive.getRole().getRoleName();
							roleNameList.add(tmpRoleName);
						}
						//去掉重复元素
						roleNameList = ArrayUtil.removeDuplicateWithOrder(roleNameList);
						//组合
						for(Iterator iterator = roleNameList.iterator();iterator.hasNext();){
							receivePersonName += "|"+iterator.next();
						}
					}else{
						for (int i = 0; i < receivesName.size(); i++) {
							ReceiveMessage receive = (ReceiveMessage) receivesName.get(i);
							receivePersonName += "|" + receive.getReceiver().getPerson().getPersonName();
						}
					}
					messageInforVo.setPersonNames(receivePersonName);
				}
				messageVos.add(messageInforVo);
			}
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		// awareObject.add("sender.person");
		rows = convert.modelCollect2JSONArray(messageVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/***************************************************************************
	 * 批量删除发送出去的讯息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=sendDelete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);
					// 获取被删除讯息
					MessageInfor message = (MessageInfor) this.messageInforManager.get(deleteId);
					message.setIsDelete(1);
					this.messageInforManager.save(message);
					// 删除附件
					//String filePaths = message.getAttachment();
					//deleteFiles(filePaths);
					//this.messageInforManager.remove(message);
				}
			}
		}
		return "listSendMessage";
	}

	/***************************************************************************
	 * 编辑讯息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=sendMessage")
	public String edit(@ModelAttribute("messageInforVo")
	MessageInforVO messageInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		MessageInfor message = new MessageInfor();
		int messageId = messageInforVo.getMessageId();
		String handle = request.getParameter("handle");
		request.setAttribute("_Handle", handle);
		
		//获取全部用户
		List users = this.systemUserManager.getAll();
		model.addAttribute("_Users", users);
		
		//获取全部角色
		List roles = this.roleManager.getRolesByName("邮件");
		
		//将自定义设为默认显示的
		messageInforVo.setReceiveType(1);

		if (messageId != 0) {
			// 修改或转发的情况
			message = (MessageInfor) messageInforManager.get(messageId);
			request.setAttribute("_Message", message);
			if (message != null) {
				// 首先判断当前用户是否有权限进行编辑
				Boolean canEdit = this.messageInforManager.canDeleteOrEditMessage(user, message);
				if (("transmit").equals(handle)) {
					canEdit = true;
				}
				if (!canEdit) {
					return null;
				}
			}
			try {
				BeanUtils.copyProperties(messageInforVo, message);

				messageInforVo.setSenderId(message.getSender().getPersonId());
				messageInforVo.setSendTimeStr(message.getSendTime().toString());
				messageInforVo.setAttachmentStr(message.getAttachment());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 已经选择的发送人员
			int[] personIds = new int[users.size()];
			Set messageReceives = message.getReceives();
			for (int i = 0; i < users.size(); i++) {
				SystemUserInfor tempUser = (SystemUserInfor) users.get(i);
				int tempPersonId = tempUser.getPersonId().intValue();

				for (Iterator it = messageReceives.iterator(); it.hasNext();) {
					ReceiveMessage receive = (ReceiveMessage) it.next();
					int rPersonId = receive.getReceiver().getPersonId().intValue();

					if (tempPersonId == rPersonId) {
						// 该用户被选择
						personIds[i] = tempPersonId;
						break;
					}
				}
			}
			
			// 已经选择的分组信息
			int[] roleIds = new int[roles.size()];
//			Set messageReceives = message.getReceives();
			for (int i = 0; i < roles.size(); i++) {
				RoleInfor tempRole = (RoleInfor) roles.get(i);
				int tempRoleId = tempRole.getRoleId().intValue();

				for (Iterator it = messageReceives.iterator(); it.hasNext();) {
					ReceiveMessage receive = (ReceiveMessage) it.next();
					int rRoleId = 0;
					if (receive.getRole()!= null){
					rRoleId = receive.getRole().getRoleId().intValue();
					}
					if (tempRoleId == rRoleId) {
						// 该用户被选择
						roleIds[i] = tempRoleId;
						break;
					}
				}
			}
			
			if (!("transmit").equals(handle)) {
				messageInforVo.setPersonIds(personIds);
				messageInforVo.setRoles(roleIds);
			}
		}
		
		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);
		
		// 全部部门信息
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);
		
		/***********新添加：获取自定义分组收件人，即角色************/
		String roleNameStr = "邮件";
		//根据模糊查询中包含“邮件”，来获取邮件收件人角色信息
		List mailRole = this.roleManager.getRolesByName(roleNameStr);
		model.addAttribute("_MailRoles",mailRole);
		/*******************************************************/

		// 对附件信息进行处理
		String attachmentFile = message.getAttachment();
		if (attachmentFile != null && !attachmentFile.equals("")) {
			String[][] attachment = processFile(attachmentFile);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}

		return "message/editMessage";
	}

	/***************************************************************************
	 * 保存发送讯息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveSendMessage")
	public String save(@ModelAttribute("messageInforVo")
	MessageInforVO messageInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);

		MessageInfor message = new MessageInfor();
		int messageId = messageInforVo.getMessageId();
		String handle = request.getParameter("handle");
		
		// 如果handle不为空，则说明本次操作为修改
		if (("transmit").equals(handle)) {
			messageInforVo.setMessageId(0);
			messageId = 0;
		}
		String oldFiles = "";
		if (messageId != 0) {
			message = (MessageInfor) this.messageInforManager.get(messageId);
			
			// 修改信息时,对附件进行修改
			String filePaths = message.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

		} else {
			SystemUserInfor preUser = (SystemUserInfor) SysCommonMethod.getSystemUser(request);
			message.setSender(preUser);

			long time = System.currentTimeMillis();
			Timestamp timeNow = new Timestamp(time);
			message.setSendTime(timeNow);
		}

		// 转发处理，将原附件信息带入
		if (("transmit").equals(handle)) {
			
			// 修改信息时,对附件进行修改
			String filePaths = request.getParameter("attachment");
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
		}
		try {
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest, "message");

			BeanUtils.copyProperties(message, messageInforVo);
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			message.setAttachment(attachment);
			message.setSender(user);
			message.setIsDelete(0);

			if (messageInforVo.getMessageId() == null || messageInforVo.getMessageId() == 0) {
				// 设置发送时间
				message.setSendTime(new Timestamp(System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
			return "/common/error";
		}

		// 信息接收者
		int receiveType = messageInforVo.getReceiveType();
		if (receiveType == 0) {
			// 全公司,不需要处理
		}else if(receiveType  == 2){
			//自定义分组收件人,没有的加上，去掉的删除
			int[] roleIds = messageInforVo.getRoles();
			Set messageReceives = message.getReceives();

			
			// 没有的加上
			if (roleIds != null && roleIds.length != 0) {
				for (int kk = 0; kk < roleIds.length; kk++) {
					RoleInfor role = (RoleInfor)this.roleManager.get(roleIds[kk]);
					boolean hasThisRole = false;
					
					//判断是不是所有此角色的人员都在，即确定是此角色
					for (Iterator it = messageReceives.iterator(); it.hasNext();) {
						ReceiveMessage tmpReceive = (ReceiveMessage) it.next();

						RoleInfor tempRoleInfor = tmpReceive.getRole();
						int rRoleId = tempRoleInfor.getRoleId().intValue();
						if (rRoleId == roleIds[kk]) {
							hasThisRole = true;
							break;
						}
					}
					
					ReceiveMessage receive = new ReceiveMessage();

					if (!hasThisRole) {
						Set roleUsers = role.getUsers();
						for(Iterator it = roleUsers.iterator();it.hasNext();){
							SystemUserInfor tmpUser = (SystemUserInfor)it.next();

							ReceiveMessage tpReceive = new ReceiveMessage();
							tpReceive.setIsReaded(0);
							tpReceive.setMessage(message);
//							SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[kk]);
							tpReceive.setReceiver(tmpUser);
							tpReceive.setRole(role);
							messageReceives.add(tpReceive);
						}
					}
				}
			}
			
			// 此次选择中，去掉的并且尚未回复的删除
			ReceiveMessage[] arrayReceive = (ReceiveMessage[]) messageReceives.toArray(new ReceiveMessage[messageReceives.size()]);
			for (int k = arrayReceive.length - 1; k >= 0; k--) {
				ReceiveMessage receive = (ReceiveMessage) arrayReceive[k];
//				SystemUserInfor tempUser = receive.getReceiver();
//				int rPersonId = tempUser.getPersonId().intValue();
				int rRoleId = receive.getRole().getRoleId().intValue();

				boolean hasThisRole = false;
				if (roleIds != null && roleIds.length != 0) {
					for (int kk = 0; kk < roleIds.length; kk++) {
						if (rRoleId == roleIds[kk]) {
							hasThisRole = true;
							break;
						}
					}
				}

				if (!hasThisRole) {
					
					Set roleUsers = receive.getRole().getUsers();
					if(messageId != 0){
						List list = this.receiveMessageManager.getReceiveMessage(rRoleId, messageId);
						boolean canDelete = true;
						
						for(Iterator it = list.iterator();it.hasNext();){
							ReceiveMessage tmpReceiveMessage = (ReceiveMessage)it.next();
							
							if(tmpReceiveMessage.getIsReaded() == 1){
								canDelete = false;
							}
						}
						if(canDelete){
							for(Iterator it = list.iterator();it.hasNext();){
								ReceiveMessage tmpReceiveMessage = (ReceiveMessage)it.next();
								messageReceives.remove(tmpReceiveMessage);
								this.receiveMessageManager.remove(tmpReceiveMessage);
							}
						}
						
					}
					
				}
			}
			
		} else {
			// 自定义,没有的加上,去掉的删除
			// Set newReceives = new HashSet();

			int[] personIds = messageInforVo.getPersonIds();
			Set messageReceives = message.getReceives();

			// 此次选择中，去掉的并且尚未回复的删除
			ReceiveMessage[] arrayReceive = (ReceiveMessage[]) messageReceives.toArray(new ReceiveMessage[messageReceives.size()]);
			for (int k = arrayReceive.length - 1; k >= 0; k--) {
				ReceiveMessage receive = (ReceiveMessage) arrayReceive[k];
				SystemUserInfor tempUser = receive.getReceiver();
				int rPersonId = tempUser.getPersonId().intValue();

				boolean hasThisUser = false;
				if (personIds != null && personIds.length != 0) {
					for (int kk = 0; kk < personIds.length; kk++) {
						if (rPersonId == personIds[kk]) {
							hasThisUser = true;
							break;
						}
					}
				}

				if (!hasThisUser && receive.getIsReaded() == 0) {
					// this.receiveMessageManager.remove(receive);
					messageReceives.remove(receive);

					// remove from db
					this.receiveMessageManager.remove(receive);
				}
			}

			// 没有的加上
			if (personIds != null && personIds.length != 0) {
				for (int kk = 0; kk < personIds.length; kk++) {
					boolean hasThisUser = false;
					ReceiveMessage receive = new ReceiveMessage();

					for (Iterator it = messageReceives.iterator(); it.hasNext();) {
						receive = (ReceiveMessage) it.next();

						SystemUserInfor tempUser = receive.getReceiver();
						int rPersonId = tempUser.getPersonId().intValue();
						if (rPersonId == personIds[kk]) {
							hasThisUser = true;
							break;
						}
					}

					if (!hasThisUser) {
						ReceiveMessage tpReceive = new ReceiveMessage();
						tpReceive.setIsReaded(0);
						tpReceive.setMessage(message);
						SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[kk]);
						tpReceive.setReceiver(tpUser);
						messageReceives.add(tpReceive);
					}
				}
			}
		}

		this.messageInforManager.save(message);
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
//		out.print("window.opener.reloadTab2();");
		out.print("window.close();");
		out.print("</script>");

		return null;
	}

	/**
	 * 查看选中的发送讯息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewSendMesage")
	public String view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int messageId = Integer.parseInt(request.getParameter("rowId"));
		MessageInfor messageInfor = (MessageInfor) this.messageInforManager.get(messageId);
		request.setAttribute("_message", messageInfor);

		// 查看接收者
		String SQL = " from ReceiveMessage receive where 1=1 and receive.message.messageId =" + messageId;
		List receivesName = this.messageInforManager.getResultByQueryString(SQL);
		request.setAttribute("_receiverNames", receivesName);
		
		//如果是分组收件人
		if(messageInfor.getReceiveType() == 2){
			String displayName = "";
			List roleNameList = new ArrayList(); 
			for(Iterator it = receivesName.iterator();it.hasNext();){
				ReceiveMessage tmpReceive = (ReceiveMessage)it.next();
				roleNameList.add(tmpReceive.getRole().getRoleName());
			}	
			//去掉重复
			roleNameList = ArrayUtil.removeDuplicateWithOrder(roleNameList);
			for(Iterator it2 = roleNameList.iterator();it2.hasNext();){
				displayName += it2.next();
				if(it2.hasNext()){
					displayName += "、";
				}
			}
			request.setAttribute("_RoleName", displayName);
		}

		// 查看回复的信息
		List list = this.messageReplyManager.getReplyMessage(messageId);
		request.setAttribute("_replyList", list);

		// 对附件信息进行处理
		String attachmentFile = messageInfor.getAttachment();
		if (attachmentFile != null && !attachmentFile.equals("")) {
			String[][] attachment = processFile(attachmentFile);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		return "message/viewSendMessage";
	}
	
	/**
	 * 显示所有未读信息(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listNotRead")
	public void listNotRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		Integer personId = user.getPersonId();
		
		/**
		 * 个人讯息
		 * 1. 发送给全公司,发送人不为自己(A.Receive表中建立了信息,但自己尚未阅读的;B.Receive表中尚未建立信息的)
		 * 2. 接收者中包含自己,自己尚未阅读的
		 */
		String queryHQL = " from MessageInfor message where message.messageId not in(select messageDeleted.message.messageId from MessageDeleted messageDeleted where messageDeleted.user.personId = " + personId + ")";
		
		//1. 发送给全公司,发送人不为自己(A.Receive表中建立了信息,但自己尚未阅读的;B.Receive表中尚未建立信息的)
		queryHQL += " and ((message.receiveType=0 and message.sender.personId != " + personId +
				"and (message.messageId in (select receive.message.messageId from ReceiveMessage receive where receive.receiver.personId=" + personId + " and receive.isReaded=0)" +
				" or (select count(receive.message.messageId) from ReceiveMessage receive where receive.message.messageId = message.messageId and receive.receiver.personId = "+personId+")=0))";
		//2. 接收者中包含自己,自己尚未阅读的
		queryHQL += " or (message.receiveType=1 " +
				"and  message.messageId in (select receive.message.messageId from ReceiveMessage receive where receive.receiver.personId=" + personId +" and receive.isReaded=0))) order by message.sendTime desc";
		
		List<MessageInfor> messages = this.messageInforManager.getResultByQueryString(queryHQL);
		
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		for (MessageInfor message : messages) {
			JSONObject json = new JSONObject();
			json.put("messageId", message.getMessageId());
			json.put("messageTitle", message.getMessageTitle());
			json.put("senderName", message.getSender().getPerson().getPersonName());
			jsonArray.add(json);
		}
		jsonObj.put("_Messages", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}

}
