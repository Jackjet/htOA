package com.kwchina.core.util;

import java.util.ArrayList;
import java.util.List;



public class ExcelObject {
	// Excel文件名
	private String fileName = "";
	
	// ExcelSheet名
//	private String sheetName = "SheetOne";
	
	// 标题
	private String Title = "";
	
	// 列名
	private List rowName;
	
	// 表的内容，数据格式为2层list。
	private List tableContent = new ArrayList();
	
	// Excel文件到处路径
	private String filePath;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List getRowName() {
		return rowName;
	}

	public void setRowName(List rowName) {
		this.rowName = rowName;
	}

	// public String getSheetName() {
	// return sheetName;
	// }
	//
	// public void setSheetName(String sheetName) {
	// this.sheetName = sheetName;
	//	}

	public List getTableContent() {
		return tableContent;
	}

	public void setTableContent(List tableContent) {
		this.tableContent = tableContent;
	}
	
	public void addContentListByList(String[] oneRow){
		this.tableContent.add(oneRow);
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
