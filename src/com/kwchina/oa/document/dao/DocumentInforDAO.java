package com.kwchina.oa.document.dao;

import java.util.List;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.oa.document.entity.DocumentInfor;


public interface DocumentInforDAO extends BasicDao<DocumentInfor> {
    
    /**
     * 获取某个分类下的文档  
     */
    public List getAllDocuments(int categoryId);
    
    //根据所属企业和文档分类,得到最后一个文档    
	public DocumentInfor getLastedDocument(String keyCode);
	
	//根据公文Id获得文档信息
	public List getDocumentByReportId(int reportId);
    
}
