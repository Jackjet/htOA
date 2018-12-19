package com.kwchina.oa.sys;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.core.util.DateHelper;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.service.ClubInforManager;
import com.kwchina.extend.domain.util.LdapADHelper;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.service.FlowInstanceManager;


import java.sql.PreparedStatement;


public class ClubSignEndTask extends TimerTask { 
		 
	private ClubSignEndTask(){}
	   
	private static ClubSignEndTask _checkTask = new ClubSignEndTask();
	public static ClubSignEndTask getCheckLicensidTask(){
		return _checkTask;
	}
	   
	private boolean _isRunning  = false;
	private ServletContext context = null;
	
	public void setContext(ServletContext context){
		this.context = context;
	}

	public void run(){
		
		try{
//			if (!this._isRunning){
//				this._isRunning = true;
			
			    //从容器中得到注入的bean 
				WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
				ClubInforManager clubInforManager = (ClubInforManager)applicationContext.getBean("clubInforManager");
				
				System.out.println("-------------------------开始执行活动自动结束报名："+new Timestamp(System.currentTimeMillis())+"-------------------------");
				
				/*
				 * 
				 * 首先得到所有未完成的、报名结束日期在今天的活动列表，自动将其状态改为报名中
				 * 
				 */
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//				//当前时间
//				long current = System.currentTimeMillis();
//				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
//				
				Date today = new Date();
				String todayStr = sf.format(today);
				
				List<ClubInfor> unFinishedInfors = clubInforManager.getUnfinishedInfors();
				for(ClubInfor club : unFinishedInfors){
					if(club.getStatus() == ClubInfor.Club_Status_Reging && club.getCutDate() != null){
						String cutDateTime = sf.format(club.getCutDate());
//						if(cutDateTime.equals(todayStr)){
						if(cutDateTime.equals(todayStr) || DateHelper.getDate(cutDateTime).before(DateHelper.getDate(todayStr))){
							System.out.println("*****************操作了：" + club.getActTitle() + "*******************");
							clubInforManager.changeTaskStatus(club, ClubInfor.Club_Status_ToGegin);
						}
					}
					
				}
				
//				this._isRunning = false;
	
				System.out.println("-------------------------活动自动结束报名执行完成："+new Timestamp(System.currentTimeMillis())+"-------------------------");
//			}else{
//				context.log("上一次任务执行还未结束 ");
//				this._isRunning = false;
//			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("------------------------活动自动结束报名执行出错-------------------------");
//			this._isRunning = false;
		}

	}
	
	   
	   
	public void destroy(){
		//Just puts "destroy " string in log
		//Put your code here
	}

}