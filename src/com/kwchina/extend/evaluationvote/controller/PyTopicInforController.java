package com.kwchina.extend.evaluationvote.controller;

import java.io.PrintWriter;
import java.sql.Date;
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
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;
import com.kwchina.extend.evaluationvote.entity.PyTopicInforRight;
import com.kwchina.extend.evaluationvote.entity.PyVoteInfor;
import com.kwchina.extend.evaluationvote.entity.PyVoteItemInfor;
import com.kwchina.extend.evaluationvote.service.PyTopicInforManager;
import com.kwchina.extend.evaluationvote.service.PyTopicInforRightManager;
import com.kwchina.extend.evaluationvote.service.PyVoteInforManager;
import com.kwchina.extend.evaluationvote.service.PyVoteItemInforManager;
import com.kwchina.extend.evaluationvote.vo.PyTopicInforVo;
import com.kwchina.extend.tpwj.entity.TopicInfor;
import com.kwchina.extend.tpwj.vo.TopicInforVo;
import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/extend/pyTopicInfor.do")
public class PyTopicInforController extends BasicController{
	
	@Resource
	private PyTopicInforManager pyTopicInforManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private PyVoteInforManager pyVoteInforManager;
	
	@Resource
	private PyVoteItemInforManager pyVoteItemInforManager;
	
	@Resource
	private PyTopicInforRightManager pyTopicInforRightManager;
	
	
	//显示所有评优
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//构造查询语句
		//String[] queryString = this.pyTopicInforManager.generateQueryString("PyTopicInfor", "topicId", getSearchParams(request));
		
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		queryString[0] = "from PyTopicInfor topicInfor where 1=1";
		queryString[1] = "select count(topicId) from PyTopicInfor topicInfor  where 1=1";
		//condition = " and sender.personId='" + user.getPersonId() + "' and isDelete = 0";
		
		int userId = user.getPersonId().intValue();
		if (user.getUserType() != SystemConstant._User_Type_Admin) {
			condition += "  and (topicInfor.person.personId = " + userId +" or openType=1"
					+ " or (topicInfor.topicId in (select userRight.topicInfor.topicId from PyTopicInforRight userRight where userRight.user.personId =" + userId + ")))";
		}
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.pyTopicInforManager.generateQueryString(queryString, getSearchParams(request));

		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.pyTopicInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		rows = convert.modelCollect2JSONArray(list, new ArrayList());
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	//	新增或者修改评优信息
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, PyTopicInforVo vo, Model model) throws Exception {

		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setTopicId(Integer.valueOf(rowId));
		}
		Integer topicId = vo.getTopicId();
		
		//修改
		if (topicId != null && topicId.intValue() != 0) {
			
			//判断是否有编辑权限
			//this.dataRightInforManager.haveDataRight(request, response, "roleId", roleId, "edit");	
			
			PyTopicInfor pyTopicInfor = (PyTopicInfor)this.pyTopicInforManager.get(topicId);						
			
			//属性,从model到vo
			BeanUtils.copyProperties(vo, pyTopicInfor);
			
			//系统用户
			List users = this.systemUserManager.getAll();
			int[] personIds = new int[users.size()];	
			Set roleUsers = pyTopicInfor.getRights();
			
			for (int i = 0; i < users.size(); i++) {
				SystemUserInfor tempUser = (SystemUserInfor) users.get(i);
				int tempPersonId = tempUser.getPersonId().intValue();
				
				for (Iterator it = roleUsers.iterator(); it.hasNext();) {
					//SystemUserInfor rUser = (SystemUserInfor) it.next();
					PyTopicInforRight rUser = (PyTopicInforRight) it.next();
					int rPersonId = rUser.getUser().getPersonId().intValue();
					
					if (tempPersonId == rPersonId) {
						//该用户属于该角色
						personIds[i] = tempPersonId;
						break;
					}
				}
			}
			
			
			
			vo.setPersonIds(personIds);
			model.addAttribute("_PersonIds", personIds);
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

		return "evaluationvote/editTopicInfor";
	}
	
	//保存评优信息
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, PyTopicInforVo vo) throws Exception {

		SystemUserInfor person = SysCommonMethod.getSystemUser(request);
		PyTopicInfor pyTopicInfor = new PyTopicInfor();
		
		//角色类型(0-自定义;1-全体用户)
		int topicId = vo.getTopicId();
		int openType = vo.getOpenType();
		
		if (topicId != 0) {
			pyTopicInfor = (PyTopicInfor) this.pyTopicInforManager.get(topicId);
			
		}
					
		//BeanUtils.copyProperties(pyTopicInfor, vo);
		pyTopicInfor.setTopicName(vo.getTopicName());
		pyTopicInfor.setDescrip(vo.getDescrip());
		pyTopicInfor.setRuler(vo.getRuler());
		pyTopicInfor.setStartTime(Date.valueOf(vo.getStartTime().toString()));
		pyTopicInfor.setEndTime(Date.valueOf(vo.getEndTime().toString()));
		if (vo.getTopicId() == null || vo.getTopicId() == 0) {
			pyTopicInfor.setCreateTime(new Date(System.currentTimeMillis()));
		}
		pyTopicInfor.setPerson(person);
		pyTopicInfor.setValid(vo.isValid());
		pyTopicInfor.setOpenType(openType);
		pyTopicInfor.setHomepage(vo.isHomepage());
		pyTopicInfor.setSameDept(vo.isSameDept());
		//Set users = new HashSet();
		
		if (openType == 0) {
			/** 获得用户对应的用户 */
			int[] personIds = vo.getPersonIds();		
			Set rights = pyTopicInfor.getRights();

			// 此次选择中，去掉的并且尚未回复的删除
			PyTopicInforRight[] arrayRight = (PyTopicInforRight[]) rights.toArray(new PyTopicInforRight[rights.size()]);
			for (int k = arrayRight.length - 1; k >= 0; k--) {
				PyTopicInforRight right = (PyTopicInforRight) arrayRight[k];
				SystemUserInfor tempUser = right.getUser();
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
					// this.receiveMessageManager.remove(receive);
					rights.remove(right);

					// remove from db
					this.pyTopicInforRightManager.remove(right);
				}
			}

			// 没有的加上
			if (personIds != null && personIds.length != 0) {
				for (int kk = 0; kk < personIds.length; kk++) {
					boolean hasThisUser = false;
					PyTopicInforRight right = new PyTopicInforRight();

					for (Iterator it = rights.iterator(); it.hasNext();) {
						right = (PyTopicInforRight) it.next();

						SystemUserInfor tempUser = right.getUser();
						int rPersonId = tempUser.getPersonId().intValue();
						if (rPersonId == personIds[kk]) {
							hasThisUser = true;
							break;
						}
					}

					if (!hasThisUser) {
						PyTopicInforRight tpRight = new PyTopicInforRight();
						// tpExcuter.setIsReaded(0);
						tpRight.setTopicInfor(pyTopicInfor);
						SystemUserInfor tpUser = (SystemUserInfor) this.systemUserManager.get(personIds[kk]);
						tpRight.setUser(tpUser);
						rights.add(tpRight);
					}
				}
			}
		}
		
		//pyTopicInfor.setRights(rights);	
		this.pyTopicInforManager.save(pyTopicInfor);
		
		
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("window.opener.location.reload();");
		out.print("window.close();");
		out.print("</script>");

		
	}

	
	/**
	 * 批量删除评优主题
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
					PyTopicInfor topic = (PyTopicInfor) this.pyTopicInforManager.get(deleteId);
				
					this.pyTopicInforManager.remove(topic);
				}
			}
		}
		return "evaluationvote/editTopicInfor";
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
	public String viewCount(HttpServletRequest request, HttpServletResponse response, PyTopicInforVo vo, Model model) throws Exception {
		//首先将此主题下的所有条目及选项找出，列在编辑表单页面
		vo.setTopicId(Integer.valueOf(request.getParameter("rowId")));
		int topicId = vo.getTopicId();
		PyTopicInfor topicInfor = new PyTopicInfor();
		if(topicId > 0){
			topicInfor = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
		}
		request.setAttribute("_TopicInfor", topicInfor);
		
		
		//得到所有用户人数
		String userCountSql = "select count(personId) from SystemUserInfor user where user.invalidate = 0 and user.person.deleted = 0";
		int userCount = this.systemUserManager.getResultNumByQueryString(userCountSql);
		request.setAttribute("_AllUserCount", userCount);

		//找到所有的投票信息
		String voteInforSql = "from PyVoteItemInfor voteItem where voteItem.voteInfor.topicInfor.topicId="+topicId;
		List<PyVoteItemInfor> voteItemList = this.pyVoteItemInforManager.getResultByQueryString(voteInforSql);
		request.setAttribute("_VoteItemList", voteItemList);
		
		return "evaluationvote/viewCount";
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
	public String viewVoters(HttpServletRequest request, HttpServletResponse response, PyTopicInforVo vo, Model model) throws Exception {
		//首先将此主题下的所有条目及选项找出，列在编辑表单页面
		int topicId = vo.getTopicId();
		PyTopicInfor topicInfor = new PyTopicInfor();
		if(topicId > 0){
			topicInfor = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
		}
		request.setAttribute("_TopicInfor", topicInfor);
		
		
		
		
		//判断投票人是否为空，如果不为空，则查出他的投票信息
		if(vo.getVoterId() > 0){
			String voteInforSql = "from PyVoteInfor voteInfor where voteInfor.person.personId="+vo.getVoterId()+" and voteInfor.topicInfor.topicId="+topicId;
			List voteInforList = this.pyVoteInforManager.getResultByQueryString(voteInforSql);
			if(voteInforList.size() > 0){
				request.setAttribute("_VoteInfor", voteInforList.get(0));
			}
		}
		
		//查出未投票人
		//全部投票人
		List<SystemUserInfor> allVoters = new ArrayList<SystemUserInfor>();
		if(topicInfor.getOpenType() == 0){   //自定义 
			Set<PyTopicInforRight> rights = topicInfor.getRights();
			for(PyTopicInforRight right : rights){
				allVoters.add(right.getUser());
			}
		}else if(topicInfor.getOpenType() == 1){   //全体
			allVoters = this.systemUserManager.getAllValid();
		}
		
		//已投票人
		Set<PyVoteInfor> voteInfors = topicInfor.getVoteInfors();
		List<SystemUserInfor> votedUsers = new ArrayList<SystemUserInfor>();
		for(PyVoteInfor voteInfor : voteInfors){
			votedUsers.add(voteInfor.getPerson());
		}
		
		//取得未投票人
		allVoters.removeAll(votedUsers);
		request.setAttribute("_NotVoteUsers", allVoters);

		
		return "evaluationvote/viewVoters";
	}

	/**
	 * 获取需要首页显示的投票问卷信息(用于首页显示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	 
	@RequestMapping(params = "method=getPyTopics")
	public void getPyTopics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Date sysDate = new Date(System.currentTimeMillis());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Date today = new Date(System.currentTimeMillis());
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		String todayStr = sf.format(today);
		
		String queryHQL = " from PyTopicInfor topic where topic.startTime<='"+todayStr+"' and topic.endTime >='"+todayStr+"' and topic.valid is true";
		String condition = "";
		
		condition += " and (topic.openType=1 or (topic.openType=0 and topic.topicId in(select topicRight.topicInfor.topicId from PyTopicInforRight topicRight where topicRight.user.personId="+systemUser.getPersonId()+")))";
		condition += " and topic.topicId not in(select voteInfor.topicInfor.topicId from PyVoteInfor voteInfor where voteInfor.person.personId="+systemUser.getPersonId()+")";
		condition += " order by topic.endTime";
		
		queryHQL += condition;
		
		List infors = this.pyTopicInforManager.getResultByQueryString(queryHQL);
		
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
	
	/***
	 * 公示或取消首页显示
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=display")
	public void display(HttpServletRequest request, HttpServletResponse response, PyTopicInforVo vo) throws Exception {
		PyTopicInfor topicInfor = new PyTopicInfor();
		
		if (vo.getTopicId() != null && vo.getTopicId().intValue() != 0) {
			topicInfor = (PyTopicInfor) this.pyTopicInforManager.get(vo.getTopicId());
		}
		
		topicInfor.setHomepage(vo.isHomepage());
		if(vo.isHomepage()){
			Date createDate = new Date(System.currentTimeMillis());
			topicInfor.setPublicTime(createDate);
		}else {
			topicInfor.setPublicTime(null);
		}
		
		this.pyTopicInforManager.save(topicInfor);
	}
	
	/**
	 * 获取需要首页公示的投票问卷结果信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getPublicTopics")
	public void getPublicTopics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		java.util.Date sysDate = new java.util.Date(System.currentTimeMillis());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		java.util.Date today = new java.util.Date();
		//今天前的一个月内
		java.util.Date oneMonthAgo = DateHelper.addMonth(today, -1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		
		String oneMonthAgoStr = sf.format(oneMonthAgo);
		
		String queryHQL = " from PyTopicInfor topic where topic.publicTime>='"+oneMonthAgoStr+"' and  topic.homepage is true and valid is true";
		String condition = "";
		
		//condition += " and (topic.openType=1 or (topic.openType=0 and topic.topicId in(select topicRight.topic.topicId from TopicRight topicRight where topicRight.systemUser.personId="+systemUser.getPersonId()+")))";
		//condition += " and topic.topicId not in(select voteInfor.topic.topicId from VoteInfor voteInfor where voteInfor.voter.personId="+systemUser.getPersonId()+")";
		condition += " order by topic.publicTime desc";
		
		queryHQL += condition;
		
		List infors = this.pyTopicInforManager.getResultByQueryString(queryHQL);
		
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
	
}