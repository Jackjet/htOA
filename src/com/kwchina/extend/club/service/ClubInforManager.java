package com.kwchina.extend.club.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.extend.club.entity.ClubInfor;

public interface ClubInforManager extends BasicManager {

	//得到所有未结束，结束日期在今天的活动
	public List<ClubInfor> getUnfinishedInfors();
	
	//更改活动状态
	public void changeTaskStatus(ClubInfor clubInfor,int status);
}
