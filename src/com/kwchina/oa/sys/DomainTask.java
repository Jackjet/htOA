package com.kwchina.oa.sys;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.extend.domain.util.LdapADHelper;


import java.sql.PreparedStatement;


public class DomainTask extends TimerTask { 
		 
	private DomainTask(){}
	   
	private static DomainTask _checkTask = new DomainTask();
	public static DomainTask getCheckLicensidTask(){
		return _checkTask;
	}
	   
	private boolean _isRunning  = false;
	private ServletContext context = null;
	
	public void setContext(ServletContext context){
		this.context = context;
	}

	public void run(){
		
		try{
		//if (!this._isRunning){
			this._isRunning = true;
		
		    //从容器中得到注入的bean 
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
			PersonInforManager personInforManager = (PersonInforManager)applicationContext.getBean("personInforManager");
			OrganizeManager organizeManager = (OrganizeManager)applicationContext.getBean("organizeManager");
			SystemUserManager systemUserManager = (SystemUserManager)applicationContext.getBean("systemUserManager");
			RoleManager roleManager = (RoleManager)applicationContext.getBean("roleManager");
			
			System.out.println("-------------------------开始执行域用户同步-------------------------");
			
			//定时将域中的用户同步到OA用户中
			/*
			 * 1、域中存在，OA中不存在时，在OA中新建此用户；
			 * 2、域中存在，OA中存在、但注销的用户，改为正常状态；(暂时忽略此条)
			 * 3、域中不存在，OA中存在的，忽略；
			 * 4、域中存在，OA中也存在的，更新其email；
			 */
			List<Map<String, String>> allList = new ArrayList<Map<String,String>>();
			LdapADHelper ap = new LdapADHelper();
			//得到取得的所有域用户
			List<Map<String, String>> list1 = ap.getSyncADUserInfo("公司领导");
			List<Map<String, String>> list2 = ap.getSyncADUserInfo("海通国际汽车物流有限公司");
			List<Map<String, String>> list3 = ap.getSyncADUserInfo("海通国际汽车码头有限公司");
			//List<Map<String, String>> list4 = ap.getSyncADUserInfo("客户&供应商");
			
			allList.addAll(list1);
			allList.addAll(list2);
			allList.addAll(list3);
			//allList.addAll(list4);
			
			//去重复
			allList = ArrayUtil.removeDuplicateWithOrder(allList);
			
			for(Map<String, String> map : allList){
				String userName = map.get("userName");
				String personName =  map.get("personName");
				String email = map.get("email");
				String departmentName = map.get("department");
				
				boolean isValid = true;
				String validStr = map.get("isValid");
				if(validStr.equals("true")){
					isValid = true;
				}
				if(validStr.equals("false")){
					isValid = false;
				}
				
//				System.out.println("Name:"+personName+"------------isValid:"+isValid);
				
				if(isValid){
					if(personName.contains("魏金海")){
						System.out.println(personName+"0000000000000000000000");
					}
					
					//先判断是否在OA中存在，如果不存在 ，新建OA用户，如果存在 ，则对比邮件地址，并更改（并对比部门是否更改）
					PersonInfor person = (PersonInfor)personInforManager.findPersonByName(personName);
					if(person != null){
						if(person.getEmail() == null || person.getEmail().equals("") || !person.getEmail().equals(email)){
							person.setEmail(email);
						}
						if(!person.getDepartment().getOrganizeName().equals(departmentName)){
							//寻找department
							OrganizeInfor department = (OrganizeInfor)organizeManager.findByOrganizeName(departmentName);
							//如果不存在 ，则创建新部门
							if(department == null){
								department = new OrganizeInfor();
								department.setDeleted(false);
								department.setLayer(1);
								department.setLevelId(1);
								department.setOrderNo(0);
								department.setOrganizeName(departmentName);
								department.setParent((OrganizeInfor)organizeManager.get(1));
								
								department = (OrganizeInfor)organizeManager.save(department);
							}
							person.setDepartment(department);
						}
						personInforManager.save(person);
					}else { 
						person = new PersonInfor();
						person.setPersonName(personName);
						person.setDeleted(false);
						person.setGender(0);
						person.setPositionLayer(1);
						person.setEmail(email);
						person.setEmailPassword("HTpassword1234");
						
						//寻找department
						OrganizeInfor department = (OrganizeInfor)organizeManager.findByOrganizeName(departmentName);
						//如果不存在 ，则创建新部门
						if(department == null){
							department = new OrganizeInfor();
							department.setDeleted(false);
							department.setLayer(1);
							department.setLevelId(1);
							department.setOrderNo(0);
							department.setOrganizeName(departmentName);
							department.setParent((OrganizeInfor)organizeManager.get(1));
							
							department = (OrganizeInfor)organizeManager.save(department);
						}
						person.setDepartment(department);
						
						PersonInfor newPersonInfor = (PersonInfor)personInforManager.save(person);
						
						//再新建user
						SystemUserInfor userInfor = new SystemUserInfor();
						userInfor.setFirstLogin(true);
						userInfor.setInvalidate(false);
						userInfor.setLoginTimes(0);
						if(departmentName.contains("客户&供应商")){
							userInfor.setUserType(2);
						}else {
							userInfor.setUserType(0);
						}
						userInfor.setUserName(userName);
						userInfor.setPassword("HTpassword1234");
						userInfor.setPerson(newPersonInfor);
						
						//默认角色，全体用户
						RoleInfor role = new RoleInfor();
						role = (RoleInfor)roleManager.get(1);
						Set<RoleInfor> roles = new HashSet<RoleInfor>();
						roles.add(role);
						userInfor.setRoles(roles);
						
						/*Set userSet = role.getUsers();
						userSet.add(userInfor);
						role.setUsers(userSet);
						roleManager.save(role);*/
						
						systemUserManager.save(userInfor);
						
						System.out.println("****新增用户："+personName+" -- "+departmentName);
					}
				}
				
				
			}
			
			this._isRunning = false;

			System.out.println("-------------------------域用户同步执行完成-------------------------");
		//}else{
			//context.log("上一次任务执行还未结束 ");
		//}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("-------------------------域用户同步执行出错-------------------------");
		}

	}
	
	   
	   
	public void destroy(){
		//Just puts "destroy " string in log
		//Put your code here
	}

}