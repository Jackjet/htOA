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
 * @Author :jjlee
 * @Date :Created in 9:59 2018/12/19
 * @Description :招投标表单
 * @Version :lastest
 */
@Entity
@Table(name = "purchase_bidInfor", schema = "dbo")
@ObjectId(id="bidInfoId")
@Data
public class BidInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidInfoId;
    /**开标时间*/
    private Date startTime;
    /**招标编号*/
    private String zbCode;
    /**项目名称*/
    private  String projectName;
    /**唱标人*/
    private String readerName;
    /**采购类型*/
    private Integer purchaseType;
    /**开标监督人*/
    private String supervisorName;
    /**招标编号*/
    private Integer purchaseId;
    /**供应商信息*/
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SupplierDesc> suppliers;
    /**模板*/
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="templateId")
    private ZhaotouTemplate template;
    /**附件*/
    private String bidAttach;
    /**申请人*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applierId")
    private SystemUserInfor zhaotouApplier;
    /**采购经办人*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseExecutorId")
    private SystemUserInfor purchaseExecutor;
    /**使用部门*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private OrganizeInfor zhaotouDepartment;
    /**预算*/
    private String zhaotouBudget;
    /**结束时间*/
    private Date zhaotouEndTime;
    /**招标项目情况*/
    private String zhaotouMemo;
    /**招投状态 0：开标 1：小组打分2：委员会评审3：完成 -1:终止*/
    private Integer zhaotouStatus;
    /**最终供应商*/
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="supplierId")
    private SupplierInfor zhaotouFinalSupplierInfor;
    /**最终价格*/
    private String zhaotouFinalMoney;
    /**定标结论*/
    private String zhaotouConclusion;
    /**打分信息*/
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ZhaotouScore> scores=new ArrayList<>();
    /**审核信息*/
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ZhaotouCheckInfor> checkInfors=new ArrayList<>();
    /**评分汇总*/
    @OneToMany(mappedBy = "bidInfo",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ZhaotouScoreTotal> totals=new ArrayList<>();

}
