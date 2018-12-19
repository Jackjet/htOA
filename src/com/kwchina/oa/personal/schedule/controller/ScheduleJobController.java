package com.kwchina.oa.personal.schedule.controller;


import java.io.PrintWriter;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

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
import com.kwchina.oa.personal.schedule.entity.ScheduleExcuter;
import com.kwchina.oa.personal.schedule.entity.ScheduleJobInfor;
import com.kwchina.oa.personal.schedule.service.ScheduleExcuterManager;
import com.kwchina.oa.personal.schedule.service.ScheduleJobLogManager;
import com.kwchina.oa.personal.schedule.service.ScheduleJobManager;
import com.kwchina.oa.personal.schedule.vo.ScheduleJobInforVo;
import com.kwchina.oa.util.SysCommonMethod;



@Controller
@RequestMapping(value="/personal/personalJobInfor.do")
public class ScheduleJobController extends BasicController {
	
	private static Log log = LogFactory.getLog(ScheduleJobController.class);
	@Resource
	private InforDocumentManager inforDocumentManager;
	
	@Resource
	private ScheduleJobManager scheduleJobManager;
	
	@Resource
	private SystemUserManager systemUserManager ;
	
	@Resource
	private ScheduleExcuterManager scheduleExcuterManager;
	
	@Resource
	private OrganizeManager organizeManager ;
	
	@Resource
	private ScheduleJobLogManager scheduleJobLogManager = null;
	
	
	/**
	 * 显示我的工作日程
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=list")
	public void list(@ModelAttribute("scheduleJobInforVo")ScheduleJobInforVo scheduleJobInforVo, 
			Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		String Type = request.getParameter("Type");//Type=0:我的日程;Type=1:安排他人的日程;Type=2:公开的日程
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		//构造查询语句(使用jqGrid弹出的多条件查询)
		
		String[] queryString = new String[2];
		String condition = "";
		
		if(Type.equals("0")){
			//自己安排给自己的和别人安排给自己的
			queryString[0] = " from  ScheduleJobInfor scheduleJobInfor where 1=1" ;
			queryString[1] = " select count(scheduleId) from ScheduleJobInfor where  1=1";
			condition =  " and (( assigner.personId="+ user.getPersonId() +" and scheduleType = "+ScheduleJobInfor.Self_Job+") or (scheduleId in (select scheduleExcuter.jobInfor.scheduleId from ScheduleExcuter scheduleExcuter where scheduleExcuter.executor.personId="+user.getPersonId()+" )) )";
			
		}else if (Type.equals("1")){
			//自己安排给别人的
			queryString[0] = " from ScheduleJobInfor scheduleJobInfor where 1=1  ";
			queryString[1] = " select count(scheduleId) from ScheduleJobInfor where  1=1 ";
			condition =  "and assigner.personId="+user.getPersonId()+" and scheduleType = " + ScheduleJobInfor.Excuter_Job;
			
		}else if (Type.equals("2")){
			//公开的
			queryString[0] = " from ScheduleJobInfor scheduleJobInfor where  1=1 ";
			queryString[1] = " select count(scheduleId) from ScheduleJobInfor where 1=1";
			condition =  "and openType = " + ScheduleJobInfor.Type_Public;
			
		}
		
		queryString[0]+=condition;
		queryString[1]+=condition;
		queryString = this.inforDocumentManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page)); 
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		
		PageList pl = this.systemUserManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//	把查询到的结果转化为VO
		List scheduleVos = new ArrayList();			
		if(list.size()>0){			
						
			for(Iterator it = list.iterator();it.hasNext();){
				ScheduleJobInfor schedule = (ScheduleJobInfor)it.next();
				//把查询到的结果转化为VO
				ScheduleJobInforVo vo = this.scheduleJobManager.transPOToVO(schedule);
				scheduleVos.add(vo);
			}
		
		}
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();	
		rows = convert.modelCollect2JSONArray(scheduleVos, awareObject);        
		jsonObj.put("rows", rows);	//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	/**
	 * 编辑讯息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("scheduleJobInforVo")ScheduleJobInforVo scheduleJobInforVo,
			Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);

		ScheduleJobInfor schedule = new ScheduleJobInfor();
		int scheduleId = scheduleJobInforVo.getScheduleId();
		String handle = request.getParameter("handle");
		request.setAttribute("_Handle", handle);
		
		String scheduleType = request.getParameter("scheduleType");
		request.setAttribute("_ScheduleType", scheduleType);
		// 获取全部用户
		List users = this.systemUserManager.getAll();
		model.addAttribute("_Users", users);

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);
		if (scheduleId != 0) {
			// 修改的情况
			schedule = scheduleJobManager.getEditSchedule(scheduleJobInforVo.getScheduleId());
			request.setAttribute("_Schedule", schedule);

			try {
				BeanUtils.copyProperties(scheduleJobInforVo, schedule);

				scheduleJobInforVo.setPersonId(schedule.getAssigner().getPersonId());
				//scheduleJobInforVo.setWriteTimeStr(schedule.getWriteTime().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 已经选择的执行人员
			scheduleJobInforVo.setStartTimeStr(schedule.getStartDate().toString());
			scheduleJobInforVo.setEndTimeStr(schedule.getEndDate().toString());
			int[] personIds = new int[users.size()];
			Set scheduleExcuters = schedule.getScheduleExcuters();
			for (int i = 0; i < users.size(); i++) {
				SystemUserInfor tempUser = (SystemUserInfor) users.get(i);
				int tempPersonId = tempUser.getPersonId().intValue();

				for (Iterator it = scheduleExcuters.iterator(); it.hasNext();) {
					ScheduleExcuter excuter = (ScheduleExcuter) it.next();
					int rPersonId = excuter.getExecutor().getPersonId()
							.intValue();

					if (tempPersonId == rPersonId) {
						// 该用户被选择
						personIds[i] = tempPersonId;
						break;
					}
				}
			}
			if (!("transmit").equals(handle)) {
				scheduleJobInforVo.setPersonIds(personIds);
			}
		}

		// 全部部门信息
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);

		String attachmentFile = schedule.getAttachment();
		if (attachmentFile != null && (!attachmentFile.equals(""))) {
			String[] arrayFiles = attachmentFile.split("\\|");
			scheduleJobInforVo.setAttatchmentArray(arrayFiles);

			String[] attachmentNames = new String[arrayFiles.length];
			for (int k = 0; k < arrayFiles.length; k++) {
				attachmentNames[k] = File.getFileName(arrayFiles[k]);
			}
			request.setAttribute("_Attachment_Names", attachmentNames);
		}
		return "schedule/editCalendar";
	}
	

	/****
	 * 保存日程
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params ="method=save")
	
	public String save(@ModelAttribute("scheduleJobInforVo")ScheduleJobInforVo scheduleJobInforVo,
			Model model,HttpServletRequest request, HttpServletResponse response,DefaultMultipartHttpServletRequest multipartRequest) 
			throws Exception{
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		
		ScheduleJobInfor schedule = new ScheduleJobInfor();
		int scheduleId = scheduleJobInforVo.getScheduleId();
		String handle = request.getParameter("handle");
		if(("transmit").equals(handle)){
			scheduleJobInforVo.getScheduleId();
			scheduleId=0;
		}
		String oldFiles = "";

		if (scheduleId != 0) {
			
			//ScheduleJobInfor scheduleJobInfor = (ScheduleJobInfor)this.scheduleJobManager.get(scheduleId);
			schedule = (ScheduleJobInfor) this.scheduleJobManager.get(scheduleId);
			// 修改信息时,对附件进行修改
			String filePaths = schedule.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

		} else {
			SystemUserInfor preUser = (SystemUserInfor) SysCommonMethod
					.getSystemUser(request);
			schedule.setAssigner(preUser);

			long time = System.currentTimeMillis();
			Timestamp timeNow = new Timestamp(time);
			schedule.setWriteTime(timeNow);
			
		}

		try {
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest,"schedule");

			BeanUtils.copyProperties(schedule, scheduleJobInforVo);
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			schedule.setAttachment(attachment);
			schedule.setAssigner(user);

			if (scheduleJobInforVo.getScheduleId() == null|| scheduleJobInforVo.getScheduleId() == 0) {
				// 设置发送时间
				schedule.setWriteTime(new Timestamp(System.currentTimeMillis()));
			}
		} catch (Exception ex) {
			model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>"
					+ ex.toString());
			return "/common/error";
		}

		// 日程执行者
		int scheduleType = scheduleJobInforVo.getScheduleType();
		if (scheduleType == 0) {
			// 全公司,不需要处理
		} else {
			// 自定义,没有的加上,去掉的删除
			// Set newReceives = new HashSet();

			int[] personIds = scheduleJobInforVo.getPersonIds();
			Set scheduleExcuters = schedule.getScheduleExcuters();

			// 此次选择中，去掉的并且尚未回复的删除
			ScheduleExcuter[] arrayExcuter = (ScheduleExcuter[]) scheduleExcuters.toArray(new ScheduleExcuter[scheduleExcuters.size()]);
			for (int k = arrayExcuter.length - 1; k >= 0; k--) {
				ScheduleExcuter excuter = (ScheduleExcuter) arrayExcuter[k];
				SystemUserInfor tempUser = excuter.getExecutor();
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
					ScheduleExcuter excuter = new ScheduleExcuter();

					for (Iterator it = scheduleExcuters.iterator(); it.hasNext();) {
						excuter = (ScheduleExcuter) it.next();

						SystemUserInfor tempUser = excuter.getExecutor();
						int rPersonId = tempUser.getPersonId().intValue();
						if (rPersonId == personIds[kk]) {
							hasThisUser = true;
							break;
						}
					}

					if (!hasThisUser) {
						ScheduleExcuter tpExcuter = new ScheduleExcuter();
						//tpExcuter.setIsReaded(0);
						tpExcuter.setJobInfor(schedule);
						SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager
								.get(personIds[kk]);
						tpExcuter.setExecutor(tpUser);
						scheduleExcuters.add(tpExcuter);
					}
				}
			}
		}
		schedule.setWriteTime(Timestamp.valueOf(scheduleJobInforVo.getStartTimeStr()));	
		schedule.setStartDate(Timestamp.valueOf(scheduleJobInforVo.getStartTimeStr()));
		schedule.setEndDate(Timestamp.valueOf(scheduleJobInforVo.getEndTimeStr()));
		
		this.scheduleJobManager.save(schedule);
			
		if (scheduleType == 0){
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>");
			out.print("window.opener.location.reload();");
			out.print("window.close();");
			out.print("</script>");
		}else if(scheduleType == 1){
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>");
			out.print("window.opener.reloadTab2();");
			out.print("window.close();");
			out.print("</script>");
			
		}else{
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>");
			out.print("window.opener.reloadTab3();");
			out.print("window.close();");
			out.print("</script>");
		}
		return null;
	}
		
	/**
	 * 批量删除日程
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params ="method=delete")
	public String delete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("rowIds");
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {
					int detleteId = Integer.valueOf(detleteIds[i]);
					
					ScheduleJobInfor scheduleJobInfor = (ScheduleJobInfor)this.scheduleJobManager.get(detleteId);
					// 删除附件
					int personId= scheduleJobInfor.getAssigner().getPersonId();
					if (personId == user.getPersonId()){
					String filePaths = scheduleJobInfor.getAttachment();
					deleteFiles(filePaths);
					this.scheduleJobManager.remove(scheduleJobInfor);
					}
				}
			}
		}
		return "listCalendar";
	}


	/**
	 * 查看日程
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewCalendar")
	public String view(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int scheduleId = Integer.parseInt(request.getParameter("rowId"));		
		ScheduleJobInfor scheduleJobInfor = (ScheduleJobInfor)this.scheduleJobManager.get(scheduleId);	
		request.setAttribute("_Schedule", scheduleJobInfor);
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		// 查看回复的信息
		List list = this.scheduleJobLogManager.getScheduleJobLog(scheduleId);
		request.setAttribute("_LogList", list);

		// 附件信息
		String documentAttachment = scheduleJobInfor.getAttachment();
		if (documentAttachment != null && !documentAttachment.equals("")) {
			String[][] attachment = processFile(documentAttachment);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		// 判断工作类型
		int scheduleType = Integer.parseInt(request.getParameter("scheduleType"));	
		if (scheduleType!=-1){
		request.setAttribute("_ScheduleType", scheduleType);
		}
		// 状态
		int status = scheduleJobInfor.getStatus();
		request.setAttribute("_Status", status);
		
		// 判断是否为工作执行人员
		Set scheduleExcuters = scheduleJobInfor.getScheduleExcuters();
		Boolean isExcuter = false;
		if (scheduleExcuters != null && scheduleExcuters.size() != 0) {
			for (Iterator it=scheduleExcuters.iterator();it.hasNext();) {
				ScheduleExcuter tmpScheduleExcuter = (ScheduleExcuter)it.next();
				
				if (tmpScheduleExcuter.getExecutor().getPersonId().intValue() == systemUser.getPersonId().intValue()) {
					isExcuter = true;
				}
			}
		}
		request.setAttribute("_Is_Excuter", isExcuter);
		
		// 判断是否为安排人员
		Boolean isAssigner = false;
		if (scheduleJobInfor.getAssigner().getPersonId().intValue() == systemUser.getPersonId().intValue()) {
			isAssigner = true;
		}
		request.setAttribute("_Is_Assigner", isAssigner);
		
		
		return "schedule/viewCalendar";
	}
	

	// 处理日常工作安排
	public String process(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ScheduleJobAction.process' method...");
		}
				
		return "process";
	}
	
	// 暂停工作
	public String pause(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ScheduleJobAction.pause' method...");
		}
		
	
		return "close";
		
	}
	
	// 显示今日日程
	public String viewTodSchedule(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ScheduleJobAction.viewTodSchedule' method...");
		}
		
		return "viewTodSchedule";
		
	}
	
	// 显示明日日程
	public String viewTomSchedule(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ScheduleJobAction.viewTomSchedule' method...");
		}
		
		return "viewTomSchedule";
		
	}
	
	// 显示本周日程
	public String viewThiSchedule(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ScheduleJobAction.viewThiSchedule' method...");
		}
		
		
		return "viewThiSchedule";
		
	}
	
	// 显示下周后日程
	public String viewNexSchedule(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ScheduleJobAction.viewNexSchedule' method...");
		}
		
		
		return "viewNexSchedule";
		
	}
	
	/**
	 * 获取从当天开始后7天(不含当天)的日程信息(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getSchedules")
	public void getSchedules(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		Integer personId = user.getPersonId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		java.util.Date sDate = cal.getTime();
		String sDateStr = sdf.format(sDate);
		cal.add(Calendar.DATE, 7);
		java.util.Date eDate = cal.getTime();
		String eDateStr = sdf.format(eDate);
		
		String queryHQL = " from ScheduleJobInfor scheduleJobInfor where 1=1";
		
		/** 别人安排给我的工作 */
		queryHQL += " and ((scheduleJobInfor.scheduleId in (select scheduleExcuter.jobInfor.scheduleId from ScheduleExcuter scheduleExcuter where scheduleExcuter.executor.personId = " + personId + ")";		
		queryHQL += " and scheduleJobInfor.status != " + ScheduleJobInfor.Status_Pause + ")";
		
		/** 自我制定的日程 */
		queryHQL += " or (scheduleJobInfor.assigner.personId = " + personId + " and scheduleJobInfor.scheduleType = " + ScheduleJobInfor.Self_Job +")";
		
		/** 公开的日程 */
		queryHQL += " or scheduleJobInfor.openType = " + ScheduleJobInfor.Type_Public + ")";
		
		queryHQL += " and ((scheduleJobInfor.endDate >= '" + sDateStr + "' and scheduleJobInfor.endDate <= '" + eDateStr + "') or (scheduleJobInfor.startDate <= '" + sDateStr + "' and scheduleJobInfor.endDate >= '" + eDateStr + "') or (scheduleJobInfor.startDate between '" + sDateStr + "' and '"+eDateStr+"' and scheduleJobInfor.endDate >= '" + eDateStr + "'))";
		queryHQL += " order by scheduleJobInfor.writeTime desc";
		List schedules = this.scheduleJobManager.getResultByQueryString(queryHQL);
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();
		
		jsonArray = convert.modelCollect2JSONArray(schedules, new ArrayList());
		jsonObj.put("_Schedules", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}

	/**
	 * 获取从当天开始后7天(不含当天)的日程数目(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params = "method=getSchedulesCounts")
	public int getSchedulesCounts(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		Integer personId = user.getPersonId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		java.util.Date sDate = cal.getTime();
		String sDateStr = sdf.format(sDate);
		cal.add(Calendar.DATE, 7);
		java.util.Date eDate = cal.getTime();
		String eDateStr = sdf.format(eDate);

		String queryHQL = " from ScheduleJobInfor scheduleJobInfor where 1=1";

		/** 别人安排给我的工作 */
		queryHQL += " and ((scheduleJobInfor.scheduleId in (select scheduleExcuter.jobInfor.scheduleId from ScheduleExcuter scheduleExcuter where scheduleExcuter.executor.personId = " + personId + ")";
		queryHQL += " and scheduleJobInfor.status != " + ScheduleJobInfor.Status_Pause + ")";

		/** 自我制定的日程 */
		queryHQL += " or (scheduleJobInfor.assigner.personId = " + personId + " and scheduleJobInfor.scheduleType = " + ScheduleJobInfor.Self_Job +")";

		/** 公开的日程 */
		queryHQL += " or scheduleJobInfor.openType = " + ScheduleJobInfor.Type_Public + ")";

		queryHQL += " and ((scheduleJobInfor.endDate >= '" + sDateStr + "' and scheduleJobInfor.endDate <= '" + eDateStr + "') or (scheduleJobInfor.startDate <= '" + sDateStr + "' and scheduleJobInfor.endDate >= '" + eDateStr + "') or (scheduleJobInfor.startDate between '" + sDateStr + "' and '"+eDateStr+"' and scheduleJobInfor.endDate >= '" + eDateStr + "'))";
		queryHQL += " order by scheduleJobInfor.writeTime desc";
		List schedules = this.scheduleJobManager.getResultByQueryString(queryHQL);

		return schedules.size();

	}
	
}
