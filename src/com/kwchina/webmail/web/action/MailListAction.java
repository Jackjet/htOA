
package com.kwchina.webmail.web.action;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.servlet.WebMailInit;
import com.kwchina.webmail.web.bean.CurrentInfor;
import com.kwchina.webmail.web.form.MailForm;
import com.kwchina.webmail.web.service.MailBasicService;
import com.kwchina.webmail.web.service.MailListService;

public class MailListAction extends MailDispatchAction {
	
	private static Log log = LogFactory.getLog(MailListAction.class);	
	
	public ActionForward list(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailListAction.list' method...");
		}
				
		MailForm mailForm = (MailForm)form;
		
		/**
		 * 判断是否用户已经登录邮箱，第一次是需要在这里登录的
		 * 之后登录OA时，及登录邮箱
		 */
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
		if (mailSession==null){		
			//登录页面
			/**@todo Get Domain Name*/
			
			ServletContext context = this.getServlet().getServletContext();
			WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);			
			WebMailInit webmailServer = (WebMailInit) webContext.getBean("webmailServer");
			
			String domainName = "";
			Enumeration domains = webmailServer.getServer().getStorage().getSystemData().getVirtualDomains();
			if(domains.hasMoreElements()){
				domainName = (String)domains.nextElement();
				//domainName = domain.getDomainName();				
			}
			request.setAttribute("_Domain_Name", domainName);			
			//XMLSystemData sysData = webmailServer.getServer().getStorage().getSystemData();			
			
			return mapping.findForward("login");
		}else{
			//页面参数
			HTTPRequestHeader http_header = this.getRequestHeader(request);
			
			//获取文件夹的信息并显示
			ArrayList folders = MailListService.getFolderInfor(request);	
			request.setAttribute("_Folders", folders);		
			
			//----获取邮件信息------------
			String folderId = mailForm.getFolderId();
			MailListService.getFolderMailInfor(request, folderId, http_header);
			
			//获取当前Folder的信息
			CurrentInfor current = MailListService.getCurrentFolder(request);
			request.setAttribute("_Current_Folder", current);
			
			//Trash Folder的信息
			String trashFolder = MailBasicService.getTrashFloderId(request);
			request.setAttribute("_Trash_Folder_Id", trashFolder);
			
			return mapping.findForward("list");
		}
	}
	
	//删除邮件到垃圾箱
	public ActionForward move(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailListAction.move' method...");
		}
				
		MailForm mailForm = (MailForm)form;
		
		//Mail Session
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
		
		//页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		
		
		//当前Folder
		String folderId = mailForm.getFolderId();
		if(folderId==null || folderId.equals("")){
			CurrentInfor current = MailBasicService.getCurrentFolder(request);
			folderId = current.getId();
		}
		
		//设置邮件标志，并更新FOLDER，MESSAGELIST
		try{		
			mailSession.setFlags(folderId, http_header);
		}catch (MessagingException ex){
			request.setAttribute("_Error_Message", ex.toString());
			
			//定位到错误页
			return mapping.findForward("mailerror");
		}
		
		
		//从定位到列表页面
		int nr = 1;
		try {
			nr = Integer.parseInt(http_header.getContent("PART"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		String path = "/webmail/mailList.do?method=list&folderId=" + folderId + "&part="+nr;
		return new ActionForward(path,true);
		//return list(mapping,form,request,response);
	}
	
	//删除选择的邮件
	public ActionForward delete(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailListAction.delete' method...");
		}
				
		MailForm mailForm = (MailForm)form;
		
		//Mail Session
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
		
		//页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		
		
		//设置邮件标志，并更新FOLDER，MESSAGELIST
		String folderId = mailForm.getFolderId();
		if(folderId==null || folderId.equals("")){
			CurrentInfor current = MailBasicService.getCurrentFolder(request);
			folderId = current.getId();
		}
		mailSession.setFlags(folderId, http_header);
		
		
		//从定位到列表页面
		int nr = 1;
		try {
			nr = Integer.parseInt(http_header.getContent("PART"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		String path = "/webmail/mailList.do?method=list&folderId=" + folderId + "&part="+nr;
		return new ActionForward(path,true);
		//return list(mapping,form,request,response);
	}	
}

