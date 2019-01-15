package com.kwchina.oa.purchase.zhaotou.VO;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class BidInfoVO {
    private Integer bidInfoId;
    private String startDate;
    private String purchaseTypeMsg;
    private String projectName;
    private String readerName;
    private String zbCode;
    private String supervisorName;
    private Integer purchaseId;
    private String templateName;
    private String bav;
    private String zhaotouApplierName;
    private String purchaseExecutorName;
    private String departmentName;
    private Double zhaotouBudget;
    private Timestamp zhaotouEndTime;
    private String zhaotouMemo;
    private Integer zhaotouStatus;

    private String supplierName1;
    private Double unitPrice1;
    private Double totalPrice1;
    private String qualification1;
    private String responseTime1;
    private String memo1;

    private String supplierName2;
    private Double unitPrice2;
    private Double totalPrice2;
    private String qualification2;
    private String responseTime2;
    private String memo2;

    private String supplierName3;
    private Double unitPrice3;
    private Double totalPrice3;
    private String qualification3;
    private String responseTime3;
    private String memo3;

    private String supplierName4;
    private Double unitPrice4;
    private Double totalPrice4;
    private String qualification4;
    private String responseTime4;
    private String memo4;

    private String supplierName5;
    private Double unitPrice5;
    private Double totalPrice5;
    private String qualification5;
    private String responseTime5;
    private String memo5;

    private int[] personIds;

    private String sav1;
    private String sav2;
    private String sav3;
    private String sav4;
    private String sav5;
}