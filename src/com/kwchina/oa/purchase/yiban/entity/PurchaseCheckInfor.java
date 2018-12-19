package com.kwchina.oa.purchase.yiban.entity;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Create by yuanjl on 2018/5/29
 */
@Entity
@Table(name = "Purchase_CheckInfor", schema = "dbo")
public class PurchaseCheckInfor {


    public static int Check_Org_Caigou = 89;
//  public static int Check_Person_Guikoulingdao = 208;
    public static int Check_Role_Caiwu = 56;
    public static int Check_Role_Guikou = 54;
    public static int Check_Role_Caigou = 55;
    public static int Check_Role_Gongsilingdao = 64;
    public static int Check_Role_Caigoujuese = 60;
    public static int Check_Role_Jiguibulingdao = 61;
    public static int Check_Role_caigouGroup = 57;
    public static int Check_Role_zhaptouScore = 58;
    public static int Check_Role_zhaotouCommit = 59;
    public static int Check_Role_fgLeader = 49;
    public static int Check_Role_fenguancaigou = 65;

    private Integer checkInforId;

    private SystemUserInfor checker;                                //审批人
    private RoleInfor checkRoler;                                //审批角色

    private Timestamp startDate; 				                    //开始时间
    private Timestamp endDate;			                    	// 审核时间

    private String attatchment; 					            	// 审核人提交的附件
    private int status;                                        //审核状态0-未审批 1-审批通过 2-审批不通过 3-退回

    private String checkComment;				                    	// 审核意见
    private String CheckMemo; 			                        	//审核信息

    private PurchaseLayerInfor layerInfor;			        //所属流程



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getCheckInforId() {
        return checkInforId;
    }
    public void setCheckInforId(Integer checkInforId) {
        this.checkInforId = checkInforId;
    }

    @Column(columnDefinition = "nvarchar(500)")
    public String getAttatchment() {
        return attatchment;
    }
    public void setAttatchment(String attatchment) {
        this.attatchment = attatchment;
    }

    @Column(columnDefinition = "ntext")
    public String getCheckComment() {
        return checkComment;
    }
    public void setCheckComment(String checkComment) {
        this.checkComment = checkComment;
    }

    public Timestamp getEndDate() {
        return endDate;
    }
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(nullable =false)
    public Timestamp getStartDate() {
        return startDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }


    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="personId")
    public SystemUserInfor getChecker() {
        return checker;
    }
    public void setChecker(SystemUserInfor checker) {
        this.checker = checker;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="roleId")
    public RoleInfor getCheckRoler() {
        return checkRoler;
    }
    public void setCheckRoler(RoleInfor checkRoler) {
        this.checkRoler = checkRoler;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="layerId",nullable=false)
    public PurchaseLayerInfor getLayerInfor() {
        return layerInfor;
    }
    public void setLayerInfor(PurchaseLayerInfor layerInfor) {
        this.layerInfor = layerInfor;
    }



    public String getCheckMemo() {
        return CheckMemo;
    }
    public void setCheckMemo(String checkMemo) {
        CheckMemo = checkMemo;
    }
}
