package com.kwchina.webmail.web.service;

import javax.servlet.http.HttpServletRequest;

import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.web.bean.UserConfigInfor;
import com.kwchina.webmail.xml.XMLUserData;

public class MailConfigService extends MailBasicService {
	
	//获取用户设置的信息
	public static void getUserConfigInfor(HttpServletRequest request,HTTPRequestHeader http_header){	
		//Mail Session
		WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");		
		XMLUserData userData = (XMLUserData)mailSession.getUser();		
		
		//XMLUserModel
		//XMLUserModel userModel = mailSession.getUserModel();
		
		//Element userData = userModel.getUserData();
		
		UserConfigInfor userConfig = new UserConfigInfor();
		userConfig.setLogin(userData.getLogin());
		userConfig.setPassword(userData.getPassword());
		
		userConfig.setFullName(userData.getFullName());
		userConfig.setEmail(userData.getEmail());
		userConfig.setSignature(userData.getSignature());
		userConfig.setSaveSent(userData.getBoolVar("save sent messages"));
		userConfig.setSaveAddress(userData.getBoolVar("auto save address"));
		userConfig.setDirectDelete(userData.getBoolVar("direct delete message"));
		userConfig.setMaxShow((int)userData.getIntVar("max show messages"));
		userConfig.setSendFolder(userData.getSentFolder());
		
		request.setAttribute("_User_Config", userConfig);
	}

}
