package com.kwchina.webmail.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.servlet.WebMailLogin;

public class MailLogin extends WebMailLogin {
	private static Log log = LogFactory.getLog(MailLogin.class);

	

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		SystemUserInfor systemUser = (SystemUserInfor)request.getSession().getAttribute("_SYSTEM_USER");
		String userName = systemUser.getUserName();
		
		if(this.newUserSession(request, userName,true)){
			//CoreConstant.Mail_Login_Sucess = true;
			
			WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
			//登录这名字放入session
			if(request.getSession().getAttribute("_Mail_User_Name")==null){
				String loginName = mailSession.getUserName();
				if(loginName.indexOf("@")>0){
					loginName = loginName.substring(0, loginName.indexOf("@"));
				}
				request.getSession().setAttribute("_Mail_User_Name", loginName);
			}
			
			return mapping.findForward("success");
			
			//String path = "/webmail/mailList.do?method=list";
			//return new ActionForward(path,true);
			
		}else{
			return mapping.findForward("fail");
		}		
	}
	
	

}
