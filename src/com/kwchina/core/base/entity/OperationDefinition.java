package com.kwchina.core.base.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "Core_OperationDefinition", schema = "dbo")
public class OperationDefinition {

	//各种操作权限所在二进制位置
	public static int RightPosition_Edit = 1;	//新增,修改
	public static int RightPosition_Delete = 2;	//删除
	public static int RightPosition_List = 3;	//浏览
	public static int RightPosition_View = 4;	//查看
	public static int RightPosition_ListSel = 5;//浏览自己发布的(为"浏览"操作的子操作)
	public static int RightPosition_ListDep = 6;//浏览本部门的(为"浏览"操作的子操作)
	
    private Integer operationId;
    private String operationName;		//操作名称,如:新增、删除、修改、查看等
    private String methodName;			//操作方法,如:add、delete、edit、view等
    private int position;				//第几位,即二进制数的第几位为1,如:2(0010),则值为2
    private OperationDefinition parent;	//父操作
    private Set<OperationDefinition> childs = new HashSet<OperationDefinition>(0);
    private Set<ViewLogicRight> viewRights = new HashSet<ViewLogicRight>(0);
     
    
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getOperationId() {
		return operationId;
	}
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
	
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId")
	public OperationDefinition getParent() {
		return parent;
	}
	public void setParent(OperationDefinition parent) {
		this.parent = parent;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	@OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	public Set<OperationDefinition> getChilds() {
		return childs;
	}
	public void setChilds(Set<OperationDefinition> childs) {
		this.childs = childs;
	}
	
	@OneToMany(mappedBy = "operation",cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	public Set<ViewLogicRight> getViewRights() {
		return viewRights;
	}
	public void setViewRights(Set<ViewLogicRight> viewRights) {
		this.viewRights = viewRights;
	}
     
}