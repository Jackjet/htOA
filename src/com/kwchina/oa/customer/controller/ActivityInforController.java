package com.kwchina.oa.customer.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.customer.entity.ActivityInfor;
import com.kwchina.oa.customer.entity.CustomerInfor;
import com.kwchina.oa.customer.service.ActivityInforManager;
import com.kwchina.oa.customer.service.ContactInforManager;
import com.kwchina.oa.customer.service.CustomerInforManager;
import com.kwchina.oa.customer.vo.ActivityInforVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/customer/activityInfor.do")
public class ActivityInforController extends BasicController {

	@Resource
	private CustomerInforManager customerInforManager;

	@Resource
	private ContactInforManager contactInforManager;

	@Resource
	private ActivityInforManager activityInforManager;

	/***************************************************************************
	 * 显示活动信息列表
	 * 
	 * @param activityInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(@ModelAttribute("activityInforVo")
	ActivityInforVo activityInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		queryString[1] = "select count(activityId) from ActivityInfor where 1=1";
		queryString[0] = "from ActivityInfor activityInfor where 1=1";
		condition = " and customer.customerId in(select customerId from CustomerInfor  where manager.personId='" + user.getPersonId() + "')";

		// 选择查询范围 type：2-计划中 3-已经完成
		if (activityInforVo.getType().equals("2")) {
			condition += " and activityDate=null ";
		} else if (activityInforVo.getType().equals("3")) {
			condition += " and activityDate!=null ";
		}
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.activityInforManager.generateQueryString(queryString, getSearchParams(request));
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.activityInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();

		// 把查询到的结果转化为VO
		List activityVos = new ArrayList();
		if (list.size() > 0) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				ActivityInfor activityInfor = (ActivityInfor) it.next();
				activityInforVo = this.activityInforManager.transPOToVO(activityInfor);
				activityVos.add(activityInforVo);
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
		rows = convert.modelCollect2JSONArray(activityVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/***************************************************************************
	 * 编辑活动信息
	 * 
	 * @param customerInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("activityInforVo")
	ActivityInforVo activityInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int activityId = activityInforVo.getActivityId();
		String planDateStr = "";
		String activityDateStr = "";
		List list = this.customerInforManager.getAll();
		request.setAttribute("customerInforList", list);
		// 如果主键不为0，则本次操作为修改操作
		if (activityId != 0) {
			ActivityInfor activityInfor = new ActivityInfor();
			activityInfor = (ActivityInfor) this.activityInforManager.get(activityId);

			// 转换activityInfor到activityInforVo
			BeanUtils.copyProperties(activityInforVo, activityInfor);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (activityInfor.getPlanDate() != null) {
				planDateStr = dateFormat.format(activityInfor.getPlanDate());
			}
			if (activityInfor.getActivityDate() != null) {
				activityDateStr = dateFormat.format(activityInfor.getActivityDate());
			}
			activityInforVo.setPlanDateStr(planDateStr);
			activityInforVo.setActivityDateStr(activityDateStr);
			activityInforVo.setActivityId(activityId);
			activityInforVo.setCustomerId(activityInfor.getCustomer().getCustomerId());
			activityInforVo.setAttachmentStr(activityInfor.getAttachment());

			// 对附件信息进行处理
			String attachmentFile = activityInfor.getAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}

		}

		return "editActivityInfor";
	}

	/***************************************************************************
	 * 保存活动信息
	 * 
	 * @param customerInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("activityInforVo")
	ActivityInforVo activityInforVo, Model model, HttpServletRequest request, HttpServletResponse response, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		ActivityInfor activityInfor = new ActivityInfor();
		CustomerInfor customerInfor = new CustomerInfor();
		int activityId = activityInforVo.getActivityId();
		customerInfor = (CustomerInfor) this.customerInforManager.get(activityInforVo.getCustomerId());

		String oldFiles = "";
		if (activityId != 0) {

			activityInfor = (ActivityInfor) this.activityInforManager.get(activityId);

			// 修改信息时,对附件进行修改
			String filePaths = activityInfor.getAttachment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

		}
		try {
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest, "customer");

			// 转换activityInforVo到activityInfor
			BeanUtils.copyProperties(activityInfor, activityInforVo);

			// 日期转换
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (activityInforVo.getPlanDateStr() != null && !activityInforVo.getPlanDateStr().equals("")) {
				java.util.Date planDateUtil = dateFormat.parse(activityInforVo.getPlanDateStr());
				Date planDate = new Date(planDateUtil.getTime());
				activityInfor.setPlanDate(planDate);
			}
			if (activityInforVo.getActivityDateStr() != null && !activityInforVo.getActivityDateStr().equals("")) {
				java.util.Date activityDateUtil = dateFormat.parse(activityInforVo.getActivityDateStr());
				Date activityDate = new Date(activityDateUtil.getTime());
				activityInfor.setActivityDate(activityDate);
			}
			activityInfor.setCustomer(customerInfor);

			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			activityInfor.setAttachment(attachment);
		} catch (Exception ex) {
			model.addAttribute("_ErrorMessage", "添加或修改该信息发生错误！<br>错误信息如下:<br>" + ex.toString());
			return "/common/error";
		}

		this.activityInforManager.save(activityInfor);

		/** 该语句不放在编辑页面的原因:
		 * 若在编辑页面提交数据后立即执行window.close()操作,
		 * 则后台无法取到编辑页面的数据.
		 * (此情况仅在页面包含附件操作时存在) 
		 * */
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("window.returnValue = 'Y';");
		out.print("window.close();");
		out.print("</script>");

		return null;
	}

	/***************************************************************************
	 * 查看活动信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewActivity")
	public String view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int activityId = Integer.parseInt(request.getParameter("rowId"));
		ActivityInfor activityInfor = (ActivityInfor) this.activityInforManager.get(activityId);
		request.setAttribute("_activityInfor", activityInfor);

		// 对附件信息进行处理
		String attachmentFile = activityInfor.getAttachment();
		if (attachmentFile != null && !attachmentFile.equals("")) {
			String[][] attachment = processFile(attachmentFile);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}

		return "viewActivityInfor";
	}

	/***************************************************************************
	 * 删除活动信息
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
					ActivityInfor activityInfor = (ActivityInfor) this.activityInforManager.get(deleteId);
					//删除附件
					String filePaths = activityInfor.getAttachment();
					deleteFiles(filePaths);
					
					// 删除客户信息
					this.activityInforManager.remove(deleteId);
					
				}
			}
		}
		return "listAllActivity";
	}

	/***************************************************************************
	 * 以日历的形式显示客户活动信息
	 * 
	 * @param activityInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listMonth")
	public String listMonth(@ModelAttribute("activityInforVo")
	ActivityInforVo activityInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SystemUserInfor systemUser = (SystemUserInfor) request.getSession().getAttribute("_SYSTEM_USER");
		int userId = systemUser.getPersonId().intValue();
		String dateData = request.getParameter("dateData");
		String tag = request.getParameter("tag");
		String tab = request.getParameter("tab");
		String searchYear = activityInforVo.getSearchYear();
		String searchMonth = activityInforVo.getSearchMonth();

		java.sql.Date currentDate = new Date(System.currentTimeMillis());
		SimpleDateFormat curFormat = new SimpleDateFormat("yyyy-MM-dd");

		String currentDateStr = curFormat.format(currentDate); // 当天日期

		String[] currentDateArray = currentDateStr.split("-");
		int dateYear = Integer.valueOf(currentDateArray[0]);
		int dateMonth = Integer.valueOf(currentDateArray[1]);
		int dateDay = Integer.valueOf(currentDateArray[2]);
		String year = String.valueOf(dateYear);

		if ((dateData != null && dateData.length() > 0) || ("changeMonth").equals(tag) || ("changeYear").equals(tab)
				||("changeMonthDown").equals(tab)||("changeMonthUp").equals(tab)) { // 上下翻月和下拉选择年份以及月份
			Calendar dateDataCal = Calendar.getInstance();
			Integer	searchMonthInteger = 0;
			Integer	searchYearInteger = 0;
			if (("changeMonth").equals(tag) || ("changeYear").equals(tab)) {
				dateData = searchYear + "-" + searchMonth + "-01";
			}
			
			//上下月
			if (("changeMonthDown").equals(tag)) {
				searchMonthInteger = Integer.parseInt(searchMonth)-1;
				if(searchMonthInteger==0){
					searchMonthInteger=1;
				}
				dateData = searchYear + "-" + searchMonthInteger.toString() + "-01";
				activityInforVo.setSearchMonth(searchMonthInteger.toString());
			}else if (("changeMonthUp").equals(tag)) {
				searchMonthInteger = Integer.parseInt(searchMonth)+1;
				if(searchMonthInteger==13){
					searchMonthInteger=12;
				}
				dateData = searchYear + "-" + searchMonthInteger.toString() + "-01";
				activityInforVo.setSearchMonth(searchMonthInteger.toString());
			}
			
			//上下年
			if (("changeYearDown").equals(tag)) {
				searchYearInteger = Integer.parseInt(searchYear)-1;
				dateData = searchYearInteger.toString() + "-" + searchMonth + "-01";
				activityInforVo.setSearchYear(searchYearInteger.toString());
			}else if (("changeYearUp").equals(tag)) {
				searchYearInteger = Integer.parseInt(searchYear)+1;
				dateData = searchYearInteger.toString() + "-" + searchMonth + "-01";
				activityInforVo.setSearchYear(searchYearInteger.toString());
			}
			
			dateDataCal.setTime(java.sql.Date.valueOf(dateData));
			int totalDay = dateDataCal.getActualMaximum(Calendar.DAY_OF_MONTH); // 总天数
			String[] dateDataArray = dateData.split("-");
			int dateDataDay = Integer.valueOf(dateDataArray[2]);
			/** 判断系统时间中的日是否超过上/下个月日期，若超过则改为所在月份的最大天数，若未超过则仍未系统时间中的日 */
			dateData = dateDataArray[0] + "-" + dateDataArray[1] + "-" + String.valueOf((totalDay < dateDay) ? (dateDataDay = totalDay) : (dateDataDay = dateDay));
			currentDate = java.sql.Date.valueOf(dateData);

			/** ******currentDate被修改后重新取相关值***** */
			currentDateArray = dateData.split("-");
			dateYear = Integer.valueOf(currentDateArray[0]);
			dateMonth = Integer.valueOf(currentDateArray[1]);
			dateDay = Integer.valueOf(currentDateArray[2]);
			/** ***************************************** */
		}

		if (searchYear == "") {
			activityInforVo.setSearchYear(String.valueOf(dateYear));
		}
		if (searchMonth == "") {
			activityInforVo.setSearchMonth(String.valueOf(dateMonth));
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		int totalDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 当月的总天数

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		String firstDateStr = sdf.format(currentDate);
		java.sql.Date firstDate = java.sql.Date.valueOf(firstDateStr); // 当月第一天日期
		cal.setTime(firstDate);

		int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;

		ArrayList scheduleList = new ArrayList();
		int[] days = new int[totalDay];

		for (int k = 0; k < weekDay; k++) { // 当月的第一天是星期几,scheduleList的开始就填入几个null数据
			scheduleList.add(null);
		}

		for (int i = 1; i <= totalDay; i++) {
			String planedDate = dateYear + "-" + dateMonth + "-" + i;
			String hql = " from ActivityInfor activityInfor where 1=1";
			String condition = "";

			// 当前客户经理的活动
			condition = " and customer.customerId in(select customerId from CustomerInfor  where manager.personId='" + systemUser.getPersonId() + "')";
			condition += " and planDate = '" + planedDate + "'";

			hql += condition;
			List plan = this.activityInforManager.getResultByQueryString(hql);
			if (plan.isEmpty()) {
				scheduleList.add(null);
			} else {
				scheduleList.add(plan);
			}
			String curDate = dateYear + "-" + dateMonth + "-" + i;
			// date[i-1] = java.sql.Date.valueOf(curDate);
			days[i - 1] = i;
		}
		for (int k = 0; k < (7 - (weekDay + totalDay) % 7) % 7; k++) { // 将scheduleList的末尾补上null数据)
			scheduleList.add(null);
		}

		request.setAttribute("_NowYear", year); // 当前年代
		request.setAttribute("_WeekDay", weekDay); // 当月的第一天是星期几
		request.setAttribute("_ScheduleList", scheduleList);
		request.setAttribute("_Year", activityInforVo.getSearchYear());
		request.setAttribute("_Month", activityInforVo.getSearchMonth());
		request.setAttribute("_CurrentDay", (weekDay + dateDay - 1)); // 当天数据在scheduleList中所处的位置
		request.setAttribute("_LastDate", ((dateMonth == 1) ? (dateYear - 1) : dateYear) + "-" + ((dateMonth == 1) ? 12 : (dateMonth - 1)) + "-01");
		request.setAttribute("_NextDate", ((dateMonth == 12) ? (dateYear + 1) : dateYear) + "-" + ((dateMonth == 12) ? 1 : (dateMonth + 1)) + "-01");
		request.setAttribute("_CurrentDate", currentDate);
		request.setAttribute("_Date", days);

		return "listMonth";

	}

}
