package com.kwchina.oa.workflow.taglib;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * 图形化显示定义的流程
 * @author Administrator
 *
 */
public class FlowDefinitionChart  extends BodyTagSupport {
	public FlowDefinitionChart() {
	}

	public int doStartTag() {
		return 2;
	}

	public int doEndTag() {
		return 6; 
	}
	
	public void setFlowId(int flowId)  {
        setValue("flowId", flowId);
    }

	
	public int doAfterBody() {
		String printStr = "";
		
		//A->B->C及分叉流程的显示
		
		
		
		
		
		JspWriter out = getBodyContent().getEnclosingWriter();
		try {
			out.print(printStr);
			//out.flush();
		} catch (IOException e) {
			ServletContext sc = super.pageContext.getServletContext();
			sc.log("[EXCEPTION] while FlowDefinitionChart outputing content...", e);
		}
		return 0;
	}

}
