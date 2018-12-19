package com.kwchina.extend.template.vo;


import lombok.Data;

@Data
public class TemplateInfoVo {
    private Integer zhaotouTemplateInfoId;      //主键
    private String target;       //指标
    private double Score;          //分值
    private String standard;  //标准
    private String type;       //指标类型
    private String adderName;
    private Integer adderId;
    private boolean valid;

}


