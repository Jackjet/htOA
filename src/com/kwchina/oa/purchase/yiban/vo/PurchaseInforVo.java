package com.kwchina.oa.purchase.yiban.vo;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;

import java.sql.Timestamp;

/**
 * Create by yuanjl on 2018-7-30
 */
public class PurchaseInforVo {

    private Integer purchaseId; 				 // 审核实例Id
    private String guikouDepartment;             //归口部门
    private String purchaseGoods;                //采购物品
    private int purchaseNumber;                  //数量
    private String startTime; 				     // 开始时间
    private String applier; 		        	 // 申请人
    private String guige;                       //规格
    private String application;                 //用途
    private String ysType;                      //预算类别
    private String gcfa;                        //工程方案
    private String supplierName;
    private String department; 		        	// 所属部门
    private String purchaseTitle; 				// 实例名称
    private String purchaseStr1; 				//
    private String purchaseStr2; 				//
    private int manager;			            // 审核人
    private int viceManagerId;			            // 审核人
    private String[] attatchArray = {}; 		// 附件路径
    private String[] purchaseTitles = {}; 		// 附件路径
    private String[] purchaseNumbers = {}; 		// 附件路径
    private String[] purchaseGoodss = {}; 		// 附件路径
    private String[] guiges = {}; 		// 附件路径
    private String[] applications = {}; 		// 附件路径


    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getGuikouDepartment() {
        return guikouDepartment;
    }

    public void setGuikouDepartment(String guikouDepartment) {
        this.guikouDepartment = guikouDepartment;
    }

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getApplier() {
        return applier;
    }

    public void setApplier(String applier) {
        this.applier = applier;
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

        public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(String purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public String[] getAttatchArray() {
        return attatchArray;
    }

    public void setAttatchArray(String[] attatchArray) {
        this.attatchArray = attatchArray;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getViceManagerId() {
        return viceManagerId;
    }

    public void setViceManagerId(int viceManagerId) {
        this.viceManagerId = viceManagerId;
    }

    public String[] getPurchaseTitles() {
        return purchaseTitles;
    }

    public void setPurchaseTitles(String[] purchaseTitles) {
        this.purchaseTitles = purchaseTitles;
    }

    public String[] getPurchaseNumbers() {
        return purchaseNumbers;
    }

    public void setPurchaseNumbers(String[] purchaseNumbers) {
        this.purchaseNumbers = purchaseNumbers;
    }

    public String[] getPurchaseGoodss() {
        return purchaseGoodss;
    }

    public void setPurchaseGoodss(String[] purchaseGoodss) {
        this.purchaseGoodss = purchaseGoodss;
    }

    public String[] getGuiges() {
        return guiges;
    }

    public void setGuiges(String[] guiges) {
        this.guiges = guiges;
    }

    public String[] getApplications() {
        return applications;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
    }

    public String getPurchaseStr1() {
        return purchaseStr1;
    }

    public void setPurchaseStr1(String purchaseStr1) {
        this.purchaseStr1 = purchaseStr1;
    }

    public String getPurchaseStr2() {
        return purchaseStr2;
    }

    public void setPurchaseStr2(String purchaseStr2) {
        this.purchaseStr2 = purchaseStr2;
    }
}
