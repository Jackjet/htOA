package com.kwchina.oa.purchase.sanfang.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;

import java.util.List;


public interface SupplierInforDAO extends BasicDao<SupplierInfor> {

    public List<SupplierInfor> getAllSupplier();

    String getContactByName(String supplierName);
}
