package com.kwchina.oa.purchase.sanfang.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.sanfang.dao.SupplierCheckDAO;
import com.kwchina.oa.purchase.sanfang.entity.SupplierCheckInfor;
import com.kwchina.oa.purchase.sanfang.service.SupplierCheckManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierCheckManagerImpl extends BasicManagerImpl<SupplierCheckInfor> implements SupplierCheckManager {
    @Autowired
    private SupplierCheckDAO supplierCheckDAO;

    @Autowired
    public void setSupplierCheckDAO(SupplierCheckDAO supplierCheckDAO) {
        this.supplierCheckDAO= supplierCheckDAO;
        super.setDao(supplierCheckDAO);
    }

}
