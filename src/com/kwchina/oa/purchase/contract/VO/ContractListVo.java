package com.kwchina.oa.purchase.contract.VO;

import lombok.Data;

import java.util.Date;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/8/1 17:09
 *
 * @desc
 */
@Data
public class ContractListVo {
    private Integer contractID;
    private String contractName;
    private String applierName;
    private String departmentName;
    private Date startTime;
    private String contractCode;
    private String contractStatus;
}
