package com.kwchina.oa.purchase.sanfang.VO;

import lombok.Data;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class SupplierCheckVO {
    private Integer supplierID;
    private Integer checkResult;
    private String checkOpinion;
    private String checkDate;
    private String checkerName;
    private boolean lastOne;
    private Integer layer;
}