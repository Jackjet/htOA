package com.kwchina.oa.sys;

import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {    

	public ContextListener(){
		super();
    }

    private java.util.Timer timer = null;
    
    private java.util.Timer checkTimer = null;
   
    public void contextInitialized(ServletContextEvent event){
        System.out.println("================同步域账户定时器开启中...=================");
    	//起始时间
        Calendar theTaskCalendar = Calendar.getInstance();
        theTaskCalendar.add(Calendar.DAY_OF_YEAR, 0); //次日1点开始运行
        theTaskCalendar.set(Calendar.HOUR_OF_DAY, 12);
        theTaskCalendar.set(Calendar.MINUTE, 0);
        theTaskCalendar.set(Calendar.SECOND, 0);

        //初始化任务
        DomainTask theTask = DomainTask.getCheckLicensidTask();
        theTask.setContext(event.getServletContext());
       
        //设置定时器，每隔24小时运行一次
        timer = new java.util.Timer(true);
        //timer.schedule(theTask, theTaskCalendar.getTime(), 2 * 60 * 60 * 1000);
        timer.schedule(theTask, theTaskCalendar.getTime(), 24 * 60 * 60 * 1000);//10秒后开始执行
        System.out.println("================同步域账户定时器开启成功！=================");
        
    }

    public void contextDestroyed(ServletContextEvent event){
        timer.cancel();
        checkTimer.cancel();
    }

} 


