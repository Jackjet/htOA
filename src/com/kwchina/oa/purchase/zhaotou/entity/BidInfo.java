package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by yuanjl on 2018/5/30
 */
@Entity
@Table(name = "purchase_bidInfor", schema = "dbo")
@ObjectId(id="bidInfoId")
@Data
public class BidInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidInfoId;
    private Date startTime;				// 开标时间
    private String zbCode;                      //招标编号
    private  String projectName;                //项目名称
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="readerId")
    private SystemUserInfor reader;             //唱标人
    private Integer purchaseType;                //采购类型
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="supervisorId")
    private SystemUserInfor supervisor;         //开标监督人
    private Integer purchaseId;
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SupplierDesc> suppliers;       //供应商信息
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="templateId")
    private ZhaotouTemplate template;           //模板
    private String bidAttach;                   //附件
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applierId")
    private SystemUserInfor zhaotouApplier;            // 申请人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseExecutorId")
    private SystemUserInfor purchaseExecutor;             //采购经办人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private OrganizeInfor zhaotouDepartment;                   //使用部门
    private String zhaotouBudget;                       //预算
    private Date zhaotouEndTime;                   // 结束时间
    private String zhaotouMemo;                         // 招标项目情况
    private Integer zhaotouStatus;                      // 招投状态 0：开标 1：小组打分2：委员会评审3：完成 -1:终止

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="supplierId")
    private SupplierInfor zhaotouFinalSupplierInfor;   //最终供应商
    private String zhaotouFinalMoney;                  //最终价格
    private String zhaotouConclusion;                  // 定标结论
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ZhaotouScore> scores=new ArrayList<>();            //打分信息
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ZhaotouCheckInfor> checkInfors=new ArrayList<>();
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ZhaotouScoreTotal> totals=new ArrayList<>();

}
