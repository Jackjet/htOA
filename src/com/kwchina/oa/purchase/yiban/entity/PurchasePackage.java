package com.kwchina.oa.purchase.yiban.entity;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Create by yuanjl on 2018/5/29
 */
@Entity
@Table(name = "Purchase_Package", schema = "dbo")
public class PurchasePackage {

    private Integer packageId;
    private String packageName;                 //包名
    private Set<PurchaseInfor> purchaseInfors;  //采购ID
    private int status;                         // 0未审批    1存在未审批  2 全部审批  3删除
    private SystemUserInfor manager;			// 审核人1
    private SystemUserInfor vicemanager;			// 审核人2
    private RoleInfor roleId;         //审批角色
    private int checkerType; 		//该节点审核者类型 0 人员 1 角色 2部门领导审核
    private Timestamp startDate; 				                    //开始时间
    private int flowId;                     //采购类型

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPackageId() {
        return packageId;
    }
    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @ManyToMany(targetEntity = PurchaseInfor.class,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "PurchasePackage_Link",schema = "dbo",joinColumns = {@JoinColumn(name = "packageId")},inverseJoinColumns = {@JoinColumn(name = "purchaseID")})
    public Set<PurchaseInfor> getPurchaseInfors() {
        return purchaseInfors;
    }
    public void setPurchaseInfors(Set<PurchaseInfor> purchaseInfors) {
        this.purchaseInfors = purchaseInfors;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="managerId")
    public SystemUserInfor getManager() {
        return manager;
    }
    public void setManager(SystemUserInfor manager) {
        this.manager = manager;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vicemanagerId")
    public SystemUserInfor getVicemanager() {
        return vicemanager;
    }
    public void setVicemanager(SystemUserInfor vicemanager) {
        this.vicemanager = vicemanager;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="roleId")
    public RoleInfor getRoleId() {
        return roleId;
    }
    public void setRoleId(RoleInfor roleId) {
        this.roleId = roleId;
    }

    public int getCheckerType() {
        return checkerType;
    }
    public void setCheckerType(int checkerType) {
        this.checkerType = checkerType;
    }

    @Column(nullable =false)
    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }
}
