package com.kwchina.core.jsptag;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kwchina.core.util.string.StringUtil;

public class HTMLCodecTag extends BodyTagSupport {

	public HTMLCodecTag() {
	}

	public int doStartTag() {
		return 2;
	}

	public int doEndTag() {
		return 6; 
	}

	public void setEscapeTag(boolean escapeTagValue) {
		setValue("escapeTag", new Boolean(escapeTagValue));
	}

	public int doAfterBody() {
		String bcString = getBodyContent().getString();
		if (bcString!=null && !bcString.equals("")){
			bcString = bcString.trim();
			
			boolean escapeTag = false;
			Boolean escapeTagValue = (Boolean) getValue("escapeTag");
			if (escapeTagValue != null)
				escapeTag = escapeTagValue.booleanValue();
			
			bcString = StringUtil.replace(bcString, "\n\r", "<br>");
			bcString = StringUtil.replace(bcString, "\r\n", "<br>");
			
			
			if (!escapeTag) {
				bcString = StringUtil.replace(bcString, "&", "&amp;");
				
				bcString = StringUtil.replace(bcString, "<", "&lt;");
				bcString = StringUtil.replace(bcString, ">", "&gt;");
			}
			/**
			else{
				bcString = StringUtil.replace(bcString, "&amp;", "&");				
			}*/
			
			
//			bcString = StringUtil.replace(bcString, "\n", "<br>");
//			bcString = StringUtil.replace(bcString, "\r", "<br>");
		}
		JspWriter out = getBodyContent().getEnclosingWriter();
		try {
			out.print(bcString);
			//out.flush();
		} catch (IOException e) {
			ServletContext sc = super.pageContext.getServletContext();
			sc.log("[EXCEPTION] while outputing content...", e);
		}
		return 0;
	}
}