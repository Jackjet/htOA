package com.kwchina.oa.document.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.document.dao.DocumentCategoryDAO;
import com.kwchina.oa.document.dao.DocumentInforDAO;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.document.service.DocumentInforManager;
import com.kwchina.oa.personal.address.entity.AddressCategory;

@Service
public class DocumentInforManagerImpl extends BasicManagerImpl<DocumentInfor> implements DocumentInforManager {
	
	private DocumentInforDAO documentInforDAO;

   
    private DocumentCategoryDAO documentCategoryDAO;
    
    @Autowired
	public void setDocumentInforDAO(DocumentInforDAO documentInforDAO) {
		this.documentInforDAO = documentInforDAO;
		super.setDao(documentInforDAO);
	}

    @Autowired
	public void setDocumentCategoryDAO(DocumentCategoryDAO documentCategoryDAO) {
		this.documentCategoryDAO = documentCategoryDAO;
		super.setDao(documentCategoryDAO);
	}

	/**
	 * 获取某个分类下的文档
	 */
	public List getAllDocuments(int categoryId) {
		return this.documentInforDAO.getAllDocuments(categoryId);
	}

	// 获取新的文档编号
	public String getNewDocumentCode(DocumentInfor document) {
		String documentCode = "";

		documentCode += document.getDepartment().getOrganizeNo();
		documentCode += "-";
		documentCode += document.getCategory().getCategoryCode();

		String keyCode = documentCode;

		documentCode += "-";

		// 目前年份月份
		java.util.Date dateNow = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowStr = sdf.format(dateNow);
		String yearStr = nowStr.substring(0, 4);
		String monthStr = nowStr.substring(5, 6);
		documentCode += yearStr + monthStr;
		documentCode += "-";

		DocumentInfor topDocument = this.documentInforDAO.getLastedDocument(keyCode);
		if (topDocument != null) {
			String tp_code = topDocument.getDocumentCode();

			if (tp_code != null && !tp_code.equals("")) {
				// 取出编号的后四位
				String serialCode = tp_code.substring(tp_code.length() - 4);
				// 编号+1为新编号
				int newSerial = Integer.parseInt(serialCode) + 1;
				String serial = String.valueOf(newSerial);
				// 新编号，长度不足4位，补足
				for (int k = 0; k < 4 - serial.length(); k++) {
					documentCode += "0";
				}
				documentCode += serial;
			}else{
				documentCode += "0001";
			}
		} else {
			documentCode += "0001";
		}

		return documentCode;
	}
	
	// 通过文档编号获取文档
	public List getDocumentByCode(String documentCode) {
		String hql = " from DocumentInfor document where document.documentCode = '" + documentCode + "'";
		List documentList = this.documentInforDAO.getResultByQueryString(hql);
		return documentList;
	}
	
	//根据公文Id获得文档信息
	public List getDocumentByReportId(int reportId) {
		return this.documentInforDAO.getDocumentByReportId(reportId);
	}
	
	//获取分类名称
	public List<DocumentCategory> getCategoryName() {
		List<DocumentCategory> returnLs = new ArrayList<DocumentCategory>();
 		String hql = "from DocumentCategory";
		returnLs = this.documentCategoryDAO.getResultByQueryString(hql); 		

 		return returnLs;
	}



}
