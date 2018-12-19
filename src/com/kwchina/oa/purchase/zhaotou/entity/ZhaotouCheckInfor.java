package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Create by yuanjl on 2018/5/29
 */
@Entity
@Table(name = "purchase_zhaotouCheckInfo", schema = "dbo")
@ObjectId(id="zhaotouCheckInfoId")
@Data
public class ZhaotouCheckInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zhaotouCheckInforId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidInfoId",nullable = false)
    private BidInfo bidInfo;               //所属招投标
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkerId")
    private SystemUserInfor checker;            //审批人
    private Date checkTime;				// 审核时间
    private Integer checkResult;				// 审核意见    0:不同意；1：同意
    private String checkMemo; 				    //审核信息

}
