package com.kwchina.oa.sys;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import com.kwchina.core.base.entity.*;
import com.kwchina.core.base.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.actions.DispatchAction;
import org.jasig.cas.client.validation.Assertion;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforCategoryRightManager;
import com.kwchina.core.cms.util.InforConstant;
import com.kwchina.core.config.entity.ConfigIndexFun;
import com.kwchina.core.config.service.ConfigIndexFunManager;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.domain.util.AdValid;
import com.kwchina.extend.domain.util.CasService;
import com.kwchina.extend.loginLog.entity.LoginLog;
import com.kwchina.extend.loginLog.service.LoginLogManager;
import com.kwchina.oa.meeting.service.MeetInforManager;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;

@Controller
public class LoginConfirm extends DispatchAction {

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private PersonInforManager personInforManager;

	@Resource
	private InforCategoryManager inforCategoryManager;

	@Resource
	private InforCategoryRightManager inforCategoryRightManager;

	@Resource
	private FlowDefinitionManager flowManager;

	@Resource
	private ConfigIndexFunManager configIndexFunManager;

	@Resource
	private RoleManager roleManager;

	@Resource
	private FlowInstanceManager flowInstanceManager;

	@Resource
	private AppImgManager appImgManager;

	@Resource
	private MeetInforManager meetInforManager;

	@Resource
	private LoginLogManager loginLogManager;

	@Resource
	private PersonModulesManager personModulesManager;
	@RequestMapping("/login.do")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		HttpSession session = request.getSession();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

//		System.out.println();
		if(systemUser == null) {
			//如果是第一次登陆系统			
			if(userName != null && userName.length() > 0 || password != null && password.length() > 0){
				try{
					systemUser = systemUserManager.checkUser(userName, password);
				}catch (ServiceException ex){
					request.setAttribute("_Action_ErrorMessage", ex.getMessage());
					return "login";
				}
			}
		}

		if(systemUser == null || systemUser.isInvalidate()) {
			session.setAttribute("_From_Login", "this");
			return "login";
		}else{
			try{
				PersonModules personModules = personModulesManager.selectByPerson(systemUser.getPersonId());
				if(personModules==null){
					personModules = new PersonModules("待办事宜","FrameDaiban.jsp","taskUl","公告栏","FrameAnnouce.jsp","messageUl","海通简报","FrameMsg.jsp?urlPath=htreports","htreportsUl","管理工作","FrameMsg.jsp?urlPath=managework"
							,"manageworkUl", "党群园地","FrameMsg.jsp?urlPath=partygarden","partygardenUl","市场信息及研究","FrameMsg.jsp?urlPath=marketinfo","marketinfoUl","#e1dd21","#b682ff","#ff4955","#22ffae","#ff6cc0","#ffaf78");
				}else {
					if(personModules.getColor1()==null){
						personModules.setColor1("#e1dd21");
						personModules.setColor2("#b682ff");
						personModules.setColor3("#ff4955");
						personModules.setColor4("#22ffae");
						personModules.setColor5("#ff6cc0");
						personModules.setColor6("#ffaf78");
					}
				}
				request.setAttribute("_PersonModules",personModules);
				//并检查是否选中有“全体”角色，若没有，则加上
				RoleInfor role = new RoleInfor();
				role = (RoleInfor)roleManager.get(1);

				boolean hasAll = roleManager.belongRole(systemUser, role);
				if(!hasAll){
					Set userSet = role.getUsers();
					userSet.add(systemUser);
					role.setUsers(userSet);
					roleManager.save(role);
				}




				request.getSession().setAttribute("_SYSTEM_USER", systemUser);
				PersonInfor person = (PersonInfor)this.personInforManager.get(systemUser.getPersonId());
				request.getSession().setAttribute("_GLOBAL_PERSON",person);

				//是否是普通用户（或者管理员），与“投票用户”及“非合同制用工”区分
				boolean isNormalUser = false;
				if(systemUser.getUserType() == 0 || systemUser.getUserType() == 1){
					isNormalUser = true;
				}
				request.getSession().setAttribute("_NORMAL_USER",isNormalUser);

				//投票用户
				boolean isTPUser = false;
				if(systemUser.getUserType() == 2){
					isTPUser = true;
				}
				request.getSession().setAttribute("_TP_USER",isTPUser);


				//非合同制用工用户
				boolean isTmpUser = false;
				if(systemUser.getUserType() == 3){
					isTmpUser = true;
				}
				request.getSession().setAttribute("_TMP_USER",isTmpUser);

				//添加判断是否为投票问卷操作人
				RoleInfor topicRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Topic_Operater);
				boolean isTopicOpertor = this.roleManager.belongRole(systemUser, topicRole);
				request.getSession().setAttribute("_TOPIC_OPERATOR", isTopicOpertor);

				//添加判断是否为论坛操作人
				RoleInfor bbsRole = (RoleInfor)this.roleManager.get(SystemConstant._User_Type_Admin_BBS);
				boolean isBbsOpertor = this.roleManager.belongRole(systemUser, bbsRole);
				request.getSession().setAttribute("_BBS_OPERATOR", isBbsOpertor);

				//添加判断是否为论坛操作人
				RoleInfor contractRole = (RoleInfor)this.roleManager.get(26);
				boolean isContractRole = this.roleManager.belongRole(systemUser, contractRole);
				request.getSession().setAttribute("_IsContractRole", isContractRole);
				/**
				 userName = systemUser.getUserName();
				 WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
				 if (mailSession==null)
				 this.newUserSession(request, userName,false);
				 //if(this.newUserSession(request, userName))
				 //CoreConstant.Mail_Login_Sucess = true;

				 mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
				 if(mailSession != null){
				 //获取第一页的邮件显示在首页
				 HTTPRequestHeader http_header = this.getRequestHeader(request);
				 MailListService.getFolderMailInfor(request,"",http_header);

				 CurrentInfor folder  = MailListService.getCurrentFolder(request);
				 String folderId = folder.getId();

				 MailFolder mailFolder = MailListService.getFolderInfor(request,folderId);
				 request.setAttribute("_INBOX_FOLDER", mailFolder);

				 //登录这名字放入session
				 if(request.getSession().getAttribute("_Mail_User_Name")==null){
				 String loginName = mailSession.getUserName();
				 if(loginName.indexOf("@")>0){
				 loginName = loginName.substring(0, loginName.indexOf("@"));
				 }
				 request.getSession().setAttribute("_Mail_User_Name", loginName);
				 }
				 }
				 */
				/*List l = this.flowInstanceManager.getAll();
				for(int i=0;i<l.size();i++){
					FlowInstanceInfor f = (FlowInstanceInfor)l.get(i);
					f.setHandOut("0");
					this.flowInstanceManager.save(f);
				}*/
				//return "index";
				leftMenuInfor(request,response);
				request.setAttribute("test",true);
				mainInfor(request,response);
				return "homepage";
				//return "maillogin";
			}catch(Exception ex){
				ex.printStackTrace();
				return "login";
			}
		}
	}

	/**
	 * 登录域的用户自动登录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/autoLogin.do")
	public String autoLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
//		String userName = request.getParameter("userName").toLowerCase().trim();
		String userName = "";
		boolean canLogin = false;
		HttpSession session = request.getSession();

		Object object = request.getSession().getAttribute("_const_cas_assertion_");
		if (object != null) {
			Assertion assertion = (Assertion) object;
			userName = assertion.getPrincipal().getName();

			//System.out.println("===当前登录用户：" + userName);
		}

		//根据域用户名寻找OA用户
		SystemUserInfor userInfor = this.systemUserManager.findSystemUserByName(userName);
		if(userInfor == null) {
//			request.setAttribute("_Action_ErrorMessage", "您当前登录windows的帐户尚未对应OA用户，请联系管理员！");
			request.setAttribute("_Action_ErrorMessage", "您尚未开通OA用户，请联系管理员！");
			return "login";
		}else if(userInfor.isInvalidate()){
			session.setAttribute("_From_Login", "this");
			request.setAttribute("_Action_ErrorMessage", "您的OA账户已被注销，请联系管理员！");
			return "login";
		}else{
			try{
				PersonModules personModules = personModulesManager.selectByPerson(userInfor.getPersonId());
				if(personModules==null){
					personModules = new PersonModules("待办事宜","FrameDaiban.jsp","taskUl","公告栏","FrameAnnouce.jsp","messageUl","海通简报","FrameMsg.jsp?urlPath=htreports","htreportsUl","管理工作","FrameMsg.jsp?urlPath=managework"
							,"manageworkUl", "党群园地","FrameMsg.jsp?urlPath=partygarden","partygardenUl","市场信息及研究","FrameMsg.jsp?urlPath=marketinfo","marketinfoUl","#e1dd21","#b682ff","#ff4955","#22ffae","#ff6cc0","#ffaf78");
				}else {
					if(personModules.getColor1()==null){
						personModules.setColor1("#e1dd21");
						personModules.setColor2("#b682ff");
						personModules.setColor3("#ff4955");
						personModules.setColor4("#22ffae");
						personModules.setColor5("#ff6cc0");
						personModules.setColor6("#ffaf78");
					}
				}
				request.setAttribute("_PersonModules",personModules);
				request.getSession().setAttribute("_SYSTEM_USER", userInfor);
				PersonInfor person = (PersonInfor)this.personInforManager.get(userInfor.getPersonId());
				request.getSession().setAttribute("_GLOBAL_PERSON",person);

				//添加判断是否为投票问卷操作人
				RoleInfor topicRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Topic_Operater);
				boolean isTopicOpertor = this.roleManager.belongRole(userInfor, topicRole);
				request.getSession().setAttribute("_TOPIC_OPERATOR", isTopicOpertor);

				//添加判断是否为论坛操作人
				RoleInfor bbsRole = (RoleInfor)this.roleManager.get(SystemConstant._User_Type_Admin_BBS);
				boolean isBbsOpertor = this.roleManager.belongRole(userInfor, bbsRole);
				request.getSession().setAttribute("_BBS_OPERATOR", isBbsOpertor);

				//添加判断是否为论坛操作人
				RoleInfor contractRole = (RoleInfor)this.roleManager.get(26);
				boolean isContractRole = this.roleManager.belongRole(userInfor, contractRole);
				request.getSession().setAttribute("_IsContractRole", isContractRole);

				//return "index";
				leftMenuInfor(request,response);
				mainInfor(request,response);
				return "homepage";
				//return "maillogin";
			}catch(Exception ex){
				ex.printStackTrace();
				return "login";
			}
		}
		//return "";
	}

	/**
	 * 使用域用户名及密码登录OA
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ldapLogin.do")
	public String ldapLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		HttpSession session = request.getSession();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		if(systemUser == null) {
			//如果是第一次登陆系统

			//使用填入的域用户名及密码判断
			if(userName != null && userName.length() > 0 || password != null && password.length() > 0){
				/*UserAuthenticate auth = new UserAuthenticate();
				boolean isLdapUser = auth.userLogin(userName+"@haitongauto.com", password);

				if(isLdapUser){
					//根据域用户名寻找OA用户
					systemUser = this.systemUserManager.findSystemUserByName(userName);

					//将此用户的OA密码更改为输入的正确的域密码
					systemUser.setPassword(password);
					this.systemUserManager.save(systemUser);
				}else {
					request.setAttribute("_Action_ErrorMessage", "请输入正确和域用户名及域密码进行登录！");
					return "login";
				}*/

				try {
					Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
					Store st = mailSession.getStore("imap");

					// 获取该用户的总邮件，最新邮件数
					// 服务器,用户名,密码
					ServletContext context = request.getSession().getServletContext();
//				String mailHost = context.getInitParameter("mailserverIP");
					String mailHost = "mail.haitongauto.com/owa/";

					StringBuffer requestURL = request.getRequestURL();
					if (requestURL != null && requestURL.length() > 0) {
						String mailserverIP = context.getInitParameter("mailserverIP");
						if (requestURL.indexOf(mailserverIP) > -1) {
							mailHost = mailserverIP;
						}
					}

					String mailUser = "";

					String email = userName+"@haitongauto.com";
					if (email != null && !email.equals("")) {
						// int pos = email.indexOf("@");
						// if(pos>0)
						// username = email.substring(0, pos);
						mailUser = email;
					}
					String mailPassword = password;

					if (!(mailHost == null || mailHost.equals("") || mailUser == null || mailUser.equals("") || mailPassword == null || mailPassword
							.equals(""))) {
						st.connect(mailHost, mailUser, mailPassword);

						Folder rootFolder = st.getDefaultFolder();
						rootFolder.setSubscribed(true);

						/**
						 * 对于Domino邮件服务器，其Folder结构为getDefaultFolder-->(INBOX,Trash等)
						 * 对于其它，其Folder结构为getDefaultFolder-->INBOX-->Trash等
						 */
						Folder inbox = rootFolder.getFolder("INBOX");
						if (inbox != null) {
							int total_messages = inbox.getMessageCount();
							int new_messages = inbox.getUnreadMessageCount();

							if ((total_messages == -1 || new_messages == -1) && !inbox.isOpen()) {
								// 先open
								inbox.open(Folder.READ_ONLY);

								total_messages = inbox.getMessageCount();
								new_messages = inbox.getUnreadMessageCount();
							}

							if (inbox.isOpen())
								inbox.close(false);

							// request.setAttribute("_TOTAL_MESSAGES",
							// total_messages);
							// request.setAttribute("_NEW_MESSAGES", new_messages);

							//String mailInfor = "邮件(" + total_messages + ")";
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("_Mail_All", total_messages);
							jsonObj.put("_Mail_New",new_messages);

							//设置字符编码
							response.setContentType(CoreConstant.CONTENT_TYPE);
							response.getWriter().print(jsonObj);
						}
					}

					st.close();
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if(systemUser == null || systemUser.isInvalidate()) {
			session.setAttribute("_From_Login", "this");
			return "login";
		}else{
			try{
				request.getSession().setAttribute("_SYSTEM_USER", systemUser);
				PersonInfor person = (PersonInfor)this.personInforManager.get(systemUser.getPersonId());
				request.getSession().setAttribute("_GLOBAL_PERSON",person);

				//添加判断是否为投票问卷操作人
				RoleInfor topicRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Topic_Operater);
				boolean isTopicOpertor = this.roleManager.belongRole(systemUser, topicRole);
				request.getSession().setAttribute("_TOPIC_OPERATOR", isTopicOpertor);

				//添加判断是否为论坛操作人
				RoleInfor bbsRole = (RoleInfor)this.roleManager.get(SystemConstant._User_Type_Admin_BBS);
				boolean isBbsOpertor = this.roleManager.belongRole(systemUser, bbsRole);
				request.getSession().setAttribute("_BBS_OPERATOR", isBbsOpertor);

				//添加判断是否为论坛操作人
				RoleInfor contractRole = (RoleInfor)this.roleManager.get(26);
				boolean isContractRole = this.roleManager.belongRole(systemUser, contractRole);
				request.getSession().setAttribute("_IsContractRole", isContractRole);
				leftMenuInfor(request,response);
				mainInfor(request,response);
				return "homepage";
				//return "maillogin";
			}catch(Exception ex){
				ex.printStackTrace();
				return "login";
			}
		}
	}

	/**
	 * 使用CAS认证接口登录OA
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/casLogin.do")
	public String casLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		HttpSession session = request.getSession();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		if(systemUser == null) {
			//如果是第一次登陆系统

			//使用填入的域用户名及密码判断
			if(userName != null && userName.length() > 0 || password != null && password.length() > 0){
				/*UserAuthenticate auth = new UserAuthenticate();
				boolean isLdapUser = auth.userLogin(userName+"@haitongauto.com", password);

				if(isLdapUser){
					//根据域用户名寻找OA用户
					systemUser = this.systemUserManager.findSystemUserByName(userName);

					//将此用户的OA密码更改为输入的正确的域密码
					systemUser.setPassword(password);
					this.systemUserManager.save(systemUser);
				}else {
					request.setAttribute("_Action_ErrorMessage", "请输入正确和域用户名及域密码进行登录！");
					return "login";
				}*/

				//admin除外
				if(userName.equals("admin")){
					systemUser = systemUserManager.checkUser(userName, password);
				}else {
					//CAS服务器IP及访问端口
					ServletContext context = request.getSession().getServletContext();
					String ip = context.getInitParameter("casserver");
					StringBuffer requestURL = request.getRequestURL();
					if (requestURL != null && requestURL.length() > 0) {
						String casserverIP = context.getInitParameter("casserverIP");
						if (requestURL.indexOf(casserverIP) > -1 || requestURL.indexOf("192.168.1.123") > -1 || requestURL.indexOf("127.0.0.1") > -1) {
							ip = casserverIP;
						}
					}
					String port = context.getInitParameter("casport");

					/************cas认证接口**************/
					String server = "http://"+ip+":"+port+"/cas/v1/tickets";
					String service = "http://localhost";
					//用户验证获取票信息
					String ticket = CasService.getTicket(server, userName, password, service);

					if(ticket != null && !ticket.equals("")){
						systemUser = this.systemUserManager.findSystemUserByName(userName);
					}else {
						request.setAttribute("_Action_ErrorMessage", "请输入正确和域用户名及域密码进行登录！");
						return "login";
					}
				}
			}
		}

		if(systemUser == null || systemUser.isInvalidate()) {
			session.setAttribute("_From_Login", "this");
			return "login";
		}else{
			try{
				//将此用户的OA密码更改为输入的正确的域密码
				if(!systemUser.getPassword().equals(password) && systemUser.getUserName().equals(userName)){
					systemUser.setPassword(password);
					if(!systemUser.getUserName().equals("admin")){
						SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //这里参数是你要获取的时间字段
						Date date1 = new Date();       //这里参数是你的String类型时间
						String date = sim.format(date1);  //这里是转换的最终方法，参数是date类型
						log.info("/casLogin.do 更改admin密码---客户端ip:"+request.getRemoteAddr()+"---时间:"+ date);
						this.systemUserManager.save(systemUser);
					}
				}

				//并检查是否选中有“全体”角色，若没有，则加上
				RoleInfor role = new RoleInfor();
				role = (RoleInfor)roleManager.get(1);
				boolean hasAll = roleManager.belongRole(systemUser, role);
				if(!hasAll){
					Set userSet = role.getUsers();
					userSet.add(systemUser);
					role.setUsers(userSet);
					roleManager.save(role);
				}



				request.getSession().setAttribute("_SYSTEM_USER", systemUser);
				PersonInfor person = (PersonInfor)this.personInforManager.get(systemUser.getPersonId());
				request.getSession().setAttribute("_GLOBAL_PERSON",person);

				//是否是普通用户（或者管理员），与“投票用户”及“非合同制用工”区分
				boolean isNormalUser = false;
				if(systemUser.getUserType() == 0 || systemUser.getUserType() == 1){
					isNormalUser = true;
				}
				request.getSession().setAttribute("_NORMAL_USER",isNormalUser);

				//投票用户
				boolean isTPUser = false;
				if(systemUser.getUserType() == 2){
					isTPUser = true;
				}
				request.getSession().setAttribute("_TP_USER",isTPUser);


				//非合同制用工用户
				boolean isTmpUser = false;
				if(systemUser.getUserType() == 3){
					isTmpUser = true;
				}
				request.getSession().setAttribute("_TMP_USER",isTmpUser);

				//添加判断是否为投票问卷操作人
				RoleInfor topicRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Topic_Operater);
				boolean isTopicOpertor = this.roleManager.belongRole(systemUser, topicRole);
				request.getSession().setAttribute("_TOPIC_OPERATOR", isTopicOpertor);

				//添加判断是否为论坛操作人
				RoleInfor bbsRole = (RoleInfor)this.roleManager.get(SystemConstant._User_Type_Admin_BBS);
				boolean isBbsOpertor = this.roleManager.belongRole(systemUser, bbsRole);
				request.getSession().setAttribute("_BBS_OPERATOR", isBbsOpertor);


				//添加判断是否为论坛操作人
				RoleInfor contractRole = (RoleInfor)this.roleManager.get(26);
				boolean isContractRole = this.roleManager.belongRole(systemUser, contractRole);
				request.getSession().setAttribute("_IsContractRole", isContractRole);
				leftMenuInfor(request,response);
				mainInfor(request,response);
				return "homepage";
				//return "maillogin";
			}catch(Exception ex){
				ex.printStackTrace();
				return "login";
			}
		}
	}



	/**
	 * 验证域用户登录OA
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/adLogin.do")
	public String adLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		HttpSession session = request.getSession();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		if(userName!=null){
			userName = userName.toLowerCase();
		}
		//记录登录日志
		LoginLog loginLog = new LoginLog();
		loginLog.setLogFrom("pc");
		loginLog.setLogTime(new Timestamp(System.currentTimeMillis()));
		loginLog.setUserName(userName);

		if(systemUser == null) {
			//如果是第一次登陆系统
			//使用填入的域用户名及密码判断
			if(userName != null && userName.length() > 0 && password != null && password.length() > 0){

				try {
					//admin除外
//					if(userName.equals("admin")){
						systemUser = systemUserManager.checkUser(userName, password);
//					}else {
//						boolean isDomainUser = AdValid.connect(userName, password);
//
//						if(isDomainUser){
//							systemUser = this.systemUserManager.findSystemUserByName(userName);
//						}else {
//							//如果无法连接与用户，则可能
//							//如果无法连接验证域用户，则可能为“投票用户”或“非合同制用工”，此时判断本地登录
//							systemUser = systemUserManager.checkUser(userName, password);
////						request.setAttribute("_Action_ErrorMessage", "请输入正确和域用户名及域密码进行登录！");
////						return "login";
//						}
//					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					loginLog.setSucTag(0);
					System.out.println("AD密码不正确！");
				}

			}
		}

		if(systemUser == null || systemUser.isInvalidate()) {


			loginLog.setSucTag(0);

			if(StringUtil.isNotEmpty(userName)){
				this.loginLogManager.save(loginLog);
			}

			session.setAttribute("_From_Login", "this");
			request.setAttribute("_Action_ErrorMessage", "请输入正确和用户名及密码进行登录！");
			return "login";
		}else{
			try{
				//将此用户的OA密码更改为输入的正确的域密码
				if(!systemUser.getPassword().equals(password)){
					systemUser.setPassword(password);
					if(!systemUser.getUserName().equals("admin")){
							this.systemUserManager.save(systemUser);
					}else {
						SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //这里参数是你要获取的时间字段
						Date date1 = new Date();       //这里参数是你的String类型时间
						String date = sim.format(date1);  //这里是转换的最终方法，参数是date类型
						log.info("/adLogin.do 更改admin密码---客户端ip:"+request.getRemoteAddr()+"---时间:"+ date);
					}
				}

				//并检查是否选中有“全体”角色，若没有，则加上
				RoleInfor role = new RoleInfor();
				role = (RoleInfor)roleManager.get(1);
				boolean hasAll = roleManager.belongRole(systemUser, role);
				if(!hasAll){
					Set userSet = role.getUsers();
					userSet.add(systemUser);
					role.setUsers(userSet);
					roleManager.save(role);
				}



				request.getSession().setAttribute("_SYSTEM_USER", systemUser);
				PersonInfor person = (PersonInfor)this.personInforManager.get(systemUser.getPersonId());
				request.getSession().setAttribute("_GLOBAL_PERSON",person);

				//是否是普通用户（或者管理员），与“投票用户”及“非合同制用工”区分
				boolean isNormalUser = false;
				if(systemUser.getUserType() == 0 || systemUser.getUserType() == 1){
					isNormalUser = true;
				}
				request.getSession().setAttribute("_NORMAL_USER",isNormalUser);

				//投票用户
				boolean isTPUser = false;
				if(systemUser.getUserType() == 2){
					isTPUser = true;
				}
				request.getSession().setAttribute("_TP_USER",isTPUser);


				//非合同制用工用户
				boolean isTmpUser = false;
				if(systemUser.getUserType() == 3){
					isTmpUser = true;
				}
				request.getSession().setAttribute("_TMP_USER",isTmpUser);

				//添加判断是否为投票问卷操作人
				RoleInfor topicRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Topic_Operater);
				boolean isTopicOpertor = this.roleManager.belongRole(systemUser, topicRole);
				request.getSession().setAttribute("_TOPIC_OPERATOR", isTopicOpertor);

				//添加判断是否为论坛操作人
				RoleInfor bbsRole = (RoleInfor)this.roleManager.get(SystemConstant._User_Type_Admin_BBS);
				boolean isBbsOpertor = this.roleManager.belongRole(systemUser, bbsRole);
				request.getSession().setAttribute("_BBS_OPERATOR", isBbsOpertor);

				//添加判断是否为论坛操作人
				RoleInfor contractRole = (RoleInfor)this.roleManager.get(26);
				boolean isContractRole = this.roleManager.belongRole(systemUser, contractRole);
				request.getSession().setAttribute("_IsContractRole", isContractRole);


				loginLog.setSucTag(1);
				this.loginLogManager.save(loginLog);

				//会议个数
				int meetingCount = 0;
				String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				List meetList = this.meetInforManager.getDayMeets(todayStr);
				meetingCount = meetList.size();
				request.setAttribute("_MEETING_COUNT", meetingCount);

//				return "redirect:/leftMenuInfor ";
				PersonModules personModules = personModulesManager.selectByPerson(systemUser.getPersonId());
				if(personModules==null){
					personModules = new PersonModules("待办事宜","FrameDaiban.jsp","taskUl","公告栏","FrameAnnouce.jsp","messageUl","海通简报","FrameMsg.jsp?urlPath=htreports","htreportsUl","管理工作","FrameMsg.jsp?urlPath=managework"
							,"manageworkUl", "党群园地","FrameMsg.jsp?urlPath=partygarden","partygardenUl","市场信息及研究","FrameMsg.jsp?urlPath=marketinfo","marketinfoUl","#e1dd21","#b682ff","#ff4955","#22ffae","#ff6cc0","#ffaf78");
				}else {
					if(personModules.getColor1()==null){
						personModules.setColor1("#e1dd21");
						personModules.setColor2("#b682ff");
						personModules.setColor3("#ff4955");
						personModules.setColor4("#22ffae");
						personModules.setColor5("#ff6cc0");
						personModules.setColor6("#ffaf78");
					}
				}
				request.setAttribute("_PersonModules",personModules);
				leftMenuInfor(request,response);
				mainInfor(request,response);
				return "homepage";
				//return "maillogin";
			}catch(Exception ex){
				loginLog.setSucTag(0);
				this.loginLogManager.save(loginLog);
				ex.printStackTrace();
				return "login";
			}
		}
	}


	/**
	 * 手机版登录入口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/login_m.do")
	public void login_m(HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
//
//		//部门信息
//		List departments = this.organizeManager.getDepartments();
//		departmentArray = convert.modelCollect2JSONArray(departments, new ArrayList());
//		jsonObj.put("_Departments", departmentArray);

		LoginLog loginLog = new LoginLog();
		try {
			String userName = request.getParameter("userName").toLowerCase();
			String password = request.getParameter("password");
			//参数：客户端带入的平台
			String platform = request.getParameter("platform");
			request.getSession().setAttribute(SystemConstant.Session_Platform, platform);

			//记录登录日志
			loginLog.setLogFrom("app");
			loginLog.setLogTime(new Timestamp(System.currentTimeMillis()));
			loginLog.setUserName(userName);

			SystemUserInfor systemUser = new SystemUserInfor();
			PersonInfor person = new PersonInfor();

			//如果是第一次登陆系统
			if(userName != null && userName.length() > 0 && password != null && password.length() > 0){
				//使用填入的域用户名及密码判断
				if(userName != null && userName.length() > 0 || password != null && password.length() > 0){
					/*//admin除外
					if(userName.equals("admin")){
						systemUser = systemUserManager.checkUser(userName, password);
					}else {
						//CAS服务器IP及访问端口
				    	ServletContext context = request.getSession().getServletContext();
						String ip = context.getInitParameter("casserver");
				    	StringBuffer requestURL = request.getRequestURL();
				    	if (requestURL != null && requestURL.length() > 0) {
				    		String casserverIP = context.getInitParameter("casserverIP");
				    		if (requestURL.indexOf(casserverIP) > -1 || requestURL.indexOf("192.168.1.123") > -1 || requestURL.indexOf("127.0.0.1") > -1) {
				    			ip = casserverIP;
				    		}
				    	}
						String port = context.getInitParameter("casport");

						//************cas认证接口**************
						String server = "http://"+ip+":"+port+"/cas/v1/tickets";
						String service = "http://localhost";
						//用户验证获取票信息
						String ticket = "";
						try {
							ticket = CasService.getTicket(server, userName, password, service);
						} catch (RuntimeException e) {

							e.printStackTrace();
						}

						if(ticket != null && !ticket.equals("")){
							systemUser = this.systemUserManager.findSystemUserByName(userName);
						}
					}*/


					//admin除外
//					if(userName.equals("admin")){
					systemUser = systemUserManager.checkUser(userName, password);
//					}else {
//						boolean isDomainUser = AdValid.connect(userName, password);
//
//						if(isDomainUser){
//							systemUser = this.systemUserManager.findSystemUserByName(userName);
//						}else {
//							systemUser = null;
//						}
//					}

					if(systemUser != null && systemUser.getPersonId() != null && systemUser.getPersonId().intValue() > 0){
						/**********全局用户***********/
						JSONArray systemUserArray = new JSONArray();
						List systemUserList = new ArrayList();
						systemUserList.add(systemUser);
						systemUserArray = convert.modelCollect2JSONArray(systemUserList, new ArrayList());
						jsonObj.put("_SYSTEM_USER", systemUserArray);
						request.getSession().setAttribute("_SYSTEM_USER", systemUser);

						//是否是普通用户（或者管理员），与“投票用户”及“非合同制用工”区分
						boolean isNormalUser = false;
						if(systemUser.getUserType() == 0 || systemUser.getUserType() == 1){
							isNormalUser = true;
						}
						jsonObj.put("_NORMAL_USER", isNormalUser);
						request.getSession().setAttribute("_NORMAL_USER",isNormalUser);

						//投票用户
						boolean isTPUser = false;
						if(systemUser.getUserType() == 2){
							isTPUser = true;
						}
						jsonObj.put("_TP_USER", isTPUser);
						request.getSession().setAttribute("_TP_USER",isTPUser);


						//非合同制用工用户
						boolean isTmpUser = false;
						if(systemUser.getUserType() == 3){
							isTmpUser = true;
						}
						jsonObj.put("_TMP_USER",isTmpUser);
						request.getSession().setAttribute("_TMP_USER",isTmpUser);

						/**********全局人员***********/
						if(systemUser != null){
//							person = (PersonInfor)this.personInforManager.get(systemUser.getPersonId());
							person = systemUser.getPerson();
						}
						JSONArray personArray = new JSONArray();
						List personList = new ArrayList();
						personList.add(person);
						personArray = convert.modelCollect2JSONArray(personList, new ArrayList());
						jsonObj.put("_GLOBAL_PERSON", personArray);
						request.getSession().setAttribute("_GLOBAL_PERSON", person);

						/*********部门信息*********/
						OrganizeInfor departmentInfor = person.getDepartment();
						JSONArray departmentArray = new JSONArray();
						List departmentList = new ArrayList();
						departmentList.add(departmentInfor);
						departmentArray = convert.modelCollect2JSONArray(departmentList, new ArrayList());
						jsonObj.put("_GLOBAL_DEPARTMENT", departmentArray);
						request.getSession().setAttribute("_GLOBAL_DEPARTMENT", departmentInfor);

						/**********添加判断是否为投票问卷操作人************/
//						RoleInfor topicRole = (RoleInfor)this.roleManager.get(CoreConstant.Role_Topic_Operater);
//						boolean isTopicOpertor = this.roleManager.belongRole(systemUser, topicRole);
//						JSONArray topicArray = new JSONArray();
//						List topicList = new ArrayList();
//						topicList.add(isTopicOpertor);
//						topicArray = convert.modelCollect2JSONArray(topicList, new ArrayList());
//						jsonObj.put("_TOPIC_OPERATOR", topicArray);
//						request.getSession().setAttribute("_TOPIC_OPERATOR", isTopicOpertor);*/

						/*********首页图片*********/
						JSONArray imgArray = new JSONArray();
						List imgList = this.appImgManager.getAll();
						imgArray = convert.modelCollect2JSONArray(imgList, new ArrayList());
						jsonObj.put("_INDEX_IMG", imgArray);
						request.getSession().setAttribute("_INDEX_IMG", imgList);


						/***********首页待办及会议个数**********/
						//会议个数
						int meetingCount = 0;
						String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						List meetList = this.meetInforManager.getDayMeets(todayStr);
						meetingCount = meetList.size();
						jsonObj.put("_MEETING_COUNT", meetingCount);


						loginLog.setSucTag(1);
					}else {
						loginLog.setSucTag(0);
					}
				}else {
					loginLog.setSucTag(0);
				}
			}else {
				loginLog.setSucTag(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			loginLog.setSucTag(0);
			e.printStackTrace();
		}


		this.loginLogManager.save(loginLog);
		//设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	//获取系统首页左边菜单信息
	@RequestMapping("/leftMenuInfor.do")
	public void leftMenuInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		/** 根据用户权限加载信息发布分类 */
		List categoryMenu = new ArrayList();
		//获取根分类下的所有子类
		InforCategory category = (InforCategory)this.inforCategoryManager.get(InforConstant._Root_Category_Id);
		if (category != null && category.getChilds() != null) {
			Set childs = category.getChilds();
			for (Iterator it=childs.iterator();it.hasNext();) {
				InforCategory tmpCategory = (InforCategory)it.next();

				//判断用户对该分类是否有浏览权限(有浏览权限时才加载)
				SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
				boolean hasRight = this.inforCategoryRightManager.hasRight(tmpCategory, systemUser, InforCategoryRight._Right_View);
				if (hasRight) {
					//清除系统固定分类
					//海通固定分类：Cms_Category_Honor  Cms_Category_Newspaper   Cms_Category_Warrant   Cms_Category_Job  Cms_Category_Ensystem  Cms_Category_Political
					if (tmpCategory.getCategoryId() != InforConstant.Cms_Category_Annouce &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Companynews &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Important &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Knowledge &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Honor &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Newspaper &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Warrant &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Job &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Ensystem &&
							tmpCategory.getCategoryId() != InforConstant.Cms_Category_Political) {
						categoryMenu.add(tmpCategory);
					}
				}
			}
		}
		request.setAttribute("_CategoryMenu", categoryMenu);

		/** 获取流程分类和所有流程信息 */
		//流程分类
		String queryString = "select distinct(categoryName) from FlowDefinition where categoryName is not null";
		List flowCategorys = this.flowManager.getResultByQueryString(queryString);
		request.setAttribute("_FlowCategorys", flowCategorys);
		//流程信息
		List flows = this.flowManager.getAll();
		request.setAttribute("_Flows", flows);
//		String target = request.getParameter("target");
//		if(target!=null && target.equals("workflow")){
//
//			if(request.getParameter("flowId")!=null && request.getParameter("flowId")!=""){
//				request.setAttribute("flowId",request.getParameter("flowId"));
//			}
//			return "workflow/workflowMain";
//		}
//		if(target!=null && target.equals("personal")){
//			if(request.getParameter("pId")!=null && request.getParameter("pId")!=""){
//				request.setAttribute("pId",request.getParameter("pId"));
//			}
//			return "personal/personalMain";
//		}
//		if(target!=null && target.equals("daily")){
//			if(request.getParameter("dId")!=null && request.getParameter("dId")!=""){
//				request.setAttribute("dId",request.getParameter("dId"));
//			}
//			return "cms/dailyMain";
//		}
//		if(target!=null && target.equals("category")){
//			if(request.getParameter("cId")!=null && request.getParameter("cId")!=""){
//				request.setAttribute("cId",request.getParameter("cId"));
//			}
//			return "categoryMain";
//		}
//		if(target!=null && target.equals("document")){
//			request.setAttribute("document","documentTag");
//			return "document/documentMain";
//		}
//		if(target!=null && target.equals("task")){
//			if(request.getParameter("categoryType")!=null && request.getParameter("categoryType")!=""){
//				request.setAttribute("categoryType",request.getParameter("categoryType"));
//			}
//			return "supervise/superviseMain";
//		}
//		if(target!=null && target.equals("baseMaintain")){
//			if(request.getParameter("bId")!=null && request.getParameter("bId")!=""){
//				request.setAttribute("bId",request.getParameter("bId"));
//			}
		//系统维护
//			return "baseMain";
//		}
//		if(target!=null && target.equals("subject")){
//			if(request.getParameter("sId")!=null && request.getParameter("sId")!=""){
//				request.setAttribute("sId",request.getParameter("sId"));
//			}
//			return "subjectMain";
//		}
//		if(target!=null && target.equals("ticket")){
//			if(request.getParameter("tId")!=null && request.getParameter("tId")!=""){
//				request.setAttribute("tId",request.getParameter("tId"));
//			}
//			return "ticketMain";
//		}
//		if(target!=null && target.equals("activity")){
//			request.setAttribute("activity","activity");
//			return "activityMain";
//		}

//		if(target!=null && !target.equals("")){
//			switch (target){
//				case "workflow":
//					if(request.getParameter("flowId")!=null && !request.getParameter("flowId").equals("")){
//						request.setAttribute("flowId",request.getParameter("flowId"));
//					}
//					break;
//				case "personal":
//					if (request.getParameter("pId") != null && !request.getParameter("pId").equals("")) {
//						request.setAttribute("pId", request.getParameter("pId"));
//					}
//					break;
//				case "daily":
//					if (request.getParameter("dId") != null && !request.getParameter("dId").equals("")) {
//						request.setAttribute("dId", request.getParameter("dId"));
//					}
//					break;
//				case "document":
//					request.setAttribute("document","documentTag");
//					break;
//				case "category":
//					if (request.getParameter("cId") != null && !request.getParameter("cId").equals("")) {
//						request.setAttribute("cId", request.getParameter("cId"));
//					}
//					break;
//				case "task":
//					if (request.getParameter("categoryType") != null && !request.getParameter("categoryType").equals("")) {
//						request.setAttribute("categoryType", request.getParameter("categoryType"));
//					}
//					break;
//				case "baseMaintain":
//					if (request.getParameter("bId") != null && !request.getParameter("bId").equals("")) {
//						request.setAttribute("bId", request.getParameter("bId"));
//					}
//					break;
//				case "subject":
//					if (request.getParameter("sId") != null && !request.getParameter("sId") .equals("")) {
//						request.setAttribute("sId", request.getParameter("sId"));
//					}
//					break;
//				case "ticket":
//					if (request.getParameter("tId") != null && !request.getParameter("tId").equals("")) {
//						request.setAttribute("tId", request.getParameter("tId"));
//					}
//					break;
//				case "activity":
//					request.setAttribute("activity","activity");
//					break;
//				case "normal":
//					request.setAttribute("normal","normal");
//				default:
//					return "menu";
//			}
//			return "baseMain";
//		}

//		return "menu";
	}

	//获取系统首页右边栏信息
	@RequestMapping("/mainInfor.do")
	public void mainInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取所有可以在首页显示的模块信息
		List<ConfigIndexFun> configIndexFun = configIndexFunManager.getAll();

		request.setAttribute("_IndexFunc", configIndexFun);

//		return "mainInfor";
	}

	/**
	 * 下载转发。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/download.do")
	public String download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePathIndex = request.getParameter("filePathIndex");
		String instanceIdStr = request.getParameter("instanceId");
//		request.setAttribute("filepath", filepath);

		String downloadPath = "";
		String fileName = "";
		if(StringUtil.isNotEmpty(instanceIdStr)){
			int instanceId = Integer.valueOf(instanceIdStr);
			FlowInstanceInfor instanceInfor = (FlowInstanceInfor)this.flowInstanceManager.get(instanceId);
			String attach = instanceInfor.getAttach();
			if(StringUtil.isNotEmpty(attach)){
				String[] attachList = attach.split("\\|");
				if(StringUtil.isNotEmpty(filePathIndex)){
					downloadPath = attachList[Integer.valueOf(filePathIndex)];
//					downloadPath += "_kwchina";
					int pos = downloadPath.lastIndexOf("/");
					fileName = downloadPath.substring(pos+1);
				}
			}
		}
		request.setAttribute("filePath", downloadPath);
		request.setAttribute("fileName", fileName);
		return "downExcel";
//		request.getSession().removeAttribute("_File_Path");
//		request.getSession().setAttribute("_File_Path", downloadPath);
//		return "/common/download_excel";

	}

}
