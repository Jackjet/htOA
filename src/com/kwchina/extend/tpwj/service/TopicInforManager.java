package com.kwchina.extend.tpwj.service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.service.BasicManager;
import com.kwchina.extend.tpwj.entity.TopicInfor;
import com.kwchina.extend.tpwj.vo.TopicInforVo;

public interface TopicInforManager extends BasicManager {

	public TopicInforVo transPOToVO(TopicInfor topic) ;
}
