package com.kwchina.oa.document.entity;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.SystemUserInfor;


@Entity
@Table(name = "Document_DocumentInfor", schema = "dbo")
public class DocumentInfor {

    private Integer documentId;
    private String documentTitle;		//文档标题
    private String documentCode;		//文档编号
    private String keyword;				//关键字
    private String description;			//摘要(说明)
    private String attachment;			//附件
    private Date updateTime;			//更新时间
    private Date createTime;            //创建时间
    private int commended;			    //是否推荐:0-未推荐;1-	推荐.
    private int reportId;				//对应的公文Id
    private SystemUserInfor author;		//作者
    private SystemUserInfor editor;		//最后更新者
    private DocumentCategory category;	//文档分类
    private OrganizeInfor department;	//所属部门
    private Set<DocumentInforRight> rights = new HashSet<DocumentInforRight>(0);

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getDocumentId() {
        return this.documentId;
    }
    
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
    
 	@Column(columnDefinition = "nvarchar(500)",nullable = false)
    public String getDocumentTitle() {
        return this.documentTitle;
    }
    
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }
    
 	@Column(columnDefinition = "nvarchar(80)")
    public String getDocumentCode() {
        return this.documentCode;
    }
    
    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }
    
 	@Column(columnDefinition = "nvarchar(80)")
    public String getKeyword() {
        return this.keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
	@Column(columnDefinition = "ntext")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
	@Column(columnDefinition = "nvarchar(2000)")
    public String getAttachment() {
        return this.attachment;
    }
    
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    public Date getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="authorId", nullable = false)
	public SystemUserInfor getAuthor() {
        return this.author;
    }
    
    public void setAuthor(SystemUserInfor author) {
        this.author = author;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="editorId", nullable = false)
    public SystemUserInfor getEditor() {
        return this.editor;
    }
    
    public void setEditor(SystemUserInfor editor) {
        this.editor = editor;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId", nullable = false)
    public DocumentCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(DocumentCategory category) {
        this.category = category;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="departmentId", nullable = false)
    public OrganizeInfor getDepartment() {
        return this.department;
    }
    
    public void setDepartment(OrganizeInfor department) {
        this.department = department;
    }

	@OneToMany(mappedBy = "document",fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<DocumentInforRight> getRights() {
		return rights;
	}

	public void setRights(Set<DocumentInforRight> rights) {
		this.rights = rights;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCommended() {
		return commended;
	}

	public void setCommended(int commended) {
		this.commended = commended;
	}

}


