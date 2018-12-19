package com.kwchina.sms.misc;

import static com.kwchina.sms.misc.SysConstants.baudrate;
import static com.kwchina.sms.misc.SysConstants.gatewayId;
import static com.kwchina.sms.misc.SysConstants.inboudable;
import static com.kwchina.sms.misc.SysConstants.initializeCONST;
import static com.kwchina.sms.misc.SysConstants.portName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import cn.sendsms.SMSLibException;
import cn.sendsms.Service;
import cn.sendsms.modem.ModemGateway;
import cn.sendsms.modem.SerialModemGateway;

public class SMSService {

	private static Service service = null;

	private static Scheduler scheduler = null;
	
	private static ModemGateway gateway = null;
	
	public static void main(String[] args) {

		initializeCONST();
		//service = new Service(Logger.getLogger("cn.sendsms"));
		service = new Service();
		service.getLogger().setLevel(Level.ERROR);
		initializeService();
		
		
		//logger.info("短信猫启动中...");
		try {
			service.startService();// 启动服务(连接到所有已定义的设备通道)
			//logger.info("成功启动短信猫！");
		} catch (Exception e) {
			if (e instanceof IOException || e instanceof SMSLibException || e instanceof InterruptedException) {
				//logger.error("短信猫启动失败：" + e.getMessage());
			}
		}
		
		
		//logger.info("定时器启动中...");
		try {
			//得到quartz配置文件的目录   
	       // String quartzConfigPath = StringUtils.substringBeforeLast(quartzConfigFileName, "/");   
	  
	        // 加载quartz配置文件
			String quartzConfigFileName = "E:\\myWorkSpace\\htOA\\src\\com\\kwchina\\sms\\config\\quartz.properties";
			FileInputStream fileInputStream  = new FileInputStream(quartzConfigFileName);
	        Properties properties = new Properties();   
	        try {   
	        	
	            properties.load(fileInputStream);  
	  
	            // 设置quartz_jobs.xml路径   
	            properties.setProperty("org.quartz.plugin.jobInitializer.fileName","E:/myWorkSpace/htOA/src/com/kwchina/sms/config/" + "quartz_jobs.xml");   
	  
	        } catch (IOException e) {   
	            //logger.error("加载quartz配置文件失败,文件名:" + quartzConfigFileName);   
	            return;   
	        }   

	        StdSchedulerFactory factory = new StdSchedulerFactory(properties); 

			scheduler = factory.getScheduler();
			scheduler.start();// 启动Quartz调度器
			//logger.info("成功启动定时器！");
		} catch (SchedulerException se) {
			//logger.error("定时器初始化失败: " + se.toString());
			System.out.println("error1:"+se.toString());
		}catch(FileNotFoundException ex){
			//
			System.out.println("error1:"+ex.toString());
		}
	}

	/**初始化短信猫*/
	private static void initializeService() {
		// 创建串口GSM modem连接通道
		gateway = new SerialModemGateway(gatewayId, portName, baudrate, "wavecom", "M1306B");
		gateway.setSimPin("0000");
		gateway.setInbound(inboudable);// 设置通道是否接收短信
		gateway.setOutbound(true);// 设置通道是否可以发送短信
		gateway.setCallNotification(new CallNotification());// 设置电话打入后调用方法
		gateway.setInboundNotification(new InboundNotification());// 设置短信到达后调用方法
		gateway.setOutboundNotification(new OutboundNotification());// 设置短信发送后调用方法
		// 增加短信通道到服务对象
		service.addGateway(gateway);// 如果有多个设备，可以依次添加到服务对象
		//logger.info("短信猫参数初始化成功.");
	}

	public static Service getService() {
		return service;
	}
	
	public static ModemGateway getGateway() {
		return gateway;
	}
}
