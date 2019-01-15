package com.kwchina.oa.purchase.yiban.service.impl;

import com.kwchina.core.base.entity.*;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseInforDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseLayerDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchasePackageDao;
import com.kwchina.oa.purchase.yiban.entity.*;
import com.kwchina.oa.purchase.yiban.service.PurchaseLayerInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.sys.PushUtil;
import com.kwchina.oa.workflow.dao.*;
import com.kwchina.oa.workflow.entity.*;
import com.kwchina.oa.workflow.exception.InstanceSuspendLayerException;
import com.kwchina.oa.workflow.exception.LayerOperateException;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.FlowLayerInforManager;
import com.kwchina.oa.workflow.service.InstanceTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


@Service("purchaseLayerInforManager")
public class PurchaseLayerInforManagerImpl extends BasicManagerImpl<PurchaseLayerInfor> implements PurchaseLayerInforManager {


	@Autowired
	private PurchaseInforDAO purchaseInforDAO;

	@Autowired
	private PurchaseLayerDAO purchaseLayerDAO;

	@Autowired
	private PersonInforManager personManager;
	@Autowired
	private PurchaseCheckDAO purchaseCheckDAO;

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
	private RoleManager roleManager;

	@Autowired
	public void setPurchaseLayerDAO(PurchaseLayerDAO purchaseLayerDAO) {
		this.purchaseLayerDAO = purchaseLayerDAO;
		super.setDao(purchaseLayerDAO);
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

		List layers = this.purchaseLayerDAO.findInstanceCheckLayers(instanceId);
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
		Timestamp current = new Timestamp(System.currentTimeMillis());

		if(layerId==null || layerId==0){
			//新增操作

			int currentLayer = 0;
			PurchaseLayerInfor currentLayerInfor = null;
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
							PurchaseLayerInfor layer = (PurchaseLayerInfor)it.next();
							if (finishedCheck(layer)) {
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

			layerInfor = (InstanceLayerInfor)this.purchaseLayerDAO.save(layerInfor);

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
						this.purchaseLayerDAO.save(otherLayer);
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
//					transition.setFromLayer(currentLayerInfor);
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
				this.purchaseCheckDAO.remove(checkInfor);
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
							this.purchaseCheckDAO.save(checkInfor);

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
		this.purchaseLayerDAO.save(layerInfor);
		return layerInfor;
	}


	/**
	 * 中止某层次的审核,可能为中止某个层次，也可能为中止当前实例，继续向下。
	 * 如果是多个层次，则需要判断是否全部是当前处理层次，并且包括全部当前处理层次
	 * 1. 设置该层次状态为中止
	 * 2. 该层未审核的人状态设置为中止
	 * 3. 进行到下一节点的操作
	 * @param
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
			this.purchaseLayerDAO.save(layerInfor);
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
		Timestamp current = new Timestamp(System.currentTimeMillis());

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
		layerInfor = (InstanceLayerInfor)this.purchaseLayerDAO.save(layerInfor);

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
					this.purchaseLayerDAO.save(layerInfor);

					if(afterTransitionNum == 1){
						//更改来源层为分叉层
						tempLayer.setForkedType(InstanceLayerInfor.Layer_Forked_Fork);
						this.purchaseLayerDAO.save(tempLayer);

						//把已有的一个分支更改为分叉内层次
						otherLayer.setForkedType(InstanceLayerInfor.Layer_Forked_ForkInner);
						otherLayer.setForkedLayer(tempLayer);
						this.purchaseLayerDAO.save(otherLayer);
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
		Timestamp current = new Timestamp(System.currentTimeMillis());

		InstanceCheckInfor checkInfor = new InstanceCheckInfor();
		checkInfor.setStartDate(current);
		checkInfor.setStatus(InstanceCheckInfor.Check_Status_Unckeck);
		
		checkInfor.setLayerInfor(layerInfor);
		checkInfor.setChecker(user);

		this.purchaseCheckDAO.save(checkInfor);
	}

	/**
	 * 检查该层是否全部审核完毕
	 * 1. 预设的,如果允许一个人审核后即往下流转,则完成;
	 * 	  若要全部人员通过审批才向下流转,则需要对所有审核信息进行判断,必须都完成才往下流转.
	 * 2. 非预设的,必须都完成.
	 * @param layerInfor
	 * @return
	 */
	public boolean finishedCheck(PurchaseLayerInfor layerInfor){
		boolean finished = true;
		
		if(1 == 1){
			//预设的节点
			finished = false;

			PurchaseNode node = layerInfor.getPurchaseNode();
//			if(node.getFinishType() == 1){
//				Set checkInfors = layerInfor.getCheckInfors();
//				for(Iterator it = checkInfors.iterator();it.hasNext();){
//					InstanceCheckInfor checkInfor = (InstanceCheckInfor)it.next();
//					if(checkInfor.getStatus() == InstanceCheckInfor.Check_Status_Checked){
//						return true;
//					}
//				}
//			}else {
				finished = allFinished(layerInfor);
//			}
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
	private boolean allFinished(PurchaseLayerInfor layerInfor) {
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

	public void buildStartCheckInfor(PurchaseLayerInfor purchaseLayerInfor, PurchaseNode node){

		Timestamp current = new Timestamp(System.currentTimeMillis());
		int status = 0;

		RoleInfor role =node.getRoleId();
//		Set users = role.getUsers();
//		for (Iterator itUser = users.iterator(); itUser.hasNext();) {
//			SystemUserInfor user = (SystemUserInfor) itUser.next();
//			if (user.getPerson().getDepartment().getOrganizeName() == purchaseLayerInfor.getPurchase().getDepartment().getOrganizeName()){
				PurchaseCheckInfor purchaseCheckInfor = new PurchaseCheckInfor();
				purchaseCheckInfor.setStartDate(current);
				purchaseCheckInfor.setStatus(status);
				purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
				purchaseCheckInfor.setCheckRoler(role);
//			}
//		}
		this.purchaseCheckDAO.save(purchaseCheckInfor);



	}
	public void buildCheckInfor(HttpServletRequest request, PurchaseLayerInfor purchaseLayerInfor, PurchaseNode node) {
		Timestamp current = new Timestamp(System.currentTimeMillis());
		int status = 0;
		int checkerType = node.getCheckerType();
		PurchaseInfor purchase = purchaseLayerInfor.getPurchase();
		PurchaseCheckInfor purchaseCheckInfor = new PurchaseCheckInfor();
		if (checkerType == 0){
			if (purchaseLayerInfor.getLayerName().equals("归口部门领导审批") && purchase.getGuikouDepartment().getOrganizeId() != 68){
				OrganizeInfor org = purchase.getGuikouDepartment();
				SystemUserInfor user = org.getDirector().getUser();
				purchaseCheckInfor.setChecker(user);
				purchaseCheckInfor.setStartDate(current);
				purchaseCheckInfor.setStatus(status);
				purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
				this.purchaseCheckDAO.save(purchaseCheckInfor);
			}else if (purchaseLayerInfor.getLayerName().equals("归口部门领导审批") && purchase.getGuikouDepartment().getOrganizeId() == 68){
				String jiguilingdao = purchase.getJigui();
				String[] jigui = jiguilingdao.split(",");
				for (int x = 0;x<jigui.length;x++){
					int y = Integer.valueOf(jigui[x]);
					PersonInfor user = (PersonInfor)this.personManager.get(y);
					SystemUserInfor u = user.getUser();
					purchaseCheckInfor.setChecker(u);
					purchaseCheckInfor.setStartDate(current);
					purchaseCheckInfor.setStatus(status);
					purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
					this.purchaseCheckDAO.save(purchaseCheckInfor);
				}
			}else{
				SystemUserInfor user =  node.getUser();
				purchaseCheckInfor.setChecker(user);
				purchaseCheckInfor.setStartDate(current);
				purchaseCheckInfor.setStatus(status);
				purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
				this.purchaseCheckDAO.save(purchaseCheckInfor);
			}

		}else{
			if (purchaseLayerInfor.getLayerName().equals("公司领导审核")){
				RoleInfor leader = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
				Set<SystemUserInfor> users = leader.getUsers();
				for (Iterator user=users.iterator();user.hasNext();) {
					SystemUserInfor userInfor = (SystemUserInfor)user.next();
					purchaseCheckInfor.setChecker(userInfor);
					purchaseCheckInfor.setStartDate(current);
					purchaseCheckInfor.setStatus(status);
					purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
					this.purchaseCheckDAO.save(purchaseCheckInfor);
				}

			}else if (purchaseLayerInfor.getLayerName().equals("分管领导审核")){
				RoleInfor leader = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_fenguancaigou);
				Set<SystemUserInfor> users = leader.getUsers();
				for (Iterator user=users.iterator();user.hasNext();) {
					SystemUserInfor userInfor = (SystemUserInfor)user.next();
					purchaseCheckInfor.setChecker(userInfor);
					purchaseCheckInfor.setStartDate(current);
					purchaseCheckInfor.setStatus(status);
					purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
					this.purchaseCheckDAO.save(purchaseCheckInfor);
				}

			}else if(purchaseLayerInfor.getLayerName().equals("财务预算审核")){
				RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
				Set<SystemUserInfor> users = caiwu.getUsers();
				for (Iterator user=users.iterator();user.hasNext();) {
					SystemUserInfor userInfor = (SystemUserInfor)user.next();
					purchaseCheckInfor.setChecker(userInfor);
					purchaseCheckInfor.setStartDate(current);
					purchaseCheckInfor.setStatus(status);
					purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
					this.purchaseCheckDAO.save(purchaseCheckInfor);
				}

			}else{
				RoleInfor role = node.getRoleId();
				purchaseCheckInfor.setStartDate(current);
				purchaseCheckInfor.setStatus(status);
				purchaseCheckInfor.setLayerInfor(purchaseLayerInfor);
				purchaseCheckInfor.setCheckRoler(role);
				this.purchaseCheckDAO.save(purchaseCheckInfor);
			}
		}


	}
}
