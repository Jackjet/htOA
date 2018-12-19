package com.kwchina.oa.sys;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.kwchina.extend.domain.util.LdapADHelper;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.service.FlowInstanceManager;


import java.sql.PreparedStatement;


public class PushTask extends TimerTask { 
		 
	private PushTask(){}
	   
	private static PushTask _checkTask = new PushTask();
	public static PushTask getCheckLicensidTask(){
		return _checkTask;
	}
	   
	private boolean _isRunning  = false;
	private ServletContext context = null;
	
	public void setContext(ServletContext context){
		this.context = context;
	}

	public void run(){
		
		try{
			if (!this._isRunning){
				this._isRunning = true;
			
			    //从容器中得到注入的bean 
				WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
				PersonInforManager personInforManager = (PersonInforManager)applicationContext.getBean("personInforManager");
				SystemUserManager systemUserManager = (SystemUserManager)applicationContext.getBean("systemUserManager");
				FlowInstanceManager flowInstanceManager = (FlowInstanceManager)applicationContext.getBean("flowInstanceManager");
				
				System.out.println("-------------------------开始执行待办推送："+new Timestamp(System.currentTimeMillis())+"-------------------------");
				
				/*
				 * 首先得到所有用户，再循环取每个用户下的待办事项，定时器设置为2分钟取一次，则每次取当前时间至2分钟之间到达的待办
				 * 不考虑了：（考虑到每次取的时候处理的时间差，故多取半分钟）
				 * 将每个用户下的移动端需要显示的待办审批使用JPUSH接口推送出
				 * 
				 * JPush接口中，ios版本包含角标数字属性，android中不包含，故需要写在扩展属性中(http://docs.jpush.cn/display/dev/Push-API-v3)
				 * 
				 */
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//当前时间
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				//两分半前
				Timestamp cutTime = Timestamp.valueOf(sf.format(DateHelper.addMinute(nowTime, -2)));
				
				List allUsers = systemUserManager.getAllValid();
				for(Iterator it = allUsers.iterator();it.hasNext();){
					SystemUserInfor systemUser = (SystemUserInfor)it.next();
					String alias = systemUser.getUserName();
					
					List<FlowInstanceInfor> returnInstances_m = flowInstanceManager.getNeedPushInstances(systemUser, cutTime);
					
					for(FlowInstanceInfor instance : returnInstances_m){
//						String flowName = instance.getFlowDefinition().getFlowName();
						//System.out.println("----:"+flowName);
						/*int flowId = instance.getFlowDefinition().getFlowId().intValue();
						String instanceTitle = instance.getInstanceTitle();
						int instanceId = instance.getInstanceId();
						int badge = returnInstances_m.size();
						System.out.println("===="+alias+"：有"+badge+"条待办事项！");
						//每个用户推两个平台//"【"+flowName+"】"+
						PushUtil androidPushUtil = new PushUtil(instanceTitle,"请处理审批");
						PushUtil iosPushUtil = new PushUtil("请处理审批：" + instanceTitle,"");
						
						Map<String, String> extras = new HashMap<String, String>();
						extras.put("badge", String.valueOf(badge));//ios用的角标
						extras.put("instanceId", String.valueOf(instanceId));//instanceId
						
						//流程分类
						
						 * android：
						 * 发文 5
						 * 合同 6
						 * 内部报告 7
						 * 制度评审 8
						 * 收文管理 9
						 
						int flowType = 0;
						if(flowId == 85){
							flowType = 5;
						}else if(flowId == 86){
							flowType = 6;
						}else if(flowId == 87){
							flowType = 7;
						}else if(flowId == 88){
							flowType = 8;
						}else if(flowId == 84){
							flowType = 9;
						}
						extras.put("androidFlowType", String.valueOf(flowType));
						
						long msgId_android = androidPushUtil.sendPushAlias("android", alias, extras, badge);
						long msgId_ios = iosPushUtil.sendPushAlias("ios", alias, extras, badge);*/
						int badge = returnInstances_m.size();
						PushUtil pushUtil = new PushUtil();
						pushUtil.pushNeedDealInstances(instance, badge, alias);
					}
					
					
				}
				
				this._isRunning = false;
	
				System.out.println("-------------------------待办推送执行完成："+new Timestamp(System.currentTimeMillis())+"-------------------------");
			}else{
				context.log("上一次任务执行还未结束 ");
				this._isRunning = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("-------------------------待办推送执行出错-------------------------");
			this._isRunning = false;
		}

	}
	
	   
	   
	public void destroy(){
		//Just puts "destroy " string in log
		//Put your code here
	}

}