package com.kwchina.core.base.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.PersonInforDAO;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service("personInforManager")
public class PersonInforManagerImpl extends BasicManagerImpl<PersonInfor> implements PersonInforManager{
	
	private PersonInforDAO personDAO;
	
	@Autowired
	private SystemUserManager systemUserManager;
	 
	@Autowired
    public void setPersonDAO(PersonInforDAO personDAO){
   	 this.personDAO = personDAO;
        super.setDao(personDAO);  
    }
	
        
    //按照名字排序
	public List getPersonOrderByName(){
		
		String sql = "from PersonInfor person where person.deleted = 0 order by person.personName";
		List list = this.personDAO.getResultByQueryString(sql);
		
		return list;
	}
	
	//取得是系统用户的人员
	public List getSystemUserPersons(){
		List returnLs = new ArrayList();
		
		List allUsers = this.systemUserManager.findUserOrderByPersonName();
		/**@todo change the following sentence*/
		//List allPersons = this.getPersonOrderByName();
		
		for(Iterator it = allUsers.iterator();it.hasNext();){
			SystemUserInfor user = (SystemUserInfor)it.next();
			returnLs.add(user.getPerson()); 
		}
		
		//按照personName排序
		//Collections.sort(returnLs, new BeanComparator("personNo"));	
		
		return returnLs;
	}
	
    //获取某个部门的人员
	public List getDepartmentPersons(int departmentId){
		
		String sql = "from PersonInfor person where person.department.organizeId = " + departmentId + " order by person.personName";
		List list = this.personDAO.getResultByQueryString(sql);
		
		return list;
	}
	
	
	//获取某个班组的人员
	public List getGroupPersons(int groupId){
		
		String sql = "from PersonInfor person where person.group.organizeId = " + groupId;
		List list = this.personDAO.getResultByQueryString(sql);
		
		return list;
	}
	
	//根据岗位获取人员
	public List getStructurePersons(Integer structureId){
		
		String sql = "from PersonInfor person where person.structure.structureId = " + structureId;
		List list = this.personDAO.getResultByQueryString(sql);
		
		return list;
	}
	
	//获取某个组织节点下的员工
	public List getOrganizePersons(int organizeId){
		
		String sql = "from PersonInfor person where person.department.organizeId = " + organizeId + " or person.group.organizeId = " + organizeId;
		List list = this.personDAO.getResultByQueryString(sql);
		
		return list;
	}
	
	
	//获取生日为某月某天的所有人员
	public List getBirthdayPersons(final int month, final int day){	
		
		String sql = "from PersonInformation person where month(person.birthday) = " + month + " and day(person.birthday) = " + day;
		List list = this.personDAO.getResultByQueryString(sql);
		
		return list;
		
	}
	
	//根据职级获取人员
	public List getPersonByPosition(int positionTag) {
		return this.personDAO.getPersonByPosition(positionTag);
	}
	
	//获取职级大于一定值的人员
	public List getOtherPositionPerson(int positionTag) {
		return this.personDAO.getOtherPositionPerson(positionTag);
	}
	
	//获取部门内大于某一职级的人员
	public List getDepartmentOtherPositionPerson(int organizeId,int positionTag){
		return this.personDAO.getDepartmentOtherPositionPerson(organizeId, positionTag);
	}

	public List<PersonInfor> getRatingPersons(int dataYear, Integer departmentId) {
		String querySQL = "from PersonInfor infor where infor.personId in";
		String condition = "(select person.personId from YearCheckDefine define join define.ratingPerson person ";
		condition += " where define.isDeleted = 0 and define.ratedPerson.dataYear = " + dataYear;
		if (departmentId != null && departmentId.intValue() > 0) {
			condition += " and person.department.organizeId = " + departmentId;
		}
		condition += " group by person.personId)";
		querySQL += condition + " order by infor.personNo";
		List<PersonInfor> persons = this.personDAO.getResultByQueryString(querySQL);
		return persons;
	}
	
	//获取所有包含手机号信息的人员
	public List getMobilePerson() {
		String querySQL = "from PersonInfor personInfor where personInfor.mobile is not null order by personName";
		List<PersonInfor> persons = this.personDAO.getResultByQueryString(querySQL);
		return persons;
	}
	
	//通过姓名查找人员
	public PersonInfor findPersonByName(String personName) {
		
		String queryString = "from PersonInfor person where person.personName = '" + personName + "'";
		List list = this.personDAO.getResultByQueryString(queryString);

		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (PersonInfor) list.get(0);
		} else {
			return null;
		}
	}
	
	//通过邮件查找人员
	public PersonInfor findPersonByEmail(String email) {
		
		String queryString = "from PersonInfor person where person.email = '" + email + "'";
		List list = this.personDAO.getResultByQueryString(queryString);

		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (PersonInfor) list.get(0);
		} else {
			return null;
		}
	}
	
	
	//通过部门或班组查找人员信息
    public List getPersonByOrganize(Integer departmentId, Integer groupId) {
    	
    	String sql = "from PersonInfor person where  person.deleted = 0";
    	
    	//部门Id
    	if (departmentId != null && departmentId.intValue() > 0) {
    		sql += " and (person.department.organizeId = " + departmentId 
    		+ " or person.group.organizeId in(select organize.organizeId from OrganizeInfor organize where organize.parent.organizeId = " + departmentId + "))";
    	}
    	
    	//班组Id
    	if (groupId != null && groupId.intValue() > 0) {
    		sql += " and person.group.organizeId = " + groupId;
    	}

		List list = this.personDAO.getResultByQueryString(sql);
		return list;
    	
    }
}
