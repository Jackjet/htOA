package com.kwchina.extend.template.entity;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.util.annotation.ObjectId;
import com.kwchina.core.util.json.JSONNotAware;
import lombok.Data;

import javax.persistence.*;

/**
 * Create by lijj on 2018/8/20
 */
@Data
@Entity
@Table(name = "purchase_zhaotouTemplateInfo", schema = "dbo")
@ObjectId(id="zhaotouTemplateInfoId")
public class ZhaotouTemplateInfo implements JSONNotAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zhaotouTemplateInfoId;      //主键
    private String target;       //指标
    private double Score;          //分值
    private String standard;  //标准
    private String type;       //指标类型
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adderId")
    private SystemUserInfor adder;
    private boolean valid;

}
