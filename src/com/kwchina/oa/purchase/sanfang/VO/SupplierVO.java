package com.kwchina.oa.purchase.sanfang.VO;

import lombok.Data;

import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/8/1 17:09
 *
 * @desc
 */
@Data
public class SupplierVO {
    private Integer supplierID;                //供方ID
    private String supplierName;                //供方名称
    private String supplierAddress;             //供方地址
    private String supplierContact;             //联系方式
    private String serviceDetail;               //服务明细
    private String serviceYear;                 //服务时长
    private String purchaseTypeMsg;
    private String single;
    private String supplierStatus;
    private String expirationTime;                    //过期时间
    private String sponsorName;                     //发起人姓名
    private Integer sponsorId;                  //发起人ID
    private String startTime;                   //发起时间
    private List<SupplierCheckVO> checkVOS;
}
