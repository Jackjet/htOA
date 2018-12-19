package com.kwchina.core.cms.entity;


import java.math.BigDecimal;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kwchina.core.base.entity.SystemUserInfor;

@Entity
@Table(name = "Infor_DocumentInfor", schema = "dbo")
public class InforDocument implements java.io.Serializable{
	
    private Integer inforId;
    private String inforTitle;		//信息主题
    private String issueUnit;		//发布单位
    private boolean homepage;		//是否需要首页显示:0-不需要;1-需要.
    private String topp;		//是否需要首页显示:0-不需要;1-需要.
    private Date startDate;			//首页开始时间
    private Date endDate;			//首页结束时间
    private boolean important;		//是否重要:0-普通;1-重要.
    private String keyword;			//关键字
    private String inforContent;	//信息内容
    private String attachment;		//附件路径
    private Date createTime;		//发布时间
    private Date inforTime;			//信息时间
    private String relateUrl;		//相关链接
    private int hits;				//点击数
    private String source;			//信息来源
    private String defaultPicUrl;	//图片路径
    private String htmlFilePath;	//html文件保存路径
    private String defStr1;			//自定义字符1
    private String defStr2;			//自定义字符2
    private String defStr3;			//自定义字符3
    private Date defDate1;			//自定义时间1
    private Date defDate2;			//自定义时间2
    private boolean defBool1;		//自定义Boolean型
    private BigDecimal defDec1;		//自定义Decimal型
    private SystemUserInfor author;	//(提交人)
    private String zuozhe;	//(作者)
    private InforCategory category;	//所属分类
    private Set<InforDocumentRight> rights = new HashSet<InforDocumentRight>(0);
    
    //静态字段，只用于“荣誉室”
    private String kind;         //所属类别
    private String honorKind;           //荣誉类别
    
    private Set<InforPraise> praises =  new HashSet<InforPraise>(0);   //点赞
    private Set<InforComment> comments = new HashSet<InforComment>(0);  //评论


    private boolean handOut;		//是否与总/分公司互通:0-不需要;1-需要.
    private boolean received;       //是否是接收到的
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getInforId() {
        return this.inforId;
    }
    
    public void setInforId(Integer inforId) {
        this.inforId = inforId;
    }
    
    @Column(columnDefinition = "nvarchar(200)")
    public String getInforTitle() {
        return this.inforTitle;
    }
    
    public void setInforTitle(String inforTitle) {
        this.inforTitle = inforTitle;
    }
    
    @Column(columnDefinition = "nvarchar(80)")
    public String getIssueUnit() {
        return this.issueUnit;
    }
    
    public void setIssueUnit(String issueUnit) {
        this.issueUnit = issueUnit;
    }
    public boolean isHomepage() {
        return this.homepage;
    }
    
    public void setHomepage(boolean homepage) {
        this.homepage = homepage;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public boolean isImportant() {
        return this.important;
    }
    
    public void setImportant(boolean important) {
        this.important = important;
    }
    
    @Column(columnDefinition = "nvarchar(200)")
    public String getKeyword() {
        return this.keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    @Column(columnDefinition = "ntext")
    public String getInforContent() {
        return this.inforContent;
    }
    
    public void setInforContent(String inforContent) {
        this.inforContent = inforContent;
    }
    
    @Column(columnDefinition = "nvarchar(2000)")
    public String getAttachment() {
        return this.attachment;
    }
    
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    
    @Column(nullable = false)
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getInforTime() {
        return this.inforTime;
    }
    
    public void setInforTime(Date inforTime) {
        this.inforTime = inforTime;
    }
    
    @Column(columnDefinition = "nvarchar(200)")
    public String getRelateUrl() {
        return this.relateUrl;
    }
    
    public void setRelateUrl(String relateUrl) {
        this.relateUrl = relateUrl;
    }
    public int getHits() {
        return this.hits;
    }
    
    public void setHits(int hits) {
        this.hits = hits;
    }
    
    @Column(columnDefinition = "nvarchar(100)")
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    @Column(columnDefinition = "nvarchar(300)")
    public String getDefaultPicUrl() {
        return this.defaultPicUrl;
    }
    
    public void setDefaultPicUrl(String defaultPicUrl) {
        this.defaultPicUrl = defaultPicUrl;
    }
    
    @Column(columnDefinition = "nvarchar(300)")
    public String getHtmlFilePath() {
        return this.htmlFilePath;
    }
    
    public void setHtmlFilePath(String htmlFilePath) {
        this.htmlFilePath = htmlFilePath;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="authorId", nullable = false)
    public SystemUserInfor getAuthor() {
        return this.author;
    }


    public String getZuozhe() {
        return zuozhe;
    }
    public void setZuozhe(String zuozhe) {
        this.zuozhe = zuozhe;
    }

    public void setAuthor(SystemUserInfor author) {
        this.author = author;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoryId")
    public InforCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(InforCategory category) {
        this.category = category;
    }
    
	@OneToMany(mappedBy = "document",fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
    public Set<InforDocumentRight> getRights() {
        return this.rights;
    }
    
    public void setRights(Set<InforDocumentRight> rights) {
        this.rights = rights;
    }

	public boolean isDefBool1() {
		return defBool1;
	}

	public void setDefBool1(boolean defBool1) {
		this.defBool1 = defBool1;
	}

	public Date getDefDate1() {
		return defDate1;
	}

	public void setDefDate1(Date defDate1) {
		this.defDate1 = defDate1;
	}

	public Date getDefDate2() {
		return defDate2;
	}

	public void setDefDate2(Date defDate2) {
		this.defDate2 = defDate2;
	}

	public BigDecimal getDefDec1() {
		return defDec1;
	}

	public void setDefDec1(BigDecimal defDec1) {
		this.defDec1 = defDec1;
	}

    @Column(columnDefinition = "nvarchar(300)")
	public String getDefStr1() {
		return defStr1;
	}

	public void setDefStr1(String defStr1) {
		this.defStr1 = defStr1;
	}

    @Column(columnDefinition = "nvarchar(300)")
	public String getDefStr2() {
		return defStr2;
	}

	public void setDefStr2(String defStr2) {
		this.defStr2 = defStr2;
	}

    @Column(columnDefinition = "nvarchar(300)")
	public String getDefStr3() {
		return defStr3;
	}

	public void setDefStr3(String defStr3) {
		this.defStr3 = defStr3;
	}

	public boolean isHandOut() {
		return handOut;
	}

	public void setHandOut(boolean handOut) {
		this.handOut = handOut;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	@OneToMany(mappedBy = "infor",fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	public Set<InforPraise> getPraises() {
		return praises;
	}

	public void setPraises(Set<InforPraise> praises) {
		this.praises = praises;
	}

	@OneToMany(mappedBy = "infor",fetch=FetchType.LAZY)
	@Cascade(value = {org.hibernate.annotations.CascadeType.DELETE_ORPHAN, org.hibernate.annotations.CascadeType.ALL})
	@OrderBy("operateDate")
	public Set<InforComment> getComments() {
		return comments;
	}

	public void setComments(Set<InforComment> comments) {
		this.comments = comments;
	}

	
	@Column(columnDefinition = "nvarchar(300)")
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	
	@Column(columnDefinition = "nvarchar(300)")
	public String getHonorKind() {
		return honorKind;
	}

	public void setHonorKind(String honorKind) {
		this.honorKind = honorKind;
	}

    public String getTopp() {
        return topp;
    }

    public void setTopp(String topp) {
        this.topp = topp;
    }
}


