package com.kwchina.extend.template.vo;


import lombok.Data;

@Data
public class TemplateVo {

    private Integer templateId;
    private String templateName;			//模板名称
    private int jsWeight;                //技术指标
    private int swWeight;                //商务指标
    private int[] templateInfoIds;          //基础信息id
    private String adderName;
    private Integer adderId;

}


