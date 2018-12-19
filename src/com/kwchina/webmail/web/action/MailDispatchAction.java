package com.kwchina.webmail.web.action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.web.bean.MailQuotaInfor;
import com.kwchina.webmail.web.service.MailListService;

public class MailDispatchAction extends DispatchAction{
		
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 设定文字编码
		//request.setCharacterEncoding("GBK");

		String name = request.getParameter("method");		
		if (null == name) {
			//如果没有指定 method ，则默认为 list
			name = "list";
		}		
		
		//获取一些公用的信息
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
		if(mailSession != null) {
			MailQuotaInfor mailQuota = MailListService.getMailQuota(request);
			request.setAttribute("_Mail_Quota", mailQuota);
		}
		
		return dispatchMethod(mapping, form, request, response, name);
	}
		
	
	//获取页面参数
	public HTTPRequestHeader getRequestHeader(HttpServletRequest request){
		HTTPRequestHeader http_header = new HTTPRequestHeader();
		Enumeration en = request.getHeaderNames();
		while (en.hasMoreElements()) {
			String s = (String) en.nextElement();
			http_header.setHeader(s, request.getHeader(s));
		}

		Enumeration enum2 = request.getParameterNames();
		while (enum2.hasMoreElements()) {
			String s = (String) enum2.nextElement();
			Object paramValue = request.getParameter(s);
			http_header.setContent(s, paramValue);
		}

		/* Then we set all the headers in http_header */
		enum2 = request.getHeaderNames();
		while (enum2.hasMoreElements()) {
			String s = (String) enum2.nextElement();
			http_header.setHeader(s, request.getHeader(s));
		}		
		
		return http_header;
	}	
	
	
	
	
}
