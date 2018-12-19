package com.kwchina.oa.purchase.zhaotou.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import com.kwchina.oa.purchase.zhaotou.VO.BidListVO;
import com.kwchina.oa.purchase.zhaotou.dao.BidInfoDAO;
import com.kwchina.oa.purchase.zhaotou.dao.ZhaotouScoreDAO;
import com.kwchina.oa.purchase.zhaotou.entity.BidInfo;
import com.kwchina.oa.purchase.zhaotou.entity.ZhaotouScore;
import com.kwchina.oa.purchase.zhaotou.enums.ZhaotouStatusEnum;
import com.kwchina.oa.purchase.zhaotou.service.BidInfoManager;
import com.kwchina.oa.purchase.zhaotou.service.ZhaotouScoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZhaotouScoreManagerImpl extends BasicManagerImpl<ZhaotouScore> implements ZhaotouScoreManager {
	private ZhaotouScoreDAO zhaotouScoreDAO;
	@Autowired
	public void setZhaotouScoreDAO(ZhaotouScoreDAO zhaotouScoreDAO) {
		this.zhaotouScoreDAO = zhaotouScoreDAO;
		super.setDao(zhaotouScoreDAO);
	}
}

