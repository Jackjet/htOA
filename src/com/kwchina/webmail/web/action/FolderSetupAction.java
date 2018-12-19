package com.kwchina.webmail.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kwchina.webmail.exception.DocumentNotFoundException;
import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.web.form.MailForm;
import com.kwchina.webmail.web.service.MailBasicService;
import com.kwchina.webmail.web.service.MailListService;

public class FolderSetupAction extends MailDispatchAction {

	private static Log log = LogFactory.getLog(MailListAction.class);

	// 罗列Folder
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'FolderSetupAction.list' method...");
		}

		MailForm mailForm = (MailForm) form;

		// 页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);

		// 获取文件夹的信息并显示
		ArrayList folders = MailListService.getFolderInfor(request);
		request.setAttribute("_Folders", folders);

		return mapping.findForward("edit");
	}

	// 保存邮件夹
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'FolderSetupAction.save' method...");
		}

		MailForm mailForm = (MailForm) form;

		// 页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);

		WebMailSession mailSession = (WebMailSession) request.getSession().getAttribute("webmail.session");
		// UserData user = mailSession.getUser();

		// 假如全部添加到收件夹下面

		try {
			// 收件夹Id
			String inboxId = MailBasicService.getInboxFloderId(request);

			String oldName = http_header.getContent("oldName");
			String folderName = http_header.getContent("fname");

			if (oldName == null || oldName.equals("")) {
				mailSession.addFolder(inboxId, folderName, true, false);
			} else {
				mailSession.editFolder(inboxId, folderName, oldName);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebMailException("Error while adding folders");
		}

		mailSession.refreshFolderInformation(false);

		/** 把XML输出查看 */
		/**
		 * try{ Writer out = new OutputStreamWriter(new
		 * FileOutputStream("c:\\test_1.xml"),"GBK"); OutputFormat format =
		 * OutputFormat.createPrettyPrint(); //指定XML编码
		 * format.setEncoding("GBK");
		 * 
		 * XMLWriter writer = new XMLWriter(out, format);
		 * writer.write(mailSession.getModel()); out.flush(); out.close();
		 * }catch(Exception ex){ }
		 */

		return list(mapping, form, request, response);
	}

	// 删除邮件夹
	public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'FolderSetupAction.remove' method...");
		}

		MailForm mailForm = (MailForm) form;

		// 页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);

		WebMailSession mailSession = (WebMailSession) request.getSession().getAttribute("webmail.session");
		// UserData user = mailSession.getUser();

		try {
			String folderId = http_header.getContent("folderId");
			mailSession.removeFolder(folderId, true);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WebMailException("Error while adding folders");
		}

		mailSession.refreshFolderInformation(false);

		return list(mapping, form, request, response);
	}
}
