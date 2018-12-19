package com.kwchina.oa.purchase.sanfang.VO;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import lombok.Data;

import java.util.Date;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/8/1 13:00
 *
 * @desc
 */
@Data
public class SanfangListVO {
    private Integer sanfangID;                          //三方ID
    private String sanfangTitle;                        //三方标题
    private String sanfangApplier;            // 申请人
    private String sanfangDepartment;            // 所属部门
    private Date applyDate;                             //申请日期
    private String purchaseCode;                    //采购编号
    private String purchaseType;                //采购类型
    private String sanfangStatus;              // 三方状态,0:待发起；1：采购领导审批；2：归口审批；3：小组审批；4：审核完毕；-1：中止
}
