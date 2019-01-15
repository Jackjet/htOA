package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;

/**
 *  供方相关资质
 * @author : JJ-Lee
 * @date : 2018-12-26 10:04
 **/
@Data
@Entity
@Table(name = "supplier_qualification", schema = "dbo")
@ObjectId(id = "qualificationId")
 public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qualificationId;
    private String qualificationCode;
    private String qualificationName;
    private String endTime;
    private String qualificationAttach;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierID")
    private SupplierInfor supplierInfor;
}
