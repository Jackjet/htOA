package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author :jjlee
 * @Date :Created in 9:59 2018/12/19
 * @Description :招投标供方信息
 * @Version :lastest
 */
@Data
@Entity
@Table(name = "purchase_zhaotouSupplier", schema = "dbo")
@ObjectId(id="supplierMsgId")
public class SupplierDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**主键*/
    private Integer supplierDescId;
    /**名称*/
    private String supplierName;
    /**单价*/
    private double unitPrice;
    /**总价*/
    private double totalPrice;
    /**单位资质*/
    private String qualification;
    /**交货周期*/
    private String responseTime;
    /**备注*/
    private String memo;
    /**附件*/
    private String attach;
    /**关联开标信息*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidInfoID",nullable = false)
    private BidInfo bidInfo;

}
