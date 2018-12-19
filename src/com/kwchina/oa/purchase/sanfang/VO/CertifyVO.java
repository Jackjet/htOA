package com.kwchina.oa.purchase.sanfang.VO;

import com.kwchina.oa.purchase.sanfang.entity.SupplierCheckInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.VO
 * 2018/8/1 17:09
 *
 * @desc
 */
@Data
public class CertifyVO {
    private Integer certifyId;
    private Integer supplierID;                //供方ID
    private String quality;
    private String price;
    private String service;
    private String delivery;
    private String management;
    private String endTime;
    private int[] personIds;
}
