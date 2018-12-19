package com.kwchina.oa.purchase.contract.entity;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Create by yuanjl on 2018/5/29
 */
@Data
@Entity
@Table(name = "purchase_ContractCheckInfo", schema = "dbo")
@ObjectId(id = "contractCheckId")
public class ContractCheckInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contractCheckId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkerId")
    private SystemUserInfor checker;        //审批人
    private String checkOpinion;            // 审核意见
    private Integer checkResult;            //审核结果 0：不同意；1：同意；
    private String checkAttach;             //审核附件
    private Date checkTime;                 //审核时间
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractID")
    private ContractInfo contractInfo;      //三方信息
    private int layer;                        //审核层级 1：采购领导；2：归口领导 3：审批小组
}
