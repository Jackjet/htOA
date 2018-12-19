package com.kwchina.oa.purchase.yiban.vo;


public class PurchaseCheckInforVo {
	
	private Integer checkInforId;					//信息Id
	private int status;							//审批同意不同意
	private String checkComment; 				//审核内容
	private String[] attatchArray = {}; 		//附件路径
	private String packageId;
	private String purchaseFinalMoney;
	private String purchaseMoney;
	private String supplierName;
	private String ysType;
	private String gcfa;
	private int purchaseNumber;
	private String purchaseStr2; 				//






	
	
	
	public String[] getAttatchArray() {
		return attatchArray;
	}
	public void setAttatchArray(String[] attatchArray) {
		this.attatchArray = attatchArray;
	}
	public String getCheckComment() {
		return checkComment;
	}
	public void setCheckComment(String checkComment) {
		this.checkComment = checkComment;
	}

	public Integer getCheckInforId() {
		return checkInforId;
	}

	public void setCheckInforId(Integer checkInforId) {
		this.checkInforId = checkInforId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPurchaseFinalMoney() {
		return purchaseFinalMoney;
	}

	public void setPurchaseFinalMoney(String purchaseFinalMoney) {
		this.purchaseFinalMoney = purchaseFinalMoney;
	}

	public String getPurchaseMoney() {
		return purchaseMoney;
	}

	public void setPurchaseMoney(String purchaseMoney) {
		this.purchaseMoney = purchaseMoney;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public int getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(int purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public String getPurchaseStr2() {
		return purchaseStr2;
	}

	public void setPurchaseStr2(String purchaseStr2) {
		this.purchaseStr2 = purchaseStr2;
	}
}
