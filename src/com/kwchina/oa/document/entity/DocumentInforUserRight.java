package com.kwchina.oa.document.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kwchina.core.base.entity.SystemUserInfor;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
public class DocumentInforUserRight extends DocumentInforRight {


    private SystemUserInfor systemUser;		//用户信息

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    public SystemUserInfor getSystemUser() {
        return this.systemUser;
    }
    
    public void setSystemUser(SystemUserInfor systemUser) {
        this.systemUser = systemUser;
    }

}


