package com.kwchina.oa.workflow.customfields.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.workflow.customfields.domain.CustomizableEntity;
import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.service.CustomizableEntityManager;
import com.kwchina.oa.workflow.customfields.util.FlowConstant;
import com.kwchina.oa.workflow.customfields.util.HibernateUtil;

import freemarker.core.TemplateElement;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@Service("commonManager")
public class CommonManagerImpl implements CommonManager{
	
	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * 获取Session对象
	 */
	public Session openSession() {
		return (Session) entityManager.getDelegate();
	}
	
	
	/** 根据数据库模板生成hbm文件 
	 * @param flowName 流程名
	 * @return hbmName 生成的hbm文件名
	 * */
	public String createHbm(String flowName) {
		
		String hbmName = null;
		
        try {
			//加载数据库模板文件
        	/* Configuration cfg = new Configuration(); 
        	cfg.setDirectoryForTemplateLoading(new File(CoreConstant.Context_Real_Path + FlowConstant.Flow_Template_Path));
			Template template = cfg.getTemplate("entityTemplate.ftl"); */
        	Template template = getTemplate("entityTemplate.ftl", FlowConstant.Flow_EntityTemplate_Path, false);
	        
			//装载数据到模板
	        OutputStreamWriter out = new OutputStreamWriter(System.out);   
	        Map<String, Object> data = new HashMap<String, Object>();
	        String tableName = flowName.substring(0,1).toUpperCase() + flowName.substring(1);
	        data.put("_TableName","Customize_" + tableName);   
	        StringWriter writer = new StringWriter();   
	        template.process(data,writer);   
	        String content = writer.toString();   
	        writer.close();
	        
	        //输出hbm文件
	        hbmName = tableName + ".hbm.xml";
	        String filePath = CoreConstant.Context_Real_Path + FlowConstant.Flow_Hbm_Path + "/" + hbmName;   
	        File hbmFile = new File(filePath);   
	        if(!hbmFile.exists()){
	        	hbmFile.createNewFile();   
	        }   
	        OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(hbmFile), Charset.forName(CoreConstant.ENCODING));        
	        osWriter.write(content);   
	        osWriter.close();   
	        out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hbmName;
		
	}
	
	
	/** 根据模板文件生成数据库表,并更新hbm文件中的自定义字段
	 * @param hbmName hbm文件名
	 * @param ftlName 自定义表单模板文件名
	 * */
	public void createTable(String hbmName, String ftlName) {
		
		//初始化hibernate实例,并加载hbm文件
		HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName(hbmName);
		hibernateUtil.getCurrentSession();
		CustomizableEntityManager contactEntityManager = new CustomizableEntityManagerImpl(CustomizableEntity.class);
		
		//获取自定义表单模板内的自定义标签元素
		Template template = getTemplate(ftlName, FlowConstant.Flow_FormTemplate_Path, true);
		TemplateElement element = template.getRootTreeNode();
        for(Enumeration children = element.children(); children.hasMoreElements();){
            Object obj = children.nextElement();
            //自定义标签都为UnifiedCall类型
            if("class freemarker.core.UnifiedCall".equals(obj.getClass().toString())){
            	//获取字段名
            	int sIndex = obj.toString().indexOf("controlName=\"");
                int eIndex = obj.toString().lastIndexOf("\"/]");
                String fieldName = obj.toString().substring(sIndex+13, eIndex);
            	
                //生成数据库表并更新hbm文件中的自定义字段
            	if (obj.toString().contains("[@input") || obj.toString().contains("[@textarea") 
            			|| obj.toString().contains("[@select") || obj.toString().contains("[@dateInput")
            			|| obj.toString().contains("[@hidden") || obj.toString().contains("[@mSelect")) {
            		contactEntityManager.addCustomField(fieldName, String.class, hbmName);
                }else if (obj.toString().contains("[@depSelect") || obj.toString().contains("[@usrSelect")) {
                	contactEntityManager.addCustomField(fieldName, Integer.class, hbmName);
                }
            }
        }
        
		//重新启动Configuration配置,使之前对hbm文件的修改同步到数据库
		hibernateUtil.getCurrentSession();
		hibernateUtil.reset();
        
	}
	
	
	/** 更新数据库表的自定义表单数据
	 * @param flowName 流程名
	 * @param fieldNames 自定义字段名
	 * @param fieldValues 自定义字段值
	 * @param instanceId 审核实例Id
	 * @param flag 用于判断是新增还是修改:0-新增;1-修改
	 *  */
	public void updateFormData(String flowName, List fieldNames, String[] fieldValues, Integer instanceId, int flag) {
		
		String sql = "";
		if (flag == 0) {
			//新增
			sql = "insert into Customize_" + flowName + "(";
			for (int i=0;i<fieldNames.size();i++) {
				sql += fieldNames.get(i).toString() + ",";
			}
			sql += "instanceId) values (";
			for (int i=0;i<fieldValues.length;i++) {
				sql += "'" + fieldValues[i] + "',";
			}
			sql += "'" + instanceId + "')";
		}else {
			//修改
			sql = "update Customize_" + flowName + " set ";
			for (int i=0;i<fieldNames.size();i++) {
				sql += fieldNames.get(i).toString() + "='" + fieldValues[i] + "'";
				if (i != fieldNames.size()-1) {
					sql += ",";
				}
			}
			sql += " where instanceId='" + instanceId + "'";
		}
		
		/*HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName(null);
		Session session = hibernateUtil.getCurrentSession();*/
		Session session = openSession();
//		Transaction tx = session.beginTransaction();
		try {
			session.createSQLQuery(sql).executeUpdate();
//			tx.commit();
		} catch (HibernateException he) {
//			tx.rollback();
			he.printStackTrace();
		} finally {
//			hibernateUtil.reset();
		}
		
	}
	
	
	/** 根据流程名和审核实例Id获取实例对应的表单数据
	 * @param flowName 流程名
	 * @param instanceId 审核实例Id
	 * @return list 返回的表单数据
	 *  */
	public List getFormData(String flowName, Integer instanceId) {
		
		String sql = "select * from Customize_" + flowName + " where instanceId='" + instanceId + "'";
		
		/*HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName(null);
		Session session = hibernateUtil.getCurrentSession();*/
		Session session = openSession();
//		Transaction tx = session.beginTransaction();
		Query query = session.createSQLQuery(sql);
			
		List list = query.list();
		
//		tx.commit();
//		hibernateUtil.reset();
		
		return list;
	}
	
	public List getFormData(String flowName, String contractNo,String colm) {
		
		String sql = "select * from Customize_" + flowName + " where "+ colm+" like '%" + contractNo + "%'";
		
		/*HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName(null);
		Session session = hibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();*/
		Session session = openSession();
		Query query = session.createSQLQuery(sql);
			
		List list = query.list();
		
//		tx.commit();
//		hibernateUtil.reset();
		
		return list;
	}
	
	
	/** 根据流程名获取表列名
	 * @param flowName 流程名
	 * @return list 返回的表列名
	 *  */
	public List getColumnNames(String flowName) {
		
		String sql = "select name from syscolumns" + " where id = object_id('Customize_" + flowName + "') order by colid";
		
		/*HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName(null);
		Session session = hibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();*/
		Session session = openSession();
		Query query = session.createSQLQuery(sql);
			
		List list = query.list();
		
//		tx.commit();
//		hibernateUtil.reset();
		
		return list;
	}
	
	
	/** 根据审核实例Id删除对应的表单数据
	 * @param flowName 流程名
	 * @param instanceId 审核实例Id
	 *  */
	public void delFormData(String flowName, Integer instanceId) {
		
		String sql = "delete from Customize_" + flowName + " where instanceId='" + instanceId + "'";
		
		HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName(null);
		Session session = hibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		try {
			session.createSQLQuery(sql).executeUpdate();
			tx.commit();
		} catch (HibernateException he) {
			tx.rollback();
			he.printStackTrace();
		} finally {
			hibernateUtil.reset();
		}
		
	}
	
	
	/** 根据模板获取自定义字段
	 * @param ftlName 模板名
	 * @return fieldNames 自定义字段名
	 *  */
	public List getFieldNames(String ftlName) {
		
		List fieldNames = new ArrayList();
		
		//获取自定义表单模板内的自定义标签元素
		Template template = getTemplate(ftlName, FlowConstant.Flow_FormTemplate_Path, true);
		TemplateElement element = template.getRootTreeNode();
        for(Enumeration children = element.children(); children.hasMoreElements();){
            Object obj = children.nextElement();
            //自定义标签都为UnifiedCall类型
            if("class freemarker.core.UnifiedCall".equals(obj.getClass().toString())){
            	if (obj.toString().contains("[@input") || obj.toString().contains("[@textarea") 
            			|| obj.toString().contains("[@select") || obj.toString().contains("[@dateInput")
            			|| obj.toString().contains("[@hidden") || obj.toString().contains("[@depSelect")
            			|| obj.toString().contains("[@usrSelect") || obj.toString().contains("[@mSelect")) {
                	//获取字段名
                	int sIndex = obj.toString().indexOf("controlName=\"");
                    int eIndex = obj.toString().lastIndexOf("\"/]");
                    String fieldName = obj.toString().substring(sIndex+13, eIndex);
                    fieldNames.add(fieldName);
            	}
            }
        }
		return fieldNames;
        
	}
	
	/** 根据模板获取动态下拉字段（部门或人员）
	 * @param ftlName 模板名
	 * @return fieldNames 自定义字段名
	 *  */
	public List getDynamicFieldNames(String ftlName,String type){
		List fieldNames = new ArrayList();
		
		//获取自定义表单模板内的自定义标签元素
		Template template = getTemplate(ftlName, FlowConstant.Flow_FormTemplate_Path, true);
		TemplateElement element = template.getRootTreeNode();
        for(Enumeration children = element.children(); children.hasMoreElements();){
            Object obj = children.nextElement();
            //自定义标签都为UnifiedCall类型
            if("class freemarker.core.UnifiedCall".equals(obj.getClass().toString())){
            	if (obj.toString().contains(type)) {
                	//获取字段名
                	int sIndex = obj.toString().indexOf("controlName=\"");
                    int eIndex = obj.toString().lastIndexOf("\"/]");
                    String fieldName = obj.toString().substring(sIndex+13, eIndex);
                    fieldNames.add(fieldName);
            	}
            }
        }
		return fieldNames;
	}
	
	/** 根据模板获取查询字段
	 * @param ftlName 模板名
	 * @return searchFields 查询字段
	 *  */
	public JSONArray getSearchFields(String ftlName) {
		
		JSONArray returnArray = new JSONArray();
		
		//获取自定义表单模板内的自定义标签元素
		Template template = getTemplate(ftlName, FlowConstant.Flow_FormTemplate_Path, true);
		TemplateElement element = template.getRootTreeNode();
        for(Enumeration children = element.children(); children.hasMoreElements();){
            Object obj = children.nextElement();
            //自定义标签都为UnifiedCall类型
            if("class freemarker.core.UnifiedCall".equals(obj.getClass().toString())){
            	if (obj.toString().contains("[@searchControl")) {
            		String[] array = obj.toString().replace("[@searchControl ", "").replace("/]", "").split(" ");
            		JSONArray jsonArray = new JSONArray();
            		for (int i=0;i<array.length;i++) {
            			String[] fields = array[i].split("=");
            			JSONObject jsonObj = new JSONObject();
            			jsonObj.put(fields[0], fields[1]);
            			jsonArray.add(jsonObj);
            		}
            		returnArray.add(jsonArray);
            	}
            }
        }
		return returnArray;
        
	}
	
	
	/** 根据模板名获取模板文件
	 * @param templateName 模板名
	 * @param path 路径
	 * @param setTagSyntax 是否设置freemarker的标签默认使用方括号
	 * @return template 模板文件
	 * */
	public Template getTemplate(String templateName, String path, boolean setTagSyntax) {
		
		//初使化FreeMarker配置
		Configuration config = new Configuration();
		
		if (setTagSyntax) {
			//设置freemarker的标签默认使用方括号
			config.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		}
		
		Template template = null;
		
		try {
			//设置要解析的模板所在的目录,并加载模板文件
			config.setDirectoryForTemplateLoading(new File(CoreConstant.Context_Real_Path + path));
			
			//设置包装器,并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());
			
			//获取模板,并设置编码方式,这个编码必须要与页面中的编码格式一致,否则会出现乱码
			template = config.getTemplate(templateName, CoreConstant.ENCODING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return template;
	}
	
}