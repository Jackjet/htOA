package com.kwchina.oa.purchase.yiban.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Create by yuanjl on 2018-7-18
 */
@Entity
@Table(name = "Purchase_LayerInfor", schema = "dbo")
public class PurchaseLayerInfor {
    private Integer layerId; 					//层次Id

    private String layerName;					//层次名称
    private String checkDemand; 				//审核说明（审核要求）
    private Timestamp startTime; 				//开始时间
    private Timestamp endTime; 					//结束时间

    private int layer; 							//层次（即第几个审核层次）
    private PurchaseNode purchaseNode;					//对应的Node
    private int status; 						//状态（0-删除  1-流转）

    private PurchaseInfor purchase; 		//审核实例
    private Set<PurchaseCheckInfor> checkInfors = new HashSet<PurchaseCheckInfor>(0);	//审核信息

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getLayerId() {
        return layerId;
    }
    public void setLayerId(Integer layerId) {
        this.layerId = layerId;
    }

    @Column(columnDefinition = "nvarchar(200)",nullable =false)
    public String getLayerName() {
        return layerName;
    }
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    @Column(columnDefinition = "ntext")
    public String getCheckDemand() {
        return checkDemand;
    }
    public void setCheckDemand(String checkDemand) {
        this.checkDemand = checkDemand;
    }


    public Timestamp getEndTime() {
        return endTime;
    }
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getLayer() {
        return layer;
    }
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Column(nullable =false)
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="purchaseId",nullable=false)
    public PurchaseInfor getPurchase() {
        return purchase;
    }
    public void setPurchase(PurchaseInfor purchase) {
        this.purchase = purchase;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="nodeId")
    public PurchaseNode getPurchaseNode() {
        return purchaseNode;
    }
    public void setPurchaseNode(PurchaseNode purchaseNode) {
        this.purchaseNode = purchaseNode;
    }


    @OneToMany(mappedBy = "layerInfor",fetch=FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
    @OrderBy("checkInforId")
    public Set<PurchaseCheckInfor> getCheckInfors() {
        return checkInfors;
    }
    public void setCheckInfors(Set<PurchaseCheckInfor> checkInfors) {
        this.checkInfors = checkInfors;
    }



}
