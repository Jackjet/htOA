package com.kwchina.oa.workflow.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.exception.InstanceDeleteException;

public interface FlowInstanceManager extends BasicManager {
	/**
	 * 保存流程实例
	 * @param instance
	 * @return
	 */
	public FlowInstanceInfor saveFlowInstance(FlowInstanceInfor instance); 
	
			
	
	/**
	 * 判断是否可以删除流程
	 * 1: 申请者尚未提交时，可以删除
	 * 2. 如果用户是主办者，任何时候均可以删除
	 * @param instance
	 * @param user
	 * @return
	 */
	public boolean canDeleteFlowInstance(FlowInstanceInfor instance,SystemUserInfor user);
	
	
	/**
	 * 判断用户是否可以浏览该审核实例
	 * @param instance
	 * @param user
	 * @return
	 */
	public boolean canViewFlowInstance(FlowInstanceInfor instance,SystemUserInfor user);
	
	
	/**
	 * 申请人提交申请到审核环节,设定实例的节点,审核人等
	 * @param instance
	 */
	public FlowInstanceInfor startInstance(FlowInstanceInfor instance);
	
	
	/**
	 * 删除流程实例
	 * @param instance
	 */
	public void deleteInstance(FlowInstanceInfor instance,SystemUserInfor user) throws InstanceDeleteException;
	
	
	/**
	 * 判断是否可以添加审核层次
	 * @param instance
	 * @param user
	 * @return
	 */
	public boolean canAddLayerInfor(FlowInstanceInfor instance,SystemUserInfor user);
	
	
	/**
	 * 审核动作发生,执行如下程序,自动到下一个节点 
	 * 1.如果为分叉内层次 
	 * 	 A. 所有子token的下一节点都一样,则直接跳转到预设的下一节点(即聚合节点)
	 *   B. 子token的下一节点存在不一样的情况,即既包含分叉内节点,又包含聚合节点：
	 * 		(a).用户选择对应的审核层跳转到下一分叉内节点;
	 * 		(b).用户选择跳转到聚合节点,则将所有审核层(包括预设的和手动的)都聚合到该节点;
	 * 2. 不是分叉内的节点,取当前Tokens中的主token,跳转到其下一预设节点
	 * 
	 * @param layerInfor
	 */
	public void flowToNextNode(InstanceLayerInfor layerInfor);
	
	
	/**
	 * 获取一个实例当前处理层
	 * @param instance
	 * @return
	 */
	public List getCurrentProcessLayers(FlowInstanceInfor instance);
	
	
	/**
	 * 获取一个实例所有处理层
	 * @param instance
	 * @return
	 */
	public List getAllProcessLayers(FlowInstanceInfor instance);
	
	/**
	 * 自动到下一层之前的验证
	 * @param instance 审核实例
	 * @return map
	 * @author huangzhen
	 */
	public Map<String, Object> nextValidate(FlowInstanceInfor instance);
	
	
	/** 用户权限判断(判断用户在流程模块内的对应权限)
	 * @param request
	 * @param method 方法名
	 * @param flowId 流程Id
	 * @param systemUser 系统用户
	 * */
	public boolean judgeRight(HttpServletRequest request, String method, int flowId, SystemUserInfor systemUser);

	/**
	 * 得到审核实例当前具有浏览权限的人员
	 * @param instance
	 * @return
	 */
	public List getViewUsers(FlowInstanceInfor instance);
	
	
	/** 判断是否当前审核人
	 * @param instance 实例信息
	 * @param systemUser 当前用户
	 * @return tmpCheck 当前审核层
	 * */
	public InstanceCheckInfor isChecker(FlowInstanceInfor instance, SystemUserInfor systemUser);
	
	/**
	 * 获取待办信息（用于首页显示）
	 * @param user
	 * @return
	 */
	public Map<String, Object> getNeedDealInstances(SystemUserInfor systemUser);
	
	
	/**
	 * 获取需要推送的待办信息
	 * @param systemUser
	 * @return
	 */
	public List<FlowInstanceInfor> getNeedPushInstances(SystemUserInfor systemUser,Timestamp cutTime);
}
