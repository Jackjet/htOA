package com.kwchina.core.base.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.SystemUserInforDAO;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service("systemUserManager")
public class SystemUserManagerImpl extends BasicManagerImpl<SystemUserInfor> implements SystemUserManager{

	private SystemUserInforDAO systemUserDAO;

	@Autowired
	public void setSystemUserDAO(SystemUserInforDAO systemUserDAO) {
		this.systemUserDAO = systemUserDAO;
		super.setDao(systemUserDAO);
	}
	
	
	public List getAll(){
		return findUserOrderByPersonName();
	}
	
	//获取全部未注销用户
	public List getAllValid(){
		
		String sql = "from SystemUserInfor systemUser where systemUser.invalidate = 0 " +
		"and systemUser.person.deleted = 0 order by systemUser.person.personName";
		List list = this.systemUserDAO.getResultByQueryString(sql);
		
		return list;
	}
	
	public void saveUser(SystemUserInfor systemUser) throws Exception {
		/**如果是新增,需要判断
			1. 该员工是否已建用户信息(x)	
			2. 用户名是否重复
		 */		
		
		Integer personId = systemUser.getPersonId();
		String userName = systemUser.getUserName();
		
		boolean has = false;
		
		/** @todo look following sentence */
		if (personId != null && personId.intValue() != 0) {
			List ls = findAllUserByName(userName,personId);
			for(Iterator it = ls.iterator();it.hasNext();){
				SystemUserInfor tempUser = (SystemUserInfor)it.next();
				int tempUserId = tempUser.getPersonId();
				if(tempUserId != personId){
					has = true;
					break;
				}
			}
		}	
		
		if(has){			
			throw new ServiceException("该用户名已存在!");			
		}	

		try{
			this.systemUserDAO.save(systemUser);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		
	}

	
	//通过用户名寻找有效用户
	public List findAllUserByName(String userName,int personId) {
		String sql = "from SystemUserInfor systemUser where systemUser.invalidate = 0";
		sql += " and systemUser.userName = '" + userName + "' and systemUser.personId != " + personId;

		List list = this.systemUserDAO.getResultByQueryString(sql);
		return list;
	}

	//通过用户名寻找所有用户
	public SystemUserInfor findSystemUserByName(String userName) {
		
		String sql = "from SystemUserInfor systemUser where systemUser.userName = '" + userName + "'";
		List list = this.systemUserDAO.getResultByQueryString(sql);

		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (SystemUserInfor) list.get(0);
		} else {
			return null;
		}
	}
	
	//通过用户名和密码
    public SystemUserInfor findUser(String userName, String password){
    	
    	String sql = "from SystemUserInfor systemUser where systemUser.userName = '" + userName + "' and systemUser.password = '" + password + "'";
    	List list = this.systemUserDAO.getResultByQueryString(sql);

		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (SystemUserInfor) list.get(0);
		} else {
			return null;
		}
    }
    
    //用户登录时验证
	public SystemUserInfor checkUser(String userName, String password)
			throws Exception {

		if(userName!=null){
			int i = userName.indexOf(" ");
			if(i!=-1){
				throw new ServiceException("用户名不正确!(第"+(i+1)+"位有空格)");
			}
		}
		//判断用户名
		SystemUserInfor systemUserInfor = findSystemUserByName(userName);
		if (systemUserInfor == null) {
			throw new ServiceException("该用户名不存在!");
		}

		//判断用户是否注销
		if (systemUserInfor.isInvalidate()) {
			throw new ServiceException("该用户已注销!");
		}

		//判断用户名,密码
		if (!systemUserInfor.getPassword().equals(password)) {
			throw new ServiceException("密码不正确!");
		}

		return this.findUser(userName, password);
	}
    
    
    //按照personName排序
    public List findUserOrderByPersonName(){
    	
    	String sql = "from SystemUserInfor systemUser order by systemUser.person.personName";
    	List list = this.systemUserDAO.getResultByQueryString(sql);
		
		return list;
    }
    
    //根据职级获取用户
	public List getUserByPosition(int positionTag){
		return this.systemUserDAO.getUserByPosition(positionTag);
	}
	
	//获取职级大于一定值的用户
	public List getOtherPositionUser(int positionTag){
		return this.systemUserDAO.getOtherPositionUser(positionTag);
	}
	
    //通过部门或班组查找用户信息
    public List getUserByOrganize(Integer departmentId, Integer groupId) {
    	
    	String sql = "from SystemUserInfor systemUser where systemUser.invalidate = 0";
    	
    	//部门Id
    	if (departmentId != null && departmentId.intValue() > 0) {
    		sql += " and (systemUser.person.department.organizeId = " + departmentId 
    		+ " or systemUser.person.group.organizeId in(select organize.organizeId from OrganizeInfor organize where organize.parent.organizeId = " + departmentId + "))";
    	}
    	
    	//班组Id
    	if (groupId != null && groupId.intValue() > 0) {
    		sql += " and systemUser.person.group.organizeId = " + groupId;
    	}

		List list = this.systemUserDAO.getResultByQueryString(sql);
		return list;
    	
    }
    
    //通过岗位查找用户信息
    public List getUserByStructure(Integer structureId) {
    	
    	String sql = "from SystemUserInfor systemUser where systemUser.invalidate = 0";
    	
    	//岗位Id
    	if (structureId != null && structureId.intValue() > 0) {
    		sql += " and systemUser.person.structure.structureId = " + structureId;
    	}

		List list = this.systemUserDAO.getResultByQueryString(sql);
		return list;
    	
    }
	
}
