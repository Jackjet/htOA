package com.kwchina.oa.document.vo;

public class DocumentInforVo {

	private Integer documentId;
	private String documentTitle; 	// 文档标题
	private String documentCode; 	// 文档编号
	private String keyword; 		// 关键字
	private String description; 	// 摘要(说明)
	// private String attachment; 	//附件
	private String[] attatchmentArray = {}; // 附件路径
	// private String updateTime; 	//更新时间
	// private String createTime; 	//创建时间
	private int commended; 			// 是否推荐:0-未推荐;1-推荐.
	private int reportId; 			// 对应的公文Id
	private int authorId; 			// 作者
	private int editorId; 			// 最后更新者
	private int categoryId; 		// 文档分类
	private int departmentId; 		// 所属部门
	// private boolean needPublic; 	//是否公开

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * public String getAttachment() { return attachment; } public void
	 * setAttachment(String attachment) { this.attachment = attachment; } public
	 * String getUpdateTime() { return updateTime; } public void
	 * setUpdateTime(String updateTime) { this.updateTime = updateTime; } public
	 * String getCreateTime() { return createTime; } public void
	 * setCreateTime(String createTime) { this.createTime = createTime; }
	 */

	public int getCommended() {
		return commended;
	}

	public void setCommended(int commended) {
		this.commended = commended;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getEditorId() {
		return editorId;
	}

	public void setEditorId(int editorId) {
		this.editorId = editorId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String[] getAttatchmentArray() {
		return attatchmentArray;
	}

	public void setAttatchmentArray(String[] attatchmentArray) {
		this.attatchmentArray = attatchmentArray;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
}
