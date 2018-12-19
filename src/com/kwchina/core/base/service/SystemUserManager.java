package com.kwchina.core.base.service;

import java.util.List;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;

public interface SystemUserManager extends BasicManager{
	//获取全体
	public List getAll();
	
	//获取全部未注销用户
	public List getAllValid();
	
	//通过用户名寻找所有用户
    public SystemUserInfor findSystemUserByName(String userName);
    
    //通过用户名寻找有效用户
    public List findAllUserByName(String userName,int personId);
    
    //通过用户名和密码
    public SystemUserInfor findUser(String userName, String password);
    
    //用户登录时验证
	public SystemUserInfor checkUser(String userName, String password) throws Exception;
    
    public void saveUser(SystemUserInfor user) throws Exception ;
    
    //按照personName排序
    public List findUserOrderByPersonName();
    
    //根据职级获取用户
	public List getUserByPosition(int positionTag);
	
	//获取职级大于一定值的用户
	public List getOtherPositionUser(int positionTag);
	
    //通过部门或班组查找用户信息
    public List getUserByOrganize(Integer departmentId, Integer groupId);
    
    //通过岗位查找用户信息
    public List getUserByStructure(Integer structureId);
	
}
