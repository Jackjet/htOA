package com.kwchina.oa.purchase.yiban.service.impl;

import com.kwchina.core.base.service.*;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.yiban.dao.PurchaseCheckDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseInforDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchaseLayerDAO;
import com.kwchina.oa.purchase.yiban.dao.PurchasePackageDao;
import com.kwchina.oa.purchase.yiban.entity.PurchasePackage;
import com.kwchina.oa.purchase.yiban.service.PackageManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseLayerInforManager;
import com.kwchina.oa.purchase.yiban.service.PurchaseManager;
import com.kwchina.oa.workflow.dao.FlowNodeDAO;
import com.kwchina.oa.workflow.service.InstanceInforRightManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Create by yuanjl on 2018-9-27 0027
 */
@Service("packageManager")
public class PackageManagerImpl extends BasicManagerImpl<PurchasePackage> implements PackageManager {
    @Resource
    private PurchaseManager purchaseManager;

    @Autowired
    private PurchasePackageDao purchasePackageDao;

    @Autowired
    private FlowNodeDAO flowNodeDAO;

    @Autowired
    private PersonInforManager personManager;
    @Autowired
    private PurchaseInforDAO purchaseInforDAO;

    @Autowired
    private PurchaseLayerDAO purchaseLayerDAO;

    @Autowired
    private PurchaseCheckDAO purchaseCheckDAO;

    @Autowired
    private PurchaseLayerInforManager purchaseLayerInforManager;

    @Autowired
    private VirtualResourceManager resourceManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private OperationDefinitionManager operationDefinitionManager;

    @Autowired
    private InstanceInforRightManager instanceInforRightManager;
    @Resource
    private OrganizeManager organizeManager;
    @Autowired
    public void setPurchasePackageDao(PurchasePackageDao purchasePackageDao) {
        this.purchasePackageDao = purchasePackageDao;
        super.setDao(purchasePackageDao);
    }




}
