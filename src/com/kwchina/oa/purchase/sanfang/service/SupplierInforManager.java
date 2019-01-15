package com.kwchina.oa.purchase.sanfang.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.purchase.sanfang.VO.SupplierVO;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;

import java.util.List;


public interface SupplierInforManager extends BasicManager {
    List getAllSupplier();
    List<SupplierInfor> getInSupplier(Integer purchaseType,Integer deptId);
    String getContactByName(String supplierName);
    //根据名称获取部门信息
    SupplierInfor findBySupplierName(String SupplierName);
    SupplierVO transPOToVO(SupplierInfor supplierInfor);
    boolean validName(String supplierName);
}

