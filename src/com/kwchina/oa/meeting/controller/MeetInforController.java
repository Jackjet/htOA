package com.kwchina.oa.meeting.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwchina.core.base.service.PersonInforManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.File;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.oa.meeting.entity.AttendInfor;
import com.kwchina.oa.meeting.entity.MeetInfor;
import com.kwchina.oa.meeting.service.AttendInforManager;
import com.kwchina.oa.meeting.service.MeetInforManager;
import com.kwchina.oa.meeting.util.EmailUtil;
import com.kwchina.oa.meeting.vo.MeetInforVo;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.sms.entity.SMSMessagesToSend;
import com.kwchina.sms.service.SmsManager;
//import com.kwchina.sms.service.SmsManager;

@Controller
@RequestMapping("/meeting/meetInfor.do")
public class MeetInforController extends BasicController {

	@Resource
	private InforDocumentManager inforDocumentManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private MeetInforManager meetInforManager;

	@Resource
	private AttendInforManager attendInforManager;

	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private SmsManager smsManager;
	
	@Resource
	private AppModuleLogManager appModuleLogManager;

	@Autowired
	private PersonInforManager personInforManager;

	/**
	 * 显示会议信息
	 * 
	 */

	@RequestMapping(params = "method=list")
	public void list(@ModelAttribute("meetInforVo")
	MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//SystemUserInfor user = SysCommonMethod.getSystemUser(request);

		//标签，标记是否为app端发出的请求
		String tag = request.getParameter("tag");
		boolean isApp = false;
		if(tag != null && !tag.equals("")){
			if(tag.equals("app")){
				isApp = true;
			}
		}
		if(isApp){
			if(StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
				SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
				/************记录app模块使用日志************/ 
				AppModuleLog appModuleLog = new AppModuleLog();
				appModuleLog.setModuleName("公司会议");
				appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
				appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
				appModuleLog.setUserName(systemUser.getUserName());
				this.appModuleLogManager.save(appModuleLog);
				/*****************************************/
			}
		}
		
		
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		queryString[1] = "select count(meetId) from MeetInfor  where 1=1";
		queryString[0] = "from MeetInfor meetInfor where 1=1";
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.meetInforManager.generateQueryString(queryString, getSearchParams(request));
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.meetInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();

		// 把查询到的结果转化为VO
		List meetVos = new ArrayList();
		if (list.size() > 0) {

			for (Iterator it = list.iterator(); it.hasNext();) {
				MeetInfor meet = (MeetInfor) it.next();

				// 把查询到的结果转化为VO
				meetInforVo = this.meetInforManager.transPOToVO(meet);
				meetVos.add(meetInforVo);
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
		rows = convert.modelCollect2JSONArray(meetVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/**
	 * 编辑会议信息
	 * 
	 * @param meetInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("meetInforVo")
	MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MeetInfor meet = new MeetInfor();
		Integer meetId = meetInforVo.getMeetId();
		String handle = request.getParameter("handle");
		request.setAttribute("_Handle", handle);

		// 获取全部用户
		List users = this.systemUserManager.getAll();
		model.addAttribute("_Users", users);

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);
		if (meetId != null && meetId.intValue() != 0) {
			// 修改的情况
			meet = (MeetInfor) meetInforManager.get(meetId);
			request.setAttribute("_Meet", meet);

			try {
				BeanUtils.copyProperties(meetInforVo, meet);
				if (meet.getBookPerson() != null) {
					meetInforVo.setBookId(meet.getBookPerson().getPersonId());
				}
				if (meet.getOrganize() != null) {
					meetInforVo.setOrganizeId(meet.getOrganize().getOrganizeId());
				}
				if (meet.getRecordUser() != null) {
					meetInforVo.setRecordId(meet.getRecordUser().getPersonId());
				}
				if (meet.getAuthor() != null) {
					meetInforVo.setAuthorId(meet.getAuthor().getPersonId());
				}
				meetInforVo.setAttachmentStr(meet.getAttachment());

			} catch (Exception e) {
				e.printStackTrace();
			}

			// 已经选择的参加提醒人员

			int[] personIds = new int[users.size()];
			Set attendInfors = meet.getAttendInfors();
			for (int i = 0; i < users.size(); i++) {
				SystemUserInfor tempUser = (SystemUserInfor) users.get(i);
				int tempPersonId = tempUser.getPersonId().intValue();

				for (Iterator it = attendInfors.iterator(); it.hasNext();) {
					AttendInfor attend = (AttendInfor) it.next();
					int rPersonId = attend.getPerson().getPersonId().intValue();

					if (tempPersonId == rPersonId) {
						// 该用户被选择
						personIds[i] = tempPersonId;
						break;
					}
				}
			}
			if (!("transmit").equals(handle)) {
				meetInforVo.setAttendIds(personIds);
			}
		} else {
			// 默认预定人、会议记录者为系统用户
			SystemUserInfor user = SysCommonMethod.getSystemUser(request);
			Integer userId = user.getPersonId();
			meetInforVo.setBookId(userId);
			meetInforVo.setRecordId(userId);
		}

		// 全部部门信息
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);

		String attachmentFile = meet.getAttachment();
		if (attachmentFile != null && (!attachmentFile.equals(""))) {
			String[] arrayFiles = attachmentFile.split("\\|");
			meetInforVo.setAttatchmentArray(arrayFiles);

			String[] attachmentNames = new String[arrayFiles.length];
			for (int k = 0; k < arrayFiles.length; k++) {
				attachmentNames[k] = File.getFileName(arrayFiles[k]);
			}
			request.setAttribute("_Attachment_Names", attachmentNames);
		}
		request.setAttribute("_EndMeetDate", meet.getEndMeetDate());
		return "editMeeting";
	}

	/***************************************************************************
	 * 保存会议
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("meetInforVo")
	MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		SystemUserInfor user = SysCommonMethod.getSystemUser(request);

		MeetInfor meet = new MeetInfor();
		int meetId = meetInforVo.getMeetId();
		int organizeId = meetInforVo.getOrganizeId();
		int bookId = meetInforVo.getBookId();
		int recordId = meetInforVo.getRecordId();
		String handle = request.getParameter("handle");
		if (("transmit").equals(handle)) {
			meetInforVo.getMeetId();
			meetId = 0;
		}
		String oldFiles = "";
		if (meetId != 0) {
			meet = (MeetInfor) this.meetInforManager.get(meetId);

			// 修改信息时,对附件进行修改
			String filePaths = meet.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

		} /*
			 * else { SystemUserInfor preUser = (SystemUserInfor)
			 * SysCommonMethod.getSystemUser(request); meet.setAuthor(preUser); }
			 */

		meet.setMeetDate(Date.valueOf(meetInforVo.getMeetDate().toString()));
		Date endMeetDate = null;
		if(request.getParameter("endMeetDate")!=null&&!request.getParameter("endMeetDate").toString().equals("")){
			endMeetDate = Date.valueOf(request.getParameter("endMeetDate").toString());
		}
		
//		if(!meetInforVo.getEndMeetDate().toString().equals("")){
//			endMeetDate = Date.valueOf(meetInforVo.getEndMeetDate().toString());
//		}else{
//			//由于BeanUtils.copyProperties处理日期为空时又错
//			meetInforVo.setEndMeetDate("0000-00-00");
//		}
		meet.setEndMeetDate(endMeetDate);
		meet.setStartHour(meetInforVo.getStartHour());

		if (organizeId != 0) {
			OrganizeInfor organize = (OrganizeInfor) this.organizeManager.get(organizeId);
			meet.setOrganize(organize);
		}

		if (bookId != 0) {
			SystemUserInfor bookPerson = (SystemUserInfor) this.systemUserManager.get(bookId);
			meet.setBookPerson(bookPerson);
		}

		if (recordId != 0) {
			SystemUserInfor recordUser = (SystemUserInfor) this.systemUserManager.get(recordId);
			meet.setRecordUser(recordUser);
		}

		try {
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest, "meet");

			BeanUtils.copyProperties(meet, meetInforVo);
			
//			if(meetInforVo.getEndMeetDate().equals("0000-00-00")){
//				meet.setEndMeetDate(null);
//			}

			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			meet.setAttachment(attachment);
			meet.setAuthor(user);
			if (meetInforVo.getMeetId() == null || meetInforVo.getMeetId() == 0) {
				// 创建时间
				meet.setCreateTime(new Date(System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
			return "/common/error";
		}
		// 自定义,没有的加上,去掉的删除
		// Set newReceives = new HashSet();
		int[] personIds = meetInforVo.getAttendIds();
		Set attendInfors = meet.getAttendInfors();

		// 此次选择中，去掉的并且尚未回复的删除
		AttendInfor[] arrayAttend = (AttendInfor[]) attendInfors.toArray(new AttendInfor[attendInfors.size()]);
		for (int k = arrayAttend.length - 1; k >= 0; k--) {
			AttendInfor attend = (AttendInfor) arrayAttend[k];
			SystemUserInfor tempUser = attend.getPerson();
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
		}

		// 没有的加上
		if (personIds != null && personIds.length != 0) {
			for (int kk = 0; kk < personIds.length; kk++) {
				boolean hasThisUser = false;
				AttendInfor attend = new AttendInfor();

				for (Iterator it = attendInfors.iterator(); it.hasNext();) {
					attend = (AttendInfor) it.next();

					SystemUserInfor tempUser = attend.getPerson();
					int rPersonId = tempUser.getPersonId().intValue();
					if (rPersonId == personIds[kk]) {
						hasThisUser = true;
						break;
					}
				}

				if (!hasThisUser) {
					AttendInfor tpAttend = new AttendInfor();
					// tpExcuter.setIsReaded(0);
					tpAttend.setMeet(meet);
					SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[kk]);
					tpAttend.setPerson(tpUser);
					attendInfors.add(tpAttend);
				}
			}
		}
		
		/***************发送短信***************/
		SMSMessagesToSend sms = new SMSMessagesToSend();
	
		String mobiles = request.getParameter("mobiles").trim();
		String status = request.getParameter("status");
		if(mobiles!=null && mobiles.length() !=0){
			sms.setMobileNos(mobiles);
			
			String sDate = meetInforVo.getMeetDate().toString();
			Integer sh =  meetInforVo.getStartHour();
		
			String sHour= meetInforVo.getStartHour().toString();
			String sHour1 ="";
			if(sHour.equals("0") ){
				sHour="00";
				sHour1="00";
			}else{
				Integer ssh= sh -1;
				sHour= ssh.toString();
				sHour1=meetInforVo.getStartHour().toString();
			}
			String sMin=meetInforVo.getStartMinutes().toString();
			if(sMin.equals("0")){
				sMin="00";
				
			}
			String sTime = sDate + " " + sHour +":" + sMin + ":00";
			String sTime1 = sDate + " " + sHour1 +":" + sMin + ":00";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//String time = df.format(sTime);
			Timestamp ts = Timestamp.valueOf(sTime);
			Timestamp ts1 = Timestamp.valueOf(sTime1);
			
			sms.setMessageText("请您于"+sTime1+"在"+meetInforVo.getMeetRoom()+"出席会务："+meetInforVo.getMeetName());
			sms.setScheduleDate(ts);
			sms.setTransmitStatus(0);
			sms.setApplier(user.getPerson().getPersonName());
			sms.setStatus(Integer.valueOf(status).intValue());
			this.smsManager.save(sms);
		}
		
		MeetInfor newMeet = (MeetInfor)this.meetInforManager.save(meet);
		
		/***************发送outlook会议邀请***************/
		//以下为参数
		String userId = user.getPerson().getEmail();
		String password = user.getPerson().getEmailPassword();
		String fromEmail = user.getPerson().getEmail();
		String toEmails = "";
		String subject = meetInforVo.getMeetName();
		String startTime = "";
		String endTime = "";
		String location = meetInforVo.getMeetRoom();
		String category = "";
		String summary = meetInforVo.getMeetName();
		
		//发送人(只要有一个为空时，则发送不出去，此时默认testoa1)
		//if(userId == null || user.equals("") || password == null || password.equals("")){
			fromEmail = "testoa1@haitongauto.com";
			userId = "haitongauto.com\\testoa1";
			password = "HTpassword1234";
		//}
		
		//接收人
		Set<AttendInfor> newAttendInfors = newMeet.getAttendInfors();
		for(AttendInfor ai : newAttendInfors){
			String tmpEmail = ai.getPerson().getPerson().getEmail();
			if(tmpEmail != null && !tmpEmail.equals("")){
				toEmails += tmpEmail + ";";
			}
		}
		
		//会议开始时间
		String beginDate = meetInforVo.getMeetDate().toString().trim().replace("-", "");
		String beginHour = meetInforVo.getStartHour() < 10 ? "0" + meetInforVo.getStartHour() : String.valueOf(meetInforVo.getStartHour());
		String beginMinute = meetInforVo.getStartMinutes() < 10 ? "0" + meetInforVo.getStartMinutes() : String.valueOf(meetInforVo.getStartMinutes());
		startTime = beginDate + "T" + beginHour + beginMinute + "00";
		
		//会议结束时间
		String endDate = "";
		String endHour = "";
		String endMinute = "";
			
		if(endMeetDate != null){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			endDate = sf.format(endMeetDate).trim().replace("-", "");
			endHour = meetInforVo.getEndHour() < 10 ? "0" + meetInforVo.getEndHour() : String.valueOf(meetInforVo.getEndHour());
			endMinute = meetInforVo.getEndMinutes() < 10 ? "0" + meetInforVo.getEndMinutes() : String.valueOf(meetInforVo.getEndMinutes());
			endTime = endDate + "T" + endHour + endMinute + "00";
		}
		
		//内容+meetInforVo.getContent()
		String content  = "本邮件是OA系统自动发出的会议邀请\\r\\n\\r\\n"
//			+"详情请即刻双击附件，将自动添加到您的日程表日历中，并将获得定时提醒！\\r\\n\\r\\n"
			+"会议安排如下：\\r\\n\\r\\n" +
			"会议时间："+beginDate+" "+beginHour+":"+beginMinute+"\\r\\n"+//+" 至 "+endDate+" "+endHour+":"+endMinute+"\\r\\n" + 
			"会议地点："+location+"\\r\\n" +
			"会议主题："+subject+"\\r\\n" +
			"与会人员："+meetInforVo.getAttendInfor() +"\\r\\n"+
			"会议要求："+meetInforVo.getDemand()+"\\r\\n"+
			"会议内容："+meetInforVo.getContent()+"\\r\\n\\r\\n"+
			"双击附件即可将会议安排添加到您的outlook日历中";
		
		
		//发送
		EmailUtil emailUtil = new EmailUtil(userId,password);
		emailUtil.sendMeetingRemind(fromEmail, toEmails, subject, startTime, endTime, location, category, content, summary);
		
		/************会议邀请结束***********/

		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		// out.print("window.returnValue = 'Y';");
		out.print("window.opener.location.reload();");
		out.print("window.close();");
		out.print("</script>");

		return null;
	}

	/**
	 * 保存会议纪要
	 * 
	 * @param meetInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveView")
	public String saveView(@ModelAttribute("meetInforVo")
	MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		int meetId = meetInforVo.getMeetId();
		MeetInfor meet = (MeetInfor) this.meetInforManager.get(meetId);
		String summary = meetInforVo.getSummary();
		String oldFiles = "";

		if (summary != null && (!summary.equals(""))) {

			String filePaths = meet.getSummaryAttach();
			oldFiles = deleteOldFile(request, filePaths, "summaryAttachArray");

			// 将原附件信息带入
			if (filePaths != null && !filePaths.equals("")) {

				String filePathss = request.getParameter("summaryAttach");
				oldFiles = deleteOldFile(request, filePathss, "summaryAttachArray");

			}
			try {
				// 上传附件
				String attachment = this.uploadAttachment(multipartRequest, "meet");

				// BeanUtils.copyProperties(meet, meetInforVo);

				if (attachment == null || attachment.equals("")) {
					attachment = oldFiles;
				} else {
					if (oldFiles == null || oldFiles.equals("")) {
						// attachment = attachment;
					} else {
						attachment = attachment + "|" + oldFiles;
					}
				}
				meet.setSummaryAttach(attachment);

			} catch (Exception ex) {
				model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
				return "/common/error";
			}

			meet.setSummary(meetInforVo.getSummary());
			this.meetInforManager.save(meet);
		}

		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("window.opener.location.reload();");
		out.print("window.close();");
		out.print("</script>");

		return null;
	}

	/**
	 * 查看会议
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewMeeting")
	public String viewMeeting(@ModelAttribute("meetInforVo")
	MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int meetId = Integer.parseInt(request.getParameter("rowId"));
		MeetInfor meetInfor = (MeetInfor) this.meetInforManager.get(meetId);
		request.setAttribute("_Meet", meetInfor);
		if (meetInfor.getSummaryAttach() != null) {
			meetInforVo.setSummaryAttachStr(meetInfor.getSummaryAttach());
		}
		// 附件信息
		String documentAttachment = meetInfor.getAttachment();
		if (documentAttachment != null && !documentAttachment.equals("")) {
			String[][] attachment = processFile(documentAttachment);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}

		// 会议纪要附件信息
		String summaryAttachment = meetInfor.getSummaryAttach();
		if (summaryAttachment != null && !summaryAttachment.equals("")) {
			String[][] attachment = processFile(summaryAttachment);
			request.setAttribute("_Attachment_SummaryNames", attachment[1]);
			request.setAttribute("_AttachmentsSummary", attachment[0]);
		}
		return "viewMeeting";
	}

	/**
	 * 批量删除会议
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);
					// 获取被删除讯息
					MeetInfor meet = (MeetInfor) this.meetInforManager.get(deleteId);
					// 删除附件
					String filePaths = meet.getAttachment();
					deleteFiles(filePaths);
					this.meetInforManager.remove(meet);
				}
			}
		}
		return "listMeeting";
	}

	/**
	 * 显示需要填写会议纪要的会议信息(用于首页显示)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getSummaryMeet")
	public void getSummaryMeet(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		Integer personId = user.getPersonId();

		// 仅获取用户负责的已经开过的会议信息
		Date sysDate = new Date(System.currentTimeMillis());
		String queryHQL = "from MeetInfor meet where meet.recordUser.personId=" + personId + " and meet.meetDate <= '" + sysDate + "' and meet.summary is null order by meetDate desc";
		List meets = this.meetInforManager.getResultByQueryString(queryHQL);

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();

		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author.person");
		jsonArray = convert.modelCollect2JSONArray(meets, awareObject);
		jsonObj.put("_Meets", jsonArray);

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);

	}

	/**
	 * 获取从当天开始后7天(不含当天)的会议(用于首页显示)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getMeets")
	public void getMeets(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		java.util.Date sDate = cal.getTime();
		String sDateStr = sdf.format(sDate);
		cal.add(Calendar.DATE, 7);
		java.util.Date eDate = cal.getTime();
		String eDateStr = sdf.format(eDate);
		String queryHQL = " from MeetInfor meet where meet.meetDate between '" + sDateStr + "' and '" + eDateStr + "'" 						
				+ " or meet.endMeetDate between '" + sDateStr + "' and '" + eDateStr + "'" 														
				+ " or ('" + sDateStr + "' > meet.meetDate  and '" + eDateStr + "'< meet.endMeetDate)" + " order by meet.meetDate,startHour";
		List meets = this.meetInforManager.getResultByQueryString(queryHQL);

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();

		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author.person");
		jsonArray = convert.modelCollect2JSONArray(meets, awareObject);
		jsonObj.put("_Meets", jsonArray);

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);

	}

	/**
	 * 获取首页会议数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	@ResponseBody
	@RequestMapping(params = "method=getMeetCounts")
	public int getMeetCounts(HttpServletRequest request, HttpServletResponse response)throws Exception{
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		if(systemUser==null){
			return 0;
		}
		PersonInfor personInfor = (PersonInfor) personInforManager.get(systemUser.getPersonId());
		if(personInfor==null){
			return 0;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		java.util.Date sDate = cal.getTime();
		String sDateStr = sdf.format(sDate);
		cal.add(Calendar.DATE, 7);
		java.util.Date eDate = cal.getTime();
		String eDateStr = sdf.format(eDate);

		String queryHQL = " from MeetInfor meet where meet.attendInfor like '%"+personInfor.getPersonName()+"%' and (meet.meetDate between '" + sDateStr + "' and '" + eDateStr + "'"
				+ " or meet.endMeetDate between '" + sDateStr + "' and '" + eDateStr + "'"
				+ " or ('" + sDateStr + "' > meet.meetDate  and '" + eDateStr + "'< meet.endMeetDate))" + " order by meet.meetDate,startHour";
		List meets = this.meetInforManager.getResultByQueryString(queryHQL);

//		JSONObject jsonObj = new JSONObject();
//		JSONConvert convert = new JSONConvert();
//		JSONArray jsonArray = new JSONArray();
//
//		// 通知Convert，哪些关联对象需要获取
//		List awareObject = new ArrayList();
////		awareObject.add("author.person");
//		jsonArray = convert.modelCollect2JSONArray(meets, awareObject);
		int size = meets.size();
//		response.getWriter().print(size);
		return size;
	}
	/**
	 * 一周会议
	 * @param meetInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */


	@RequestMapping(params = "method=viewIndex")
	public String viewIndex(MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar mydate = Calendar.getInstance();

				Calendar calStart = Calendar.getInstance();
				Calendar calEnd = Calendar.getInstance();
				int n=0;
				if(request.getParameter("n")==null){
					
				}else{
					 n=Integer.valueOf(request.getParameter("n"));
				}
				
				
			
				mydate.add(Calendar.DATE, n*7);
				calStart.add(Calendar.DATE, n*7);
				calEnd.add(Calendar.DATE, n*7);
				int dayOfWeek = mydate.get(Calendar.DAY_OF_WEEK);

				calStart.add(Calendar.DATE, 1 - dayOfWeek + 1);
				calEnd.add(Calendar.DATE, 7 - dayOfWeek + 1);

				java.util.Date startDate = calStart.getTime();
				java.util.Date endDate = calEnd.getTime();
				java.util.Date nowdate = mydate.getTime();
				String startStr = sdf.format(startDate);
				String endStr = sdf.format(endDate);

				String sql = " from MeetInfor meet where meet.meetDate >= '" + startStr + "' and meet.meetDate <= '" + endStr + "'";
				sql += " order by meetDate,startHour";
				List meets = this.meetInforManager.getResultByQueryString(sql);

				ArrayList dateList = new ArrayList();
				ArrayList weekList = new ArrayList();
				SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日EEE");
				String start = df.format(startDate).toString().substring(5, 11);
				String end = df.format(endDate).toString().substring(5, 11);
				String now = df.format(nowdate).toString().substring(0, 11);

				for (Iterator it = meets.iterator(); it.hasNext();) {
					MeetInfor preMeet = (MeetInfor) it.next();
					Date mdate = preMeet.getMeetDate();
					SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
					String meetDate = datesdf.format(mdate).toString().substring(0, 10);
					dateList.add(meetDate);
					String week = df.format(mdate).toString().substring(11,14);
					weekList.add(week);
				}
				request.setAttribute("_Start_Date", start);
				request.setAttribute("_End_Date", end);
				request.setAttribute("_Now_Date", now);
				request.setAttribute("_DateList", dateList);
				request.setAttribute("_WeekList", weekList);

				request.setAttribute("_Meets", meets);
				request.setAttribute("_N", n);
				String sp = request.getParameter("sp");
				if(sp!=null&&sp.equals("sp")){
					request.setAttribute("sp","sp");
					return "weekMeet_sp";
				}
				return "weekMeet";
			}

	
	@RequestMapping(params = "method=getMeets_m")
	public void getMeets_m(MeetInforVo meetInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		if(StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			/************记录app模块使用日志************/ 
			AppModuleLog appModuleLog = new AppModuleLog();
			appModuleLog.setModuleName("公司会议");
			appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
			appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
			appModuleLog.setUserName(systemUser.getUserName());
			this.appModuleLogManager.save(appModuleLog);
			/*****************************************/
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar mydate = Calendar.getInstance();

		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		int n=0;
		if(request.getParameter("n")==null){
			
		}else{
			 n=Integer.valueOf(request.getParameter("n"));
		}
		mydate.add(Calendar.DATE, n*7);
		calStart.add(Calendar.DATE, n*7);
		calEnd.add(Calendar.DATE, n*7);
		int dayOfWeek = mydate.get(Calendar.DAY_OF_WEEK);

		calStart.add(Calendar.DATE, 1 - dayOfWeek + 1);
		calEnd.add(Calendar.DATE, 7 - dayOfWeek + 1);

		java.util.Date startDate = calStart.getTime();
		java.util.Date endDate = calEnd.getTime();
		java.util.Date nowdate = mydate.getTime();
		String startStr = sdf.format(startDate);
		String endStr = sdf.format(endDate);
		
		String queryHQL = " from MeetInfor meet where meet.meetDate >= '" + startStr + "' and meet.meetDate <= '" + endStr + "'";
		queryHQL += " order by meetDate,startHour";
		
		List meets = this.meetInforManager.getResultByQueryString(queryHQL);

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();

		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author.person");
		jsonArray = convert.modelCollect2JSONArray(meets, awareObject);
		jsonObj.put("_Meets", jsonArray);

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
}