package com.kwchina.oa.purchase.zhaotou.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.purchase.zhaotou.VO.BidListVO;
import com.kwchina.oa.purchase.zhaotou.entity.BidInfo;

import java.util.List;

/**
 * BidInfoManager
 * @author : JJ-Lee
 * @date : 2018-12-19 11:25
 **/
public interface BidInfoManager extends BasicManager {
    /**
    * 所有招标
    * @return : java.util.List<com.kwchina.oa.purchase.zhaotou.entity.BidInfo>
    * @author : JJ-Lee
    * @date : 2018/12/19
    */ 
    List<BidInfo> getAllBidInfo();
    
    /**
    * po2vo
    * @param bidInfo 传入BidInfo实体
    * @return : com.kwchina.oa.purchase.zhaotou.VO.BidListVO
    * @author : JJ-Lee
    * @date : 2018/12/19
    */ 
    BidListVO transPOToVO(BidInfo bidInfo);
}
