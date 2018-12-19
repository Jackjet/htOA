package com.kwchina.oa.workflow.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.kwchina.oa.workflow.controller.LayerInforController;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.FunctionRightInfor;
import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.OperationDefinitionManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.VirtualResourceManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.workflow.dao.FlowNodeDAO;
import com.kwchina.oa.workflow.dao.FlowTransitionDAO;
import com.kwchina.oa.workflow.dao.InstanceCheckDAO;
import com.kwchina.oa.workflow.dao.InstanceInforDAO;
import com.kwchina.oa.workflow.dao.InstanceLayerDAO;
import com.kwchina.oa.workflow.dao.InstanceTokenDAO;
import com.kwchina.oa.workflow.dao.InstanceTransitionDAO;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.entity.FlowTransition;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceInforRight;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.entity.InstanceToken;
import com.kwchina.oa.workflow.entity.InstanceTransitionInfor;
import com.kwchina.oa.workflow.exception.InstanceDeleteException;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.FlowLayerInforManager;
import com.kwchina.oa.workflow.service.FlowTransitionManager;
import com.kwchina.oa.workflow.service.InstanceInforRightManager;
import com.kwchina.oa.workflow.service.InstanceTokenManager;

@Service("flowInstanceManager")
public class FlowInstanceManagerImpl extends BasicManagerImpl<FlowInstanceInfor> implements FlowInstanceManager {

	@Autowired
	private FlowNodeDAO flowNodeDAO;

	@Autowired
	private InstanceInforDAO instanceInforDAO;

	@Autowired
	private InstanceLayerDAO instanceLayerDAO;

	@Autowired
	private InstanceCheckDAO instanceCheckDAO;

	@Autowired
	private InstanceTokenDAO instanceTokenDAO;

	@Autowired
	private InstanceTransitionDAO instanceTransitionDAO;

	@Autowired
	private FlowTransitionDAO flowTransitionDAO;

	@Autowired
	private InstanceTokenManager instanceTokenManager;
	
	@Autowired
	private FlowLayerInforManager flowLayerInforManager;
	
	@Autowired
	private FlowTransitionManager flowTransitionManager;
	
	@Autowired
	private VirtualResourceManager resourceManager;
	
	@Autowired
	private RoleManager roleManager;
	
	@Autowired
	private OperationDefinitionManager operationDefinitionManager;
	
	@Autowired
	private InstanceInforRightManager instanceInforRightManager;

	@Autowired
	public void setInstanceInforDAO(InstanceInforDAO instanceInforDAO) {
		this.instanceInforDAO = instanceInforDAO;
		super.setDao(instanceInforDAO);
	}
	

	/**
	 * 保存流程实例
	 * 
	 * @param instance
	 * @return
	 */
	public FlowInstanceInfor saveFlowInstance(FlowInstanceInfor instance) {
		return instance;
	}

	/**
	 * 判断是否可以删除流程 1: 申请者尚未提交时，可以删除 2. 如果用户是主办者，任何时候均可以删除
	 * 
	 * @param instance
	 * @param user
	 * @return
	 */
	public boolean canDeleteFlowInstance(FlowInstanceInfor instance, SystemUserInfor user) {

		int applierId = instance.getApplier().getPersonId();
		int personId = user.getPersonId();
		
		if(user.getUserName().equals("admin")){
			return true;
		}

		// 申请者：尚未提交，可以删除
		Timestamp startTime = instance.getStartTime();
		if (startTime == null && personId == applierId)
			return true;

		// 主办者,随时可删除
		//SystemUserInfor charger = instance.getFlowDefinition().getCharger();
		SystemUserInfor charger = instance.getCharger();
		if (charger != null && personId == charger.getPersonId())
			return true;

		return false;
	}

	/**
	 * 判断用户是否可以浏览该审核实例 主办者，提交者，部门审核人，审核者可以浏览审核实例
	 * 后来添加：流程的归档角色也具有权限
	 * 
	 * @param instance
	 * @param user
	 * @return
	 */
	public boolean canViewFlowInstance(FlowInstanceInfor instance, SystemUserInfor user) {
		int instanceId = instance.getInstanceId();
		int applierId = instance.getApplier().getPersonId();
		int personId = user.getPersonId();

		if(instance.getFlowDefinition().getFilerType() == 1){
			int fileRoleId = instance.getFlowDefinition().getFileRole().getRoleId();
			
			//流程归档角色
			Set<RoleInfor> sysRoleSet = user.getRoles();
			for(Iterator it = sysRoleSet.iterator();it.hasNext();){
				RoleInfor tmpRole = (RoleInfor)it.next();
				if(fileRoleId == tmpRole.getRoleId()){
					return true;
				}
			}
		}
		
		
		// 提交者
		if (personId == applierId)
			return true;

		// 主办者
		//SystemUserInfor charger = instance.getFlowDefinition().getCharger();
		SystemUserInfor charger = instance.getCharger();
		if (charger != null && personId == charger.getPersonId())
			return true;
		
		// 部门审核人
		SystemUserInfor manager = instance.getManager();
		if (manager != null && personId == manager.getPersonId())
			return true;
		SystemUserInfor viceManager = instance.getViceManager();
		if (viceManager != null && personId == viceManager.getPersonId())
			return true;

		//		获取合同审批特定角色的角色信息（ID为固定）
		RoleInfor contractRole = (RoleInfor)roleManager.get(24);
		
		boolean isContract  = roleManager.belongRole(user, contractRole);
		if(isContract){
			return true;
		}

		// 审核者
		List checkInfors = this.instanceCheckDAO.findChecksByInstance(instanceId);
		for (Iterator it = checkInfors.iterator(); it.hasNext();) {
			InstanceCheckInfor checkInfor = (InstanceCheckInfor) it.next();
			int checkerId = checkInfor.getChecker().getPersonId();
			if (checkerId == personId)
				return true;
		}
		
		//		权限表中设置的浏览权限
		boolean hasViewRight = this.instanceInforRightManager.hasRight(instance, user, InstanceInforRight._Right_View);
		if(hasViewRight){
			return true;
		}

		return false;
		

		
	}

	/**
	 * 申请人提交申请到审核环节,设定实例的节点,审核人等
	 * 
	 * @param instance
	 */
	public FlowInstanceInfor startInstance(FlowInstanceInfor instance) {
		int flowId = instance.getFlowDefinition().getFlowId();
		Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());

		// 设定开始时间
		instance.setStartTime(current);

		/*
		 * 如果该流程有预设的流程节点信息，则需要自动开始 
		 * 1.自动设定第一层的审核人信息 
		 * 2.生成相关Token信息
		 */
		int firstLayerNodeNum = 0;
		List nodes = this.flowNodeDAO.findFlowNodes(flowId);
		if (nodes != null && !nodes.isEmpty()) {
			for (Iterator it = nodes.iterator(); it.hasNext();) {
				FlowNode node = (FlowNode) it.next();
				if (node.getLayer() == 1) {
					firstLayerNodeNum += 1;
				}
			}

			/**
			 * 如果第一层的Node数量就大于1,说明流程从一开始就分叉 则需要构建一个不含currentNode的主Token
			 */
			InstanceToken mainToken = new InstanceToken();
			if (firstLayerNodeNum > 1) {
				// 产生主Token信息				
				mainToken.setInstance(instance);
				mainToken = (InstanceToken)this.instanceTokenDAO.save(mainToken);
			}

			for (Iterator it = nodes.iterator(); it.hasNext();) {
				FlowNode node = (FlowNode) it.next();
				if (node.getLayer() == 1) {
					//生成审核人
					flowLayerInforManager.generateCheckLayer(node, instance, null);

					if (firstLayerNodeNum > 1) {
						// 如果有多个firstLayer,需要生成子Token
						InstanceToken childToken = new InstanceToken();
						
						childToken.setCurrentNode(node);
						childToken.setParent(mainToken);
						childToken.setInstance(instance);
						this.instanceTokenDAO.save(childToken);
					} else {
						// 只有一个firstLayer,则需要生成主Token信息						
						mainToken.setCurrentNode(node);
						mainToken.setInstance(instance);
						this.instanceTokenDAO.save(mainToken);
					}
				}
			}
		}
		
		instance = (FlowInstanceInfor)this.instanceInforDAO.save(instance);
		
		return instance;
	}
	

	/**
	 * 删除流程实例，按照如下顺序进行删除 
	 * 1. 删除Token 
	 * 2. 删除审核信息 
	 * 3. 删除审核层次信息 
	 * 4. 删除流程实例信息
	 * 
	 * 以上删除在entity的关系中进行了处理
	 * 
	 * @param instance
	 */
	public void deleteInstance(FlowInstanceInfor instance, SystemUserInfor user) throws InstanceDeleteException {
		// 先判断是否可以删除，否则抛出异常
		if (!canDeleteFlowInstance(instance, user))
			throw new InstanceDeleteException();

		this.instanceInforDAO.remove(instance);
	}

	/**
	 * 判断是否可以添加审核层次
	 * 
	 * @param instance
	 * @param user
	 * @return
	 */
	public boolean canAddLayerInfor(FlowInstanceInfor instance, SystemUserInfor user) {
		//int instanceId = instance.getInstanceId();
		//int chargerId = instance.getFlowDefinition().getCharger().getPersonId();
		int chargerId = instance.getCharger().getPersonId();
		int personId = user.getPersonId();

		// 不是主办者，不能添加
		if (chargerId != personId)
			return false;

		// 申请者尚未提交审核，不能添加
		if (instance.getStartTime() == null)
			return false;

//		if (instance.getInstanceId() == 7018) {
//			System.out.println();
//		}
		if(instance.getFlowDefinition().getFlowId() == 86){
			Set<InstanceLayerInfor> layers = instance.getLayers();
			int num =layers.size();
			int _num =0;
//			boolean _status =true;
			for(InstanceLayerInfor instanceLayerInfor :layers){
				_num =_num+1;
				//合同 里只有在法务意见时 不能设定审核层，
				if(instanceLayerInfor.getLayerName().equals("法务意见") && instanceLayerInfor.getStatus()!=3){
					return false;
				}
				//合同总经理层审核完毕 不能出现 设定审核层
				if(instanceLayerInfor.getLayerName().equals("公司总经理")){
					return false;
				}
				//其他层如果没有选人也不能设定审核层
				Set<InstanceCheckInfor> checkInfors = instanceLayerInfor.getCheckInfors();
				if(checkInfors!=null ){
					if(checkInfors.isEmpty()){
						if (num == _num) {
							if (instanceLayerInfor.getStatus() != 3){
								return false;
							}
						}
					}
					else {
						for(InstanceCheckInfor instanceCheckInfor : checkInfors){
							if (instanceLayerInfor.getStatus() == 1){
//								if (instanceCheckInfor.getStatus() == 0){
//									_status = _status & true;
//								}
//								else{
//									_status = _status & false;
//								}
							}
							else if(instanceCheckInfor.getStatus() == 0){
								return false;
							}
						}
//						if (num == _num) {
//							if (_status == true){
//								return false;
//							}
//						}
					}
				}
			}
		}

		//体系文件 最后一层不能再设定审核层   上一层没选人员 或者 选的人员没有全审核完毕时，不应出现设定审核层
		if(instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101){

			Set<InstanceLayerInfor> layers = instance.getLayers();
			int num =layers.size();
			int _num =0;
			for(InstanceLayerInfor instanceLayerInfor :layers){
				_num =_num+1;
				if(num == _num && instanceLayerInfor.getStatus()!=3 && instanceLayerInfor.getStatus()!=1){
					return false;
				}
				if(instanceLayerInfor.getLayerName().equals("公司领导")){
					return false;
				}
			}

		}
		// 已经结束审核，不能添加
		if (instance.getEndTime() != null)
			return false;
		
		// 判断正在处理的审核层是否存在处理完毕的层次
		/*List processLayers = getCurrentProcessLayers(instance);
		if (processLayers != null && processLayers.size() > 0) {
			boolean canAddLayer = false;
			for (Iterator it=processLayers.iterator();it.hasNext();) {
				InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
				if (this.flowLayerInforManager.finishedCheck(layer)) {
					canAddLayer = true;
					String layerName = layer.getLayerName();
					break;
				}
			}
			if (canAddLayer) {
				return true;
			}else {
				return false;
			}
		}*/

		return true;
	}

	/**
	 * 获取一个实例当前处理层，即Transition中没有以该层作为fromLayer的层次
	 * 
	 * @param instance
	 * @return
	 */
	public List getCurrentProcessLayers(FlowInstanceInfor instance) {
		List rLayers = new ArrayList();

		int instanceId = instance.getInstanceId();

		List transitions = this.instanceTransitionDAO.findInstanceTransition(instanceId);
		List layers = this.instanceLayerDAO.findInstanceCheckLayers(instanceId);
		for (Iterator it = layers.iterator(); it.hasNext();) {
			InstanceLayerInfor layerInfor = (InstanceLayerInfor) it.next();
			int layerId = layerInfor.getLayerId();

			boolean findIt = false;
			for (Iterator itTransition = transitions.iterator(); itTransition.hasNext();) {
				InstanceTransitionInfor transition = (InstanceTransitionInfor) itTransition.next();
				int tempLayerId = transition.getFromLayer().getLayerId();
				if (layerId == tempLayerId) {
					findIt = true;
					break;
				}
			}

			if (!findIt)
				rLayers.add(layerInfor);
		}

		return rLayers;
	}
	
	/**
	 * 获取一个实例所有处理层，即Transition中没有以该层作为fromLayer的层次
	 * 
	 * @param instance
	 * @return
	 */
	public List getAllProcessLayers(FlowInstanceInfor instance) {
		List rLayers = new ArrayList();

		int instanceId = instance.getInstanceId();

		//List transitions = this.instanceTransitionDAO.findInstanceTransition(instanceId);
		List layers = this.instanceLayerDAO.findInstanceCheckLayers(instanceId);
		for (Iterator it = layers.iterator(); it.hasNext();) {
			InstanceLayerInfor layerInfor = (InstanceLayerInfor) it.next();
		
				rLayers.add(layerInfor);
		}

		return rLayers;
	}

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
	public void flowToNextNode(InstanceLayerInfor layerInfor) {
		FlowInstanceInfor instance = layerInfor.getInstance();
		int instanceId = instance.getInstanceId();
		
		//Token信息
		List tokens = this.instanceTokenDAO.findInstanceTokens(instanceId);
		InstanceToken mainToken = this.instanceTokenManager.getMainToken(tokens);
		List childTokens = this.instanceTokenManager.getChildTokens(tokens);
		
		if (layerInfor.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner) {
			//分叉内
			
			/*boolean allIsPreset = true;*/
			List processLayers = getCurrentProcessLayers(instance);
			/*for (Iterator it = processLayers.iterator(); it.hasNext();) {
				InstanceLayerInfor tempLayer = (InstanceLayerInfor) it.next();
				if(tempLayer.getLayerType() == InstanceLayerInfor.Layer_Type_Set){
					allIsPreset = false;
					break;
				}
			}*/
			
			/*if(allIsPreset){
				//全部为预设的,则需要判断是否toNode一致 */
				int k = 0;
				int toNodeId = 0;
				FlowNode toNode = null;
				
				for (Iterator it = childTokens.iterator(); it.hasNext();) {
					InstanceToken token = (InstanceToken) it.next();
					FlowNode node = token.getCurrentNode();

					List aTransitions = this.flowTransitionDAO.findAfterNodeTransition(node.getNodeId());
					if (!aTransitions.isEmpty()) {
						FlowTransition tTransition = (FlowTransition) aTransitions.get(0);
						if (k > 0 && tTransition.getToNode().getNodeId() != toNodeId) {
							toNode = null;
							break;
						}

						toNode = tTransition.getToNode();
						toNodeId = tTransition.getToNode().getNodeId();
						k += 1;
					} else {
						// 其后没预定义节点,不能自动
						toNode = null;
						break;
					}
				}
				
				if(toNode != null){
					//toNode一致,则流转到聚合节点
					
					//更新主Token位置
					mainToken.setCurrentNode(toNode);
					this.instanceTokenDAO.save(mainToken);
					
					//增加审核层次与审核人					
					flowLayerInforManager.generateCheckLayer(toNode, instance, processLayers);
					
					//删除子Token的信息
					for(Iterator it = childTokens.iterator();it.hasNext();){
						InstanceToken token = (InstanceToken)it.next();
						this.instanceTokenDAO.remove(token);
					}
					
					//更新当前几个层次为完成状态
					for (Iterator it = processLayers.iterator(); it.hasNext();) {
						InstanceLayerInfor tempLayer = (InstanceLayerInfor) it.next();
						tempLayer.setStatus(InstanceLayerInfor.Layer_Status_Finished);
						this.instanceLayerDAO.save(tempLayer);
					}					
				}else{
					autoProcessCurrentLayer(layerInfor,true);
				}				
			/*}else{
				//有非预设的，则只能处理本分支
				autoProcessCurrentLayer(layerInfor,true);
			}*/
		}else {
			//非分叉内
			autoProcessCurrentLayer(layerInfor,false);
		}
		
		/*if (layerInfor.getCheckInfors() == null || layerInfor.getCheckInfors().isEmpty()) {
			//如果该审核层未设置审核人,则删除
			try {
				this.flowLayerInforManager.deleteInstanceLayer(layerInfor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		//修改该审核层状态为"完毕"
		
	}
	
	
	/**
	 * 某个层次,自动到下一层次(非预设的审核层,则取其最近的预设层)
	 * 1. 如果在分叉内,查看其后有无分叉内的预设节点,有即流转到该节点
	 * 2. 非分叉内,根据其后续节点进行操作
	 * @param layerInfor 审核层次
	 * @param isForked 是否位于分叉内
	 */
	private void autoProcessCurrentLayer(InstanceLayerInfor layerInfor, boolean isForked){
		
		FlowNode flowNode = layerInfor.getFlowNode();
		InstanceToken mainToken = this.instanceTokenManager.getMainToken(layerInfor.getInstance().getInstanceId());
		if(layerInfor.getLayerType() == InstanceLayerInfor.Layer_Type_Set){
			if(isForked){
				//非预设的分叉内审核层,则取其最近的预设层
				InstanceLayerInfor beforeLayer = this.flowLayerInforManager.findBeforePresetForkedLayer(layerInfor);
				flowNode = beforeLayer.getFlowNode();
			}else {
				//非预设的非分叉内审核层,则取当前Tokens中的主token
				flowNode = mainToken.getCurrentNode();
			}
		}
		
		if(flowNode != null){
			int nodeId = flowNode.getNodeId();
				
			if(isForked){
				//分叉内(后续节点只能有一个)
				List transitions = this.flowTransitionDAO.findAfterNodeTransition(nodeId);
				if(!transitions.isEmpty() && transitions.size()==1){
					FlowTransition flowTransition = (FlowTransition)transitions.get(0);
					FlowNode toNode = flowTransition.getToNode();
					
					List beforeLayers = new ArrayList();
					//判断toNode是否为聚合节点,是聚合节点,则将所有当前审核层聚合
					List childTokens = this.instanceTokenManager.getChildTokens(layerInfor.getInstance().getInstanceId());
					List bTransitions = this.flowTransitionDAO.findBeforeNodeTransition(toNode.getNodeId());
					if (bTransitions.size() > 1) {
						beforeLayers = getCurrentProcessLayers(layerInfor.getInstance());
						
						//更新主Token位置
						mainToken.setCurrentNode(toNode);
						this.instanceTokenDAO.save(mainToken);
							
						//删除子Token的信息
						for(Iterator it = childTokens.iterator();it.hasNext();){
							InstanceToken token = (InstanceToken)it.next();
							this.instanceTokenDAO.remove(token);
						}
					}else {
						beforeLayers.add(layerInfor);
							
						//修改子Token信息:将子Token的当前节点信息改为预设的下一节点
						for(Iterator it=childTokens.iterator();it.hasNext();){
							InstanceToken token = (InstanceToken)it.next();
							if (token.getCurrentNode().getNodeId().intValue() == nodeId) {
								token.setCurrentNode(toNode);
								this.instanceTokenDAO.save(token);
								break;
							}
						}
					}
						
					//增加审核层次\审核人\Transition
					flowLayerInforManager.generateCheckLayer(toNode, layerInfor.getInstance(), beforeLayers);
						
				}
			}else{
				//非分叉内
				List transitions = this.flowTransitionDAO.findAfterNodeTransition(nodeId);
				if(!transitions.isEmpty()){
					for(Iterator itTransition=transitions.iterator();itTransition.hasNext();){
						FlowTransition flowTransition = (FlowTransition)itTransition.next();
						FlowNode toNode = flowTransition.getToNode();
							
						//增加审核层次\审核人\Transition
						List beforeLayers = new ArrayList();
						beforeLayers.add(layerInfor);
						flowLayerInforManager.generateCheckLayer(toNode, layerInfor.getInstance(), beforeLayers);
							
						if (transitions.size() == 1) {
							//修改主Token信息
							mainToken.setCurrentNode(toNode);
							this.instanceTokenDAO.save(mainToken);
						}else {
							//新增子Token
							InstanceToken token = new InstanceToken();
							token.setInstance(layerInfor.getInstance());
							token.setCurrentNode(toNode);
							token.setParent(mainToken);
							this.instanceTokenDAO.save(token);
						}
					}						
				}
			}
		}
		
	}
	
	/**
	 * 自动到下一层之前的验证
	 * @param instance 审核实例
	 * @return map
	 * @author huangzhen
	 */
	public Map<String, Object> nextValidate(FlowInstanceInfor instance){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取当前正在处理的审核层
		List processLayers = getCurrentProcessLayers(instance);
		
		//获取正在处理的已结束或已中止的审核层
		boolean canNext = false;
		List currentLayers = new ArrayList(processLayers.size());
		for(Iterator it = processLayers.iterator(); it.hasNext();){
			InstanceLayerInfor tmpLayer = (InstanceLayerInfor)it.next();
			if (tmpLayer.getCheckInfors() == null || tmpLayer.getCheckInfors().isEmpty() 
					|| tmpLayer.getStatus() == InstanceLayerInfor.Layer_Status_Finished || tmpLayer.getStatus() == InstanceLayerInfor.Layer_Status_End) {
				//如果当前正在处理的审核层未设置审核人或者已结束、已中止,都可执行下一步操作
				canNext = true;
				currentLayers.add(tmpLayer);
			}
			
			//添加判断，如果某一层的所有审核人均已审核完成，但本层的状态还未改，则先把本层状态改掉，再改为可下一步
			if(tmpLayer.getStatus() == InstanceLayerInfor.Layer_Status_Normal && tmpLayer.getCheckInfors() != null && tmpLayer.getCheckInfors().size() > 0){
				List<InstanceCheckInfor> checkInfors = new ArrayList<InstanceCheckInfor>(tmpLayer.getCheckInfors());
				boolean allDone = true;
				for(InstanceCheckInfor tmpCheck : checkInfors){
					if(tmpCheck.getEndDate() == null || tmpCheck.getStatus() == InstanceCheckInfor.Check_Status_Unckeck){
						allDone = false;
						break;
					}
				}
				if(allDone){
					tmpLayer.setStatus(InstanceLayerInfor.Layer_Status_Finished);
					tmpLayer.setEndTime(new java.sql.Timestamp(System.currentTimeMillis()));
					this.instanceLayerDAO.save(tmpLayer);
					
					canNext = true;
					currentLayers.add(tmpLayer);
				}
			}
		}

		if (!canNext) {
			//若不存在满足条件的审核层,则无法执行下一步操作
			map.put("needChoose", false);
			map.put("message", "至少有一个审核层符合条件(无审核人/已结束/已中止),才能执行下一步操作！");
		}else {
			/** [执行下一步操作的情况分析]
			 * 1.当前审核不处于分叉内,则取当前Tokens中的主token,跳转到其下一预设节点.
			 * 2.当前审核处于分叉内,获取当前Tokens中的子token
			 * 	A.所有子token的下一节点都一样,则直接跳转到预设的下一节点(即聚合节点);
			 * 	B.子token的下一节点存在不一样的情况,即既包含分叉内节点,又包含聚合节点：
			 * 		(a).用户选择对应的审核层跳转到下一分叉内节点;
			 * 		(b).用户选择跳转到聚合节点,则将所有审核层(包括预设的和手动的)都聚合到该节点;
			 * 		(c).用户选择手动添加下一层：选择某一审核层设置下一分叉层或聚合所有审核层(包括预设的和手动的).
			 *  */
			List childTokens = this.instanceTokenManager.getChildTokens(instance.getInstanceId());
			if (childTokens == null || childTokens.size() == 0) {
				//1.当前审核不处于分叉内,则取当前Tokens中的主token,跳转到其下一预设节点
				InstanceToken mainToken = this.instanceTokenManager.getMainToken(instance.getInstanceId());
				if (mainToken != null) {
					FlowNode currentNode = mainToken.getCurrentNode();
					List transitions = this.flowTransitionManager.findAfterNodeTransition(currentNode.getNodeId());
					if (transitions != null && transitions.size() > 0) {
						InstanceLayerInfor currentLayer = (InstanceLayerInfor)currentLayers.get(0);
						flowToNextNode(currentLayer);
						map.put("needChoose", false);
						map.put("message", "已转到下一节点！");
					}else {
						map.put("needChoose", false);
						map.put("message", "后面没有预设节点！");
					}
				}else {
					map.put("needChoose", false);
					map.put("message", "后面没有预设节点！");
				}
			}else {
				//2.当前审核处于分叉内,获取当前Tokens中的子token
				int k = 0;
				int toNodeId = 0;
				List nextNodes = new ArrayList();
				for (Iterator it = childTokens.iterator(); it.hasNext();) {
					InstanceToken token = (InstanceToken) it.next();
					FlowNode node = token.getCurrentNode();

					List aTransitions = this.flowTransitionManager.findAfterNodeTransition(node.getNodeId());
					if (aTransitions != null && !aTransitions.isEmpty()) {
						FlowTransition tTransition = (FlowTransition) aTransitions.get(0);
						if (k > 0 && tTransition.getToNode().getNodeId() != toNodeId) {
							nextNodes.add(tTransition.getToNode());
						}
						
						if (k == 0) {
							nextNodes.add(tTransition.getToNode());
							toNodeId = tTransition.getToNode().getNodeId();
						}
						k += 1;
					}
				}
				
				if (nextNodes.size() == 0) {
					map.put("needChoose", false);
					map.put("message", "没有可以跳转的下一节点！");
				}else if (nextNodes.size() == 1) {
					InstanceLayerInfor layerInfor = (InstanceLayerInfor)currentLayers.get(0);
					flowToNextNode(layerInfor);
					
					map.put("needChoose", false);
					map.put("message", "已转到下一节点！");
				}else {
					//当下一个预设的节点有多个时,需要由用户手动选取
					JSONArray nodeArray = new JSONArray();
					int j=0;
					for (Iterator it=nextNodes.iterator();it.hasNext();) {
						FlowNode node = (FlowNode)it.next();
						int fromLayerId = 0;
						for (Iterator itProcess = processLayers.iterator(); itProcess.hasNext();) {
							InstanceLayerInfor layerInfor = (InstanceLayerInfor)itProcess.next();
							if (layerInfor.getLayerType() == InstanceLayerInfor.Layer_Type_Set) {
								//为非预设审核层,则取其最近的预设审核层的节点进行比较
								InstanceLayerInfor beforeLayer = this.flowLayerInforManager.findBeforePresetForkedLayer(layerInfor);
								layerInfor = beforeLayer;
							}
							
							//判断审核层的下一节点是否与该节点一样
							int flowNodeId = layerInfor.getFlowNode().getNodeId().intValue();
							List transitions = this.flowTransitionManager.findAfterNodeTransition(flowNodeId);
							if(!transitions.isEmpty() && transitions.size()==1){
								FlowTransition flowTransition = (FlowTransition)transitions.get(0);
								FlowNode toNode = flowTransition.getToNode();
								if (toNode.getNodeId().intValue() == node.getNodeId().intValue()) {
									fromLayerId = layerInfor.getLayerId().intValue();
									break;
								}
							}
							
						}
						Map<String, Object> tmpMap = new HashMap<String, Object>();
						tmpMap.put("nodeName", node.getNodeName());
						tmpMap.put("fromLayerId", fromLayerId);
						nodeArray.add(tmpMap);
						j++;
					}
					map.put("nextNodes", nodeArray);
					
					map.put("needChoose", true);
				}
			}
		}
		return map;
	}
	
	
	/** 用户权限判断(判断用户在流程模块内的对应权限) 
	 * @param request
	 * @param method 方法名
	 * @param flowId 流程Id
	 * @param systemUser 系统用户
	 * */
	public boolean judgeRight(HttpServletRequest request, String method, int flowId, SystemUserInfor systemUser) {
		boolean hasRight = false;
		
		if (systemUser.getUserType() == SystemUserInfor._Type_Admin) {
			hasRight = true;
		}else {
			String pathURI = request.getRequestURI();
			pathURI += "?flowId=" + flowId;
			
			method = ("recycle").equals(method)?"delete":method;
			
			VirtualResource virtualResource = resourceManager.getVirtualResource(pathURI);
			
			if (virtualResource != null) {
				Set<FunctionRightInfor> functionRights = virtualResource.getFunctionRights();
				if (!functionRights.isEmpty()) {
					for (FunctionRightInfor functionRight : functionRights) {
						RoleInfor role = functionRight.getRole();
						long rightData = functionRight.getRightData();
						//判断用户对该操作是否拥有权限(通过对权限数据进行移位来判断)
						if (this.roleManager.belongRole(systemUser, role)) {
							OperationDefinition od = this.operationDefinitionManager.getOperationByMethod(method);
							if (od != null) {
								int position = od.getPosition();
								long a = rightData >> (position - 1);
								long result = (a & 1);
								if (result == 1) {
									hasRight = true;
									break;
								}
							}
						}
					}
				}
			}else {
				hasRight = true;
			}
		}
		
		return hasRight;
	}
	
	
	public List getViewUsers(FlowInstanceInfor instance) {
		List list = new ArrayList();
		//提交者
		SystemUserInfor applier = instance.getApplier();
		list.add(applier);
		
		//部门审核人
		SystemUserInfor manager = instance.getManager();
		if(manager != null){
			list.add(manager);
		}
		SystemUserInfor viceManager = instance.getViceManager();
		if(viceManager != null){
			list.add(viceManager);
		}
		
		//主办人
		SystemUserInfor charger = instance.getCharger();
		if(charger != null){
			list.add(charger);
		}
		
		//审核人 
		Set<InstanceLayerInfor> layerSet = instance.getLayers();
		for(Iterator it = layerSet.iterator();it.hasNext();){
			InstanceLayerInfor tmpLayerInfor = (InstanceLayerInfor)it.next();
			
			Set<InstanceCheckInfor> checkSet = tmpLayerInfor.getCheckInfors();
			for(Iterator it1 = checkSet.iterator();it1.hasNext();){
				InstanceCheckInfor tmpCheckInfor = (InstanceCheckInfor)it1.next();
				
				SystemUserInfor checker = tmpCheckInfor.getChecker();
				if(checker != null){
					list.add(checker);
				}
			}
		}
		
		//权限表中设置的浏览权限
		String hql = "select userRight.systemUser from InstanceInforUserRight userRight where userRight.instance.instanceId="+instance.getInstanceId();
		List checkerList = this.instanceInforRightManager.getResultByQueryString(hql);
		for(Iterator it = checkerList.iterator();it.hasNext();){
			SystemUserInfor tmpChecker = (SystemUserInfor)it.next();
			if(tmpChecker != null){
				list.add(tmpChecker);
			}
		}
		String hql1 = "select roleRight.role from InstanceInforRoleRight roleRight where roleRight.instance.instanceId="+instance.getInstanceId();
		List roleList = this.instanceInforRightManager.getResultByQueryString(hql1);
		for(Iterator it = roleList.iterator();it.hasNext();){
			RoleInfor tmpRole = (RoleInfor)it.next();
			Set<SystemUserInfor> tmpUserSet = tmpRole.getUsers();
			for(Iterator it1 = tmpUserSet.iterator();it1.hasNext();){
				SystemUserInfor tmpUserInfor = (SystemUserInfor)it1.next();
				if(tmpUserInfor != null){
					list.add(tmpUserInfor);
				}
			}
		}
		
		
		return list;
	}
	
	
	/** 判断是否当前审核人
	 * @param instance 实例信息
	 * @param systemUser 当前用户
	 * @return tmpCheck 当前审核层
	 * */
	public InstanceCheckInfor isChecker(FlowInstanceInfor instance, SystemUserInfor systemUser){
		InstanceCheckInfor tmpCheck = null;
		List rLayers = getCurrentProcessLayers(instance);
		if (rLayers != null && rLayers.size() > 0) {
			for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
				InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
				Set checks = layer.getCheckInfors();
				
				for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
					InstanceCheckInfor check = (InstanceCheckInfor)itCheck.next();
					
					int checkerId = check.getChecker().getPersonId().intValue();
					if (systemUser.getPersonId().intValue() == checkerId) {
						tmpCheck = check;
						break;
					}
				}
			}
		}
		return tmpCheck;
	}
	
	
	/**
	 * 获取待办信息（用于首页显示）
	 * @param systemUser
	 * @return
	 */
	public Map<String, Object> getNeedDealInstances(SystemUserInfor systemUser){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(systemUser != null){
			Integer personId = systemUser.getPersonId();
			// 获取尚未结束且未暂停的
			String queryHQL = "from FlowInstanceInfor instance where deleteFlag = 0 and suspended = 0 and filed = 0 and ( submiterWord is null or submiterWord = '' )";
			String condition = "";
			
			// 申请人
			condition += " and ((applier.personId = " + personId + " and endTime is null and startTime is null)";
			
			// 部门审核人
			condition += " or (submiterWord is null and endTime is null and ((manager.personId = " + systemUser.getPersonId() + " and managerChecked = 0) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 0)))";
			
			// 主办人
			condition += " or (charger.personId = " + personId + " and startTime is not null)";

			//具有归档权限的角色
			String sysRoleStr = "";
			Set<RoleInfor> tmpRoleSet = systemUser.getRoles();
			List<RoleInfor> tmpRoleList = new ArrayList<RoleInfor>();
			for(Iterator it = tmpRoleSet.iterator();it.hasNext();){
				tmpRoleList.add((RoleInfor)it.next());
			}
			for(int i=0;i<tmpRoleList.size();i++){
				sysRoleStr += tmpRoleList.get(i).getRoleId().toString();
				if(i<tmpRoleList.size()-1){
					sysRoleStr += ",";
				}
			}
			
			if(!sysRoleStr.equals("") && sysRoleStr != null){
				condition += " or (instance.flowDefinition.fileRole.roleId in ("+sysRoleStr+"))";
			}
			
			// 审核人
			condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
					"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.checker.personId = " + personId + " )) and endTime is null))";
			
			
				

			
			
			queryHQL += condition;
			queryHQL += " order by flowDefinition";
			List instances = getResultByQueryString(queryHQL);
			
			// 判断是否可以在首页显示
			List<FlowInstanceInfor> returnInstances = new ArrayList<FlowInstanceInfor>(instances);
			
			//移动端返回的list（只包含填写意见的）
			List<FlowInstanceInfor> returnInstances_m = new ArrayList<FlowInstanceInfor>();
			
			List<String> warningStrs = new ArrayList<String>(instances.size());
			for (Iterator it=instances.iterator();it.hasNext();) {

				FlowInstanceInfor instance = (FlowInstanceInfor)it.next();
//				if (instance.getInstanceId()==6997){
//					System.out.println("aaaaaaaaaa");
//				}

				boolean a= (instance.getFlowDefinition().getFilerType() == 1);
				boolean b = false;
				if(instance.getFlowDefinition().getFlowId()<90 || instance.getFlowDefinition().getFlowId()>100){
					//非体系文件时正常判断
					if(instance.getFlowDefinition().getFilerType() == 1){
						int fileRoleId = instance.getFlowDefinition().getFileRole().getRoleId();
						for(Iterator it1 = tmpRoleSet.iterator();it1.hasNext();){
							RoleInfor tmpRole = (RoleInfor)it1.next();
							if(fileRoleId == tmpRole.getRoleId()){
								b = true;
								break;
							}
						}
					}
				}else {
					//体系文件归档人为 文件流转审核人  谁审核谁归档
					Set<InstanceLayerInfor> instanceLayerInfors= instance.getLayers();
					InstanceLayerInfor layerInfor = new InstanceLayerInfor();
					if(instanceLayerInfors!=null && !instanceLayerInfors.isEmpty()){
						for(InstanceLayerInfor instanceLayerInfor:instanceLayerInfors){
							if(instanceLayerInfor.getLayerName().equals("文件流转审核人")){
								layerInfor = instanceLayerInfor;
							}
						}
					}
					Set<InstanceCheckInfor> checkInfors = layerInfor.getCheckInfors();
					if(checkInfors!=null && !checkInfors.isEmpty()){
						for(InstanceCheckInfor instanceCheckInfor : checkInfors){
							SystemUserInfor systemUserInfor = instanceCheckInfor.getChecker();
							if(systemUserInfor!=null && systemUserInfor.getPersonId().intValue() == systemUser.getPersonId().intValue() && instanceCheckInfor.getStatus() == 1 ){
								b = true;
							}
						}
					}
				}


				
				
				boolean canAdd = false;
				
				// 判断是否为当前审核人
				InstanceCheckInfor tmpCheck = isChecker(instance, systemUser);

				if (tmpCheck != null) {
					// 能否看见
					if (tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
							&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
							&& !instance.isSuspended() && tmpCheck.getEndDate() == null) {
						warningStrs.add("<font color='red'>请查看并签署意见</font>");
						canAdd = true;
						
						/***************添加到移动端的返回list中****************/
						returnInstances_m.add(instance);
					}
				}else if (((instance.getManager() != null && instance.getManager().getPersonId().intValue() == personId.intValue() && !instance.isManagerChecked())
						||(instance.getViceManager() != null && instance.getViceManager().getPersonId().intValue() == personId.intValue() && !instance.isViceManagerChecked()))
						&& instance.getStartTime() == null) {
					// 部门审核人
					warningStrs.add("<font color='red'>请审核文件</font>");
					canAdd = true;
					
					/***************添加到移动端的返回list中****************/
					returnInstances_m.add(instance);
				}
				else if (instance.getApplier().getPersonId().intValue() == personId.intValue() && instance.getStartTime() == null) {
					// 提交人
					if (instance.isSuspended()) {
						warningStrs.add("<font color='gray'>已暂停</font>");
					}else if (instance.getStartTime() != null) {
						warningStrs.add("<font color='red'>审核中</font>");
					}else if ((instance.isManagerChecked() || instance.isViceManagerChecked()) && instance.getSubmiterWord() == null) {
						warningStrs.add("<font color='red'>请进行相关处理</font>");
					}else if ((instance.getManager() != null || instance.getViceManager() != null) && instance.getSubmiterWord() == null) {
						warningStrs.add("<font color='red'>部门审核中</font>");
					}else {
						warningStrs.add("<font color='red'>请进行相关处理</font>");
					}
					canAdd = true;
					if ((instance.getManager() != null || instance.getViceManager() != null) && instance.getSubmiterWord() == null){
						if (!(instance.isManagerChecked()) ){canAdd = false;warningStrs.remove(warningStrs.size()-1);}
						else if ((instance.getViceManager()) != null){
							if (!(instance.isManagerChecked()) || !(instance.isViceManagerChecked()))
							{canAdd = false;warningStrs.remove(warningStrs.size()-1);}
						}
					}
				//}else if (instance.getFlowDefinition().getCharger().getPersonId() == personId.intValue()) {
				}else if (instance.getCharger().getPersonId() == personId.intValue() && instance.getFlowDefinition().getFilerType() == 0) {
					// 主办人
					
					/** 获取当前处理层进行判断:
					 * 若审核层的情况为中止或结束,或者审核层中未设审核人,则该审核实例显示在首页,提醒主办人执行下一步操作,否则不显示.
					 *  */

					boolean canDisplay = true;
					List curLyaers = getCurrentProcessLayers(instance);
					for (Iterator cl=curLyaers.iterator();cl.hasNext();) {
						InstanceLayerInfor layer = (InstanceLayerInfor)cl.next();
						if (layer.getStatus() != InstanceLayerInfor.Layer_Status_End && layer.getStatus() != InstanceLayerInfor.Layer_Status_Finished) {
							if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
								Set checkInfors = layer.getCheckInfors();

								if (checkInfors != null && checkInfors.size() > 0) {
									canDisplay = false;
									break;
								}
							}else {
								canDisplay = false;
								break;
							}
						}
						else{
							if (instance.getStamped() > 0){
								canDisplay = false;
								break;
							}
						}
					}
					
					if (canDisplay) {
						if (instance.getEndTime() != null) {
							warningStrs.add("<font color='red'>请归档</font>");
						}/*else if (instance.isSuspended()) {
							warningStrs.add("<font color='gray'>已暂停</font>");
						}else if (instance.getStartTime() != null) {
							warningStrs.add("<font color='red'>审核中</font>");
						}else {
							warningStrs.add("<font color='red'>草稿</font>");
						}*/
						else {
							boolean stampWarning = false;
							if (instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract) {
								//判断当前审核层是否为"公司领导意见",且状态为已经完毕或中止,若是则提示执行盖章操作
								List rLayers = getCurrentProcessLayers(instance);
								if (rLayers != null && rLayers.size() > 0) {
									for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
										InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
										if ((("公司总经理").equals(layer.getLayerName()) || ("公司领导意见").equals(layer.getLayerName())) && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
											stampWarning = true;
											break;
										}
									}
								}
							}
							
							if (stampWarning) {
								if (instance.getStamped()==2) {
									warningStrs.add("<font color='red'>已盖章,请进行相关处理</font>");
								}else {
									warningStrs.add("<font color='red'>请盖章</font>");
								}
							}else {
								warningStrs.add("<font color='red'>请进行相关处理</font>");
							}
						}
						canAdd = true;
					}

				}else if (instance.getCharger().getPersonId() == personId.intValue() && instance.getFlowDefinition().getFilerType() == 1) {
					// 主办人
					
					/** 获取当前处理层进行判断:
					 * 若审核层的情况为中止或结束,或者审核层中未设审核人,则该审核实例显示在首页,提醒主办人执行下一步操作,否则不显示.
					 *  */
					boolean canDisplay = true;
					List curLyaers = getCurrentProcessLayers(instance);
					for (Iterator cl=curLyaers.iterator();cl.hasNext();) {
						InstanceLayerInfor layer = (InstanceLayerInfor)cl.next();
//						System.out.println(layer.getStatus());
						if (layer.getStatus() != InstanceLayerInfor.Layer_Status_End && layer.getStatus() != InstanceLayerInfor.Layer_Status_Finished) {
							if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
								Set checkInfors = layer.getCheckInfors();
								if (checkInfors != null && checkInfors.size() > 0) {
									canDisplay = false;
									break;
								}
							}else {
								canDisplay = false;
								break;
							}
						}else{

							if (instance.getStamped() > 0){
								if (instance.getEndTime() != null && instance.getFlowDefinition().getFlowId() ==86){}
								else {
									canDisplay = false;
									break;
								}
							}
						}
					}

					if (canDisplay) {
						
						canAdd = true;
						
						if (instance.getEndTime() != null) {
							//canAdd = false;
							warningStrs.add("<font color='red'>已结束待归档</font>");
						}
						/*else if (instance.isSuspended()) {
							warningStrs.add("<font color='gray'>已暂停</font>");
						}else if (instance.getStartTime() != null) {
							warningStrs.add("<font color='red'>审核中</font>");
						}else {
							warningStrs.add("<font color='red'>草稿</font>");
						}*/
						else {
							boolean stampWarning = false;
							boolean stampWarning_File = false;
							if (instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract) {
								//判断当前审核层是否为"公司领导意见",且状态为已经完毕或中止,若是则提示执行盖章操作
								List rLayers = getCurrentProcessLayers(instance);
								if (rLayers != null && rLayers.size() > 0) {
									for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
										InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
										if ((("公司总经理").equals(layer.getLayerName()) || ("公司领导意见").equals(layer.getLayerName())) && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
											stampWarning = true;
											break;
										}
										if (("公司领导").equals(layer.getLayerName()) && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
											stampWarning_File = true;
											break;
										}
									}
								}
							}
							
							if (stampWarning) {
								if (instance.getStamped()==2) {
									warningStrs.add("<font color='red'>已盖章,请进行相关处理</font>");
								}else if (instance.getStamped()==1){
									warningStrs.add("<font color='red'>盖章已申请</font>");
								}else if (instance.getStamped()==0){
									warningStrs.add("<font color='red'>申请盖章</font>");
								}
							}else {
								if (stampWarning_File) {
									if (instance.getStamped()==2) {
										warningStrs.add("<font color='red'>已盖章,请进行相关处理</font>");
									}else if (instance.getStamped()==1){
										warningStrs.add("<font color='red'>盖章已申请</font>");
									}else if (instance.getStamped()==0){
										warningStrs.add("<font color='red'>申请盖章</font>");
									}
								}else {
									warningStrs.add("<font color='red'>请进行相关处理</font>");
								}
							}

						}
						
					}
				}else if(a && b){  //归档角色
					/** 获取当前处理层进行判断:
					 * 若审核层的情况为中止或结束,或者审核层中未设审核人,则该审核实例显示在首页,提醒归档人执行下一步操作,否则不显示.
					 *  */
					boolean canDisplay = true;
					
					//if(instance.getEndTime()==null  ){
						//canDisplay = false;
					//}
//					List curLyaers = this.flowInstanceManager.getCurrentProcessLayers(instance);
//					for (Iterator cl=curLyaers.iterator();cl.hasNext();) {
//						InstanceLayerInfor layer = (InstanceLayerInfor)cl.next();
//						if (layer.getStatus() != InstanceLayerInfor.Layer_Status_End && layer.getStatus() != InstanceLayerInfor.Layer_Status_Finished) {
//							if (layer.getStatus() == InstanceLayerInfor.Layer_Status_Normal) {
//								Set checkInfors = layer.getCheckInfors();
//								if (checkInfors != null && checkInfors.size() > 0) {
//									canDisplay = false;
//									break;
//								}
//							}else {
//								canDisplay = false;
//								break;
//							}
//						}
//					}
					canAdd = true;
					if (canDisplay) {
						if (instance.getEndTime() != null) {
							warningStrs.add("<font color='red'>请归档</font>");
						}else{
							
							
							boolean stampWarning = false;
							if (instance.getFlowDefinition().getFlowId() == SubmitConstant.SubmitFlow_Report_Contract || instance.getFlowDefinition().getFlowId()>89 && instance.getFlowDefinition().getFlowId()<101) {
								//判断当前审核层是否为"公司领导意见",且状态为已经完毕或中止,若是则提示执行盖章操作
								
								if(instance.getStamped()==1 || instance.getStamped()==2){
									stampWarning = true;
								}
//								List rLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
//								if (rLayers != null && rLayers.size() > 0) {
//									for (Iterator itLayer=rLayers.iterator();itLayer.hasNext();) {
//										InstanceLayerInfor layer = (InstanceLayerInfor)itLayer.next();
//										if (("公司领导意见").equals(layer.getLayerName()) && (layer.getStatus() == InstanceLayerInfor.Layer_Status_End||layer.getStatus() == InstanceLayerInfor.Layer_Status_Finished)) {
//											stampWarning = true;
//											break;
//										}
//									}
//								}
							}
							if(instance.getFlowDefinition().getFlowId()<90 || instance.getFlowDefinition().getFlowId()>100){
								if (stampWarning) {
									if (instance.getStamped()==2) {
										warningStrs.add("<font color='red'>已盖章,请进行相关处理</font>");
									}else if (instance.getStamped()==1){
										warningStrs.add("<font color='red'>请盖章</font>");
									}else if (instance.getStamped()==0){
										canAdd = false;
									}
								}else {
									//warningStrs.add("<font color='red'>审核中</font>");
									canAdd = false;
								}
							}else {
								if (stampWarning) {
									if (instance.getStamped()==2) {
										warningStrs.add("<font color='red'>已提交文档,请进行相关处理</font>");
									}else if (instance.getStamped()==1){
										warningStrs.add("<font color='red'>请提交文档</font>");
									}else if (instance.getStamped()==0){
										canAdd = false;
									}
								}else {
									//warningStrs.add("<font color='red'>审核中</font>");
									canAdd = false;
								}
							}

							
						
						}
						
					}
				}
				
				if (!canAdd) {
					// 不符合上面任何一种情况时,删除
					returnInstances.remove(instance);
				}

			}
			
			//pc端首页需要显示的
			map.put("ReturnInstances", returnInstances);
			map.put("WarningStrs", warningStrs);
			
			//手机端需要显示的（仅显示需要填写审批内容的）
			map.put("ReturnInstances_m", returnInstances_m);
		}
		
		return map; 
	}
	
	
	/**
	 * 获取需要推送的待办信息
	 * @param systemUser
	 * @return
	 */
	public List<FlowInstanceInfor> getNeedPushInstances(SystemUserInfor systemUser,Timestamp cutTime){
//		List<FlowInstanceInfor> rtnInstances = new ArrayList<FlowInstanceInfor>();
		
		Integer personId = systemUser.getPersonId();
		// 获取尚未结束且未暂停的,非体系
//		String queryHQL = "from FlowInstanceInfor instance where deleteFlag = 0 and suspended = 0 and instance.flowDefinition.flowId in (84,85,86,87,88) and filed = 0";
		String queryHQL = "from FlowInstanceInfor instance where deleteFlag = 0 and suspended = 0  and filed = 0";
		String condition = "";
		
		// 申请人
		condition += " and ((applier.personId = " + personId + " and endTime is null and startTime is null)";
		
		// 部门审核人
		condition += " or (submiterWord is null and endTime is null and ((manager.personId = " + systemUser.getPersonId() + " and managerChecked = 0) or (viceManager.personId = " + systemUser.getPersonId() + " and viceManagerChecked = 0)))";
		
		// 主办人
		condition += " or (charger.personId = " + personId + " and startTime is not null)";

		//具有归档权限的角色
		String sysRoleStr = "";
		Set<RoleInfor> tmpRoleSet = systemUser.getRoles();
		List<RoleInfor> tmpRoleList = new ArrayList<RoleInfor>();
		for(Iterator it = tmpRoleSet.iterator();it.hasNext();){
			tmpRoleList.add((RoleInfor)it.next());
		}
		for(int i=0;i<tmpRoleList.size();i++){
			sysRoleStr += tmpRoleList.get(i).getRoleId().toString();
			if(i<tmpRoleList.size()-1){
				sysRoleStr += ",";
			}
		}
		
		if(!sysRoleStr.equals("") && sysRoleStr != null){
			condition += " or (instance.flowDefinition.fileRole.roleId in ("+sysRoleStr+"))";
		}
		
		// 审核人
		condition += " or (instanceId in (select layer.instance.instanceId from InstanceLayerInfor layer where layerId in " +
				"(select checkInfor.layerInfor.layerId from InstanceCheckInfor checkInfor where checkInfor.checker.personId = " + personId + " )) and endTime is null))";
		
		queryHQL += condition;
		queryHQL += " order by flowDefinition";
		List instances = getResultByQueryString(queryHQL);
		
		//移动端返回的list（只包含填写意见的）
		List<FlowInstanceInfor> returnInstances_m = new ArrayList<FlowInstanceInfor>();
		for (Iterator it=instances.iterator();it.hasNext();) {
			FlowInstanceInfor instance = (FlowInstanceInfor)it.next();
			
			// 判断是否为当前审核人
			InstanceCheckInfor tmpCheck = isChecker(instance, systemUser);
			
			if (tmpCheck != null) {
				// 能否看见
				if (tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
						&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
						&& !instance.isSuspended() && tmpCheck.getEndDate() == null
						) {//&& (tmpCheck.getStartDate().after(cutTime) || tmpCheck.getStartDate() == cutTime)
					
					/***************添加到移动端的返回list中****************/
					returnInstances_m.add(instance);
				}
			}else if (((instance.getManager() != null && instance.getManager().getPersonId().intValue() == personId.intValue() && !instance.isManagerChecked())
					||(instance.getViceManager() != null && instance.getViceManager().getPersonId().intValue() == personId.intValue() && !instance.isViceManagerChecked()))
					&& instance.getStartTime() == null) {// && (instance.getUpdateTime().after(cutTime) || instance.getUpdateTime() == cutTime)
				// 部门审核人
				
				/***************添加到移动端的返回list中****************/
				returnInstances_m.add(instance);
			}
			
		}
		
		return returnInstances_m;
	}
}
