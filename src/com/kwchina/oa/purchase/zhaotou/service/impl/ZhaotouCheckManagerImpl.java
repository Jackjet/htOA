package com.kwchina.oa.purchase.zhaotou.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.purchase.zhaotou.VO.BidListVO;
import com.kwchina.oa.purchase.zhaotou.dao.BidInfoDAO;
import com.kwchina.oa.purchase.zhaotou.dao.ZhaotouCheckDAO;
import com.kwchina.oa.purchase.zhaotou.entity.BidInfo;
import com.kwchina.oa.purchase.zhaotou.entity.ZhaotouCheckInfor;
import com.kwchina.oa.purchase.zhaotou.enums.ZhaotouStatusEnum;
import com.kwchina.oa.purchase.zhaotou.service.BidInfoManager;
import com.kwchina.oa.purchase.zhaotou.service.ZhaotouCheckManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZhaotouCheckManagerImpl extends BasicManagerImpl<ZhaotouCheckInfor> implements ZhaotouCheckManager {
	private ZhaotouCheckDAO zhaotouCheckDAO;
	@Autowired
	public void setZhaotouCheckDAO(ZhaotouCheckDAO zhaotouCheckDAO) {
		this.zhaotouCheckDAO = zhaotouCheckDAO;
		super.setDao(zhaotouCheckDAO);
	}

}

