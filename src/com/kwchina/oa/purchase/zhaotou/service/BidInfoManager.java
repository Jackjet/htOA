package com.kwchina.oa.purchase.zhaotou.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.meeting.entity.MeetInfor;
import com.kwchina.oa.meeting.vo.MeetInforVo;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import com.kwchina.oa.purchase.zhaotou.VO.BidListVO;
import com.kwchina.oa.purchase.zhaotou.entity.BidInfo;

import java.util.List;


public interface BidInfoManager extends BasicManager {

    /**
     * 获取所有已发起三方
     */
    List getAllBidInfo();
    //转化bidInfor为bidListVO
    public BidListVO transPOToVO(BidInfo bidInfo);
}

