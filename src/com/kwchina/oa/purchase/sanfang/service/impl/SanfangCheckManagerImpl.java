package com.kwchina.oa.purchase.sanfang.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.sanfang.dao.SanfangCheckDAO;
import com.kwchina.oa.purchase.sanfang.entity.SanfangCheckInfor;
import com.kwchina.oa.purchase.sanfang.service.SanfangCheckManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SanfangCheckManagerImpl extends BasicManagerImpl<SanfangCheckInfor> implements SanfangCheckManager {
    @Autowired
    private SanfangCheckDAO sanfangCheckDAO;

    @Autowired
    public void setSanfangCheckDAO(SanfangCheckDAO sanfangCheckDAO) {
        this.sanfangCheckDAO= sanfangCheckDAO;
        super.setDao(sanfangCheckDAO);
    }

}
