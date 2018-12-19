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
@Table(name = "supplier_CheckInfo", schema = "dbo")
public class SupplierCheckInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierCheckID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkerId")
    private SystemUserInfor checker;        //审批人
    private String checkOpinion;            // 审核意见
    private Integer checkResult;            //审核结果 0：不同意；1：同意；
    private String checkAttach;             //审核附件
    private Date checkTime;                 //审核时间
    private boolean lastOne;                //是否最新的一条
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierID")
    private SupplierInfor supplierInfor;
    private int layer;
}
