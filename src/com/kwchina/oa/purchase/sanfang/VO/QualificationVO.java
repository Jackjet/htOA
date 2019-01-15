package com.kwchina.oa.purchase.sanfang.VO;

import lombok.Data;

import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 */
@Data
public class QualificationVO {
    private Integer qualificationId;
    private String qualificationCode;
    private String qualificationName;
    private String endTime;
    private String attach;
    private Integer supplierID;
}