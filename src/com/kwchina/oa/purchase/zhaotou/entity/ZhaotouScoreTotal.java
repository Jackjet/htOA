package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.extend.template.entity.ZhaotouTemplateInfo;
import lombok.Data;

import javax.persistence.*;

/**
 * Create by yuanjl on 2018/5/30
 */
@Entity
@Table(name = "purchase_score_total", schema = "dbo")
@ObjectId(id="scoreTotalId")
@Data
public class ZhaotouScoreTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scoreTotalId;
    private String SupplierName;
    private double price;
    private double jsAvgScore;
    private double swAvgScore;
    private double totalScore;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidInfoId",nullable = false)
    private BidInfo bidInfo;
}
