package com.kwchina.oa.purchase.yiban.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseLayerInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseNode;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.exception.InstanceSuspendLayerException;
import com.kwchina.oa.workflow.exception.LayerOperateException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface PurchaseLayerInforManager extends BasicManager {




	/**
	 * 添加层次层次信息时，获取所有可以选择的FromLayer供用户选择
	 * 1. 如果已有分叉，则分叉节点前节点，不能作为fromNode
	 * 2. 聚合节点前的所有节点，均不能作为fromNode
	 * 3. 分叉节点，在分叉内，其后有后续节点，不能作为fromNode
	 * @param instance
	 * @return
	 */
	public List getFromLayers(FlowInstanceInfor instance);



	/**
	 * 保存审核层次信息
	 * @param layerInfor: 审核层次信息
	 * @param checkerIds：选择的审核人Id
	 * @param sendMessage: 是否发送手机短信
	 * @param sendMail:	是否发送邮件提醒
	 * @return
	 */
	public InstanceLayerInfor saveInstanceLayer(InstanceLayerInfor layerInfor, List fromLayers, int[] checkerIds,
                                                boolean sendMessage, boolean sendMail) throws LayerOperateException;
	
	
	/**
	 * 根据预设的FlowNode,生成审核层次信息
	 * @param node:需要生成审核层次的node
	 * @param instance: 对应的实例
	 * @param beforeLayers: 前面的审核层次
	 *  
	 */
	public void generateCheckLayer(FlowNode node, FlowInstanceInfor instance, List beforeLayers);
	
	
	
	/**
	 * 中止当前处理(一个或者即多个层次)
	 * @param layerInfor
	 * @param instance
	 */
	public void endCheckLayer(List layerInfors, FlowInstanceInfor instance)  throws InstanceSuspendLayerException;
	
	
	/**
	 * 删除审核层次
	 * @param layerInfor
	 */
//	public void deleteInstanceLayer(InstanceLayerInfor layerInfor) throws Exception;
	
	
	/**
	 * 检查该层是否全部审核完毕
	 * @param layerInfor
	 * @return
	 */
	public boolean finishedCheck(PurchaseLayerInfor layerInfor);
	
	
	//分叉内的层，根据层与层的Transition,找到分叉内,且在其之前的预设层，递归查找

	public void buildStartCheckInfor(PurchaseLayerInfor purchaseLayerInfor, PurchaseNode node);
	public void buildCheckInfor(HttpServletRequest request, PurchaseLayerInfor purchaseLayerInfor, PurchaseNode node);

}
