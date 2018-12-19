package com.kwchina.oa.purchase.sanfang.VO;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.purchase.sanfang.entity.SanfangCheckInfor;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class FormVo {
    private Integer sanfangID;                          //三方ID
    private String sanfangTitle;                        // 三方名称
    private String purchaseType;                           //采购类型
    private String sanfangFlowId;                       //三方流程ID
    private Integer sanfangStatus;                            // 三方状态,0:待发起；1：采购负责人审批；2：归口审批；3：小组审批；-1：中止
    private String sanfangDesc;                         //需求说明
    private String sanfangApplier;            // 申请人
    private String sanfangDepartment;            // 所属部门
    private String applyDate;                        //申请时间
    private String sav;

   // private boolean sanfangFiled;                        // 是否归档：0-否;1-是
    private String supplier1;                           //供应商1
    private String contact1;                            //联系方式1
    private String supplierMemo1;                       //供应商1方案信息
    private double sanfangMTPrice1;                       //码头报价1
    private double sanfangWLPrice1;                       //物流报价1
    private double price1;
    private String sav1;

    private String supplier2;                           //供应商2
    private String contact2;                            //联系方式2
    private String supplierMemo2;                       //供应商2方案信息
    private double sanfangMTPrice2;                       //码头报价2
    private double sanfangWLPrice2;                       //物流报价2
    private double price2;                       //报价2
    private String sav2;

    private String supplier3;                           //供应商3
    private String contact3;                            //联系方式3
    private String supplierMemo3;                       //供应商3方案信息
    private double sanfangMTPrice3;                       //码头报价3
    private double sanfangWLPrice3;                       //物流报价3
    private double price3;                       //报价3
    private String sav3;

    private String sanfangConclusion;                        //结论
    private String sanfangFinalSupplier;                //最终选定供应商
    private String purchaseCharge;             //采购负责人
    private String startDate;                           //发起时间
    private double sanfangFinalMoney;                  //最终价格
    //审核层1信息
    private String checker;                             //审核人
    private String checkOpinion;                        //审核意见
    private Integer checkResult;                            //审核结果  0:不同意；1：同意
    private String checkDate;                            //审核日期
    private Integer purchaseId;                           //采购Id;

}
