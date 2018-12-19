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
public class DingbiaoVO {
    private Integer bidInfoId;
    private String supplierName1;
    private String supplierName2;
    private String supplierName3;
    private String supplierName4;
    private String supplierName5;
    private double price1;
    private double price2;
    private double price3;
    private double price4;
    private double price5;
    private double jsAvgScore1;
    private double jsAvgScore2;
    private double jsAvgScore3;
    private double jsAvgScore4;
    private double jsAvgScore5;
    private double swAvgScore1;
    private double swAvgScore2;
    private double swAvgScore3;
    private double swAvgScore4;
    private double swAvgScore5;
    private double totalScore1;
    private double totalScore2;
    private double totalScore3;
    private double totalScore4;
    private double totalScore5;
    private String zhaotouBudget;
    private String zhaotouFinalSupplierName;   //最终供应商
    private String zhaotouFinalMoney;                  //最终价格
    private String zhaotouConclusion;                  // 定标结论
}