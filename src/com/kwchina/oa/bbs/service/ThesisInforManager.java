package com.kwchina.oa.bbs.service;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.bbs.entity.ThesisInfor;
import com.kwchina.oa.bbs.vo.ThesisInforVo;

public interface ThesisInforManager extends BasicManager {

	//转化MessageInfor为MessageInforVO
	public ThesisInforVo transPOToVO(ThesisInfor thesis);
	
}
