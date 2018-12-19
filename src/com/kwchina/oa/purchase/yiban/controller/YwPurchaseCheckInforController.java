package com.kwchina.oa.purchase.yiban.controller;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.ApproveSentenceManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.oa.purchase.sanfang.service.SupplierInforManager;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchasePackageDao;
import com.kwchina.oa.purchase.yiban.entity.PurchaseCheckInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseLayerInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchasePackage;
import com.kwchina.oa.purchase.yiban.service.PurchaseCheckInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseLayerInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.purchase.yiban.vo.PurchaseCheckInforVo;
import com.kwchina.oa.util.SysCommonMethod;
import com.kwchina.oa.workflow.service.ModifyInforManager;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import com.kwchina.sms.service.SmsManager;

@Controller
@RequestMapping("/ywpurchase/checkInfor.do")
public class YwPurchaseCheckInforController extends PurchaseBaseController {

	@Resource
	private PurchaseCheckInforManager purchaseCheckInforManager;
	@Autowired
	private SupplierInforManager supplierInforManager;
	@Autowired
	private PurchaseLayerInforManager purchaseLayerInforManager;
	@Autowired
	private PurchaseCheckDAO purchaseCheckDAO;
	@Resource
	private PurchaseManager purchaseManager;

	@Autowired
	private PurchasePackageDao purchasePackageDao;

	@Autowired
	private RoleManager roleManager;

	@Resource
	private ApproveSentenceManager approveSentenceManager;

//	@Resource
//	private SmsManager smsManager;
@Resource
private OrganizeManager organizeManager;
	@Resource
	private ModifyInforManager modifyInforManager;



	//编辑审核信息
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, PurchaseCheckInforVo vo) throws Exception {

		String purchaseId = request.getParameter("purchaseId");
		Integer checkInforId = vo.getCheckInforId();
		String packageId = request.getParameter("packageId");
		RoleInfor caiwu = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Caiwu);
		RoleInfor lingdao = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Gongsilingdao);
		RoleInfor fenguan = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_fenguancaigou);
		if (packageId != null || packageId != ""){
			request.setAttribute("packageId",packageId);
		}

		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);

		if (purchaseId != null && purchaseId.length() > 0) {

			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();

			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(Integer.valueOf(purchaseId));

			//获取审核实例相关信息
			getProcessInfors(request, response, purchase);

			if (checkInforId != null && checkInforId.intValue() > 0) {
				/** 主办人修改审核信息时 */
				PurchaseCheckInfor check = (PurchaseCheckInfor)this.purchaseCheckInforManager.get(checkInforId);

				BeanUtils.copyProperties(vo, check);

				//审核时间
				request.setAttribute("_CheckDate", check.getEndDate());

				//附件
				request.setAttribute("_CanUpload", true);
				if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
					String attachmentFile = check.getAttatchment();
					if (attachmentFile != null && !attachmentFile.equals("")) {
						String[][] attachment = processFile(attachmentFile);
						request.setAttribute("_CheckAttachment_Names", attachment[1]);
						request.setAttribute("_CheckAttachments", attachment[0]);
					}
				}
//				request.setAttribute("_CheckId",checkInforId);
			}else {
				/** 审核人审核时 */
				//获取该审核实例目前正在处理的审核层
				List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
				if (rLayers != null && rLayers.size() > 0) {
					for (Iterator it=rLayers.iterator();it.hasNext();) {
						PurchaseLayerInfor layer = (PurchaseLayerInfor)it.next();
						Set checks = layer.getCheckInfors();

						//判断是否存在该用户需要审核的信息
						boolean isChecker = false;
						for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
							PurchaseCheckInfor check = (PurchaseCheckInfor)itCheck.next();
							int ss = layer.getStatus();
							if(check.getStatus()==0){
								//审批人是个人
								if (layer.getPurchaseNode().getCheckerType() == 0) {
									int checkerId = check.getChecker().getPersonId().intValue();
									if (userId == checkerId && ss != 0) {
										BeanUtils.copyProperties(vo, check);

										//审核时间
										request.setAttribute("_CheckDate", check.getEndDate());

										//判断是否可以上传附件
										boolean canUpload = false;
										PurchaseLayerInfor layerInfor = check.getLayerInfor();
										if (layerInfor.getPurchaseNode() != null) {
											if (layerInfor.getPurchaseNode().isUpload()) {
												canUpload = true;
											}
										} else {
											canUpload = true;
										}
										request.setAttribute("_CanUpload", canUpload);

										//处理附件信息
										if (canUpload) {
											if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
												String attachmentFile = check.getAttatchment();
												if (attachmentFile != null && !attachmentFile.equals("")) {
													String[][] attachment = processFile(attachmentFile);
													request.setAttribute("_CheckAttachment_Names", attachment[1]);
													request.setAttribute("_CheckAttachments", attachment[0]);
												}
											}
										}
										isChecker = true;
										int checkId = check.getCheckInforId();
										request.setAttribute("_CheckId",checkId);

										boolean canBefore = false;
										boolean canAgree = false;
										boolean canDisagree = false;
										RoleInfor jigui = (RoleInfor) roleManager.get(PurchaseCheckInfor.Check_Role_Jiguibulingdao);
										OrganizeInfor org = (OrganizeInfor)this.organizeManager.get(PurchaseCheckInfor.Check_Org_Caigou);
										int caigoulingdao = org.getDirector().getPersonId();

										if(systemUser.getPersonId() == caigoulingdao){
											canBefore =true;
											canAgree = true;
										}else if(roleManager.belongRole(systemUser,jigui)){
											canBefore =true;
											canAgree = true;
											canDisagree = true;
										}
										request.setAttribute("_CanBefore",canBefore);
										request.setAttribute("_CanAgree",canAgree);
										request.setAttribute("_CanDisagree",canDisagree);
										break;
									}
								}else{
									//审批人是角色
									if (this.roleManager.belongRole(systemUser, lingdao) || this.roleManager.belongRole(systemUser, fenguan)){
										//公司领导审批
										int checkerId = check.getChecker().getPersonId().intValue();
										if (userId == checkerId && ss != 0) {
											BeanUtils.copyProperties(vo, check);

											//审核时间
											request.setAttribute("_CheckDate", check.getEndDate());

											//判断是否可以上传附件
											boolean canUpload = false;
											PurchaseLayerInfor layerInfor = check.getLayerInfor();
											if (layerInfor.getPurchaseNode() != null) {
												if (layerInfor.getPurchaseNode().isUpload()) {
													canUpload = true;
												}
											} else {
												canUpload = true;
											}
											request.setAttribute("_CanUpload", canUpload);

											//处理附件信息
											if (canUpload) {
												if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
													String attachmentFile = check.getAttatchment();
													if (attachmentFile != null && !attachmentFile.equals("")) {
														String[][] attachment = processFile(attachmentFile);
														request.setAttribute("_CheckAttachment_Names", attachment[1]);
														request.setAttribute("_CheckAttachments", attachment[0]);
													}
												}
											}
											isChecker = true;
											int checkId = check.getCheckInforId();
											request.setAttribute("_CheckId",checkId);

											boolean canBefore = false;
											boolean canAgree = false;
											boolean canDisagree = false;
											canAgree = true;
											canDisagree = true;

//
											request.setAttribute("_CanBefore",canBefore);
											request.setAttribute("_CanAgree",canAgree);
											request.setAttribute("_CanDisagree",canDisagree);
											break;
										}
									}else if (this.roleManager.belongRole(systemUser, caiwu)){
										//财务预算审批
										int checkerId = check.getChecker().getPersonId().intValue();
										if (userId == checkerId && ss != 0) {
											BeanUtils.copyProperties(vo, check);

											//审核时间
											request.setAttribute("_CheckDate", check.getEndDate());

											//判断是否可以上传附件
											boolean canUpload = false;
											PurchaseLayerInfor layerInfor = check.getLayerInfor();
											if (layerInfor.getPurchaseNode() != null) {
												if (layerInfor.getPurchaseNode().isUpload()) {
													canUpload = true;
												}
											} else {
												canUpload = true;
											}
											request.setAttribute("_CanUpload", canUpload);

											//处理附件信息
											if (canUpload) {
												if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
													String attachmentFile = check.getAttatchment();
													if (attachmentFile != null && !attachmentFile.equals("")) {
														String[][] attachment = processFile(attachmentFile);
														request.setAttribute("_CheckAttachment_Names", attachment[1]);
														request.setAttribute("_CheckAttachments", attachment[0]);
													}
												}
											}
											isChecker = true;
											int checkId = check.getCheckInforId();
											request.setAttribute("_CheckId",checkId);

											boolean canBefore = false;
											boolean canAgree = false;
											boolean canDisagree = false;
											canAgree = true;
											canBefore = true;
//											int directorId = systemUserif (systemUser.getPersonId() == directorId ){
//												canBefore =true;
//												canAgree = true;
//											}else if(systemUser.getPersonId() == PurchaseCheckInfor.Check_Person_Guikoulingdao){
//												canBefore = true;
//												canAgree = true;
//												canDisagree = true;
//											}.getPerson().getDepartment().getDirector().getPersonId();
//
											request.setAttribute("_CanBefore",canBefore);
											request.setAttribute("_CanAgree",canAgree);
											request.setAttribute("_CanDisagree",canDisagree);
											break;
										}
									}else {
										int roleId = check.getCheckRoler().getRoleId();
										RoleInfor role = (RoleInfor) this.roleManager.get(roleId);
										if (this.roleManager.belongRole(systemUser, role) && ss != 0) {
											BeanUtils.copyProperties(vo, check);

											//判断是否可以上传附件
											boolean canUpload = false;
											PurchaseLayerInfor layerInfor = check.getLayerInfor();
											if (layerInfor.getPurchaseNode() != null) {
												if (layerInfor.getPurchaseNode().isUpload()) {
													canUpload = true;
												}
											} else {
												canUpload = true;
											}
											request.setAttribute("_CanUpload", canUpload);

											//处理附件信息
											if (canUpload) {
												if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
													String attachmentFile = check.getAttatchment();
													if (attachmentFile != null && !attachmentFile.equals("")) {
														String[][] attachment = processFile(attachmentFile);
														request.setAttribute("_CheckAttachment_Names", attachment[1]);
														request.setAttribute("_CheckAttachments", attachment[0]);
													}
												}
											}
											isChecker = true;
											int checkId = check.getCheckInforId();
											request.setAttribute("_CheckId", checkId);

											boolean canBefore = false;
											boolean canAgree = false;
											boolean canDisagree = false;

											if (roleManager.belongRole(systemUser, caiwu)) {
												canBefore = true;
												canAgree = true;
											} else if (roleManager.belongRole(systemUser, lingdao)) {
												canAgree = true;
												canDisagree = true;
											}
											request.setAttribute("_CanBefore", canBefore);
											request.setAttribute("_CanAgree", canAgree);
											request.setAttribute("_CanDisagree", canDisagree);
											break;

										}
									}
								}
							}
						}
						if (isChecker) {
							break;
						}
					}
				}
			}
		}


		return "yiban/editCheckInfor";
	}

	//编辑采购负责人审核信息
	@RequestMapping(params = "method=canSave")
	public String canSave(HttpServletRequest request, HttpServletResponse response, PurchaseCheckInforVo vo) throws Exception {

		String purchaseId = request.getParameter("purchaseId");
		Integer checkInforId = vo.getCheckInforId();

		String hql ="from SupplierInfor where valid=1 and status > 0 ";
		List supplierInfors = this.supplierInforManager.getResultByQueryString(hql);
		request.setAttribute("_Suppliers",supplierInfors);

		List approve = this.approveSentenceManager.getApproveSentenceOrderBy();
		request.setAttribute("_Approves", approve);

		if (purchaseId != null && purchaseId.length() > 0) {

			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			int userId = systemUser.getPersonId().intValue();

			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(Integer.valueOf(purchaseId));

			//获取审核实例相关信息
			getProcessInfors(request, response, purchase);

			if (checkInforId != null && checkInforId.intValue() > 0) {
				/** 主办人修改审核信息时 */
				PurchaseCheckInfor check = (PurchaseCheckInfor)this.purchaseCheckInforManager.get(checkInforId);

				BeanUtils.copyProperties(vo, check);

				//审核时间
				request.setAttribute("_CheckDate", check.getEndDate());

				//附件
				request.setAttribute("_CanUpload", true);
				if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
					String attachmentFile = check.getAttatchment();
					if (attachmentFile != null && !attachmentFile.equals("")) {
						String[][] attachment = processFile(attachmentFile);
						request.setAttribute("_CheckAttachment_Names", attachment[1]);
						request.setAttribute("_CheckAttachments", attachment[0]);
					}
				}
			}else {
				/** 审核人审核时 */
				//获取该审核实例目前正在处理的审核层
				List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
				if (rLayers != null && rLayers.size() > 0) {
					for (Iterator it=rLayers.iterator();it.hasNext();) {
						PurchaseLayerInfor layer = (PurchaseLayerInfor)it.next();
						Set checks = layer.getCheckInfors();

						//判断是否存在该用户需要审核的信息
						boolean isChecker = false;
						for (Iterator itCheck=checks.iterator();itCheck.hasNext();) {
							PurchaseCheckInfor check = (PurchaseCheckInfor)itCheck.next();
							int ss = layer.getStatus();
							if(check.getStatus()==0){
								//审批人是个人
								if (layer.getPurchaseNode().getCheckerType() == 0) {
									int checkerId = check.getChecker().getPersonId().intValue();
									if (userId == checkerId && ss != 0) {
										BeanUtils.copyProperties(vo, check);

										//审核时间
										request.setAttribute("_CheckDate", check.getEndDate());

										//判断是否可以上传附件
										boolean canUpload = false;
										PurchaseLayerInfor layerInfor = check.getLayerInfor();
										if (layerInfor.getPurchaseNode() != null) {
											if (layerInfor.getPurchaseNode().isUpload()) {
												canUpload = true;
											}
										} else {
											canUpload = true;
										}
										request.setAttribute("_CanUpload", canUpload);

										//处理附件信息
										if (canUpload) {
											if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
												String attachmentFile = check.getAttatchment();
												if (attachmentFile != null && !attachmentFile.equals("")) {
													String[][] attachment = processFile(attachmentFile);
													request.setAttribute("_CheckAttachment_Names", attachment[1]);
													request.setAttribute("_CheckAttachments", attachment[0]);
												}
											}
										}
										isChecker = true;
										int checkId = check.getCheckInforId();
										request.setAttribute("_CheckId",checkId);
										break;
									}
								}else{
									//审批人是角色
									int roleId = check.getCheckRoler().getRoleId();
									RoleInfor role = (RoleInfor)this.roleManager.get(roleId);
									if (this.roleManager.belongRole(systemUser,role) && ss != 0) {
										BeanUtils.copyProperties(vo, check);



										//判断是否可以上传附件
										boolean canUpload = false;
										PurchaseLayerInfor layerInfor = check.getLayerInfor();
										if (layerInfor.getPurchaseNode() != null) {
											if (layerInfor.getPurchaseNode().isUpload()) {
												canUpload = true;
											}
										} else {
											canUpload = true;
										}
										request.setAttribute("_CanUpload", canUpload);

										//处理附件信息
										if (canUpload) {
											if (check.getAttatchment() != null && check.getAttatchment().length() > 0) {
												String attachmentFile = check.getAttatchment();
												if (attachmentFile != null && !attachmentFile.equals("")) {
													String[][] attachment = processFile(attachmentFile);
													request.setAttribute("_CheckAttachment_Names", attachment[1]);
													request.setAttribute("_CheckAttachments", attachment[0]);
												}
											}
										}
										isChecker = true;
										int checkId = check.getCheckInforId();
										request.setAttribute("_CheckId",checkId);
										break;

									}
								}
							}
						}
						if (isChecker) {
							break;
						}
					}
				}
			}
			String sql5 = "";




		}


		return "yiban/editSaveCheckInfor";
	}

	//保存审核信息
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response,
					   PurchaseCheckInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		Integer checkId = vo.getCheckInforId();
		String packageId = vo.getPackageId();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer purchaseId = null;

		if (checkId != null && checkId.intValue() > 0) {
			PurchaseCheckInfor check = (PurchaseCheckInfor)this.purchaseCheckInforManager.get(checkId);

			BeanUtils.copyProperties(check, vo);

			String oldFiles = "";
			//对原附件进行处理
			String filePaths = check.getAttatchment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

			//新提交的附件
			String attachment = this.uploadAttachment(multipartRequest, "workflow");
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			check.setAttatchment(attachment);


			String checkDateStr = request.getParameter("checkDate");
			Timestamp checkDate = null;
			if (checkDateStr != null && checkDateStr.length() > 0) {
				checkDate = Timestamp.valueOf(checkDateStr);
			}
//			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			check.setChecker(systemUser);
			check = (PurchaseCheckInfor)this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);

			//更改主表采购状态
			PurchaseInfor purchase=check.getLayerInfor().getPurchase();
			purchaseId =purchase.getPurchaseId();

			boolean hasAllchecked = this.purchaseManager.hasAllchecked(purchase,check);

			if (vo.getStatus() == 1 ){
				if (hasAllchecked){
					int pstatus = purchase.getPurchaseStatus();
					if (purchase.getPurchaseWay() == 0 && purchase.getPurchaseStatus() == 4){
						pstatus = pstatus+2;
					}else{
						pstatus = pstatus+1;
					}
					purchase.setPurchaseStatus(pstatus);
					String purchaseFinalMoney = vo.getPurchaseFinalMoney();
					if (purchaseFinalMoney != null){
						purchase.setPurchaseFinalMoney(purchaseFinalMoney);
					}
					this.purchaseManager.save(purchase);
					purchaseId =purchase.getPurchaseId();
				}

				if (hasAllchecked){
					return flowToNextNode(request, response, purchaseId);
				}else{
					return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
				}
				//退回
			}else if (vo.getStatus() == 3){

					int pstatus = 1;
					purchase.setPurchaseStatus(pstatus);
					this.purchaseManager.save(purchase);

					List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
					PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
					if (layer.getStatus() == 1) {
						Set checks = layer.getCheckInfors();
						//结束本层
						for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
							PurchaseCheckInfor check1 = (PurchaseCheckInfor) itCheck.next();
							if (check1.getStatus() == 0) {
								check1.setStatus(4);
								Timestamp checkDate1 = null;
								this.purchaseCheckInforManager.saveCheckInfor(check1, checkDate1);
							}
						}
					}

					purchaseId =purchase.getPurchaseId();

				return flowToBeforeNode(request, response, purchaseId,check);
			}else if (vo.getStatus() == 2){
				int pstatus = PurchaseInfor.Purchase_Status_No;
				purchase.setPurchaseStatus(pstatus);
				this.purchaseManager.save(purchase);
				purchaseId = purchase.getPurchaseId();

				List rLayers = this.purchaseManager.getCurrentProcessLayers(purchase);
				PurchaseLayerInfor layer = (PurchaseLayerInfor)rLayers.get(0);
				if (layer.getStatus() == 1) {
					Set checks = layer.getCheckInfors();
					//结束本层
					for (Iterator itCheck = checks.iterator(); itCheck.hasNext(); ) {
						PurchaseCheckInfor check1 = (PurchaseCheckInfor) itCheck.next();
						if (check1.getStatus() == 0) {
							check1.setStatus(4);
							Timestamp checkDate1 = null;
							this.purchaseCheckInforManager.saveCheckInfor(check1, checkDate1);
						}
					}
				}


				return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
			}else{
				return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
			}
		}else{
			return "/common/error";
		}
	}

	//保存采购负责人审核信息
	@RequestMapping(params = "method=editSave")
	public String editSave(HttpServletRequest request, HttpServletResponse response,
					   PurchaseCheckInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		Integer checkId = vo.getCheckInforId();
		Integer purchaseId = null;

		if (checkId != null && checkId.intValue() > 0) {
			PurchaseCheckInfor check = (PurchaseCheckInfor)this.purchaseCheckInforManager.get(checkId);
			PurchaseInfor purchase=check.getLayerInfor().getPurchase();

			BeanUtils.copyProperties(check, vo);

			String oldFiles = "";
			//对原附件进行处理
			String filePaths = check.getAttatchment();
			oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");

			//新提交的附件
			String attachment = this.uploadAttachment(multipartRequest, "workflow");
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
					attachment = attachment + "|" + oldFiles;
				}
			}
			check.setAttatchment(attachment);

			String checkDateStr = request.getParameter("checkDate");
			Timestamp checkDate = null;
			if (checkDateStr != null && checkDateStr.length() > 0) {
				checkDate = Timestamp.valueOf(checkDateStr);
			}
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			check.setChecker(systemUser);
//			check = (PurchaseCheckInfor) this.purchaseCheckInforManager.saveCheckInfor(check, checkDate);
			check.setStatus(0);
			this.purchaseCheckDAO.save(check);
			//更改主表采购状态
			String caigou = request.getParameter("caigou");
			String supplierName = vo.getSupplierName();
			String purchaseMoney = vo.getPurchaseMoney();
			int purchaseWay = Integer.parseInt(caigou);
			int pstatus = purchase.getPurchaseStatus();
			pstatus = pstatus + 1;
			if (supplierName != null){
				purchase.setSupplierName(supplierName);
			}
			purchase.setPurchaseStatus(pstatus);
			purchase.setPurchaseWay(purchaseWay);
			purchase.setPurchaseMoney(purchaseMoney);
			this.purchaseManager.save(purchase);
			purchaseId = purchase.getPurchaseId();

		}
		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
	}

	//跳转到下一节点
	public String flowToNextNode(HttpServletRequest request, HttpServletResponse response, Integer purchaseId) {

		if (purchaseId != null && purchaseId.intValue() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);
			this.purchaseManager.nextNode(request,response,purchase);
		}
		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;
	}
	//跳转到之前节点
	public String flowToBeforeNode(HttpServletRequest request, HttpServletResponse response, Integer purchaseId,PurchaseCheckInfor checkInfor) {
		if (purchaseId != null && purchaseId.intValue() > 0) {
			PurchaseInfor purchase = (PurchaseInfor)this.purchaseManager.get(purchaseId);
			this.purchaseManager.beforeNode(request,response,purchase,checkInfor);
		}
		return "redirect:purchaseInfor.do?method=view&purchaseId=" + purchaseId;

	}


}