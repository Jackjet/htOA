package com.kwchina.oa.workflow.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.sys.PushUtil;
import com.kwchina.oa.workflow.dao.InstanceCheckDAO;
import com.kwchina.oa.workflow.dao.InstanceInforDAO;
import com.kwchina.oa.workflow.dao.InstanceLayerDAO;
import com.kwchina.oa.workflow.dao.InstanceTokenDAO;
import com.kwchina.oa.workflow.dao.InstanceTransitionDAO;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.entity.InstanceToken;
import com.kwchina.oa.workflow.entity.InstanceTransitionInfor;
import com.kwchina.oa.workflow.entity.NodeCheckerPerson;
import com.kwchina.oa.workflow.entity.NodeCheckerRole;
import com.kwchina.oa.workflow.exception.InstanceSuspendLayerException;
import com.kwchina.oa.workflow.exception.LayerOperateException;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.FlowLayerInforManager;
import com.kwchina.oa.workflow.service.InstanceTokenManager;


@Service
public class FlowLayerInforManagerImpl extends BasicManagerImpl<InstanceLayerInfor> implements FlowLayerInforManager {
	
	@Autowired
	private InstanceInforDAO instanceInforDAO;
	
	@Autowired
	private InstanceLayerDAO instanceLayerDAO;
	
	@Autowired
	private InstanceCheckDAO instanceCheckDAO;
	
	@Autowired
	private InstanceTransitionDAO instanceTransitionDAO;
	
	@Autowired
	private InstanceTokenDAO instanceTokenDAO;
	
	@Autowired
	private FlowInstanceManager flowInstanceManager;
	
	@Autowired
	private InstanceTokenManager instanceTokenManager;
	
	@Autowired
	private SystemUserManager systemUserManager;
	
	@Autowired
	public void setInstanceLayerDAO(InstanceLayerDAO instanceLayerDAO) {
		this.instanceLayerDAO = instanceLayerDAO;
		super.setDao(instanceLayerDAO);
	}
	
	
	/**
	 * 添加层次层次信息时，获取所有可以选择的FromLayer供用户选择
	 * 1. 如果已有分叉，则分叉节点前节点，不能作为fromNode 
	 * 2. 聚合节点前的所有节点，均不能作为fromNode 
	 * 3. 分叉节点，在分叉内，其后有后续节点，不能作为fromNode
	 * @param instance
	 * @return
	 */
	public List getFromLayers(FlowInstanceInfor instance){
		List rLayers = new ArrayList();

		int instanceId = instance.getInstanceId();
		
		List layers = this.instanceLayerDAO.findInstanceCheckLayers(instanceId);
		List transitions = this.instanceTransitionDAO.findInstanceTransition(instanceId);
		
		// 得到层次最大的分叉层,聚合层
		int maxForkLayer = 0;
		int maxJoinLayer = 0;
		for (Iterator it = layers.iterator(); it.hasNext();) {
			InstanceLayerInfor layer = (InstanceLayerInfor) it.next();
			if (layer.getForkedType() == InstanceLayerInfor.Layer_Forked_Fork && layer.getLayer() > maxForkLayer) {
				maxForkLayer = layer.getLayer();
			}

			if (layer.getForkedType() == InstanceLayerInfor.Layer_Forked_Join && layer.getLayer() > maxJoinLayer) {
				maxJoinLayer = layer.getLayer();
			}
		}

		for (Iterator it = layers.iterator(); it.hasNext();) {
			InstanceLayerInfor layer = (InstanceLayerInfor) it.next();

			// 最大聚合节点前的节点不能作为fromNode，最大分叉节点前的节点不能作为fromNode
			if (layer.getLayer() < maxJoinLayer || layer.getLayer() < maxForkLayer)
				continue;
			
			//如果该层的审核状态为尚未结束(即状态为正常或暂停),不能作为fromLayer
			if(layer.getStatus()==InstanceLayerInfor.Layer_Status_Normal || layer.getStatus()==InstanceLayerInfor.Layer_Status_Suspended)
				continue;			
			
			boolean canAdd = true;
			if (layer.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner) {
				// 分叉内的层次,其后有后续层次，不能作为fromLayer
				for (Iterator itTransition = transitions.iterator(); itTransition.hasNext();) {
					InstanceTransitionInfor transition = (InstanceTransitionInfor) itTransition.next();
					if (transition.getFromLayer() != null && transition.getFromLayer().getLayerId() == layer.getLayerId()){
						canAdd=false;
						break;
					}	
				}
			}

			if(canAdd)
				rLayers.add(layer);
		}

		return rLayers;
	}
	
	
	/**
	 * 保存审核层次信息
	 * @param layerInfor: 审核层次信息
	 * @param checkerIds：选择的审核人Id
	 * @param sendMessage: 是否发送手机短信
	 * @param sendMail:	是否发送邮件提醒
	 * @return
	 */
	public InstanceLayerInfor saveInstanceLayer(InstanceLayerInfor layerInfor,List fromLayers,int[] checkerIds,
			boolean sendMessage, boolean sendMail) throws LayerOperateException{
		
		Integer layerId = layerInfor.getLayerId();
		FlowInstanceInfor instance = layerInfor.getInstance();
		Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());
		
		if(layerId==null || layerId==0){
			//新增操作
			
			int currentLayer = 0;
			InstanceLayerInfor currentLayerInfor = null;
			/**
			 * 如果fromLayers有多个，说明是聚合层, 聚合层必须来自于同一个分叉，并且要完整
			 */
			List processLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
			if (fromLayers != null && !fromLayers.isEmpty()) {
				
				if (fromLayers.size() > 1) {
					int k = 0;
					int forkedLayerId = 0;
					int forkSize = 0;
					
					for (Iterator it = fromLayers.iterator(); it.hasNext();) {
						InstanceLayerInfor tempLayer = (InstanceLayerInfor) it.next();
						if (tempLayer.getForkedType()!=InstanceLayerInfor.Layer_Forked_ForkInner)
							// 抛出异常
							throw new LayerOperateException("Join Layer,Selected from Layers error!");

						forkSize += 1;
						k += 1;
						
						if (k > 1 && forkedLayerId != tempLayer.getForkedLayer().getLayerId())
							throw new LayerOperateException("Join Layer,Selected from Layers error!");
						else
							forkedLayerId = tempLayer.getForkedLayer().getLayerId();
						
						currentLayer = tempLayer.getLayer();
					}
					
					//完整性判断,获取有几个分叉，然后与forkSize比较
					if(processLayers.isEmpty() || processLayers.size()!=forkSize)
						throw new LayerOperateException("Join Layer,Must select all forked layers!");
				}else {
					
					InstanceLayerInfor tempLayer = (InstanceLayerInfor)fromLayers.get(0);
					currentLayer = tempLayer.getLayer();
					
					//完整性判断,看是否存在当前处理层
					if(processLayers == null ||processLayers.isEmpty())
						throw new LayerOperateException("Process Layer,Must be not empty!");
				}
				
			}else {
				/** 如果fromLayers为空,存在三种情况：
				 * A.此时为审核刚开始,没有可选审核层(不做任何程序处理);
				 * B.在审核过程中,没有可选审核层(抛出异常);
				 * C.在审核过程中,有可选审核层,但不做任何选择(判断是否存在处理中的普通或聚合审核层,存在则取其层次信息,否则抛出异常);
				 * */
				if (processLayers != null && !processLayers.isEmpty()) {
					//获取所有可以选择的审核层
					List canChooseLayers = getFromLayers(instance);
					if (canChooseLayers == null || canChooseLayers.size() == 0) {
						//情况B
						throw new LayerOperateException("Must have at least a layer finished!");
					}else {
						//情况C
						boolean hasLayer = false;
						for (Iterator it=canChooseLayers.iterator();it.hasNext();) {
							InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
							if ((layer.getForkedType() == InstanceLayerInfor.Layer_Forked_Normal || layer.getForkedType() == InstanceLayerInfor.Layer_Forked_Join)
									&& finishedCheck(layer)) {
								hasLayer = true;
								currentLayerInfor = layer;
								currentLayer = layer.getLayer();
								//break;
							}
						}
						if (!hasLayer) {
							throw new LayerOperateException("There's no normal or join layer,must selected a parent layer!");
						}
					}
				}
			}
			
			
			//设置保存的Layer相关信息			
			layerInfor.setStartTime(current);							//设置开始时间
			layerInfor.setLayerType(InstanceLayerInfor.Layer_Type_Set);	//设置层类型
			layerInfor.setLayer(currentLayer+1);
			layerInfor.setStatus(InstanceLayerInfor.Layer_Status_Normal);
			layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_Normal);
			layerInfor.setForkedLayer(null);
			
			layerInfor = (InstanceLayerInfor)this.instanceLayerDAO.save(layerInfor);
			
			//增加层与层之间的Transition
			if(fromLayers != null && !fromLayers.isEmpty()) {
				// 有起始层次,且起始只有一个
				if (fromLayers.size() == 1) {
					InstanceLayerInfor tempLayer = (InstanceLayerInfor) fromLayers.get(0);
					
					if (tempLayer.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner) {
						//如果fromLayer就是分叉内层次，设定本节点也为分叉层次
						layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
						layerInfor.setForkedLayer(tempLayer.getForkedLayer());
					}	
					
					int fromLayerId = tempLayer.getLayerId();
					List aTransitions = this.instanceTransitionDAO.findAfterLayerTransition(fromLayerId);
					if (aTransitions.size() == 1) {
						/**
						 * 如果fromLayer后有且仅有一个层次，则需要
						 * 1.更改该层为分叉层 
						 * 2.把另外一个分支更改为分叉内层次
						 */
						InstanceTransitionInfor theOtherTrans = (InstanceTransitionInfor) aTransitions.get(0);
						InstanceLayerInfor otherLayer = theOtherTrans.getToLayer();
						otherLayer.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
						otherLayer.setForkedLayer(tempLayer);						
						this.instanceLayerDAO.save(otherLayer);	
					}
					
					//如果fromNode后已经有节点，则本节点也需要设置为分叉内节点
					if(!aTransitions.isEmpty()){
						layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
						layerInfor.setForkedLayer(tempLayer);
					}
				} else {
					// 设定其聚合的相关属性
					layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_Join);					
				}
	
				// 新增Transition
				int fromIndex = 0;
				for (Iterator it = fromLayers.iterator(); it.hasNext();) {
					InstanceLayerInfor tempLayer = (InstanceLayerInfor) it.next();
					
					fromIndex += 1;
	
					InstanceTransitionInfor transition = new InstanceTransitionInfor();
					transition.setInstance(instance);
					transition.setFromIndex(fromIndex);
					transition.setFromLayer(tempLayer);
					transition.setToLayer(layerInfor);
					transition.setTransName("");
				
					this.instanceTransitionDAO.save(transition);
				}
			}else {
				/** 未选择任何审核层,存在两种情况：
				 * A.审核刚开始时,此时不需要新增Transition;
				 * B.审核过程中,未选择任何审核层,此时默认将前一审核层作为Transition的fromLayer;
				 *  */
				if (currentLayerInfor != null){
					InstanceTransitionInfor transition = new InstanceTransitionInfor();
					transition.setInstance(instance);
					transition.setFromIndex(0);
					transition.setFromLayer(currentLayerInfor);
					transition.setToLayer(layerInfor);
					transition.setTransName("");
				
					this.instanceTransitionDAO.save(transition);
				}
				
			}
			
			//为聚合层时,需要删除子Token
			if (fromLayers.size() > 1) {
				List childTokens = this.instanceTokenManager.getChildTokens(instance.getInstanceId());
				//删除子Token的信息
				for(Iterator it = childTokens.iterator();it.hasNext();){
					InstanceToken token = (InstanceToken)it.next();
					this.instanceTokenDAO.remove(token);
				}
			}
			
		}else{
			//修改的状况，需要判断是否能修改
			if(layerInfor.getStatus()==InstanceLayerInfor.Layer_Status_Finished || layerInfor.getStatus()==InstanceLayerInfor.Layer_Status_End)
				throw new LayerOperateException("Layer finished,can't modify this layer!");
		}
		
		
		/**
		 * 处理审核人信息
		 * 第一步：去掉以前存在，此次未选择，并且该审核人尚未审核
		 * 第二步：添加以前没有的审核人
		 */		
		//去掉以前有且没有审核，并且现在没有的审核人
		Set checkInfors = layerInfor.getCheckInfors();
		InstanceCheckInfor[] arrayCheck = (InstanceCheckInfor[])checkInfors.toArray(new InstanceCheckInfor[checkInfors.size()]);		
		for (int k = arrayCheck.length - 1; k >= 0; k--) {
			InstanceCheckInfor checkInfor = (InstanceCheckInfor) arrayCheck[k];
			if (checkInfor.getStatus() == InstanceCheckInfor.Check_Status_Checked)
				continue;

			int tempUserId = checkInfor.getChecker().getPersonId().intValue();
			boolean findIt = false;
			if (checkerIds != null && checkerIds.length > 0) {
				for (int j = 0; j < checkerIds.length; j++) {
					int tempId = checkerIds[j];
					if (tempId == tempUserId) {
						findIt = true;						
						break;
					}
				}
			}		
			//去掉
			if (!findIt) {
				checkInfors.remove(checkInfor);
				this.instanceCheckDAO.remove(checkInfor);
			}
			
		}
		
		//添加以前没有的审核人信息
		if (checkerIds != null && checkerIds.length > 0) {
			for (int j = 0; j < checkerIds.length; j++) {
				int tempCheckerId = checkerIds[j];

				boolean findIt = false;
				for(Iterator it=checkInfors.iterator();it.hasNext();){
					InstanceCheckInfor checkInfor = (InstanceCheckInfor) it.next();
					int tempUserId = checkInfor.getChecker().getPersonId().intValue();
					if (tempCheckerId == tempUserId) {
						findIt = true;
						break;
					}
				}

				/**@todo:需判断分叉层次中，其它审核层次有没有该人员*/
				boolean otherLayerFindIt = false;
				if (layerInfor.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner) {
					List processLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
					for(Iterator itLayer = processLayers.iterator();itLayer.hasNext();){
						InstanceLayerInfor tempLayer = (InstanceLayerInfor) itLayer.next();
						if(tempLayer.getLayerId()!=layerId){
							Set tempCheckInfors = tempLayer.getCheckInfors();
							for(Iterator itCheckInfor = tempCheckInfors.iterator();itCheckInfor.hasNext();){
								InstanceCheckInfor checkInfor = (InstanceCheckInfor)itCheckInfor.next();
								if(checkInfor.getChecker().getPersonId() == tempCheckerId){
									otherLayerFindIt = true;
									break;
								}
							}
						}
						
						if(otherLayerFindIt)
							break;
					}
				}
				
				if (!findIt && !otherLayerFindIt) {
					SystemUserInfor userInfor = (SystemUserInfor)this.systemUserManager.get(tempCheckerId);
					if (userInfor != null) {
						InstanceCheckInfor checkInfor = new InstanceCheckInfor();
						
						try {
							checkInfor.setStartDate(current);
							checkInfor.setStatus(InstanceCheckInfor.Check_Status_Unckeck);
							checkInfor.setCheckComment("");						
							checkInfor.setLayerInfor(layerInfor);
							checkInfor.setChecker(userInfor);
							
							//checkInfors.add(checkInfor);
							this.instanceCheckDAO.save(checkInfor);
							
							/***************发送app推送***************/
							List<FlowInstanceInfor> returnInstances_m = flowInstanceManager.getNeedPushInstances(userInfor, current);
							String alias = userInfor.getUserName();
							int badge = returnInstances_m.size();
							
							PushUtil pushUtil = new PushUtil();
							pushUtil.pushNeedDealInstances(instance, badge+1, alias);
							/**************发送app结束****************/
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		//保存层次信息
		this.instanceLayerDAO.save(layerInfor);		
		return layerInfor;
	}

	
	/**
	 * 中止某层次的审核,可能为中止某个层次，也可能为中止当前实例，继续向下。
	 * 如果是多个层次，则需要判断是否全部是当前处理层次，并且包括全部当前处理层次
	 * 1. 设置该层次状态为中止
	 * 2. 该层未审核的人状态设置为中止
	 * 3. 进行到下一节点的操作
	 * @param layerInfor
	 * @param instance
	 */
	public void endCheckLayer(List layerInfors, FlowInstanceInfor instance) throws InstanceSuspendLayerException{
		if(layerInfors.isEmpty())
			throw new InstanceSuspendLayerException("Suspend don't select any layers!");
		/**
		 * 判断是否可以中止这些层次
		 * 每个层次至少有一个人审核过或者该层次未设定审核人才能中止 
		 * 
		 * 添加（2016-07-07）：每层只有一个审核人且未审核时也可中止
		 */
		for(Iterator it = layerInfors.iterator();it.hasNext();){
			InstanceLayerInfor layerInfor = (InstanceLayerInfor)it.next();
			
			boolean canEnd = false;
			Set checkInfors = layerInfor.getCheckInfors();
			if (checkInfors.isEmpty()) {
				canEnd = true;
			}else {
				for(Iterator itCheck = checkInfors.iterator();itCheck.hasNext();){
					InstanceCheckInfor checkInfor = (InstanceCheckInfor)itCheck.next();
					if(checkInfor.getStatus()==InstanceCheckInfor.Check_Status_Checked){
						canEnd = true;
						//break;
					}	
					
					if(checkInfors.size() == 1 && checkInfor.getStatus() == InstanceCheckInfor.Check_Status_Unckeck){
						canEnd = true;
					}
				}
			}
			
			if(!canEnd)
				throw new InstanceSuspendLayerException();
		}
		
		
		//设置各个层次为中止状态，已经完成的则不能更改状态
		for(Iterator it = layerInfors.iterator();it.hasNext();){
			InstanceLayerInfor layerInfor = (InstanceLayerInfor)it.next();
			if(layerInfor.getStatus() == InstanceLayerInfor.Layer_Status_Finished)
				break;
			
						
			layerInfor.setStatus(InstanceLayerInfor.Layer_Status_End);
			this.instanceLayerDAO.save(layerInfor);
		}
		
		//判断流程类型为'固定'还是'人工'
		FlowDefinition flow = instance.getFlowDefinition();
		int flowType = flow.getFlowType();
		if (flowType == 0) {
			//进行到下一个节点
			//if(layerInfors.size()>=1){
			InstanceLayerInfor theLayer = (InstanceLayerInfor)layerInfors.get(0);
			this.flowInstanceManager.flowToNextNode(theLayer);
			//}
		}
		
	}
	
	/**
	 * 
	 * @param node:需要生成审核层次的node
	 * @param instance:
	 *            对应的实例
	 * 
	 */
	public void generateCheckLayer(FlowNode node, FlowInstanceInfor instance, List beforeLayers) {
		Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());
		
		// 先生成审核层次信息
		InstanceLayerInfor layerInfor = new InstanceLayerInfor();
		layerInfor.setInstance(instance);
		layerInfor.setLayerName(node.getNodeName());
		layerInfor.setCheckDemand("");
		layerInfor.setStartTime(current);
		layerInfor.setLayerType(InstanceLayerInfor.Layer_Type_PreSet);
		layerInfor.setStatus(InstanceLayerInfor.Layer_Status_Normal);
		layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_Normal);
		
		
		int maxLayer = 0; 
		if(beforeLayers!=null && !beforeLayers.isEmpty()){
			//获取前面几个层次的最大层
			for(Iterator it = beforeLayers.iterator();it.hasNext();){
				InstanceLayerInfor tempLayer = (InstanceLayerInfor) it.next();
			
				if(tempLayer.getLayer()>maxLayer)
					maxLayer = tempLayer.getLayer();
				
				if(tempLayer.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner) {
					layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
					layerInfor.setForkedLayer(tempLayer.getForkedLayer());
				}
			}
			
			
			//是否聚合
			if(beforeLayers.size()>1) {
				layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_Join);
				layerInfor.setForkedLayer(null);
			}
		}
		
		layerInfor.setLayer(maxLayer+1);
		layerInfor.setFlowNode(node);
		layerInfor = (InstanceLayerInfor)this.instanceLayerDAO.save(layerInfor);
		
		if(beforeLayers!=null && !beforeLayers.isEmpty()){
			//生成Transition信息
			List transitions = this.instanceTransitionDAO.findInstanceTransition(instance.getInstanceId());
			int k = 0;
			for(Iterator it = beforeLayers.iterator();it.hasNext();){
				InstanceLayerInfor tempLayer = (InstanceLayerInfor) it.next();
				
				/**
				 * 1.如果fromLayer的Transition数量大于0,则需要将当前分支更改为分叉内层次
				 * 2.如果fromLayer的Transition数量只有一个,则需要：
				 * 	A.更改来源层为分叉层 
				 * 	B.把已有的一个分支更改为分叉内层次
				 */
				int afterTransitionNum = 0;
				InstanceLayerInfor otherLayer = null;
				for(Iterator itTransition=transitions.iterator();itTransition.hasNext();){
					InstanceTransitionInfor tempTransition = (InstanceTransitionInfor)itTransition.next();
					if(tempTransition.getFromLayer().getLayerId() == tempLayer.getLayerId()) {
						afterTransitionNum += 1;
					}
					
					if(afterTransitionNum == 1){
						otherLayer = tempTransition.getToLayer();
					}
				}
				if(afterTransitionNum > 0){
					//将当前分支更改为分叉内层次
					layerInfor.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
					layerInfor.setForkedLayer(tempLayer);						
					this.instanceLayerDAO.save(layerInfor);
					
					if(afterTransitionNum == 1){
						//更改来源层为分叉层
						tempLayer.setForkedType(InstanceLayerInfor.Layer_Forked_Fork);
						this.instanceLayerDAO.save(tempLayer);
						
						//把已有的一个分支更改为分叉内层次
						otherLayer.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
						otherLayer.setForkedLayer(tempLayer);						
						this.instanceLayerDAO.save(otherLayer);	
					}
				}
				
				InstanceTransitionInfor transition = new InstanceTransitionInfor();
				transition.setFromIndex(k+1);
				transition.setFromLayer(tempLayer);
				transition.setToLayer(layerInfor);
				transition.setInstance(instance);
				this.instanceTransitionDAO.save(transition);
				
				k+=1;			
			}
		}
		

		// 根据节点信息，生成其审核人信息
		if (node.getCheckerType() == FlowNode.Node_CheckerType_PreSet) {
			// 预设的审核人
			Set checkPersons = node.getCheckerPersons();
			if (!checkPersons.isEmpty()) {
				for (Iterator it = checkPersons.iterator(); it.hasNext();) {
					NodeCheckerPerson checkPerson = (NodeCheckerPerson) it.next();

					// 保存审核人信息
					generateCheckInfor(layerInfor, checkPerson.getUser());
				}
			}

			Set checkRoles = node.getCheckerRoles();
			if (!checkRoles.isEmpty()) {
				for (Iterator it = checkRoles.iterator(); it.hasNext();) {
					NodeCheckerRole checkerRole = (NodeCheckerRole) it.next();
					RoleInfor role = checkerRole.getRole();
					Set users = role.getUsers();
					for (Iterator itUser = users.iterator(); itUser.hasNext();) {
						SystemUserInfor user = (SystemUserInfor) itUser.next();

						// 保存审核人信息
						generateCheckInfor(layerInfor, user);
					}
				}
			}
		} else if (node.getCheckerType() == FlowNode.Node_CheckerType_Person) {
			// 设定的为某个具体的审核人
			if (node.getUser() != null) {
				generateCheckInfor(layerInfor, node.getUser());
			}
		} else if (node.getCheckerType() == FlowNode.Node_CheckerType_Department) {
			// 设定为部门，则该部门人员为审核人
			OrganizeInfor department = node.getDepartment();
			List users = this.systemUserManager.getUserByOrganize(department.getOrganizeId(), null);
			for (Iterator it = users.iterator(); it.hasNext();) {
				SystemUserInfor user = (SystemUserInfor)it.next();
				
				// 保存审核人信息
				generateCheckInfor(layerInfor, user);
			}
		} else if (node.getCheckerType() == FlowNode.Node_CheckerType_Structrue) {
			// 设定为岗位，则查找该岗位对应的人员
			StructureInfor structure = node.getStructure();
			List users = this.systemUserManager.getUserByStructure(structure.getStructureId());
			for (Iterator it = users.iterator(); it.hasNext();) {
				SystemUserInfor user = (SystemUserInfor)it.next();
				
				// 保存审核人信息
				generateCheckInfor(layerInfor, user);
			}
		} else if (node.getCheckerType() == FlowNode.Node_CheckerType_Special) {
			// 设定为特殊审核人，则根据选择查找对应的人员
			if (node.getSpecial() == 1) {
				//部门领导者
				/** @todo */
				
			}else if (node.getSpecial() == 2) {
				//申请者
				SystemUserInfor user = instance.getApplier();
				
				// 保存审核人信息
				generateCheckInfor(layerInfor, user);
			}
		}
	}

	private void generateCheckInfor(InstanceLayerInfor layerInfor, SystemUserInfor user) {
		Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());

		InstanceCheckInfor checkInfor = new InstanceCheckInfor();
		checkInfor.setStartDate(current);
		checkInfor.setStatus(InstanceCheckInfor.Check_Status_Unckeck);
		
		checkInfor.setLayerInfor(layerInfor);
		checkInfor.setChecker(user);

		this.instanceCheckDAO.save(checkInfor);
	}

	/**
	 * 检查该层是否全部审核完毕
	 * 1. 预设的,如果允许一个人审核后即往下流转,则完成;
	 * 	  若要全部人员通过审批才向下流转,则需要对所有审核信息进行判断,必须都完成才往下流转.
	 * 2. 非预设的,必须都完成.
	 * @param layerInfor
	 * @return
	 */
	public boolean finishedCheck(InstanceLayerInfor layerInfor){
		boolean finished = true;
		
		if(layerInfor.getLayerType() == InstanceLayerInfor.Layer_Type_PreSet){
			//预设的节点
			finished = false;
			
			FlowNode node = layerInfor.getFlowNode();
			if(node.getFinishType() == 1){
				Set checkInfors = layerInfor.getCheckInfors();
				for(Iterator it = checkInfors.iterator();it.hasNext();){
					InstanceCheckInfor checkInfor = (InstanceCheckInfor)it.next();
					if(checkInfor.getStatus() == InstanceCheckInfor.Check_Status_Checked){
						return true;
					}
				}	
			}else {
				finished = allFinished(layerInfor);
			}
		}else{
			//非预设的
			/*finished = true;
			
			Set checkInfors = layerInfor.getCheckInfors();
			for(Iterator it = checkInfors.iterator();it.hasNext();){
				InstanceCheckInfor checkInfor = (InstanceCheckInfor)it.next();
				if(checkInfor.getStatus() == InstanceCheckInfor.Check_Status_Unckeck){
					return false;
				}
			}*/
			finished = allFinished(layerInfor);
		}
		
		return finished;
	}
	
	//判断审核层是否全部审核完毕
	private boolean allFinished(InstanceLayerInfor layerInfor) {
		boolean finished = true;
		
		Set checkInfors = layerInfor.getCheckInfors();
		if (checkInfors == null || checkInfors.size() == 0) {
			return false;
		}else {
			for(Iterator it = checkInfors.iterator();it.hasNext();){
				InstanceCheckInfor checkInfor = (InstanceCheckInfor)it.next();
				if(checkInfor.getStatus() == InstanceCheckInfor.Check_Status_Unckeck
						&& (layerInfor.getStatus() == InstanceLayerInfor.Layer_Status_Normal||layerInfor.getStatus() == InstanceLayerInfor.Layer_Status_Suspended)){
					return false;
				}
			}
		}
		
		return finished;
	}
	
	
	/**
	 * 删除审核层次
	 * @param layerInfor
	 */
	public void deleteInstanceLayer(InstanceLayerInfor layerInfor) throws Exception{
		if(layerInfor==null)
			throw new Exception("");
		
		int layerId = layerInfor.getLayerId();
		int layer = layerInfor.getLayer();
		List aTransitions = this.instanceTransitionDAO.findAfterLayerTransition(layerId);
		
		/*// 该层次后面有审核层次，则不能删除
		if (aTransitions.size() > 1)
			throw new LayerOperateException("Can't delete this layer,after it layers exsits!");*/
		
			/**
			 * 如果该层为预设层,则需要更新Token的信息
			 *1. 如果不是分叉内的层，则需要更新主Token(在其前的，预设的，非分叉的节点)
			 *2. 如果是分叉内的层,则：
			 *	A. 如果该节点属于子Token,其前有分叉内层，并且为预设的，则需要更新子Token的位置
			 *  B. 如果该节点属于子Token,其前没有预设的分叉层,则需要删除该子Token,且更新主Token到合适位置（这里又分子Token是2个还是多个)
			 *  C. 如果该节点属于父Token,则找到其前的一个预设层，更新主Token到该位置
			 *  D. 如果该节点属于父Token,其前没有预设层，则需要删除该主Token
			 *  
			 *   按照我们的逻辑，该审核层次必然有对应的Token,不是子Token就是父Token
			 *   并且，父Token一定不是分叉内的层，子Token一定是分叉内的层
			 *非预设层，则只有该层为Join层，可能会影响Token的信息(可能需要增加分支内子Token)
			 */
			
			FlowInstanceInfor instance = layerInfor.getInstance();
			int instanceId = instance.getInstanceId();
			List layers = this.instanceLayerDAO.findInstanceCheckLayers(instanceId);
					
			List tokens = this.instanceTokenDAO.findInstanceTokens(instanceId);
			InstanceToken mainToken = this.instanceTokenManager.getMainToken(tokens);
			List childTokens = this.instanceTokenManager.getChildTokens(tokens);
			
			
			if(layerInfor.getLayerType() == InstanceLayerInfor.Layer_Type_PreSet && layerInfor.getFlowNode()!=null){
				//预设层的删除，涉及Token的变化
				if(layerInfor.getForkedType()!=InstanceLayerInfor.Layer_Forked_ForkInner){
					//非分叉内的
					int tempLayer = 0; //第几层
					FlowNode tokenNode = null;
					for(Iterator itLayer = layers.iterator();itLayer.hasNext();){					
						InstanceLayerInfor tempLayerInfor = (InstanceLayerInfor)itLayer.next();
						if(tempLayerInfor.getLayer()<layer && tempLayerInfor.getLayer()>tempLayer && tempLayerInfor.getLayerType() == InstanceLayerInfor.Layer_Type_PreSet && tempLayerInfor.getForkedType()!=InstanceLayerInfor.Layer_Forked_ForkInner){
							if(tempLayerInfor.getFlowNode()!=null){						
								tempLayer = tempLayerInfor.getLayer();
								tokenNode = tempLayerInfor.getFlowNode();
							}
						}						
					}
					
					if(tokenNode!=null && mainToken!=null){
						mainToken.setCurrentNode(tokenNode);
						this.instanceTokenDAO.save(mainToken);
						
						if(layerInfor.getForkedType()==InstanceLayerInfor.Layer_Forked_Join){
							/** 为预设的聚合层,则需要为预设层添加子Token 
							 * A.当预设的分叉节点为一个时,说明该分叉节点原来为非分叉,是手动添加分叉后才更改的,所以需要将主Token改到此节点上;
							 * B.当预设的分叉节点大于一个时,生成子Token.
							 * */
							List innerLayers = new ArrayList();
							List bTransitions = this.instanceTransitionDAO.findBeforeLayerTransition(layerId);
							for(Iterator it = bTransitions.iterator();it.hasNext();){
								InstanceTransitionInfor transition = (InstanceTransitionInfor)it.next();
								InstanceLayerInfor beforeLayer = transition.getFromLayer();
								
								if(beforeLayer.getLayerType() == InstanceLayerInfor.Layer_Type_PreSet){
									innerLayers.add(beforeLayer);
								}else {
									InstanceLayerInfor theLayer = findBeforePresetForkedLayer(beforeLayer);
									if (theLayer != null) {
										innerLayers.add(theLayer);
									}
								}
							}
							if (innerLayers.size() == 1) {
								//A.将主Token改到此节点
								InstanceLayerInfor tmpLayer = (InstanceLayerInfor)innerLayers.get(0);
								mainToken.setCurrentNode(tmpLayer.getFlowNode());
								this.instanceTokenDAO.save(mainToken);
							}else if (innerLayers.size() > 1) {
								//B.生成子Token
								for(Iterator it = innerLayers.iterator();it.hasNext();){
									InstanceLayerInfor tmpLayer = (InstanceLayerInfor)it.next();
									InstanceToken childToken = new InstanceToken();
									
									childToken.setCurrentNode(tmpLayer.getFlowNode());
									childToken.setParent(mainToken);
									childToken.setInstance(instance);
									this.instanceTokenDAO.save(childToken);
								}
							}
						}
						
					}else{
						//如果之前没有预设层信息,则要删除该Instance的Token的信息
						if(!childTokens.isEmpty()){
							for(Iterator it=childTokens.iterator();it.hasNext();){
								InstanceToken tempToken = (InstanceToken)it.next();
								this.instanceTokenDAO.remove(tempToken);
							}
						}
						if(mainToken!=null)
							this.instanceTokenDAO.remove(mainToken);
					}
				}else {
					/**
					 * 分叉内的层
					 *  A. 如果该节点属于子Token,其前有预设的分叉内层，则需要更新子Token的位置
					 *  B. 如果该节点属于子Token,其前没有预设的分叉层,则需要删除该子Token,且更新主Token到合适位置（这里又分子Token是2个还是多个)
					 *  C. 如果该节点属于主Token,则找到其前的一个预设层，更新主Token到该位置
					 *  D. 如果该节点属于主Token,其前没有预设层，则需要删除该主Token 
					 */
					FlowNode layerNode = layerInfor.getFlowNode();
					InstanceToken thisChildToken = getChildToken (childTokens,layerNode);
					if(thisChildToken!=null){
						//属于子Token
						InstanceLayerInfor theLayer = findBeforePresetForkedLayer(layerInfor);
						if(theLayer!=null){
							//分叉内有预设的审核层
							thisChildToken.setCurrentNode(theLayer.getFlowNode());
							this.instanceTokenDAO.save(thisChildToken);
						}else{
							/**
							 * 前面无预设的审核层，则
							 * 1: 如果子Token只有2个，则需要删除这两个子Token，并且把主Token更新到另外一个分支上
							 * 2：如果子Token多余2个，则仅需要删除该子Token,其它无需改变
							 */
							if(!childTokens.isEmpty() && childTokens.size()==2){
								//只有2个，删除子Token,并且把主Token定位到另外一个子Token所在的位置
								for(Iterator it=childTokens.iterator();it.hasNext();){
									InstanceToken tempToken = (InstanceToken)it.next();
									if(!tempToken.equals(thisChildToken)){
										FlowNode tokenNode = tempToken.getCurrentNode();
										
										mainToken.setCurrentNode(tokenNode);
										this.instanceTokenDAO.save(mainToken);
										
										break;
									}	
								}
								
								for(Iterator it=childTokens.iterator();it.hasNext();){
									InstanceToken tempToken = (InstanceToken)it.next();
									this.instanceTokenDAO.remove(tempToken);
								}							
							}else{
								//多于2个
								this.instanceTokenDAO.remove(thisChildToken);
							}
						}
					}else{
						//属于主Token
						InstanceLayerInfor tempLayerInfor =findBeforePresetUnforkedLayer(layerInfor);
						if(tempLayerInfor!=null){
							//找到其前的一个预设层
							FlowNode tokenNode = tempLayerInfor.getFlowNode();
							
							mainToken.setCurrentNode(tokenNode);
							this.instanceTokenDAO.save(mainToken);
						}else{
							//未找到其前的预设层
							this.instanceTokenDAO.remove(mainToken);
						}
					}				
				}
			}else if(layerInfor.getLayerType() == InstanceLayerInfor.Layer_Type_Set && layerInfor.getForkedType()==InstanceLayerInfor.Layer_Forked_Join){
				/**
				 * 非预设的，Join层
				 * 如果主Token对应的层位于分叉内，则说明分叉内只有一个是预设层，其它的为手工添加,则不作任何操作
				 * 如果主Token不在分叉内，则说明分叉内要么没有预设层，要么有多个预设层。如果有多个，则需要添加上子Token
				 */
	
				boolean findMainTokenInFork = false;
				List bTransitions = this.instanceTransitionDAO.findBeforeLayerTransition(layerId);
				for(Iterator it = bTransitions.iterator();it.hasNext();){
					InstanceTransitionInfor transition = (InstanceTransitionInfor)it.next();
					InstanceLayerInfor beforeLayer = transition.getFromLayer();
					
					if(findMainTokenLayer(beforeLayer,mainToken)!=null){
						//主Token位于分层内
						findMainTokenInFork = true;
						break;					
					}
				}
				
				if(!findMainTokenInFork){
					//在分叉内未找到主Token,则需要为预设层添加子Token
					for(Iterator it = bTransitions.iterator();it.hasNext();){
						InstanceTransitionInfor transition = (InstanceTransitionInfor)it.next();
						InstanceLayerInfor beforeLayer = transition.getFromLayer();
						
						InstanceLayerInfor tokenLayer = null;
						
						if(beforeLayer.getLayerType()==InstanceLayerInfor.Layer_Type_PreSet){
							tokenLayer  = beforeLayer;
						}else{
							tokenLayer = findBeforePresetForkedLayer(beforeLayer);
						}
						
						if(tokenLayer!=null){
							//生成子Token
							InstanceToken childToken = new InstanceToken();
							
							childToken.setCurrentNode(tokenLayer.getFlowNode());
							childToken.setParent(mainToken);
							childToken.setInstance(instance);
							this.instanceTokenDAO.save(childToken);
						}
					}
				}			
			}
		
		List bTransitions = this.instanceTransitionDAO.findBeforeLayerTransition(layerId);
		if (aTransitions == null || aTransitions.isEmpty()) {
			/** 如果后面没有审核层 */
			// 删除链接关系,即toLayer为该层次的Transition
			for (Iterator it = bTransitions.iterator(); it.hasNext();) {
				InstanceTransitionInfor instanceTransition = (InstanceTransitionInfor) it.next();
	
				/**
				 * 如果前面一个节点有且只有2个作为fromLayer的Transition(即删除节点的前面一个为分叉节点)
				 * 则需要把前面一个层次从Fork类型改为Normal类型,并且分叉的另外一个层次的forked属性需要更改
				 */
				InstanceLayerInfor fromLayer = instanceTransition.getFromLayer();
				int fromLayerId = fromLayer.getLayerId();
				List layerTransitions = this.instanceTransitionDAO.findAfterLayerTransition(fromLayerId);
				if (!layerTransitions.isEmpty() && layerTransitions.size() == 2) {
					// 更改另外一个层次的forked属性
					for (Iterator itTansition = layerTransitions.iterator(); itTansition.hasNext();) {
						InstanceTransitionInfor theOtherTrans = (InstanceTransitionInfor) itTansition.next();
						InstanceLayerInfor otherLayer = theOtherTrans.getToLayer();
						if (otherLayer.getLayerId() != layerId) {
							otherLayer.setForkedType(InstanceLayerInfor.Layer_Forked_Normal);
							otherLayer.setForkedLayer(null);						
							this.instanceLayerDAO.save(otherLayer);
	
							break;
						}
					}
	
					fromLayer.setForkedType(InstanceLayerInfor.Layer_Forked_Normal);
					this.instanceLayerDAO.save(fromLayer);
				}
	
				// 删除该Transition
				this.instanceTransitionDAO.remove(instanceTransition);
			}
			
		}else {
			/** 如果后面有审核层 */
			if(layerInfor.getForkedType()==InstanceLayerInfor.Layer_Forked_Normal){
				//该审核层为普通层次时,将后面的审核层与前一审核层建立连接
				InstanceTransitionInfor aTransition = (InstanceTransitionInfor)aTransitions.get(0);
				InstanceLayerInfor toLayer = aTransition.getToLayer();
				if (bTransitions != null && bTransitions.size() > 0) {
					InstanceTransitionInfor bTransition = (InstanceTransitionInfor)bTransitions.get(0);
					bTransition.setToLayer(toLayer);
					this.instanceTransitionDAO.save(bTransition);
				}
				this.instanceTransitionDAO.remove(aTransition);
			}else if(layerInfor.getForkedType()==InstanceLayerInfor.Layer_Forked_ForkInner){
				/** 该审核层为分叉内层次时,分三种情况:
				 * a.该审核层所在的分叉内有不止一个分叉内层次,则直接将后面的审核层与前面的建立连接即可; 
				 * b.该审核层所在的分叉内只有该审核层一个分叉内层次,且总共有多于2个分叉,则直接把该审核层及其transition删除即可;
				 * c.该审核层所在的分叉内只有该审核层一个分叉内层次,且总共只有2个分叉,则需要将另外一个分叉上的所有分叉内层次改为正常层次.*/
				InstanceTransitionInfor aTransition = (InstanceTransitionInfor)aTransitions.get(0);
				InstanceLayerInfor toLayer = aTransition.getToLayer();
				if (bTransitions != null && bTransitions.size() > 0) {
					InstanceTransitionInfor bTransition = (InstanceTransitionInfor)bTransitions.get(0);
					InstanceLayerInfor fromLayer = bTransition.getFromLayer();
					if (toLayer.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner || fromLayer.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner){
						//情况a
						bTransition.setToLayer(toLayer);
						this.instanceTransitionDAO.save(bTransition);
					}else {
						List fromATransitions = this.instanceTransitionDAO.findAfterLayerTransition(fromLayer.getLayerId());
						if (fromATransitions.size() == 2) {
							//情况c
							for (Iterator it=fromATransitions.iterator();it.hasNext();) {
								InstanceTransitionInfor transition = (InstanceTransitionInfor)it.next();
								//修改另外一个分叉上的分叉内层次
								if (transition.getTransId().intValue() != bTransition.getTransId().intValue()) {
									InstanceLayerInfor tmpToLayer = transition.getToLayer();
									tmpToLayer.setForkedType(InstanceLayerInfor.Layer_Status_Normal);
									this.instanceLayerDAO.save(tmpToLayer);
									
									//修改该审核层后面的分叉内层次
									List forkedLayers = findAfterForkedLayer(tmpToLayer);
									for (Iterator itLayer=forkedLayers.iterator();itLayer.hasNext();) {
										InstanceLayerInfor tmpLayer = (InstanceLayerInfor)itLayer.next();
										tmpLayer.setForkedType(InstanceLayerInfor.Layer_Status_Normal);
										this.instanceLayerDAO.save(tmpLayer);
									}
								}
							}
						}
						//情况b
						this.instanceTransitionDAO.remove(bTransition);
					}
				}
				this.instanceTransitionDAO.remove(aTransition);
			}else if (layerInfor.getForkedType()==InstanceLayerInfor.Layer_Forked_Fork) {
				//该审核层为分叉层次时,将后面的分叉内审核层与前一审核层建立连接
				if (bTransitions != null && bTransitions.size() > 0) {
					if (bTransitions.size() > 1) {
						//当前面存在分叉时,则无法判断前面的分叉与后面的分叉该如何建立连接
						throw new LayerOperateException("Can't delete this layer,do not know how to create transition!");
					}else {
						InstanceTransitionInfor bTransition = (InstanceTransitionInfor)bTransitions.get(0);
						InstanceLayerInfor fromLayer = bTransition.getFromLayer();
						for (Iterator it=aTransitions.iterator();it.hasNext();) {
							InstanceTransitionInfor aTransition = (InstanceTransitionInfor)it.next();
							aTransition.setFromLayer(fromLayer);
							this.instanceTransitionDAO.save(aTransition);
						}
						fromLayer.setForkedType(InstanceLayerInfor.Layer_Forked_Fork);
						this.instanceLayerDAO.save(fromLayer);
						this.instanceTransitionDAO.remove(bTransition);
					}
				}else {
					for (Iterator it=aTransitions.iterator();it.hasNext();) {
						InstanceTransitionInfor aTransition = (InstanceTransitionInfor)it.next();
						this.instanceTransitionDAO.remove(aTransition);
					}
				}
			}else {
				//该审核层为聚合层次时,将前面的分叉内审核层与后一审核层建立联系
				InstanceTransitionInfor aTransition = (InstanceTransitionInfor)aTransitions.get(0);
				InstanceLayerInfor toLayer = aTransition.getToLayer();
				for (Iterator it=bTransitions.iterator();it.hasNext();) {
					InstanceTransitionInfor bTransition = (InstanceTransitionInfor)it.next();
					bTransition.setToLayer(toLayer);
					this.instanceTransitionDAO.save(bTransition);
				}
				toLayer.setForkedType(InstanceLayerInfor.Layer_Forked_Fork);
				this.instanceLayerDAO.save(toLayer);
				this.instanceTransitionDAO.remove(aTransition);
			}
		}

		// 删除层次信息、审核人信息
		this.instanceLayerDAO.remove(layerInfor);		
		
	}
	
	
	//分叉内的层,根据层与层之间的Transition,找到分叉内，该条路径之前的主Token对应层
	private InstanceLayerInfor findMainTokenLayer(InstanceLayerInfor beforeLayer,InstanceToken mainToken){
		FlowNode node = mainToken.getCurrentNode();
		
		if(beforeLayer.getForkedType()!=beforeLayer.Layer_Forked_ForkInner)
			return null;
		
		FlowNode layerNode = beforeLayer.getFlowNode();
		if(layerNode!=null && layerNode.equals(node)) {
			return beforeLayer;
		} else{
			int layerId = beforeLayer.getLayerId();
			List transitions = this.instanceTransitionDAO.findBeforeLayerTransition(layerId);
			if(transitions.isEmpty()) {
				return null;
			}else{
				//分叉内的层，其前只有一个Transition
				InstanceTransitionInfor instanceTransition = (InstanceTransitionInfor)transitions.get(0);
				InstanceLayerInfor bLayer = instanceTransition.getFromLayer();
							
				return findMainTokenLayer(bLayer,mainToken);
			}	
		}
		
	}
	
	//分叉内的层，根据层与层的Transition,找到分叉内,且在其之前的预设层，递归查找
	public InstanceLayerInfor findBeforePresetForkedLayer(InstanceLayerInfor layerInfor){
		if(layerInfor.getForkedType()!=layerInfor.Layer_Forked_ForkInner)
			return null;
		
		int layerId = layerInfor.getLayerId();
		List transitions = this.instanceTransitionDAO.findBeforeLayerTransition(layerId);
		if(transitions.isEmpty()) {
			return null;
		}else{
			//分叉内的层，其前只有一个Transition
			InstanceTransitionInfor instanceTransition = (InstanceTransitionInfor)transitions.get(0);
			InstanceLayerInfor beforeLayer = instanceTransition.getFromLayer();
			if(beforeLayer.getForkedType()==InstanceLayerInfor.Layer_Forked_ForkInner 
					&& beforeLayer.getLayerType() == InstanceLayerInfor.Layer_Type_PreSet
					&& beforeLayer.getFlowNode()!=null)
				return beforeLayer;
			else
				return findBeforePresetForkedLayer(beforeLayer);
		}
	}
	
	//非分叉内的层，根据审核层次所在的层,找到非分叉,且在其之前的预设层
	private InstanceLayerInfor findBeforePresetUnforkedLayer(InstanceLayerInfor layerInfor){
			
		int layerId = layerInfor.getLayerId();
		int instanceId = layerInfor.getInstance().getInstanceId();
		List instanceLayers = this.instanceLayerDAO.findInstanceCheckLayers(instanceId);
		
		int layer = layerInfor.getLayer();
		for(int k=layer-1;k>0;k--){
			for(Iterator it=instanceLayers.iterator();it.hasNext();){
				InstanceLayerInfor tempLayer = (InstanceLayerInfor)it.next();
				if(tempLayer.getLayer()==k && tempLayer.getLayerType()==InstanceLayerInfor.Layer_Type_PreSet
						&& tempLayer.getForkedType()!=InstanceLayerInfor.Layer_Forked_ForkInner)
					return tempLayer;
			}
		}
		
		return null;
	}
	
	
	//某个实例，某个节点对应的子Token
	private InstanceToken getChildToken(List childTokens,FlowNode node){
		if(!childTokens.isEmpty()){
			for(Iterator it=childTokens.iterator();it.hasNext();){
				InstanceToken tempToken = (InstanceToken)it.next();
				if(tempToken.getCurrentNode().equals(node))
					return tempToken;
			}
		}
		
		return null;
	}
	
	
	//找到某审核层所在分叉的所有分叉内层次，根据层与层的Transition,找到分叉内,且在该审核层之后的所有分叉内层次，递归查找
	public List findAfterForkedLayer(InstanceLayerInfor layerInfor){
		List returnLayers = new ArrayList();
		
		int layerId = layerInfor.getLayerId();
		List transitions = this.instanceTransitionDAO.findAfterLayerTransition(layerId);
		if(!transitions.isEmpty()) {
			//分叉内的层，其前只有一个Transition
			InstanceTransitionInfor instanceTransition = (InstanceTransitionInfor)transitions.get(0);
			InstanceLayerInfor afterLayer = instanceTransition.getToLayer();
			if(afterLayer.getForkedType()==InstanceLayerInfor.Layer_Forked_ForkInner) {
				returnLayers.add(afterLayer);
				findAfterForkedLayer(afterLayer);
			}
		}
		return returnLayers;
	}
	
}
