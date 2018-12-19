package com.kwchina.extend.template.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create by lijj on 2018/8/20
 */
@Data
@Entity
@Table(name = "purchase_zhaotouTemplate", schema = "dbo")
@ObjectId(id="TemplateId")
public class ZhaotouTemplate implements JSONNotAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateId;      //主键
    private String templateName;       //模板名称
    private int jsWeight;            //技术权重
    private int swWeight;            //商务权重
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adderId")
    private SystemUserInfor adder;
    @ManyToMany(targetEntity = ZhaotouTemplateInfo.class,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Template_Link",schema = "dbo",joinColumns = {@JoinColumn(name = "templateId")},inverseJoinColumns = {@JoinColumn(name = "zhaotouTemplateInfoId")})
    @OrderBy("zhaotouTemplateInfoId")
    private List<ZhaotouTemplateInfo> templateInfos = new ArrayList<ZhaotouTemplateInfo>();
    private boolean valid;

}
