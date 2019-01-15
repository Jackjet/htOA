package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;

/**
 * 供方背景调查
 * @author : JJ-Lee
 * @date : 2018-12-26 10:46
 **/
@Data
@Entity
@Table(name = "supplier_background", schema = "dbo")
@ObjectId(id = "backId")
 public class BackGround {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer backId;
    private Integer backCode;
    private String clientName;
    private String serviceContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierID")
    private SupplierInfor supplierInfor;
}
