package com.kwchina.oa.purchase.sanfang.service.impl;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.purchase.sanfang.VO.SanfangListVO;
import com.kwchina.oa.purchase.sanfang.dao.SanfangInforDAO;
import com.kwchina.oa.purchase.sanfang.entity.SanfangInfor;
import com.kwchina.oa.purchase.sanfang.enums.SanfangfStatusEnum;
import com.kwchina.oa.purchase.sanfang.service.SanfangInforManager;
import com.kwchina.oa.purchase.sanfang.utils.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanfangInforManagerImpl extends BasicManagerImpl<SanfangInfor> implements SanfangInforManager {
	private SanfangInforDAO sanfangInforDAO;
	@Autowired
	public void setSanfangInforDAO(SanfangInforDAO sanfangInforDAO) {
		this.sanfangInforDAO = sanfangInforDAO;
		super.setDao(sanfangInforDAO);
	}

	@Override
	public List getAllSanfangInfo() {
		return sanfangInforDAO.getAllSanfang();
	}

	@Override
	public SanfangInfor getByFlowId(String flowId) {
		return sanfangInforDAO.getSanfangInfoByFlowId(flowId);
	}

	@Override
	public SanfangListVO transToVo(SanfangInfor sanfangInfor) {
		SanfangListVO vo=new SanfangListVO();
		vo.setPurchaseCode(sanfangInfor.getSanfangFlowId());
		vo.setPurchaseType(sanfangInfor.getPurchaseType());
		vo.setSanfangTitle(sanfangInfor.getSanfangTitle());
		vo.setSanfangID(sanfangInfor.getSanfangID());
		vo.setSanfangDepartment(sanfangInfor.getSanfangDepartment().getOrganizeName());
		vo.setSanfangApplier(sanfangInfor.getSanfangApplier().getPerson().getPersonName());
		vo.setApplyDate(sanfangInfor.getApplyDate());
		vo.setSanfangStatus(EnumUtil.getByCode(sanfangInfor.getSanfangStatus(), SanfangfStatusEnum.class).getMsg());
		return vo;
	}


}

