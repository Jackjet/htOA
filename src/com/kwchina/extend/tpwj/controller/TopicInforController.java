package com.kwchina.extend.tpwj.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.weaver.ISourceContext;
import org.hamcrest.core.Is;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.club.entity.ActAttendInfor;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.entity.RegisterInfor;
import com.kwchina.extend.club.vo.ClubExcelVo;
import com.kwchina.extend.tpwj.entity.ItemInfor;
import com.kwchina.extend.tpwj.entity.OptionInfor;
import com.kwchina.extend.tpwj.entity.TopicInfor;
import com.kwchina.extend.tpwj.entity.TopicRight;
import com.kwchina.extend.tpwj.entity.VoteInfor;
import com.kwchina.extend.tpwj.entity.VoteItemInfor;
import com.kwchina.extend.tpwj.service.ItemInforManager;
import com.kwchina.extend.tpwj.service.TopicInforManager;
import com.kwchina.extend.tpwj.service.TopicRightManager;
import com.kwchina.extend.tpwj.service.VoteInforManager;
import com.kwchina.extend.tpwj.service.VoteItemInforManager;
import com.kwchina.extend.tpwj.vo.TopicInforVo;
import com.kwchina.extend.tpwj.vo.VoteExcelVo;
import com.kwchina.oa.meeting.entity.AttendInfor;
import com.kwchina.oa.meeting.entity.MeetInfor;
import com.kwchina.oa.util.SysCommonMethod;
import com.sun.xml.bind.v2.runtime.unmarshaller.IntArrayData;

@Controller
@RequestMapping("/tpwj/topicInfor.do")
public class TopicInforController extends BasicController{

	@Resource
	private TopicInforManager topicInforManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private ItemInforManager itemInforManager;
	
	@Resource
	private VoteItemInforManager voteItemInforManager;
	
	@Resource
	private VoteInforManager voteInforManager;
	
	@Resource
	private TopicRightManager topicRightManager;
	
	/***
	 * 显示所有投票问卷信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=list")
	public void list(@ModelAttribute("topicInforVo")
	TopicInforVo topicInforVo, Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {

//		// 构造查询语句
//		String[] queryString = this.topicInforManager.generateQueryString("InvestmentEnterpriseInfor", "enterpriseId", getSearchParams(request));
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		String[] queryString = new String[2];
		String condition = "";
		queryString[0] = "from TopicInfor topicInfor where topicInfor.deleted is false ";
		queryString[1] = "select count(topicInfor.topicId) from TopicInfor topicInfor where topicInfor.deleted is false ";
		
		condition += " and topicInfor.type="+topicInforVo.getType();
		condition += " and (topicInfor.openType=1 or (topicInfor.openType=0 and topicInfor.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+"))";
		condition += " or topicInfor.creater.personId="+systemUser.getPersonId();
		condition += " or 1="+systemUser.getUserType()+")";
		
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.topicInforManager.generateQueryString(queryString, getSearchParams(request));

		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.topicInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		// 把查询到的结果转化为VO
//		List topicVos = new ArrayList();
//		if (list.size() > 0) {
//			for (Iterator it = list.iterator(); it.hasNext();) {
//				TopicInfor topic = (TopicInfor) it.next();
//
//				// 把查询到的结果转化为VO
//				topicInforVo = this.topicInforManager.transPOToVO(topic);
//				//是否是本投票的投票人
//				boolean isVoter = false;
//				
//				if(topic.getOpenType() == 1){
//					isVoter = true;
//				}else {
//					String voterSql = "from TopicRight topicRight where topicRight.topic.topicId="+topic.getTopicId()+"and topicRight.systemUser.personId="+systemUser.getPersonId();
//					List voterList = this.topicRightManager.getResultByQueryString(voterSql);
//					if(voterList != null && voterList.size() > 0){
//						isVoter = true;
//					}
//				}
//				topicInforVo.setVoter(isVoter);
//				
//				topicVos.add(topicInforVo);
//			}
//		}
		
		
		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
//		awareObject.add("department");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
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
	public String expertExcel(@ModelAttribute("topicInforVo")
	TopicInforVo topicInforVo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			String queryString = "";
			queryString = "from TopicInfor topicInfor where topicInfor.deleted is false ";
			
			queryString += " and topicInfor.type="+topicInforVo.getType();
			queryString += " and (topicInfor.openType=1 or (topicInfor.openType=0 and topicInfor.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+"))";
			queryString += " or topicInfor.creater.personId="+systemUser.getPersonId();
			queryString += " or 1="+systemUser.getUserType()+")";
			
			//过滤条件
			String topicName = request.getParameter("topicName");
			String departmentId = request.getParameter("departmentId");
			String personId = request.getParameter("personId");
			
			if(topicName != null && !topicName.equals("")){
				queryString += " and topicInfor.topicName like '%" + topicName + "%' ";
			}
			
			List<TopicInfor> infors = this.topicInforManager.getResultByQueryString(queryString);
			
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<VoteExcelVo> vos = new ArrayList<VoteExcelVo>();
			
			//再根据部门、人员条件筛选投票结果
			for(TopicInfor topic : infors){
				String voteQueryString = "from VoteInfor vote where vote.topic.topicId = " + topic.getTopicId();
				if(departmentId != null && !departmentId.equals("")){  //部门筛选不为空时
					voteQueryString += " and vote.voter.person.department.organizeId = " + Integer.valueOf(departmentId);
				}
				if(personId != null && !personId.equals("")){
					voteQueryString += " and vote.voter.person.personId = " + Integer.valueOf(personId);
				}
				
				List<VoteInfor> votes = this.voteInforManager.getResultByQueryString(voteQueryString);
				for(VoteInfor vote : votes){
					Set<VoteItemInfor> items = vote.getVoteItems();
					for(VoteItemInfor voteItem : items){
						VoteExcelVo vo = new VoteExcelVo();
						
						vo.setDepartment(vote.getVoter().getPerson().getDepartment().getOrganizeName());
						vo.setPersonName(vote.getVoter().getPerson().getPersonName());
						vo.setTopicName(topic.getTopicName());
						vo.setItemName(voteItem.getItem().getItemName());
						
						String optionName = "";
						if(voteItem.getItem().getItemType() == 0){//单选
							if(StringUtil.isNotEmpty(voteItem.getVoteValue()) && voteItem.getVoteValue().contains(",")){
								int selOptionId = Integer.valueOf(voteItem.getVoteValue().split(",")[0]);
								
								//所有待选项
								Set<OptionInfor> options = voteItem.getItem().getOptions();
								for(OptionInfor option : options){
									if(option.getOptionId() == selOptionId){
										optionName = option.getOptionName();
										break;
									}
								}
							}else {
								optionName = "";
							}
						}else if(voteItem.getItem().getItemType() == 1){//多选
							if(StringUtil.isNotEmpty(voteItem.getVoteValue()) && voteItem.getVoteValue().contains(",")){
								String[] selOptionIds = voteItem.getVoteValue().split(",");
								
								for(int i=0;i<selOptionIds.length;i++){
									int selOptionId = Integer.valueOf(selOptionIds[i]);
									
									//所有待选项
									Set<OptionInfor> options = voteItem.getItem().getOptions();
									for(OptionInfor option : options){
										if(option.getOptionId() == selOptionId){
											optionName += option.getOptionName() + " | ";
											break;
										}
									}
								}
								
							}else {
								optionName = "";
							}
						}else if(voteItem.getItem().getItemType() == 2 || voteItem.getItem().getItemType() == 3){//文本或者段落
							optionName = voteItem.getVoteText();
							
						}else if(voteItem.getItem().getItemType() == 5){//列表型
							
						}
						
						vo.setOptionName(optionName);
						
						vos.add(vo);
					}
				}
				
			}

			/******************导出Excel********************/
//			String filePath = SystemConstant.Submit_Path + time + "/";
			String filePath = "/"+CoreConstant.Attachment_Path + "tpwj/";
			String fileTitle = "投票问卷统计结果";

			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);

			List<String> rowName = new ArrayList<String>();
			
			int k = 0;// 列数

			rowName.add("序号");
			rowName.add("部门");
			rowName.add("人员");
			rowName.add("投票问卷名称");
			rowName.add("题目");
			rowName.add("答案");
			k = 7;

			
			String[][] data = new String[k][vos.size()];
			
			for(int i = 0;i < vos.size(); i++){
				VoteExcelVo vo = vos.get(i);
				
				data[0][i] = String.valueOf(i + 1);
				data[1][i] = vo.getDepartment();
				data[2][i] = vo.getPersonName();
				data[3][i] = vo.getTopicName();
				data[4][i] = vo.getItemName();
				data[5][i] = vo.getOptionName();
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
	
	
	/***
	 * 修改或新增投票
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, TopicInforVo vo, Model model) throws Exception {
		
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setTopicId(Integer.valueOf(rowId));
		}
		Integer topicId = vo.getTopicId();
		
		String beginDate = "";
		String endDate = "";
		
		//修改
		if (topicId != null && topicId.intValue() != 0) {
			
			TopicInfor topicInfor = (TopicInfor) this.topicInforManager.get(topicId.intValue());

			BeanUtils.copyProperties(vo, topicInfor);
			
						
			//系统用户
			List users = this.systemUserManager.getAll();
			int[] personIds = new int[users.size()];	
			Set roleUsers = topicInfor.getRights();
			
			for (int i = 0; i < users.size(); i++) {
				SystemUserInfor tempUser = (SystemUserInfor) users.get(i);
				int tempPersonId = tempUser.getPersonId().intValue();
				
				for (Iterator it = roleUsers.iterator(); it.hasNext();) {
					TopicRight right = (TopicRight) it.next();
					int rPersonId = right.getSystemUser().getPersonId().intValue();
					
					if (tempPersonId == rPersonId) {
						//该用户属于该角色
						personIds[i] = tempPersonId;
						break;
					}
				}
			}
			vo.setPersonIds(personIds);
			model.addAttribute("_PersonIds", personIds);
			
			model.addAttribute("_StartTime",topicInfor.getStartTime());
			model.addAttribute("_EndTime",topicInfor.getEndTime());
		}else {
			vo.setValid(true);
			vo.setCheckCount(false);
			vo.setOpenType(1);
		}

		//根据职级获取用户
		List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);		
		model.addAttribute("_Users", users);
		
		//获取职级大于一定值的用户
		List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);		
		model.addAttribute("_OtherUsers", otherUsers);
		
		//全部部门信息
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);
		
		return "/extend/tpwj/editTopicInfor";
	}
	
	/***
	 * 公示或取消首页显示
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=display")
	public void display(HttpServletRequest request, HttpServletResponse response, TopicInforVo vo) throws Exception {
		TopicInfor topicInfor = new TopicInfor();
		
		if (vo.getTopicId() != null && vo.getTopicId().intValue() != 0) {
			topicInfor = (TopicInfor) this.topicInforManager.get(vo.getTopicId());
		}
		
		topicInfor.setDisplay(vo.isDisplay());
		if(vo.isDisplay()){
			Date createDate = new Date(System.currentTimeMillis());
			topicInfor.setDisplayDate(createDate);
		}else {
			topicInfor.setDisplayDate(null);
		}
		
		this.topicInforManager.save(topicInfor);
	}
	
	
	/***
	 * 保存投票基本信息
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, TopicInforVo vo) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		TopicInfor topicInfor = new TopicInfor();
		
		if (vo.getTopicId() != null && vo.getTopicId().intValue() != 0) {
			topicInfor = (TopicInfor) this.topicInforManager.get(vo.getTopicId());
			
			topicInfor.setDisplay(topicInfor.isDisplay());
			topicInfor.setDisplayDate(topicInfor.getDisplayDate());
		}else{
			// 创建时间默认为当前时间
			Date createDate = new Date(System.currentTimeMillis());
			topicInfor.setCreateTime(createDate);
			topicInfor.setCreater(systemUser);
			
			topicInfor.setDisplay(false);
			topicInfor.setDisplayDate(null);
		}


		// 保存信息
		topicInfor.setTopicName(vo.getTopicName());
		topicInfor.setDescrip(vo.getDescrip());
		topicInfor.setRules(vo.getRules());
		topicInfor.setValid(vo.isValid());
		topicInfor.setOpenType(vo.getOpenType());
		topicInfor.setType(vo.getType());
		
		topicInfor.setCheckCount(vo.isCheckCount());
		
		
		//起始日期
		String beginTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		
		if(beginTimeStr != null && !beginTimeStr.equals("")){
			topicInfor.setStartTime(java.sql.Date.valueOf(beginTimeStr));
		}
		
		if(endTimeStr != null && !endTimeStr.equals("")){
			topicInfor.setEndTime(java.sql.Date.valueOf(endTimeStr));
		}
		
		
		//开放类型(0-自定义;1-全体用户)
		int openType = vo.getOpenType();
		Set users = new HashSet();
		
		if(openType == 0){
			// 自定义,没有的加上,去掉的删除
			int[] personIds = vo.getPersonIds();
			Set rights = topicInfor.getRights();

			// 此次选择中，去掉的删除
			TopicRight[] arrayRight = (TopicRight[]) rights.toArray(new TopicRight[rights.size()]);
			for (int k = arrayRight.length - 1; k >= 0; k--) {
				TopicRight right = (TopicRight) arrayRight[k];
				SystemUserInfor tempUser = right.getSystemUser();
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
				
				if (!hasThisUser) {
					rights.remove(right);
					// remove from db
					this.topicRightManager.remove(right);
				}
			}

			// 没有的加上
			if (personIds != null && personIds.length != 0) {
				for (int kk = 0; kk < personIds.length; kk++) {
					boolean hasThisUser = false;
					TopicRight right = new TopicRight();

					for (Iterator it = rights.iterator(); it.hasNext();) {
						right = (TopicRight) it.next();

						SystemUserInfor tempUser = right.getSystemUser();
						int rPersonId = tempUser.getPersonId().intValue();
						if (rPersonId == personIds[kk]) {
							hasThisUser = true;
							break;
						}
					}

					if (!hasThisUser) {
						TopicRight tpRight = new TopicRight();
						tpRight.setTopic(topicInfor);
						SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[kk]);
						tpRight.setSystemUser(tpUser);
						rights.add(tpRight);
					}
				}
			}
		}
		
//		if (openType == 0) {
//			/** 获得用户对应的用户 */
//			int[] personIds = vo.getPersonIds();		
//			if (personIds != null) {
//				for (int k = 0; k < personIds.length; k++) {
//					SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(personIds[k]);				
//					users.add(user);
//				}
//			}
//		}else {
//			List<SystemUserInfor> validUsers = this.systemUserManager.getAllValid();
//			if (validUsers != null && validUsers.size() > 0) {
//				for (SystemUserInfor user:validUsers) {
//					users.add(user);
//				}
//			}
//		}
		
//		topicInfor.setRights(rights);	

		this.topicInforManager.save(topicInfor);
	}
	
	/***
	 * 查看投票信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=viewTopicInfor")
	public String viewTopicInfor(HttpServletRequest request, HttpServletResponse response, TopicInforVo vo, Model model) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		//首先将此主题下的所有条目及选项找出，列在编辑表单页面
		int topicId = vo.getTopicId();
		TopicInfor topicInfor = new TopicInfor();
		if(topicId > 0){
			topicInfor = (TopicInfor)this.topicInforManager.get(topicId);
		}
		request.setAttribute("_TopicInfor", topicInfor);
		
		//查出当前人员的投票信息
		//if(vo.getVoterId() > 0){
			String voteInforSql = "from VoteInfor voteInfor where voteInfor.topic.topicId="+topicId+" and voteInfor.voter.personId="+systemUser.getPersonId();
			List voteInforList = this.voteInforManager.getResultByQueryString(voteInforSql);
			if(voteInforList.size() > 0){
				request.setAttribute("_VoteInfor", voteInforList.get(0));
			}
		//}
		
		
		//得到当前topic下所有item的categoryName，以供选择使用
		String categoryNameSql = "select distinct(categoryName) from ItemInfor item where item.topic.topicId="+topicId;
		List categoryList = this.itemInforManager.getResultByQueryString(categoryNameSql);
		request.setAttribute("_CategoryList", categoryList);
		
		boolean isOut = false;
		Date today = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		String todayStr = sf.format(today);
		String endDayStr = sf.format(topicInfor.getEndTime());
		//判断当前是否已过期
		if(new Date().after(topicInfor.getEndTime()) && !todayStr.equals(endDayStr)){
			//System.out.println(new Date()+"--"+!new Date().equals(topicInfor.getEndTime()));
			isOut = true;
		}
		request.setAttribute("_IsOut",isOut);
		
		//判断当前人员是否已投过
		boolean hasDone = false;
		Set<VoteInfor> voteInfors = topicInfor.getVoteInfors();
		if(voteInfors.size() > 0){
			for(VoteInfor vt : voteInfors){
				int a = vt.getVoter().getPersonId();
				int b = systemUser.getPersonId();
				if(a == b){
					hasDone = true;
					break;
				}
			}
		}
		request.setAttribute("_HasDone", hasDone);
		

		
		return "/extend/tpwj/viewTopicInfor";
	}
	
	/***
	 * 查看投票统计信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=viewCount")
	public String viewCount(HttpServletRequest request, HttpServletResponse response, TopicInforVo vo, Model model) throws Exception {
		//首先将此主题下的所有条目及选项找出，列在编辑表单页面
		int topicId = vo.getTopicId();
		TopicInfor topicInfor = new TopicInfor();
		if(topicId > 0){
			topicInfor = (TopicInfor)this.topicInforManager.get(topicId);
		}
		request.setAttribute("_TopicInfor", topicInfor);
		
		
		//得到当前topic下所有item的categoryName，以供选择使用
		String categoryNameSql = "select distinct(categoryName) from ItemInfor item where item.topic.topicId="+topicId;
		List categoryList = this.itemInforManager.getResultByQueryString(categoryNameSql);
		request.setAttribute("_CategoryList", categoryList);

		//找到所有的投票信息
		String voteInforSql = "from VoteItemInfor voteItem where voteItem.voteInfor.topic.topicId="+topicId;
		List<VoteItemInfor> voteItemList = this.voteItemInforManager.getResultByQueryString(voteInforSql);
		request.setAttribute("_VoteItemList", voteItemList);
		
		//得到所有用户人数
		String userCountSql = "select count(personId) from SystemUserInfor user where user.invalidate = 0 and user.person.deleted = 0";
		int userCount = this.systemUserManager.getResultNumByQueryString(userCountSql);
		request.setAttribute("_AllUserCount", userCount);
		
		String directStr = "/extend/tpwj/viewCount";
		//如果是问卷的话，跳转到另一个页面
		if(topicInfor.getType() == 1){
			List<ItemInfor> itemList = new ArrayList<ItemInfor>();
			itemList.addAll(topicInfor.getItems());
			Set<OptionInfor> opSet = itemList.get(0).getOptions();
			request.setAttribute("_Options", opSet);
			directStr = "/extend/tpwj/viewWjCount";
		}
		return directStr;
	}
	
	
	/***
	 * 按人查看投票信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=viewVoters")
	public String viewVoters(HttpServletRequest request, HttpServletResponse response, TopicInforVo vo, Model model) throws Exception {
		//首先将此主题下的所有条目及选项找出，列在编辑表单页面
		int topicId = vo.getTopicId();
		TopicInfor topicInfor = new TopicInfor();
		if(topicId > 0){
			topicInfor = (TopicInfor)this.topicInforManager.get(topicId);
		}
		request.setAttribute("_TopicInfor", topicInfor);
		
		
		//得到当前topic下所有item的categoryName，以供选择使用
		String categoryNameSql = "select distinct(categoryName) from ItemInfor item where item.topic.topicId="+topicId;
		List categoryList = this.itemInforManager.getResultByQueryString(categoryNameSql);
		request.setAttribute("_CategoryList", categoryList);
		
		//判断投票人是否为空，如果不为空，则查出他的投票信息
		if(vo.getVoterId() > 0){
			String voteInforSql = "from VoteInfor voteInfor where voteInfor.topic.topicId="+topicId+" and voteInfor.voter.personId="+vo.getVoterId();
			List voteInforList = this.voteInforManager.getResultByQueryString(voteInforSql);
			if(voteInforList.size() > 0){
				request.setAttribute("_VoteInfor", voteInforList.get(0));
			}
		}
		
		//查出未投票人
		//全部投票人
		List<SystemUserInfor> allVoters = new ArrayList<SystemUserInfor>();
		if(topicInfor.getOpenType() == 0){   //自定义 
			Set<TopicRight> rights = topicInfor.getRights();
			for(TopicRight right : rights){
				allVoters.add(right.getSystemUser());
			}
		}else if(topicInfor.getOpenType() == 1){   //全体
			allVoters = this.systemUserManager.getAllValid();
		}
		
		//已投票人
		Set<VoteInfor> voteInfors = topicInfor.getVoteInfors();
		List<SystemUserInfor> votedUsers = new ArrayList<SystemUserInfor>();
		for(VoteInfor voteInfor : voteInfors){
			votedUsers.add(voteInfor.getVoter());
		}
		
		//取得未投票人
		allVoters.removeAll(votedUsers);
		request.setAttribute("_NotVoteUsers", allVoters);
		
		return "/extend/tpwj/viewVoters";
	}
	
	
	/**
	 * 删除主题信息
	 * 
	 * @param request
	 * @param response
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
					TopicInfor topic = (TopicInfor) this.topicInforManager.get(deleteId);
					topic.setDeleted(true);
					this.topicInforManager.save(topic);
//					this.topicInforManager.remove(topic);
				}
			}
		}
		return "/extend/tpwj/listTopicInfor";
	}

	/**
	 * 获取需要首页显示的投票问卷信息(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getTopics")
	public void getTopics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Date sysDate = new Date(System.currentTimeMillis());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		Date today = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		String todayStr = sf.format(today);
		
		String queryHQL = " from TopicInfor topic where topic.startTime<='"+todayStr+"' and topic.endTime >='"+todayStr+"' and topic.valid is true and topic.deleted is false ";
		String condition = "";
		
		condition += " and (topic.openType=1 or (topic.openType=0 and topic.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+")))";
		condition += " and topic.topicId not in(select voteInfor.topic.topicId from VoteInfor voteInfor where voteInfor.voter.personId="+systemUser.getPersonId()+")";
		condition += " order by topic.endTime";
		
		queryHQL += condition;
		
		List infors = this.topicInforManager.getResultByQueryString(queryHQL);
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		//awareObject.add("author.person");
		
		jsonArray = convert.modelCollect2JSONArray(infors, awareObject);
		jsonObj.put("_Topics", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}
	
	
	/**
	 * 获取需要首页公示的投票问卷结果信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getPublicTopics")
	public void getPublicTopics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Date sysDate = new Date(System.currentTimeMillis());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		Date today = new Date();
		//今天前的一个月内
		Date oneMonthAgo = DateHelper.addMonth(today, -1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		String oneMonthAgoStr = sf.format(oneMonthAgo);
		
		String queryHQL = " from TopicInfor topic where topic.displayDate>='"+oneMonthAgoStr+"' and  topic.display is true and valid is true and topic.deleted is false ";
		String condition = "";
		
		//condition += " and (topic.openType=1 or (topic.openType=0 and topic.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+")))";
		//condition += " and topic.topicId not in(select voteInfor.topic.topicId from VoteInfor voteInfor where voteInfor.voter.personId="+systemUser.getPersonId()+")";
		condition += " order by topic.displayDate desc";
		
		queryHQL += condition;
		
		List infors = this.topicInforManager.getResultByQueryString(queryHQL);
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		//awareObject.add("author.person");
		
		jsonArray = convert.modelCollect2JSONArray(infors, awareObject);
		jsonObj.put("_PublicTopics", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}
	
	/**
	 * 获取需要首页显示的、有“查看统计”权限的投票人能看到的，问卷结果信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getCanCountTopics")
	public void getCanCountTopics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Date sysDate = new Date(System.currentTimeMillis());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		Date today = new Date();
		//今天前的一个月内
		Date oneMonthAgo = DateHelper.addMonth(today, -1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		String oneMonthAgoStr = sf.format(oneMonthAgo);
		
		String queryHQL = " from TopicInfor topic where topic.startTime>='"+oneMonthAgoStr+"' and checkCount is true and valid is true and topic.deleted is false ";
		
		queryHQL += " and (topic.openType=1 or (topic.openType=0 and topic.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+"))";
		queryHQL += " or topic.creater.personId="+systemUser.getPersonId()+")";
		String condition = "";
		
		//condition += " and (topic.openType=1 or (topic.openType=0 and topic.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+")))";
		//condition += " and topic.topicId not in(select voteInfor.topic.topicId from VoteInfor voteInfor where voteInfor.voter.personId="+systemUser.getPersonId()+")";
		condition += " order by topic.startTime desc";
		
		queryHQL += condition;
		
		List infors = this.topicInforManager.getResultByQueryString(queryHQL);
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		//awareObject.add("author.person");
		
		jsonArray = convert.modelCollect2JSONArray(infors, awareObject);
		jsonObj.put("_CountTopics", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}
}
