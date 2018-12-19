package com.kwchina.core.base.entity;




import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Core_PersonModules", schema = "dbo")
@ObjectId(id="CMId")
public class PersonModules {

    private int personId;
    private PersonInfor personInfor; //人员

    private String Name1;       //模块名字
    private String MoreUrl1;    //More的Url
    private String ulId1;        //

    private String Name2;       //模块名字
    private String MoreUrl2;    //More的Url

    private String ulId2;
    private String Name3;       //模块名字
    private String MoreUrl3;    //More的Url
    private String ulId3;
    private String Name4;       //模块名字
    private String MoreUrl4;    //More的Url
    private String ulId4;
    private String Name5;       //模块名字
    private String MoreUrl5;    //More的Url
    private String ulId5;
    private String Name6;       //模块名字
    private String MoreUrl6;    //More的Url
    private String ulId6;

    private String color1;
    private String color2;
    private String color3;
    private String color4;
    private String color5;
    private String color6;

    public PersonModules() {
    }
    @Column(columnDefinition = "nvarchar(20)",nullable = false)
    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }
    @Column(columnDefinition = "nvarchar(20)",nullable = false)
    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }
    @Column(columnDefinition = "nvarchar(20)",nullable = false)
    public String getColor3() {
        return color3;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }
    @Column(columnDefinition = "nvarchar(20)",nullable = false)
    public String getColor4() {
        return color4;
    }

    public void setColor4(String color4) {
        this.color4 = color4;
    }
    @Column(columnDefinition = "nvarchar(20)",nullable = false)
    public String getColor5() {
        return color5;
    }

    public void setColor5(String color5) {
        this.color5 = color5;
    }
    @Column(columnDefinition = "nvarchar(20)",nullable = false)
    public String getColor6() {
        return color6;
    }

    public void setColor6(String color6) {
        this.color6 = color6;
    }


    public PersonModules(String name1, String moreUrl1, String ulId1, String name2, String moreUrl2, String ulId2, String name3, String moreUrl3, String ulId3, String name4, String moreUrl4, String ulId4, String name5, String moreUrl5, String ulId5, String name6, String moreUrl6, String ulId6, String color1, String color2, String color3, String color4, String color5, String color6) {
        Name1 = name1;
        MoreUrl1 = moreUrl1;
        this.ulId1 = ulId1;
        Name2 = name2;
        MoreUrl2 = moreUrl2;
        this.ulId2 = ulId2;
        Name3 = name3;
        MoreUrl3 = moreUrl3;
        this.ulId3 = ulId3;
        Name4 = name4;
        MoreUrl4 = moreUrl4;
        this.ulId4 = ulId4;
        Name5 = name5;
        MoreUrl5 = moreUrl5;
        this.ulId5 = ulId5;
        Name6 = name6;
        MoreUrl6 = moreUrl6;
        this.ulId6 = ulId6;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
        this.color6 = color6;
    }

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "foreign", parameters = { @Parameter(name = "property", value = "personInfor") })
    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getUlId1() {
        return ulId1;
    }

    public void setUlId1(String ulId1) {
        this.ulId1 = ulId1;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getUlId2() {
        return ulId2;
    }

    public void setUlId2(String ulId2) {
        this.ulId2 = ulId2;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getUlId3() {
        return ulId3;
    }

    public void setUlId3(String ulId3) {
        this.ulId3 = ulId3;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getUlId4() {
        return ulId4;
    }

    public void setUlId4(String ulId4) {
        this.ulId4 = ulId4;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getUlId5() {
        return ulId5;
    }

    public void setUlId5(String ulId5) {
        this.ulId5 = ulId5;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getUlId6() {
        return ulId6;
    }

    public void setUlId6(String ulId6) {
        this.ulId6 = ulId6;
    }

    @OneToOne(fetch= FetchType.LAZY)
    public PersonInfor getPersonInfor() {
        return personInfor;
    }

    public void setPersonInfor(PersonInfor personInfor) {
        this.personInfor = personInfor;
    }

    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getName1() {
        return Name1;
    }

    public void setName1(String name1) {
        Name1 = name1;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getMoreUrl1() {
        return MoreUrl1;
    }

    public void setMoreUrl1(String moreUrl1) {
        MoreUrl1 = moreUrl1;
    }
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getMoreUrl2() {
        return MoreUrl2;
    }

    public void setMoreUrl2(String moreUrl2) {
        MoreUrl2 = moreUrl2;
    }
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getName3() {
        return Name3;
    }

    public void setName3(String name3) {
        Name3 = name3;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getMoreUrl3() {
        return MoreUrl3;
    }

    public void setMoreUrl3(String moreUrl3) {
        MoreUrl3 = moreUrl3;
    }
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getName4() {
        return Name4;
    }

    public void setName4(String name4) {
        Name4 = name4;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getMoreUrl4() {
        return MoreUrl4;
    }

    public void setMoreUrl4(String moreUrl4) {
        MoreUrl4 = moreUrl4;
    }
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getName5() {
        return Name5;
    }

    public void setName5(String name5) {
        Name5 = name5;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getMoreUrl5() {
        return MoreUrl5;
    }

    public void setMoreUrl5(String moreUrl5) {
        MoreUrl5 = moreUrl5;
    }
    @Column(columnDefinition = "nvarchar(80)",nullable = false)
    public String getName6() {
        return Name6;
    }

    public void setName6(String name6) {
        Name6 = name6;
    }
    @Column(columnDefinition = "nvarchar(200)",nullable = false)
    public String getMoreUrl6() {
        return MoreUrl6;
    }

    public void setMoreUrl6(String moreUrl6) {
        MoreUrl6 = moreUrl6;
    }
}


