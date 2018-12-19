package com.kwchina.core.base.controller;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.DataRightInforManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.base.vo.SystemUserInforVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.sys.Log4jHandlerAOP;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.util.CommonLinuxMethod;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping(value="/core/systemUserInfor.do")
public class SystemUserController extends BasicController{
	protected static Log log;
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private PersonInforManager personManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private DataRightInforManager dataRightInforManager;
	
	Logger logger = Logger.getLogger(SystemUserController.class);
	
	//分页显示所有系统用户
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//构造查询语句
		//String[] queryString = this.systemUserManager.generateQueryString("SystemUserInfor", "personId", getSearchParams(request));
		
		String[] queryString = new String[2];
		queryString[0] = "from SystemUserInfor user where 1=1";
		queryString[1] = "select count(personId) from SystemUserInfor user where 1=1";
		
		String isvalid = request.getParameter("isvalid");
		if (isvalid != null && isvalid.length() > 0 && ("false").equals(isvalid)) {
			queryString[0] += " and invalidate = 1";
			queryString[1] += " and invalidate = 1";
		}else {
			queryString[0] += " and invalidate = 0";
			queryString[1] += " and invalidate = 0";
		}
		
		queryString = this.systemUserManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.systemUserManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
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
		awareObject.add("person.department");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}
	
	//获取查询条件数据(用户信息)
	@RequestMapping(params="method=getUsers")
	public void getUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//用户信息
		String departmentIdStr = request.getParameter("departmentId");
		String groupIdStr = request.getParameter("groupId");
		Integer departmentId = (departmentIdStr != null && departmentIdStr.length() > 0)?Integer.valueOf(departmentIdStr):null;
		Integer groupId = (groupIdStr != null && groupIdStr.length() > 0)?Integer.valueOf(groupIdStr):null;
		JSONArray userArray = new JSONArray();
		List users = this.systemUserManager.getUserByOrganize(departmentId, groupId);
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("person");
		userArray = convert.modelCollect2JSONArray(users, awareObject);
		jsonObj.put("_Users", userArray);
		
		//部门经理
//		OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentIdStr));
//		jsonObj.put("_Director", department.getDirector());
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	//获取查询条件数据(用户信息)
	@RequestMapping(params="method=getUsersByDepartName")
	public void getUsersByDepartName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//用户信息
		String departName = request.getParameter("departName");
		JSONArray userArray = new JSONArray();
		
		List users = new ArrayList();
		OrganizeInfor department = this.organizeManager.findByOrganizeName(departName);
		if(department != null && department.getOrganizeId() != null && department.getOrganizeId().intValue() > 0){
			users = this.systemUserManager.getUserByOrganize(department.getOrganizeId(), 0);
		}
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("person");
		userArray = convert.modelCollect2JSONArray(users, awareObject);
		jsonObj.put("_Users", userArray);
		
		//部门经理
//			OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentIdStr));
//			jsonObj.put("_Director", department.getDirector());
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	//获取查询条件数据(用户信息)
	@RequestMapping(params="method=getTaskUsers")
	public void getTaskUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//用户信息
		String departmentIdStr = request.getParameter("departmentId");
		String groupIdStr = request.getParameter("groupId");
		Integer departmentId = (departmentIdStr != null && departmentIdStr.length() > 0)?Integer.valueOf(departmentIdStr):null;
		Integer groupId = (groupIdStr != null && groupIdStr.length() > 0)?Integer.valueOf(groupIdStr):null;
		JSONArray userArray = new JSONArray();
		
		//部门经理
		OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentIdStr));
		PersonInfor director = department.getDirector();
		
		List users = new ArrayList();
		//如果部门经理为空，则再查出所有
		if(director == null){
			users = this.systemUserManager.getUserByOrganize(departmentId, groupId);
		}else {
			users.add(director.getUser());
		}
		
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("person");
		userArray = convert.modelCollect2JSONArray(users, awareObject);
		jsonObj.put("_Users", userArray);
		
		
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//获取查询条件数据(用户信息)--根据部门获取此部门中特定角色的人员
	@RequestMapping(params="method=getRoleUsers")
	public void getRoleUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//用户信息
		String departmentIdStr = request.getParameter("departmentId");
		String groupIdStr = request.getParameter("groupId");
		Integer departmentId = (departmentIdStr != null && departmentIdStr.length() > 0)?Integer.valueOf(departmentIdStr):null;
		Integer groupId = (groupIdStr != null && groupIdStr.length() > 0)?Integer.valueOf(groupIdStr):null;
		JSONArray userArray = new JSONArray();
		
		//部门经理
		OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentIdStr));
		List users = new ArrayList();
		/*PersonInfor director = department.getDirector();
		
		//如果部门经理为空，则再查出所有
		if(director == null){
			users = this.systemUserManager.getUserByOrganize(departmentId, groupId);
		}else {
			users.add(director.getUser());
		}*/
		
		
		RoleInfor managerRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Manager_Above);
		if(managerRole != null && managerRole.getRoleId().intValue() > 0){
			Set<SystemUserInfor> userSet = managerRole.getUsers();
			if(userSet != null && userSet.size() > 0){
				for(SystemUserInfor tmpUser : userSet){
					if(tmpUser.getPerson().getDepartment().getOrganizeId().intValue() == department.getOrganizeId().intValue()){
						users.add(tmpUser);
					}
				}
			}
			
		}
		
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("person");
		userArray = convert.modelCollect2JSONArray(users, awareObject);
		jsonObj.put("_Users", userArray);
		
		
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	//获取用户对应的人员名(用于工作流自定义标签)
	@RequestMapping(params="method=getPersonName")
	public void getPersonName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		
		//用户对应的人员名
		String personId = request.getParameter("personId");
		String personName = "";
		if (personId != null && personId.length() > 0) {
			SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(personId));
			if (user != null) {
				personName = user.getPerson().getPersonName();
			}
		}
		jsonObj.put("_PersonName", personName);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//新增或者修改系统用户
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, SystemUserInforVo vo, Model model) throws Exception {
		
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setPersonId(Integer.valueOf(rowId));
		}else {
			int[] roleIds = new int[1];	
			roleIds[0] = 1;
			vo.setRoleIds(roleIds);
		}
		
		getUserInfor(request, response, vo, model);
		
		//所属部门
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);
		
		return "base/editUser";
	}
	
	
	private void getUserInfor(HttpServletRequest request, HttpServletResponse response, SystemUserInforVo vo, Model model) throws Exception {
		
		Integer personId = vo.getPersonId();
		//角色
		List roles = this.roleManager.getAll(); 
		request.setAttribute("_Roles", roles);
		
		if (personId != null && personId.intValue() != 0) {
			
			//判断是否有编辑权限
			this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "edit");
			
			//修改用户时
			SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(personId);
			model.addAttribute("_User", user);
			
			//BeanUtils.copyProperties(vo, systemUser);
			vo.setPersonId(user.getPersonId());
			vo.setUserName(user.getUserName());
			vo.setPassword(user.getPassword());
			vo.setUserType(user.getUserType());
						
			int[] roleIds = new int[roles.size()];	
			Set userRoles = user.getRoles();
			
			for (int i = 0; i < roles.size(); i++) {
				RoleInfor tempRole = (RoleInfor) roles.get(i);
				int tempRoleId = tempRole.getRoleId().intValue();
				
				//int k = 0;
				for (Iterator it = userRoles.iterator(); it.hasNext();) {
					RoleInfor uRole = (RoleInfor) it.next();
					int roleId = uRole.getRoleId().intValue();
					
					if (tempRoleId == roleId) {
						//该用户属于该角色
						roleIds[i] = roleId;
					}
					//k += 1;
				}
			}
			vo.setRoleIds(roleIds);
		}
		
		//人员
		List persons = this.personManager.getPersonOrderByName();
		//按照personNo排序
		//Collections.sort(persons, new BeanComparator("personNo"));
		request.setAttribute("_Persons",persons);

	}
	
	
	//保存系统用户
	@RequestMapping(params="method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, SystemUserInforVo vo) throws Exception {

		Integer personId = vo.getPersonId();
		SystemUserInfor systemUser = new SystemUserInfor();
		
		if (personId != null && personId.intValue() != 0) {
			PersonInfor person = (PersonInfor)personManager.get(personId);
			if(person!=null){
				systemUser = person.getUser();
			}	
			if(systemUser==null) {
				systemUser = new SystemUserInfor();
				systemUser.setPerson(person);
				systemUser.setInvalidate(false);
			}
		}
		
		systemUser.setUserName(vo.getUserName());
		systemUser.setPassword(vo.getPassword());
		systemUser.setUserType(vo.getUserType());
		
		/** 获得用户对应的角色 */
		int[] roleIds = vo.getRoleIds();		
		List allRoles = this.roleManager.getAll();
		//保存
		if (systemUser.getPersonId() == null ) {
			if (roleIds != null && roleIds.length > 0) {
				for (int k = 0; k < roleIds.length; k++) {
					RoleInfor role = (RoleInfor)this.roleManager.get(roleIds[k]);
					Set userSet = role.getUsers();
					userSet.add(systemUser);
						
					role.setUsers(userSet);
					this.roleManager.save(role);
				}
			}else {
				/**
				//添加Linux用户
				String shellComm[]={"/bin/sh","/opt/oa/check.sh",vo.getUserName()};
				String result = CommonLinuxMethod.runShell(shellComm);
				if(result!=null && result.equals("000")){
					//表示已经有该用户
					
				}else{
					//写入用户名密码到/opt/oa/newuser
					FileWriter fw = new FileWriter("/opt/oa/newuser"); 
					PrintWriter out=new PrintWriter(fw); 
					out.write(vo.getUserName()+":"+vo.getPassword()+"\n");					
					out.flush(); 
					fw.close(); 
					out.close();
					
					//创建Linux用户
					Runtime rt=Runtime.getRuntime();
					String str[]={"/bin/sh","/opt/oa/useradd.sh"};
					Process pcs=rt.exec(str);
					try{
						pcs.waitFor();
						pcs.exitValue();
					} catch(InterruptedException e){
						System.err.println("processes was interrupted");
					}		
				}*/

				//this.systemUserManager.saveUser(systemUser);
//				this.systemUserManager.save(systemUser);
				if(systemUser.getUserName().equals("admin")){

					SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //这里参数是你要获取的时间字段
					Date date1 = new Date();       //这里参数是你的String类型时间
					String date = sim.format(date1);  //这里是转换的最终方法，参数是date类型
					log.info("保存系统用户method=save---客户端ip:"+request.getRemoteAddr()+"---时间:"+ date);

				}
				this.systemUserManager.save(systemUser);
			}
				
		}else {
			Set roleSet = systemUser.getRoles();	//原来的角色集
				
			for (Iterator r=allRoles.iterator();r.hasNext();) {
				RoleInfor tmpRole = (RoleInfor)r.next();
				Set userSet = tmpRole.getUsers();
					
				boolean yetHave = false;
				boolean nowHave = false;
					
				if (roleSet != null && roleSet.size() != 0) {
					for (Iterator it=roleSet.iterator();it.hasNext();) {
						RoleInfor role = (RoleInfor)it.next();
							
						if (role.getRoleId().intValue() == tmpRole.getRoleId().intValue()) {
							yetHave = true;
						}
					}
				}
					
				if (roleIds != null && roleIds.length != 0) {
					for (int k = 0; k < roleIds.length; k++) {
						RoleInfor role = (RoleInfor)this.roleManager.get(roleIds[k]);
							
						if (role.getRoleId().intValue() == tmpRole.getRoleId().intValue()) {
							nowHave = true;
						}
					}
				}					
					
				if (yetHave && !nowHave) {
					userSet.remove(systemUser);
				}
					
				if (!yetHave && nowHave) {
					userSet.add(systemUser);
				}
					
				tmpRole.setUsers(userSet);
				this.roleManager.save(tmpRole);
			}
			
			/**
			//更改linux用户密码
			//第一步：拷贝/opt/oa/user/userpass到/opt/oa/user/$user
			Runtime rt=Runtime.getRuntime();
			String tempUserFile = "temp_" + systemUser.getUserName();
			String[] str={"/bin/sh","-c","cp /opt/oa/user/userpass /opt/oa/user/"+tempUserFile};
			Process pcs=rt.exec(str);
			try{
				pcs.waitFor();
				pcs.exitValue();
			} catch(InterruptedException e){
				System.err.println("processes was interrupted");
			}		
			
			//第二步：用户密码写入该文件
			FileWriter fw = new FileWriter("/opt/oa/user/"+tempUserFile); 
			PrintWriter out=new PrintWriter(fw); 
			out.write(systemUser.getUserName()+":"+vo.getPassword()+"\n");					
			out.flush(); 
			fw.close(); 
			out.close();
			
			//第三步:更改密码
			String[] commnds={"/bin/sh","/opt/oa/updatepass.sh",systemUser.getUserName()};
			pcs=rt.exec(commnds);
			try{
				pcs.waitFor();
				pcs.exitValue();
			} catch(InterruptedException e){
				System.err.println("processes was interrupted");
			}	
			*/			
		}
		return "success";
	}
	
	//验证用户名重复
	/*@RequestMapping(params="method=validate", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, ? extends Object> create(@RequestBody Account account, HttpServletResponse response) { 
	    Set<ConstraintViolation<Account>> failures = validator.validate(account);  

	    if (!failures.isEmpty()) {
	         response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  
	         return validationMessages(failures);  
		}else {
		     accounts.put(account.assignId(), account);  
		     return Collections.singletonMap("id", account.getId());  
		}
	 }*/
	
	//验证用户名重复
	@RequestMapping(params="method=validate")
	@ResponseBody
	public Map<String, Object> validate(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userName = request.getParameter("userName");
		String personId = request.getParameter("personId");
		
		//若用户名重复则返回标识符信息到页面
		SystemUserInfor systemUser = this.systemUserManager.findSystemUserByName(userName);
		if (systemUser != null) {
			if (personId != null && personId.length() > 0) {
				Integer personIdInt = Integer.valueOf(personId);
				if (personIdInt == systemUser.getPersonId().intValue()) {
					map.put("isDuplicate", false);
				}else {
					map.put("isDuplicate", true);
				}
			}else {
				map.put("isDuplicate", true);
			}
			
		}else {
			map.put("isDuplicate", false);
		}
		
		return map;
	}

	
	//员工自行修改密码
	@RequestMapping(params="method=editPassword")
	public String editPassword(HttpServletRequest request, HttpServletResponse response, SystemUserInforVo vo) throws Exception {

		String sessionUser = SystemConstant.Session_SystemUser;
		SystemUserInfor systemUser = (SystemUserInfor) request.getSession().getAttribute(sessionUser);
		
		int personId = systemUser.getPersonId();
		SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(personId);
		request.setAttribute("_User", user);
		
		BeanUtils.copyProperties(vo, systemUser);	
		
		return "base/editPassword";
	}
	
	//验证原始密码是否正确
	@RequestMapping(params="method=validateOldPas")
	@ResponseBody
	public Map<String, Object> validateOldPas(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String oldPassword = request.getParameter("oldPassword");
		
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		if(!oldPassword.equals(systemUser.getPassword())){
			map.put("isRight", false);
		}else{
			map.put("isRight", true);
		}
		
		return map;
	}
	
	
	//保存密码
	@RequestMapping(params="method=savePassword")
	public String savePassword(HttpServletRequest request, HttpServletResponse response, SystemUserInforVo vo) throws Exception {
		
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		//判断输入的旧密码是否正确
		/*String oldPassword = vo.getOldPassword();
		if(!oldPassword.equals(systemUser.getPassword())){
			request.setAttribute("_Message", "旧密码不正确,未修改成功!");
		}else{*/
			systemUser.setPassword(vo.getPassword());

		if(systemUser.getUserName().equals("admin")){
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //这里参数是你要获取的时间字段
			Date date1 = new Date();       //这里参数是你的String类型时间
			String date = sim.format(date1);  //这里是转换的最终方法，参数是date类型
			log.info("保存密码 更改admin密码---客户端ip:"+request.getRemoteAddr()+"---时间:"+ date);

		}
		this.systemUserManager.save(systemUser);

			
			/*request.setAttribute("_Message", "密码已经成功修改!");
		}	*/	
		
		return editPassword(request, response, vo);
	}
	
	
	//删除用户信息
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i=0;i<detleteIds.length;i++) {
					Integer personId = Integer.valueOf(detleteIds[i]);
					
					//判断是否有删除权限
					this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "delete");
					
					SystemUserInfor systemUser = (SystemUserInfor)this.systemUserManager.get(personId);
					String userName = systemUser.getUserName();
					
					this.systemUserManager.remove(personId);
										
					/**
					//删除Linux用户
					Runtime rt=Runtime.getRuntime();
					String str[]= new String[] {"/bin/sh","-c","userdel -r " + userName};
					Process pcs=rt.exec(str);
					try{
						pcs.waitFor();
						pcs.exitValue();
					} catch(InterruptedException e){
						System.err.println("processes was interrupted");
					}*/	
				}
			}
		}
	}
	
	//注销/恢复用户
	@RequestMapping(params="method=cancelOrResume")
	public void cancelOrResume(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String operationId = request.getParameter("rowId");
		if (operationId != null && operationId.length() > 0) {
			Integer personId = Integer.valueOf(operationId);
			
			//判断是否有注销或恢复权限
			this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "cancelOrResume");
			
			SystemUserInfor systemUser = (SystemUserInfor)this.systemUserManager.get(personId);
			if(systemUser.isInvalidate()){
				systemUser.setInvalidate(false);			
			}else{
				systemUser.setInvalidate(true);
			}
			logger.info(new Timestamp((System.currentTimeMillis())) + "时，controller中，用户【"+systemUser.getUserName()+"】 状态被更改成："+ String.valueOf(systemUser.isInvalidate()));
			if(systemUser.getUserName().equals("admin")){
				SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //这里参数是你要获取的时间字段
				Date date1 = new Date();       //这里参数是你的String类型时间
				String date = sim.format(date1);  //这里是转换的最终方法，参数是date类型
				log.info("注销恢复用户---客户端ip:"+request.getRemoteAddr()+"---时间:"+ date);

			}
			this.systemUserManager.save(systemUser);

		}
	}
}
