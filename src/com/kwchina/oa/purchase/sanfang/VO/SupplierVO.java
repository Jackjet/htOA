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
    private Integer supplierID;
    private String supplierName;
    private String supplierAddress;
    private String supplierTel;
    private String contactName;
    private String supplierContact;
    private String serviceDetail;
    private String companyType;
    private String relevance;
    private String purchaseTypeMsg;
    private String pass;
    private String singleOne;
    private String serviceYear;
    private String supplierStatus;
    private String expirationTime;
    private String sponsorName;
    private Integer sponsorId;
    private String startTime;
    private List<Integer> organizeIDs;
    private List<String> organizeNames;
    private List<SupplierCheckVO> checkVOS;
    private List<QualificationVO> qualificationVOS;
    private List<BackgroundVO> backgroundVOS;
}
