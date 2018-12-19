package com.kwchina.oa.purchase.zhaotou.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.meeting.vo.MeetInforVo;
import com.kwchina.oa.purchase.sanfang.enums.PurchaseTypeEnum;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.purchase.zhaotou.VO.BidListVO;
import com.kwchina.oa.purchase.zhaotou.dao.BidInfoDAO;
import com.kwchina.oa.purchase.zhaotou.entity.BidInfo;
import com.kwchina.oa.purchase.zhaotou.enums.ZhaotouStatusEnum;
import com.kwchina.oa.purchase.zhaotou.service.BidInfoManager;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class BidInfoManagerImpl extends BasicManagerImpl<BidInfo> implements BidInfoManager {
	private BidInfoDAO bidInfoDAO;
	@Autowired
	public void setBidInfoDAO(BidInfoDAO bidInfoDAO) {
		this.bidInfoDAO = bidInfoDAO;
		super.setDao(bidInfoDAO);
	}


	@Override
	public List getAllBidInfo() {
		String hql="from BidInfo where 1=1";
		List<BidInfo> resultByQueryString = getResultByQueryString(hql);
		return resultByQueryString;
	}

	@Override
	public BidListVO transPOToVO(BidInfo bidInfo) {
		BidListVO bidListVO=new BidListVO();
		bidListVO.setApplierName(bidInfo.getZhaotouApplier().getPerson().getPersonName());
		bidListVO.setBidInfoId(bidInfo.getBidInfoId());
		bidListVO.setDepartmentName(bidInfo.getZhaotouDepartment().getOrganizeName());
		bidListVO.setProjectName(bidInfo.getProjectName());
		bidListVO.setZbcode(bidInfo.getZbCode());
		bidListVO.setStartTime(bidInfo.getStartTime());
		bidListVO.setZhaotouStatus(EnumUtil.getByCode(bidInfo.getZhaotouStatus(),ZhaotouStatusEnum.class).getMsg());
		return bidListVO;
	}
}

