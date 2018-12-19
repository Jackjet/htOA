package com.kwchina.extend.club.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.File;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.club.entity.ActAttendInfor;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.entity.RegisterInfor;
import com.kwchina.extend.club.service.ClubInforManager;
import com.kwchina.extend.club.vo.ClubExcelVo;
import com.kwchina.extend.club.vo.ClubInforVo;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/club/clubInfor.do")
public class ClubInforController extends BasicController {

	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private ClubInforManager clubInforManager;
	
	@Resource
	private AppModuleLogManager appModuleLogManager;
	

	/**
	 * 获取活动信息列表
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//手机端只显示有效的活动 dTag = deviceTag 如果为true，则是
			String dTag = request.getParameter("dTag");

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = " from ClubInfor clubInfor where isDeleted=0 ";
			queryString[1] = " select count(clubInfor.actId) from ClubInfor clubInfor where isDeleted=0 ";
			String condition = "";
			
			//手机端时
			if(dTag != null && !dTag.equals("")){
				if(dTag.trim().equals("true")){
					condition += " and clubInfor.status > " + ClubInfor.Club_Status_ToReg + " and clubInfor.status < " + ClubInfor.Club_Status_Over;
					
					if(StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
						SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
						/************记录app模块使用日志************/ 
						AppModuleLog appModuleLog = new AppModuleLog();
						appModuleLog.setModuleName("主题活动");
						appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
						appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
						appModuleLog.setUserName(systemUser.getUserName());
						this.appModuleLogManager.save(appModuleLog);
						/*****************************************/
					}
				}
			}

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			queryString[0] += condition;
			queryString[1] += condition;
			
			queryString = this.clubInforManager.generateQueryString(queryString, getSearchParams(request));
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.clubInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List infors = pl.getObjectList();
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			//将数据转为vo
			List vos = new ArrayList();
			for(Iterator it = infors.iterator();it.hasNext();){
				ClubInfor tmpClubInfor = (ClubInfor)it.next();
				ClubInforVo vo = new ClubInforVo();
				
				BeanUtils.copyProperties(vo, tmpClubInfor);
				vo.setActTimeStr(sf1.format(tmpClubInfor.getActTime()));
				vo.setBeginSignDateStr(tmpClubInfor.getBeginSignDate() != null ? sf.format(tmpClubInfor.getBeginSignDate()) : "");
				vo.setCutDateStr(tmpClubInfor.getCutDate() != null ? sf.format(tmpClubInfor.getCutDate()) : "");
				vo.setActTimeStr(tmpClubInfor.getActTime() != null ? sf1.format(tmpClubInfor.getActTime()) : "");
				vo.setToTimeStr(tmpClubInfor.getToTime() != null ? sf1.format(tmpClubInfor.getToTime()) : "");
				vo.setManagerId(tmpClubInfor.getManager() != null ? tmpClubInfor.getManager().getPersonId() : 0);
				vo.setManagerName(tmpClubInfor.getManager() != null ? tmpClubInfor.getManager().getPerson().getPersonName() : "");
				vo.setCreaterId(tmpClubInfor.getCreater().getPersonId());
				vo.setCreaterName(tmpClubInfor.getCreater().getPerson().getPersonName());
				vo.setCreateTimeStr(sf.format(tmpClubInfor.getCreateTime()));
				vo.setMainPicStr(tmpClubInfor.getMainPic());
				vo.setTwoPic(tmpClubInfor.getTwoPic());
				vo.setEndTimeStr(String.valueOf(tmpClubInfor.getEndTime()));
				
				vo.setRegisterCount(tmpClubInfor.getRegisters().size());
				vo.setSignerCount(tmpClubInfor.getAttends().size());
				
				vos.add(vo);
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
			awareObject.add("creater.person");
			awareObject.add("manager.person");
			rows = convert.modelCollect2JSONArray(vos, awareObject);
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)


			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 导出excel
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=expertExcel")
	public String expertExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//手机端只显示有效的活动 dTag = deviceTag 如果为true，则是
			String dTag = request.getParameter("dTag");

			// 构造查询语句
			String[] queryString = new String[2];
			queryString[0] = " from ClubInfor clubInfor where isDeleted=0 ";
			queryString[1] = " select count(clubInfor.actId) from ClubInfor clubInfor where isDeleted=0 ";
			String condition = "";
			
			//手机端时
			if(dTag != null && !dTag.equals("")){
				if(dTag.trim().equals("true")){
					condition += " and clubInfor.status > " + ClubInfor.Club_Status_ToReg + " and clubInfor.status < " + ClubInfor.Club_Status_Over;
				}
			}

			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数

			queryString[0] += condition;
			queryString[1] += condition;
			
			queryString = this.clubInforManager.generateQueryString(queryString, getSearchParams(request));
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.clubInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List infors = pl.getObjectList();
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			/******************导出Excel********************/
//			String filePath = SystemConstant.Submit_Path + time + "/";
			String filePath = "/"+CoreConstant.Attachment_Path + "club/";
			String fileTitle = "主题活动开展情况";

			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);

			List<String> rowName = new ArrayList<String>();
			
			int k = 0;// 列数

			rowName.add("序号");
			rowName.add("社团");
			rowName.add("部门");
			rowName.add("人员");
			rowName.add("活动名称");
			rowName.add("活动开始时间");
			rowName.add("报名时间");
			rowName.add("签到时间");
			k = 9;

			List<ClubExcelVo> vos = new ArrayList<ClubExcelVo>();
			for (int i = 0; i < infors.size(); i++) {
				ClubInfor club = (ClubInfor)infors.get(i);
				
				Set<RegisterInfor> registers = club.getRegisters();
				Set<ActAttendInfor> attends = club.getAttends();
				
				//报名信息 
				for(RegisterInfor reg : registers){
					ClubExcelVo vo = new ClubExcelVo();
					
					vo.setLeague(club.getLeague());
					vo.setDepartment(reg.getReger().getPerson().getDepartment().getOrganizeName());
					vo.setPersonName(reg.getReger().getPerson().getPersonName());
					vo.setActName(club.getActTitle());
					vo.setActDate((club.getActTime() != null) ? sf.format(club.getActTime()) : "");
					vo.setRegTime(reg.getRegTime() != null ? sf1.format(reg.getRegTime()) : "");
					vo.setSignTime("");
					
					vos.add(vo);
				}
				
				//签到信息
				for(ActAttendInfor attend : attends){
					ClubExcelVo vo = new ClubExcelVo();
					
					vo.setLeague(club.getLeague());
					vo.setDepartment(attend.getAttender().getPerson().getDepartment().getOrganizeName());
					vo.setPersonName(attend.getAttender().getPerson().getPersonName());
					vo.setActName(club.getActTitle());
					vo.setActDate((club.getActTime() != null) ? sf.format(club.getActTime()) : "");
					vo.setRegTime("");
					vo.setSignTime(attend.getAttTime() != null ? sf1.format(attend.getAttTime()) : "");
					
					vos.add(vo);
				}
				
			}
			
			String[][] data = new String[9][vos.size()];
			
			for(int i = 0;i < vos.size(); i++){
				ClubExcelVo vo = vos.get(i);
				
				data[0][i] = String.valueOf(i + 1);
				data[1][i] = vo.getLeague();
				data[2][i] = vo.getDepartment();
				data[3][i] = vo.getPersonName();
				data[4][i] = vo.getActName();
				data[5][i] = vo.getActDate();
				data[6][i] = vo.getRegTime();
				data[7][i] = vo.getSignTime();
			}

			for (int i = 0; i < k; i++) {
				object.addContentListByList(data[i]);
			}
			object.setRowName(rowName);
			ExcelOperate operate = new ExcelOperate();
			try {
				operate.exportExcel(object, vos.size(), true, request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			filePath = filePath + fileTitle + ".xls";
//			request.getSession().setAttribute("_File_Path", "");
			request.getSession().removeAttribute("_File_Path");
			request.getSession().setAttribute("_File_Path", filePath);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download";
	}
	
	/**
	 * 编辑活动信息
	 * 
	 * @param clubInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("clubInforVo")
	ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ClubInfor clubInfor = new ClubInfor();
		Integer actId = clubInforVo.getActId();
		
		boolean isNew = true;

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);
		if (actId != null && actId.intValue() != 0) {
			isNew = false;
			
			// 修改的情况
			clubInfor = (ClubInfor) clubInforManager.get(actId);
			request.setAttribute("_ClubInfor", clubInfor);

			try {
				//BeanUtils.copyProperties(clubInforVo, clubInfor);
				clubInforVo.setActTitle(clubInfor.getActTitle());
				clubInforVo.setActItem(clubInfor.getActItem() != null && !clubInfor.getActItem().equals("") ? clubInfor.getActItem().replaceAll("<br/>", "\r\n") : clubInfor.getActItem());
				clubInforVo.setActPlace(clubInfor.getActPlace() != null && !clubInfor.getActPlace().equals("") ? clubInfor.getActPlace().replaceAll("<br/>", "\r\n") : clubInfor.getActPlace());
				clubInforVo.setRegisterWay(clubInfor.getRegisterWay() != null && !clubInfor.getRegisterWay().equals("") ? clubInfor.getRegisterWay().replaceAll("<br/>", "\r\n") : clubInfor.getRegisterWay());
				clubInforVo.setActRule(clubInfor.getActRule() != null && !clubInfor.getActRule().equals("") ? clubInfor.getActRule().replaceAll("<br/>", "\r\n") : clubInfor.getActRule());
				clubInforVo.setMemo(clubInfor.getMemo() != null && !clubInfor.getMemo().equals("") ? clubInfor.getMemo().replaceAll("<br/>", "\r\n") : clubInfor.getMemo());
				
				if (clubInfor.getManager() != null) {
					clubInforVo.setManagerId(clubInfor.getManager().getPersonId());
				}

				if(clubInfor.getCutDate() != null){
					clubInforVo.setCutDateStr(clubInfor.getCutDate().toString());
					request.setAttribute("_CutDateStr", clubInfor.getCutDate().toString());
				}
				if(clubInfor.getBeginSignDate() != null){
					clubInforVo.setBeginSignDateStr(clubInfor.getBeginSignDate().toString());
					request.setAttribute("_BeginSignDateStr", clubInfor.getBeginSignDate().toString());
				}
				
				if(clubInfor.getActTime() != null){
					clubInforVo.setActTimeStr(clubInfor.getActTime().toString());
					request.setAttribute("_ActTimeStr", clubInfor.getActTime().toString());
				}
				
				if(clubInfor.getToTime() != null){
					clubInforVo.setToTimeStr(clubInfor.getToTime().toString());
					request.setAttribute("_ToTimeStr", clubInfor.getToTime().toString());
				}
				
				
				clubInforVo.setMainPicStr(clubInfor.getMainPic());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		String attachmentFile = clubInfor.getMainPic();
		if (attachmentFile != null && (!attachmentFile.equals(""))) {
			String[] arrayFiles = attachmentFile.split("\\|");
			clubInforVo.setAttatchmentArray(arrayFiles);

			String[] attachmentNames = new String[arrayFiles.length];
			for (int k = 0; k < arrayFiles.length; k++) {
				attachmentNames[k] = File.getFileName(arrayFiles[k]);
			}
			request.setAttribute("_Attachment_Names", attachmentNames);
		}
//
//		// 全部部门信息
//		List departments = this.organizeManager.getDepartments();
//		model.addAttribute("_Departments", departments);
//		
		//获取用户信息
		List users = this.systemUserManager.getAllValid();
		request.setAttribute("_Users", users);
		
		request.setAttribute("_IsNew", isNew);

		return "editClubInfor";
	}
	
	/**
	 * 保存活动
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("clubInforVo")
			ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		try {
			SystemUserInfor user = SysCommonMethod.getSystemUser(request);

			ClubInfor clubInfor = new ClubInfor();
			Integer actId = clubInforVo.getActId();
			Integer managerId = clubInforVo.getManagerId();
			
			String oldFiles = "";
			
			//是否新增
			boolean isNew = false;
			if (actId != null && actId.intValue() > 0) {
				clubInfor = (ClubInfor) this.clubInforManager.get(actId);
				
				// 修改信息时,对附件进行修改
				String filePaths = clubInfor.getMainPic();
				oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			} else { 
				isNew = true;
				clubInfor.setCreater(user);
				clubInfor.setCreateTime(new Timestamp((System.currentTimeMillis())));
				
				
			}

			//BeanUtils.copyProperties(clubInfor, clubInforVo);
			clubInfor.setActTitle(clubInforVo.getActTitle());
			clubInfor.setActItem(clubInforVo.getActItem() != null && !clubInforVo.getActItem().equals("") ? clubInforVo.getActItem().replaceAll("\\r\\n", "<br/>") : clubInforVo.getActItem());
			clubInfor.setActPlace(clubInforVo.getActPlace() != null && !clubInforVo.getActPlace().equals("") ? clubInforVo.getActPlace().replaceAll("\\r\\n", "<br/>") : clubInforVo.getActPlace());
			clubInfor.setRegisterWay(clubInforVo.getRegisterWay() != null && !clubInforVo.getRegisterWay().equals("") ? clubInforVo.getRegisterWay().replaceAll("\\r\\n", "<br/>") : clubInforVo.getRegisterWay());
			clubInfor.setActRule(clubInforVo.getActRule() != null && !clubInforVo.getActRule().equals("") ? clubInforVo.getActRule().replaceAll("\\r\\n", "<br/>") : clubInforVo.getActRule());
			clubInfor.setMemo(clubInforVo.getMemo() != null && !clubInforVo.getMemo().equals("") ? clubInforVo.getMemo().replaceAll("\\r\\n", "<br/>") : clubInforVo.getMemo());
			
			clubInfor.setLeague(clubInforVo.getLeague());
			
			
			
			if(isNew){
				//将状态设置为1
				clubInfor.setStatus(ClubInfor.Club_Status_ToReg);
			}
			
			//活动时间
			if(clubInforVo.getActTimeStr() != null && !clubInforVo.getActTimeStr().equals("")){
//				if(isNew){
//					clubInfor.setActTime(Timestamp.valueOf(clubInforVo.getActTimeStr().toString()+":00"));
//				}else {
					clubInfor.setActTime(Timestamp.valueOf(clubInforVo.getActTimeStr().toString()));
//				}
				
			}
			if(clubInforVo.getToTimeStr() != null && !clubInforVo.getToTimeStr().equals("")){
//				if(isNew){
//					clubInfor.setToTime(Timestamp.valueOf(clubInforVo.getToTimeStr().toString()+":00"));
//				}else {
					clubInfor.setToTime(Timestamp.valueOf(clubInforVo.getToTimeStr().toString()));
//				}
				
				
			}
			
			//报名开始时间
			if(clubInforVo.getBeginSignDateStr() != null && !clubInforVo.getBeginSignDateStr().equals("")){
				clubInfor.setBeginSignDate(Timestamp.valueOf(clubInforVo.getBeginSignDateStr().toString()));
			}
			
			//报名截止时间
			if(clubInforVo.getCutDateStr() != null && !clubInforVo.getCutDateStr().equals("")){
				clubInfor.setCutDate(Timestamp.valueOf(clubInforVo.getCutDateStr().toString()));
			}
				 
			clubInfor.setIsDeleted(0);
			

			if (managerId != null && managerId.intValue() > 0) {
				SystemUserInfor manager = (SystemUserInfor) this.systemUserManager.get(managerId);
				clubInfor.setManager(manager);
			}
			
			try {
				// 上传附件
				String attachment = this.uploadAttachment(multipartRequest, "clubInfor");

				
				if (attachment == null || attachment.equals("")) {
					attachment = oldFiles;
				} else {
					if (oldFiles == null || oldFiles.equals("")) {
						
					} else {
//						attachment = attachment + "|" + oldFiles;
						attachment = attachment;
					}
				}
				clubInfor.setMainPic(attachment);
			} catch (Exception ex) {
				model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
				return "/common/error";
			}
			

			ClubInfor newClubInfor = (ClubInfor)this.clubInforManager.save(clubInfor);
			
			
			//新增时，添加批量新增的功能 
			if(isNew){
				String daysStr = request.getParameter("days");   //间隔天数
				String loopCount = request.getParameter("loopCount");    //循环次数
				
				if(loopCount != null && !loopCount.equals("") && daysStr != null && !daysStr.equals("")){
					int count = Integer.valueOf(loopCount) - 1;
					int days = Integer.valueOf(daysStr);
					
					if(count > 0){
						for(int i=1;i<=count;i++){
							ClubInfor tmpClubInfor = new ClubInfor();
							
							tmpClubInfor = clubInfor;
							
							tmpClubInfor.setActId(0);
							
							//设置四个时间
							tmpClubInfor.setBeginSignDate(DateHelper.addDay(newClubInfor.getBeginSignDate(), i * days));
							tmpClubInfor.setCutDate(DateHelper.addDay(newClubInfor.getCutDate(), i * days));
							tmpClubInfor.setActTime(DateHelper.addDay(newClubInfor.getActTime(), i * days));
							tmpClubInfor.setToTime(DateHelper.addDay(newClubInfor.getToTime(), i * days));
							
							this.clubInforManager.save(tmpClubInfor);
						}
					}
				}
			}

			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>");
			// out.print("window.returnValue = 'Y';");
			out.print("window.opener.location.reload();");
			out.print("window.close();");
			out.print("</script>");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * 查看活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewClubInfor")
	public String viewClubInfor(@ModelAttribute("clubInforVo")
	ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int actId = Integer.parseInt(request.getParameter("rowId"));
		ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(actId);
		request.setAttribute("_ClubInfor", clubInfor);
		
		// 附件信息
		String documentAttachment = clubInfor.getMainPic();
		if (documentAttachment != null && !documentAttachment.equals("")) {
			String[][] attachment = processFile(documentAttachment);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		
		return "viewClubInfor";
	}
	
	/**
	 * 手机端查看活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewClubInfor_m")
	public void viewClubInfor_m(@ModelAttribute("clubInforVo")
	ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		int actId = Integer.parseInt(request.getParameter("actId"));
		ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(actId);
		
//		JsonConfig jsonConfig = new JsonConfig();            
//		jsonConfig.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());            
//		jsonObj = JSONObject.fromObject(jsonObj, jsonConfig);
		JSONArray inforArray = new JSONArray();
		List inforList = new ArrayList();
		inforList.add(clubInfor);
		
		List awareObject = new ArrayList();
		awareObject.add("creater.person");
		awareObject.add("manager.person");
		inforArray = convert.modelCollect2JSONArray(inforList, awareObject);
		
		jsonObj.put("_ClubInfor", inforArray);
		
		/*******报名者及签到者************/
		Set<RegisterInfor> registers = clubInfor.getRegisters();
		Set<ActAttendInfor> attenders = clubInfor.getAttends();
		
		JSONArray regArray = new JSONArray();
		JSONArray attArray = new JSONArray();
		
		List regAware = new ArrayList();
		List attAware = new ArrayList();
		
		regAware.add("reger.person");
		attAware.add("attender.person");
		
		regArray = convert.modelCollect2JSONArray(registers,regAware);
		attArray = convert.modelCollect2JSONArray(attenders,attAware);
		
		jsonObj.put("_Registers", regArray);
		jsonObj.put("_Attenders", attArray);
		
		/********当前用户是否已报名、签到*********/
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int personId = systemUser.getPersonId().intValue();
		
//		Set<ActAttendInfor> attenders = new HashSet<ActAttendInfor>();
		//是否已签到
		boolean hasSigned = false;
		for(ActAttendInfor att : attenders){
			if(att.getAttender().getPersonId().intValue() == systemUser.getPersonId().intValue()){
				hasSigned = true;
			}
		}
		
		jsonObj.put("_Signed", hasSigned);
		
//		Set<RegisterInfor> registers = new HashSet<RegisterInfor>();
		//是否已报名
		boolean hasReged = false;
		for(RegisterInfor reg : registers){
			if(reg.getReger().getPersonId().intValue() == systemUser.getPersonId().intValue()){
				hasReged = true;
			}
		}

		jsonObj.put("_Reged", hasReged);
		
		//是否可以报名、签到
		boolean canReg = false;
		if(clubInfor.getStatus() == ClubInfor.Club_Status_Reging && !hasReged){
			canReg = true;
		}
		jsonObj.put("_CanReg", canReg);
		
		boolean canSign = false;
		if(clubInfor.getStatus() == ClubInfor.Club_Status_Acting && !hasSigned){
			canSign = true;
		}
		jsonObj.put("_CanSign", canSign);
		
		/*******当前人是否是发起人或管理员*********/
		boolean isManager = false;
		if(personId == clubInfor.getCreater().getPersonId().intValue() || personId == clubInfor.getManager().getPersonId().intValue()){
			isManager = true;
		}
		jsonObj.put("_IsManager", isManager);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	/**
	 * 手机查看分享的活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewSharedClubInfor")
	public String viewSharedClubInfor(@ModelAttribute("clubInforVo")
	ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		if(StringUtil.isNotEmpty(request.getParameter("actId"))){
			int actId = Integer.parseInt(request.getParameter("actId"));
			
			ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(actId);
			request.setAttribute("_ClubInfor", clubInfor);
		}else {
			return "/login";
		}
		
		
		
		return "/clubShare";
	}
	
	/**
	 * 改变活动状态
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=changeStatus")
	public String changeStatus(@ModelAttribute("clubInforVo")
	ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int actId = clubInforVo.getActId();
		ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(actId);
//		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//		long current = System.currentTimeMillis();
		
		//状态值
		int status = clubInforVo.getStatus();
		
		this.clubInforManager.changeTaskStatus(clubInfor, status);
		
		
		return "redirect:clubInfor.do?method=viewClubInfor&rowId=" + actId;
	}
	
	
	/**
	 *  改变活动状态
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=changeStatus_m")
	public void changeStatus_m(@ModelAttribute("clubInforVo")
			ClubInforVo clubInforVo,HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		JSONObject jsonObj = new JSONObject();
		
		try {
			int actId = clubInforVo.getActId();
			ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(actId);
//			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//			long current = System.currentTimeMillis();
			
			//状态值
			int status = clubInforVo.getStatus();
			
			this.clubInforManager.changeTaskStatus(clubInfor, status);
			
			/*//状态值为2时，即开始报名，此时生成签到二维码  同时，默认发起人及管理员报名(后改为默认都不报名)
			if(status == ClubInfor.Club_Status_Reging){
				if(clubInfor.getTwoPic() == null || clubInfor.getTwoPic().equals("")){
					*//*************生成签到二维码***************//*
					//二维码内容
					String content = "/club/actAttendInfor.do?method=saveAttend&actId="+actId+"&attLocation=";
					int width = 300;          
					int height = 300;          
					//二维码的图片格式          
					String format = "gif";          
					Hashtable hints = new Hashtable();          
					//内容所使用编码          
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");          
					BitMatrix bitMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, width, height, hints); 
					//生成二维码     
					//路径
					String savePath = CoreConstant.Attachment_Path + "clubInfor";
					java.io.File file = new java.io.File(CoreConstant.Context_Real_Path + savePath);
					if(!file.exists()){
						file.mkdir();
					}
					//在folder下面建立目录，以当前时间为目录
					savePath += "/" + current;
					file = new java.io.File(CoreConstant.Context_Real_Path + savePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					savePath += "/" + current + ".gif";
					
					java.io.File outputFile = new java.io.File(CoreConstant.Context_Real_Path + savePath);          
					MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile); 
					
					clubInfor.setTwoPic(CoreConstant.Attachment_Path + "clubInfor" + "/" + current + "/" + current + ".gif");
					*//***************************//*
				}
				
				
			}
			
			//状态值为5时，即结束，此时保存结束时间
			if(status == ClubInfor.Club_Status_Over){
				clubInfor.setEndTime(new Date(current));
			}
			
			clubInfor.setStatus(status);
			
			this.clubInforManager.save(clubInfor);*/
					
			//是否成功标志
			jsonObj.put("_RtnTag", 1);
		} catch (Exception e) {
			jsonObj.put("_RtnTag", 0);
			e.printStackTrace();
		}
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	/**
	 * 批量删除活动
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
					ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(deleteId);
					
					clubInfor.setIsDeleted(1);
					
					this.clubInforManager.save(clubInfor);
				}
			}
		}
		return "listClubInfor";
	}
	
}
