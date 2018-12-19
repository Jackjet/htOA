package com.kwchina.core.sys;

public class CoreConstant {
	
	//用于分割字符串的符号
	public static final String SPLIT_SIGN = ";";
	//编码形式
	public static final String ENCODING = "utf-8";
	//字符编码
	public static final String CONTENT_TYPE = "text/html;charset=" + ENCODING;
	
	//OrganizeInfor层级定义
	public static int Core_Organize_Level_Department = 1;	//部门
	public static int Core_Organize_Level_Group = 2;		//班组
	
	//查询排序变量
	public static String Order_By_Asc = "ASC";
	public static String Order_By_Desc = "DESC";
	
	//组织结构,岗位,系统资源,资讯分类默认开始编号
	public static int Structure_Begin_Id = 1;
	public static int Organize_Begin_Id = 1;
	public static int Resource_Begin_Id = 1;
	public static int InforCategory_Begin_Id = 1;
	
	//系统ContextPath
	public static String Context_Name = "";  //如"/oa"
	
	//context real path
	public static String Context_Real_Path = "";
	
	//附件存放路径
	public static String Attachment_Path = "upload/";
	
	//档案系统文书档案待归档附件
	public static String Administration_EveFile_Path = "/attach/oa/";
	
	/** 角色定义-常用审核角色：
	 * 1.公司领导;
	 * 2.部门经理;
	 * 3.党群;
	 * 4.人事;
	 * 5.备选审核角色一;
	 * 6.备选审核角色二.
	 *  */

	public static int Role_Checker_Leader = 17;		//公司管理层
	public static int Role_Checker_DepManager = 18;	//部门经理
	public static int Role_Checker_Party = 19;//党群
	public static int Role_Checker_HR = 20;	//人事
	public static int Role_Checker_BackupOne = 21; //备选审核角色一
	public static int Role_Checker_BackupTwo = 22; //备选审核角色二
	public static int Role_Checker_Law = 26;	//法务
	public static int Role_Leader_Party = 31;  //党群工作领导
	public static int Role_Leader_Manager = 39;  //责任部门审核人
	
	public static int Role_Manager_Above = 40;//部门总监助理及以上人员（用于工作督办选择部门负责人使用）

	public static int Role_Checker_FileManager = 47;  //文件流转审核人
	public static int Role_Checker_QDManager = 48;		//安质部审核
	public static int Role_Checker_DivisionLeader = 49;//分管领导
	public static int Role_Checker_LeaderFile = 50;//公司领导



	//投票问卷的操作人角色
	public static int Role_Topic_Operater = 27;
	
	//默认从总公司发文接收的，成为收文的创建人
	public static int User_Receive = 1;
	//默认部门审核人
	public static int User_Receive_Manager = 1;
	
	//信息发布类默认作者
	public static int User_Infor_Author = 1;
	
}
