package com.kwchina.webmail.web.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.personal.address.service.AddressCategoryManager;
import com.kwchina.oa.personal.address.service.PersonalAddressManager;
import com.kwchina.webmail.misc.ByteStore;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.servlet.WebMailInit;
import com.kwchina.webmail.web.bean.CurrentInfor;
import com.kwchina.webmail.web.form.MailForm;
import com.kwchina.webmail.web.service.MailBasicService;
import com.kwchina.webmail.web.service.MailListService;
import com.kwchina.webmail.web.service.MailMessageService;

public class MailInforAction extends MailDispatchAction {
	
	private static Log log = LogFactory.getLog(MailListAction.class);	
	
	
	
	//显示单个邮件信息
	public ActionForward list(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailInforAction.list' method...");
		}
				
		MailForm mailForm = (MailForm)form;
		
		//页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		
		//获取文件夹的信息并显示
		ArrayList folders = MailListService.getFolderInfor(request);	
		request.setAttribute("_Folders", folders);
		
		//----获取邮件信息------------
		//int serialNo = mailForm.getSerialNo();
		MailMessageService.getMailMessageInfor(request,http_header);
				
		//获取当前Folder的信息
		CurrentInfor current = MailListService.getCurrentFolder(request);
		request.setAttribute("_Current_Folder", current);
				
		//Trash Folder的信息
		String trashFolder = MailBasicService.getTrashFloderId(request);
		request.setAttribute("_Trash_Folder_Id", trashFolder);
		
		return mapping.findForward("list");
	}
	
	
	//回复发件人
	public ActionForward reply(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailInforAction.reply' method...");
		} 
		
		//获取邮件回复的相关信息
		MailMessageService.getWriteInfor(request,1);
		
		//
		getMailWriteInfor(request);
		
		return mapping.findForward("edit");
	}
	
	//转发邮件
	public ActionForward forward(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailInforAction.forward' method...");
		} 
		
		//获取邮件回复的相关信息
		MailMessageService.getWriteInfor(request,2);
		
		//
		getMailWriteInfor(request);
		
		return mapping.findForward("edit");
	}
	
	//编写新邮件
	public ActionForward edit(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailInforAction.edit' method...");
		}
		
		//获取相关信息
		MailMessageService.getWriteInfor(request,0);
		
		
		getMailWriteInfor(request);
		
		
		return mapping.findForward("edit");
	}
	
	
	
	
	private void getMailWriteInfor(HttpServletRequest request){
		//获取文件夹的信息并显示
		ArrayList folders = MailListService.getFolderInfor(request);	
		request.setAttribute("_Folders", folders);			
		
		ServletContext context = request.getSession().getServletContext();//getServlet().getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		PersonalAddressManager personalAddressManager = (PersonalAddressManager) webContext.getBean("personalAddressManagerImpl");
		PersonInforManager personManager = (PersonInforManager) webContext.getBean("personInforManagerImpl");		
		AddressCategoryManager addressCategoryManager = (AddressCategoryManager) webContext.getBean("addressCategoryManagerImpl");
		
		//获取公司，个人通讯录
		SystemUserInfor preUser = (SystemUserInfor) request.getSession().getAttribute("_SYSTEM_USER");
		List selfPersons = personalAddressManager.getAll();
		request.setAttribute("_Self_Persons", selfPersons);
		
		List companyPersons = personManager.getAll();
		request.setAttribute("_Company_Persons", companyPersons);
		
		//获取个人通讯录分类
		List addressCategorys = addressCategoryManager.getCategoryName(preUser.getPersonId());
		request.setAttribute("_Address_Categorys", addressCategorys);
	}
	
	
	//发送邮件
	public ActionForward send(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailInforAction.send' method...");
		}
		
		MailForm mailForm = (MailForm)form;
		
		ServletContext context = this.getServlet().getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		WebMailInit webmailServer = (WebMailInit) webContext.getBean("webmailServer");
		
		HTTPRequestHeader http_header = this.getRequestHeader(request);
				
		//对附件信息进行处理
		Hashtable files = form.getMultipartRequestHandler().getFileElements();
		
		//最大附件设置
		int size = Integer.parseInt(webmailServer.getStorage().getConfig("max attach size"));		
		
		FormFile f[] = null;
		if (files != null || files.size() != 0) {			
			// System.out.println(files.size());
			f = new FormFile[files.size()];
			int i = 0;
			String fname = "";
			java.util.Enumeration enums = files.keys();
			while (enums.hasMoreElements()) {
				fname = (String) enums.nextElement();
				// System.out.println(fname);
				f[i] = (FormFile) files.get(fname);
				i++;
			}
			
			
			for (int a = 0; a < f.length; a++) {
				if (f[a] == null || f[a].getFileSize() == 0 || f[a].getFileName() == null || f[a].getFileName().equals("")) {
				} else {
					String fileName = f[a].getFileName();
					fileName  = MimeUtility.encodeText(fileName);
					
					ByteStore bs = ByteStore.getBinaryFromIS(f[a].getInputStream(), size);					
					bs.setName(fileName);
					bs.setContentType(webmailServer.getStorage().getMimeType(fileName));
					
					java.util.Random r=new java.util.Random(); 
					http_header.setContent("file_" + fileName + r.nextInt() , bs);					
				}
			}
		}
				
		
		//发送邮件
		MailMessageService.sendMessage(request,http_header,webmailServer);
		
		//获取文件夹的信息并显示
		ArrayList folders = MailListService.getFolderInfor(request);	
		request.setAttribute("_Folders", folders);	
		
		return mapping.findForward("sendresult");
	}
	
	
	//获取组织结构及人员，自我通讯录
	public ActionForward getPerson(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'MailInforAction.getPerson' method...");
		}
		
		getMailWriteInfor(request);
		
		//获取部门班组等
		ServletContext context = request.getSession().getServletContext();//getServlet().getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		OrganizeManager organizeManager = (OrganizeManager) webContext.getBean("organizeManagerImpl");
		ArrayList returnArray = organizeManager.getOrganizeAsTree(CoreConstant.Organize_Begin_Id);
		request.setAttribute("_TREE", returnArray);	
		
		return mapping.findForward("getPerson");
	}		
	
}