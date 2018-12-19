package com.kwchina.oa.purchase.contract.VO;

import lombok.Data;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class ContractVo {
    private Integer contractID;                          //ID
    private String contractName;                        // 三方名称
    private String purchaseTypeName;                           //采购类型
    private String contractCode;                       //三方流程ID
    private String contractDesc;                         //需求说明
    private String applierName;            // 申请人
    private String applyDept;            // 所属部门
    private String applyDate;                        //申请时间
    private String cav;
    private String techSolution;
    private String sav;

    private String supplierName;                           //供应商1
    private String supplierContact;                            //联系方式1
    private String projectName;                       //供应商1方案信息
    private String projectModel;
    private String projectCount;
    private String projectMemo;
    private double lastYearPrice;                       //码头报价1
    private double thisYearPrice;                       //物流报价1
    private String conclusion;                        //结论
    private String executorName;             //经办人
    private String contractDate;                           //发起时间

    private Integer purchaseId;
}
