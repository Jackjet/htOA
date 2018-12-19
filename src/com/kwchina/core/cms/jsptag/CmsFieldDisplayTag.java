package com.kwchina.core.cms.jsptag;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforFieldManager;

public class CmsFieldDisplayTag extends TagSupport {
	private String name;
	private String fieldName;
	private int categoryId;
	private PageContext pageContext;
	private Tag parent;


	public void setName(String name) {
		this.name = name;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}

	private void init() {
		fieldName = null;
		categoryId = 0;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag tag) {
		this.parent = parent; 	
	}

	public Tag getParent() {
		return super.getParent();
	}

	public int doStartTag() throws JspException {
		//如果未传入相应参数,则不显示
		if(this.categoryId==0 || fieldName==null || fieldName.equals("")){
			return super.SKIP_BODY;
		}
		
		ServletContext context = pageContext.getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		
		/*//首先根据该categoryId,找到其根分类Id(即获取根分类),进而获取其设定是否显示相关字段信息
		InforCategoryManager inforCategoryManager = (InforCategoryManager)webContext.getBean("inforCategoryManager");
		List parents = inforCategoryManager.getParents(categoryId);
		if(! parents.isEmpty()){
			InforCategory category = (InforCategory)parents.get(0);
			categoryId = category.getCategoryId();
		}*/		
		
		InforFieldManager fieldManager = (InforFieldManager)webContext.getBean("inforFieldManagerImpl");
		//如果没有该InforField存在,则不显示
		InforField field = fieldManager.getFieldByName(fieldName, categoryId);
		if(field==null){
			return super.SKIP_BODY;
		}
		
		//根据该InforField的displayed属性确定是否显示
		boolean displayed = field.isDisplayed();
		if(!displayed){
			return super.SKIP_BODY;
		}else{
			return super.EVAL_BODY_INCLUDE;
		}	
	}
	
	public   int  doEndTag()  throws  JspException{
		return super.EVAL_PAGE;
	}
	
	public void release(){
		//super.release();
	    init();
	}
}

