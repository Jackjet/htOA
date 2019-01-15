package com.kwchina.oa.purchase.sanfang.dao.impl;
import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.purchase.sanfang.dao.SupplierInforDAO;
import com.kwchina.oa.purchase.sanfang.entity.SupplierInfor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ListIterator;

@Repository
public class SupplierInforDAOImpl extends BasicDaoImpl<SupplierInfor> implements SupplierInforDAO{
	@Override
	public List<SupplierInfor> getAllSupplier() {
		String sql = "from SupplierInfor where 1=1 and valid=1";
		List list = getResultByQueryString(sql);
		return list;
	}

	@Override
	public String getContactByName(String supplierName) {
		String sql="from SupplierInfor supplier where supplier.supplierName="+"'"+supplierName+"'";
		List list = getResultByQueryString(sql);
		if(list.size()>0){
			SupplierInfor supplierInfor = (SupplierInfor)list.get(0);
			String supplierContact = supplierInfor.getContactName()+"/"+supplierInfor.getSupplierContact();

			return supplierContact;
		}
		return null;
	}
}
