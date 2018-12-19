package com.kwchina.oa.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.webmail.server.WebMailSession;

@Controller
public class Logout {

	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("_SYSTEM_USER", null);
		
		  //邮件用户关闭，清除相关信息
    	WebMailSession mailSession = (WebMailSession)request.getSession().getAttribute("webmail.session");
		if (mailSession!=null) {
			mailSession.logout();
			
			//清除session
			request.getSession().setAttribute("webmail.session",null);			
		}
		
		
		return "login";
	}
}

