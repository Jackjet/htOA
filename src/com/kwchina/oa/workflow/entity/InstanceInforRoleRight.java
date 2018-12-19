package com.kwchina.oa.workflow.entity;


import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.oa.workflow.entity.InstanceInforRight;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class InstanceInforRoleRight extends InstanceInforRight {


    private RoleInfor role;		//角色信息

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    public RoleInfor getRole() {
        return this.role;
    }
    
    public void setRole(RoleInfor role) {
        this.role = role;
    }

}


