package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import lombok.Data;

import javax.persistence.*;

/**
 * Create by yuanjl on 2018/5/30
 */
@Entity
@Table(name = "purchase_score", schema = "dbo")
@ObjectId(id="zhaotouScoreId")
@Data
public class ZhaotouScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zhaotouScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkerId")
    private SystemUserInfor checker;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateInfoId")
    private ZhaotouTemplateInfo zhaotouTemplateInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierId")
    private SupplierDesc supplierDesc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidInfoId",nullable = false)
    private BidInfo bidInfo;
    private Integer score;
}
