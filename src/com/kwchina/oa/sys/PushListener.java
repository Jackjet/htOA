package com.kwchina.oa.sys;

import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PushListener implements ServletContextListener {    

	public PushListener(){
		super();
    }

    private java.util.Timer timer = null;
   
    public void contextInitialized(ServletContextEvent event){
        System.out.println("================待办推送定时器开启中...=================");
    	/*//起始时间
        Calendar theTaskCalendar = Calendar.getInstance();
        theTaskCalendar.add(Calendar.DAY_OF_YEAR, 0); //次日1点开始运行
        theTaskCalendar.set(Calendar.HOUR_OF_DAY, 12);
        theTaskCalendar.set(Calendar.MINUTE, 0);
        theTaskCalendar.set(Calendar.SECOND, 0);*/

        //初始化任务
        PushTask theTask = PushTask.getCheckLicensidTask();
        theTask.setContext(event.getServletContext());
       
        //设置定时器
        timer = new java.util.Timer(true);
        timer.schedule(theTask, 60 * 1000, 2 * 60 * 1000);//1分钟后开始执行，每2分钟执行一次
        System.out.println("================待办推送定时器开启成功！=================");
        
    }

    public void contextDestroyed(ServletContextEvent event){
        timer.cancel();
    }

} 


