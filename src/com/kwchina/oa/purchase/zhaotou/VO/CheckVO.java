package com.kwchina.oa.purchase.zhaotou.VO;

import lombok.Data;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class CheckVO {
    private Integer bidInfoId;
    private Integer checkResult;
    private String checkMemo;
    private String checkDate;
}