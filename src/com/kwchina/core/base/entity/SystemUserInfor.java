package com.kwchina.core.base.entity;


import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.kwchina.core.sys.Log4jHandlerAOP;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;

@Entity
@Table(name = "Core_SystemUserInfor", schema = "dbo")
@ObjectId(id="personId")
public class SystemUserInfor implements JSONNotAware {
	public static int _Type_Admin = 1;
	public static int _Type_Normal = 0;
	
	public static int _Type_Tp = 2;
	public static int _Type_Hd = 3;
	
	Logger logger = Logger.getLogger(SystemUserInfor.class);

    private Integer personId;
    private String userName;		//登录用户名
    private String password;		//登录密码
    private boolean invalidate;		//注销与否:0-未注销;1-已注销.
    private int userType;			//用户类型:0-普通用户;1-系统管理员.2-投票用户 .  3-仅可查看活动用户（可查看活动及投票信息）
    private Timestamp lastLoginTime;//最近登录时间
    private int loginTimes;			//登录次数
    private boolean firstLogin;		//是否首次登陆:0-否;1-是
    
//    private String domainUserName;  //域用户名
//    private String domainPassword;  //域密码
   
    
    private Set<RoleInfor> roles = new HashSet<RoleInfor>(0);

    private PersonInfor person;		//人员角色

   	@Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
  	@Column(columnDefinition = "nvarchar(50)",nullable = false)  	
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isInvalidate() {
        return this.invalidate;
    }
    
    public void setInvalidate(boolean invalidate) {
    	if(getUserName() != null && !getUserName().equals("")){
    		logger.info(new Timestamp((System.currentTimeMillis())) + "时，用户【"+getUserName()+"】 状态被更改成："+ String.valueOf(invalidate));
    	}
    	
        this.invalidate = invalidate;
    }
    public int getUserType() {
        return this.userType;
    }
    
    public void setUserType(int userType) {
        this.userType = userType;
    }
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER,mappedBy = "users",targetEntity = RoleInfor.class)
    public Set<RoleInfor> getRoles() {
        return this.roles;
    }
    
    public void setRoles(Set<RoleInfor> roles) {
        this.roles = roles;
    }
    
    @OneToOne(fetch=FetchType.LAZY)
    public PersonInfor getPerson() {
        return this.person;
    }
    
    public void setPerson(PersonInfor person) {
        this.person = person;
    }



    //通过主表PersonInfor的主键生成
    @Id    
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "foreign", parameters = { @Parameter(name = "property", value = "person") })
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}


//   	@Column(columnDefinition = "nvarchar(80)",nullable = true)
//	public String getDomainUserName() {
//		return domainUserName;
//	}
//
//	public void setDomainUserName(String domainUserName) {
//		this.domainUserName = domainUserName;
//	}
//
//	
//
//   	@Column(columnDefinition = "nvarchar(80)",nullable = true)
//	public String getDomainPassword() {
//		return domainPassword;
//	}
//
//	public void setDomainPassword(String domainPassword) {
//		this.domainPassword = domainPassword;
//	}
	

	
}


