package com.kwchina.core.cms.jsptag;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforFieldManager;

public class CmsFieldNameTag extends TagSupport {
	private String fieldName;

	private int categoryId;

	public CmsFieldNameTag() {
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int doStartTag() {
		// return 2;
		// String printStr = "";
		// String contextPath = (String)getValue("contextPath");
		try {

			// 如果未传入相应参数,则不显示
			if (this.categoryId == 0 || fieldName == null
					|| fieldName.equals("")) {
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
			
			InforFieldManager fieldManager = (InforFieldManager) webContext.getBean("inforFieldManagerImpl");
			// 如果没有该InforField存在,则不显示
			InforField field = fieldManager.getFieldByName(fieldName,categoryId);
			if (field == null) {
				return super.SKIP_BODY;
			}

			// 根据该InforField显示起内容
			try {
				String displayTitle = field.getDisplayTitle();
				pageContext.getOut().print(displayTitle);

				return super.SKIP_BODY;
			} catch (IOException e) {
				ServletContext sc = super.pageContext.getServletContext();
				sc.log("[EXCEPTION] while CmsFieldNameTag outputing content...",e);
				return super.SKIP_BODY;
			}

			// return super.SKIP_BODY;
		} finally {
			return super.SKIP_BODY;
		}
	}

	public int doEndTag() {
		return super.EVAL_PAGE;
	}

	// public void setContextPath(String path) {
	// setValue("contextPath", path);
	// }

	// public void setBodyContent() {
	//
	// }

}
