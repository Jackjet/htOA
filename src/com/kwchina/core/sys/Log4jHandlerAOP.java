package com.kwchina.core.sys;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.util.SysCommonMethod;
@Component
@Aspect
public class Log4jHandlerAOP {
    
	Logger logger = Logger.getLogger(Log4jHandlerAOP.class);
	
	//拦截网页登录信息
    @After("execution(* com.kwchina.oa.sys.LoginConfirm.casLogin(..)) && args(request,..)")
    public void loginLog(JoinPoint joinPoint, HttpServletRequest request) {

        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if (systemUser != null) {
        	//登录时间
            Date loginTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("用户【" + systemUser.getUserName() + " - " + systemUser.getPerson().getPersonName() + "】于" + sdf.format(loginTime) + "登录网页端.");
        }
//        System.out.println("----执行登录拦截----");
    }
    
    //拦截手机登录信息
    @After("execution(* com.kwchina.oa.sys.LoginConfirm.login_m(..)) && args(request,..)")
    public void loginMLog(JoinPoint joinPoint, HttpServletRequest request) {

        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if (systemUser != null) {
        	//登录时间
            Date loginTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("用户【" + systemUser.getUserName() + " - " + systemUser.getPerson().getPersonName() + "】于" + sdf.format(loginTime) + "登录app端.");
        }
//        System.out.println("----执行登录拦截----");
    }
    
	//拦截Controller
    @Around("execution(* com.kwchina.*.*.controller.*.*(..)) && args(request,..)")
    public Object controllerLog(ProceedingJoinPoint pjp, HttpServletRequest request) {
        
        Object obj = null;
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
    	
    	//获取目标类名、方法名
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        
        //请求路径
        String requestURL = getRequestURL(request);
        
        if (systemUser != null) {
            logger.info("用户【" + systemUser.getUserName() + "】进入类: " + className + " 执行操作: " + methodName + " 路径：" + requestURL);
        }
        
		try {
			//执行目标类
			obj = pjp.proceed();
			if (systemUser != null) {
//				logger.info("用户【" + systemUser.getUserName() + "】退出类: " + className);
			}
		} catch (Throwable e) {
			logger.error("发生异常:" + e.toString());
		}
//        System.out.println("----执行Controller拦截----");
        return obj;
    }
    
//	//拦截service
//    @After("execution(* com.kwchina.*.*.service.*.*(..))")
//    public void serviceLog(JoinPoint joinPoint) {
//    	//获取目标类名、方法名
//    	String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        logger.info("调用类: " + className + "的方法: " +methodName);
////        System.out.println("----执行service拦截----");
//    }
    
	//拦截退出信息
    @Before("execution(* com.kwchina.oa.sys.Logout.logout(..)) && args(request,..)")
    public void logoutLog(JoinPoint joinPoint, HttpServletRequest request) {

        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if (systemUser != null) {
        	//退出时间
            Date logoutTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("用户【" + systemUser.getUserName() + " - " + systemUser.getPerson().getPersonName() + "】于" + sdf.format(logoutTime) + "退出.");
        }
//        System.out.println("----执行退出拦截----");
    }
    
    
    public static String getRequestURL(HttpServletRequest request) { 
    	if (request == null) { 
    		return "";

		}
    	String url = "";
    	url = request.getContextPath(); 
    	url = url + request.getServletPath();
    	java.util.Enumeration names = request.getParameterNames(); 
    	int i = 0; 
//    	if (!"".equals(request.getQueryString()) || request.getQueryString() != null) { 
//    		url = url + "?" + request.getQueryString(); 
//    	} 

    	if (names != null) { 
	    	while (names.hasMoreElements()) { 
		    	String name = (String) names.nextElement(); 
		    	if (i == 0) { 
		    		url = url + "?"; 
		    	} else { 
		    		url = url + "&"; 
		    	} 
		    	i++; 
		
		    	String value = request.getParameter(name); 
		    	if (value == null) { 
		    		value = ""; 
		    	} 
		
		    	url = url + name + "=" + value; 
		    	try { 
		    		// java.net.URLEncoder.encode(url, "ISO-8859"); 
		    	} catch (Exception e) { 
		    		e.printStackTrace(); 
		    	} 
	    	} 
    	} 
    	try { 
    	// String enUrl = java.net.URLEncoder.encode(url, "utf-8"); 
    	} catch (Exception ex) { 
    		ex.printStackTrace(); 
    	} 

    	return url; 
    }  
    
}
