package com.kwchina.oa.purchase.yiban.entity;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.json.JSONNotAware;
import org.hibernate.annotations.Cascade;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Create by yuanjl on 2018/5/29
 */
@Entity
@Table(name = "Purchase_InstanceInfor", schema = "dbo")
public class PurchaseInfor implements JSONNotAware {

    public static int Purchase_Status_No = 21;

    private Integer purchaseId; 				        // 审核实例Id
    private String purchaseStr1;
    private String purchaseStr2;
    private String purchaseTitle; 				        //看editpurchase.jsp中的字段含义
    private String purchaseGoods;                       //
    private int purchaseNumber;                         //
    private String guige;                               //
    private String application;                         //
    private String ysType;                              //预算类别
    private String gcfa;                                //工程方案
    private int purchaseStatus;					        // 采购状态0-新采购 1部门审核完 2归口负责人审核完 3归口领导审核完 4采购负责人保存完 5采购负责人提交完
                                                        //6采购部领导审批完 7财务预算审核完 8公司领导审核 9直接或三方或招投标流程完 10采购部确认

    //    private int purchaseType;					        // 采购分类 0-一般采购 1-业务采购 2-工程 3-零星
    private int purchaseWay;                            //采购方式  0-线下采购 1-三方比价 2-招投标 3-合同变更
    private int sanfangId;                              //三方ID
    private int zhaotouId;                             //招投标ID
    private int hetongId;                             //合同变更
    private OrganizeInfor guikouDepartment;             //归口部门
    private String jigui;                             //技术规划部选人
    private int lastNode; 					        	// 已执行的最后一个预设节点
    private String purchaseMoney;                       //报价
    private String purchaseFinalMoney;                  //最终价格

    private Timestamp startTime; 				// 开始时间
    private Timestamp endTime; 					// 结束时间
    private String attach; 						// 申请人提交的附件
    private String formalAttach; 				// 最终正式文件

    private int deleteFlag; 					// 删除标志
    private SystemUserInfor applier; 			// 申请人
    private OrganizeInfor department; 			// 所属部门
    private PurchaseFlowDefinition flowId; 		// 所属流程
    //部门审核相关字段
    private String managerWord;					// 审核人一的审核意见
    private String viceManagerWord;					// 审核人二的审核意见
    private Timestamp checkTime;				// 审核人一的审核时间
    private Timestamp viceManagercheckTime;				// 审核人二的审核时间
    private boolean managerChecked; 			// 审核人一是否审核
    private boolean viceManagerChecked; 			// 审核人二是否审核
    private int managerCheckStatus;             //是否同意  1同意  2不同意
    private int viceManagerCheckStatus;             //是否同意  1同意  2不同意
    private String managerAttachment;			// 审核人一的附件
    private String viceManagerAttachment;			// 审核人二的附件
    private SystemUserInfor manager;			// 审核人一
    private SystemUserInfor viceManager;			// 审核人二
    private String attachMemo;					// 备注
    private boolean filed;						// 是否归档：0-否;1-是.
    private String supplierName;
    private int  batchNumber;                       //批次号



    private Set<PurchaseLayerInfor> layers = new HashSet<PurchaseLayerInfor>(0);					//审核层次信息
//    private Set<InstanceInforRight> rights = new HashSet<InstanceInforRight>(0);
    private Set<PurchasePackage> packages = new HashSet<PurchasePackage>(0);


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    @Column(columnDefinition = "nvarchar(800)")
    public String getAttach() {
        return attach;
    }
    public void setAttach(String attach) {
        this.attach = attach;
    }

//    @Column(columnDefinition = "nvarchar(500)")
//    public String getContentPath() {
//        return contentPath;
//    }
//    public void setContentPath(String contentPath) {
//        this.contentPath = contentPath;
//    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getPurchaseStr1() {
        return purchaseStr1;
    }
    public void setPurchaseStr1(String purchaseStr1) {
        this.purchaseStr1 = purchaseStr1;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getPurchaseStr2() {
        return purchaseStr2;
    }
    public void setPurchaseStr2(String purchaseStr2) {
        this.purchaseStr2 = purchaseStr2;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getPurchaseGoods() {
        return purchaseGoods;
    }
    public void setPurchaseGoods(String purchaseGoods) {
        this.purchaseGoods = purchaseGoods;
    }


    public int getPurchaseNumber() {
        return purchaseNumber;
    }
    public void setPurchaseNumber(int purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public int getPurchaseStatus() {
        return purchaseStatus;
    }
    public void setPurchaseStatus(int purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

//    public int getPurchaseType() {
//        return purchaseType;
//    }
//    public void setPurchaseType(int purchaseType) {
//        this.purchaseType = purchaseType;
//    }

    public int getPurchaseWay() {
        return purchaseWay;
    }
    public void setPurchaseWay(int purchaseWay) {
        this.purchaseWay = purchaseWay;
    }


    public int getSanfangId() {
        return sanfangId;
    }
    public void setSanfangId(int sanfangId) {
        this.sanfangId = sanfangId;
    }


    public int getZhaotouId() {
        return zhaotouId;
    }
    public void setZhaotouId(int zhaotouId) {
        this.zhaotouId = zhaotouId;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="guikouDepartment")
    public OrganizeInfor getGuikouDepartment() {
        return guikouDepartment;
    }
    public void setGuikouDepartment(OrganizeInfor guikouDepartment) {
        this.guikouDepartment = guikouDepartment;
    }

    public String getJigui() {
        return jigui;
    }
    public void setJigui(String jigui) {
        this.jigui = jigui;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getPurchaseMoney() {
        return purchaseMoney;
    }
    public void setPurchaseMoney(String purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getPurchaseFinalMoney() {
        return purchaseFinalMoney;
    }
    public void setPurchaseFinalMoney(String purchaseFinalMoney) {
        this.purchaseFinalMoney = purchaseFinalMoney;
    }
    //    @Column(columnDefinition = "nvarchar(2000)")
//    public String getSuspendedReason() {
//        return suspendedReason;
//    }
//    public void setSuspendedReason(String suspendedReason) {
//        this.suspendedReason = suspendedReason;
//    }

    public int getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


    public Timestamp getEndTime() {
        return endTime;
    }
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getFormalAttach() {
        return formalAttach;
    }
    public void setFormalAttach(String formalAttach) {
        this.formalAttach = formalAttach;
    }

    @Column(columnDefinition = "nvarchar(200)",nullable =false)
    public String getPurchaseTitle() {
        return purchaseTitle;
    }
    public void setPurchaseTitle(String purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public int getLastNode() {
        return lastNode;
    }
    public void setLastNode(int lastNode) {
        this.lastNode = lastNode;
    }

//    @Column(nullable =false)
//    public Timestamp getUpdateTime() {
//        return updateTime;
//    }
//    public void setUpdateTime(Timestamp updateTime) {
//        this.updateTime = updateTime;
//    }

    //@Column(nullable =false)
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

//    public boolean isSuspended() {
//        return suspended;
//    }
//    public void setSuspended(boolean suspended) {
//        this.suspended = suspended;
//    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="applier")
    public SystemUserInfor getApplier() {
        return applier;
    }
    public void setApplier(SystemUserInfor applier) {
        this.applier = applier;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="department")
    public OrganizeInfor getDepartment() {
        return department;
    }
    public void setDepartment(OrganizeInfor department) {
        this.department = department;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="flowId")
    public PurchaseFlowDefinition getFlowId() {
        return flowId;
    }
    public void setFlowId(PurchaseFlowDefinition flowId) {
        this.flowId = flowId;
    }

//    @OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)
//    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
//    public Set<InstanceToken> getTokens() {
//        return tokens;
//    }
//    public void setTokens(Set<InstanceToken> tokens) {
//        this.tokens = tokens;
//    }

//    @OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)
//    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
//    @OrderBy("layer")
//    public Set<InstanceLayerInfor> getLayers() {
//        return layers;
//    }
//    public void setLayers(Set<InstanceLayerInfor> layers) {
//        this.layers = layers;
//    }


    public Timestamp getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="managerId")
    public SystemUserInfor getManager() {
        return manager;
    }
    public void setManager(SystemUserInfor manager) {
        this.manager = manager;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getManagerAttachment() {
        return managerAttachment;
    }
    public void setManagerAttachment(String managerAttachment) {
        this.managerAttachment = managerAttachment;
    }
    public boolean isManagerChecked() {
        return managerChecked;
    }
    public void setManagerChecked(boolean managerChecked) {
        this.managerChecked = managerChecked;
    }
    public int getManagerCheckStatus() {
        return managerCheckStatus;
    }
    public void setManagerCheckStatus(int managerCheckStatus) {
        this.managerCheckStatus = managerCheckStatus;
    }

    @Column(columnDefinition = "nText")
    public String getManagerWord() {
        return managerWord;
    }
    public void setManagerWord(String managerWord) {
        this.managerWord = managerWord;
    }

//    @Column(columnDefinition = "nvarchar(200)")
//    public String getSubmiterWord() {
//        return submiterWord;
//    }
//    public void setSubmiterWord(String submiterWord) {
//        this.submiterWord = submiterWord;
//    }
//    public Timestamp getViceCheckTime() {
//        return viceCheckTime;
//    }
//    public void setViceCheckTime(Timestamp viceCheckTime) {
//        this.viceCheckTime = viceCheckTime;
//    }

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="viceManagerId")
//    public SystemUserInfor getViceManager() {
//        return viceManager;
//    }
//    public void setViceManager(SystemUserInfor viceManager) {
//        this.viceManager = viceManager;
//    }

    public boolean isFiled() {
        return filed;
    }
    public void setFiled(boolean filed) {
        this.filed = filed;
    }

//    @Column(columnDefinition = "nvarchar(500)")
//    public String getResAttach() {
//        return resAttach;
//    }
//    public void setResAttach(String resAttach) {
//        this.resAttach = resAttach;
//    }

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="chargerId",nullable = false)
//    public SystemUserInfor getCharger() {
//        return charger;
//    }
//    public void setCharger(SystemUserInfor charger) {
//        this.charger = charger;
//    }

    @Column(columnDefinition = "nText")
    public String getAttachMemo() {
        return attachMemo;
    }
    public void setAttachMemo(String attachMemo) {
        this.attachMemo = attachMemo;
    }


//    public int getStamped() {
//        return stamped;
//    }
//    public void setStamped(int stamped) {
//        this.stamped = stamped;
//    }
//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="modifyerId")
//    public SystemUserInfor getModifyer() {
//        return modifyer;
//    }
//    public void setModifyer(SystemUserInfor modifyer) {
//        this.modifyer = modifyer;
//    }

//    @OneToMany(mappedBy = "instance",fetch=FetchType.LAZY)
//    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
//    public Set<InstanceInforRight> getRights() {
//        return rights;
//    }
//    public void setRights(Set<InstanceInforRight> rights) {
//        this.rights = rights;
//    }
//    public Integer getOldInstanceId() {
//        return oldInstanceId;
//    }
//    public void setOldInstanceId(Integer oldInstanceId) {
//        this.oldInstanceId = oldInstanceId;
//    }
//    public boolean isHandOut() {
//        return handOut;
//    }
//    public void setHandOut(boolean handOut) {
//        this.handOut = handOut;
//    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER,mappedBy = "purchaseInfors",targetEntity = PurchasePackage.class)
    public Set<PurchasePackage> getPackages() {
        return packages;
    }
    public void setPackages(Set<PurchasePackage> packages) {
        this.packages = packages;
    }

    public String getGuige() {
        return guige;
    }
    public void setGuige(String guige) {
        this.guige = guige;
    }

    public String getApplication() {
        return application;
    }
    public void setApplication(String application) {
        this.application = application;
    }

    public String getYsType() {
        return ysType;
    }
    public void setYsType(String ysType) {
        this.ysType = ysType;
    }

    public String getGcfa() {
        return gcfa;
    }
    public void setGcfa(String gcfa) {
        this.gcfa = gcfa;
    }

    @OneToMany(mappedBy = "purchase",fetch=FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
    @OrderBy("layer")
    public Set<PurchaseLayerInfor> getLayers() {
        return layers;
    }
    public void setLayers(Set<PurchaseLayerInfor> layers) {
        this.layers = layers;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getHetongId() {
        return hetongId;
    }

    public void setHetongId(int hetongId) {
        this.hetongId = hetongId;
    }

    public String getViceManagerWord() {
        return viceManagerWord;
    }

    public void setViceManagerWord(String viceManagerWord) {
        this.viceManagerWord = viceManagerWord;
    }

    public Timestamp getViceManagercheckTime() {
        return viceManagercheckTime;
    }

    public void setViceManagercheckTime(Timestamp viceManagercheckTime) {
        this.viceManagercheckTime = viceManagercheckTime;
    }

    public boolean isViceManagerChecked() {
        return viceManagerChecked;
    }

    public void setViceManagerChecked(boolean viceManagerChecked) {
        this.viceManagerChecked = viceManagerChecked;
    }

    public String getViceManagerAttachment() {
        return viceManagerAttachment;
    }

    public void setViceManagerAttachment(String viceManagerAttachment) {
        this.viceManagerAttachment = viceManagerAttachment;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="viceManagerId")
    public SystemUserInfor getViceManager() {
        return viceManager;
    }

    public void setViceManager(SystemUserInfor viceManager) {
        this.viceManager = viceManager;
    }

    public int getViceManagerCheckStatus() {
        return viceManagerCheckStatus;
    }

    public void setViceManagerCheckStatus(int viceManagerCheckStatus) {
        this.viceManagerCheckStatus = viceManagerCheckStatus;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }
}
