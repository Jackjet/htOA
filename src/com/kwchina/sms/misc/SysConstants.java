package com.kwchina.sms.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SysConstants {

	/**MS SQLServer数据库连接参数*/
	public static String msURL;				//连接字符串
	public static String msDriver;			//数据库驱动
	public static String msUser;			//用户名
	public static String msPassword;		//密码
	
	/**Oracle SQLServer数据库连接参数*/
	public static String oracleURL;			//连接字符串
	public static String oracleDriver;		//数据库驱动
	public static String oracleUser;		//用户名
	public static String oraclePwd;			//密码


	/**短信猫连接参数*/
	public static String portName;			//端口号
	public static int baudrate;				//波特率
	public static String gatewayId;			//网关名
	public static boolean inboudable;		//是否接收短信		
	
	//卡内余额
	public static String warnOfBalance_No;	//余额提醒手机号	
	public static float minBalance;			//最低余额值
	
	//public static final Logger logger = Logger.getLogger("com.kwchina");
	
	/**初始化各基本参数*/
	public static boolean initializeCONST() {
		Properties prop = new Properties();
		try {
			InputStream reader = SysConstants.class.getResourceAsStream("/com/kwchina/sms/config/sms.conf");
			prop.load(reader);
			reader.close();
		} catch (IOException e) {
			//logger.error("-----读取配置文件错误-----" + e.toString());
			//logger.error(e.getMessage());
			return false;
		}
		if (!prop.isEmpty()) {
			msURL = prop.getProperty("msURL");
			msDriver = prop.getProperty("msDriver");
			msUser = prop.getProperty("msUser");
			msPassword = prop.getProperty("msPassword");
			
			oracleURL = prop.getProperty("oracleURL");
			oracleDriver = prop.getProperty("oracleDriver");
			oracleUser = prop.getProperty("oracleUser");
			oraclePwd = prop.getProperty("oraclePwd");

			portName = prop.getProperty("portName");
			baudrate = Integer.parseInt(prop.getProperty("baudrate"));
			gatewayId = prop.getProperty("gatewayId");
			inboudable = prop.getProperty("inboudable").equalsIgnoreCase("true");

			
			warnOfBalance_No = prop.getProperty("warnOfBalance_No");
			minBalance = Float.parseFloat(prop.getProperty("minBalance"));
			
			//logger.info("系统参数正确初始化.");
		}
		
		return true;
	}
}
