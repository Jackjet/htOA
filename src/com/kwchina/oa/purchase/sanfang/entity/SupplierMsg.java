package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.DTO
 * 2018/8/2 16:54
 *
 * @desc
 */
@Entity
@Table(name = "purchase_SupplierMsg", schema = "dbo")
@ObjectId(id="supplierMsgId")
@Data
public class SupplierMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierMsgId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierID")
    private SupplierInfor supplierInfor;
    private String supplierDesc;
    private String supplierAttach;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sanfangID",nullable = false)
    private SanfangInfor sanfangInfor;
    private double mtPrice;
    private double wlPrice;
    private double price;
}
