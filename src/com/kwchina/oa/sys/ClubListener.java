package com.kwchina.oa.sys;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ClubListener implements ServletContextListener {    

	public ClubListener(){
		super();
    }

    
    private java.util.Timer beginSignTimer = null;   //报名开始
    private java.util.Timer endSignTimer = null;     //报名结束
    private java.util.Timer beginTaskTimer = null;   //活动开始
    private java.util.Timer endTaskTimer = null;     //活动结束
   
    public void contextInitialized(ServletContextEvent event){
        System.out.println("================自动更新活动状态定时器开启中...=================");
        
        
        /*************************报名开始*************************/
    	//起始时间
        Calendar beginSignCalendar = Calendar.getInstance();
        beginSignCalendar.add(Calendar.DAY_OF_YEAR, 1); //次日1点开始运行
        beginSignCalendar.set(Calendar.HOUR_OF_DAY, 1);
        beginSignCalendar.set(Calendar.MINUTE, 0);
        beginSignCalendar.set(Calendar.SECOND, 0);

//        Date date = beginSignCalendar.getTime();
        
        //初始化任务
        ClubSignBeginTask signBeginTask = ClubSignBeginTask.getCheckLicensidTask();
        signBeginTask.setContext(event.getServletContext());
       
        //设置定时器，每隔24小时运行一次
        beginSignTimer = new java.util.Timer();
        beginSignTimer.schedule(signBeginTask, beginSignCalendar.getTime(), 24 * 60 * 60 * 1000);
        
        
        
        /*************************报名结束*************************/
    	//起始时间
        Calendar endSignCalendar = Calendar.getInstance();
        endSignCalendar.add(Calendar.DAY_OF_YEAR, 0); //23点开始运行
        endSignCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endSignCalendar.set(Calendar.MINUTE, 0);
        endSignCalendar.set(Calendar.SECOND, 0);

        //初始化任务
        ClubSignEndTask signEndTask = ClubSignEndTask.getCheckLicensidTask();
        signEndTask.setContext(event.getServletContext());
       
        //设置定时器，每隔24小时运行一次
        endSignTimer = new java.util.Timer();
        endSignTimer.schedule(signEndTask, endSignCalendar.getTime(), 24 * 60 * 60 * 1000);
        
        
        
        /*************************活动开始*************************/
    	//起始时间
        Calendar beginTaskCalendar = Calendar.getInstance();
        beginTaskCalendar.add(Calendar.DAY_OF_YEAR, 1); //1点开始运行
        beginTaskCalendar.set(Calendar.HOUR_OF_DAY, 1);
        beginTaskCalendar.set(Calendar.MINUTE, 0);
        beginTaskCalendar.set(Calendar.SECOND, 0);

        //初始化任务
        ClubBeginTask beginTask = ClubBeginTask.getCheckLicensidTask();
        beginTask.setContext(event.getServletContext());
       
        //设置定时器，每隔半小时运行一次
        beginTaskTimer = new java.util.Timer(true);
        //timer.schedule(theTask, theTaskCalendar.getTime(), 2 * 60 * 60 * 1000);
//        endTaskTimer.schedule(beginTask, beginTaskCalendar.getTime(), 30 * 60 * 1000);
        beginTaskTimer.schedule(beginTask, 30 * 1000, 30 * 60 * 1000);
        
        
        
        /*************************活动结束*************************/
    	//起始时间
        Calendar endTaskCalendar = Calendar.getInstance();
        endTaskCalendar.add(Calendar.DAY_OF_YEAR, 0); //23点开始运行
        endTaskCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endTaskCalendar.set(Calendar.MINUTE, 0);
        endTaskCalendar.set(Calendar.SECOND, 0);

        //初始化任务
        ClubEndTask endTask = ClubEndTask.getCheckLicensidTask();
        endTask.setContext(event.getServletContext());
       
        //设置定时器，每隔24小时运行一次
        endTaskTimer = new java.util.Timer();
        //timer.schedule(theTask, theTaskCalendar.getTime(), 2 * 60 * 60 * 1000);
        endTaskTimer.schedule(endTask, endTaskCalendar.getTime(), 24 * 60 * 60 * 1000);
        
        
        System.out.println("================自动更新活动状态定时器开启成功！=================");
        
    }

    public void contextDestroyed(ServletContextEvent event){
        beginSignTimer.cancel();
        endSignTimer.cancel();
        beginTaskTimer.cancel();
    	endTaskTimer.cancel();
    }

} 


