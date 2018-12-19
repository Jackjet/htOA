package com.kwchina.oa.personal.message.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.personal.message.entity.MessageDeleted;
import com.kwchina.oa.personal.message.entity.MessageInfor;
import com.kwchina.oa.personal.message.entity.MessageReply;
import com.kwchina.oa.personal.message.entity.ReceiveMessage;
import com.kwchina.oa.personal.message.service.MessageInforManager;
import com.kwchina.oa.personal.message.service.MessageReplyManager;
import com.kwchina.oa.personal.message.service.ReceiveMessageManager;
import com.kwchina.oa.personal.message.vo.MessageInforVO;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/personal/receiveMessage.do")
public class ReceiveMessageController extends BasicController {

	@Resource
	private InforDocumentManager inforDocumentManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private MessageInforManager messageInforManager = null;

	@Resource
	private ReceiveMessageManager receiveMessageManager = null;

	@Resource
	private MessageReplyManager messageReplyManager = null;

	/***************************************************************************
	 * 显示用户收到的讯息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listRecive")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		queryString[1] = "select count(messageId) from MessageInfor  where 1=1";
		queryString[0] = "from MessageInfor messageInfor where 1=1";
		condition = " and ";
		condition += " messageId not in (select message.messageId from MessageDeleted  where user.personId='" + user.getPersonId() + "')";
		condition += " and (messageId in (select message.messageId from ReceiveMessage  where receiver.personId='" + user.getPersonId() + "')";
		condition +=" or (receiveType=0 and sender.personId!='" + user.getPersonId() + "'))";
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.inforDocumentManager.generateQueryString(queryString, getSearchParams(request));
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
			String querySQL = " from ReceiveMessage receive where 1=1 and receive.message.messageId in (" + messageIds + ") and receiver.personId="+user.getPersonId();
			List receives = this.receiveMessageManager.getResultByQueryString(querySQL);

			// 双重循环，判断各个消息是否阅读
			for (Iterator it = list.iterator(); it.hasNext();) {
				MessageInfor message = (MessageInfor) it.next();
				int tempId = message.getMessageId();

				// 把查询到的结果转化为VO
				MessageInforVO vo = this.messageInforManager.transPOToVO(message);
				for (Iterator itR = receives.iterator(); itR.hasNext();) {
					ReceiveMessage receive = (ReceiveMessage) itR.next();
					int theId = receive.getMessage().getMessageId();
					if (tempId == theId && receive.getIsReaded() == 1) {
						vo.setIsReaded(1);
						break;
					}
				}
				// 查出接收者
				if (message.getReceiveType() == 0) {
					String receivePersonName = "公司全体";
					vo.setPersonNames(receivePersonName);
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
					vo.setPersonNames(receivePersonName);

				}
				messageVos.add(vo);
			}
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		JSONConvert convert = new JSONConvert();

		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		rows = convert.modelCollect2JSONArray(messageVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}


	/***************************************************************************
	 * 查看选中的接收讯息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewReceiveMesage")
	public String view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		int messageId = Integer.parseInt(request.getParameter("rowId"));
		MessageInfor messageInfor = (MessageInfor) this.messageInforManager.get(messageId);
		request.setAttribute("_message", messageInfor);
		String querySQL = " from ReceiveMessage receive where 1=1 " + "and receive.message.messageId =" + messageId + "and receive.receiver.personId=" + user.getPersonId();
		List receives = this.receiveMessageManager.getResultByQueryString(querySQL);
		ReceiveMessage receiveMessage = new ReceiveMessage();
		
//		 查看接收者
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

		// 如果receives为空，则此消为公共接收消息，所以要在receive表中添加一条当前用户的阅读状态，否则直接修改receive表中的信息
		if (receives.size() != 0) {
			receiveMessage = (ReceiveMessage) receives.get(0);
			if (receiveMessage.getIsReaded() == 0) {

				// 保存当前用户对此信息是已经阅读状态
				receiveMessage.setIsReaded(1);
				long time = System.currentTimeMillis();
				Timestamp readTime = new Timestamp(time);
				receiveMessage.setReadTime(readTime);
				this.receiveMessageManager.save(receiveMessage);
			}
		} else {
			receiveMessage.setIsReaded(1);
			receiveMessage.setMessage(messageInfor);
			long time = System.currentTimeMillis();
			Timestamp readTime = new Timestamp(time);
			receiveMessage.setReadTime(readTime);
			receiveMessage.setReceiver(user);
			this.receiveMessageManager.save(receiveMessage);
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

		return "message/viewReceiveMessage";
	}

	/***************************************************************************
	 * 回复讯息
	 */
	@RequestMapping(params = "method=replyMessage")
	public String replyMessage(@ModelAttribute("messageInforVo")
	MessageInforVO messageInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int messageId = Integer.parseInt(request.getParameter("messageId"));

		// 取出要回复的讯息
		MessageInfor messageInfor = (MessageInfor) this.messageInforManager.get(messageId);
		model.addAttribute("_message", messageInfor);
		try {
			BeanUtils.copyProperties(messageInforVo, messageInfor);
			messageInforVo.setSenderId(messageInfor.getSender().getPersonId());
			messageInforVo.setSendTimeStr(messageInfor.getSendTime().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 对附件信息进行处理
		String attachmentFile = messageInfor.getAttachment();
		if (attachmentFile != null && !attachmentFile.equals("")) {
			String[][] attachment = processFile(attachmentFile);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}

		// 查看回复的信息
		List list = this.messageReplyManager.getReplyMessage(messageId);
		model.addAttribute("_replyList", list);

		return "message/replyMessage";
	}

	/***************************************************************************
	 * 保存回复信息
	 * 
	 * @param messageInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveReplyMessage")
	public String save(@ModelAttribute("messageInforVo")
	MessageInforVO messageInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		int messageId = Integer.parseInt(request.getParameter("messageId"));
		String replyContent = messageInforVo.getReplyContent();
		MessageInfor message = new MessageInfor();

		// 如果回复栏不为空，则添加回复信息
		if (replyContent != null && !replyContent.equals("")) {
			MessageReply messageReply = new MessageReply();
			message = (MessageInfor) this.messageInforManager.get(messageId);

			// 回复时间
			long time = System.currentTimeMillis();
			Timestamp replyTime = new Timestamp(time);
			messageReply.setReplyTime(replyTime);
			messageReply.setMessage(message);
			messageReply.setReplyContent(replyContent);
			messageReply.setRestorer(user);
			this.messageReplyManager.save(messageReply);
		}

		// 判断此消息相对当前用户为接收者还是发送者,相等则为发送者，否则为接收者
		if ((message.getSender().getPersonId() - user.getPersonId()) != 0) {
			return "redirect:receiveMessage.do?method=viewReceiveMesage&rowId=" + messageId;
		}
		return "redirect:messageInfor.do?method=viewSendMesage&rowId=" + messageId;

	}

	/***************************************************************************
	 * 批量删除接收到的讯息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=receiveDelete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("rowIds");
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);

					// 获取被删除讯息
					MessageInfor message = (MessageInfor) this.messageInforManager.get(deleteId);

					// 如果是公共讯息，则要在删除表上添加一条信息
					//if (message.getReceiveType() == 0) {
						MessageDeleted messageDeleted = new MessageDeleted();
						messageDeleted.setMessage(message);
						messageDeleted.setUser(user);
						this.receiveMessageManager.save(messageDeleted);
					//}
					// 如果是个别讯息
					//else {
						// 删除附件
						//String filePaths = message.getAttachment();
						//deleteFiles(filePaths);
						//this.messageInforManager.remove(message);
					//}
				}
			}
		}
		return "message/listReceiveMessage";
	}

}
