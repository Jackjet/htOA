package com.kwchina.oa.servlet;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.sys.CoreConstant;

@Controller
@RequestMapping("/mail/getMailInfor.do")
public class GetMailInfor extends BasicController {

	/**
	 * 获取邮件信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getMail")
	public void getMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// doPost(request,response);

		Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
		Store st = mailSession.getStore("imap");
		
		// 获取该用户的总邮件，最新邮件数
		try {
			// 服务器,用户名,密码
			ServletContext context = request.getSession().getServletContext();
			String mailHost = context.getInitParameter("mailserverIP");
			
	    	StringBuffer requestURL = request.getRequestURL();
	    	if (requestURL != null && requestURL.length() > 0) {
	    		String mailserverIP = context.getInitParameter("mailserverIP");
	    		if (requestURL.indexOf(mailserverIP) > -1) {
	    			mailHost = mailserverIP;
	    		}
	    	}

			String mailUser = "";
			SystemUserInfor systemUser = (SystemUserInfor) request.getSession().getAttribute("_SYSTEM_USER");
			if (systemUser != null) {

				String email = systemUser.getPerson().getEmail();
				if (email != null && !email.equals("")) {
					// int pos = email.indexOf("@");
					// if(pos>0)
					// username = email.substring(0, pos);
					mailUser = email;
				}
				String mailPassword = systemUser.getPerson().getEmailPassword() == null ? "" : systemUser.getPerson()
						.getEmailPassword();

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
			}
			
			st.close();			
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println(ex.toString());
		} finally {
			if(st!=null)
				st.close();
		}
	}

}
