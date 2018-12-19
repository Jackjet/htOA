package com.kwchina.oa.bbs.entity;


import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.*;

import com.kwchina.core.base.entity.SystemUserInfor;


@Entity
@Table(name = "BBS_ThesisInfor", schema = "dbo")
public class ThesisInfor {

	private Integer thesisId;           //id
	private String title;		    //标题
    private String content;		    //内容
    private Timestamp updateDate;   //更新时间
    private String nickName;	    //昵称
    private String attachment;	    //附件
    private int viewsCount;		    //浏览次数统计
    private boolean topThesis;	    //是否置顶：0-否；1-是.
    private boolean essence;	    //是否精品：0-否；1-是.
    private String imgAttachment;   //图片附件
    private SystemUserInfor author; //作者
    
    private Collection<CommentInfor> commentinfor;

    
    @OneToMany(mappedBy = "thesisInfor",cascade = CascadeType.ALL)
    public Collection<CommentInfor> getCommentinfor() {
        return commentinfor;
    }

    public void setCommentinfor(Collection<CommentInfor> commentinfor) {
        this.commentinfor = commentinfor;
    }
	
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getThesisId() {
		return thesisId;
	}
	public void setThesisId(Integer thesisId) {
		this.thesisId = thesisId;
	}
	
	
	@Column(columnDefinition = "nvarchar(1000)", nullable = false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

	@Column(columnDefinition = "ntext", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	@Column(columnDefinition = "nvarchar(100)", nullable = false)
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	 @Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	
	public int getViewsCount() {
		return viewsCount;
	}
	public void setViewsCount(int viewsCount) {
		this.viewsCount = viewsCount;
	}
	
	
	public boolean isTopThesis() {
		return topThesis;
	}
	public void setTopThesis(boolean topThesis) {
		this.topThesis = topThesis;
	}
	
	
	public boolean isEssence() {
		return essence;
	}
	public void setEssence(boolean essence) {
		this.essence = essence;
	}
	
	
	@Column(columnDefinition = "nvarchar(1000)", nullable = true)
	public String getImgAttachment() {
		return imgAttachment;
	}
	public void setImgAttachment(String imgAttachment) {
		this.imgAttachment = imgAttachment;
	}
	
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
	public SystemUserInfor getAuthor() {
		return author;
	}
	public void setAuthor(SystemUserInfor author) {
		this.author = author;
	}
    

}
