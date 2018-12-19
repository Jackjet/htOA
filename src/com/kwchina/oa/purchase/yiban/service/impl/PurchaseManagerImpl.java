package com.kwchina.oa.purchase.yiban.service.impl;

import com.kwchina.core.base.entity.*;
import com.kwchina.core.base.service.*;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseInforDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseLayerDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchasePackageDao;
import com.kwchina.oa.purchase.yiban.entity.*;
import com.kwchina.oa.purchase.yiban.service.PackageManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseLayerInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.dao.*;
import com.kwchina.oa.workflow.entity.*;
import com.kwchina.oa.workflow.exception.InstanceDeleteException;
import com.kwchina.oa.workflow.service.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Service("purchaseManager")
public class PurchaseManagerImpl extends BasicManagerImpl<PurchaseInfor> implements PurchaseManager {


	@Resource
	private PurchaseManager purchaseManager;
	@Resource
	private PackageManager packageManager;

	@Autowired
	private PurchasePackageDao purchasePackageDao;

	@Autowired
	private FlowNodeDAO flowNodeDAO;

	@Autowired
	private PersonInforManager personManager;
	@Autowired
	private PurchaseInforDAO purchaseInforDAO;

	@Autowired
	private PurchaseLayerDAO purchaseLayerDAO;

	@Autowired
	private PurchaseCheckDAO purchaseCheckDAO;

	@Autowired
	private PurchaseLayerInforManager purchaseLayerInforManager;

	@Autowired
	private VirtualResourceManager resourceManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private OperationDefinitionManager operationDefinitionManager;

	@Autowired
	private InstanceInforRightManager instanceInforRightManager;
	@Resource
	private OrganizeManager organizeManager;
	@Autowired
	public void setPurchaseInforDAO(PurchaseInforDAO purchaseInforDAO) {
		this.purchaseInforDAO = purchaseInforDAO;
		super.setDao(purchaseInforDAO);
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
	public boolean canDeleteFlowInstance(PurchaseInfor instance, SystemUserInfor user) {

		int applierId = instance.getApplier().getPersonId();
		int personId = user.getPersonId();

		if(user.getUserName().equals("admin")){
			return true;
		}

		// 申请者：尚未提交，可以删除
		Timestamp startTime = instance.getStartTime();
		if (startTime == null && personId == applierId){
			return true;
		}


		// 主办者,随时可删除
		//SystemUserInfor charger = instance.getFlowId().getCharger();
//		SystemUserInfor charger = instance.getCharger();
//		if (charger != null && personId == charger.getPersonId())
//			return true;

		return false;
	}

	/**
	 * 判断用户是否可以浏览该审核实例 主办者，提交者，部门审核人，审核者可以浏览审核实例
	 * 后来添加：流程的归档角色也具有权限
	 *
	 * @param purchase
	 * @param user
	 * @return
	 */
	public boolean canViewFlowInstance(PurchaseInfor purchase, SystemUserInfor user) {
		int purchaseId = purchase.getPurchaseId();
		int applierId = purchase.getApplier().getPersonId();
		int personId = user.getPersonId();

		if(purchase.getFlowId().getFlowId() == 1){
//			int fileRoleId = instance.getFlowId().getRole().getRoleId();

			//流程归档角色
			Set<RoleInfor> sysRoleSet = user.getRoles();
			for(Iterator it = sysRoleSet.iterator();it.hasNext();){
				RoleInfor tmpRole = (RoleInfor)it.next();
//				if(fileRoleId == tmpRole.getRoleId()){
//					return true;
//				}
			}
		}


		// 提交者
		if (personId == applierId){
			return true;}
		//部门领导审核
		if (purchase.getManager().getPersonId() == personId){
			return true;
		}
		// 主办者
		//SystemUserInfor charger = instance.getFlowId().getCharger();
//		SystemUserInfor charger = instance.getCharger();
//		if (charger != null && personId == charger.getPersonId())
//			return true;

		// 部门审核人
//		SystemUserInfor manager = instance.getManager();
//		if (manager != null && personId == manager.getPersonId()) {
//			return true;
//		SystemUserInfor viceManager = instance.getViceManager();
//		if (viceManager != null && personId == viceManager.getPersonId())
//			return true;

			//		获取合同审批特定角色的角色信息（ID为固定）
//		RoleInfor contractRole = (RoleInfor)roleManager.get(24);

//		boolean isContract  = roleManager.belongRole(user, contractRole);
//		if(isContract){
//			return true;
//		}

			// 审核者````````

		List checkInfors = this.purchaseCheckDAO.findChecksByInstance(purchaseId);
		for (Iterator it = checkInfors.iterator(); it.hasNext(); ) {
			PurchaseCheckInfor checkInfor = (PurchaseCheckInfor) it.next();
			int checkerId = checkInfor.getChecker().getPersonId();
			if (checkerId == personId)
				return true;
//			}

			//		权限表中设置的浏览权限
//		boolean hasViewRight = this.instanceInforRightManager.hasRight(instance, user, InstanceInforRight._Right_View);
//		if(hasViewRight){
//			return true;
		}

			return false;

	}

	/**
	 * 申请人提交申请到审核环节,设定实例的节点,审核人等
	 *
	 * @param instance
	 */
//	public PurchaseInfor startInstance(PurchaseInfor instance) {
//		int flowId = instance.getFlowId().getFlowId();
//		Timestamp current = new Timestamp(System.currentTimeMillis());
//
//		// 设定开始时间
//		instance.setStartTime(current);
//
//		/*
//		 * 如果该流程有预设的流程节点信息，则需要自动开始
//		 * 1.自动设定第一层的审核人信息
//		 * 2.生成相关Token信息
//		 */
//		int firstLayerNodeNum = 0;
//		List nodes = this.flowNodeDAO.findFlowNodes(flowId);
//		if (nodes != null && !nodes.isEmpty()) {
//			for (Iterator it = nodes.iterator(); it.hasNext();) {
//				FlowNode node = (FlowNode) it.next();
//				if (node.getLayer() == 1) {
//					firstLayerNodeNum += 1;
//				}
//			}
//
//			/**
//			 * 如果第一层的Node数量就大于1,说明流程从一开始就分叉 则需要构建一个不含currentNode的主Token
//			 */
////			InstanceToken mainToken = new InstanceToken();
////			if (firstLayerNodeNum > 1) {
////				// 产生主Token信息
////				mainToken.setInstance(instance);
////				mainToken = (InstanceToken)this.instanceTokenDAO.save(mainToken);
////			}
//
//			for (Iterator it = nodes.iterator(); it.hasNext();) {
//				FlowNode node = (FlowNode) it.next();
//				if (node.getLayer() == 1) {
//					//生成审核人
//					flowLayerInforManager.generateCheckLayer(node, instance, null);
//
//					if (firstLayerNodeNum > 1) {
//						// 如果有多个firstLayer,需要生成子Token
//						InstanceToken childToken = new InstanceToken();
//
//						childToken.setCurrentNode(node);
//						childToken.setParent(mainToken);
//						childToken.setInstance(instance);
//						this.instanceTokenDAO.save(childToken);
//					} else {
//						// 只有一个firstLayer,则需要生成主Token信息
//						mainToken.setCurrentNode(node);
//						mainToken.setInstance(instance);
//						this.instanceTokenDAO.save(mainToken);
//					}
//				}
//			}
//		}
//
//		instance = (PurchaseInfor)this.purchaseInforDAO.save(instance);
//
//		return instance;
//	}


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
	public void deleteInstance(PurchaseInfor instance, SystemUserInfor user) throws InstanceDeleteException {
		// 先判断是否可以删除，否则抛出异常
		if (!canDeleteFlowInstance(instance, user))
			throw new InstanceDeleteException();

		this.purchaseInforDAO.remove(instance);
	}

	/**
	 * 判断是否可以添加审核层次
	 *
	 * @param instance
	 * @param user
	 * @return
	 */
//	public boolean canAddLayerInfor(PurchaseInfor instance, SystemUserInfor user) {
//		//int instanceId = instance.getInstanceId();
//		//int chargerId = instance.getFlowId().getCharger().getPersonId();
////		int chargerId = instance.getCharger().getPersonId();
//		int personId = user.getPersonId();
//
//		// 不是主办者，不能添加
////		if (chargerId != personId)
////			return false;
//
//		// 申请者尚未提交审核，不能添加
//		if (instance.getStartTime() == null)
//			return false;
//
////		if (instance.getInstanceId() == 7018) {
////			System.out.println();
////		}
//		if(instance.getFlowId().getFlowId() == 86){
//			Set<InstanceLayerInfor> layers = instance.getLayers();
//			int num =layers.size();
//			int _num =0;
////			boolean _status =true;
//			for(InstanceLayerInfor instanceLayerInfor :layers){
//				_num =_num+1;
//				//合同 里只有在法务意见时 不能设定审核层，
//				if(instanceLayerInfor.getLayerName().equals("法务意见") && instanceLayerInfor.getStatus()!=3){
//					return false;
//				}
//				//合同总经理层审核完毕 不能出现 设定审核层
//				if(instanceLayerInfor.getLayerName().equals("公司总经理")){
//					return false;
//				}
//				//其他层如果没有选人也不能设定审核层
//				Set<InstanceCheckInfor> checkInfors = instanceLayerInfor.getCheckInfors();
//				if(checkInfors!=null ){
//					if(checkInfors.isEmpty()){
//						if (num == _num) {
//							if (instanceLayerInfor.getStatus() != 3){
//								return false;
//							}
//						}
//					}
//					else {
//						for(InstanceCheckInfor instanceCheckInfor : checkInfors){
//							if (instanceLayerInfor.getStatus() == 1){
////								if (instanceCheckInfor.getStatus() == 0){
////									_status = _status & true;
////								}
////								else{
////									_status = _status & false;
////								}
//							}
//							else if(instanceCheckInfor.getStatus() == 0){
//								return false;
//							}
//						}
////						if (num == _num) {
////							if (_status == true){
////								return false;
////							}
////						}
//					}
//				}
//			}
//		}
//
//		//体系文件 最后一层不能再设定审核层   上一层没选人员 或者 选的人员没有全审核完毕时，不应出现设定审核层
//		if(instance.getFlowId().getFlowId()>89 && instance.getFlowId().getFlowId()<101){
//
//			Set<InstanceLayerInfor> layers = instance.getLayers();
//			int num =layers.size();
//			int _num =0;
//			for(InstanceLayerInfor instanceLayerInfor :layers){
//				_num =_num+1;
//				if(num == _num && instanceLayerInfor.getStatus()!=3 && instanceLayerInfor.getStatus()!=1){
//					return false;
//				}
//				if(instanceLayerInfor.getLayerName().equals("公司领导")){
//					return false;
//				}
//			}
//
//		}
//		// 已经结束审核，不能添加
//		if (instance.getEndTime() != null)
//			return false;
//
//		// 判断正在处理的审核层是否存在处理完毕的层次
//		/*List processLayers = getCurrentProcessLayers(instance);
//		if (processLayers != null && processLayers.size() > 0) {
//			boolean canAddLayer = false;
//			for (Iterator it=processLayers.iterator();it.hasNext();) {
//				InstanceLayerInfor layer = (InstanceLayerInfor)it.next();
//				if (this.flowLayerInforManager.finishedCheck(layer)) {
//					canAddLayer = true;
//					String layerName = layer.getLayerName();
//					break;
//				}
//			}
//			if (canAddLayer) {
//				return true;
//			}else {
//				return false;
//			}
//		}*/
//
//		return true;
//	}

	/**
	 * 获取一个实例当前处理层
	 *purchase
	 * @param
	 * @return
	 */
	public List getCurrentProcessLayers(PurchaseInfor purchase) {
		List rLayers = new ArrayList();

		int purchaseId = purchase.getPurchaseId();

//		List transitions = this.instanceTransitionDAO.findInstanceTransition(instanceId);
		List layers = this.purchaseLayerDAO.findInstanceCheckLayers(purchaseId);
		PurchaseLayerInfor layerInfor = new PurchaseLayerInfor();
		if (layers != null && layers.size() > 0) {
			layerInfor = (PurchaseLayerInfor) layers.get(0);
			rLayers.add(layerInfor);
		}
//		for (Iterator it = layers.iterator(); it.hasNext();) {
//			PurchaseLayerInfor layerInfor = (PurchaseLayerInfor) it.next();
//			int layerId = layerInfor.getLayerId();
//
//			boolean findIt = false;
//			for (Iterator itTransition = transitions.iterator(); itTransition.hasNext();) {
//				InstanceTransitionInfor transition = (InstanceTransitionInfor) itTransition.next();
//				int tempLayerId = transition.getFromLayer().getLayerId();
//				if (layerId == tempLayerId) {
//					findIt = true;
//					break;
//				}
//			}
//
//			if (!findIt)
//				rLayers.add(layerInfor);
//		}
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
		List layers = this.purchaseLayerDAO.findInstanceCheckLayers(instanceId);
		for (Iterator it = layers.iterator(); it.hasNext();) {
			InstanceLayerInfor layerInfor = (InstanceLayerInfor) it.next();

				rLayers.add(layerInfor);
		}

		return rLayers;
	}


	public void startNode(HttpServletRequest request, HttpServletResponse response, PurchaseInfor purchase){
		int flowId = purchase.getFlowId().getFlowId();
		String hql = "from PurchaseNode node where flowId ="+ flowId +" order by layer";
		PurchaseNode node = (PurchaseNode)this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
		String nodeName = node.getNodeName();
		Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());
		int currentLayer = 1;
		int status = 1;
		PurchaseLayerInfor purchaseLayerInfor =new PurchaseLayerInfor();

		purchaseLayerInfor.setLayerName(nodeName);
		purchaseLayerInfor.setStartTime(current);
		purchaseLayerInfor.setStatus(status);
		purchaseLayerInfor.setLayer(currentLayer);
		purchaseLayerInfor.setPurchaseNode(node);
		purchaseLayerInfor.setPurchase(purchase);

		purchaseLayerInfor= (PurchaseLayerInfor)this.purchaseLayerDAO.save(purchaseLayerInfor);

//		Set<PurchaseCheckInfor> purchaseCheckInfors = new HashSet<PurchaseCheckInfor>();
		this.purchaseLayerInforManager.buildStartCheckInfor(purchaseLayerInfor,node);
	}

	public void nextNode(HttpServletRequest request, HttpServletResponse response, PurchaseInfor purchase){
		List rLayers = getCurrentProcessLayers(purchase);
		if (rLayers != null && rLayers.size() > 0) {
			PurchaseLayerInfor layer = (PurchaseLayerInfor) rLayers.get(0);
			int currentLayer =layer.getLayer();
			String layername = layer.getLayerName();
			if (layername.equals("采购部确认")){

			}else if(layername.equals("采购部领导审批") && purchase.getPurchaseStr2().equals("零部件物流")){
				int newcurrentLayer = currentLayer + 1;
				int layerNode = layer.getPurchaseNode().getNodeId();
				int newlayerNode = layerNode + 16;
				String hql = "from PurchaseNode node where nodeId =" + newlayerNode ;
				PurchaseNode node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
				String nodeName = node.getNodeName();

				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
				newLayer.setLayer(newcurrentLayer);
				newLayer.setStatus(1);
				newLayer.setStartTime(currentTime);
				newLayer.setPurchase(purchase);
				newLayer.setPurchaseNode(node);
				newLayer.setLayerName(nodeName);
				PurchaseLayerInfor purchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);
				this.purchaseLayerInforManager.buildCheckInfor(request,purchaseLayerInfor, node);
			}else {
				int newcurrentLayer = currentLayer + 1;
				int layerNode = layer.getPurchaseNode().getNodeId();
				int newlayerNode;
				if (layername.equals("分管领导审核")){
					newlayerNode = layerNode - 14;
				}else{
					newlayerNode = layerNode + 1;
				}
				String hql = "from PurchaseNode node where nodeId =" + newlayerNode ;
				PurchaseNode node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
				String nodeName = node.getNodeName();
				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
				newLayer.setLayer(newcurrentLayer);
				newLayer.setStatus(1);
				newLayer.setStartTime(currentTime);
				newLayer.setPurchase(purchase);
				newLayer.setPurchaseNode(node);
				newLayer.setLayerName(nodeName);

				//单个审批单个包
				if(purchase.getFlowId().getFlowId() == 1 ||purchase.getFlowId().getFlowId() == 4) {
					Set<PurchaseInfor> purchaseInfors = new HashSet<PurchaseInfor>();
					purchaseInfors.add(purchase);
					PurchasePackage purchasePackage = new PurchasePackage();
					if (rLayers != null && rLayers.size() > 0) {
						if (layername.equals("归口负责人提交") || layername.equals("采购负责人提交")) {

							if (layername.equals("采购负责人提交")){
								purchasePackage.setStatus(0);
								purchasePackage.setStartDate(currentTime);
								purchasePackage.setManager(node.getUser());
								purchasePackage.setCheckerType(0);
								purchasePackage.setPackageName(purchase.getApplier().getPerson().getDepartment().getOrganizeName() + "   采购计划");
								purchasePackage.setPurchaseInfors(purchaseInfors);
								purchasePackage.setFlowId(purchase.getFlowId().getFlowId());
								this.purchasePackageDao.save(purchasePackage);
							}else if (layername.equals("归口负责人提交") && purchase.getGuikouDepartment().getOrganizeId() == 68){
								//技术规划部
								String jiguilingdao = purchase.getJigui();
								String[] jigui = jiguilingdao.split(",");
								for (int x = 0;x<jigui.length;x++){
									int y = Integer.parseInt(jigui[x]);
									PersonInfor user = (PersonInfor)this.personManager.get(y);
									SystemUserInfor u = user.getUser();
									PurchasePackage jiguiPackage = new PurchasePackage();
									jiguiPackage.setStatus(0);
									jiguiPackage.setStartDate(currentTime);
									jiguiPackage.setManager(u);
									jiguiPackage.setCheckerType(0);
									jiguiPackage.setPackageName(purchase.getApplier().getPerson().getDepartment().getOrganizeName() + "   采购计划");
									jiguiPackage.setPurchaseInfors(purchaseInfors);
									jiguiPackage.setFlowId(purchase.getFlowId().getFlowId());
									this.purchasePackageDao.save(jiguiPackage);
								}

							}else{
								purchasePackage.setStatus(0);
								purchasePackage.setStartDate(currentTime);
								purchasePackage.setManager(purchase.getGuikouDepartment().getDirector().getUser());
								purchasePackage.setCheckerType(0);
								purchasePackage.setPackageName(purchase.getApplier().getPerson().getDepartment().getOrganizeName() + "   采购计划");
								purchasePackage.setPurchaseInfors(purchaseInfors);
								purchasePackage.setFlowId(purchase.getFlowId().getFlowId());
								this.purchasePackageDao.save(purchasePackage);
							}

						} else if (layername.equals("采购部领导审批") || layername.equals("财务预算审核")) {
//							purchasePackage.setStatus(0);
//							purchasePackage.setStartDate(currentTime);
//							purchasePackage.setRoleId(node.getRoleId());
//							purchasePackage.setCheckerType(1);
//							purchasePackage.setPackageName(purchase.getApplier().getPerson().getDepartment().getOrganizeName() + "   采购计划");
//							purchasePackage.setPurchaseInfors(purchaseInfors);
							RoleInfor a = (RoleInfor) this.roleManager.get(node.getRoleId().getRoleId());
							for (Iterator iterator = a.getUsers().iterator(); iterator.hasNext();) {
								SystemUserInfor sys = (SystemUserInfor) iterator.next();
								PurchasePackage purchasePackage1 = new PurchasePackage();
								purchasePackage1.setStatus(0);
								purchasePackage1.setManager(sys);
								purchasePackage1.setCheckerType(0);
								purchasePackage1.setPackageName(purchase.getApplier().getPerson().getDepartment().getOrganizeName() + "   采购计划");
								purchasePackage1.setStartDate(currentTime);
								purchasePackage1.setPurchaseInfors(purchaseInfors);
								purchasePackage1.setFlowId(purchase.getFlowId().getFlowId());
								this.purchasePackageDao.save(purchasePackage1);
							}

						}
					}
				}

				PurchaseLayerInfor purchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);
				this.purchaseLayerInforManager.buildCheckInfor(request,purchaseLayerInfor, node);
			}
		}
	}
	public void beforeNode(HttpServletRequest request, HttpServletResponse response, PurchaseInfor purchase,PurchaseCheckInfor checkInfor){
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
		List rLayers = getCurrentProcessLayers(purchase);
		if (rLayers != null && rLayers.size() > 0) {
			PurchaseLayerInfor layer = (PurchaseLayerInfor) rLayers.get(0);
			int currentLayer = layer.getLayer();
			String layername = layer.getLayerName();
			OrganizeInfor org = (OrganizeInfor)this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
			int caigoulingdao = org.getDirector().getPersonId();
			if (systemUser.getPersonId() == caigoulingdao) {
				int newcurrentLayer = currentLayer + 1;
				int layerNode = layer.getPurchaseNode().getNodeId();
				int newlayerNode = layerNode - 1;
				int flowId = purchase.getFlowId().getFlowId();
				String hql = "from PurchaseNode node where nodeId =" + newlayerNode;
				PurchaseNode node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
				String nodeName = node.getNodeName();
				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
				newLayer.setLayer(newcurrentLayer);
				newLayer.setStatus(1);
				newLayer.setStartTime(currentTime);
				newLayer.setPurchase(purchase);
				newLayer.setPurchaseNode(node);
				newLayer.setLayerName(nodeName);

				PurchaseLayerInfor purchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);
				this.purchaseLayerInforManager.buildCheckInfor(request,purchaseLayerInfor, node);

			}  else if (roleManager.belongRole(systemUser, caiwu)) {
				int newcurrentLayer = currentLayer + 1;
				int layerNode = layer.getPurchaseNode().getNodeId();
				int newlayerNode = layerNode - 2;
				int flowId = purchase.getFlowId().getFlowId();
				String hql = "from PurchaseNode node where nodeId =" + newlayerNode;
				PurchaseNode node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
				String nodeName = node.getNodeName();
				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
				newLayer.setLayer(newcurrentLayer);
				newLayer.setStatus(1);
				newLayer.setStartTime(currentTime);
				newLayer.setPurchase(purchase);
				newLayer.setPurchaseNode(node);
				newLayer.setLayerName(nodeName);

				PurchaseLayerInfor purchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);
				this.purchaseLayerInforManager.buildCheckInfor(request,purchaseLayerInfor, node);

			}else {
				int newcurrentLayer = currentLayer + 1;
				int layerNode = layer.getPurchaseNode().getNodeId();
				int newlayerNode = layerNode - 1;
				int flowId = purchase.getFlowId().getFlowId();
				String hql = "from PurchaseNode node where nodeId =" + newlayerNode;
				PurchaseNode node = (PurchaseNode) this.purchaseLayerInforManager.getResultByQueryString(hql).get(0);
				String nodeName = node.getNodeName();
				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				PurchaseLayerInfor newLayer = new PurchaseLayerInfor();
				newLayer.setLayer(newcurrentLayer);
				newLayer.setStatus(1);
				newLayer.setStartTime(currentTime);
				newLayer.setPurchase(purchase);
				newLayer.setPurchaseNode(node);
				newLayer.setLayerName(nodeName);

				PurchaseLayerInfor purchaseLayerInfor = (PurchaseLayerInfor) this.purchaseLayerDAO.save(newLayer);
				this.purchaseLayerInforManager.buildCheckInfor(request,purchaseLayerInfor, node);

			}

		}

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

	public boolean hasAllchecked(PurchaseInfor purchase,PurchaseCheckInfor check){
		PurchaseLayerInfor purchaseLayerInfor = check.getLayerInfor();
		boolean checked = true;
		Set<PurchaseCheckInfor> checkInfors = purchaseLayerInfor.getCheckInfors();
		for (Iterator it=checkInfors.iterator();it.hasNext();) {
			PurchaseCheckInfor checkInfor = (PurchaseCheckInfor)it.next();
			if (checkInfor.getStatus() == 0){
				checked = false;
				break;
			}
		}
		return checked;
	}
	
	/** 判断是否当前审核人
	 * @param instance 实例信息
	 * @param systemUser 当前用户
	 * @return tmpCheck 当前审核层
	 * */
	public PurchaseCheckInfor isChecker(PurchaseInfor instance, SystemUserInfor systemUser){
		PurchaseCheckInfor tmpCheck = null;
		List rLayers = getCurrentProcessLayers(instance);
		if (rLayers != null && rLayers.size() > 0) {
			PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
			Set checks = layer.getCheckInfors();
				if (layer.getPurchaseNode().getCheckerType() == 0) {
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();

						int checkerId = check.getChecker().getPersonId().intValue();
						if (systemUser.getPersonId().intValue() == checkerId) {
							tmpCheck = check;
							break;
						}
					}
				}else{
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check = (PurchaseCheckInfor) itCheck.next();
						if (check.getCheckRoler() == null){
							int checkerId = check.getChecker().getPersonId().intValue();
							if (systemUser.getPersonId().intValue() == checkerId) {
								tmpCheck = check;
								break;
							}
						}else{
							int roleId = check.getCheckRoler().getRoleId();
							RoleInfor role = (RoleInfor)this.roleManager.get(roleId);
							if (this.roleManager.belongRole(systemUser,role)) {
								tmpCheck = check;
								break;
							}
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
	public Map<String, Object> getNeedDealInstances(SystemUserInfor systemUser,int purchaseType){
		Map<String, Object> map = new HashMap<String, Object>();
		RoleInfor guikouf = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Guikou);
		RoleInfor caigouf = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caigou);
		RoleInfor jiguilingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Jiguibulingdao);
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
		RoleInfor lingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
		if(systemUser != null){
			Integer personId = systemUser.getPersonId();
			OrganizeInfor org = systemUser.getPerson().getDepartment();
			OrganizeInfor caigou = (OrganizeInfor)this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
			String queryHQL = null;
			String queryHQL2 = null;

			if (purchaseType == 1){

				if (roleManager.belongRole(systemUser, caiwu)){
					//财务预算审核
					queryHQL = "from PurchasePackage a where a.flowId=1 and a.status in (0,1) and (a.roleId.roleId="+ caiwu.getRoleId()+" or a.manager.personId="+ systemUser.getPersonId()+")";
				}
				else if(roleManager.belongRole(systemUser, lingdao)){
					//公司领导审核
					queryHQL = "from PurchasePackage a where a.flowId=1 and a.status in (0,1) and (a.roleId.roleId="+ lingdao.getRoleId()+" or a.manager.personId="+ systemUser.getPersonId()+")";
				}else if(roleManager.belongRole(systemUser, jiguilingdao)){
					//技术规划部领导
					queryHQL = "from PurchasePackage a where a.flowId=1 and a.status in (0,1) and (a.roleId.roleId="+ jiguilingdao.getRoleId()+" or a.manager.personId="+ systemUser.getPersonId()+")";
				}else{
					queryHQL = "from PurchasePackage a where a.flowId=1 and a.status in (0,1) and (a.manager.personId="+ systemUser.getPersonId()+" or a.vicemanager.personId="+ systemUser.getPersonId()+")";
				}
				List purchasePackages = this.packageManager.getResultByQueryString(queryHQL);
				for (Iterator iterator = purchasePackages.iterator();iterator.hasNext();){
					PurchasePackage purchasePackage = (PurchasePackage)iterator.next();
					Set purchaseInfors = purchasePackage.getPurchaseInfors();
					//是否审批人
					boolean rem = false;

					for (Iterator it = purchaseInfors.iterator();it.hasNext();){

						PurchaseInfor purchaseInfor = (PurchaseInfor)it.next();
						PurchaseCheckInfor purchaseCheckInfor = isChecker(purchaseInfor,systemUser);
						if (purchaseCheckInfor != null && purchaseCheckInfor.getStatus() == 0){
							rem = true;
							break;
						}
						if ((purchaseInfor.getManager()!= null &&purchaseInfor.getManager().getPersonId().intValue() == personId && !purchaseInfor.isManagerChecked())
								|| (purchaseInfor.getViceManager()!= null && purchaseInfor.getViceManager().getPersonId().intValue() == personId && !purchaseInfor.isViceManagerChecked())){
							rem = true;
							break;
						}
					}
					if (!rem){
						iterator.remove();
					}
				}
				if (purchasePackages.size()>0){
					map.put("ReturnInstances", purchasePackages);
					map.put("type", "0");

				}else{
					queryHQL2="from PurchaseInfor a where a.flowId.flowId=1 and a.deleteFlag = 0 and a.purchaseStatus != 21 and ";
					if (roleManager.belongRole(systemUser, caigouf)) {
						queryHQL2+= " (a.purchaseStatus = 4 or a.purchaseStatus = 5 or a.purchaseStatus = 10 ";
						queryHQL2+= " or (a.purchaseStatus = 9 and (a.sanfangId > 0 or a.zhaotouId > 0 or hetongId > 0))) ";
					}else if (roleManager.belongRole(systemUser, guikouf)){
						queryHQL2+= "a.guikouDepartment.organizeId ="+org.getOrganizeId() +"  and (a.purchaseStatus = 1 or a.purchaseStatus = 2)";
					}else{
						queryHQL2+= "a.purchaseStatus = -1 ";
					}
					List purchaseInfors = this.packageManager.getResultByQueryString(queryHQL2);
					for (Iterator iterator = purchaseInfors.iterator();iterator.hasNext();) {
						PurchaseInfor purchaseInfor = (PurchaseInfor) iterator.next();
						PurchaseCheckInfor purchaseCheckInfor = isChecker(purchaseInfor,systemUser);
						if (purchaseCheckInfor == null){
							iterator.remove();
							break;
						}
					}
					if (purchaseInfors.size()>0) {
						map.put("ReturnInstances", purchaseInfors);
						map.put("type","1");
					}
				}

			}else if (purchaseType == 2){
				queryHQL2="from PurchaseInfor a where a.flowId.flowId=2 and a.deleteFlag = 0 and a.purchaseStatus != 21 and (";
				queryHQL2+= "(a.purchaseStatus = 0 and (a.manager.personId = "+personId+" or a.viceManager.personId = "+personId+")) ";
				if (roleManager.belongRole(systemUser, caigouf)) {
					queryHQL2+= " or (a.purchaseStatus = 1 or a.purchaseStatus = 2 or a.purchaseStatus = 6  ";
					queryHQL2+= " or (a.purchaseStatus = 5 and (a.sanfangId > 0 or a.zhaotouId > 0 or hetongId > 0))) ";
				}else if (caigou.getDirector().getPersonId().intValue() == systemUser.getPersonId()){
					queryHQL2+= " or (a.purchaseStatus = 3) ";
				}else {
					queryHQL2+= " or (a.purchaseId in (select layer.purchase.purchaseId from PurchaseLayerInfor layer " +
							"where layerId in " +
							"(select checkInfor.layerInfor.layerId from PurchaseCheckInfor checkInfor " +
							"where checkInfor.checker.personId = " + personId + "  and checkInfor.status = 0 ))) ";
//					queryHQL2+= " or (purchaseStatus = 4) ";
				}
				queryHQL2+= " ) ";
				List purchaseInfors = this.purchaseManager.getResultByQueryString(queryHQL2);
//				for (Iterator iterator = purchaseInfors.iterator();iterator.hasNext();) {
//					PurchaseInfor purchaseInfor = (PurchaseInfor) iterator.next();
//					PurchaseCheckInfor purchaseCheckInfor = isChecker(purchaseInfor,systemUser);
//					if (purchaseCheckInfor == null){
//						iterator.remove();
//						break;
//					}
//				}
				if (purchaseInfors.size()>0) {
					map.put("ReturnInstances", purchaseInfors);
					map.put("type","1");
				}


			}else if (purchaseType == 3) {
				queryHQL2="from PurchaseInfor a where a.flowId.flowId=3 and a.deleteFlag = 0 and a.purchaseStatus != 21 and ( ";
				queryHQL2+= "(a.purchaseStatus = 0 and (a.manager.personId = "+personId+" or a.viceManager.personId = "+personId+")) ";
				if (roleManager.belongRole(systemUser, caigouf)) {
					queryHQL2+= " or (a.purchaseStatus = 4 or a.purchaseStatus = 5 or a.purchaseStatus = 10  ";
					queryHQL2+= " or (a.purchaseStatus = 9 and (a.sanfangId > 0 or a.zhaotouId > 0 or hetongId > 0))) ";
				}else if (roleManager.belongRole(systemUser, guikouf)) {
					queryHQL2+= " or ((a.purchaseStatus = 1 or a.purchaseStatus = 2) and a.guikouDepartment.organizeId = "+ org.getOrganizeId() +" ) ";
				}else if (caigou.getDirector().getPersonId().intValue() == systemUser.getPersonId()){
					queryHQL2+= " or (a.purchaseStatus = 6) ";
//				}else if (roleManager.belongRole(systemUser, caiwu)) {
//					queryHQL2+= " or (a.purchaseStatus = 7 ) ";
				}else {
					queryHQL2+= " or (a.purchaseStatus = 3 and a.guikouDepartment.director.user.personId = "+ personId +") or " +
							"(a.purchaseId in (select layer.purchase.purchaseId from PurchaseLayerInfor layer where layerId in " +
							"(select checkInfor.layerInfor.layerId from PurchaseCheckInfor checkInfor " +
							"where checkInfor.checker.personId = " + personId + "  and checkInfor.status = 0 ))) ";
//					queryHQL2+= " or (purchaseStatus = 4) ";
				}
				queryHQL2+= " ) ";
				List purchaseInfors = this.purchaseManager.getResultByQueryString(queryHQL2);
//				for (Iterator iterator = purchaseInfors.iterator();iterator.hasNext();) {
//					PurchaseInfor purchaseInfor = (PurchaseInfor) iterator.next();
//					PurchaseCheckInfor purchaseCheckInfor = isChecker(purchaseInfor,systemUser);
//					if (purchaseCheckInfor == null){
//						iterator.remove();
//						break;
//					}
//				}
				if (purchaseInfors.size()>0) {
					map.put("ReturnInstances", purchaseInfors);
					map.put("type","1");
				}

			}else if (purchaseType == 4){
				if (roleManager.belongRole(systemUser, caiwu)){
					//财务预算审核
					queryHQL = "from PurchasePackage a where a.flowId=4 and a.status in (0,1) and (a.roleId.roleId="+ caiwu.getRoleId()+" or a.manager.personId="+ systemUser.getPersonId()+")";
				}
				else if(roleManager.belongRole(systemUser, lingdao)){
					//公司领导审核
					queryHQL = "from PurchasePackage a where a.flowId=4 and a.status in (0,1) and (a.roleId.roleId="+ lingdao.getRoleId()+" or a.manager.personId="+ systemUser.getPersonId()+")";
				}else if(roleManager.belongRole(systemUser, jiguilingdao)){
					//技术规划部领导
					queryHQL = "from PurchasePackage a where a.flowId=4 and a.status in (0,1) and (a.roleId.roleId="+ jiguilingdao.getRoleId()+" or a.manager.personId="+ systemUser.getPersonId()+")";
				}else{
					queryHQL = "from PurchasePackage a where a.flowId=4 and a.status in (0,1) and (a.manager.personId="+ systemUser.getPersonId()+" or a.vicemanager.personId="+ systemUser.getPersonId()+")";
				}
				List purchasePackages = this.packageManager.getResultByQueryString(queryHQL);
				for (Iterator iterator = purchasePackages.iterator();iterator.hasNext();){
					PurchasePackage purchasePackage = (PurchasePackage)iterator.next();
					Set purchaseInfors = purchasePackage.getPurchaseInfors();
					boolean rem = false;
					for (Iterator it = purchaseInfors.iterator();it.hasNext();){

						PurchaseInfor purchaseInfor = (PurchaseInfor)it.next();
						PurchaseCheckInfor purchaseCheckInfor = isChecker(purchaseInfor,systemUser);
						if (purchaseCheckInfor != null && purchaseCheckInfor.getStatus() == 0){
							rem = true;
							break;
						}
						if ((purchaseInfor.getManager()!= null &&purchaseInfor.getManager().getPersonId().intValue() == personId && !purchaseInfor.isManagerChecked()) || (purchaseInfor.getViceManager()!= null && purchaseInfor.getViceManager().getPersonId().intValue() == personId && !purchaseInfor.isViceManagerChecked())){
							rem = true;
							break;
						}
					}
					if (!rem){
						iterator.remove();
					}
				}
				if (purchasePackages.size()>0){
					map.put("ReturnInstances", purchasePackages);
					map.put("type", "0");

				}else{
					queryHQL2="from PurchaseInfor a where a.flowId.flowId=4 and a.deleteFlag = 0 and a.purchaseStatus != 21 and ";
					if (roleManager.belongRole(systemUser, caigouf)) {
						queryHQL2+= "(a.purchaseStatus = 4 or a.purchaseStatus = 5  or a.purchaseStatus = 9 ";
						queryHQL2+= " or (a.purchaseStatus = 8 and (a.sanfangId > 0 or a.zhaotouId > 0 or hetongId > 0))) ";
					}else if (roleManager.belongRole(systemUser, guikouf)){
						queryHQL2+= "a.guikouDepartment.organizeId ="+org.getOrganizeId() +"  and (a.purchaseStatus = 1 or a.purchaseStatus = 2) ";
					}else{
						queryHQL2+= "a.purchaseStatus = -1";
					}
					List purchaseInfors = this.packageManager.getResultByQueryString(queryHQL2);
					for (Iterator iterator = purchaseInfors.iterator();iterator.hasNext();) {
						PurchaseInfor purchaseInfor = (PurchaseInfor) iterator.next();
						PurchaseCheckInfor purchaseCheckInfor = isChecker(purchaseInfor,systemUser);
						if (purchaseCheckInfor == null){
							iterator.remove();
							break;
						}
					}
					if (purchaseInfors.size()>0) {
						map.put("ReturnInstances", purchaseInfors);
						map.put("type","1");
					}
				}

			}

			
			//pc端首页需要显示的

			
			//手机端需要显示的（仅显示需要填写审批内容的）
//			map.put("ReturnInstances_m", returnInstances_m);
		}
		
		return map; 
	}
	
	
	/**
	 * 获取需要推送的待办信息
	 * @param systemUser
	 * @return
	 */
	public List<PurchaseInfor> getNeedPushInstances(SystemUserInfor systemUser,Timestamp cutTime){
//		List<FlowInstanceInfor> rtnInstances = new ArrayList<FlowInstanceInfor>();
		
		Integer personId = systemUser.getPersonId();
		// 获取尚未结束且未暂停的
		String queryHQL = "from FlowInstanceInfor instance where deleteFlag = 0 and suspended = 0 and filed = 0";
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
		List<PurchaseInfor> returnInstances_m = new ArrayList<PurchaseInfor>();
		for (Iterator it=instances.iterator();it.hasNext();) {
			PurchaseInfor instance = (PurchaseInfor)it.next();
			
			// 判断是否为当前审核人
			PurchaseCheckInfor tmpCheck = isChecker(instance, systemUser);
			
			if (tmpCheck != null) {
				// 能否看见
				if (tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_End
						&& tmpCheck.getLayerInfor().getStatus() != InstanceLayerInfor.Layer_Status_Suspended
						 && tmpCheck.getEndDate() == null
						) {//&& (tmpCheck.getStartDate().after(cutTime) || tmpCheck.getStartDate() == cutTime)
					
					/***************添加到移动端的返回list中****************/
					returnInstances_m.add(instance);
				}
			}else if (((instance.getManager() != null && instance.getManager().getPersonId().intValue() == personId.intValue() && !instance.isManagerChecked())
					)
					&& instance.getStartTime() == null) {// && (instance.getUpdateTime().after(cutTime) || instance.getUpdateTime() == cutTime)
				// 部门审核人
				
				/***************添加到移动端的返回list中****************/
				returnInstances_m.add(instance);
			}
			
		}
		
		return returnInstances_m;
	}
}
