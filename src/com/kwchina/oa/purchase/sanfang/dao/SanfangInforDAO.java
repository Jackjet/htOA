package com.kwchina.oa.purchase.sanfang.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import java.util.List;


public interface SanfangInforDAO extends BasicDao<SanfangInfor> {
    /**
     * 获取所有三方信息
     */
    public List<SanfangInfor> getAllSanfang();
    /**
     * 根据id获取三方信息
     */
    public SanfangInfor getSanfangInfoByFlowId(String flowId);
}
