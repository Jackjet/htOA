package com.kwchina.webmail.web.form;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.kwchina.core.util.DateConverter;

public class MailForm extends ActionForm {
	static {
		ConvertUtils.register(new DateConverter(), Date.class);
	}
	
	private String folderId;
	
	private String messageId;
	
	
	private int serialNo;
	
	private int page;

	private int inpages;
	

	public ActionErrors validate(ActionMapping actionMapping,
			HttpServletRequest httpServletRequest) {
		ActionErrors errors = new ActionErrors();
		if (getInpages() == 0) {
			setInpages(1);
		}

		if (getPage() == 0) {
			setPage(1);
		}

		return errors;
	}
	
	public int getInpages() {
		return inpages;
	}

	public void setInpages(int inpages) {
		this.inpages = inpages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

}
