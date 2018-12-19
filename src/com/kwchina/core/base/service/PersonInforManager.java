package com.kwchina.core.base.service;

import java.util.List;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.common.service.BasicManager;


public interface PersonInforManager extends BasicManager{
	//获取某个部门的人员
	public List getDepartmentPersons(int departmentId);
	
	//获取某个班组的人员
	public List getGroupPersons(int groupId);
	
	//根据岗位获取人员
	public List getStructurePersons(Integer structureId);
	
	//获取某个组织节点下的员工
	public List getOrganizePersons(int organizeId);
	
	//获取生日为某月某天的所有人员
	public List getBirthdayPersons(final int month, final int day);
	
	//按照名字排序
	public List getPersonOrderByName();
	
	//取得是系统用户的人员
	public List getSystemUserPersons();
	
	//根据职级获取人员
	public List getPersonByPosition(int positionTag);
	
	//获取职级大于一定值的人员
	public List getOtherPositionPerson(int positionTag);
	
	//获取部门内大于某一职级的人员
	public List getDepartmentOtherPositionPerson(int organizeId,int positionTag);
	
	//获取某年某部门下的考核人员
	public List<PersonInfor> getRatingPersons(int dataYear, Integer departmentId);
	
	//获取所有包含手机号信息的人员
	public List getMobilePerson();
	
	//通过姓名查找人员
	public PersonInfor findPersonByName(String personName);
	
	//通过邮件查找人员
	public PersonInfor findPersonByEmail(String email);
	
	//通过部门查找人员
	public List getPersonByOrganize(Integer departmentId, Integer groupId);
	
}

