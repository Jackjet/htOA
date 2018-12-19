package com.kwchina.core.sys;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class SystemLauncher implements ServletContextListener {

	public SystemLauncher() {
	
	}

	private String systemName;

	public void contextInitialized(ServletContextEvent sce) {
		//从web.xml获取系统名称
		ServletContext sc = sce.getServletContext();
		this.systemName = sc.getInitParameter("systemName");
		
		System.out.println("[" + systemName + "] Now Initializing...");
		
		//获取项目工程的绝对路径
		CoreConstant.Context_Real_Path = sc.getRealPath("/");
	
		System.out.println("[" + systemName + "] Has Initialized.");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("[" + systemName + "] Now Finalizing...");

		//这里执行某些需要关闭的操作

		System.out.println("[" + systemName + "] Has Finalized.");
	}
}
