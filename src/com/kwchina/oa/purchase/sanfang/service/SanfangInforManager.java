package com.kwchina.oa.purchase.sanfang.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.purchase.sanfang.VO.SanfangListVO;
import com.kwchina.oa.purchase.sanfang.VO.SanfangVO;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;

import java.util.List;


public interface SanfangInforManager extends BasicManager {

    /**
     * 获取所有已发起三方
     */
    public List getAllSanfangInfo();

    SanfangInfor getByFlowId(String flowId);
    SanfangListVO transToVo(SanfangInfor sanfangInfor);
}

