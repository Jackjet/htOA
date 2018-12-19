package com.kwchina.oa.purchase.contract.entity;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by lijj
 * 三方信息
 */
@Entity
@Table(name = "purchase_contractInfo", schema = "dbo")
@ObjectId(id="contractID")
@Data
public class ContractInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contractID;                             //主键
    private String contractName;                            //合同标题
    private Integer purchaseId;                             //所属采购Id
    private Integer purchaseType;                           //采购类型
    private String contractCode;                           //文件编号
    private Integer contractStatus;                       // 变更状态,0:待发起；1：采购部负责人；2：相关部门负责人；3：小组审批；-1：中止
    private String supplierName;                        // 供应商名称
    private String supplierContact;                         //联系方式
    private String contractDesc;                       //变更内容描述
    private String contractAttach;                      //变更附件
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private OrganizeInfor submitDepartment;            // 提交部门
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applierId")
    private SystemUserInfor contractApplier;                    // 申请人
    private Date submitDate;                             //提交时间
    private String techSolution;                        //技术方案
    private String solutionAttach;                       //方案附件
    private String projectName;                         //采购项目
    private String projectModel;                               //规格型号
    private String projectCount;                                  //数量
    private double lastYearPrice;                       //去年价格
    private double thisYearPrice;                       //今年价格
    private String projectMemo;                           //备注
    private boolean isParity;                           //是否比价 1：比  0：不比
    private String parityAttach;                         //比价附件
    private boolean isBid;                           //是否招标 1：招  0：不招
    private String bidAttach;                           //招标附件
    private String conclusion;                          //结论
    private Date contractTime;                          //日期
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executorId")
    private SystemUserInfor executor;                    // 经办人
    @OneToMany(mappedBy = "contractInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ContractCheckInfo> checkInfos = new ArrayList<>();       //审核信息
}





























