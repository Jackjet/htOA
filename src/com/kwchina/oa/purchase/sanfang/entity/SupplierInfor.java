package com.kwchina.oa.purchase.sanfang.entity;

import com.kwchina.core.base.entity.OrganizeInfor;
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
    private Integer supplierID;
    /**供方名称*/
    private String supplierName;
    /**供方地址*/
    private String supplierAddress;
    /**联系人*/
    private String contactName;
    /**联系电话*/
    private String supplierContact;
    /**服务明细*/
    private String serviceDetail;
    /**服务年限*/
    private String serviceYear;
    /**单一供方*/
    private boolean single;
    /**归口部门*/
    @ManyToMany(targetEntity = OrganizeInfor.class,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "supplier_dept_Link",schema = "dbo",joinColumns = {@JoinColumn(name = "supplierID")},inverseJoinColumns = {@JoinColumn(name = "organizeId")})
    @OrderBy("organizeId")
    private Set<OrganizeInfor> organizes = new HashSet<>(0);
    /**公司性质*/
    private String companyType;
    /**采购分类 0：一般采购；1：业务采购；2：工程采购；3：零星采购*/
    private Integer purchaseType;
    /**是否通过质量体系认证 1：是；0：否*/
    private boolean passISO;
    /**状态*/
    private Integer status;
    /**供方相关资质 */
    @OneToMany(mappedBy = "supplierInfor", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Qualification> qualifications=new ArrayList<>();
    /**供方背景调查 */
    @OneToMany(mappedBy = "supplierInfor", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<BackGround> backGrounds=new ArrayList<>();
    /**关联性*/
    private String relevance;
    /**过期时间*/
    private Date expiration;
    /**发起人*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsorId")
    private SystemUserInfor sponsor;
    /**新增时间*/
    private Date startDate;
    /**审核信息*/
    @OneToMany(mappedBy = "supplierInfor", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<SupplierCheckInfor> checkInfors = new ArrayList<>();
    /**认证信息*/
    @OneToMany(mappedBy = "supplierInfor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CertifyInfo> certifyInfos=new ArrayList<>();
    /**0：无效   1：有效*/
    private boolean valid;
}
