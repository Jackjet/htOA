package com.kwchina.oa.sys;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyApplicationContextUtil implements ApplicationContextAware {
	// 声明一个静态变量保存
	private static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext contex) throws BeansException {
		System.out.println("======进入初始化ApplicationContext=====");
		if(MyApplicationContextUtil.context == null){
			MyApplicationContextUtil.context=contex;
			System.out.println("======完成初始化ApplicationContext=====");
		}
		
	}
	
	public static ApplicationContext getContext(){
		return context;
	}
	
//	public static void setContext(ApplicationContext context){
//		MyApplicationContextUtil.context = context;
//	}
	
	/**
	  * 获取对象   
	  * @param name
	  * @return Object 一个以所给名字注册的bean的实例
	  * @throws BeansException
	  */
	  public static Object getBean(String name) throws BeansException {
	    return getContext().getBean(name);
	  }
}