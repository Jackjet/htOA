package com.kwchina.webmail.web.bean;

public class MailMessagePart {
	private String type;

	private int partCount;

	private boolean isHidden;

	private String content;

	private int quoteLevel;
	

	//附件信息
	private String fileName;
	private String hrefFileName;
	private String size;
	private String description;
	private String binaryType;
	
	
	public String getBinaryType() {
		return binaryType;
	}

	public void setBinaryType(String binaryType) {
		this.binaryType = binaryType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHrefFileName() {
		return hrefFileName;
	}

	public void setHrefFileName(String hrefFileName) {
		this.hrefFileName = hrefFileName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public int getPartCount() {
		return partCount;
	}

	public void setPartCount(int partCount) {
		this.partCount = partCount;
	}

	public int getQuoteLevel() {
		return quoteLevel;
	}

	public void setQuoteLevel(int quoteLevel) {
		this.quoteLevel = quoteLevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
