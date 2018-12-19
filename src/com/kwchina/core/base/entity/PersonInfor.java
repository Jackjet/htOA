package com.kwchina.core.base.entity;




import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;

@Entity
@Table(name = "Core_PersonInfor", schema = "dbo")
@ObjectId(id="personId")
public class PersonInfor implements JSONNotAware {

	public static int _Position_Leader =0;
	public static int _Position_Tag = 4;
	
    private Integer personId;
    private String personName;			//人员姓名
    private String personNo;			//人员编号
    private String mobile;				//手机
    private String email;				//邮件
    private int gender;					//性别:0-男;1-女.
    private Date birthday;				//出生日期
    private String officeAddress;		//办公室地址
    private String officePhone;			//办公室电话
    private String officeCode;			//办公室邮编
    private String homePhone;			//家庭电话
    private String homeAddress;			//家庭地址
    private String postCode;			//家庭邮政编码
    private String memo;				//备注
    private boolean deleted;			//删除否:0-否;1-是.
    private int positionLayer;			//职级
    private String photoAttachment;		//个人照片
    private String emailPassword;   	//邮件密码
    
    private OrganizeInfor department;	//所属部门
    private OrganizeInfor group;		//所属班组
    private OrganizeInfor company;		//所属公司(为分公司、投资公司等下属公司时，只显示公司，不显示具体部门或班组)
    private StructureInfor structure;	//岗位
    private SystemUserInfor user;		//用户�û�

    private PersonModules personModules;
  	@Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getPersonName() {
        return this.personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
  	@Column(columnDefinition = "nvarchar(80)")
    public String getPersonNo() {
        return this.personNo;
    }
    
    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }
    
  	@Column(columnDefinition = "nvarchar(100)")
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
  	@Column(columnDefinition = "nvarchar(100)")
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public int getGender() {
        return this.gender;
    }
    
    public void setGender(int gender) {
        this.gender = gender;
    }
    public Date getBirthday() {
        return this.birthday;
    }
    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
  	@Column(columnDefinition = "nvarchar(200)")
    public String getOfficeAddress() {
        return this.officeAddress;
    }
    
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
    
  	@Column(columnDefinition = "nvarchar(80)")
    public String getOfficePhone() {
        return this.officePhone;
    }
    
    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }
    
  	@Column(columnDefinition = "nvarchar(20)")
    public String getOfficeCode() {
        return this.officeCode;
    }
    
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
    
  	@Column(columnDefinition = "nvarchar(80)")
    public String getHomePhone() {
        return this.homePhone;
    }
    
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }
    
  	@Column(columnDefinition = "nvarchar(200)")
    public String getHomeAddress() {
        return this.homeAddress;
    }
    
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
    
  	@Column(columnDefinition = "nvarchar(20)")
    public String getPostCode() {
        return this.postCode;
    }
    
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    
	@Column(columnDefinition = "ntext")
    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public int getPositionLayer() {
		return positionLayer;
	}


	public void setPositionLayer(int positionLayer) {
		this.positionLayer = positionLayer;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="departmentId")
	public OrganizeInfor getDepartment() {
        return this.department;
    }
    
    public void setDepartment(OrganizeInfor department) {
        this.department = department;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
    public OrganizeInfor getGroup() {
        return this.group;
    }
    
    public void setGroup(OrganizeInfor group) {
        this.group = group;
    }
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="structureId")
    public StructureInfor getStructure() {
        return this.structure;
    }
    
    public void setStructure(StructureInfor structure) {
        this.structure = structure;
    }
    
    //与表SystemUserInfor共享主键
    @OneToOne
    @PrimaryKeyJoinColumn
    public SystemUserInfor getUser() {
        return this.user;
    }
    
    public void setUser(SystemUserInfor user) {

        this.user = user;
    }

    @OneToOne
    @PrimaryKeyJoinColumn
    public PersonModules getPersonModules() {
        return personModules;
    }

    public void setPersonModules(PersonModules personModules) {
        this.personModules = personModules;
    }

    @Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment") 
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="companyId")
	public OrganizeInfor getCompany() {
		return company;
	}

	public void setCompany(OrganizeInfor company) {
		this.company = company;
	}

  	@Column(columnDefinition = "nvarchar(200)")
	public String getPhotoAttachment() {
		return photoAttachment;
	}

	public void setPhotoAttachment(String photoAttachment) {
		this.photoAttachment = photoAttachment;
	}

	@Column(columnDefinition = "nvarchar(50)")
	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
}


