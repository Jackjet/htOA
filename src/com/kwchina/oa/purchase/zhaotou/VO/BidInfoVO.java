package com.kwchina.oa.purchase.zhaotou.VO;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import com.kwchina.oa.purchase.yiban.entity.PurchaseInfor;
import com.kwchina.oa.purchase.zhaotou.entity.SupplierDesc;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/7/27 16:23
 *
 * @desc
 */
@Data
public class BidInfoVO {
    private Integer bidInfoId;
    private String startDate;                // 开标时间
    private String purchaseTypeMsg;                      //采购类型
    private String projectName;                //项目名称
    private String readerName;                       //唱标
    private String zbCode;                    //招标编号
    private String supervisorName;                  //开标监督人
    private Integer purchaseId;                      //所属采购
    private String templateName;                     //模板
    private String bav;                   //附件
    private String zhaotouApplierName;              // 申请人
    private String purchaseExecutorName;             //采购经办人
    private String departmentName;            //使用部门
    private double zhaotouBudget;                //预算
    private Timestamp zhaotouEndTime;                   // 结束时间
    private String zhaotouMemo;                         // 招标项目情况
    private Integer zhaotouStatus;                      // 招投状态 0：开标 1：小组打分2：委员会评审3：完成 -1:终止
    private String supplierName1;
    private String managerRate1;
    private String constructRate1;
    private String qualification1;
    private String responseTime1;
    private String shelflife1;
    private String supplierName2;
    private String managerRate2;
    private String constructRate2;
    private String qualification2;
    private String responseTime2;
    private String shelflife2;
    private String supplierName3;
    private String managerRate3;
    private String constructRate3;
    private String qualification3;
    private String responseTime3;
    private String shelflife3;
    private String supplierName4;
    private String managerRate4;
    private String constructRate4;
    private String qualification4;
    private String responseTime4;
    private String shelflife4;
    private String supplierName5;
    private String managerRate5;
    private String constructRate5;
    private String qualification5;
    private String responseTime5;
    private String shelflife5;
    private String sav1;
    private String sav2;
    private String sav3;
    private String sav4;
    private String sav5;
}