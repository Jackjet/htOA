package com.kwchina.oa.document.service;

import java.util.List;

import com.kwchina.core.common.service.BasicManager;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentInfor;


public interface DocumentInforManager extends BasicManager {	

    /**
     * 获取某个分类下的文档  
     */
    public List getAllDocuments(int categoryId);
    
    //获取新的文档编号
    public String getNewDocumentCode(DocumentInfor document);
    
    //通过文档编号获取文档
	public List getDocumentByCode(String documentCode);
	
	//根据公文Id获得文档信息
	public List getDocumentByReportId(int reportId);
	
	//获取分类
	public List<DocumentCategory> getCategoryName();
   
}

