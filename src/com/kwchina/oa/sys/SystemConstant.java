package com.kwchina.oa.sys;



public class SystemConstant {

	//message存储文件
	//public static MessageResources MESSAGE = MessageResources.getMessageResources("messages_base");
	
	//系统用户session变量
	public static String Session_SystemUser = "_SYSTEM_USER";
	//APP平台session变量
	public static String Session_Platform = "_PLATFORM";
		
	//系统访问后缀
	public static String FILEPREFIX = ".do";		
	
	//保存呈报文的路径
	public static String Submit_Path = "/uploadfiles/submit/";
	//保存文档大全文档的路径
	public static String Document_Path = "/uploadfiles/document/";
	//保存会议信息附件的路径
	public static String Meet_path = "/uploadfiles/meet/";
	//保存日常工作安排附件的路径
	public static String ScheduleJob_path = "/uploadfiles/scheduleJob/";
	//保存工作日志附件的路径
	public static String ScheduleJobLog_path = "/uploadfiles/scheduleJobLog/";
	//保存讯息附件的路径
	public static String Message_path = "/uploadfiles/message/";
	
	//用户类型
	public static int _User_Type_Admin = 1;
	
	public static String Img_folder = "img";
	//保存用户论坛头像的路径
	public static String Photo_path = "/upload/photo/";
	//保存论坛图片附件的路径
	public static String Img_path = "/upload/img/";
	//保存论坛附件路径
	public static String Attch_path = "/upload/bbsAttch/";
	//论坛版主
	public static int _User_Type_Admin_BBS = 28;
	
}
	