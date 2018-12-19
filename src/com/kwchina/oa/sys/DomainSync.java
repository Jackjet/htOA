package com.kwchina.oa.sys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.extend.domain.util.LdapADHelper;

@Controller
public class DomainSync {
	@Resource
	private PersonInforManager personInforManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private RoleManager roleManager;

	public static final Logger log = Logger.getLogger(DomainSync.class);
	public static Logger log2 = Logger.getLogger("logger2");

	
	/**
	 * 同步域用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/domainSync.do")
	public void domainSync(HttpServletRequest request, HttpServletResponse response) {
		try {
			SimpleDateFormat detailSf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("-------------------------开始执行域用户同步-------------------------");
			log2.info("-------------------开始执行域用户同步----------------------");
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
				
				if(isValid){
//					if(personName.contains("李娜")){
//						System.out.println(personName+"0000000000000000000000");
//					}
					
					//查找对应的用户信息，并做相应更改
					SystemUserInfor user = systemUserManager.findSystemUserByName(userName);
					if(user != null){
						if(user.isInvalidate()){
							user.setInvalidate(false);
							systemUserManager.save(user);
							log.info("还原OA存在人员--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
							log2.info("还原OA存在人员--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
						}
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
							log.info("还原OA存在人员--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
							log2.info("还原OA存在人员--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
						}
						person.setDeleted(false);
						personInforManager.save(person);
						
						//新建用户
						if(person.getUser() == null){
							SystemUserInfor tmpUser = new SystemUserInfor();
							tmpUser.setFirstLogin(true);
							tmpUser.setInvalidate(false);
							tmpUser.setLoginTimes(0);
							if(departmentName.contains("客户&供应商")){
								tmpUser.setUserType(2);
							}else {
								tmpUser.setUserType(0);
							}
							
							tmpUser.setUserName(userName);
							tmpUser.setPassword("HTpassword1234");
							tmpUser.setPerson(person);
							
							//默认角色，全体用户
							RoleInfor role = new RoleInfor();
							role = (RoleInfor)roleManager.get(1);
							
							Set userSet = role.getUsers();
							userSet.add(tmpUser);
							role.setUsers(userSet);
							this.roleManager.save(role);
							
							//systemUserManager.save(userInfor);
							log.info("添加OA不存在用户--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
							log2.info("添加OA不存在用户--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
							System.out.println("****新增用户："+personName+" -- "+departmentName);
							log2.info("****新增用户："+personName+" -- "+departmentName);
						}
					}else {
						log.info("添加OA不存在人员--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
						log2.info("添加OA不存在人员--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
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
						
						//查找对应的用户信息，并做相应更改
						userInfor = systemUserManager.findSystemUserByName(userName);
						if(userInfor == null){
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
							/*Set<RoleInfor> roles = new HashSet<RoleInfor>();
							roles.add(role);
							userInfor.setRoles(roles);*/
							
							Set userSet = role.getUsers();
							userSet.add(userInfor);
							role.setUsers(userSet);
							this.roleManager.save(role);
							
							//systemUserManager.save(userInfor);
							log.info("添加OA不存在用户--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
							log2.info("添加OA不存在用户--  " + personName + "（" + departmentName + "）  --，时间：" + detailSf.format(new java.util.Date()));
							System.out.println("****新增用户："+personName+" -- "+departmentName);
							log2.info("****新增用户："+personName+" -- "+departmentName);
						}
						
						
					}
				}

					
				
			}
			System.out.println("-------------------------域用户同步执行完成-------------------------");
			log2.info("-------------------域用户同步执行完成----------------------");
			
			//return "success";
			response.getWriter().print("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("-------------------------域用户同步执行出错-------------------------");
			log2.info("-------------------域用户同步执行出错----------------------");
			//return "fail";
			try {
				response.getWriter().print("fail");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}

