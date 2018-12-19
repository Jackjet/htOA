package com.kwchina.oa.workflow.customfields.service;

import java.util.List;

import net.sf.json.JSONArray;
import freemarker.template.Template;


public interface CommonManager {
	
	/** 根据数据库模板生成hbm文件 
	 * @param flowName 流程名
	 * @return hbmName 生成的hbm文件名
	 * */
	public String createHbm(String flowName);
	
	
	/** 根据模板文件生成数据库表,并更新hbm文件中的自定义字段
	 * @param hbmName hbm文件名
	 * @param ftlName 自定义表单模板文件名
	 * */
	public void createTable(String hbmName, String ftlName);
	
	
	/** 更新数据库表的自定义表单数据
	 * @param flowName 流程名
	 * @param fieldNames 自定义字段名
	 * @param fieldValues 自定义字段值
	 * @param instanceId 审核实例Id
	 * @param flag 用于判断是新增还是修改:0-新增;1-修改
	 *  */
	public void updateFormData(String flowName, List fieldNames, String[] fieldValues, Integer instanceId, int flag);
	
	
	/** 根据流程名和审核实例Id获取实例对应的表单数据
	 * @param flowName 流程名
	 * @param instanceId 审核实例Id
	 * @return list 返回的表单数据
	 *  */
	public List getFormData(String flowName, Integer instanceId);
	
	public List getFormData(String flowName, String contractNo,String colm);
	/** 根据流程名获取表列名
	 * @param flowName 流程名
	 * @return list 返回的表列名
	 *  */
	public List getColumnNames(String flowName);
	
	
	/** 根据审核实例Id删除对应的表单数据
	 * @param flowName 流程名
	 * @param instanceId 审核实例Id
	 *  */
	public void delFormData(String flowName, Integer instanceId);
	
	
	/** 根据模板获取自定义字段
	 * @param ftlName 模板名
	 * @return fieldNames 自定义字段名
	 *  */
	public List getFieldNames(String ftlName);
	
	
	/** 根据模板获取查询字段
	 * @param ftlName 模板名
	 * @return searchFields 查询字段
	 *  */
	public JSONArray getSearchFields(String ftlName);
	
	
	/** 根据模板名获取模板文件
	 * @param templateName 模板名
	 * @param path 路径
	 * @param setTagSyntax 是否设置freemarker的标签默认使用方括号
	 * @return template 模板文件
	 * */
	public Template getTemplate(String templateName, String path, boolean setTagSyntax);
	
	/** 根据模板获取动态下拉字段（部门或人员）
	 * @param ftlName 模板名
	 * @return fieldNames 自定义字段名
	 *  */
	public List getDynamicFieldNames(String ftlName,String type);
	
}