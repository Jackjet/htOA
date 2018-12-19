package com.kwchina.oa.personal.address.dao;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.personal.address.entity.CompanyAddress;

public interface CompanyAddressDAO  extends BasicDao<CompanyAddress> {
	//通过主键查询指定讯息
	public CompanyAddress showById(int personId);
}

