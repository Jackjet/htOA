package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

/**
 * Create by lijj
 * 三方信息
 */
@Entity
@Table(name = "purchase_sanfangInfor", schema = "dbo")
@ObjectId(id="sanfangID")
@Data
public class SanfangInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sanfangID;
    private Integer purchaseId;                             //所属采购
    private String sanfangTitle;                              // 三方名称
    private String purchaseType;                           //采购类型
    private Integer sanfangStatus;                       // 三方状态,0:待发起；1：采购领导审批；2：归口审批；3：小组审批；-1：中止
    private String sanfangAttach;                        // 申请人提交的附件
    private String sanfangDesc;                         //需求说明
    private String sanfangFlowId;                       //三方流水号
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applierId")
    private SystemUserInfor sanfangApplier;            // 申请人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private OrganizeInfor sanfangDepartment;            // 所属部门
    private Date applyDate;                        //申请时间
    private String sanfangConclusion;                        //结论
    private String sanfangFinalSupplier;                //最终选定供应商
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseChargeId")
    private SystemUserInfor purchaseCharge;             //采购负责人
    private Date startDate;                           //发起时间
    private Date endDate;                           //发起时间
    private double sanfangFinalMoney;                  //最终价格
    @OneToMany(mappedBy = "sanfangInfor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SupplierMsg> supplierInfors = new ArrayList<>();       //供应商信息
    @OneToMany(mappedBy = "sanfangInfor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SanfangCheckInfor> checkInfors = new ArrayList<SanfangCheckInfor>();       //审核信息

}





























