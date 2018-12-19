package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.entity
 * 2018/7/27 18:20
 *
 * @desc
 */

@Entity
@Table(name = "purchase_supplierInfo", schema = "dbo")
@ObjectId(id="supplierID")
@Data
public class SupplierInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierID;                //供方ID
    private String supplierName;                //供方名称
    private String supplierAddress;             //供方地址
    private String supplierContact;             //联系方式
    private String serviceDetail;               //服务明细
    private String serviceYear;                 //服务时长
    private Integer purchaseType;             //采购分类 0：一般采购；1：业务采购；2：工程采购；3：零星采购
    private boolean singleSupplier;           //是否单一供方 1：是；0：否
    private Integer status;
    private Date expiration;                    //过期时间
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsorId")
    private SystemUserInfor sponsor;            //发起人
    private Date startDate;                     //新增时间
    @OneToMany(mappedBy = "supplierInfor", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<SupplierCheckInfor> checkInfors = new ArrayList<>();       //审核信息
    @OneToMany(mappedBy = "supplierInfor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CertifyInfo> certifyInfos=new ArrayList<>();
    private boolean valid;                      //0：无效   1：有效
}
