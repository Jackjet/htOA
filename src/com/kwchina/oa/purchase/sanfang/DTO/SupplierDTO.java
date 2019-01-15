package com.kwchina.oa.purchase.sanfang.DTO;

import com.kwchina.oa.purchase.sanfang.VO.BackgroundVO;
import com.kwchina.oa.purchase.sanfang.VO.QualificationVO;
import com.kwchina.oa.purchase.sanfang.VO.SupplierCheckVO;
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
public class SupplierDTO {
    private Integer supplierID;
    private String supplierName;
    private String supplierAddress;
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
    private Integer[] organizeIds;

    private String qualificationCode1;
    private String qualificationName1;
    private String endTime1;
    private String quav1;

    private String qualificationCode2;
    private String qualificationName2;
    private String endTime2;
    private String quav2;

    private String qualificationCode3;
    private String qualificationName3;
    private String endTime3;
    private String quav3;

    private String qualificationCode4;
    private String qualificationName4;
    private String endTime4;
    private String quav4;
    private String qualificationCode5;
    private String qualificationName5;
    private String endTime5;
    private String quav5;

    private Integer backCode1;
    private String clientName1;
    private String serviceContent1;
    private Integer backCode2;
    private String clientName2;
    private String serviceContent2;
    private Integer backCode3;
    private String clientName3;
    private String serviceContent3;
    private Integer backCode4;
    private String clientName4;
    private String serviceContent4;
    private Integer backCode5;
    private String clientName5;
    private String serviceContent5;
}
