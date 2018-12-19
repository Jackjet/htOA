package com.kwchina.oa.bbs.service;

import com.kwchina.core.common.page.Pages;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.bbs.entity.CommentInfor;
import com.kwchina.oa.bbs.vo.CommentInforVo;

public interface CommentInforManager extends BasicManager {

	//转化MessageInfor为MessageInforVO
	public CommentInforVo transPOToVO(CommentInfor comment);
	
	public String createHql(CommentInforVo vo, Pages pages, String method, int hqlType);
	
}
