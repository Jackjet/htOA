package com.kwchina.webmail.web.action;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.kwchina.webmail.exception.DocumentNotFoundException;
import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.web.form.MailForm;
import com.kwchina.webmail.web.service.MailConfigService;
import com.kwchina.webmail.web.service.MailListService;

public class UserConfigAction extends MailDispatchAction {
	
	private static Log log = LogFactory.getLog(MailListAction.class);	
	
	public ActionForward list(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'UserConfigAction.list' method...");
		}
				
		MailForm mailForm = (MailForm)form;
		
		//页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		
		//读取设置的内容
		MailConfigService.getUserConfigInfor(request,http_header);		
	
		//获取文件夹的信息并显示
		ArrayList folders = MailListService.getFolderInfor(request);	
		request.setAttribute("_Folders", folders);	
		
		
		return mapping.findForward("edit");
	}
	
	
	//保存设置的参数
	public ActionForward save(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'UserConfigAction.save' method...");
		}
				
		MailForm mailForm = (MailForm)form;
		
		//页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");			
		//UserData user = mailSession.getUser();

		//
		mailSession.refreshFolderInformation();		
		
	    try {
			//保存设置的内容
			mailSession.changeSetup(http_header);
		} catch (InvalidPasswordException e) {
				throw new DocumentNotFoundException(
						"The two passwords did not match");
		}	
		
		/**把XML输出查看*/		
		/**
		try{
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"),"GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML编码 
	        format.setEncoding("GBK");           

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(mailSession.getModel());
			out.flush();
			out.close();
		}catch(Exception ex){
			
		}*/
		
		return list(mapping,form,request,response);
	}
}
