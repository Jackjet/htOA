package com.kwchina.oa.purchase.zhaotou.entity;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.zhaotou.entity
 * 2018/8/22 14:24
 *
 * @desc
 */
@Data
@Entity
@Table(name = "purchase_zhaotouSupplier", schema = "dbo")
@ObjectId(id="supplierMsgId")
public class SupplierDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierDescId;         //主键
    private String supplierName;         //供应商名称
    private String managerRate;             //管理费率
    private String constructRate;           //施工费率
    private String qualification;           //资质
    private String responseTime;            //维修响应时间
    private String shelflife;               //质保期
    private String attach;                  //附件
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidInfoID",nullable = false)
    private BidInfo bidInfo;                //关联开标信息

}
