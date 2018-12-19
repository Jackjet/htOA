package com.kwchina.oa.document.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.oa.document.dao.DocumentInforDAO;
import com.kwchina.oa.document.entity.DocumentInfor;

@Repository
public class DocumentInforDAOImpl extends BasicDaoImpl<DocumentInfor> implements DocumentInforDAO{
	    
    /**
     * 获取某个分类下的文档  
     */
    public List getAllDocuments(int categoryId){
    	String sql ="from DocumentInfor document  where document.category.categoryId = " + categoryId;		
		List list = getResultByQueryString(sql);

		return list;
    }
    
    //根据所属企业和文档分类,得到最后一个文档    
	public DocumentInfor getLastedDocument(String keyCode){
		String sql = " from DocumentInfor document";
		sql += " where document.documentCode like '%-" + keyCode + "-%' order by document.documentId";
		
		List ls = getResultByQueryString(sql);
		if (ls != null && !ls.isEmpty()) {
			return (DocumentInfor) ls.get(0);
		} else {
			return null;
		}
	}
	
	//根据公文Id获得文档信息
	public List getDocumentByReportId(int reportId){
    	String sql ="from DocumentInfor document  where document.reportId = " + reportId;		
		List list = getResultByQueryString(sql);
		
		return list;
    }

}
