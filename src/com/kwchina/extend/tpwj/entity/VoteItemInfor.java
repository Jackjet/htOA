package com.kwchina.extend.tpwj.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 用户投票详细信息
 * @author suguan
 *
 */
@Entity
@Table(name = "TP_VoteItemInfor", schema = "dbo")
public class VoteItemInfor {

    private Integer detailId;
    private String voteText;        //内容
    private String voteValue;       //option选择项的ID （单选/多选/列表） “2,3,4”
    private String voteContent;     //option选项的值
    
    private VoteInfor voteInfor;
    private ItemInfor item;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getDetailId() {
        return this.detailId;
    }
    
    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    
    @Column(columnDefinition = "ntext")
	public String getVoteText() {
		return voteText;
	}

	public void setVoteText(String voteText) {
		this.voteText = voteText;
	}

	
	@Column(columnDefinition = "nvarchar(500)",nullable = true)
	public String getVoteValue() {
		return voteValue;
	}

	public void setVoteValue(String voteValue) {
		this.voteValue = voteValue;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="voteId", nullable = false)
	public VoteInfor getVoteInfor() {
		return voteInfor;
	}

	public void setVoteInfor(VoteInfor voteInfor) {
		this.voteInfor = voteInfor;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemId", nullable = false)
	public ItemInfor getItem() {
		return item;
	}

	public void setItem(ItemInfor item) {
		this.item = item;
	}

	
	@Column(columnDefinition = "nvarchar(500)",nullable = true)
	public String getVoteContent() {
		return voteContent;
	}

	public void setVoteContent(String voteContent) {
		this.voteContent = voteContent;
	}

    

}


