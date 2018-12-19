package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.entity
 * 2018/7/27 18:20
 *
 * @desc
 */

@Entity
@Table(name = "supplier_certify", schema = "dbo")
@Data
public class CertifyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer certifyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierID")
    private SupplierInfor supplierInfor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsorId")
    private SystemUserInfor sponsor;
    private Date spoDate;
    private String quality;
    private String price;
    private String service;
    private String delivery;
    private String management;
    private Date endDate;

}
