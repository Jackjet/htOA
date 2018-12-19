package com.kwchina.oa.purchase.contract.VO;

import lombok.Data;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class ContractCheckVo {
    private Integer contractID;
    private Integer checkResult;
    private String checkMemo;
    private String checkDate;
}