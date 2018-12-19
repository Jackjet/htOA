package com.kwchina.oa.purchase.sanfang.entity;

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
@Table(name = "purchase_SanfangCheckInfor", schema = "dbo")
@ObjectId(id = "sanfangCheckId")
public class SanfangCheckInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sanfangCheckId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private RoleInfor roleInfor;              //角色
    private Integer type;                  //1:三方 2：招投标  3:合同变更
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkerId")
    private SystemUserInfor checker;        //审批人
    private String checkOpinion;            // 审核意见
    private Integer checkResult;            //审核结果 0：不同意；1：同意；
    private String checkAttach;             //审核附件
    private Date checkTime;                 //审核时间
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sanfangID")
    private SanfangInfor sanfangInfor;      //三方信息
    private int layer;                        //审核层级 1：采购领导；2：归口领导 3：审批小组

}
